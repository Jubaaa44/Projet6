export interface Post {
  id: number;
  title: string;
  content: string;
  date: string;
  authorId: number;
  authorUsername: string;
  subjectId: number;
  subjectName: string;
  comments?: any[];  // Si on utilise les commentaires
}
