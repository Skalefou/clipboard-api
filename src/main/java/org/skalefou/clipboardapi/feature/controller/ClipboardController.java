package org.skalefou.clipboardapi.feature.controller;

import lombok.RequiredArgsConstructor;
import org.skalefou.clipboardapi.feature.config.InjectUser;
import org.skalefou.clipboardapi.feature.model.Clipboard;
import org.skalefou.clipboardapi.feature.model.Users;
import org.skalefou.clipboardapi.feature.service.ClipboardService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ClipboardController {
    private final ClipboardService clipboardService;

    @InjectUser
    @GetMapping("/public/clipboard/{access}")
    public Clipboard getClipboardByAccess(@PathVariable String access, Users users) {
        Clipboard clipboard = clipboardService.getClipboardByAccess(access);
        if (clipboard == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Clipboard not found or has no ID");
        }
        return clipboard;
    }

    @PostMapping("/public/clipboard")
    public Clipboard createClipboard(@RequestBody Clipboard clipboard) {
        clipboard = clipboardService.createClipboard(clipboard);
        if (clipboard == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Clipboard invalid");
        }
        return clipboard;
    }

    @PutMapping("/public/clipboard")
    public Clipboard updateClipboard(@RequestBody Clipboard clipboard) {
        clipboard = clipboardService.updateClipboard(clipboard);
        if (clipboard == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Clipboard invalid");
        }
        return clipboard;
    }

    @DeleteMapping("/public/clipboard/{access}")
    public void deleteClipboard(@PathVariable String access) {
        if (!clipboardService.deleteClipboard(access)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Clipboard not found or has no ID");
        }
    }
}
