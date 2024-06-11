package microservice.site.video.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.micronaut.serde.annotation.Serdeable;

@Entity
@Serdeable
public class Video{
	@Id
	@GeneratedValue
	private long id;
	
	@ManyToOne
	@JoinColumn(name="username")
	private User uploader;
	
	@Column(nullable=false)
	private String title;
	
	@JsonIgnore
	@ManyToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(name="tags_tagged_videos", 
		joinColumns=
			@JoinColumn(name="video_id",referencedColumnName="id"),
		inverseJoinColumns=
			@JoinColumn(name="tag_id",referencedColumnName="tag"))
	private Set<Tag> tags;
	
	@JsonIgnore
	@ManyToMany(mappedBy="viewedVideos",fetch=FetchType.EAGER)
	private Set<User> views;
	
	@JsonIgnore
	@ManyToMany(mappedBy="likedVideos",fetch=FetchType.EAGER)
	private Set<User> likes;
	
	@JsonIgnore
	@ManyToMany(mappedBy="dislikedVideos",fetch=FetchType.EAGER)
	private Set<User> dislikes;
	
	public long getId() {
		return id;
	}
	
	public User getUploader() {
		return uploader;
	}
	
	public String getTitle() {
		return title;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public Set<User> getViews(){
		return views;
	}
	
	public Set<User> getLikes() {
		return likes;
	}
	
	public Set<User> getDislikes() {
		return dislikes;		
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public void setUploader(User uploader) {
		this.uploader = uploader;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}
	
	public void setViews(Set<User> views) {
		this.views = views;
	}
	
	public void setLikes(Set<User> likes) {
		this.likes = likes;
	}
	
	public void setDislikes(Set<User> dislikes) {
		this.dislikes = dislikes;
	}
	
	@Override
	public String toString() {
		return "Video "+id+": '"+title+"', Uploaded by:"+uploader+"\n  Views: "+views+" Likes: "+likes+" Dislikes: "+dislikes+"\n  Tags: " + tags;
	}
}