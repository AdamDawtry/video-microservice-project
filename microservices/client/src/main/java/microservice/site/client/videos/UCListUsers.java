package microservice.site.client.videos;

import jakarta.inject.Inject;
import microservice.site.client.domain.User;
import picocli.CommandLine.Command;

@Command(name="list-users", description="Lists all users")
public class UCListUsers implements Runnable {
	
	@Inject
	UserClient client;

	@Override
	public void run() {
		Iterable<User> users = client.list();
		for (User u : users) {
			System.out.printf("%s",u.getUsername());
			System.out.println();
		}
		System.out.println();
	}
}