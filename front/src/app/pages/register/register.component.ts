import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  registerForm: FormGroup;
  isSubmitting = false;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.registerForm = this.formBuilder.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  ngOnInit(): void {
    // Si l'utilisateur est déjà connecté, rediriger vers la page d'articles
    if (this.authService.isLoggedIn) {
      this.router.navigate(['/articles']);
    }
  }

  onSubmit(): void {
    if (this.registerForm.invalid) {
      return;
    }

    this.isSubmitting = true;
    const credentials = this.registerForm.value;

    this.authService.register(credentials).subscribe({
      next: (user) => {
        // Après inscription réussie, connecter automatiquement l'utilisateur
        this.authService.login({
          email: credentials.email,
          password: credentials.password
        }).subscribe({
          next: () => {
            // Puis rediriger vers la page d'articles
            this.router.navigate(['/articles']);
          },
          error: (loginError) => {
            console.error('Auto-login error after registration:', loginError);
            // En cas d'échec de connexion automatique, rediriger vers la page de connexion
            this.router.navigate(['/login']);
          }
        });
      },
      error: (error) => {
        console.error('Registration error:', error);
        this.isSubmitting = false;
      }
    });
  }
}
