package microservice.site.subscription.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.micronaut.serde.annotation.Serdeable;

@Entity
@Table(name="videos")
@Serdeable
public class Video {
	@Id
	@GeneratedValue
	private long id;
	
	@Column(nullable=false)
	private String title;
	
	@Column(nullable=false)
	private String uploader;
	
	@Column(nullable=false)
	private int views;
	
	@Column(nullable=false)
	private int likeTotal;
	
	@JsonIgnore
	@ManyToMany
	@JoinTable(name="subsMStags_tagged_videos", 
	joinColumns=
		@JoinColumn(name="video_id",referencedColumnName="id"),
	inverseJoinColumns=
		@JoinColumn(name="tag_id",referencedColumnName="tag"))
	private Set<Tag> tags;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUploader() {
		return uploader;
	}
	public void setUploader(String uploader) {
		this.uploader = uploader;
	}
	public int getViews() {
		return views;
	}
	public void setViews(int views) {
		this.views = views;
	}
	public int getLikeTotal() {
		return likeTotal;
	}
	public void setLikeTotal(int likeTotal) {
		this.likeTotal = likeTotal;
	}
	public Set<Tag> getTags() {
		return tags;
	}
	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	public List<String> tagsAsStrings() {
		Iterator<Tag> iterTag = tags.iterator();
		List<String> vidTags = new ArrayList<String>();
		while (iterTag.hasNext()) {
			vidTags.add(iterTag.next().getTag());
		}
		return vidTags;
	}
}
