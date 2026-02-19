# Task Manager Application

A full-stack task management application built with React and Java Spring Boot.

## Tech Stack

### Backend

- Java 17
- Spring Boot 3.x
- Spring Security with JWT
- Spring Data JPA
- PostgreSQL
- Maven

### Frontend

- React 18
- Redux Toolkit
- React Router
- Axios
- Tailwind CSS (optional)

## Project Structure

task-manager/
├── backend/ # Spring Boot application
|
├── frontend/ # React application
|
└── README.md

## Getting Started

### Prerequisites

- Java 17 or higher
- Node.js 18 or higher
- PostgreSQL 14 or higher
- Maven

### Backend Setup

cd backend
./mvnw spring-boot:run

### Frontend Setup

cd frontend
npm install
npm run dev

### Features

- User authentication with JWT
- Create, read, update, and delete tasks
- Responsive design
- Real-time updates
- Task categories and priorities

### API Documentation

API documentation will be available at /swagger-ui.html when running.

### 11. Create a Shell Script to Run Everything (Optional)

# Create a helper script

cat > setup-backend.sh << 'EOF'
#!/bin/bash

echo "🚀 Setting up Task Manager Backend..."

# Create directory structure

echo "📁 Creating directory structure..."
mkdir -p backend/src/main/java/com/taskmanager/{controller,service,repository,model,dto,config,security,exception,util}
mkdir -p backend/src/main/resources/db/migration
mkdir -p backend/src/test/java/com/taskmanager/{controller,service,repository}

# Make the script executable

chmod +x setup-backend.sh

echo "✅ Backend structure created successfully!"
echo ""
echo "Next steps:"
echo "1. cd backend"
echo "2. ./mvnw spring-boot:run"
echo "3. Test the API at http://localhost:8080/api/test"
EOF

chmod +x setup-backend.sh

### 🚀 Running the Backend

Now that we have the structure, let's run it:

# Navigate to backend

cd backend

# If you have Maven installed

mvn spring-boot:run

# Or using the wrapper

./mvnw spring-boot:run

### ✅ Verify It Works

Once running, open your browser and go to:
http://localhost:8080/api/test

You should see: Task Manager Backend is running!

### 📝 Important Notes

1. Make sure PostgreSQL is installed and running before starting the backend
2. Create the database in PostgreSQL:
   CREATE DATABASE taskmanager_db;
   CREATE DATABASE taskmanager_dev;

3. If you get connection errors, check:

- PostgreSQL is running: sudo service postgresql status (Linux) or check Services (Windows)
- Database credentials in application.yml match your PostgreSQL setup
- Database exists: psql -U postgres -c "\l"
