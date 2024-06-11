package microservice.site.subscription;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.micronaut.http.HttpStatus;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import microservice.site.subscription.clients.SubscriptionsClient;
import microservice.site.subscription.domain.Tag;
import microservice.site.subscription.domain.User;
import microservice.site.subscription.repositories.TagRepo;
import microservice.site.subscription.repositories.UserRepo;
import microservice.site.subscription.repositories.VideoRepo;

@MicronautTest(transactional=false, environments="no_streams")
public class SubscriptionsControllersTest {
	@Inject
	SubscriptionsClient client;
	
	@Inject
	TagRepo tagRepo;
	
	@Inject 
	UserRepo userRepo;
	
	@Inject
	VideoRepo videoRepo;
	
	//static Map<String, Object> config;
	//static KafkaEmbedded kafkaEmbedded;
	
	@BeforeEach
	public void clean() {
		videoRepo.deleteAll();
		userRepo.deleteAll();
		tagRepo.deleteAll();
		
		/*config = Collections.
                unmodifiableMap(new HashMap<String, Object>() {
                    {
                        put(AbstractKafkaConfiguration.EMBEDDED, true); // Why doesnt EMBEDDED exist? out of date Kafka version?
                        put(AbstractKafkaConfiguration.EMBEDDED_TOPICS, "test_topic");
                    }
                });*/
	}
	
	String TEST_USERNAME = "TestUser";
	String TEST_TAG = "TestTag";
	
	@Test
	public void subscribeToTag() {
		User u = new User();
		u.setUsername(TEST_USERNAME);
		Tag t = new Tag();
		t.setTag(TEST_TAG);
		userRepo.save(u);
		tagRepo.save(t);
		
		Awaitility.await().until(() -> userRepo.findAll().iterator().hasNext() && tagRepo.findAll().iterator().hasNext());
		
		// No subscribed tags when a new User is added
		Set<String> response = client.getSubscriptions(TEST_USERNAME).body().get(TEST_USERNAME);
		assertTrue(response.size() == 0);

		// 1 subbed tag after subscribing
		assertEquals(HttpStatus.OK, client.subscribeToTag(TEST_USERNAME, TEST_TAG).getStatus());
		assertTrue(client.getSubscriptions(TEST_USERNAME).body().get(TEST_USERNAME).size() == 1);

		// 1 subbed tag after subscribing to the same tag twice
		assertEquals(HttpStatus.OK, client.subscribeToTag(TEST_USERNAME, TEST_TAG).getStatus());
		assertTrue(client.getSubscriptions(TEST_USERNAME).body().get(TEST_USERNAME).size() == 1);
	}
	@Test
	public void unsubscribeFromTag() {
		// Test currently doesn't work due to InstanceAlreadyExistsException occurring when kafka tries to instantiate a second video-microservice-listener for some reason
		User u = new User();
		u.setUsername(TEST_USERNAME);
		Tag t = new Tag();
		t.setTag(TEST_TAG);
		userRepo.save(u);
		tagRepo.save(t);
		
		Awaitility.await().until(() -> userRepo.findAll().iterator().hasNext() && tagRepo.findAll().iterator().hasNext());
		
		// No subscribed tags when a new User is added
		assertTrue(client.getSubscriptions(TEST_USERNAME).body().get(TEST_USERNAME).size() == 0);

		// 1 subbed tag after subscribing
		assertEquals(HttpStatus.OK, client.subscribeToTag(TEST_USERNAME, TEST_TAG).getStatus());
		assertTrue(client.getSubscriptions(TEST_USERNAME).body().get(TEST_USERNAME).size() == 1);
		
		// 0 subbed tags after unsubscribing
		assertEquals(HttpStatus.OK, client.unsubscribeFromTag(TEST_USERNAME, TEST_TAG));
		assertTrue(client.getSubscriptions(TEST_USERNAME).body().get(TEST_USERNAME).size() == 0);

		// 0 subbed tags after unsubscribing from the same tag twice
		assertEquals(HttpStatus.OK, client.unsubscribeFromTag(TEST_USERNAME, TEST_TAG));
		assertTrue(client.getSubscriptions(TEST_USERNAME).body().get(TEST_USERNAME).size() == 0);
	}
	
}
