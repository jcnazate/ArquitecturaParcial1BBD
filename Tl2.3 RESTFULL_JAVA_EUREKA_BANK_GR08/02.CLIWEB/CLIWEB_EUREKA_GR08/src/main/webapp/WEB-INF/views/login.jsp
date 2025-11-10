<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Eureka Bank</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <style>
        body {
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 1rem;
            position: relative;
            overflow-x: hidden;
        }
    </style>
</head>
<body>
    <img src="${pageContext.request.contextPath}/imgs/sullyvan.png" alt="Sullyvan" class="sullyvan-image" />
    <div class="login-container">
        <div class="login-header">
            <i class="fas fa-university"></i>
            <h1>Eureka Bank</h1>
            <p>Iniciar Sesión</p>
        </div>

        <c:if test="${error != null}">
            <div class="alert alert-danger">
                <i class="fas fa-exclamation-circle"></i> ${error}
            </div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/Login" class="login-form">
            <div class="form-group">
                <label class="form-label" for="username">
                    <i class="fas fa-user"></i> Usuario
                </label>
                <div class="input-wrapper">
                    <i class="fas fa-user"></i>
                    <input type="text" id="username" name="username" class="form-control input-with-icon" placeholder="Ingrese su usuario" required autofocus>
                </div>
            </div>

            <div class="form-group">
                <label class="form-label" for="password">
                    <i class="fas fa-lock"></i> Contraseña
                </label>
                <div class="input-wrapper">
                    <i class="fas fa-lock"></i>
                    <input type="password" id="password" name="password" class="form-control input-with-icon" placeholder="Ingrese su contraseña" required>
                </div>
            </div>

            <button type="submit" class="btn-login">
                <i class="fas fa-sign-in-alt"></i> Iniciar Sesión
            </button>
        </form>
    </div>
</body>
</html>

