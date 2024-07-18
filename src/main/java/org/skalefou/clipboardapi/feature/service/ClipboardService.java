package org.skalefou.clipboardapi.feature.service;

import lombok.RequiredArgsConstructor;
import org.skalefou.clipboardapi.feature.model.Clipboard;
import org.skalefou.clipboardapi.feature.repository.ClipboardRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ClipboardService {

    private final ClipboardRepository clipboardRepository;
    private final Random random = new Random();


    public Clipboard getClipboard(UUID id) {
        return clipboardRepository.findClipboardById(id);
    }

    public Clipboard createClipboard(Clipboard clipboard) {
        Set<String> allAccess = new HashSet<>(clipboardRepository.findAllAccess());
        LocalDate today = LocalDate.now();
        LocalDate expirationDateTest = clipboard.getExpirationTime().toLocalDate();

        if (expirationDateTest == null) {
            clipboard.setExpirationTime(today.plusWeeks(2).atStartOfDay());
            expirationDateTest = clipboard.getExpirationTime().toLocalDate();
        }

        if (expirationDateTest.isBefore(today) ||
                expirationDateTest.isEqual(today) ||
                expirationDateTest.isAfter(today.plusYears(1)) ||
                allAccess.size() > 999998) {
            return null;
        }

        String randomAccess;
        do {
            randomAccess = String.format("%06d", random.nextInt(999999) + 1);
        } while (allAccess.contains(randomAccess));

        UUID userId = (clipboard.getUserId() != null) ? clipboard.getUserId() : null;
        return clipboardRepository.createClipboard(UUID.randomUUID(), clipboard.getExpirationTime(), randomAccess, userId);
    }

}
