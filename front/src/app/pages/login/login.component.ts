import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService, LoginRequest } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  isSubmitting = false;
  loginError: string | null = null;
  returnUrl: string = '/articles';

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    // Récupérer l'URL de retour des query params ou utiliser la valeur par défaut
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/articles';

    // Si l'utilisateur est déjà connecté, rediriger vers la page d'articles
    if (this.authService.isLoggedIn) {
      this.router.navigate(['/articles']);
    }
  }

  onSubmit(): void {
    this.loginError = null;

    if (this.loginForm.invalid) {
      return;
    }

    this.isSubmitting = true;

    const loginRequest: LoginRequest = {
      email: this.loginForm.value.email,
      password: this.loginForm.value.password
    };

    this.authService.login(loginRequest).subscribe({
      next: () => {
        // Redirection vers la page d'articles ou l'URL de retour après connexion réussie
        this.router.navigateByUrl(this.returnUrl);
      },
      error: (error) => {
        console.error('Login error:', error);
        this.loginError = error.message || 'Une erreur est survenue lors de la connexion';
        this.isSubmitting = false;
      }
    });
  }
}
