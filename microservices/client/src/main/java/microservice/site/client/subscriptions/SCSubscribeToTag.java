package microservice.site.client.subscriptions;

import io.micronaut.http.HttpResponse;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name="subscribe-to-tag", description="Subscribe a user to a tag")
public class SCSubscribeToTag implements Runnable {
	
	@Inject
	SubscriptionsClient client;

	@Parameters(index="0")
	String username;
	
	@Parameters(index="1")
	String tag;
	
	@Override
	public void run() {
		HttpResponse<String> response = client.subscribeToTag(username, tag);
		System.out.println(response.body());
	}
}