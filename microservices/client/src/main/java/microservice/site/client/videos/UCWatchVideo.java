package microservice.site.client.videos;

import io.micronaut.http.HttpResponse;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name="watch-video", description="Watch a video as a specific user")
public class UCWatchVideo implements Runnable {
	
	@Inject
	UserClient client;
	
	@Parameters(index="0")
	long id;
	
	@Parameters(index="1")
	String username;

	@Override
	public void run() {
		HttpResponse<String> response = client.watchVideo(id, username);
		System.out.println(response.getBody());
	}
}