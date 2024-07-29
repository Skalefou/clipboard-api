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
import java.util.stream.Collectors;

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

    // Mock global
    private Clipboard expectedClipboard;
    private final String testContent = "Bonjour";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        expectedClipboard = new Clipboard();
        expectedClipboard.setAccess("789123");

        when(clipboardRepository.findAllAccess()).thenReturn(List.of("123456"));
        when(clipboardRepository.createClipboard(any(UUID.class), any(LocalDateTime.class), anyString(), any(UUID.class)))
                .thenReturn(1);
        when(clipboardRepository.getClipboardById(any(UUID.class))).thenReturn(expectedClipboard);
        when(clipboardRepository.getClipboardByAccess(expectedClipboard.getAccess())).thenReturn(expectedClipboard);
        when(clipboardRepository.deleteClipboardByAccess(expectedClipboard.getAccess())).thenReturn(1);
        when(clipboardRepository.updateClipboard(testContent, expectedClipboard.getAccess())).thenReturn(1);

    }

    @Test
    void testGetClipboardByAccessValid() {
        // Exec
        Clipboard result = clipboardRepository.getClipboardByAccess("789123");

        // Verif
        assertNotNull(result);
        assertEquals(expectedClipboard.getAccess(), result.getAccess());
    }

    @Test
    void testCreateClipboardValid() {
        // Exec
        Clipboard result = clipboardService.createClipboard(expectedClipboard);

        //Verif

        assertNotNull(result);
    }

    @Test
    void testCreateClipboardExpirationDate() {
        // Init
        Clipboard clipExpirationBeforeToday = new Clipboard();
        Clipboard clipExpirationAfterOneYear = new Clipboard();
        Clipboard clipExpirationToday = new Clipboard();
        Clipboard clipAccurate = new Clipboard();
        clipExpirationBeforeToday.setExpirationTime(LocalDateTime.now().minusDays(1));
        clipExpirationAfterOneYear.setExpirationTime(LocalDateTime.now().plusYears(2));
        clipExpirationToday.setExpirationTime(LocalDateTime.now());
        clipAccurate.setExpirationTime(LocalDateTime.now().plusDays(1));

        // Exec

        Clipboard resultExpirationBeforeToday = clipboardService.createClipboard(clipExpirationBeforeToday);
        Clipboard resultExpirationAfterOneYear = clipboardService.createClipboard(clipExpirationAfterOneYear);
        Clipboard resultExpirationToday = clipboardService.createClipboard(clipExpirationToday);
        Clipboard resultAccurate = clipboardService.createClipboard(clipAccurate);
        // Verif

        assertNull(resultExpirationBeforeToday);
        assertNull(resultExpirationAfterOneYear);
        assertNull(resultExpirationToday);
        assertNotNull(resultAccurate);
    }

    @Test
    void testCreateClipboardMaxAccess() {
        // Init
        Set<String> allAccess = java.util.stream.IntStream.range(0, 999999)
                .mapToObj(Integer::toString)
                .collect(Collectors.toSet());
        when(clipboardRepository.findAllAccess()).thenReturn(new ArrayList<>(allAccess));

        // Exec
        Clipboard result = clipboardService.createClipboard(expectedClipboard);

        // Verif
        assertNull(result);
    }

    @Test
    void testGenerateUniqueAccessCode() {
        // Init
        Set<String> predefinedAccessCodes = new HashSet<>(Arrays.asList("000001", "000002", "000003"));
        when(clipboardRepository.findAllAccess()).thenReturn(new ArrayList<>(predefinedAccessCodes));

        Clipboard clipboard = new Clipboard();
        clipboard.setExpirationTime(LocalDateTime.now().plusDays(1));

        // Exec
        Clipboard result = clipboardService.createClipboard(clipboard);

        // Verif
        assertNotNull(result);
        assertFalse(predefinedAccessCodes.contains(result.getAccess()));
    }

    @Test
    void testDeleteClipboardValid() {
        // Exec
        boolean deleteResult = clipboardService.deleteClipboard(expectedClipboard.getAccess());
        // Verif
        assertTrue(deleteResult);
    }

    @Test
    void testDeleteClipboardInvalid() {
        // Init
        when(clipboardRepository.deleteClipboardByAccess("123456")).thenReturn(0);

        // Exec
        boolean deleteResult = clipboardService.deleteClipboard("123456");

        // Verif
        assertFalse(deleteResult);
    }

    @Test
    void testUpdateClipboardValid() {
        // Init
        Clipboard inputClip = expectedClipboard;
        inputClip.setContent(testContent);

        when(clipboardRepository.getClipboardByAccess(expectedClipboard.getAccess())).thenReturn(inputClip);

        // Exec
        Clipboard updatedClipboard = clipboardService.updateClipboard(inputClip);

        // Verif
        assertNotNull(updatedClipboard);
        assertEquals(updatedClipboard.getContent(), testContent);
        assertEquals(updatedClipboard.getContent(), expectedClipboard.getContent());
    }

    // Fais moi des tests unitaire pour la méthode getClipboardByAccess
    @Test
    void testGetClipboardByAccess() {
        // Init
        when(clipboardRepository.getClipboardByAccess("123456")).thenReturn(null);

        // Exec
        Clipboard resultNull = clipboardService.getClipboardByAccess("123456");
        Clipboard resultNotNull = clipboardService.getClipboardByAccess(expectedClipboard.getAccess());

        // Verif
        assertNotNull(resultNotNull);
        assertNull(resultNull);
    }
}
