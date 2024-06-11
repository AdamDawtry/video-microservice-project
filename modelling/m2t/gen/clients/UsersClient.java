package // TODO;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.Delete;

//Generated client interface, double check the port is correct
@Client("${users.url:`http://localhost:8080/users`}") 
public interface UsersClient{
	
	@Put("/{username}/likes/{videoId}")
	public HttpResponse<String> likeVideo(String username, long videoId);
	
	@Put("/{username}/likes/{videoId}")
	public HttpResponse<String> dislikeVideo(String username, long videoId);
	
	@Put("/{username}/views/{videoId}")
	public HttpResponse<String> watchVideo(String username, long videoId);
	
	@Get("/uploads")
	public Iterable<Video> getUsersUploads(String username);
	
	@Post("/")
	public HttpResponse<Void> postUser(String username);
}

