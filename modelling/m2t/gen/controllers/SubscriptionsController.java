package // TODO;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.Delete;

@Controller("/subscriptions")
public class SubscriptionsController {
	// Inject resources
	
	
	@Get("/")
	public HttpResponse<Void> healthCheck() {
		//Generated stub
		return null;
	}
	
	@Put("/{username}/{tag}")
	public HttpResponse<String> subscribeToTag(String username, String tag) {
		//Generated stub
		//TODO: Method should produce event(s): user-subscribed
		return null;
	}
	
	@Delete("/{username}/{tag}")
	public HttpResponse<String> unsubscribeFromTag(String username, String tag) {
		//Generated stub
		//TODO: Method should produce event(s): user-unsubscribed
		return null;
	}
	
	@Get("/{username}/recommended")
	public Iterable<Video> topTenRecs(String username) {
		//Generated stub
		return null;
	}
}

