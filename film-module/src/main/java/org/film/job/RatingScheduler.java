package org.film.job;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.film.db.entity.RatingEntity;
import org.film.db.repository.RatingRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class RatingScheduler {

    private final RatingRepository ratingRepository;

    @Scheduled(fixedRate = 1000)
    @Transactional
    public void updateFilmRatings() {

        List<RatingEntity> entities = ratingRepository.getEntityByIsRequiredCalculate(true);

        for (RatingEntity rating : entities) {
            try {
                recalculateRating(rating);
            } catch (Exception e) {
                log.error("Error calculate rating: {}",
                        rating.getFilm().getId(), e);
            }
        }

        ratingRepository.saveAll(entities);
    }

    private void recalculateRating(RatingEntity rating) {
        Map<Integer, Long> distribution = rating.getGradeDistribution();

        long totalVotes = distribution.values().stream()
                .mapToLong(Long::longValue)
                .sum();

        if (totalVotes == 0) {
            rating.setAverageRating(0.0);
        } else {
            long sum = distribution.entrySet().stream()
                    .mapToLong(entry -> entry.getKey() * entry.getValue())
                    .sum();

            double average = (double) sum / totalVotes;
            double roundedAverage = Math.round(average * 10) / 10.0;

            rating.setAverageRating(roundedAverage);

            log.debug("Film '{}' - rating: {} (votes: {}, sum: {})",
                    rating.getFilm().getName(),
                    roundedAverage,
                    totalVotes,
                    sum);
        }

        rating.setIsRequiredCalculate(false);
    }

}