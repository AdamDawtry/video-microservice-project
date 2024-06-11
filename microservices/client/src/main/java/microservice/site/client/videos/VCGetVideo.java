package microservice.site.client.videos;

import jakarta.inject.Inject;
import microservice.site.client.dto.VideoDTO;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name="get-video", description="Get a video by its numerical ID")
public class VCGetVideo implements Runnable {
	
	@Inject
	VideoClient client;

	@Parameters(index="0")
	long id;
	
	@Override
	public void run() {
		VideoDTO v = client.getVideo(id);
		System.out.println("title: "+v.getTitle());
		System.out.println("uploader: "+v.getUploader());
		System.out.println("views: "+v.getViews());
		System.out.println("viewer score (likes minus dislikes): "+v.getLikeTotal());
		System.out.println("tags: "+v.getTags());
		System.out.println();
	}
}