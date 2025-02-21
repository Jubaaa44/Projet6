import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { Post } from '../interfaces/post.interface';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private apiUrl = 'http://localhost:8080/api/posts';

  constructor(private http: HttpClient) {}

  // Récupérer tous les posts
  getPosts(): Observable<Post[]> {
    console.log('Tentative de récupération des posts depuis:', this.apiUrl);
    return this.http.get<Post[]>(this.apiUrl).pipe(
      tap(posts => console.log('Posts récupérés:', posts)),
      catchError(error => {
        console.error('Erreur lors de la récupération des posts:', error);
        return throwError(() => error);
      })
    );
  }

  // Récupérer un post par son ID
  getPostById(id: number): Observable<Post> {
    return this.http.get<Post>(`${this.apiUrl}/${id}`).pipe(
      catchError(error => {
        console.error(`Erreur lors de la récupération du post ${id}:`, error);
        return throwError(() => error);
      })
    );
  }

  // Créer un nouveau post
  createPost(post: Omit<Post, 'id' | 'date'>): Observable<Post> {
    return this.http.post<Post>(this.apiUrl, post).pipe(
      catchError(error => {
        console.error('Erreur lors de la création du post:', error);
        return throwError(() => error);
      })
    );
  }

  // Mettre à jour un post
  updatePost(id: number, post: Partial<Post>): Observable<Post> {
    return this.http.put<Post>(`${this.apiUrl}/${id}`, post).pipe(
      catchError(error => {
        console.error(`Erreur lors de la mise à jour du post ${id}:`, error);
        return throwError(() => error);
      })
    );
  }

  // Supprimer un post
  deletePost(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      catchError(error => {
        console.error(`Erreur lors de la suppression du post ${id}:`, error);
        return throwError(() => error);
      })
    );
  }
}
