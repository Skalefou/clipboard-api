package org.skalefou.clipboardapi.feature.repository;

import org.skalefou.clipboardapi.feature.model.Roles;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RolesRepository extends CrudRepository<Roles, UUID> {
    @Query(value = "SELECT * FROM roles WHERE name = :name", nativeQuery = true)
    Optional<Roles> findByName(@Param("name") String name);
}
