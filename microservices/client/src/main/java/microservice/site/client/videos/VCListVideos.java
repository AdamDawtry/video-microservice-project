package microservice.site.client.videos;

import jakarta.inject.Inject;
import microservice.site.client.dto.VideoDTO;
import picocli.CommandLine.Command;

@Command(name="list-all-videos", description="List all videos (Debug)")
public class VCListVideos implements Runnable {
	
	@Inject
	VideoClient client;

	@Override
	public void run() {
		for (VideoDTO v : client.list()) {
			System.out.println("title: "+v.getTitle());
			System.out.println("uploader: "+v.getUploader());
			System.out.println("views: "+v.getViews());
			System.out.println("viewer score (likes minus dislikes): "+v.getLikeTotal());
			System.out.println("tags: "+v.getTags());
			System.out.println();
		}
	}
}