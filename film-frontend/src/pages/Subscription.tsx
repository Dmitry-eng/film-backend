import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Check, Crown } from 'lucide-react';
import { useAuth } from '@/context/AuthContext';
import './Subscription.css';

const PLANS = [
  {
    id: 'month',
    name: 'Месячная',
    price: 299,
    period: 'месяц',
    description: 'Доступ к новинкам и каталогу без рекламы',
    features: ['Все новинки текущего года', 'Без рекламы', 'Просмотр на одном устройстве'],
  },
  {
    id: 'year',
    name: 'Годовая',
    price: 2490,
    period: 'год',
    save: 'Экономия 30%',
    description: 'Выгодная подписка на год',
    features: ['Всё из месячной подписки', 'Эксклюзивные премьеры', 'До 3 устройств'],
  },
];

export default function Subscription() {
  const { user, isSubscribed, subscribe } = useAuth();
  const navigate = useNavigate();
  const [selectedPlan, setSelectedPlan] = useState<string>('month');
  const [processing, setProcessing] = useState(false);
  const [success, setSuccess] = useState(false);

  const handlePay = async () => {
    setProcessing(true);
    try {
      // Имитация оплаты: в реальном приложении здесь Stripe / ЮKassa / и т.д.
      await new Promise((r) => setTimeout(r, 1500));
      const until = new Date();
      if (selectedPlan === 'month') until.setMonth(until.getMonth() + 1);
      else until.setFullYear(until.getFullYear() + 1);
      subscribe(until.toISOString().slice(0, 10));
      setSuccess(true);
      setTimeout(() => navigate('/catalog'), 2000);
    } catch {
      // show error
    } finally {
      setProcessing(false);
    }
  };

  if (isSubscribed) {
    return (
      <div className="container subscription-page">
        <div className="subscription-page__active">
          <Crown size={48} className="subscription-page__crown" aria-hidden />
          <h1 className="subscription-page__title">Подписка активна</h1>
          <p className="subscription-page__text">
            У вас есть доступ к новинкам и всему каталогу. Приятного просмотра!
          </p>
          <button type="button" className="btn btn--primary" onClick={() => navigate('/catalog')}>
            Перейти в каталог
          </button>
        </div>
      </div>
    );
  }

  if (success) {
    return (
      <div className="container subscription-page">
        <div className="subscription-page__success">
          <Check size={64} className="subscription-page__check" aria-hidden />
          <h1 className="subscription-page__title">Оплата прошла успешно</h1>
          <p className="subscription-page__text">Подписка активирована. Перенаправляем в каталог…</p>
        </div>
      </div>
    );
  }

  return (
    <div className="container subscription-page">
      <h1 className="subscription-page__heading">Подписка на новинки</h1>
      <p className="subscription-page__intro">
        Получите доступ к премьерам и смотрите без рекламы. Отменить можно в любой момент.
      </p>

      <div className="subscription-page__plans">
        {PLANS.map((plan) => (
          <div
            key={plan.id}
            className={`subscription-card ${selectedPlan === plan.id ? 'subscription-card--selected' : ''}`}
            onClick={() => setSelectedPlan(plan.id)}
            onKeyDown={(e) => e.key === 'Enter' && setSelectedPlan(plan.id)}
            role="button"
            tabIndex={0}
          >
            {plan.save && <span className="subscription-card__save">{plan.save}</span>}
            <div className="subscription-card__header">
              <h2 className="subscription-card__name">{plan.name}</h2>
              <p className="subscription-card__price">
                {plan.price} ₽ <span className="subscription-card__period">/ {plan.period}</span>
              </p>
            </div>
            <p className="subscription-card__desc">{plan.description}</p>
            <ul className="subscription-card__features">
              {plan.features.map((f) => (
                <li key={f}>
                  <Check size={18} aria-hidden />
                  {f}
                </li>
              ))}
            </ul>
            <button
              type="button"
              className="btn btn--primary subscription-card__btn"
              onClick={(e) => {
                e.stopPropagation();
                handlePay();
              }}
              disabled={processing}
            >
              {processing ? 'Оформление…' : 'Оформить подписку'}
            </button>
          </div>
        ))}
      </div>

      {!user && (
        <p className="subscription-page__login-hint">
          <a href="/login">Войдите</a> в аккаунт, чтобы оформить подписку.
        </p>
      )}

      <p className="subscription-page__disclaimer">
        Это демо-режим. Реальная оплата не списывается. Для продакшена подключите платёжный шлюз (Stripe, ЮKassa и т.д.).
      </p>
    </div>
  );
}
