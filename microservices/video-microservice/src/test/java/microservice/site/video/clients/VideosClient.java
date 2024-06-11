package microservice.site.video.clients;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import microservice.site.video.dto.VideoCreationDTO;
import microservice.site.video.dto.VideoDTO;

@Client("/videos")
public interface VideosClient {
	@Get("/")
	HttpResponse<Void> healthCheck();
	
	@Get("/{id}")
	VideoDTO getVideo(long id);
	
	@Get("/list")
	Iterable<VideoDTO> list();
	
	@Post("/")
	HttpResponse<Void> postVideo(@Body VideoCreationDTO videoInfo);
}
