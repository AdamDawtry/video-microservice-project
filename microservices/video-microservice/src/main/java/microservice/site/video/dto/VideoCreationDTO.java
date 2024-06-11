package microservice.site.video.dto;

import java.util.Set;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class VideoCreationDTO{
	private String title;
	private String uploader;
	private Set<String> tags;
	
	public String getTitle() {
		return title;
	}
	
	public String getUploader() {
		return uploader;
	}
	
	public Set<String> getTags() {
		return tags;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setUploader(String uploader) {
		this.uploader = uploader;
	}
	
	public void setTags(Set<String> tags) {
		this.tags = tags;
	}
}