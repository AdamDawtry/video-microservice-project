package microservice.site.client;

import io.micronaut.configuration.picocli.PicocliRunner;
import microservice.site.client.subscriptions.SCGetSubscriptions;
import microservice.site.client.subscriptions.SCSubscribeToTag;
import microservice.site.client.subscriptions.SCUnsubscribeFromTag;
import microservice.site.client.trending.TCTopTenTags;
import microservice.site.client.videos.UCDislikeVideo;
import microservice.site.client.videos.UCGetUploads;
import microservice.site.client.videos.UCLikeVideo;
import microservice.site.client.videos.UCListUsers;
import microservice.site.client.videos.UCPostUser;
import microservice.site.client.videos.UCWatchVideo;
import microservice.site.client.videos.VCGetVideo;
import microservice.site.client.videos.VCListVideos;
import microservice.site.client.videos.VCPostVideo;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "client", description = "Interface for video microservice facilitating posting, liking and more.",
        mixinStandardHelpOptions = true, 
        subcommands = {VCPostVideo.class, VCListVideos.class, VCGetVideo.class, 
        				UCWatchVideo.class, UCLikeVideo.class, UCDislikeVideo.class, UCPostUser.class, UCListUsers.class, UCGetUploads.class,
        				TCTopTenTags.class,
        				SCGetSubscriptions.class, SCSubscribeToTag.class, SCUnsubscribeFromTag.class})
public class Client implements Runnable {

    @Option(names = {"-v", "--verbose"}, description = "...")
    boolean verbose;

    public static void main(String[] args) throws Exception {
        PicocliRunner.run(Client.class, args);
    }

    public void run() {
        // business logic here
        if (verbose) {
            System.out.println("Hi!");
        }
    }
}
