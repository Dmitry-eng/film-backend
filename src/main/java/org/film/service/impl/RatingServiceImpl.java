package org.film.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.film.entity.AccountEntity;
import org.film.entity.FilmEntity;
import org.film.entity.GradeEntity;
import org.film.entity.RatingEntity;
import org.film.db.repository.FilmRepository;
import org.film.db.repository.GradeRepository;
import org.film.db.repository.RatingRepository;
import org.film.dto.request.VoteRating;
import org.film.exception.ServiceException;
import org.film.service.RatingService;
import org.springframework.stereotype.Service;

import java.util.Map;

import static org.film.exception.ServiceExceptionType.FILM_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final GradeRepository gradeRepository;
    private final RatingRepository ratingRepository;
    private final FilmRepository filmRepository;
    private final SecurityContextService securityContextService;

    @Override
    @Transactional
    public void voteRating(VoteRating voteRating) {
        Long filmId = voteRating.getFilmId();

        FilmEntity filmEntity = filmRepository.findById(filmId)
                .orElseThrow(() -> new ServiceException(FILM_NOT_FOUND));

        AccountEntity accountEntity = securityContextService.getAccount();

        GradeEntity gradeEntity = gradeRepository.getGradeEntityByFilmAndAccount(filmEntity, accountEntity)
                .orElseGet(() -> {
                    GradeEntity newGradeEntity = new GradeEntity();
                    newGradeEntity.setFilm(filmEntity);
                    newGradeEntity.setAccount(accountEntity);
                    return newGradeEntity;
                });

        RatingEntity ratingEntity = ratingRepository.getRatingEntityByFilm(filmEntity)
                .orElseGet(() -> {
                    RatingEntity newRatingEntity = new RatingEntity();
                    newRatingEntity.setFilm(filmEntity);
                    return newRatingEntity;
                });

        filmEntity.setRating(ratingEntity);

        calculate(ratingEntity, gradeEntity, voteRating);

        gradeRepository.save(gradeEntity);
        ratingRepository.save(ratingEntity);
        filmRepository.save(filmEntity);

    }


    private void calculate(RatingEntity ratingEntity, GradeEntity gradeEntity, VoteRating voteRating) {
        Integer newGrade = voteRating.getGrade();
        Integer oldGrade = gradeEntity.getGrade();

        Map<Integer, Long> distribution = ratingEntity.getGradeDistribution();

        if (oldGrade != null) {
            distribution.merge(oldGrade, -1L, Long::sum);
            if (distribution.get(oldGrade) <= 0) {
                distribution.remove(oldGrade);
            }
        }

        distribution.merge(newGrade, 1L, Long::sum);

        gradeEntity.setGrade(newGrade);

        ratingEntity.setGradeDistribution(distribution);
        ratingEntity.setIsRequiredCalculate(true);
    }
}
