package microservice.site.client.videos;

import io.micronaut.http.HttpResponse;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name="create-user", description="Create a user")
public class UCPostUser implements Runnable {
	
	@Inject
	UserClient client;
	
	@Parameters(index="0")
	String username;

	@Override
	public void run() {
		HttpResponse<String> response = client.postUser(username);
		System.out.println(response.getBody());
	}
}