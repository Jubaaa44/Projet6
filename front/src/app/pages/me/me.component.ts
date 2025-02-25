import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { SubjectService } from '../../services/subject.service';

@Component({
  selector: 'app-me',
  templateUrl: './me.component.html',
  styleUrls: ['./me.component.scss']
})
export class MeComponent implements OnInit {
  isMobileMenuOpen = false;
  currentUser: any = null;
  subscriptions: any[] = [];
  allSubjects: any[] = []; // Tous les sujets disponibles
  isSaving = false;
  userForm = {
    username: '',
    email: ''
  };

  constructor(
    private authService: AuthService,
    private subjectService: SubjectService,
    private router: Router
  ) { }

  ngOnInit(): void {
    // Récupère l'utilisateur courant
    this.currentUser = this.authService.currentUser;
    if (!this.currentUser) {
      this.router.navigate(['/login']);
      return;
    }

    // Initialise le formulaire avec les données actuelles
    this.userForm = {
      username: this.currentUser.username,
      email: this.currentUser.email
    };

    // Charge les abonnements de l'utilisateur
    this.loadSubscriptions();
  }

  loadSubscriptions(): void {
    // Récupère tous les sujets
    this.subjectService.getSubjects().subscribe({
      next: (subjects) => {
        this.allSubjects = subjects;

        // Filtre les sujets auxquels l'utilisateur est abonné
        if (this.currentUser && this.currentUser.subscriptionIds) {
          this.subscriptions = this.allSubjects.filter(subject =>
            this.currentUser.subscriptionIds.includes(subject.id)
          );
        } else {
          // Méthode alternative si les IDs d'abonnement ne sont pas dans l'utilisateur
          this.subscriptions = this.allSubjects.filter(subject =>
            subject.subscriberIds?.includes(this.currentUser.id)
          );
        }

        console.log('Abonnements récupérés:', this.subscriptions);
      },
      error: (error) => {
        console.error('Erreur lors du chargement des sujets:', error);
      }
    });
  }

  updateUsername(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.userForm.username = input.value;
  }

  updateEmail(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.userForm.email = input.value;
  }

  saveProfile(): void {
    if (this.isSaving) return;

    this.isSaving = true;

    // Met à jour le profil utilisateur
    this.authService.updateUserProfile({
      username: this.userForm.username,
      email: this.userForm.email
    }).subscribe({
      next: (updatedUser) => {
        this.currentUser = updatedUser;
        this.isSaving = false;
      },
      error: (error) => {
        console.error('Erreur lors de la mise à jour du profil:', error);
        this.isSaving = false;
      }
    });
  }

  unsubscribe(subjectId: number): void {
    this.subjectService.unsubscribeFromSubject(subjectId).subscribe({
      next: () => {
        // Filtre pour supprimer l'abonnement de la liste
        this.subscriptions = this.subscriptions.filter(sub => sub.id !== subjectId);

        // Met à jour les subscriptionIds de l'utilisateur si nécessaire
        if (this.currentUser && this.currentUser.subscriptionIds) {
          this.currentUser.subscriptionIds = this.currentUser.subscriptionIds
            .filter((id: number) => id !== subjectId);
        }
      },
      error: (error) => {
        console.error('Erreur lors du désabonnement:', error);
      }
    });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  toggleMobileMenu(): void {
    this.isMobileMenuOpen = !this.isMobileMenuOpen;
    document.body.style.overflow = this.isMobileMenuOpen ? 'hidden' : '';
  }

  closeMobileMenu(): void {
    this.isMobileMenuOpen = false;
    document.body.style.overflow = '';
  }
}
