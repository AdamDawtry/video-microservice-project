package microservice.site.video;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import microservice.site.video.clients.VideosClient;
import microservice.site.video.domain.Tag;
import microservice.site.video.domain.User;
import microservice.site.video.dto.VideoCreationDTO;
import microservice.site.video.dto.VideoDTO;
import microservice.site.video.events.VideosProducer;
import microservice.site.video.repositories.TagRepo;
import microservice.site.video.repositories.UserRepo;
import microservice.site.video.repositories.VideoRepo;

@MicronautTest(transactional=false, environments="no_streams")
public class VideoControllersTest {
	@Inject
	VideosClient client;
	
	@Inject
	VideoRepo videoRepo;
	
	@Inject
	UserRepo userRepo;
	
	@Inject
	TagRepo tagRepo;
	
	private String TEST_USER = "TestUser";
	private String TEST_VIDEO = "ABCVideo";
	
	@BeforeEach
	public void clean() {
		videoRepo.deleteAll();
		userRepo.deleteAll();
		tagRepo.deleteAll();
	}

	/*private final Map<Long, VideoDTO> postedVideos = new HashMap<>();

	@MockBean(VideosProducer.class)
	VideosProducer testProducer() {
		return (key, value) -> { postedVideos.put(key,  value); };
	}
	
	@Test
	public void noVideos() {
		Iterable<VideoDTO> iterVids = client.list();
		assertFalse(iterVids.iterator().hasNext(), "Should not have any videos on startup");
	}*/
	
	@Test
	public void getNonExistantVideo() {
		assertEquals(null, client.getVideo(99));
	}
	
	@Test
	public void postVideoUserDoesntExist() {
		// Create a video DTO
		VideoCreationDTO videoInfo = new VideoCreationDTO();
		videoInfo.setTitle(TEST_VIDEO);
		videoInfo.setUploader(TEST_USER);
		videoInfo.setTags(new HashSet<String>());
		
		// Check post method responds with METHOD_NOT_ALLOWED
		HttpResponse<Void> response = client.postVideo(videoInfo);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
		
		// Check DB doesn't have any erroneous artefacts
		List<VideoDTO> vids = iterableToList(client.list());
		assertEquals(0, vids.size());
	}
	
	@Test
	public void postVideoNoTags() {
		// Create video uploader
		User u = new User();
		u.setUsername(TEST_USER);
		userRepo.save(u);
		
		// Create a video DTO with no tags
		VideoCreationDTO videoInfo = new VideoCreationDTO();
		videoInfo.setTitle(TEST_VIDEO);
		videoInfo.setUploader(TEST_USER);
		videoInfo.setTags(new HashSet<String>());
		
		// Check post method responds with CREATED
		HttpResponse<Void> response = client.postVideo(videoInfo);
		assertEquals(HttpStatus.CREATED, response.getStatus());
		
		// Check video is in the DB and has correct info
		List<VideoDTO> vids = iterableToList(client.list());
		assertEquals(1, vids.size());
		assertEquals(TEST_VIDEO, vids.get(0).getTitle());
		assertEquals(TEST_USER, vids.get(0).getUploader());
		assertEquals(0, vids.get(0).getTags().size());
		
		// Check nothing has been added to the tags repository
		assertTrue(tagRepo.findAll().iterator().hasNext()==false);
	}
	
	@Test
	public void postVideoWithTags() {
		// Create video uploader
		User u = new User();
		u.setUsername(TEST_USER);
		userRepo.save(u);
		
		// Create a video with no tags
		VideoCreationDTO videoInfo = new VideoCreationDTO();
		Set<String> tags = new HashSet<String>();
		tags.add("Comedy");
		tags.add("Gameplay");
		videoInfo.setTitle(TEST_VIDEO);
		videoInfo.setUploader(TEST_USER);
		videoInfo.setTags(tags);
		
		// Check post method responds with CREATED
		HttpResponse<Void> response = client.postVideo(videoInfo);
		assertEquals(HttpStatus.CREATED, response.getStatus());
		
		// Check video is in the DB and has correct info
		List<VideoDTO> vids = iterableToList(client.list());
		assertEquals(1, vids.size());
		assertEquals(TEST_VIDEO, vids.get(0).getTitle());
		assertEquals(TEST_USER, vids.get(0).getUploader());
		assertTrue(vids.get(0).getTags().contains("Comedy".toLowerCase()));
		assertTrue(vids.get(0).getTags().contains("Gameplay".toLowerCase()));
		
		// Check all tags were created and added to repo
		Set<String> dbTags = new HashSet<String>();
		for (Tag t : tagRepo.findAll()) {dbTags.add(t.getTag());}
		assertTrue(tags.equals(dbTags));
	}

	private <T> List<T> iterableToList(Iterable<T> iterable) {
		List<T> l = new ArrayList<>();
		iterable.forEach(l::add);
		return l;
	}
}
