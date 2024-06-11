package microservice.site.client.videos;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.client.annotation.Client;
import microservice.site.client.domain.User;
import microservice.site.client.domain.Video;

@Client("${videos.url:`http://localhost:8080/users`}")
public interface UserClient {

	@Get("/")
	public Iterable<User> list();
	
	@Post("/")
	public HttpResponse<String> postUser(@Body String username);
	
	@Get("/{username}/uploads")
	public Iterable<Video> getUsersUploads(@Body String username);
	
	@Put("/{username}/views/{id}")
	public HttpResponse<String> watchVideo(long id, String username);
	
	@Put("/{username}/likes/{id}")
	public HttpResponse<String> likeVideo(long id, String username);
	
	@Put("/{username}/dislikes/{id}")
	public HttpResponse<String> dislikeVideo(long id, String username);
}
