package microservice.site.client.trending;

import java.util.Map;

import jakarta.inject.Inject;
import microservice.site.client.dto.VideoDTO;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name="get-trending-tags", description="Gets the top 10 hashtags trending by likes")
public class TCTopTenTags implements Runnable {
	
	@Inject
	TrendingClient client;
	
	@Override
	public void run() {
		Map<String, Long> topTags = client.topTenTags();
		System.out.println("Top 10 tags (with likes in the past hour):");
		topTags.forEach((k, v) -> System.out.printf("%s (%d)%n",k,v));
	}
}