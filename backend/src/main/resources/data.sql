-- This file will be executed automatically when ddl-auto is set to create or create-drop
-- Insert sample data for development

-- Insert sample users (passwords are encoded versions of 'password123')
INSERT INTO users (id, username, email, password, first_name, last_name, enabled, email_verified, created_at) VALUES
(1, 'john_doe', 'john@example.com', '$2a$10$X7VYx8FnwQsqJZyZ5x5Q8O7yL5K5X5X5X5X5X5X5X5X5X5X5X5', 'John', 'Doe', true, true, NOW()),
(2, 'jane_smith', 'jane@example.com', '$2a$10$X7VYx8FnwQsqJZyZ5x5Q8O7yL5K5X5X5X5X5X5X5X5X5X5X5X5', 'Jane', 'Smith', true, true, NOW());

-- Insert user roles
INSERT INTO user_roles (user_id, role) VALUES
(1, 'USER'),
(2, 'USER');

-- Insert sample tasks
INSERT INTO tasks (id, title, description, status, priority, due_date, category, user_id, created_at) VALUES
(1, 'Complete project documentation', 'Write comprehensive documentation for the task manager API', 'PENDING', 'HIGH', DATEADD('DAY', 7, NOW()), 'Work', 1, NOW()),
(2, 'Review pull requests', 'Review and merge pending PRs from team members', 'IN_PROGRESS', 'MEDIUM', DATEADD('DAY', 2, NOW()), 'Work', 1, NOW()),
(3, 'Buy groceries', 'Milk, eggs, bread, vegetables', 'PENDING', 'LOW', DATEADD('DAY', 1, NOW()), 'Personal', 2, NOW()),
(4, 'Gym workout', 'Cardio and strength training', 'COMPLETED', 'MEDIUM', DATEADD('DAY', -1, NOW()), 'Health', 2, NOW());

-- Reset sequence values
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
SELECT setval('tasks_id_seq', (SELECT MAX(id) FROM tasks));
