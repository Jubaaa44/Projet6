export interface Comment {
  id?: number;
  content: string;
  date: string;  // LocalDateTime du backend sera reçu comme string
  authorId: number;
  authorUsername: string;
  postId: number;
}

export interface CreateCommentRequest {
  content: string;
  postId: number;
  // authorId sera ajouté par le backend via le token
}
