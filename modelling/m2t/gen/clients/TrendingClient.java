package // TODO;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.Delete;

//Generated client interface, double check the port is correct
@Client("${trending.url:`http://localhost:8080/trending`}") 
public interface TrendingClient{
	
	@Get("/")
	public HttpResponse<Void> healthCheck();
	
	@Get("/")
	public Map<String, Long> topTenTags();
}

