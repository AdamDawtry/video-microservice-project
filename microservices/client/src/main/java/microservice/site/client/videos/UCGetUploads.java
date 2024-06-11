package microservice.site.client.videos;

import jakarta.inject.Inject;
import microservice.site.client.domain.Video;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name="get-uploads", description="Gets a users uploads")
public class UCGetUploads implements Runnable {
	
	@Inject
	UserClient client;

	@Parameters(index="0")
	String username;
	
	@Override
	public void run() {
		for (Video v : client.getUsersUploads(username)) {
			System.out.println(v.toString());
		}
	}
}