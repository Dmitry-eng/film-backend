import { createContext, useContext, useState, useCallback, ReactNode } from 'react';

type SubscriptionTier = 'free' | 'premium';

interface User {
  id: number;
  name: string;
  email: string;
  subscription: SubscriptionTier;
  subscriptionUntil?: string;
}

interface AuthState {
  user: User | null;
  isSubscribed: boolean;
  login: (name: string, email: string) => void;
  logout: () => void;
  subscribe: (until: string) => void;
}

const AuthContext = createContext<AuthState | null>(null);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(() => {
    try {
      const raw = localStorage.getItem('film-user');
      if (raw) return JSON.parse(raw) as User;
    } catch {}
    return null;
  });

  const isSubscribed =
    !!user &&
    user.subscription === 'premium' &&
    (user.subscriptionUntil ? new Date(user.subscriptionUntil) > new Date() : false);

  const login = useCallback((name: string, email: string) => {
    const u: User = { id: 1, name, email, subscription: 'free' };
    setUser(u);
    localStorage.setItem('film-user', JSON.stringify(u));
  }, []);

  const logout = useCallback(() => {
    setUser(null);
    localStorage.removeItem('film-user');
  }, []);

  const subscribe = useCallback((until: string) => {
    setUser((prev) => {
      if (!prev) return prev;
      const next = { ...prev, subscription: 'premium' as const, subscriptionUntil: until };
      localStorage.setItem('film-user', JSON.stringify(next));
      return next;
    });
  }, []);

  return (
    <AuthContext.Provider
      value={{
        user,
        isSubscribed,
        login,
        logout,
        subscribe,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
}
