@echo off

set JAVA_HOME=C:\Program Files\Java\jdk-25.0.2

echo [1/6] Iniciando App Principal (Porta 8080)...
start "Automanager Principal (8080)" cmd /k ".\mvnw.cmd spring-boot:run"

timeout /t 15 /nobreak >nul

echo [2/6] Iniciando ms-clientes (Porta 8081)...
start "MS Clientes (8081)" cmd /k "cd ms-clientes && ..\mvnw.cmd spring-boot:run"

echo [3/6] Iniciando ms-funcionarios (Porta 8082)...
start "MS Funcionarios (8082)" cmd /k "cd ms-funcionarios && ..\mvnw.cmd spring-boot:run"

echo [4/6] Iniciando ms-catalogo (Porta 8083)...
start "MS Catalogo (8083)" cmd /k "cd ms-catalogo && ..\mvnw.cmd spring-boot:run"

echo [5/6] Iniciando ms-vendas (Porta 8084)...
start "MS Vendas (8084)" cmd /k "cd ms-vendas && ..\mvnw.cmd spring-boot:run"

echo [6/6] Iniciando ms-veiculos (Porta 8085)...
start "MS Veiculos (8085)" cmd /k "cd ms-veiculos && ..\mvnw.cmd spring-boot:run"

echo.
echo Todos os servicos estao inicializando em novas janelas!
pause
