package microservice.site.trending.events;

import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaListener(groupId = "videos-debug")
public class StreamConsumerDebug {
	
	/*@Topic("liked-by-hour")
	public void vidsLiked(@KafkaKey WindowedIdentifier window, Long count) {
		System.out.printf("New value for key %s (%s): %d%n", window, window.getId(), count);
	}*/
	
	@Topic("liked-by-hour")
	public void tagsLiked(@KafkaKey WindowedIdentifier window, long count) {
		System.out.printf("New value for window %s (%s): %d%n", window, window.getId(), count);
	}
	
}