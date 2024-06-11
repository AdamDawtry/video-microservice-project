package microservice.site.subscription.repositories;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import microservice.site.subscription.domain.User;

@Repository
public interface UserRepo extends CrudRepository<User, String>{
	@Override
	Optional<User> findById(@NotNull String id);
}
