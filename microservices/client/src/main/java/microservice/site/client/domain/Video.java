package microservice.site.client.domain;

import java.util.Set;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class Video{
	private long id;
	
	private User uploader;
	
	private String title;
	
	private Set<Tag> tags;
	
	private Set<User> views;
	
	private Set<User> likes;
	
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
	
	public String tagsAsString() {
		if (tags == null) {
			return "[]";
		}
		String out = "[";
		for (Tag t : tags) {
			out.concat(t.getTag());
		}
		out.concat("]");
		return out;
	}
	
	@Override
	public String toString() {
		return String.format("Video %d%n\nUploader: %s\nViews: %d\nLikes: %d\nDislikes: %d\nTags: %s", 
				id, uploader.getUsername(),views.size(),likes.size(),dislikes.size(),tagsAsString());
	}
}