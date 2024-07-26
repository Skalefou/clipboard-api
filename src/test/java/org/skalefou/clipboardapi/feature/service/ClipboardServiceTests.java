package org.skalefou.clipboardapi.feature.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.skalefou.clipboardapi.feature.model.Clipboard;
import org.skalefou.clipboardapi.feature.repository.ClipboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class ClipboardServiceTests {
    @MockBean
    private ClipboardRepository clipboardRepository;

    @Autowired
    private ClipboardService clipboardService;
    
    

    private Random random = new Random();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

    }

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
        Clipboard expectedClipboard = new Clipboard();
        expectedClipboard.setAccess("789123");

        when(clipboardRepository.findAllAccess()).thenReturn(List.of("123456"));
        when(clipboardRepository.createClipboard(any(UUID.class), any(LocalDateTime.class), anyString(), any(UUID.class)))
                .thenReturn(1);
        when(clipboardRepository.getClipboardById(any(UUID.class))).thenReturn(expectedClipboard);

        // Exec
        Clipboard result = clipboardService.createClipboard(expectedClipboard);

        //Verif

        assertNotNull(result);
    }

    @Test
    void testDeleteClipboard() {
        // Init
        when(clipboardRepository.deleteClipboardByAccess(anyString())).thenReturn(1);
        // Exec
        boolean deleteResult = clipboardService.deleteClipboard("123456");
        // Verif
        assertTrue(deleteResult);
    }

    @Test
    void testUpdateClipboard() {
        // Init
        Clipboard originalClipboard = new Clipboard();
        originalClipboard.setExpirationTime(LocalDateTime.now().plusDays(1));
        originalClipboard.setUserId(UUID.randomUUID());
        originalClipboard.setAccess("123456");
        originalClipboard.setContent("yes");

        when(clipboardRepository.updateClipboard(originalClipboard.getContent(), originalClipboard.getAccess())).thenReturn(1);
        when(clipboardRepository.getClipboardByAccess(originalClipboard.getAccess())).thenReturn(originalClipboard);

        // Exec
        Clipboard updatedClipboard = clipboardService.updateClipboard(originalClipboard);

        // Verif
        assertNotNull(updatedClipboard);
        assertEquals(originalClipboard.getContent(), updatedClipboard.getContent());
        assertEquals(originalClipboard.getAccess(), updatedClipboard.getAccess());
    }
}
