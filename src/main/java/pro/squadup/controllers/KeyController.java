package pro.squadup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.squadup.services.KeyService;

@RestController
public class KeyController {

    @Autowired
    private KeyService keys;

    @GetMapping("/keys")
    public KeyService getKeys() {
        return keys;
    }
}
