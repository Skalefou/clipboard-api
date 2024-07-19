package org.skalefou.clipboardapi.feature.controller;

import lombok.RequiredArgsConstructor;
import org.skalefou.clipboardapi.feature.model.Clipboard;
import org.skalefou.clipboardapi.feature.service.ClipboardService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/clipboard")
public class ClipboardController {
    private final ClipboardService clipboardService;

    @GetMapping("/{access}")
    public Clipboard getClipboardByAccess(@PathVariable String access) {
        Clipboard clipboard = clipboardService.getClipboardByAccess(access);
        if (clipboard == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Clipboard not found or has no ID");
        }
        return clipboard;
    }

    @PostMapping
    public Clipboard createClipboard(@RequestBody Clipboard clipboard) {
        clipboard = clipboardService.createClipboard(clipboard);
        if (clipboard == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Clipboard invalid");
        }
        return clipboard;
    }

    @PutMapping
    public Clipboard updateClipboard(@RequestBody Clipboard clipboard) {
        clipboard = clipboardService.updateClipboard(clipboard);
        if (clipboard == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Clipboard invalid");
        }
        return clipboard;
    }
}
