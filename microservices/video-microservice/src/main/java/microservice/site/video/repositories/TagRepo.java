package microservice.site.video.repositories;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import microservice.site.video.domain.Tag;

@Repository
public interface TagRepo extends CrudRepository<Tag, String> {
	@Override
	Optional<Tag> findById(@NotNull String id);
	//Optional<TagDTO> findOne(long id);
	//Optional<Tag> findByTag(String tag);
}