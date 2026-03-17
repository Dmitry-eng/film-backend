import { Link } from 'react-router-dom';
import { Play, Sparkles } from 'lucide-react';
import { useEffect, useState } from 'react';
import { api } from '@/api/client';
import type { ShortFilmInfo } from '@/types/api';
import './Home.css';

export default function Home() {
  const [newReleases, setNewReleases] = useState<ShortFilmInfo[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.films
      .list({
        pageable: { page: 0, size: 8 },
        filter: [],
      })
      .then((data) => setNewReleases(Array.isArray(data) ? data : []))
      .catch(() => setNewReleases([]))
      .finally(() => setLoading(false));
  }, []);

  const sorted = [...newReleases].sort(
    (a, b) => new Date(b.releaseDate).getTime() - new Date(a.releaseDate).getTime()
  );
  const featured = sorted[0];

  return (
    <div className="home">
      <section className="hero">
        <div className="hero__bg" />
        <div className="container hero__content">
          <p className="hero__badge">
            <Sparkles size={18} aria-hidden />
            Домашний кинотеатр
          </p>
          <h1 className="hero__title">Смотрите фильмы и сериалы когда угодно</h1>
          <p className="hero__subtitle">
            Тысячи фильмов в одном месте. Оформите подписку и получите доступ к новинкам без рекламы.
          </p>
          <div className="hero__actions">
            <Link to="/catalog" className="btn btn--primary btn--lg">
              <Play size={22} aria-hidden />
              Смотреть каталог
            </Link>
            <Link to="/subscription" className="btn btn--outline btn--lg">
              Оформить подписку
            </Link>
          </div>
        </div>
      </section>

      <section className="categories-bar container">
        <h2 className="sr-only">Категории</h2>
        <div className="categories-bar__list">
          <Link to="/catalog?sort=new" className="categories-bar__item">Новинки</Link>
          <Link to="/catalog?sort=rating" className="categories-bar__item">По рейтингу</Link>
          <Link to="/catalog" className="categories-bar__item">Весь каталог</Link>
        </div>
      </section>

      {featured && (
        <section className="featured container">
          <h2 className="section-title">Главный фильм недели</h2>
          <Link to={`/film/${featured.id}`} className="featured-card">
            <div className="featured-card__poster">
              {featured.image ? (
                <img src={featured.image} alt="" />
              ) : (
                <div className="featured-card__placeholder" />
              )}
              <span className="featured-card__play">
                <Play size={48} aria-hidden />
              </span>
            </div>
            <div className="featured-card__info">
              <h3 className="featured-card__title">{featured.name}</h3>
              <p className="featured-card__desc">{featured.description.slice(0, 160)}…</p>
              {featured.rating != null && (
                <span className="featured-card__rating">{featured.rating.toFixed(1)}</span>
              )}
            </div>
          </Link>
        </section>
      )}

      <section className="section container">
        <div className="section__head">
          <h2 className="section-title">Новинки</h2>
          <Link to="/catalog?sort=new" className="section-link">
            Все новинки →
          </Link>
        </div>
        {loading ? (
          <div className="film-grid film-grid--loading">
            {[...Array(4)].map((_, i) => (
              <div key={i} className="film-card film-card--skeleton" />
            ))}
          </div>
        ) : (
          <div className="film-grid">
            {sorted.slice(0, 8).map((film) => (
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
        )}
      </section>

      <section className="cta-section">
        <div className="container cta-section__inner">
          <h2 className="cta-section__title">Подписка на новинки</h2>
          <p className="cta-section__text">
            Получите доступ к премьерам и эксклюзивному контенту без рекламы.
          </p>
          <Link to="/subscription" className="btn btn--primary btn--lg">
            Подключить подписку
          </Link>
        </div>
      </section>
    </div>
  );
}
