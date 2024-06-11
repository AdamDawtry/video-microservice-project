package microservice.site.subscription.clients;

import java.util.Map;
import java.util.Set;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.client.annotation.Client;
import microservice.site.subscription.dto.VideoDTO;

@Client("/subscriptions")
public interface SubscriptionsClient {
	@Get("/{username}")
	HttpResponse<Map <String,Set<String>>> getSubscriptions(String username);
	
	@Put("/{username}/{tagName}")
	HttpResponse<String> subscribeToTag(String username, String tagName);
	
	@Delete("/{username}/{tagName}")
	HttpResponse<String> unsubscribeFromTag(String username, String tagName);
	
	@Get("/{username}/recommended")
	HttpResponse<Map<Iterable<VideoDTO>, String>> topTenRecs(String username);
}
