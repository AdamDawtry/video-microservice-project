package microservice.site.client.domain;

import java.util.Set;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class Tag {
	private String tag;
	
	private Set<Video> taggedVideos;
	
	public String getTag() {
		return tag;
	}
	
	public Set<Video> getTaggedVideos() {
		return taggedVideos;
	}
	
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	public void setTaggedVideos(Set<Video> taggedVideos) {
		this.taggedVideos = taggedVideos;
	}
}