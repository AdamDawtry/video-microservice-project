package microservice.site.trending.clients;

import java.util.Map;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;

@Client("/trending")
public interface TrendingTagsClient {
	@Get("/")
	public Map<String, Long> topTenTags();
}
