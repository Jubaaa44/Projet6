import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { PostService } from '../../services/post.service';
import { AuthService } from '../../services/auth.service';
import { SubjectService } from '../../services/subject.service';

@Component({
  selector: 'app-create-post',
  templateUrl: './create-post.component.html',
  styleUrls: ['./create-post.component.scss']
})
export class CreatePostComponent implements OnInit {
  postForm: FormGroup;
  themes: any[] = [];
  isSubmitting = false;
  isMobileMenuOpen = false;
  userAvatarUrl = '/assets/utilisateur.png'; // URL par défaut de l'avatar

  constructor(
    private formBuilder: FormBuilder,
    private postService: PostService,
    private subjectService: SubjectService,
    private authService: AuthService,
    private router: Router
  ) {
    this.postForm = this.formBuilder.group({
      title: ['', [Validators.required, Validators.minLength(3)]],
      content: ['', [Validators.required, Validators.minLength(10)]],
      themeId: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.loadThemes();
  }

  loadThemes(): void {
    this.subjectService.getSubjects().subscribe({
      next: (themes) => {
        this.themes = themes;
      },
      error: (error) => {
        console.error('Erreur lors du chargement des thèmes:', error);
      }
    });
  }

  onSubmit(): void {
    if (this.postForm.invalid || this.isSubmitting) {
      return;
    }

    this.isSubmitting = true;

    // Récupération des données du formulaire et de l'utilisateur
    const currentUser = this.authService.currentUser;
    const selectedThemeId = Number(this.postForm.value.themeId);
    const selectedTheme = this.themes.find(t => t.id === selectedThemeId);

    if (!currentUser) {
      console.error('Utilisateur non connecté');
      this.isSubmitting = false;
      return;
    }

    // Construction de l'objet post selon la structure attendue par le backend
    const postData = {
      title: this.postForm.value.title,
      content: this.postForm.value.content,
      // Ces valeurs seront ignorées par le backend mais sont nécessaires pour le typage
      authorId: currentUser.id,
      authorUsername: currentUser.username,
      // Le backend utilise ces valeurs pour associer le sujet
      subjectId: selectedThemeId,
      subjectName: selectedTheme?.name || ''
    };

    console.log('Données envoyées pour la création:', postData);

    this.postService.createPost(postData).subscribe({
      next: (post) => {
        console.log('Article créé avec succès:', post);
        this.isSubmitting = false;
        this.router.navigate(['/articles']);
      },
      error: (error) => {
        console.error('Erreur lors de la création de l\'article:', error);
        this.isSubmitting = false;
      }
    });
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
