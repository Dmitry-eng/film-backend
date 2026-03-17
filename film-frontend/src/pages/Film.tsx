import { useParams, Link } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { Play, Star, Lock, ArrowLeft } from 'lucide-react';
import { api } from '@/api/client';
import { useAuth } from '@/context/AuthContext';
import type { FullFilmInfo } from '@/types/api';
import './Film.css';

const CURRENT_YEAR = new Date().getFullYear();
const NEW_RELEASE_YEARS = 2;
const isNewRelease = (releaseDate: string) =>
  CURRENT_YEAR - new Date(releaseDate).getFullYear() <= NEW_RELEASE_YEARS;

export default function Film() {
  const { id } = useParams<{ id: string }>();
  const { isSubscribed } = useAuth();
  const [film, setFilm] = useState<FullFilmInfo | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [commentText, setCommentText] = useState('');
  const [submittingComment, setSubmittingComment] = useState(false);
  const [userGrade, setUserGrade] = useState<number | null>(null);
  const [submittingVote, setSubmittingVote] = useState(false);

  const filmId = id ? parseInt(id, 10) : NaN;
  const needSubscription = film && isNewRelease(film.releaseDate) && !isSubscribed;

  useEffect(() => {
    if (!id || Number.isNaN(filmId)) {
      setError('Неверный ID фильма');
      setLoading(false);
      return;
    }
    setLoading(true);
    setError(null);
    api.films
      .get(filmId)
      .then(setFilm)
      .catch(() => setError('Не удалось загрузить фильм'))
      .finally(() => setLoading(false));
  }, [id, filmId]);

  const handleSubmitComment = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!film || !commentText.trim() || submittingComment) return;
    setSubmittingComment(true);
    try {
      await api.comments.create({ filmId: film.id, value: commentText.trim() });
      setCommentText('');
      const updated = await api.films.get(film.id);
      setFilm(updated);
    } catch {
      // ignore
    } finally {
      setSubmittingComment(false);
    }
  };

  const handleVote = async (grade: number) => {
    if (!film || submittingVote) return;
    setSubmittingVote(true);
    try {
      await api.rating.vote({ filmId: film.id, grade });
      setUserGrade(grade);
      const updated = await api.films.get(film.id);
      setFilm(updated);
    } catch {
      // ignore
    } finally {
      setSubmittingVote(false);
    }
  };

  if (loading) {
    return (
      <div className="container film-page">
        <div className="film-page__skeleton" />
      </div>
    );
  }

  if (error || !film) {
    return (
      <div className="container film-page">
        <p className="film-page__error">{error || 'Фильм не найден'}</p>
        <Link to="/catalog" className="btn btn--outline">
          <ArrowLeft size={18} aria-hidden />
          В каталог
        </Link>
      </div>
    );
  }

  const primaryImage = film.images?.find((i) => i.isPrimary) || film.images?.[0];
  const posterUrl = primaryImage?.image || null;

  return (
    <div className="film-page">
      <div className="film-page__hero">
        <div className="film-page__hero-bg">
          {posterUrl && <img src={posterUrl} alt="" />}
        </div>
        <div className="container film-page__hero-content">
          <Link to="/catalog" className="film-page__back">
            <ArrowLeft size={20} aria-hidden />
            Назад к каталогу
          </Link>
          <div className="film-page__hero-info">
            <h1 className="film-page__title">{film.name}</h1>
            <p className="film-page__meta">
              {new Date(film.releaseDate).getFullYear()}
              {film.rating != null && (
                <>
                  <span className="film-page__dot">·</span>
                  <span className="film-page__rating">
                    <Star size={18} fill="currentColor" aria-hidden />
                    {film.rating.toFixed(1)}
                  </span>
                </>
              )}
              {isNewRelease(film.releaseDate) && (
                <span className="film-page__badge film-page__badge--new">Новинка</span>
              )}
            </p>
            <p className="film-page__desc">{film.description}</p>
            {needSubscription ? (
              <Link to="/subscription" className="btn btn--primary btn--lg">
                <Lock size={20} aria-hidden />
                Подписка для просмотра новинок
              </Link>
            ) : (
              <div className="film-page__watch">
                <button type="button" className="btn btn--primary btn--lg" disabled>
                  <Play size={22} aria-hidden />
                  Смотреть (плеер в разработке)
                </button>
              </div>
            )}
          </div>
        </div>
      </div>

      <div className="container film-page__body">
        <section className="film-page__section">
          <h2 className="film-page__section-title">Оценка</h2>
          <div className="film-page__rating-widget">
            {[1, 2, 3, 4, 5, 6, 7, 8, 9, 10].map((g) => (
              <button
                key={g}
                type="button"
                className={`film-page__star-btn ${userGrade === g ? 'film-page__star-btn--active' : ''}`}
                onClick={() => handleVote(g)}
                disabled={submittingVote}
                aria-label={`Оценить ${g}`}
                title={`${g}`}
              >
                {g}
              </button>
            ))}
          </div>
          {film.rating != null && (
            <p className="film-page__rating-avg">
              Средняя оценка: <strong>{film.rating.toFixed(1)}</strong>
            </p>
          )}
        </section>

        <section className="film-page__section">
          <h2 className="film-page__section-title">Комментарии</h2>
          <form className="film-page__comment-form" onSubmit={handleSubmitComment}>
            <textarea
              className="film-page__comment-input"
              placeholder="Написать комментарий..."
              value={commentText}
              onChange={(e) => setCommentText(e.target.value)}
              rows={3}
              disabled={submittingComment}
            />
            <button type="submit" className="btn btn--primary" disabled={submittingComment || !commentText.trim()}>
              {submittingComment ? 'Отправка…' : 'Отправить'}
            </button>
          </form>
          <ul className="film-page__comments">
            {film.comments?.map((c) => (
              <li key={c.id} className="film-page__comment">
                <div className="film-page__comment-header">
                  <span className="film-page__comment-author">{c.author.name}</span>
                  <time className="film-page__comment-date" dateTime={c.createDateTime}>
                    {new Date(c.createDateTime).toLocaleDateString('ru-RU')}
                  </time>
                </div>
                <p className="film-page__comment-text">{c.value}</p>
              </li>
            ))}
          </ul>
          {(!film.comments || film.comments.length === 0) && (
            <p className="film-page__no-comments">Пока нет комментариев.</p>
          )}
        </section>
      </div>
    </div>
  );
}
