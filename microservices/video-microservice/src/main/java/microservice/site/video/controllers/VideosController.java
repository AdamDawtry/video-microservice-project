package microservice.site.video.controllers;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;
import microservice.site.video.domain.Tag;
import microservice.site.video.domain.User;
import microservice.site.video.domain.Video;
import microservice.site.video.dto.VideoCreationDTO;
import microservice.site.video.dto.VideoDTO;
import microservice.site.video.events.VideosProducer;
import microservice.site.video.repositories.TagRepo;
import microservice.site.video.repositories.UserRepo;
import microservice.site.video.repositories.VideoRepo;

@Controller("/videos")
public class VideosController {
	@Inject
	VideoRepo videoRepo;
	
	@Inject
	UserRepo userRepo;
	
	@Inject
	TagRepo tagRepo;
	
	@Inject
	VideosProducer producer;
	
	@Get("/")
	public HttpResponse<Void> healthCheck() {
		return HttpResponse.ok();
	}
	
	@Transactional
	@Get("/{id}")
	public VideoDTO getVideo(long id) {
		return videoRepo.findById(id).map(v -> buildDTO(v)).orElse(null);
	}
	
	@Get("/list")
	public Iterable<VideoDTO> list() {

		List<VideoDTO> dtos = new ArrayList<>();
		for (Video v : videoRepo.findAll()) {
			dtos.add(buildDTO(v));
		}
		return dtos;
	}
	
	@Post("/")
	@Transactional
	public HttpResponse<Void> postVideo(@Body VideoCreationDTO videoInfo) {
		Optional<User> user = userRepo.findById(videoInfo.getUploader());
		if (user.isEmpty()) {
			return HttpResponse.notFound();
		}
		User uploader = user.get();
		
		// Need to do some checking for tags
		Set<Tag> tags = new HashSet<Tag>();
		for (String t : videoInfo.getTags()) {
			// Check tag is alphanumeric only - if not, return badRequest status response
			String regex = "^[a-zA-Z0-9]*$";
			boolean result = t.matches(regex);
			if (!result) {
				return HttpResponse.badRequest();
			}
			Optional<Tag> tag = tagRepo.findById(t.toLowerCase());

			// If tag is already in the repo, do nothing and add it to our temporary Set
			if (!tag.isEmpty()) {
				tags.add(tag.get());
			// Otherwise add it to the DB (more convenient to do this as needs than through a separate controller)
			} else {
				System.out.println("Tag was not in DB");
				Tag dbTag = new Tag();
				dbTag.setTag(t.toLowerCase());
				tagRepo.save(dbTag);
				producer.createTag(t, "");
				tags.add(dbTag);
			}
		}
		
		Video video = new Video();
		video.setTitle(videoInfo.getTitle());
		video.setUploader(uploader);
		video.setTags(tags);
		
		System.out.println("Saving video to repo");
		videoRepo.save(video);
		System.out.println("SAVED VIDEO");
		producer.postVideo(video.getId(), buildEmptyDTO(video));
		System.out.println("Produced Video Post event");
		
		// update user to have uploaded this video
		if (uploader.getUploadedVideos().add(video)) {
			userRepo.update(uploader);
		}
		
		return HttpResponse.created(URI.create("/videos/" + video.getId()));
	}
	
	/*@Transactional
	@Get("/{id}")
	public VideoDTO getVideo(long id) {
		return videoRepo.findOne(id).orElse(null);
	}
	
	@Transactional
	@Get("/{id}/tags")
	public List<String> getTags(long id) {
		Optional<Video> vid = videoRepo.findById(id);
		if (vid.isEmpty()) {
			return null;
		}

		List<String> tags = new ArrayList<>();
		for (Tag t : vid.get().getTags()) {
			tags.add(t.getTag());
		}
		return tags;
	}
	
	@Transactional
	@Get("/{id}/views")
	public int getViews(long id) {
		Optional<Video> video = videoRepo.findById(id);
		if (video.isEmpty()) {
			return -1;
		}
		return video.get().getViews().size();
	}
	
	@Transactional
	@Get("/{id}/likes")
	public int getLikes(long id) {
		Optional<Video> video = videoRepo.findById(id);
		if (video.isEmpty()) {
			return -1;
		}
		return video.get().getLikes().size();
	}
	
	@Transactional
	@Get("/{id}/dislikes")
	public int getDislikes(long id) {
		Optional<Video> video = videoRepo.findById(id);
		if (video.isEmpty()) {
			return -1;
		}
		return video.get().getDislikes().size();
	}*/
	
	// Building DTO manually as micronaut's ability to automatically build is scuffed by @JsonIgnore and @ManyToMany entity relations

	private VideoDTO buildDTO(Video video) {
		VideoDTO dto = new VideoDTO();
		dto.setTitle(video.getTitle());
		dto.setUploader(video.getUploader().getUsername());
		dto.setViews(video.getViews().size());
		dto.setLikeTotal(video.getLikes().size() - video.getDislikes().size());
		List<String> tags = new ArrayList<>();
		Iterator<Tag> iterTag = video.getTags().iterator();
		while (iterTag.hasNext()) {
			tags.add(iterTag.next().getTag());
		}
		dto.setTags(tags);
		return dto;
	}
	
	// This method exists so I can build a DTO during the transaction of the postVideo method. If it wasn't transactional
	// then buildDTO might work fine after comitting a new video to the DB, but removing the @Transactional tag breaks postVideo
	private VideoDTO buildEmptyDTO(Video video) {
		System.out.println("making DTO");
		VideoDTO dto = new VideoDTO();
		dto.setTitle(video.getTitle());
		System.out.println("set tilte");
		dto.setUploader(video.getUploader().getUsername());
		System.out.println("set uploader");
		dto.setViews(0);
		System.out.println("set views");
		dto.setLikeTotal(0);
		System.out.println("set like/dislikes");
		List<String> tags = new ArrayList<>();
		Iterator<Tag> iterTag = video.getTags().iterator();
		while (iterTag.hasNext()) {
			tags.add(iterTag.next().getTag());
		}
		dto.setTags(tags);
		System.out.println("set tags, finished DTO");
		return dto;
	}

}