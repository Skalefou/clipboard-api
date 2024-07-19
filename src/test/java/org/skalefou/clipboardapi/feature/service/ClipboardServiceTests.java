package org.skalefou.clipboardapi.feature.service;


import org.junit.jupiter.api.Test;
import org.skalefou.clipboardapi.feature.model.Clipboard;
import org.skalefou.clipboardapi.feature.repository.ClipboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ClipboardServiceTests {
    @MockBean
    private ClipboardRepository clipboardRepository;

    @Autowired
    private ClipboardService clipboardService;

    private Random random = new Random();

    @Test
    void testGetClipboardByAccess() {
        // Init
        Clipboard expectedClipboard = new Clipboard();
        expectedClipboard.setAccess("123456");
        when(clipboardRepository.getClipboardByAccess(anyString())).thenReturn(expectedClipboard);

        // Exec
        Clipboard result = clipboardRepository.getClipboardByAccess("123456");

        // Verif
        assertNotNull(result);
        assertEquals("123456", result.getAccess());
    }

    @Test
    void testCreateClipboard() {
        // Init
        Clipboard inputClipboard = new Clipboard();
        inputClipboard.setExpirationTime(LocalDateTime.now().plusDays(1));
        inputClipboard.setUserId(UUID.randomUUID());

        List<String> existingAccess = List.of("123456");
        when(clipboardRepository.findAllAccess()).thenReturn(existingAccess);

        // Exec
        Clipboard result = clipboardService.createClipboard(inputClipboard);

        //Verif
        assertNotNull(result);
        assertNotNull(result.getAccess());

        Clipboard retrievedClipboard = clipboardService.getClipboardByAccess(result.getAccess());
        assertNotNull(retrievedClipboard);
        assertEquals(inputClipboard.getAccess(), retrievedClipboard.getAccess());
    }

    @Test
    void testDeleteClipboard() {
        // Init
        Clipboard inputClipboard = new Clipboard();
        inputClipboard.setExpirationTime(LocalDateTime.now().plusDays(1));
        inputClipboard.setUserId(UUID.randomUUID());

        Clipboard createdClipboard = clipboardService.createClipboard(inputClipboard);

        // Exec
        boolean deleteResult = clipboardService.deleteClipboard(createdClipboard.getAccess());

        // Verif
        assertTrue(deleteResult);
        Clipboard retrievedClipboard = clipboardService.getClipboardByAccess(createdClipboard.getAccess());
        assertNull(retrievedClipboard);
    }
}
