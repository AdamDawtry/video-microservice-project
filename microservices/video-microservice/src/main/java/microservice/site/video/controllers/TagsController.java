package microservice.site.video.controllers;

import java.net.URI;
import java.util.Optional;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;
import microservice.site.video.domain.Tag;
import microservice.site.video.domain.Video;
import microservice.site.video.repositories.TagRepo;
import microservice.site.video.repositories.UserRepo;
import microservice.site.video.repositories.VideoRepo;

@Controller("/tags")
public class TagsController {
	@Inject
	UserRepo userRepo;
	
	@Inject
	VideoRepo videoRepo;
	
	@Inject
	TagRepo tagRepo;
	
	@Get("/")
	public Iterable<Tag> list() {
		return tagRepo.findAll();
	}
	
	@Get("/{tagName}/videos")
	public Iterable<Video> getVideos(String tagName) {
		Optional<Tag> otag = tagRepo.findById(tagName);
		if (otag.isEmpty()) {
			return null;
		}
		return otag.get().getTaggedVideos();
	}
	
	@Post("/")
	public HttpResponse<Void> postTag(@Body String tagName) {
		// Check tag is alphanumeric - if not give a badRequest status response
		String regex = "^[a-zA-Z0-9]*$";
		boolean result = tagName.matches(regex);
		if (!result) {
			return HttpResponse.badRequest();
		}
		
		// Check tag isn't already in use
		if (!userRepo.findById(tagName).isEmpty()) {
			return HttpResponse.notAllowed();
		}
		
		// Save user to DB
		Tag tag = new Tag();
		tag.setTag(tagName);
		tagRepo.save(tag);
		return HttpResponse.created(URI.create("/tags/" + tag.getTag()));
	}
}