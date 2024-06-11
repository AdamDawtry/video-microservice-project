package // TODO;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.Delete;

@Controller("/users")
public class UsersController {
	// Inject resources
	
	
	@Put("/{username}/likes/{videoId}")
	public HttpResponse<String> likeVideo(String username, long videoId) {
		//Generated stub
		//TODO: Method should produce event(s): tags-like, video-like
		return null;
	}
	
	@Put("/{username}/likes/{videoId}")
	public HttpResponse<String> dislikeVideo(String username, long videoId) {
		//Generated stub
		//TODO: Method should produce event(s): video-dislike
		return null;
	}
	
	@Put("/{username}/views/{videoId}")
	public HttpResponse<String> watchVideo(String username, long videoId) {
		//Generated stub
		//TODO: Method should produce event(s): video-watch
		return null;
	}
	
	@Get("/uploads")
	public Iterable<Video> getUsersUploads(String username) {
		//Generated stub
		return null;
	}
	
	@Post("/")
	public HttpResponse<Void> postUser(String username) {
		//Generated stub
		return null;
	}
}

