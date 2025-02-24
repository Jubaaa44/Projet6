import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SubjectService } from '../../services/subject.service';
import { AuthService } from '../../services/auth.service';
import { Subject } from '../../interfaces/subject.interface';

@Component({
  selector: 'app-subjects',
  templateUrl: './subjects.component.html',
  styleUrls: ['./subjects.component.scss']
})
export class SubjectsComponent implements OnInit {
  subjects: Subject[] = [];
  isLoading = false;
  error = false;
  errorMessage = '';
  isMobileMenuOpen = false;

  constructor(
    private subjectService: SubjectService,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.fetchSubjects();
  }

  fetchSubjects(): void {
    this.isLoading = true;
    this.error = false;

    this.subjectService.getSubjects().subscribe({
      next: (subjects) => {
        this.subjects = subjects;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Erreur lors de la récupération des sujets:', error);
        this.error = true;
        this.errorMessage = 'Une erreur est survenue lors du chargement des thèmes.';
        this.isLoading = false;
      }
    });
  }

  viewSubject(id: number): void {
    this.router.navigate(['/subject', id]);
  }

  isSubscribed(subjectId: number): boolean {
    if (!this.authService.isLoggedIn) return false;

    const currentUser = this.authService.currentUser;
    if (!currentUser) return false;

    return this.subjects.find(subject =>
      subject.id === subjectId)?.subscriberIds?.includes(currentUser.id) || false;
  }

  toggleSubscription(subjectId: number, event: Event): void {
    event.stopPropagation();

    if (!this.authService.isLoggedIn) {
      this.router.navigate(['/login']);
      return;
    }

    const currentUser = this.authService.currentUser;
    if (!currentUser) return;

    if (this.isSubscribed(subjectId)) {
      this.subjectService.unsubscribeFromSubject(subjectId).subscribe({
        next: () => {
          const subject = this.subjects.find(s => s.id === subjectId);
          if (subject && subject.subscriberCount) {
            subject.subscriberCount--;
            if (subject.subscriberIds) {
              subject.subscriberIds = subject.subscriberIds.filter(id => id !== currentUser.id);
            }
          }
        },
        error: (error) => {
          console.error('Erreur lors du désabonnement:', error);
        }
      });
    } else {
      this.subjectService.subscribeToSubject(subjectId).subscribe({
        next: () => {
          const subject = this.subjects.find(s => s.id === subjectId);
          if (subject) {
            subject.subscriberCount = (subject.subscriberCount || 0) + 1;
            if (!subject.subscriberIds) {
              subject.subscriberIds = [];
            }
            subject.subscriberIds.push(currentUser.id);
          }
        },
        error: (error) => {
          console.error('Erreur lors de l\'abonnement:', error);
        }
      });
    }
  }

  toggleMobileMenu(): void {
    this.isMobileMenuOpen = !this.isMobileMenuOpen;
    const mobileMenu = document.querySelector('.mobile-menu');
    const overlay = document.querySelector('.mobile-menu-overlay');

    if (this.isMobileMenuOpen) {
      mobileMenu?.classList.add('active');
      overlay?.classList.add('active');
      document.body.style.overflow = 'hidden';
    } else {
      this.closeMobileMenu();
    }
  }

  closeMobileMenu(): void {
    this.isMobileMenuOpen = false;
    document.querySelector('.mobile-menu')?.classList.remove('active');
    document.querySelector('.mobile-menu-overlay')?.classList.remove('active');
    document.body.style.overflow = '';
  }

  getExcerpt(description: string, maxLength: number = 150): string {
    if (description.length <= maxLength) return description;
    return description.substring(0, maxLength) + '...';
  }
}
