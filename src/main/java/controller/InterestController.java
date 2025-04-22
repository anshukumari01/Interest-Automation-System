package controller;

import model.Interest;
import repository.InterestRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interests")
public class InterestController {
    private final InterestRepository interestRepository;

    public InterestController(InterestRepository interestRepository) {
        this.interestRepository = interestRepository;
    }

    @GetMapping
    public List<Interest> getAllInterests() {
        return interestRepository.findAll();
    }

    @PostMapping
    public Interest addInterest(@RequestBody Interest interest) {
        return interestRepository.save(interest);
    }
    @GetMapping("/test")
    public List<Interest> testFetch() {
        return interestRepository.findAll();
}
}
