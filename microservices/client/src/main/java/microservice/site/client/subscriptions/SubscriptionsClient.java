package microservice.site.client.subscriptions;

import java.util.Map;
import java.util.Set;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.client.annotation.Client;
import microservice.site.client.domain.Tag;
import microservice.site.client.dto.VideoDTO;

@Client("${videos.url:`http://localhost:8082/subscriptions`}")
public interface SubscriptionsClient {
	@Get("/")
	public HttpResponse<Void> healthCheck();
	
	@Get("/{username}")
	public HttpResponse<Map<String,Set<String>>> getSubscriptions(String username);
	
	@Put("/{username}/{tagName}")
	public HttpResponse<String> subscribeToTag(String username, String tagName);
	
	@Delete("/{username}/{tagName}")
	public HttpResponse<String> unsubscribeFromTag(String username, String tagName);
	
	@Get("/{username}/recommended")
	public HttpResponse<Map<Iterable<VideoDTO>, String>> topTenRecs(String username);
}