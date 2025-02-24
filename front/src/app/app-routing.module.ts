import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { PostsComponent } from './pages/posts/posts.component';
import { AuthGuard } from './guards/auth.guard';
import { SubjectsComponent } from './pages/subjects/subjects.component';
import { PostDetailComponent } from './pages/post-detail/post-detail.component';
import { CreatePostComponent } from './pages/create-post/create-post.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  {
    path: 'articles',
    component: PostsComponent,
    canActivate: [AuthGuard] // Protection de la route avec AuthGuard
  },
  {
    path: 'themes',
    component: SubjectsComponent,
    canActivate: [AuthGuard] // Protection de la route avec AuthGuard
  },
  {
    path: 'post/:id',
    component: PostDetailComponent,
    canActivate: [AuthGuard] // Protection de la route avec AuthGuard
  },
  {
    path: 'create-post',
    component: CreatePostComponent,
    canActivate: [AuthGuard] // Protection de la route avec AuthGuard
  },
  // Redirection si route inconnue
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
