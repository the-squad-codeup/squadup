package pro.squadup.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.squadup.models.Rating;
import pro.squadup.repositories.RatingRepository;

import java.util.List;

@RestController
public class RatingController {

    private RatingRepository ratingDao;

    public RatingController(RatingRepository ratingDao) {
        this.ratingDao = ratingDao;
    }

    @GetMapping("/rating/all")
    public List<Rating> getAllRatings(){
        System.out.println("Inside get all Ratings");
        return ratingDao.findAll();
    }

}
