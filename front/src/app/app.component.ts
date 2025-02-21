import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'MDD Application';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    // Surveiller les changements de route
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: any) => {
      // Vérifier si on a un token au chargement
      if (this.authService.getAuthToken()) {
        // Valider optionnellement le token côté serveur
        this.authService.validateToken().subscribe({
          error: () => {
            // Si le token est invalide, rediriger vers la connexion
            this.router.navigate(['/login']);
          }
        });
      }
    });
  }
}
