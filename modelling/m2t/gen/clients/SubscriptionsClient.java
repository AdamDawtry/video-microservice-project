package // TODO;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.Delete;

//Generated client interface, double check the port is correct
@Client("${subscriptions.url:`http://localhost:8080/subscriptions`}") 
public interface SubscriptionsClient{
	
	@Get("/")
	public HttpResponse<Void> healthCheck();
	
	@Put("/{username}/{tag}")
	public HttpResponse<String> subscribeToTag(String username, String tag);
	
	@Delete("/{username}/{tag}")
	public HttpResponse<String> unsubscribeFromTag(String username, String tag);
	
	@Get("/{username}/recommended")
	public Iterable<Video> topTenRecs(String username);
}

