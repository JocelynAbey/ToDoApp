# 📌 Project Requirement Document  
# TODO Application (Hierarchical Task Management System)

---

# 1. Project Overview

## 1.1 Objective

Design and develop a modern, extensible TODO application using:

- **Spring Boot** for backend services
- **React JS** for frontend
- **Material Design UI**
- Support for **light and dark theme toggle**
- Hierarchical task structure (tasks and subtasks unified)

The system must be designed with clean architecture principles and be scalable for future enhancements such as authentication, multi-user support, and collaboration features.

---

# 2. Technology Stack

## 2.1 Backend

- Java 17+
- Spring Boot
- Spring Data JPA (Hibernate)
- H2 Database (development)
- Production DB (PostgreSQL/MySQL) via profiles
- RESTful API architecture
- Maven or Gradle build system
- JUnit 5 + Mockito for testing

## 2.2 Frontend

- React JS (latest stable version)
- Material UI (MUI)
- Axios (API communication)
- React Router (if needed)
- LocalStorage for theme persistence

---

# 3. System Architecture

The application will follow a layered architecture:

### Backend Layers

- Controller Layer (REST endpoints)
- Service Layer (business logic)
- Repository Layer (JPA data access)
- DTO layer for request/response mapping
- Global exception handling

### Frontend Structure

- Component-based architecture
- State management (React state / Context API)
- API service layer abstraction
- Theme provider wrapper

---

# 4. Core Functional Requirements (MVP Scope)

---

# 4.1 List Management

## 4.1.1 Features

- Support multiple task lists
- Predefined fixed lists:
  - Shopping
  - Personal
  - Work
- Users can:
  - Create new custom lists
  - Rename lists
  - Delete lists (fixed lists optionally protected)

## 4.1.2 List Fields

- id
- name
- createdAt
- updatedAt
- isSystemDefined (boolean)

---

# 4.2 Task Management

## 4.2.1 Task Model

Tasks and subtasks are unified under a **single Task entity**.

Each task must support:

- id
- title (mandatory)
- description (optional)
- completed (boolean)
- dueDate (optional)
- priority (LOW, MEDIUM, HIGH)
- notes (optional)
- location (optional)
- meetingLink (optional URL)
- listId (foreign key)
- parentTaskId (nullable self-reference)
- createdAt
- updatedAt

---

# 4.3 Hierarchical Task Structure

## 4.3.1 Design Principles

- A task may have zero or one parent.
- A task may have zero or many child tasks.
- Unlimited nesting depth allowed.
- Subtasks are simply tasks with a parentTaskId.

## 4.3.2 Behavior Rules

- If parentTaskId is null → Top-level task
- If parentTaskId is not null → Subtask
- System must prevent circular references:
  - A task cannot become a child of itself.
  - A task cannot become a child of its own descendant.

---

# 4.4 Task Movement Capabilities

The system must support:

- Convert top-level task → subtask
- Promote subtask → top-level task
- Move subtask between different parent tasks
- Move tasks between lists (optional MVP enhancement)

---

# 4.5 Task Completion Logic

For MVP:

- Parent task completion is independent of child tasks.
- No automatic cascading behavior required.

Future Enhancement:

- Optional auto-completion when all subtasks are completed.

---

# 4.6 Smart Views

The application must support predefined filtered views:

- Today (tasks due today)
- This Week (tasks due within current week)
- Overdue
- All Tasks
- By Priority (optional)

Smart views must flatten hierarchical structure for filtering purposes.

---

# 4.7 Calendar View

## 4.7.1 Requirements

- Monthly calendar layout
- Tasks displayed based on dueDate
- Clicking a date shows associated tasks
- Tasks without due date excluded from calendar view

---

# 5. Non-Functional Requirements

---

# 5.1 Performance

- API response time under 200ms for standard operations
- Efficient handling of hierarchical queries
- Pagination support for large task lists

---

# 5.2 Maintainability

- Clean code principles
- Clear naming conventions
- Proper logging
- Modular frontend structure
- DTO usage (do not expose JPA entities directly)

---

# 5.3 Testing

## 5.3.1 Backend Testing

- Unit tests for service layer
- Controller tests using MockMvc
- Repository tests (optional)
- Target test coverage: 80–90%+

## 5.3.2 Frontend Testing (Optional MVP)

- Component-level testing using Jest/React Testing Library

---

# 5.4 Security (Future Scope)

- JWT-based authentication
- Multi-user support
- Role-based access control
- Per-user task isolation

---

# 5.5 Scalability

- Designed to support:
  - Multi-user environments
  - Cloud deployment
  - Containerization (Docker)
  - Horizontal scaling

---

# 6. Database Design

## 6.1 Task Table

- id (PK)
- title
- description
- completed
- due_date
- priority
- notes
- location
- meeting_link
- list_id (FK)
- parent_task_id (FK self-reference)
- created_at
- updated_at

## 6.2 List Table

- id (PK)
- name
- is_system_defined
- created_at
- updated_at

---

# 7. API Requirements

The backend must expose REST endpoints for:

## Lists

- Create list
- Get all lists
- Update list
- Delete list

## Tasks

- Create task
- Update task
- Delete task
- Move task (change parentTaskId)
- Fetch tasks by list
- Fetch hierarchical task tree
- Fetch filtered tasks (today, week, overdue)

---

# 8. UI Requirements

## 8.1 Material Design

- Use Material UI components
- Clean and modern interface
- Responsive layout

## 8.2 Theme Support

- Light mode
- Dark mode
- Toggle switch in UI
- Persist user preference in localStorage
- Optional: detect system theme

---

# 9. Deployment Strategy

- Single Spring Boot JAR
- React build output served from `/static`
- Profile-based database configuration
- Production build configuration
- Docker support (future enhancement)

---

# 10. Future Enhancements

- User authentication
- Task sharing
- Notifications & reminders
- Recurring tasks
- Drag-and-drop hierarchy
- Tags and advanced filtering
- Attachments
- Search functionality
- Mobile application

---

# 11. Deliverables

- Source code repository
- API documentation (Swagger/OpenAPI)
- Test coverage report
- Setup and run instructions
- Production build instructions

---

# End of Document