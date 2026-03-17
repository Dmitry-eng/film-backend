/** API types aligned with Spring Boot backend */

export type FilterOperator = 'EQUALS' | 'NOT_EQUALS' | 'CONTAINS' | 'IN';

export interface FilterCriteria {
  field: string;
  operator: FilterOperator;
  value: string;
}

export interface PageRequestDto {
  page: number;
  size: number;
}

export interface FilterContext {
  filter?: FilterCriteria[];
  pageable: PageRequestDto;
}

export interface ShortFilmInfo {
  id: number;
  image: string | null;
  name: string;
  description: string;
  rating: number | null;
  releaseDate: string;
  createDateTime: string;
  updateDatetime: string;
}

export interface Image {
  id: number;
  isPrimary: boolean;
  image: string;
}

export interface ShortAccountInfo {
  id: number;
  name: string;
}

export interface CommentInfo {
  id: number;
  previewCommentId: number | null;
  author: ShortAccountInfo;
  value: string;
  createDateTime: string;
}

export interface FullFilmInfo {
  id: number;
  images: Image[];
  name: string;
  description: string;
  rating: number | null;
  comments: CommentInfo[];
  releaseDate: string;
  createDateTime: string;
  updateDatetime: string;
}

export interface CreateFilm {
  name: string;
  description: string;
  releaseDate: string;
  images?: { isPrimary: boolean; image: string }[];
}

export interface UpdateFilm extends CreateFilm {
  id: number;
  images?: { id?: number; isPrimary: boolean; image: string }[];
}

export interface CreateComment {
  filmId: number;
  value: string;
  previewCommentId?: number;
}

export interface VoteRating {
  filmId: number;
  grade: number;
}
