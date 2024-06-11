package microservice.site.client.domain;

import java.util.Set;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class User {
	//Using a generated Id would make it easier to allow username changes and such, but makes searching harder for users if many uploaders share a username
	private String username;
	
	private Set<Video> uploadedVideos;
	
	private Set<Video> viewedVideos;
	
	private Set<Video> likedVideos;
	
	private Set<Video> dislikedVideos;
	
	public String getUsername() {
		return username;
	}
	
	public Set<Video> getUploadedVideos() {
		return uploadedVideos;
	}
	
	public Set<Video> getViewedVideos() {
		return viewedVideos;
	}
	
	public Set<Video> getLikedVideos() {
		return likedVideos;
	}
	
	public Set<Video> getDislikedVideos() {
		return dislikedVideos;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setUploadedVideos(Set<Video> uploadedVideos) {
		this.uploadedVideos = uploadedVideos;
	}
	
	public void setViewedVideos(Set<Video> viewedVideos) {
		this.viewedVideos = viewedVideos;
	}
	
	public void setLikedVideos(Set<Video> likedVideos) {
		this.likedVideos = likedVideos;
	}
	
	public void setDislikedVideos(Set<Video> dislikedVideos) {
		this.dislikedVideos = dislikedVideos;
	}
}