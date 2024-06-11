package microservice.site.trending.events;

import java.time.Duration;
import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.TimeWindows;

import io.micronaut.configuration.kafka.serde.SerdeRegistry;
import io.micronaut.configuration.kafka.streams.ConfiguredStreamBuilder;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Factory
public class TrendingHashtagStream {
	public static final String TOPIC_LIKED_BY_HOUR = "liked-by-hour";
	
	@Inject
	private SerdeRegistry serdeRegistry;
	
	@Singleton 
	public KStream<WindowedIdentifier, Long> likedByHour(ConfiguredStreamBuilder builder) {
		Properties props = builder.getConfiguration();
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, "videos-metrics");
		props.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE);
		
		KStream<String, Long> tagsLikedStream = builder.stream("tags-like", Consumed.with(Serdes.String(), Serdes.Long()));
		
		KStream<WindowedIdentifier, Long> stream = tagsLikedStream
				.groupByKey()
				.windowedBy(TimeWindows.of(Duration.ofHours(1)).advanceBy(Duration.ofMinutes(20)))
				.count(Materialized.as("tag-liked-by-hour"))
				.toStream()
				.selectKey((k, v) -> new WindowedIdentifier(k.key(), k.window().start(), k.window().end()));
		
		stream.to(TOPIC_LIKED_BY_HOUR,
				Produced.with(serdeRegistry.getSerde(WindowedIdentifier.class), Serdes.Long()));
		
		return stream;
	}
}