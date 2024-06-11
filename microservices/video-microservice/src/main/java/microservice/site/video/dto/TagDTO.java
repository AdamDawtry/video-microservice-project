package microservice.site.video.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class TagDTO {
	private String tag;
	
	
	public String getTag() {
		return tag;
	}
	
	public void setTag(String tag) {
		this.tag = tag;
	}
}