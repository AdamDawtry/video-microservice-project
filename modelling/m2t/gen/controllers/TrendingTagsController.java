package // TODO;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.Delete;

@Controller("/trending")
public class TrendingTagsController {
	// Inject resources
	
	
	@Get("/")
	public HttpResponse<Void> healthCheck() {
		//Generated stub
		return null;
	}
	
	@Get("/")
	public Map<String, Long> topTenTags() {
		//Generated stub
		return null;
	}
}

