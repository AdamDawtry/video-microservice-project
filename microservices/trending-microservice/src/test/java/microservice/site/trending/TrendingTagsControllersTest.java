package microservice.site.trending;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KafkaStreams.State;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import jakarta.inject.Inject;
import microservice.site.trending.clients.TrendingTagsClient;
import microservice.site.trending.events.TrendingHashtagStream;
import microservice.site.trending.events.WindowedIdentifier;
import microservice.site.trending.mock.TagLikeProducerMock;

@Property(name = "spec.name", value="TrendingTagsControllersTest")
@TestInstance(Lifecycle.PER_CLASS)
@MicronautTest
public class TrendingTagsControllersTest {
	@Inject
	TrendingTagsClient client;
	
	@Inject
	KafkaStreams kStreams;
	
	@Inject
	TagLikeProducerMock producer;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TrendingTagsControllersTest.class);
	private static final Map<WindowedIdentifier, Long> events = new HashMap<>();
	
	@BeforeEach
	public void setUp() {
		events.clear();
		Awaitility.await().until(() -> kStreams.state().equals(State.RUNNING));
		LOGGER.info("Kafka Streams is RUNNING");
	}
	
	@AfterEach
	public void clean() {
		kStreams.close();
	}
	
	
	@Test
	public void topTenTags() {
		// topTenTags shouldn't show any tags if the stream has received no events
		assertTrue(client.topTenTags().entrySet().size() == 0);
		
		LOGGER.info("About to send event");
		producer.likeTag("testTag", 1l);
		LOGGER.info("Sent event");

		// Check the kafka record produces an update record in the event stream
		Awaitility.await()
			.atMost(Duration.ofSeconds(30))
			.until(() -> !events.isEmpty());
		
		// After eventStream receives a liked tag event, topTenTags should give back a map with 1 entry
		assertTrue(client.topTenTags().entrySet().size() == 1);
	}
	
	@Requires(property = "spec.name", value = "MicronautStreamsTest")
	@KafkaListener(groupId = "micronaut-streams-test")
	static class StreamsListener {

		@Topic(TrendingHashtagStream.TOPIC_LIKED_BY_HOUR)
		public void tagLikedMetric(@KafkaKey WindowedIdentifier window, Long videoId) {
			LOGGER.info("Received event: {}", window);
			events.put(window, videoId);
		}
	}
	
}
