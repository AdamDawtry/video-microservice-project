package microservice.site.video;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import microservice.site.video.clients.UsersClient;
import microservice.site.video.domain.User;
import microservice.site.video.domain.Video;
import microservice.site.video.repositories.TagRepo;
import microservice.site.video.repositories.UserRepo;
import microservice.site.video.repositories.VideoRepo;

@MicronautTest(transactional=false, environments="no_streams")
public class UserControllersTest {
	@Inject
	UsersClient client;
	
	@Inject
	VideoRepo videoRepo;
	
	@Inject
	UserRepo userRepo;
	
	@Inject
	TagRepo tagRepo;
	
	@BeforeEach
	public void clean() {
		videoRepo.deleteAll();
		userRepo.deleteAll();
		tagRepo.deleteAll();
	}

	private String TEST_USER = "TestUser";
	private String TEST_VIDEO = "ABCVideo";
	
	@Test
	public void noUsers() {
		Iterable<User> iterUsers = client.list();
		assertFalse(iterUsers.iterator().hasNext(), "Should not have any users on startup");
	}
	
	@Test
	public void postUser() {
		// Check post response OK
		HttpResponse<String> response = client.postUser(TEST_USER);
		
		// Check DB has correct data
		Optional<User> u = userRepo.findById(TEST_USER);
		assertFalse(u.isEmpty(), "User was not saved to DB");
		assertEquals(TEST_USER,u.get().getUsername());
		assertEquals(1, iterableToList(userRepo.findAll()).size());
		
		assertEquals(HttpStatus.CREATED, response.getStatus(), String.format("Resource not created. Response from client: %s",response.body()));
		
	}

	@Test
	public void getUploads() {
		
		// Check null is returned if user doesn't exist
		assertEquals(null, client.getUsersUploads(TEST_USER));
		
		User u = new User();
		u.setUsername(TEST_USER);
		userRepo.save(u);
		
		// Check a new user has no uploads
		assertEquals(false, client.getUsersUploads(TEST_USER).iterator().hasNext());
	
		// Add a video uploaded by the user
		Video v = new Video();
		v.setTitle(TEST_VIDEO);
		v.setUploader(u);
		videoRepo.save(v);
		
		// Check the video has been mapped on the user's side
		assertTrue(iterableToList(client.getUsersUploads(TEST_USER)).contains(v));	
	}
	
	@Test
	public void watchVideo() {
		User u = new User();
		u.setUsername(TEST_USER);
		userRepo.save(u);
		
		Video v = new Video();
		v.setTitle(TEST_VIDEO);
		v.setUploader(u);
		videoRepo.save(v);
		
		// Video has 0 views when created
		assertEquals(0, videoRepo.findById(v.getId()).get().getViews().size());
		
		// Check watch response OK, watch added to video
		HttpResponse<String> response = client.watchVideo(v.getId(), TEST_USER);
		assertEquals(HttpStatus.OK, response.getStatus());
		assertEquals(1, videoRepo.findById(v.getId()).get().getLikes().size());
	
		// Check liking a 2nd time returns OK, and doesn't change views count
		response = client.watchVideo(v.getId(), TEST_USER);
		assertEquals(HttpStatus.OK, response.getStatus());
		assertEquals(1, videoRepo.findById(v.getId()).get().getLikes().size());
	}
	
	@Test
	public void likeVideo() {
		User u = new User();
		u.setUsername(TEST_USER);
		userRepo.save(u);

		Video v = new Video();
		v.setTitle(TEST_VIDEO);
		v.setUploader(u);
		videoRepo.save(v);
		
		// Check 0 likes when video created
		assertEquals(0, videoRepo.findById(v.getId()).get().getLikes().size());
		
		// Check response OK
		HttpResponse<String> response = client.likeVideo(v.getId(), TEST_USER);
		assertEquals(HttpStatus.OK, response.getStatus());
	
		// Check video like was added
		assertEquals(1, videoRepo.findById(v.getId()).get().getLikes().size());
		
		// Check liking a 2nd time returns OK, and doesn't change likes count
		response = client.likeVideo(v.getId(), TEST_USER);
		assertEquals(HttpStatus.OK, response.getStatus());
		assertEquals(1, videoRepo.findById(v.getId()).get().getLikes().size());
	}
	
	@Test
	public void dislikeVideo() {
		User u = new User();
		u.setUsername(TEST_USER);
		userRepo.save(u);
		
		// Add a video uploaded by the user
		Video v = new Video();
		v.setTitle(TEST_USER);
		v.setUploader(u);
		videoRepo.save(v);
		
		// Check 0 likes when video created
		assertEquals(0, videoRepo.findById(v.getId()).get().getDislikes().size());
		
		// Check response OK
		HttpResponse<String> response = client.dislikeVideo(v.getId(), TEST_USER);
		assertEquals(HttpStatus.OK, response.getStatus());
	
		// Check video like was added
		assertEquals(1, videoRepo.findById(v.getId()).get().getDislikes().size());
	
		// Check disliking a 2nd time returns OK, and doesn't change dislikes count
		response = client.dislikeVideo(v.getId(), TEST_USER);
		assertEquals(HttpStatus.OK, response.getStatus());
		assertEquals(1, videoRepo.findById(v.getId()).get().getDislikes().size());
	}

	@Test
	public void swapLikeDislike() {
		User u = new User();
		u.setUsername(TEST_USER);
		userRepo.save(u);
		
		// Add a video uploaded by the user
		Video v = new Video();
		v.setTitle(TEST_VIDEO);
		v.setUploader(u);
		videoRepo.save(v);
		
		// Add a like, then dislike
		client.likeVideo(v.getId(), TEST_USER);
		HttpResponse<String> response = client.dislikeVideo(v.getId(), TEST_USER);
		// Response OK, like removed and dislike added
		assertEquals(HttpStatus.OK, response.getStatus());
		assertEquals(0, videoRepo.findById(v.getId()).get().getLikes().size());
		assertEquals(1, videoRepo.findById(v.getId()).get().getDislikes().size());
		
		
	}
	
	private <T> List<T> iterableToList(Iterable<T> iterable) {
		List<T> l = new ArrayList<>();
		iterable.forEach(l::add);
		return l;
	}
}
