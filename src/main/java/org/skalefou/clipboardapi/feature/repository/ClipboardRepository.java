package org.skalefou.clipboardapi.feature.repository;

import jakarta.transaction.Transactional;
import org.skalefou.clipboardapi.feature.model.Clipboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ClipboardRepository extends JpaRepository<Clipboard, UUID> {

    @Query(value = "SELECT * FROM clipboard WHERE access = :access", nativeQuery = true)
    Clipboard getClipboardByAccess(@Param("access") String access);

    @Query(value = "INSERT INTO clipboard (id, date_creation, expiration_time, last_update, access, id_user) " +
            "VALUES (:id, CURRENT_TIMESTAMP, :expiration_time, CURRENT_TIMESTAMP, :access, :id_user) " +
            "RETURNING *", nativeQuery = true)
    Clipboard createClipboard(@Param("id") UUID id,
                              @Param("expiration_time") LocalDateTime expirationTime,
                              @Param("access") String access,
                              @Param("id_user") UUID idUser);

    @Query(value = "SELECT clipboard.access FROM clipboard", nativeQuery = true)
    List<String> findAllAccess();

    @Query(value = "UPDATE clipboard SET content = :content, last_update = CURRENT_TIMESTAMP " +
            "WHERE access = :access " +
            "RETURNING *", nativeQuery = true)
    Clipboard updateClipboard(@Param("content") String content,
                              @Param("access") String access);

}
