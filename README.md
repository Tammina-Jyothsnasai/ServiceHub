# ServiceHub: Smart Service Request & Resolution Management Platform

ServiceHub is a modern, full-stack enterprise web application designed for colleges, offices, residential hostels, apartments, and corporate facilities to manage complaints and service requests end-to-end.

---

## 🌟 Key Features

### User Role (Members / Staff)
- **User Authentication**: Secure Registration & Login with **BCrypt password hashing**.
- **Interactive User Dashboard**: View key metric cards (*Total, Pending, In Progress, Resolved*) and recent service requests.
- **Smart Service Request Creation**:
  - Request fields: Title, Category (*Electrical, Internet, Plumbing, Cleaning, Maintenance, IT, Furniture, Other*), Location/Room Number, Description, Priority.
  - **Automatic Priority Suggestion Engine**: Rule-based Java keyword analyzer parses issue descriptions in real-time to recommend priorities (*URGENT, HIGH, MEDIUM, LOW*).
- **Search & Multi-Filter Requests**: Search tickets by ID, title, or location; filter by status, priority, and category.
- **Visual Status Timeline Stepper**: Track ticket lifecycles (*Created → Pending → Assigned → In Progress → Resolved*).
- **Customer Service Feedback System**: Submit 5-star ratings and comments upon resolution. Prevents duplicate submissions.
- **In-App Notification Center**: Instant notifications for ticket creation, staff assignment, and status updates.

### Admin Role (Facility Managers)
- **Admin Analytics Dashboard**: Real-time summary statistics cards + **3 Interactive Chart.js Graphs** (*Requests by Category, Status Distribution, Priority Breakdown*).
- **Service Request Management**: View all requests, assign specialized technician staff members, change priority, update statuses, and attach resolution work notes.
- **Staff Technician Management**: Full CRUD operations (*Add staff, Edit details, Select department, Deactivate staff*).
- **User Directory**: Overview of all registered users and system administrators.
- **Feedback & Rating Reviews**: Monitor customer feedback and average satisfaction rating metrics.

---

## 🛠️ Technology Stack

- **Frontend**: HTML5, CSS3 (Custom SaaS Design System), JavaScript (ES6+), Bootstrap 5.3, Bootstrap Icons, Chart.js.
- **Backend**: Java 23 / 17, Spring Boot 3.2, Spring Web, Spring Data JPA, Hibernate ORM, Maven.
- **Database**: MySQL 8.0 (`servicehub_db`).
- **Security**: BCrypt Password Hashing (`PasswordEncoder`).

---

## 📁 Project Folder Structure

```
ServiceHub/
├── backend/
│   ├── src/main/java/com/servicehub/
│   │   ├── config/              # CORS & PasswordEncoder beans
│   │   ├── controller/          # REST Controllers
│   │   ├── dto/                 # Request & Response Data Transfer Objects
│   │   ├── entity/              # JPA Entities (User, Staff, ServiceRequest, etc.)
│   │   ├── enums/               # Java Enums (Role, Category, Priority, RequestStatus, etc.)
│   │   ├── exception/           # Global Exception Handler & Custom Exceptions
│   │   ├── repository/          # Spring Data JPA Repositories
│   │   ├── service/             # Business Logic & Rule-based Priority Suggestion Engine
│   │   └── ServiceHubApplication.java
│   ├── src/main/resources/
│   │   └── application.properties  # Database URL & server configuration
│   └── pom.xml
├── frontend/
│   ├── index.html               # Landing page
│   ├── login.html               # Login page
│   ├── register.html            # Registration page
│   ├── dashboard.html           # User dashboard
│   ├── create-request.html      # Create request page with AI Priority Suggestion
│   ├── my-requests.html         # User requests filterable table
│   ├── request-details.html     # Ticket timeline stepper & feedback form
│   ├── profile.html             # User profile page
│   ├── admin-dashboard.html     # Admin dashboard with Chart.js graphs
│   ├── admin-requests.html      # Admin ticket management & staff assignment modal
│   ├── admin-staff.html        # Staff directory CRUD modal
│   ├── admin-users.html        # Registered users list
│   ├── admin-feedback.html     # Feedback & reviews overview
│   ├── css/
│   │   └── style.css            # SaaS design system & badge styling
│   └── js/
│       ├── api.js               # API client wrapper & Toast notification engine
│       └── auth.js              # Auth & session management helpers
├── database/
│   └── schema.sql               # MySQL DDL script
└── README.md
```

---

## ⚙️ Configuration & Setup

### 1. Zero-Config Database (H2 Embedded / MySQL Optional)
By default, the project uses an embedded in-memory H2 database that starts automatically with pre-seeded demo data (Admin, Users, Staff, Sample Tickets). No manual database setup or password is required!

If you wish to switch to MySQL, simply configure `backend/src/main/resources/application.properties` with your MySQL credentials.

---

### 2. How to Build and Run (Easiest Method: 1-Click Scripts)

In the root `ServiceHub/` folder:

1. **To Run (Backend + Frontend together)**:
   - Double-click **`run.bat`** (or execute `.\run.bat` in terminal).
   - This starts the Spring Boot server and automatically opens **`http://localhost:8080/`** in your browser.
   - Everything (Frontend & Backend API) runs seamlessly together on port 8080!

2. **To Rebuild Project**:
   - Double-click **`build.bat`** (or execute `.\build.bat` in terminal).
   - This copies the frontend assets and packages an updated executable JAR into `backend/target/servicehub-backend-1.0.0.jar`.

---

### 3. Alternative Manual Execution

```bash
# Run the built JAR directly
java -jar backend/target/servicehub-backend-1.0.0.jar

# Or rebuild with Maven using the local portable Maven
& "C:\Users\H6jyo\mvn-bin\apache-maven-3.9.6\bin\mvn.cmd" package -DskipTests
```

Once running, access the web app at **`http://localhost:8080/`** (or open `frontend/index.html` in your browser).

---

## 🔑 Default Demo Credentials

| Role | Email | Password |
| :--- | :--- | :--- |
| **ADMIN** | `admin@servicehub.com` | `Admin@123` |
| **USER** | `rahul@servicehub.com` | `User@123` |
| **USER** | `priya@servicehub.com` | `User@123` |

---

## 📡 REST API Endpoints Summary

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Register a new user |
| `POST` | `/api/auth/login` | Authenticate user & return role/details |
| `GET` | `/api/requests/user/{userId}` | Fetch requests for a specific user |
| `POST` | `/api/requests?userId={id}` | Create new service request |
| `POST` | `/api/requests/suggest-priority` | Priority suggestion rule engine |
| `GET` | `/api/admin/requests` | Fetch all service requests (Admin) |
| `PUT` | `/api/admin/requests/{id}/assign` | Assign technician staff to request |
| `PUT` | `/api/admin/requests/{id}/status` | Update status, priority & resolution notes |
| `GET` | `/api/admin/statistics` | Fetch dashboard metrics & Chart.js data |
| `GET` | `/api/staff` | Fetch staff directory |
| `POST` | `/api/staff` | Create new staff member |
| `PUT` | `/api/staff/{id}` | Edit staff member details |
| `DELETE` | `/api/staff/{id}` | Deactivate staff member |
| `POST` | `/api/feedback?userId={id}` | Submit star rating and comment |
| `GET` | `/api/feedback` | Fetch all customer feedback |
