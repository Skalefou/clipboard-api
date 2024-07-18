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

    @GetMapping("/{id}")
    public Clipboard getClipboard(@PathVariable UUID id) {
        Clipboard clipboard = clipboardService.getClipboard(id);
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
}
