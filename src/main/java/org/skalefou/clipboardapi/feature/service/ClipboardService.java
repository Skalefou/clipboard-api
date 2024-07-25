package org.skalefou.clipboardapi.feature.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.skalefou.clipboardapi.feature.model.Clipboard;
import org.skalefou.clipboardapi.feature.repository.ClipboardRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Service
public class ClipboardService {

    private final ClipboardRepository clipboardRepository;
    private final Random random = new Random();


    public Clipboard getClipboardByAccess(String access) {
        return clipboardRepository.getClipboardByAccess(access);
    }

    public Clipboard createClipboard(Clipboard clipboard) {
        Set<String> allAccess = new HashSet<>(clipboardRepository.findAllAccess());
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime expirationDateTest = clipboard.getExpirationTime();

        if ((expirationDateTest != null &&
                (expirationDateTest.isBefore(today) ||
                        expirationDateTest.isEqual(today) ||
                        expirationDateTest.isAfter(today.plusYears(1)))) ||
                allAccess.size() > 999998) {
            return null;
        }

        String randomAccess;
        do {
            randomAccess = String.format("%06d", random.nextInt(999999) + 1);
        } while (allAccess.contains(randomAccess));

        UUID userId = (clipboard.getUserId() != null) ? clipboard.getUserId() : null;
        UUID id = UUID.randomUUID();
        int ligneAffected = clipboardRepository.createClipboard(id, clipboard.getExpirationTime(), randomAccess, userId);
        return ligneAffected > 0 ? clipboardRepository.getClipboardById(id) : null;
    }

    public Clipboard updateClipboard(Clipboard clipboard) {
        return clipboardRepository.updateClipboard(clipboard.getContent(), clipboard.getAccess());
    }

    public boolean deleteClipboard(String access) {
        return clipboardRepository.deleteClipboardByAccess(access) > 0;
    }

}
