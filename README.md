# 📊 Enterprise Asset Tracker System

A modern, full-stack, containerized web application designed for tracking, managing, and auditing organizational assets in real-time. Built with a robust **Spring Boot** REST API, a reactive **Vue.js 3** frontend, and powered by a cloud-native **TiDB (MySQL-compatible)** distributed database. The entire ecosystem is fully dockerized for seamless, one-command local development and cloud deployment.

---

## 🚀 Key Features

* **Full-Stack Architecture:** Decoupled architecture featuring a secure Spring Boot RESTful API backend and a responsive Vue 3 single-page application (SPA).
* **Cloud-Native Database:** Integrated with **TiDB Cloud** (Distributed SQL / MySQL compatible) ensuring high availability, scalable transactions, and robust data persistence.
* **Containerized Deployment:** Fully Dockerized using multi-stage builds (`Dockerfile` for API and UI) and orchestrated with **Docker Compose** behind an **Nginx** reverse proxy.
* **Secure Authentication & Routing:** Protected endpoints and smooth SPA navigation configured via Vue Router (HTML5 History Mode) with custom Nginx fallback routing.
* **Real-time Asset Management:** Full CRUD capabilities for IT equipment, hardware, and corporate resources with instant status updates and auditing.
* **Automated Build Pipeline:** Multi-stage Docker builds using Maven and Node.js Alpine images for minimal container footprint and ultra-fast build times.

---

## 🛠️ Tech Stack

### **Backend**
* **Language/Framework:** Java 21 / Spring Boot 3
* **Database Access:** Spring Data JPA / Hibernate
* **Build Tool:** Apache Maven
* **Database:** TiDB Cloud / MySQL 8.0

### **Frontend**
* **Framework:** Vue.js 3 (TypeScript)
* **Web Server / Proxy:** Nginx (Alpine)
* **HTTP Client:** Axios

### **DevOps & Infrastructure**
* **Containerization:** Docker & Docker Compose
* **Cloud Hosting:** Railway Ready

---

## 💻 How to Run (Docker)

The entire application (Backend, Frontend, and Nginx Reverse Proxy) can be launched with a single command without needing Java, Node.js, or local database installations.

### **Prerequisites**
* [Docker Desktop](https://www.docker.com/products/docker-desktop/) installed and running.

### **Quick Start**

docker compose up --build

## 📦 Project Structure

```text
asset-tracker/
├── api/                        # Spring Boot Backend Service
│   ├── src/                    # Java source code & application properties
│   ├── pom.xml                 # Maven dependencies
│   └── Dockerfile              # Multi-stage Java 21 build definition
├── ui/                         # Vue.js Frontend Service
│   ├── src/                    # Vue components, router & Axios setup
│   ├── nginx.conf              # Nginx reverse proxy & SPA routing setup
│   ├── package.json            # Node.js dependencies
│   └── Dockerfile              # Multi-stage Node/Nginx build definition
└── docker-compose.yml          # Multi-container orchestration config
