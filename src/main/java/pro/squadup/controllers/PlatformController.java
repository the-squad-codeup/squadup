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

    @GetMapping("/platform/all")
    public List<Platform> getAllPlatforms(){
        System.out.println("Inside get all Platforms");
        return platformDao.findAll();
    }

}
