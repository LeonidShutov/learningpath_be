# Learning Path Generator

This project is a web service designed to help software engineers prepare for job interviews by generating personalized,
day-by-day study plans.

---

### Vision & Purpose

The goal is to provide structure and a clear path for interview preparation. Users can input their current skill level
and the amount of time they have available to study, and the service will generate a tailored plan. The plan is created
using a combination of a curated knowledge base of resources and AI-powered personalization.

### Core Features

* **Personalized Plan Generation**: Creates study plans based on user's expertise and available time.
* **Curated Knowledge Base**: A database of high-quality resources like books, articles, and coding tasks.
* **AI-Powered Personalization**: Uses AI to tailor the study plan to the user's needs.
* **User Accounts**: Users can sign up, log in, and track their progress.
* **Email Notifications**: Transactional emails for events like registration and password resets.
* **(Optional) Calendar Integration**: Export study plans to Google Calendar.

### Technology Stack

#### Backend

* **Java 21** & **Spring Boot 3**
* **Spring Security** for authentication
* **Spring Data JPA** & **Hibernate** for database access
* **PostgreSQL** as the primary database
* **Redis** for caching
* **OpenAI GPT-4-Turbo** for AI-powered features

#### Frontend

* Server-rendered views with **Spring MVC** and **Thymeleaf**
* **Bootstrap 5** for responsive UI

#### Infrastructure & DevOps

* **Docker & Docker Compose** for containerization
* **NGINX** as a reverse proxy
* **GitHub Actions** for CI/CD

### Project Structure

The project is a monolith, with the Spring Boot application containing both the backend logic and the server-rendered
frontend views. The `/learningpath_be` directory contains the entire application.
