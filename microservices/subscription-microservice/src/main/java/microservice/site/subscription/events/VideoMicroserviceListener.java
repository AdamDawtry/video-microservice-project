package microservice.site.subscription.events;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Inject;
import microservice.site.subscription.domain.Tag;
import microservice.site.subscription.domain.User;
import microservice.site.subscription.domain.Video;
import microservice.site.subscription.dto.VideoDTO;
import microservice.site.subscription.repositories.TagRepo;
import microservice.site.subscription.repositories.UserRepo;
import microservice.site.subscription.repositories.VideoRepo;

@KafkaListener(groupId = "subscriptions-db-updater")
public class VideoMicroserviceListener {
	@Inject
	VideoRepo videoRepo;
	
	@Inject
	TagRepo tagRepo;
	
	@Inject
	UserRepo userRepo;
	
	@Topic("user-create")
	public void saveUserLocally(@KafkaKey String username, String makeKafkaHappy) {
		if (userRepo.findById(username).isEmpty()) {
			User u = new User();
			u.setUsername(username);
			userRepo.save(u);
		} else {
			System.out.printf("User %s already exists in local DB",username);
		}
	}
	
	@Topic("video-post")
	public void saveVideoLocally(@KafkaKey Long videoId, VideoDTO vidInfo) {
		Video vid = new Video();
		// If video tags don't exist, create DB entries
		Set<Tag> vidTags = new HashSet<Tag>();
		for (String t : vidInfo.getTags()) {
			if (tagRepo.findById(t).isEmpty()) {
				Tag tag = new Tag();
				tag.setTag(t);
				tagRepo.save(tag);
				vidTags.add(tag);
			}
		}
		vid.setTitle(vidInfo.getTitle());
		vid.setUploader(vidInfo.getUploader());
		vid.setViews(vidInfo.getViews());
		vid.setLikeTotal(vidInfo.getLikeTotal());
		vid.setTags(vidTags);
		videoRepo.save(vid);
		// Set video fields and save to DB
	}
	
	@Topic("video-watch")
	public void updateViews(@KafkaKey Long videoId, String username) {
		Optional<Video> video = videoRepo.findById(videoId);
		if (!video.isEmpty()) {
			Video v = video.get();
			v.setViews(v.getViews()+1);
			videoRepo.update(v);
		}
	}
	
	@Topic("video-like")
	public void like(@KafkaKey Long videoId, String username) {
		Optional<Video> video = videoRepo.findById(videoId);
		if (!video.isEmpty()) {
			Video v = video.get();
			v.setViews(v.getLikeTotal()+1);
			videoRepo.update(v);
		}
	}
	
	@Topic("video-dislike")
	public void dislike(@KafkaKey Long videoId, String username) {
		Optional<Video> video = videoRepo.findById(videoId);
		if (!video.isEmpty()) {
			Video v = video.get();
			v.setViews(v.getLikeTotal()-1);
			videoRepo.update(v);
		}
	}
	
	@Topic("tag-create")
	public void createTag(@KafkaKey String tag, String makeKafkaHappy) {
		if (tagRepo.findById(tag).isEmpty()) {
			Tag t = new Tag();
			t.setTag(tag);
			tagRepo.save(t);
		} else {
			System.out.printf("Tag %s already exists in local DB",tag);
		}
	}
}
