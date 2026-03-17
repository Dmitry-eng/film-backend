import { Link, NavLink } from 'react-router-dom';
import { Film, Search, User, LogOut, Crown } from 'lucide-react';
import { useAuth } from '@/context/AuthContext';
import './Header.css';

export default function Header() {
  const { user, isSubscribed, logout } = useAuth();

  return (
    <header className="header">
      <div className="container header__inner">
        <Link to="/" className="header__logo">
          <Film size={28} aria-hidden />
          <span className="header__logo-text">Кинотеатр</span>
        </Link>

        <nav className="header__nav" aria-label="Основное меню">
          <NavLink to="/" className={({ isActive }) => `header__link ${isActive ? 'header__link--active' : ''}`} end>
            Главная
          </NavLink>
          <NavLink to="/catalog" className={({ isActive }) => `header__link ${isActive ? 'header__link--active' : ''}`}>
            Каталог
          </NavLink>
          {!isSubscribed && (
            <NavLink to="/subscription" className={({ isActive }) => `header__link header__link--premium ${isActive ? 'header__link--active' : ''}`}>
              <Crown size={18} aria-hidden />
              Подписка
            </NavLink>
          )}
        </nav>

        <div className="header__actions">
          <Link to="/catalog?search=" className="header__icon-btn" aria-label="Поиск">
            <Search size={22} aria-hidden />
          </Link>
          {user ? (
            <div className="header__user">
              <span className="header__user-name">{user.name}</span>
              <button type="button" className="header__icon-btn" onClick={logout} aria-label="Выйти">
                <LogOut size={20} aria-hidden />
              </button>
            </div>
          ) : (
            <Link to="/login" className="header__btn header__btn--primary">
              <User size={18} aria-hidden />
              Войти
            </Link>
          )}
        </div>
      </div>
    </header>
  );
}
