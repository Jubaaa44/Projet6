export interface Subject {
  id: number;
  name: string;
  description: string;
  postIds?: number[];
  subscriberIds?: number[];
  subscriberCount: number;
  postCount: number;
  date?: string; // Pour la compatibilité avec l'affichage des dates dans le composant
  authorUsername?: string; // Pour la compatibilité avec l'affichage de l'auteur dans le composant
}
