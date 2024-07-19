package org.skalefou.clipboardapi.feature.service;


import org.junit.jupiter.api.Test;
import org.skalefou.clipboardapi.feature.model.Clipboard;
import org.skalefou.clipboardapi.feature.repository.ClipboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
}
