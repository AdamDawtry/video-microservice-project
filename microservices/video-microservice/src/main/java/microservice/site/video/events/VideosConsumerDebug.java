package microservice.site.video.events;

import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import microservice.site.video.dto.VideoDTO;

@KafkaListener(groupId = "videos-debug")
public class VideosConsumerDebug {
	
	/*@Topic(VideosStreams.TOPIC_LIKED_BY_HOUR)
	public void vidsLiked(@KafkaKey WindowedIdentifier window, Long count) {
		System.out.printf("New value for key %s: %d%n", window, count);
	}*/
	
	@Topic(VideosProducer.TOPIC_POST)
	public void postVideo(@KafkaKey Long id, VideoDTO video) {
		System.out.printf("Video posted: %d%n", id);
	}
	
	@Topic(VideosProducer.TOPIC_WATCH)
	public void watchVideo(@KafkaKey Long id) {
		System.out.printf("Video watched: %d%n", id);
	}
	
	@Topic(VideosProducer.TOPIC_LIKE)
	public void likeVideo(@KafkaKey Long id) {
		System.out.printf("Video liked: %d%n", id);
	}
	
	@Topic(VideosProducer.TOPIC_LIKE_TAGS)
	public void likeTag(@KafkaKey String tag, Long id) {
		System.out.printf("Tag liked: %s", tag);
	}
	
	@Topic(VideosProducer.TOPIC_DISLIKE)
	public void dislikeVideo(@KafkaKey Long id) {
		System.out.printf("Video disliked: %d%n", id);
	}
}