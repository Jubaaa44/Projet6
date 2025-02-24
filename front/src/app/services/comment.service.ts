import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { Comment, CreateCommentRequest } from '../interfaces/comment.interface';

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  private apiUrl = 'http://localhost:8080/api/comments';

  constructor(private http: HttpClient) {}

  // Récupérer tous les commentaires d'un post
  getCommentsByPostId(postId: number): Observable<Comment[]> {
    const url = `${this.apiUrl}/post/${postId}`;
    return this.http.get<Comment[]>(url).pipe(
      catchError(error => {
        console.error('Erreur lors de la récupération des commentaires:', error);
        return throwError(() => error);
      })
    );
  }

  // Créer un nouveau commentaire
  createComment(comment: CreateCommentRequest): Observable<Comment> {
    const url = `${this.apiUrl}/post/${comment.postId}`;
    // Envoi uniquement du content puisque postId est dans l'URL
    // et authorId est géré par le backend
    const commentDTO = {
        content: comment.content
    };

    return this.http.post<Comment>(url, commentDTO).pipe(
        catchError(error => {
            console.error('Erreur lors de la création du commentaire:', error);
            return throwError(() => error);
        })
    );
}

  // Modifier un commentaire
  updateComment(id: number, content: string): Observable<Comment> {
    const url = `${this.apiUrl}/${id}`;
    const commentDTO = {
        content: content
    };

    return this.http.put<Comment>(url, commentDTO).pipe(
      catchError(error => {
        console.error('Erreur lors de la modification du commentaire:', error);
        return throwError(() => error);
      })
    );
  }

  // Supprimer un commentaire
  deleteComment(id: number): Observable<void> {
    const url = `${this.apiUrl}/${id}`;
    return this.http.delete<void>(url).pipe(
      catchError(error => {
        console.error('Erreur lors de la suppression du commentaire:', error);
        return throwError(() => error);
      })
    );
  }
}
