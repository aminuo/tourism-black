@echo off

echo 旅游黑名单管理系统运行脚本
echo ==========================
echo 1. 开发模式运行
echo 2. 构建并运行（生产模式）
echo 3. 退出
echo ==========================

set /p choice=请选择操作编号：

if "%choice%"=="1" (
    echo 正在以开发模式运行项目...
    .\mvnw spring-boot:run
) else if "%choice%"=="2" (
    echo 正在构建项目...
    .\mvnw clean package
    echo 构建完成，正在运行项目...
    java -jar target/tourism-black-0.0.1-SNAPSHOT.jar
) else if "%choice%"=="3" (
    echo 退出脚本
) else (
    echo 无效的选择，请重新运行脚本
)

pause
