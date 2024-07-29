package org.skalefou.clipboardapi.feature.controller;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.skalefou.clipboardapi.feature.model.Clipboard;
import org.skalefou.clipboardapi.feature.service.ClipboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;

import javax.sound.sampled.Clip;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ClipboardControllerTest {
    @MockBean
    private ClipboardService clipboardService;

    @Autowired
    private ClipboardController clipboardController;

    // Clipboard that supposedly existed in the database
    private Clipboard expectedClipboard;

    // Clipboard that does not exist in the database
    private Clipboard invalidClipboard;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.expectedClipboard = new Clipboard();
        this.invalidClipboard = new Clipboard();
        this.expectedClipboard.setAccess("789123");
        this.invalidClipboard.setExpirationTime(LocalDateTime.now().minusYears(1));

        when(clipboardService.getClipboardByAccess(expectedClipboard.getAccess())).thenReturn(expectedClipboard);
        when(clipboardService.getClipboardByAccess("123456")).thenReturn(null);

        when(clipboardService.createClipboard(any(Clipboard.class))).thenReturn(expectedClipboard);
        when(clipboardService.updateClipboard(any(Clipboard.class))).thenReturn(expectedClipboard);
        when(clipboardService.deleteClipboard(expectedClipboard.getAccess())).thenReturn(true);
        when(clipboardService.deleteClipboard("123456")).thenReturn(false);
    }

    @Test
    void testGetClipboardByAccessValid() {
        Clipboard result = clipboardController.getClipboardByAccess(expectedClipboard.getAccess());
        assertNotNull(result);
    }

    @Test
    void testGetClipboardByAccessInvalid() {
        assertThrows(ResponseStatusException.class, () -> {
            clipboardController.getClipboardByAccess("123456");
        });
    }

    @Test
    void testCreateClipboardValid() {
        Clipboard result = clipboardController.createClipboard(expectedClipboard);
        assertNotNull(result);
    }

    @Test
    void testCreateClipboardInvalid() {
        // Init
        when(clipboardService.createClipboard(any(Clipboard.class))).thenReturn(null);

        // Exec and verif
        assertThrows(ResponseStatusException.class, () -> {
            clipboardController.createClipboard(invalidClipboard);
        });
    }

    @Test
    void testUpdateClipboardValid() {
        Clipboard result = clipboardController.updateClipboard(expectedClipboard);
        assertNotNull(result);
    }

    @Test
    void testUpdateClipboardInvalid() {
        // Init
        when(clipboardService.updateClipboard(any(Clipboard.class))).thenReturn(null);

        // Exec and verif
        assertThrows(ResponseStatusException.class, () -> {
            clipboardController.updateClipboard(invalidClipboard);
        });
    }

    @Test
    void testDeleteClipboardValid() {
        assertDoesNotThrow(() -> clipboardController.deleteClipboard(expectedClipboard.getAccess()));
    }

    @Test
    void testDeleteClipboardInvalid() {
        assertThrows(ResponseStatusException.class, () -> {
            clipboardController.deleteClipboard("123456");
        });
    }
}
