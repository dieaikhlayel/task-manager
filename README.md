# Task Manager Application

![Project Status](https://img.shields.io/badge/status-in%20progress-blue)
![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green)
![React](https://img.shields.io/badge/React-18-61DAFB)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14-blue)

A full-stack task management application that allows users to **create, update, delete, and organize tasks**.  
Built with **React** for the frontend and **Java Spring Boot** for the backend, featuring JWT authentication and PostgreSQL database.

---

## 🌟 Features

- **Authentication**
  - Secure user login & registration
  - JWT-based token authentication

- **Task Management**
  - Create, read, update, and delete tasks (CRUD)
  - Assign categories and priorities
  - Real-time updates (optional via WebSocket)

- **User Experience**
  - Responsive design
  - Easy-to-use UI
  - Error handling with clear messages

- **Technical Features**
  - RESTful API
  - Spring Data JPA for database access
  - Role-based security with Spring Security
  - PostgreSQL integration
  - Frontend state management with Redux Toolkit

---

## 🛠️ Tech Stack

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

---

## 📁 Project Structure

```bash
task-manager/
├── backend/     # Spring Boot application
│   ├── src/main/java/com/taskmanager/
│   │   ├── controller
│   │   ├── service
│   │   ├── repository
│   │   ├── model
│   │   ├── dto
│   │   ├── config
│   │   ├── security
│   │   ├── exception
│   │   └── util
│   ├── src/main/resources/
│   │   └── db/migration
│   └── pom.xml
├── frontend/    # React application
│   ├── src/
│   │   ├── components/
│   │   ├── pages/
│   │   ├── redux/
│   │   ├── App.jsx
│   │   └── main.jsx
│   └── package.json
└── README.md
```

---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Node.js 18+
- PostgreSQL 14+
- Maven (or use the Maven wrapper)

---

### Backend Setup

1. Navigate to the backend:

```bash
cd backend
```

2. Install dependencies (Maven handles this automatically).

3. Configure PostgreSQL:

- Create databases:

```sql
CREATE DATABASE taskmanager_db;
CREATE DATABASE taskmanager_dev;
```

- Update `application.yml` with your database credentials.

4. Run the backend:

```bash
# Using Maven wrapper
./mvnw spring-boot:run

# Or if Maven is installed
mvn spring-boot:run
```

5. Verify backend is running:

Open browser: [http://localhost:8080/api/test](http://localhost:8080/api/test)  
Expected response: `Task Manager Backend is running!`

---

### Frontend Setup

1. Navigate to frontend:

```bash
cd frontend
```

2. Install dependencies:

```bash
npm install
```

3. Run the frontend dev server:

```bash
npm run dev
```

4. Open in browser:

```
http://localhost:5173
```

---

## 📡 API Endpoints

### Authentication

- `POST /api/auth/register` – Register a new user  
- `POST /api/auth/login` – Login and receive JWT token  

### Tasks

- `GET /api/tasks` – Retrieve all tasks  
- `POST /api/tasks` – Create a new task  
- `PUT /api/tasks/{id}` – Update a task  
- `DELETE /api/tasks/{id}` – Delete a task  

> All `/api/tasks` routes require Authorization header with JWT:  
> `Authorization: Bearer <token>`

---

## 📝 Shell Script to Initialize Backend Structure (Optional)

```bash
cat > setup-backend.sh << 'EOF'
#!/bin/bash

echo "🚀 Setting up Task Manager Backend..."

mkdir -p backend/src/main/java/com/taskmanager/{controller,service,repository,model,dto,config,security,exception,util}
mkdir -p backend/src/main/resources/db/migration
mkdir -p backend/src/test/java/com/taskmanager/{controller,service,repository}

chmod +x setup-backend.sh

echo "✅ Backend structure created!"
echo "Next steps:"
echo "1. cd backend"
echo "2. ./mvnw spring-boot:run"
echo "3. Test the API at http://localhost:8080/api/test"
EOF

chmod +x setup-backend.sh
```

---

## ✅ Notes & Tips

1. Ensure PostgreSQL is running before starting backend.  
2. Check that credentials in `application.yml` match your PostgreSQL setup.  
3. If ports conflict, adjust `server.port` in `application.yml` (default: 8080).  

---

## 🌍 Deployment (Optional)

- Frontend: Vercel / Netlify  
- Backend: Heroku / Render / Railway  

Make sure to update frontend `.env` with backend URL for production.

---

## 📌 Future Improvements

- WebSocket real-time task updates  
- Task reminders/notifications  
- User roles & permissions  
- File attachments for tasks  
- Improved UI/UX with Tailwind or Material UI

---

## 📄 License

MIT License

---

## 🙌 Acknowledgements

- Inspired by full-stack project challenges  
- JWT auth implemented following official Spring Security guidelines
