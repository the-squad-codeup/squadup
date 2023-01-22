package pro.squadup.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.squadup.models.Platform;
import pro.squadup.repositories.PlatformRepository;

import java.util.List;

@RestController
public class PlatformController {

    private PlatformRepository platformDao;

    public PlatformController(PlatformRepository platformDao) {
        this.platformDao = platformDao;
    }

    // returns all platforms in database
    @GetMapping("/platform/all")
    public List<Platform> getAllPlatforms(){
        return platformDao.findAll();
    }

}
