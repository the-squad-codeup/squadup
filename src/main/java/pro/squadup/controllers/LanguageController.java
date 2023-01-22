package pro.squadup.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.squadup.models.Language;
import pro.squadup.repositories.LanguageRepository;

import java.util.List;

@RestController
public class LanguageController {

    private LanguageRepository languageDao;

    public LanguageController(LanguageRepository languageDao) {
        this.languageDao = languageDao;
    }

    // returns list of all languages currently in our database
    @GetMapping("/language/all")
    public List<Language> getAllLanguages(){
        return languageDao.findAll();
    }

}
