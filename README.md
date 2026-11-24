# 🌿 BotanyBliss

BotanyBliss is a full-stack plant information and community contribution platform built with React, Spring Boot, and MySQL. Users can explore plants, contribute new information, and suggest updates, while administrators manage and review contributions.

## ✨ Features

- Browse plants and view detailed information.
- User registration, login, and JWT authentication.
- Submit new plants and suggest updates.
- Track contribution status (Pending, Approved, Rejected).
- Admin dashboard for plant management.
- Approve or reject submissions and update suggestions.
- Local and AWS S3 image storage support.

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| React, Vite, JavaScript | Frontend |
| Java 21, Spring Boot | Backend |
| MySQL | Database |
| Spring Security, JWT | Authentication |
| Spring Data JPA, Hibernate | ORM |
| AWS S3 | Image storage |
| AWS EC2, Ubuntu | Hosting |
| Nginx | Web server |
| Postman | API testing |
| Git, GitHub | Version control |

## 📁 Project Structure

```text
BotanyBliss/
├── frontend/     # React + Vite
├── backend/     # Spring Boot
└── README.md
```

## 🚀 Local Setup

### Prerequisites

- Git
- Java 21
- Node.js 24 (recommended)
- MySQL

### 1. Clone the Repository

```bash
git clone https://github.com/YOUR_USERNAME/BotanyBliss.git
cd BotanyBliss
```

### 2. Set Up MySQL

```sql
CREATE DATABASE botanybliss_db;
```

### 3. Configure Environment Variables

Set the following variables for the backend:

```env
DB_URL=jdbc:mysql://localhost:3306/botanybliss_db
DB_USERNAME=your_database_username
DB_PASSWORD=your_database_password

JWT_SECRET=your_long_random_secret
ADMIN_PASSWORD=your_admin_password

AWS_ACCESS_KEY_ID=your_aws_access_key
AWS_SECRET_ACCESS_KEY=your_aws_secret_key
```

AWS credentials are only required when using S3.

### 4. Configure Image Storage

**Local storage (default):**

Images are stored in `uploads/plant-images/`.

```bash
SPRING_PROFILES_ACTIVE=local
```

**AWS S3 storage:**

```bash
SPRING_PROFILES_ACTIVE=s3
```

Update the S3 bucket name and region in `application.yml`.

### 5. Run the Backend

Navigate to the `backend/` directory.

**Windows:**

```bash
mvnw.cmd spring-boot:run
```

**Linux/macOS:**

```bash
./mvnw spring-boot:run
```

Backend: `http://localhost:8080`

API: `http://localhost:8080/api`

### 6. Run the Frontend

Navigate to the `frontend/` directory.

```bash
npm install
npm run dev
```

Frontend: `http://localhost:5173`

Make sure the frontend API URL points to the backend.

## 👤 Admin Account

An initial admin account is automatically created on the first startup if it does not already exist.

| Field | Value |
|---|---|
| Email | `admin@bontanybliss.com` |
| Password | Configured through `ADMIN_PASSWORD` |

## ☁️ AWS EC2 Deployment

BotanyBliss can be deployed on an Ubuntu EC2 instance using MySQL, Spring Boot, Nginx, and S3.

### 1. Launch EC2

- Launch an Ubuntu 24.04 LTS instance.
- Configure a key pair for SSH access.
- Allow inbound ports **22 (SSH)**, **80 (HTTP)**, and **8080 (Backend API)** as required.
- Restrict SSH and backend access to trusted IPs.

### 2. Connect to the Server

```bash
ssh -i your-key.pem ubuntu@YOUR_EC2_PUBLIC_IP
```

### 3. Install Dependencies

Update the server and install Java and MySQL:

```bash
sudo apt update && sudo apt upgrade -y
sudo apt install openjdk-21-jdk mysql-server nginx -y
```

Install Node.js 24:

```bash
curl -fsSL https://deb.nodesource.com/setup_24.x | sudo -E bash -
sudo apt install nodejs -y
```

Verify installations:

```bash
java -version
node -v
npm -v
mysql --version
nginx -v
```

### 4. Set Up MySQL

Log into MySQL:

```bash
sudo mysql
```

Create the database and user:

```sql
CREATE DATABASE botanybliss_db;

CREATE USER 'botanyblissuser'@'localhost'
IDENTIFIED BY 'your_database_password';

GRANT ALL PRIVILEGES ON botanybliss_db.*
TO 'botanyblissuser'@'localhost';

FLUSH PRIVILEGES;
EXIT;
```

### 5. Upload the Project

Upload the project to EC2 using SSH/SFTP.

```text
/home/ubuntu/botanybliss-app/
├── backend/
└── frontend/
```

Exclude `node_modules`, `target`, and `dist` folders.

### 6. Configure Environment Variables

Create an environment file:

```bash
nano ~/botanybliss.env
```

Add:

```env
DB_URL=jdbc:mysql://localhost:3306/botanybliss_db
DB_USERNAME=botanyblissuser
DB_PASSWORD=your_database_password

JWT_SECRET=your_long_random_secret
ADMIN_PASSWORD=your_admin_password

AWS_ACCESS_KEY_ID=your_aws_access_key
AWS_SECRET_ACCESS_KEY=your_aws_secret_key
```

Secure the file and load the variables:

```bash
chmod 600 ~/botanybliss.env

set -a
source ~/botanybliss.env
set +a
```

For production, use an EC2 IAM role instead of storing AWS access keys.

### 7. Build and Run the Backend

```bash
cd ~/botanybliss-app/backend

./mvnw clean package -DskipTests
```

Run the application:

```bash
nohup java -jar target/botanybliss-0.0.1-SNAPSHOT.jar \
--server.port=8080 > botanybliss.log 2>&1 &
```

View logs:

```bash
tail -f botanybliss.log
```

### 8. Build the Frontend

```bash
cd ~/botanybliss-app/frontend

npm install
npm run build
```

Copy the production build to Nginx:

```bash
sudo rm -rf /var/www/html/*
sudo cp -r dist/* /var/www/html/
```

Update the frontend API URL and backend CORS configuration to use your EC2 public IP.

### 9. Configure Nginx

Edit the default configuration:

```bash
sudo nano /etc/nginx/sites-available/default
```

Inside the `location /` block, replace the `404` fallback with `/index.html`:

```nginx
location / {
    try_files $uri $uri/ /index.html;
}
```

Test and reload Nginx:

```bash
sudo nginx -t
sudo systemctl reload nginx
```

### 10. Access the Application

Open the application in your browser:

```text
http://YOUR_EC2_PUBLIC_IP
```

The frontend is served through Nginx, the backend runs on port 8080, and plant images are stored in S3 when the S3 profile is enabled.

> If your EC2 public IP changes, update the frontend API URL and backend CORS configuration, rebuild the frontend, and redeploy.

## 🔐 Security

- Never commit passwords, secrets, `.env` files, or AWS credentials.
- Use strong JWT and admin passwords.
- Restrict SSH and database access.
- Use least-privilege IAM and S3 permissions.
- Use HTTPS for production deployments.

## 🤝 Contributing

1. Fork the repository.
2. Create a feature branch.
3. Commit and push your changes.
4. Open a Pull Request.

## 📄 License

This project is licensed under the MIT License.
See the [LICENSE](LICENSE) file for details.

---

**Built with React, Spring Boot, MySQL, and AWS. 🌱**
