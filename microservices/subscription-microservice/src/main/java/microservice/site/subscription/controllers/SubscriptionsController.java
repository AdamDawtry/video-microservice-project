package microservice.site.subscription.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Put;
import jakarta.inject.Inject;
import microservice.site.subscription.domain.Tag;
import microservice.site.subscription.domain.User;
import microservice.site.subscription.domain.Video;
import microservice.site.subscription.dto.VideoDTO;
import microservice.site.subscription.events.SubscriptionsProducer;
import microservice.site.subscription.repositories.TagRepo;
import microservice.site.subscription.repositories.UserRepo;
import microservice.site.subscription.repositories.VideoRepo;

@Controller("/subscriptions")
public class SubscriptionsController {
	// Inject resources
	@Inject
	SubscriptionsProducer subscriptionProducer;
	
	@Inject
	UserRepo userRepo;
	
	@Inject
	TagRepo tagRepo;
	
	@Inject
	VideoRepo videoRepo;
	
	@Get("/")
	public HttpResponse<Void> healthCheck() {
		return HttpResponse.ok();
	}
	
	@Get("/{username}")
	public HttpResponse<Map <String,Set<String>>> getSubscriptions(String username) {
		Optional<User> user = userRepo.findById(username);
		Map<String,Set<String>> response = new HashMap<String,Set<String>>();
		if (user.isEmpty()) {
			response.put("User does not exist",Collections.emptySet());
			return HttpResponse.notFound(response);
		}
		Set<Tag> tags = user.get().getSubscribedTags();
		Set<String> tagNames = new HashSet<>();
		tags.forEach(t -> tagNames.add(t.getTag()));
		response.put(username,tagNames);
		String allTagsStr = user.get().subbedTagsAsStrings().toString();
		System.out.printf("User %s subscribed to %s tags",username,allTagsStr);
		return HttpResponse.ok(response);
	}
	
	@Put("/{username}/{tagName}")
	@Transactional
	public HttpResponse<String> subscribeToTag(String username, String tagName) {
		Optional<User> user = userRepo.findById(username);
		Optional<Tag> tag = tagRepo.findById(tagName);
		
		if (tag.isEmpty()) {
			return HttpResponse.notFound(String.format("Tag %s does not exist",tagName));
		} else if (user.isEmpty()) {
			return HttpResponse.notFound(String.format("User %s does not exist", username));
		}
		
		User u = user.get();
		if (!u.getSubscribedTags().add(tag.get())) {
			return HttpResponse.ok(String.format("User %s is already subscribed to tag %s",username, tagName));
		}
		userRepo.update(u);
		subscriptionProducer.subToTag(username, tagName);

		return HttpResponse.ok(String.format("Subscribed user %s to tag %s",username, tagName));
	}
	
	@Delete("/{username}/{tagName}")
	@Transactional
	public HttpResponse<String> unsubscribeFromTag(String username, String tagName) {
		Optional<User> user = userRepo.findById(username);
		Optional<Tag> tag = tagRepo.findById(tagName);
		
		if (tag.isEmpty()) {
			return HttpResponse.notFound(String.format("Tag %s does not exist",tagName));
		} else if (user.isEmpty()) {
			return HttpResponse.notFound(String.format("User %s does not exist", username));
		}
		
		User u = user.get();
		System.out.printf("Size is %d",u.getSubscribedTags().size());
		if (!u.getSubscribedTags().remove(tag.get())) {
			System.out.println("User was not subbed to tag");
			return HttpResponse.ok(String.format("Cannot unsubscribe user %s from tag %s as they are not subscribed to it",username, tagName));
		}
		userRepo.update(u);
		System.out.printf("Size is %d",u.getSubscribedTags().size());
		subscriptionProducer.unsubFromTag(username, tagName);
		System.out.println("Unsubbed user from tag");
		return HttpResponse.ok(String.format("Unsubscribed user %s from tag %s", username, tagName));
	}
	
	// Repo query doesn't work for some reason. Complains about query returning multiple values when that's the whole point
	@Get("/{username}/recommended")
	public HttpResponse<Map<Iterable<VideoDTO>, String>> topTenRecs(String username) {
		Optional<User> user = userRepo.findById(username);
		Map<Iterable<VideoDTO>, String> response = new HashMap<>();
		// Check user exists
		if (user.isEmpty()) {
			response.put(Collections.emptyList(), String.format("User %s does not exist", username));
			return HttpResponse.notFound(response);
		}
		
		// Not bothering with recs if user hasn't subbed to tags
		if (user.get().getSubscribedTags().size() == 0) {
			response.put(Collections.emptyList(), String.format("No recommendations for %s. Try subscribing to some tags.",username));
			return HttpResponse.ok(null);
		}

		Iterator<Tag> subbedTags = user.get().getSubscribedTags().iterator();
		List<Video> vidsList = new ArrayList<>();
		List<VideoDTO> recs = new ArrayList<>();
		
		// Collect all videos with tags user has subbed to
		while (subbedTags.hasNext()) {
			vidsList.addAll(videoRepo.findByTag(subbedTags.next()));
		}
		
		// Sort by likes total
		Collections.sort(vidsList, (v1,v2) -> v1.getLikeTotal() - v2.getLikeTotal());
		int upperBound = 10;
		if (vidsList.size() < upperBound) {
			upperBound = vidsList.size();
		}
		List<Video> topVids = vidsList.subList(0, upperBound);
		for (Video v : topVids) {
			recs.add(buildDTO(v));
		}
		response.put(recs, username);
		return HttpResponse.ok(response);
	}
	
	private VideoDTO buildDTO(Video video) {
		VideoDTO vidDTO = new VideoDTO();
		vidDTO.setTitle(video.getTitle());
		vidDTO.setUploader(video.getUploader());
		vidDTO.setLikeTotal(video.getLikeTotal());
		vidDTO.setViews(video.getViews());
		vidDTO.setTags(video.tagsAsStrings());
		return vidDTO;
	}
}

