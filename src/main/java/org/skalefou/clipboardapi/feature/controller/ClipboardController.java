package org.skalefou.clipboardapi.feature.controller;

import org.skalefou.clipboardapi.feature.model.Clipboard;
import org.skalefou.clipboardapi.feature.service.ClipboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clipboard")
public class ClipboardController {
    private ClipboardService clipboardService;

    @GetMapping
    public Clipboard getClipboard() {
        return clipboardService.getClipboard();
    }
}
