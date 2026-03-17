import { Link } from 'react-router-dom';
import type { ShortFilmInfo } from '@/types/api';
import './FilmGrid.css';

interface FilmGridProps {
  films: ShortFilmInfo[];
}

export default function FilmGrid({ films }: FilmGridProps) {
  return (
    <div className="film-grid">
      {films.map((film) => (
        <Link key={film.id} to={`/film/${film.id}`} className="film-card">
          <div className="film-card__poster">
            {film.image ? (
              <img src={film.image} alt="" />
            ) : (
              <div className="film-card__placeholder" />
            )}
            <span className="film-card__rating-badge">
              {film.rating != null ? film.rating.toFixed(1) : '—'}
            </span>
          </div>
          <div className="film-card__body">
            <h3 className="film-card__title">{film.name}</h3>
            <p className="film-card__year">{new Date(film.releaseDate).getFullYear()}</p>
          </div>
        </Link>
      ))}
    </div>
  );
}
