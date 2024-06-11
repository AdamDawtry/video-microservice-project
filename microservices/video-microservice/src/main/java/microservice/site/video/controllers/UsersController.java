package microservice.site.video.controllers;

import java.net.URI;
import java.util.Collections;
import java.util.Optional;

import javax.transaction.Transactional;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import jakarta.inject.Inject;
import microservice.site.video.domain.Tag;
import microservice.site.video.domain.User;
import microservice.site.video.domain.Video;
import microservice.site.video.events.VideosProducer;
import microservice.site.video.repositories.UserRepo;
import microservice.site.video.repositories.VideoRepo;

@Controller("/users")
public class UsersController {
	
	@Inject
	UserRepo userRepo;
	
	@Inject
	VideoRepo videoRepo;
	
	@Inject
	VideosProducer producer;
	
	@Get("/")
	public Iterable<User> list() {
		return userRepo.findAll();
	}
	
	@Post("/")
	public HttpResponse<String> postUser(@Body String username) {
		// Check username is alphanumeric - if not give a badRequest status response
		String regex = "^[a-zA-Z0-9]*$";
		boolean result = username.matches(regex);
		if (!result) {
			return HttpResponse.notModified();
		}
		
		// Check username isn't already in use
		if (!userRepo.findById(username).isEmpty()) {
			return HttpResponse.notModified();
		}
		
		// Save user to DB
		User user = new User();
		user.setUsername(username);
		userRepo.save(user);
		producer.createUser(username, "");
		return HttpResponse.created(URI.create("/users/" + user.getUsername()).toString());
	}
	
	@Get("/{username}/uploads")
	public Iterable<Video> getUsersUploads(@Body String username) {
		Optional<User> oUser = userRepo.findById(username);
		if (oUser.isEmpty()) {
			return Collections.emptySet();
		}
		return oUser.get().getUploadedVideos();
	}

	@Transactional
	@Put("/{username}/views/{id}")
	public HttpResponse<String> watchVideo(long id, String username) {
		Optional<User> user = userRepo.findById(username);
		Optional<Video> video = videoRepo.findById(id);
		if (video.isEmpty() || user.isEmpty()) {
			return HttpResponse.notFound(String.format("User or Video ID does not exist"));
		}
		User u = user.get();
		Video v = video.get();

		if (u.getViewedVideos().add(v)) {
			userRepo.update(u);
			producer.watchVideo(v.getId(), username);
			return HttpResponse.ok(String.format("Watched video %d as user %s", id, username));
		}
		
		return HttpResponse.ok(String.format("Rewatched video %d as user %s (view not counted again)", id, username));
	}
	
	@Transactional
	@Put("/{username}/likes/{id}")
	public HttpResponse<String> likeVideo(long id, String username) {
		Optional<User> user = userRepo.findById(username);
		Optional<Video> video = videoRepo.findById(id);
		if (video.isEmpty() || user.isEmpty()) {
			return HttpResponse.notFound(String.format("User or Video ID does not exist"));
		}
		User u = user.get();
		Video v = video.get();
		if (u.getLikedVideos().add(v)) {
			if (u.getDislikedVideos().remove(v)) {
				producer.likeVideo(v.getId(), username); // Have to produce a 2nd event so subscription microservice count is accurate
			}
			userRepo.update(u);
			producer.likeVideo(v.getId(), username);
			for (Tag t : v.getTags()) {
				producer.likeTag(t.getTag(), v.getId());
			}
			return HttpResponse.ok(String.format("Liked video %d as user %s", id, username));
		} 
		
		return HttpResponse.ok(String.format("Already liked video %d as user %s", id, username));
	}

	@Transactional
	@Put("/{username}/dislikes/{id}")
	public HttpResponse<String> dislikeVideo(long id, String username) {
		Optional<User> user = userRepo.findById(username);
		Optional<Video> video = videoRepo.findById(id);
		if (video.isEmpty() || user.isEmpty()) {
			return HttpResponse.notFound(String.format("User or Video ID does not exist"));
		}
		User u = user.get();
		Video v = video.get();
		if (u.getDislikedVideos().add(v)) {
			if (u.getLikedVideos().remove(v)) {
				producer.dislikeVideo(v.getId(), username); // Have to produce a 2nd event so subscription microservice count is accurate
			}
			producer.dislikeVideo(v.getId(), username);
			userRepo.update(u);
			return HttpResponse.ok(String.format("Disliked video %d as user %s", id, username));
		} 
		
		return HttpResponse.ok(String.format("Already disliked video %d as user %s", id, username));
	}
}