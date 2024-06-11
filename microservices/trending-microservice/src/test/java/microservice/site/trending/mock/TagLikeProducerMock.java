package microservice.site.trending.mock;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface TagLikeProducerMock {
	@Topic("tags-like")
	void likeTag(@KafkaKey String tag, Long id);
}
