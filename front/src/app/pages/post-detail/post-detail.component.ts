import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PostService } from '../../services/post.service';
import { CommentService } from '../../services/comment.service';
import { AuthService } from '../../services/auth.service';
import { Post } from '../../interfaces/post.interface';
import { Comment } from '../../interfaces/comment.interface';

@Component({
  selector: 'app-post-detail',
  templateUrl: './post-detail.component.html',
  styleUrls: ['./post-detail.component.scss']
})
export class PostDetailComponent implements OnInit {
  post: Post | null = null;
  postId: number = 0;
  comments: Comment[] = [];
  newComment: string = '';
  isMobileMenuOpen = false;
  isLoading = true;
  error = false;
  errorMessage = '';
  isSubmittingComment = false;

  constructor(
    private route: ActivatedRoute,
    private postService: PostService,
    private commentService: CommentService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.postId = +params['id'];
      this.loadPost(this.postId);
      this.loadComments(this.postId);
    });
  }

  loadPost(postId: number): void {
    this.isLoading = true;
    this.error = false;

    this.postService.getPostById(postId).subscribe({
      next: (post) => {
        this.post = post;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Erreur lors du chargement de l\'article:', error);
        this.error = true;
        this.errorMessage = 'Une erreur est survenue lors du chargement de l\'article.';
        this.isLoading = false;
      }
    });
  }

  loadComments(postId: number): void {
    this.commentService.getCommentsByPostId(postId).subscribe({
      next: (comments) => {
        this.comments = comments;
      },
      error: (error) => {
        console.error('Erreur lors du chargement des commentaires:', error);
      }
    });
  }

  submitComment(): void {
    if (!this.newComment.trim() || this.isSubmittingComment) return;

    this.isSubmittingComment = true;
    const commentRequest = {
      content: this.newComment.trim(),
      postId: this.postId
    };

    this.commentService.createComment(commentRequest).subscribe({
      next: (comment) => {
        this.comments.push(comment);
        this.newComment = '';
        this.isSubmittingComment = false;
      },
      error: (error) => {
        console.error('Erreur lors de la création du commentaire:', error);
        this.isSubmittingComment = false;
      }
    });
  }

  get currentUser() {
    return this.authService.currentUser;
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
