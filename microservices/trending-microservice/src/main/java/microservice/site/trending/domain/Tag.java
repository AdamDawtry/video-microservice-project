package microservice.site.trending.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.micronaut.serde.annotation.Serdeable;

@Entity
@Serdeable
public class Tag {
	@Id
	private String tag;
	
	@JsonIgnore
	@ManyToMany(mappedBy="tags", cascade=CascadeType.PERSIST, fetch=FetchType.EAGER)
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