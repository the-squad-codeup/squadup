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

    // returns list of all age ratings in database
    @GetMapping("/rating/all")
    public List<Rating> getAllRatings(){
        return ratingDao.findAll();
    }

}
