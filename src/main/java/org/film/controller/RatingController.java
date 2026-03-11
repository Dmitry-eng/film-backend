package org.film.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.film.dto.request.VoteRating;
import org.film.service.RatingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rating")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @Operation(summary = "Голосование")
    @PostMapping("/vote")
    public void voteRating(@RequestBody VoteRating voteRating) {
        ratingService.voteRating(voteRating);
    }

}
