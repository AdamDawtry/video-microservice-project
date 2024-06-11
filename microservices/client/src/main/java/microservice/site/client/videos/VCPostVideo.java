package microservice.site.client.videos;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import io.micronaut.http.HttpResponse;
import jakarta.inject.Inject;
import microservice.site.client.dto.VideoCreationDTO;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name="post-video", description="Post a video with a certain user as the uploader. User must have been created first.", mixinStandardHelpOptions = true)
public class VCPostVideo implements Runnable {
	
	@Inject
	VideoClient client;
	
	@Parameters(index="0")
	String title;
	
	@Parameters(index="1")
	String uploader;
	
	@Parameters(index="2..*")
	String[] tags;
	
	@Override
	public void run() {
		VideoCreationDTO video = new VideoCreationDTO();
		Set<String> a;
		if (tags == null) {
			a = Collections.emptySet();
		} else {
			a = new HashSet<>(Arrays.asList(tags));
		}
		
		video.setTitle(title);
		video.setUploader(uploader);
		video.setTags(a);
		HttpResponse<Void> response = client.postVideo(video);
		System.out.println("Server response: "+response.getStatus());
	}
}