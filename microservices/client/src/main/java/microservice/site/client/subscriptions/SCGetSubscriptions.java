package microservice.site.client.subscriptions;

import java.util.Map;
import java.util.Set;

import io.micronaut.http.HttpResponse;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name="get-subscriptions", description="Get a user's tag subscriptions")
public class SCGetSubscriptions implements Runnable {
	
	@Inject
	SubscriptionsClient client;

	@Parameters(index="0")
	String username;
	
	@Override
	public void run() {
		HttpResponse<Map<String,Set<String>>> response = client.getSubscriptions(username);
		Set<String> tags = response.body().get(username);
		if (tags.isEmpty()) {
			System.out.printf("%s is not subscribed to any tags.",username);
		}
		tags.forEach(t -> System.out.println(t));
	}
}