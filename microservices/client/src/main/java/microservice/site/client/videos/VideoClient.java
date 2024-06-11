package microservice.site.client.videos;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import microservice.site.client.dto.VideoCreationDTO;
import microservice.site.client.dto.VideoDTO;

@Client("${videos.url:`http://localhost:8080/videos`}")
public interface VideoClient {
	@Get("/")
	public HttpResponse<Void> healthCheck();
	
	@Get("/{id}")
	public VideoDTO getVideo(long id);
	
	@Get("/list")
	public Iterable<VideoDTO> list();
	
	@Post("/")
	public HttpResponse<Void> postVideo(@Body VideoCreationDTO videoInfo);
}