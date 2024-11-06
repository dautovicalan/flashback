# Image Upload & Conversion Platform

A platform for uploading, downloading, and manipulating images. This application allows users to upload images, apply transformations, manage metadata, and set tags. Additionally, users can select subscription plans, which dictate daily upload limits. The admin view provides full control over user accounts and images, with capabilities to monitor statistics and logs.

# Table of Contents

	•	Features
	•	Architecture
	•	Tech Stack
	•	Setup & Installation
	•	Usage
	•	Environment Variables
	•	Deployment
	•	Contributing
	•	License

# Features

## User Features

	•	Image Upload: Upload images with options to apply transformations like file extension conversion and scaling.
	•	Image Download: Download images with optional filters, including sepia and blur effects.
	•	Tags & Metadata: Each image can have associated tags and metadata for easy categorization and retrieval.
	•	Subscription Plans: Users can select from subscription plans (Free, Pro, and Gold), which vary in daily upload limits.

## Admin Features

	•	User Management: View and modify user details, including subscription plans and image management.
	•	Image Management: Full control over all images uploaded by users, including viewing, editing, and deleting images.
	•	Statistics & Logs: Access user activity logs, view statistics on image uploads/downloads, and track daily usage limits.

## Architecture

### This project is structured into backend, frontend, and database layers:

	1.	Backend: A REST API built with Spring Boot that handles user authentication, image processing, and data storage.
	2.	Frontend: A React application (with TypeScript) providing user interface and interaction capabilities, using React Query and Axios for API requests.
	3.	Database: PostgreSQL database for storing user and image metadata.
	4.	Logging: Elasticsearch stores application logs, enabling efficient log retrieval and analytics.

## Tech Stack

	•	Backend: Spring Boot (Kotlin), PostgreSQL, Elasticsearch
	•	Frontend: React (TypeScript), React Query, Axios
	•	Authentication: Keycloak for user authentication and authorization
	•	Deployment: Docker Compose

# License

This project is licensed under the MIT License. See the LICENSE file for details.
