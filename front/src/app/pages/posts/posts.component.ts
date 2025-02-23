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
