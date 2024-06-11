package microservice.site.video.repositories;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import microservice.site.video.domain.Video;

@Repository
public interface VideoRepo extends CrudRepository<Video, Long> {
	@Override
	Optional<Video> findById(@NotNull Long id);
	//Optional<VideoDTO> findOne(long id);
}