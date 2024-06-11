package // TODO;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.Delete;

@Controller("/videos")
public class VideosController {
	// Inject resources
	
	
	@Get("/")
	public HttpResponse<Void> healthCheck() {
		//Generated stub
		return null;
	}
	
	@Post("/")
	public HttpResponse<Void> postVideo(VideoCreationDTO videoInfo) {
		//Generated stub
		//TODO: Method should produce event(s): video-post
		return null;
	}
	
	@Get("/{videoId}/likes")
	public int getLikes(long videoId) {
		//Generated stub
		return null;
	}
	
	@Get("/{videoId}/dislikes")
	public int getDislikes(long videoId) {
		//Generated stub
		return null;
	}
	
	@Get("/{videoId}/tags")
	public Iterable<String> getTags(long videoId) {
		//Generated stub
		return null;
	}
	
	@Get("/{videoId}/views")
	public int getViews(long videoId) {
		//Generated stub
		return null;
	}
}

