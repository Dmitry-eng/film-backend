const API_BASE = '/api';

async function request<T>(
  path: string,
  options: RequestInit & { params?: Record<string, string> } = {}
): Promise<T> {
  const { params, ...init } = options;
  const url = new URL(path.startsWith('http') ? path : `${API_BASE}${path}`, window.location.origin);
  if (params) {
    Object.entries(params).forEach(([k, v]) => url.searchParams.set(k, String(v)));
  }
  const res = await fetch(url.toString(), {
    ...init,
    headers: {
      'Content-Type': 'application/json',
      ...init.headers,
    },
  });
  if (!res.ok) {
    const text = await res.text();
    throw new Error(text || `HTTP ${res.status}`);
  }
  if (res.status === 204) return undefined as T;
  return res.json();
}

export const api = {
  films: {
    list: (body: { filter?: { field: string; operator: string; value: string }[]; pageable: { page: number; size: number } }, ignoreRemove?: boolean) =>
      request<unknown[]>('/film/all', {
        method: 'POST',
        body: JSON.stringify(body),
        params: ignoreRemove !== undefined ? { ignoreRemove: String(ignoreRemove) } : undefined,
      }) as Promise<import('@/types/api').ShortFilmInfo[]>,

    get: (id: number, ignoreRemove?: boolean) =>
      request<import('@/types/api').FullFilmInfo>(`/film/${id}`, {
        params: ignoreRemove !== undefined ? { ignoreRemove: String(ignoreRemove) } : undefined,
      }),

    create: (body: import('@/types/api').CreateFilm) =>
      request<import('@/types/api').FullFilmInfo>('/film', { method: 'POST', body: JSON.stringify(body) }),

    update: (body: import('@/types/api').UpdateFilm) =>
      request<import('@/types/api').FullFilmInfo>('/film', { method: 'PUT', body: JSON.stringify(body) }),

    delete: (id: number) => request<void>(`/film/${id}`, { method: 'DELETE' }),
  },

  comments: {
    create: (body: import('@/types/api').CreateComment) =>
      request<import('@/types/api').CommentInfo>('/comment', { method: 'POST', body: JSON.stringify(body) }),
    delete: (id: number) => request<void>(`/comment/${id}`, { method: 'DELETE' }),
  },

  rating: {
    vote: (body: import('@/types/api').VoteRating) =>
      request<void>('/rating/vote', { method: 'POST', body: JSON.stringify(body) }),
  },
};
