package microservice.site.client.trending;

import java.util.Map;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import microservice.site.client.dto.VideoCreationDTO;
import microservice.site.client.dto.VideoDTO;

@Client("${videos.url:`http://localhost:8081/trending`}")
public interface TrendingClient {
	@Get("/")
	public Map<String, Long> topTenTags();
}