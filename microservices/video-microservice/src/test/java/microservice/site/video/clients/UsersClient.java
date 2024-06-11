package microservice.site.video.clients;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.client.annotation.Client;
import microservice.site.video.domain.User;
import microservice.site.video.domain.Video;

@Client("/users")
public interface UsersClient {
	@Get("/")
	Iterable<User> list();
	
	@Post("/")
	HttpResponse<String> postUser(@Body String username);
	
	@Get("/{username}/uploads")
	Iterable<Video> getUsersUploads(@Body String username);
	
	@Put("{username}/views/{id}")
	HttpResponse<String> watchVideo(long id, String username);
	
	@Put("{username}/likes/{id}")
	HttpResponse<String> likeVideo(long id, String username);
	
	@Put("{username}/dislikes/{id}")
	HttpResponse<String> dislikeVideo(long id, String username);
}
