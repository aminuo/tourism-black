# 项目运行指南

## 项目简介
这是一个基于Spring Boot 2.7.18的旅游黑名单管理系统，使用SQLite作为数据库。

## 运行环境要求
- JDK 8+
- Maven 3.6+

## 运行命令

### 1. 直接运行（开发模式）
```bash
# Windows (PowerShell)
./mvnw spring-boot:run

# Windows (CMD)
mvnw spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

### 2. 构建并运行（生产模式）
```bash
# 构建项目 (PowerShell)
./mvnw clean package

# 构建项目 (CMD)
mvnw clean package

# 运行jar包
java -jar target/tourism-black-0.0.1-SNAPSHOT.jar
```

### 3. 使用Maven命令
```bash
# 直接运行
mvn spring-boot:run

# 构建项目
mvn clean package
```

## 项目配置
- 应用端口：8081
- 数据库：SQLite
- 数据库路径：D:\\Wx-develop\\project\\tourism-data\\tourism.db

## API接口
- Banner API: http://localhost:8081/api/banner
- 景点 API: http://localhost:8081/api/scenic-spot

## 注意事项
- 确保数据库文件存在且可访问
- 首次运行时，系统会自动初始化数据库结构
