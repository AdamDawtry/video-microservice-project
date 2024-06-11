package microservice.site.trending.controllers;

import java.util.HashMap;
import java.util.Map;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;
import microservice.site.trending.events.TagQueryService;

@Controller("/trending")
public class TrendingTagsController {
	@Inject
	TagQueryService tagQueryService;
	
	@Get("/")
	public Map<String, Long> topTenTags() {
		Map<String, Long> topTags = tagQueryService.getTopTags("tag-liked-by-hour", 10).get();
		if (topTags == null) {
			return new HashMap<String, Long>();
		}
		return topTags;
	}
}
