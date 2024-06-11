package microservice.site.trending.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.micronaut.serde.annotation.Serdeable;

@Entity
@Serdeable
public class User {
	//Using a generated Id would make it easier to allow username changes and such, but makes searching harder for users if many uploaders share a username
	@Id
	private String username;
	
	@JsonIgnore
	@OneToMany(targetEntity=Video.class, mappedBy="uploader", fetch=FetchType.EAGER)
	private Set<Video> uploadedVideos;
	
	@JsonIgnore
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="viewed_videos_views", 
		joinColumns=
			@JoinColumn(name="user_id",referencedColumnName="username"),
		inverseJoinColumns=
			@JoinColumn(name="video_id",referencedColumnName="id"))
	private Set<Video> viewedVideos;
	
	@JsonIgnore
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="liked_videos_likes", 
		joinColumns=
			@JoinColumn(name="user_id",referencedColumnName="username"),
		inverseJoinColumns=
			@JoinColumn(name="video_id",referencedColumnName="id"))
	private Set<Video> likedVideos;
	
	@JsonIgnore
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="disliked_videos_dislikes", 
		joinColumns=
			@JoinColumn(name="user_id",referencedColumnName="username"),
		inverseJoinColumns=
			@JoinColumn(name="video_id",referencedColumnName="id"))
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