package microservice.site.trending.events;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyWindowStore;

import io.micronaut.configuration.kafka.streams.InteractiveQueryService;
import jakarta.inject.Singleton;

@Singleton
public class TagQueryService {

    private InteractiveQueryService interactiveQueryService;

    public TagQueryService(InteractiveQueryService interactiveQueryService) {
        this.interactiveQueryService = interactiveQueryService;
    }
    
    /**
     * Get the top X tags from the last hour. Result is a linkedHashMap, so element order is consistent
     *
     * @param stateStore the name of the state store (should be "liked-by-hour")
     * @param top        the number of top tags to return
     * @return 			 an ordered map of {@code top} tags in descending order by likes in current rolling window
     */
    public Optional<Map<String, Long>> getTopTags(String stateStore, Integer top) {
    	// Grab the state store
    	Optional<ReadOnlyWindowStore<String, Long>> queryableStore = interactiveQueryService.getQueryableStore(
    			stateStore, QueryableStoreTypes.windowStore());
    	// Get the top tags, or return null if window has no data
    	return Optional.ofNullable(queryableStore.map(kvReadOnlyKeyValueStore -> 
    		iterTagsStore(kvReadOnlyKeyValueStore, top)).orElse(null));
    }
    
    /**
     * Helper function to map onto the Optional queryable store.
     *
     * @param keyValueStore the key-value queryable store
     * @param num        	the number of records stored in the final list
     * @return 			 	list of {@code num} tags in descending order by likes in current rolling window, length <= num
     */
    private Map<String, Long> iterTagsStore(ReadOnlyWindowStore<String, Long> keyValueStore, Integer num) {
    	// Current timestamp to select correct window to take aggregate from
    	long time = System.currentTimeMillis();
    	
    	// Convert the iterator from key-value-store.all() to a Map containing all entries
    	KeyValueIterator<Windowed<String>, Long> allTags = keyValueStore.all();
    	
    	// Chances are I could've used this iterable to do all my filtering, but I dare
    	// not touch the streams lest I face retribution (and time crunch hell)
    	Iterable<KeyValue<Windowed<String>, Long>> allTagIter = () -> allTags;
    	
    	// Collect all the iterable items into a map
    	Map<Windowed<String>, Long> allTagsMap = StreamSupport.
    			stream(allTagIter.spliterator(), false).
    			collect(Collectors.toMap(k -> k.key, v -> v.value));
    	
    	// So that I can use java streams to filter
    	// First order of business, discard windows that are closed
    	Map<Windowed<String>,Long> filteredWindows =
    		    allTagsMap.entrySet().stream()
    		       .filter(window -> window.getKey().window().end() > time && time > window.getKey().window().start())
    		       .collect(Collectors.toMap(
    		    			Map.Entry::getKey, Map.Entry::getValue, (e1,e2) -> e1, LinkedHashMap::new));

    	// Second, among current open windows, we only care about the most recent one
    	Map<String, Long> mostRecentWindowTagAggregates = new HashMap<String,Long>();
    	long maxTime = Long.MIN_VALUE;
    	Iterator<Entry<Windowed<String>,Long>> a = filteredWindows.entrySet().iterator();

    	while (a.hasNext()) {
    		Entry<Windowed<String>,Long> b = a.next();
    		long curWinTime = b.getKey().window().start();
    	    if (curWinTime == maxTime) {
    	        mostRecentWindowTagAggregates.put(b.getKey().key(), b.getValue());
    	    } else if (maxTime < curWinTime) {
    	        mostRecentWindowTagAggregates.clear();
    	        mostRecentWindowTagAggregates.put(b.getKey().key(), b.getValue());

    	        maxTime = curWinTime;
    	    }
    	}
    	
    	// Once we have the mapping of tags:likes from the oldest running window, sort the
    	// linked hashMap by likes (descending, so reverseOrder) using streams
    	Map<String,Long> topTags = mostRecentWindowTagAggregates.entrySet().stream()
    			.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
    			.limit(num)
    			.collect(Collectors.toMap(
    		    		Map.Entry::getKey, Map.Entry::getValue, (e1,e2) -> e1, LinkedHashMap::new));
    	
		// Close key-value iterator
    	allTags.close();
		return topTags;
    }
}