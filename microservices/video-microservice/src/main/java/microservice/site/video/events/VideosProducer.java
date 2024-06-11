package microservice.site.video.events;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;
import microservice.site.video.dto.VideoDTO;

@KafkaClient
public interface VideosProducer {

	String TOPIC_USER_CREATE = "user-create";
	String TOPIC_TAG_CREATE = "tag-create";
	String TOPIC_POST = "video-post";
	String TOPIC_LIKE = "video-like";
	String TOPIC_LIKE_TAGS = "tags-like";
	String TOPIC_DISLIKE = "video-dislike";
	String TOPIC_WATCH = "video-watch";
	
	@Topic(TOPIC_USER_CREATE)
	void createUser(@KafkaKey String username, String makeKafkaHappy);
	
	@Topic(TOPIC_TAG_CREATE)
	void createTag(@KafkaKey String tag, String makeKafkaHappy);
	
	@Topic(TOPIC_POST)
	void postVideo(@KafkaKey Long id, VideoDTO videoInfo);
	
	@Topic(TOPIC_LIKE)
	void likeVideo(@KafkaKey Long id, String username);
	
	@Topic(TOPIC_LIKE_TAGS)
	void likeTag(@KafkaKey String tag, Long id);
	
	@Topic(TOPIC_DISLIKE)
	void dislikeVideo(@KafkaKey Long id, String username);
	
	@Topic(TOPIC_WATCH)
	void watchVideo(@KafkaKey Long id, String username);
}