package org.film.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.film.entity.FilmEntity;
import org.film.entity.RatingEntity;
import org.film.repository.FilmRepository;
import org.film.repository.RatingRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class RatingScheduler {

    private final RatingRepository ratingRepository;
    private final FilmRepository filmRepository;

    @Scheduled(fixedRate = 1000)
    public void updateFilmRatings() {

        Map<FilmEntity, List<RatingEntity>> ratingsByFilm = ratingRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(RatingEntity::getFilm));

        for (Map.Entry<FilmEntity, List<RatingEntity>> entry : ratingsByFilm.entrySet()) {
            FilmEntity film = entry.getKey();
            List<RatingEntity> ratings = entry.getValue();

            double averageRating = ratings.stream()
                    .mapToInt(RatingEntity::getGrade)
                    .average()
                    .orElse(0.0);

            double roundedRating = Math.round(averageRating * 10) / 10.0;

            film.setRating(roundedRating);

            log.debug("Фильм '{}' - рейтинг: {}, голосов: {}",
                    film.getName(), roundedRating, ratings.size());
        }

        filmRepository.saveAll(ratingsByFilm.keySet());
    }

}