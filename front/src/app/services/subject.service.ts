import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { Subject } from '../interfaces/subject.interface';

@Injectable({
  providedIn: 'root'
})
export class SubjectService {
  private apiUrl = 'http://localhost:8080/api/subjects';

  constructor(private http: HttpClient) {}

  // Récupérer tous les sujets
  getSubjects(): Observable<Subject[]> {
    console.log('Tentative de récupération des sujets depuis:', this.apiUrl);
    return this.http.get<Subject[]>(this.apiUrl).pipe(
      tap(subjects => console.log('Sujets récupérés:', subjects)),
      catchError(error => {
        console.error('Erreur lors de la récupération des sujets:', error);
        return throwError(() => error);
      })
    );
  }

  // Récupérer un sujet par son ID
  getSubjectById(id: number): Observable<Subject> {
    return this.http.get<Subject>(`${this.apiUrl}/${id}`).pipe(
      catchError(error => {
        console.error(`Erreur lors de la récupération du sujet ${id}:`, error);
        return throwError(() => error);
      })
    );
  }

  // Récupérer les abonnements de l'utilisateur connecté
  getSubscriptionsForUser(): Observable<Subject[]> {
    return this.http.get<Subject[]>(`${this.apiUrl}/subscriptions`).pipe(
      tap(subscriptions => console.log('Abonnements récupérés:', subscriptions)),
      catchError(error => {
        console.error('Erreur lors de la récupération des abonnements:', error);
        return throwError(() => error);
      })
    );
  }

  // Créer un nouveau sujet
  createSubject(subject: Omit<Subject, 'id' | 'subscriberCount' | 'postCount'>): Observable<Subject> {
    return this.http.post<Subject>(this.apiUrl, subject).pipe(
      catchError(error => {
        console.error('Erreur lors de la création du sujet:', error);
        return throwError(() => error);
      })
    );
  }

  // Mettre à jour un sujet
  updateSubject(id: number, subject: Partial<Subject>): Observable<Subject> {
    return this.http.put<Subject>(`${this.apiUrl}/${id}`, subject).pipe(
      catchError(error => {
        console.error(`Erreur lors de la mise à jour du sujet ${id}:`, error);
        return throwError(() => error);
      })
    );
  }

  // Supprimer un sujet
  deleteSubject(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      catchError(error => {
        console.error(`Erreur lors de la suppression du sujet ${id}:`, error);
        return throwError(() => error);
      })
    );
  }

  // S'abonner à un sujet
  subscribeToSubject(id: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${id}/subscribe`, {}).pipe(
      catchError(error => {
        console.error(`Erreur lors de l'abonnement au sujet ${id}:`, error);
        return throwError(() => error);
      })
    );
  }

  // Se désabonner d'un sujet
  unsubscribeFromSubject(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}/unsubscribe`).pipe(
      catchError(error => {
        console.error(`Erreur lors du désabonnement du sujet ${id}:`, error);
        return throwError(() => error);
      })
    );
  }

  // Récupérer les posts d'un sujet
  getSubjectPosts(id: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/${id}/posts`).pipe(
      catchError(error => {
        console.error(`Erreur lors de la récupération des posts du sujet ${id}:`, error);
        return throwError(() => error);
      })
    );
  }
}
