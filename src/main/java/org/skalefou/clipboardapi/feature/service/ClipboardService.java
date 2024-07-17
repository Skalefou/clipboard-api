package org.skalefou.clipboardapi.feature.service;

import org.skalefou.clipboardapi.feature.model.Clipboard;
import org.springframework.stereotype.Service;

@Service
public class ClipboardService {

    public Clipboard getClipboard() {
        return new Clipboard();
    }

}
