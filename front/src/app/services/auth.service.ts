import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { Router } from '@angular/router';

export interface User {
  id: number;
  username: string;
  email: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface JwtResponse {
  token: string;
  id: number;
  username: string;
  email: string;
}

export interface RegisterCredentials {
  username: string;
  email: string;
  password: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();
  private tokenKey = 'auth_token';

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
    this.loadUserFromStorage();
  }

  private loadUserFromStorage(): void {
    const token = localStorage.getItem(this.tokenKey);
    if (token) {
      try {
        // Récupérer les informations utilisateur du localStorage si disponibles
        const userStr = localStorage.getItem('user');
        if (userStr) {
          const user = JSON.parse(userStr);
          this.currentUserSubject.next(user);
        } else {
          // Sinon, récupérer le profil utilisateur
          this.getUserProfile().subscribe();
        }
      } catch (e) {
        console.error('Erreur lors du chargement des données utilisateur:', e);
        this.logout();
      }
    }
  }

  get currentUser(): User | null {
    return this.currentUserSubject.value;
  }

  get isLoggedIn(): boolean {
    return !!this.getAuthToken();
  }

  register(credentials: RegisterCredentials): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/register`, credentials)
      .pipe(
        catchError(error => {
          console.error('Erreur d\'inscription:', error);
          return throwError(() => new Error(error.error?.message || 'Erreur lors de l\'inscription'));
        })
      );
  }

  login(loginRequest: LoginRequest): Observable<User> {
    return this.http.post<JwtResponse>(`${this.apiUrl}/login`, loginRequest)
      .pipe(
        tap(response => {
          // Stocker le token JWT
          localStorage.setItem(this.tokenKey, response.token);

          // Créer et stocker l'objet utilisateur
          const user: User = {
            id: response.id,
            username: response.username,
            email: response.email
          };

          // Sauvegarder l'utilisateur dans le localStorage pour les chargements futurs
          localStorage.setItem('user', JSON.stringify(user));

          // Mettre à jour le sujet BehaviorSubject
          this.currentUserSubject.next(user);
        }),
        map(response => {
          return {
            id: response.id,
            username: response.username,
            email: response.email
          };
        }),
        catchError(error => {
          console.error('Erreur de connexion:', error);
          return throwError(() => new Error(error.error?.message || 'Identifiants invalides'));
        })
      );
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem('user');
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  getAuthToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  // Récupérer le profil utilisateur
  getUserProfile(): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/profile`)
      .pipe(
        tap(user => {
          this.currentUserSubject.next(user);
          localStorage.setItem('user', JSON.stringify(user));
        }),
        catchError(error => {
          if (error.status === 401) {
            this.logout();
          }
          return throwError(() => new Error('Erreur lors de la récupération du profil'));
        })
      );
  }

  // Mettre à jour le profil utilisateur
  updateUserProfile(userData: Partial<User>): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/profile`, userData)
      .pipe(
        tap(updatedUser => {
          // Mettre à jour l'utilisateur dans le state et le localStorage
          this.currentUserSubject.next(updatedUser);
          localStorage.setItem('user', JSON.stringify(updatedUser));
        }),
        catchError(error => {
          return throwError(() => new Error('Erreur lors de la mise à jour du profil'));
        })
      );
  }

  // Pour vérifier si le token est toujours valide
  validateToken(): Observable<boolean> {
    return this.getUserProfile()
      .pipe(
        map(() => true),
        catchError(() => {
          this.logout();
          return throwError(() => new Error('Session expirée'));
        })
      );
  }
}
