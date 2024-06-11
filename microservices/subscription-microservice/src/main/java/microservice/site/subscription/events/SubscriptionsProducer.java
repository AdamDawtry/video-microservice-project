package microservice.site.subscription.events;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface SubscriptionsProducer {

	String TOPIC_SUB = "user-subscribe";
	String TOPIC_UNSUB = "user-unsubscribe";
	
	@Topic(TOPIC_SUB)
	void subToTag(@KafkaKey String username, String tag);
	
	@Topic(TOPIC_UNSUB)
	void unsubFromTag(@KafkaKey String username, String tag);
}