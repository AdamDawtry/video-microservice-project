package microservice.site.trending.events;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class WindowedIdentifier {

	private String id;
	private Long startMillis, endMillis;

	public WindowedIdentifier() {
		// empty constructor for reflection
	}

	public WindowedIdentifier(String id, Long startMillis, Long endMillis) {
		this.id = id;
		this.startMillis = startMillis;
		this.endMillis = endMillis;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getStartMillis() {
		return startMillis;
	}

	public void setStartMillis(Long startMillis) {
		this.startMillis = startMillis;
	}

	public Long getEndMillis() {
		return endMillis;
	}

	public void setEndMillis(Long endMillis) {
		this.endMillis = endMillis;
	}

	@Override
	public String toString() {
		return "WindowedIdentifier [id=" + id + ", startMillis=" + startMillis + ", endMillis=" + endMillis + "]";
	}

}
