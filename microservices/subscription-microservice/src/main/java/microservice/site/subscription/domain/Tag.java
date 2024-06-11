package microservice.site.subscription.domain;

import io.micronaut.serde.annotation.Serdeable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="tags")
@Serdeable
public class Tag {
	@Id
	private String tag;

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
}
