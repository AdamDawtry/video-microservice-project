package microservice.site.subscription.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.micronaut.serde.annotation.Serdeable;

@Entity
@Table(name="users")
@Serdeable
public class User {
	@Id
	private String username;
	
	@JsonIgnore
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="subscribed_tags_subscriptions", 
	joinColumns=
		@JoinColumn(name="user_id",referencedColumnName="username"),
	inverseJoinColumns=
		@JoinColumn(name="tag_id",referencedColumnName="tag"))
	private Set<Tag> subscribedTags;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Set<Tag> getSubscribedTags() {
		return subscribedTags;
	}
	public void setSubscribedTags(Set<Tag> subscribedTags) {
		this.subscribedTags = subscribedTags;
	}
	
	public List<String> subbedTagsAsStrings() {
		Iterator<Tag> iterTag = subscribedTags.iterator();
		List<String> tags = new ArrayList<String>();
		while (iterTag.hasNext()) {
			tags.add(iterTag.next().getTag());
		}
		return tags;
	}
}
