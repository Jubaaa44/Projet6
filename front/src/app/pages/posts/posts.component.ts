import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PostService } from '../../services/post.service';
import { Post } from '../../interfaces/post.interface';

@Component({
  selector: 'app-posts',
  templateUrl: './posts.component.html',
  styleUrls: ['./posts.component.scss']
})
export class PostsComponent implements OnInit {
  posts: Post[] = [];
  isLoading = false;
  error = false;
  errorMessage = '';
  isMobileMenuOpen = false;

  // Variables pour le tri
  sortOptions = [
    { value: 'date-desc', label: 'Date (récente)' },
    { value: 'date-asc', label: 'Date (ancienne)' },
    { value: 'title', label: 'Titre' },
    { value: 'author', label: 'Auteur' }
  ];
  currentSort = 'date-desc';
  showSortOptions = false;

  constructor(
    private postService: PostService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.fetchPosts();
  }

  fetchPosts(): void {
    this.isLoading = true;
    this.error = false;

    this.postService.getPosts().subscribe({
      next: (posts) => {
        this.posts = posts;
        this.sortPosts(this.currentSort);
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Erreur lors de la récupération des posts:', error);
        this.error = true;
        this.errorMessage = 'Une erreur est survenue lors du chargement des articles.';
        this.isLoading = false;
      }
    });
  }

  // Méthode pour trier les posts
  sortPosts(sortType: string): void {
    this.currentSort = sortType;
    this.showSortOptions = false;

    switch (sortType) {
      case 'date-desc':
        this.posts.sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime());
        break;
      case 'date-asc':
        this.posts.sort((a, b) => new Date(a.date).getTime() - new Date(b.date).getTime());
        break;
      case 'title':
        this.posts.sort((a, b) => a.title.localeCompare(b.title));
        break;
      case 'author':
        this.posts.sort((a, b) => a.authorUsername.localeCompare(b.authorUsername));
        break;
      default:
        break;
    }
  }

  // Méthode pour basculer l'affichage des options de tri
  toggleSortOptions(): void {
    this.showSortOptions = !this.showSortOptions;
  }

  // Méthode pour fermer les options de tri si on clique ailleurs
  closeSortOptions(): void {
    this.showSortOptions = false;
  }

  // Obtenir le label d'option de tri actuelle
  getCurrentSortLabel(): string {
    const option = this.sortOptions.find(opt => opt.value === this.currentSort);
    return option ? option.label : '';
  }

  createPost(): void {
    this.router.navigate(['/create-post']);
  }

  viewPost(id: number): void {
    this.router.navigate(['/post', id]);
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

  formatDate(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleDateString('fr-FR', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  }

  getExcerpt(content: string, maxLength: number = 150): string {
    if (content.length <= maxLength) return content;
    return content.substring(0, maxLength) + '...';
  }
}
