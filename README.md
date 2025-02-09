# 🌐 Translation Web Application 

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.2-brightgreen)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17%2B-orange)](https://openjdk.org/projects/jdk/17/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14%2B-blue)](https://www.postgresql.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

**Semester Project for Java Course @ ITIS KFU**  
**By Egor Kachanov Romanovich (Group 11-313)**

A high-performance translation service leveraging Spring Boot and Yandex Translate API, featuring concurrent processing and persistent history tracking.

## ✨ Key Features

| Feature                | Technology Stack       | Description                                                                 |
|------------------------|------------------------|-----------------------------------------------------------------------------|
| Cloud Translation      | Yandex Translate API   | Supports 100+ languages with neural machine translation                     |
| Concurrent Processing  | Java Executor Service  | Parallel words processing with configurable thread pool (max 10 threads)    |
| Data Persistence       | PostgreSQL             | Stores translation history with timestamps and IP metadata                  |
| Database Connection    | JDBC                   | Provides low-level API for working with Database management systems         |
| Request Tracking       | Spring AOP             | Tracking IPs of clients                                                     |
| REST API               | Spring Web MVC         | JSON-based endpoints with proper HTTP status codes                          |

## 🚀 Getting Started

### 📋 Prerequisites

**All Systems:**
- Yandex Cloud Account ([Sign Up](https://cloud.yandex.com/))

**Windows:**
1. [JDK 17](https://adoptium.net/temurin/releases/?version=17)
2. [Maven 3.6+](https://maven.apache.org/download.cgi)
3. [PostgreSQL 14+](https://www.postgresql.org/download/windows/)
4. [Git for Windows](https://git-scm.com/download/win)

**Linux/macOS:**
```bash
# Ubuntu/Debian
sudo apt-get install openjdk-17-jdk maven postgresql git

# macOS
brew install openjdk@17 maven postgresql git
```

**Verify Installations:**
```cmd
:: Windows Command Prompt
java -version
/.mvnw -v
psql --version
git --version
```

```bash
# Linux/macOS
java -version
/.mvnw -v
psql --version
git --version
```

## ⚙️ Configuration

### 🔑 Environment Variables

**Windows:**
```cmd
:: Command Prompt
setx YANDEX_API_KEY "your_iam_token"
setx YANDEX_CLOUD_FOLDER_ID "your_folder_id"
setx SPRING_DATASOURCE_URL "jdbc:postgresql://localhost:5433/translation_service"
setx SPRING_DATASOURCE_USERNAME "postgres"
setx SPRING_DATASOURCE_PASSWORD "secure_password"

:: PowerShell
$env:YANDEX_API_KEY = "your_iam_token"
$env:YANDEX_CLOUD_FOLDER_ID = "your_folder_id"
```

**Linux/macOS:**
```bash
export YANDEX_API_KEY="your_iam_token"
export YANDEX_CLOUD_FOLDER_ID="your_folder_id"
export SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5433/translation_service"
export SPRING_DATASOURCE_USERNAME="postgres"
export SPRING_DATASOURCE_PASSWORD="secure_password"
```

### 🗄️ Database Setup

**Windows:**
1. Run PostgreSQL installer (port 5433)
2. Open pgAdmin or psql:
```cmd
psql -U postgres -p 5433 -h localhost
```

**All Systems:**
```sql
CREATE DATABASE translation_service;
CREATE USER app_user WITH PASSWORD 'user_password';
GRANT ALL PRIVILEGES ON DATABASE translation_service TO app_user;
```

Update `src/main/resources/application.yaml`:
```yaml
spring:
  datasource:
    url: ${SPRING_DATASOURCE_URL}
    username: ${SPRING_DATASOURCE_USERNAME}
    password: ${SPRING_DATASOURCE_PASSWORD}
```

## 🏃 Running the Application

**Windows:**
```cmd
git clone https://github.com/PythonGuySup/semestr_work1.git
cd semestr_work1
/.mvnw clean install
/.mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

**Linux/macOS:**
```bash
git clone https://github.com/PythonGuySup/semestr_work1.git
cd semestr_work1
/.mvnw clean install
/.mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

Access the API at: `http://localhost:9999`

## 📚 API Documentation

### POST /translate
**Request:**
```json
{
  "textToTranslate": "Hello world!",
  "targetedLanguage": "RU"
}
```

**Response:**
```json
{
  "translatedText": "Привет мир!",
  "detectedLanguage": "en"
}
```

**cURL Examples:**
```cmd
:: Windows
curl.exe -X POST "http://localhost:9999/translate" ^
-H "Content-Type: application/json" ^
-d "{\"textToTranslate\": \"Spring Boot\", \"targetedLanguage\": \"ES\"}"
```

```bash
# Linux/macOS
curl -X POST "http://localhost:9999/translate" \
-H "Content-Type: application/json" \
-d '{"textToTranslate": "Spring Boot", "targetedLanguage": "ES"}'
```

## 🧩 Project Structure

```
translation-service/
├── src/
│   ├── main/
│   │   ├── java/ru/kpfu/itis/translation
│   │   │   ├── config/         # Configuration classes
│   │   │   ├── controller/     # REST controllers
│   │   │   ├── exception/      # Custom exception classes
│   │   │   ├── service/        # Service layer
│   │   │   └── model/          # Application data layer
│   │   │       ├── dto/        # DTOs
│   │   │       │   └── enums   # Enums for DTOs
│   │   │       └── repository/ # Database operations
│   │   └── resources/
│   │       ├── application.yaml
│   │       └── schema.sql
│   └── test/
│       └── java/ru/kpfu/itis/translation # Future tests
├── .gitattributes
├── .gitignore
├── mvnw
├── mwnv.cmd
├── pom.xml
└── README.md
```

## 🧪 Testing

**Windows Prerequisites:**
1. Install [Docker Desktop](https://www.docker.com/products/docker-desktop/)
2. Enable WSL2 backend (Windows 10/11 Pro)

**Run Tests:**
```cmd
/.mvnw test -Dspring.profiles.active=test
```

Test Coverage (in progress):
- API endpoint validation
- Translation service unit tests
- Database integration tests
- Concurrent processing tests

## ⚠️ Troubleshooting

**Common Windows Issues:**
1. **Port 5433 Conflicts:**
   ```cmd
   netstat -ano | findstr :5433
   taskkill /PID <PID> /F
   ```

2. **Docker Not Running:**
   - Start Docker Desktop
   - Ensure WSL2 integration enabled

3. **Environment Variables Not Recognized:**
   - Restart Command Prompt/PowerShell
   - Check System Properties → Environment Variables

**All Systems:**
- Check logs: `tail -f logs/translation-service.log`
- Verify Yandex credentials: https://console.cloud.yandex.com/folders

## 🤝 Contributing

1. Fork repository
2. Create feature branch (`git checkout -b feature/improvement`)
3. Commit changes (`git commit -m 'Add new feature'`)
4. Push to branch (`git push origin feature/improvement`)
5. Open Pull Request

## 📜 License

MIT License - See [LICENSE](LICENSE) for details.

## 📬 Contact

**Egor Kachanov**  
📧 Email: egorkachanov2006@gmail.com    
💻 GitHub: [@PythonGuySup](https://github.com/PythonGuySup)  
🏫 ITIS KFU, Kazan Federal University  
📆 2025 Java Course Semester Project

