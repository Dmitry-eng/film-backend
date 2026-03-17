import { useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { useAuth } from '@/context/AuthContext';
import './Login.css';

export default function Login() {
  const { login, user } = useAuth();
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const redirect = searchParams.get('redirect') || '/';

  const [name, setName] = useState('');
  const [email, setEmail] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!name.trim() || !email.trim()) return;
    login(name.trim(), email.trim());
    navigate(redirect, { replace: true });
  };

  if (user) {
    return (
      <div className="container login-page">
        <div className="login-page__box">
          <h1 className="login-page__title">Вы уже вошли</h1>
          <p className="login-page__text">Имя: {user.name}</p>
          <button type="button" className="btn btn--outline" onClick={() => navigate(redirect)}>
            Продолжить
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="container login-page">
      <div className="login-page__box">
        <h1 className="login-page__title">Вход</h1>
        <p className="login-page__hint">
          Демо-режим: данные сохраняются только в браузере. Для продакшена нужна реальная авторизация (JWT, OAuth).
        </p>
        <form className="login-page__form" onSubmit={handleSubmit}>
          <label className="login-page__label">
            <span>Имя</span>
            <input
              type="text"
              className="login-page__input"
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="Ваше имя"
              required
              autoComplete="name"
            />
          </label>
          <label className="login-page__label">
            <span>Email</span>
            <input
              type="email"
              className="login-page__input"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="email@example.com"
              required
              autoComplete="email"
            />
          </label>
          <button type="submit" className="btn btn--primary login-page__submit">
            Войти
          </button>
        </form>
      </div>
    </div>
  );
}
