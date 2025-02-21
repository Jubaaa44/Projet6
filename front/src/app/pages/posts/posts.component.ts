import { Component, OnInit, HostListener } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, finalize } from 'rxjs/operators';
import { of } from 'rxjs';
import { PostService } from '../../services/post.service';
import { Post } from '../../interfaces/post.interface';

@Component({
  selector: 'app-posts',
  templateUrl: './posts.component.html',
  styleUrls: ['./posts.component.scss']
})
export class PostsComponent implements OnInit {
  posts: Post[] = [];
  sortOptions = ['Plus récent', 'Plus ancien', 'Alphabétique'];
  selectedSort = 'Plus récent';
  showSortDropdown = false;
  isMobileMenuOpen = false;
  isMobileView = false;
  isLoading = true;
  error = false;
  errorMessage = '';
  isEmpty = false;

  constructor(
    private router: Router,
    private postService: PostService
  ) { }

  ngOnInit(): void {
    // Récupérer les articles depuis la BDD
    this.fetchPosts();
    // Vérifie si on est en vue mobile
    this.checkMobileView();
  }

  @HostListener('window:resize')
  onResize() {
    this.checkMobileView();
  }

  checkMobileView(): void {
    this.isMobileView = window.innerWidth <= 768;
    // Applique la classe pour la vue mobile si nécessaire
    if (this.isMobileView) {
      document.querySelector('.articles-container')?.classList.add('mobile-view');
    } else {
      document.querySelector('.articles-container')?.classList.remove('mobile-view');
      // Ferme le menu mobile si on passe en mode desktop
      this.closeMobileMenu();
    }
  }

  toggleMobileMenu(): void {
    this.isMobileMenuOpen = !this.isMobileMenuOpen;
    const mobileMenu = document.querySelector('.mobile-menu');
    const overlay = document.querySelector('.mobile-menu-overlay');

    if (this.isMobileMenuOpen) {
      mobileMenu?.classList.add('active');
      overlay?.classList.add('active');
      // Empêche le défilement du body
      document.body.style.overflow = 'hidden';
    } else {
      this.closeMobileMenu();
    }
  }

  closeMobileMenu(): void {
    this.isMobileMenuOpen = false;
    document.querySelector('.mobile-menu')?.classList.remove('active');
    document.querySelector('.mobile-menu-overlay')?.classList.remove('active');
    // Réactive le défilement
    document.body.style.overflow = '';
  }

  fetchPosts(): void {
    this.isLoading = true;
    this.error = false;

    this.postService.getPosts()
      .pipe(
        catchError(err => {
          console.error('Erreur lors de la récupération des posts', err);
          this.error = true;
          this.errorMessage = "Une erreur est survenue lors du chargement des articles.";
          return of([]);
        }),
        finalize(() => {
          this.isLoading = false;
        })
      )
      .subscribe(posts => {
        this.posts = posts;
        console.log('Posts récupérés dans le composant:', posts);

        if (posts.length === 0) {
          this.isEmpty = true;
        } else {
          this.isEmpty = false;
          // Trier par défaut selon le plus récent
          this.sortPosts();
        }
      });
  }

  toggleSortDropdown(): void {
    this.showSortDropdown = !this.showSortDropdown;
  }

  selectSortOption(option: string): void {
    this.selectedSort = option;
    this.showSortDropdown = false;
    this.sortPosts();
  }

  sortPosts(): void {
    if (!this.posts.length) return;

    switch (this.selectedSort) {
      case 'Plus récent':
        // Tri par date décroissante (plus récent d'abord)
        this.posts.sort((a, b) =>
          new Date(b.date).getTime() - new Date(a.date).getTime()
        );
        break;
      case 'Plus ancien':
        // Tri par date croissante (plus ancien d'abord)
        this.posts.sort((a, b) =>
          new Date(a.date).getTime() - new Date(b.date).getTime()
        );
        break;
      case 'Alphabétique':
        // Tri par titre alphabétiquement
        this.posts.sort((a, b) => a.title.localeCompare(b.title));
        break;
    }
  }

  createPost(): void {
    this.router.navigate(['/create-post']);
  }

  viewPost(id: number): void {
    this.router.navigate(['/post', id]);
  }

  // Formate la date pour l'affichage
  formatDate(dateString: string): string {
    const date = new Date(dateString);
    const options: Intl.DateTimeFormatOptions = {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    };
    return date.toLocaleDateString('fr-FR', options);
  }

  // Tronque le contenu pour l'extrait
  getExcerpt(content: string, maxLength: number = 150): string {
    if (content.length <= maxLength) return content;
    return content.substring(0, maxLength) + '...';
  }
}
