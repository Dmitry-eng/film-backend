import { useSearchParams } from 'react-router-dom';
import { useCallback, useEffect, useState } from 'react';
import { Search, Filter as FilterIcon } from 'lucide-react';
import { api } from '@/api/client';
import type { ShortFilmInfo } from '@/types/api';
import type { FilterContext } from '@/types/api';
import FilmGrid from '@/components/FilmGrid';
import './Catalog.css';

const PAGE_SIZE = 12;
const SORT_OPTIONS = [
  { value: 'new', label: 'Сначала новинки' },
  { value: 'old', label: 'Сначала старые' },
  { value: 'rating', label: 'По рейтингу' },
];

export default function Catalog() {
  const [searchParams, setSearchParams] = useSearchParams();
  const [films, setFilms] = useState<ShortFilmInfo[]>([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(() => Math.max(0, parseInt(searchParams.get('page') || '0', 10)));
  const [search, setSearch] = useState(() => searchParams.get('search') || '');
  const [sort, setSort] = useState(() => searchParams.get('sort') || 'new');
  const [searchInput, setSearchInput] = useState(search);

  const fetchFilms = useCallback(
    async (pageNum: number, searchQuery: string, sortBy: string) => {
      setLoading(true);
      const filter: FilterContext['filter'] = [];
      if (searchQuery.trim()) {
        filter.push({ field: 'name', operator: 'CONTAINS', value: searchQuery.trim() });
      }
      const body: FilterContext = {
        filter,
        pageable: { page: pageNum, size: PAGE_SIZE },
      };
      try {
        const data = await api.films.list(body);
        const list = Array.isArray(data) ? data : [];
        let ordered = list;
        if (sortBy === 'new') {
          ordered = [...list].sort((a, b) => new Date(b.releaseDate).getTime() - new Date(a.releaseDate).getTime());
        } else if (sortBy === 'old') {
          ordered = [...list].sort((a, b) => new Date(a.releaseDate).getTime() - new Date(b.releaseDate).getTime());
        } else if (sortBy === 'rating') {
          ordered = [...list].sort((a, b) => (b.rating ?? 0) - (a.rating ?? 0));
        }
        setFilms(ordered);
      } catch {
        setFilms([]);
      } finally {
        setLoading(false);
      }
    },
    []
  );

  useEffect(() => {
    fetchFilms(page, search, sort);
  }, [page, search, sort, fetchFilms]);

  useEffect(() => {
    const p = searchParams.get('page') || '0';
    const s = searchParams.get('search') || '';
    const sortVal = searchParams.get('sort') || 'new';
    setPage(Math.max(0, parseInt(p, 10)));
    setSearch(s);
    setSearchInput(s);
    setSort(sortVal);
  }, [searchParams]);

  const handleSearchSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setSearch(searchInput);
    setPage(0);
    setSearchParams((prev) => {
      const next = new URLSearchParams(prev);
      next.set('search', searchInput);
      next.set('page', '0');
      return next;
    });
  };

  const handleSortChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const v = e.target.value;
    setSort(v);
    setPage(0);
    setSearchParams((prev) => {
      const next = new URLSearchParams(prev);
      next.set('sort', v);
      next.set('page', '0');
      return next;
    });
  };

  const goToPage = (newPage: number) => {
    setPage(newPage);
    setSearchParams((prev) => {
      const next = new URLSearchParams(prev);
      next.set('page', String(newPage));
      return next;
    });
  };

  return (
    <div className="catalog container">
      <h1 className="catalog__title">Каталог фильмов</h1>

      <div className="catalog__toolbar">
        <form className="catalog__search" onSubmit={handleSearchSubmit}>
          <Search size={20} className="catalog__search-icon" aria-hidden />
          <input
            type="search"
            className="catalog__search-input"
            placeholder="Поиск по названию..."
            value={searchInput}
            onChange={(e) => setSearchInput(e.target.value)}
            aria-label="Поиск"
          />
          <button type="submit" className="btn btn--primary">
            Найти
          </button>
        </form>

        <div className="catalog__filters">
          <FilterIcon size={18} aria-hidden />
          <label className="catalog__sort-label">
            <span className="sr-only">Сортировка</span>
            <select
              className="catalog__sort"
              value={sort}
              onChange={handleSortChange}
              aria-label="Сортировка"
            >
              {SORT_OPTIONS.map((opt) => (
                <option key={opt.value} value={opt.value}>
                  {opt.label}
                </option>
              ))}
            </select>
          </label>
        </div>
      </div>

      {loading ? (
        <div className="film-grid film-grid--loading">
          {[...Array(PAGE_SIZE)].map((_, i) => (
            <div key={i} className="film-card film-card--skeleton" />
          ))}
        </div>
      ) : (
        <>
          <FilmGrid films={films} />
          {films.length === 0 && (
            <p className="catalog__empty">По вашему запросу ничего не найдено. Попробуйте изменить фильтры.</p>
          )}

          <div className="catalog__pagination">
            <button
              type="button"
              className="btn btn--outline"
              disabled={page === 0}
              onClick={() => goToPage(page - 1)}
            >
              Назад
            </button>
            <span className="catalog__page-info">
              Страница {page + 1}
            </span>
            <button
              type="button"
              className="btn btn--outline"
              disabled={films.length < PAGE_SIZE}
              onClick={() => goToPage(page + 1)}
            >
              Вперёд
            </button>
          </div>
        </>
      )}
    </div>
  );
}
