package // TODO;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.Delete;

//Generated client interface, double check the port is correct
@Client("${videos.url:`http://localhost:8080/videos`}") 
public interface VideosClient{
	
	@Get("/")
	public HttpResponse<Void> healthCheck();
	
	@Post("/")
	public HttpResponse<Void> postVideo(VideoCreationDTO videoInfo);
	
	@Get("/{videoId}/likes")
	public int getLikes(long videoId);
	
	@Get("/{videoId}/dislikes")
	public int getDislikes(long videoId);
	
	@Get("/{videoId}/tags")
	public Iterable<String> getTags(long videoId);
	
	@Get("/{videoId}/views")
	public int getViews(long videoId);
}

