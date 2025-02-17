-- Insertion des sujets
INSERT INTO subjects (name) VALUES 
('Java'),
('Spring'),
('Angular'),
('TypeScript'),
('Git');

-- Insertion des utilisateurs
INSERT INTO users (email, password, username) VALUES 
('john@test.com', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqaFS', 'john_doe'),
('jane@test.com', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqaFS', 'jane_doe');

-- Insertion des posts
INSERT INTO posts (title, content, date, author_id, subject_id) VALUES 
('Introduction à Spring Boot', 'Spring Boot simplifie le développement...', '2024-02-14 10:00:00', 1, 2),
('Les bases d''Angular', 'Angular est un framework...', '2024-02-14 11:00:00', 2, 3);

-- Insertion des commentaires
INSERT INTO comments (content, date, author_id, post_id) VALUES 
('Super article !', '2024-02-14 12:00:00', 2, 1),
('Merci pour ces explications', '2024-02-14 13:00:00', 1, 2);

-- Abonnements des utilisateurs aux sujets
INSERT INTO user_subscriptions (user_id, subject_id) VALUES 
(1, 1), (1, 2), -- John suit Java et Spring
(2, 2), (2, 3); -- Jane suit Spring et Angular