Mohan Library Management System
A robust desktop application designed to manage the internal library resources of Mohan Company. This application provides a centralized platform for administrators to track book inventory, manage employee borrowing history, and automate notification processes.

Key Features
Core Modules
Inventory Management: Full CRUD operations for book records, including title, author, ISBN, category, and real-time stock tracking.

Loan History Tracking: Comprehensive system to record book issuance and check-ins for employees.

Employee Management: Manage staff profiles and their respective borrowing privileges.

Advanced Capabilities
Automated Email Reminders: Integrated notification system that sends automated emails to employees for overdue books or upcoming return deadlines.

Multi-language Support: Users can choose their preferred language (i18n) at the application's main frame during startup.

Security: All user passwords are encrypted using the BCrypt hashing algorithm to ensure data integrity and security.

Technical Stack
Language: Java

UI Framework: Java Swing (Classic Interface)

Database: MySQL 

Security Library: jBcrypt

Messaging: Gmail SMPT

Installation and Setup
Prerequisites
Java Development Kit (JDK) 11 or higher.

Database Management System (MySQL or equivalent).

Internet connection (for email functionality).

Database Configuration
Create a database named mohan_library.

Execute the provided SQL scripts in the /database directory to set up the required tables and initial data.

Update the database.properties file with your database credentials.

Running the Application
Clone the repository to your local machine.

Navigate to the project directory.

Build the project using your preferred IDE or command line.

Run the Main.java file to launch the application.

System Architecture
The application follows a standard N-Tier architecture to separate concerns and ensure maintainability:

Presentation Layer: Built with Java Swing components, focusing on a classic and user-friendly desktop experience.

Business Logic Layer: Handles the core processing, including loan validation, email scheduling, and language switching logic.

Data Access Layer: Manages interaction with the database through JDBC.

Contributors
This project was developed by a team of 5 members:

thtoai1996 (Team Leader)

thanhdattt2006 (Database Designer)

HuyTranlk (Developer)

MCuong (Developer)

HongThai20157 (Developer)

Note
This application is intended for internal use within Mohan Company. Unauthorized distribution or modification is prohibited.
