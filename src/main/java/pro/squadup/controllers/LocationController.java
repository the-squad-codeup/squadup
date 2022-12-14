package pro.squadup.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.squadup.models.Location;
import pro.squadup.repositories.LocationRepository;

import java.util.List;

@RestController
public class LocationController {

    private LocationRepository locationDao;

    public LocationController(LocationRepository locationDao) {
        this.locationDao = locationDao;
    }

    @GetMapping("/location/all")
    public List<Location> getAllLocations(){
        System.out.println("Inside get all Locations");
        return locationDao.findAll();
    }

}
