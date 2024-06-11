package microservice.site.subscription.repositories;

import java.util.Collection;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import microservice.site.subscription.domain.Video;
import microservice.site.subscription.domain.Tag;

@Repository
public interface VideoRepo extends CrudRepository<Video, Long>{
	@Override
	Optional<Video> findById(@NotNull Long id);
	
	@Query("from Video v join v.tags t where t.tag=:tagName")
	Collection<Video> findByTag(@NotNull Tag tagName);
}
