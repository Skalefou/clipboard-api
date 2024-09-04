package org.skalefou.clipboardapi.feature.repository;

import org.skalefou.clipboardapi.feature.model.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersRepository extends CrudRepository<Users, UUID> {
    public Optional<Users> findByMail(String mail);
}
