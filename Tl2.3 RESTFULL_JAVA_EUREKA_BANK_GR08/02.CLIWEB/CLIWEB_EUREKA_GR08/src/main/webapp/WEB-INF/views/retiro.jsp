<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Retiro - Eureka Bank</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <nav class="navbar">
        <a href="${pageContext.request.contextPath}/Home" class="navbar-brand">
            <i class="fas fa-university"></i>
            Eureka Bank
        </a>
        <div class="navbar-user">
            <span><i class="fas fa-user"></i> ${sessionScope.username}</span>
            <a href="${pageContext.request.contextPath}/Logout" class="btn-logout">
                <i class="fas fa-sign-out-alt"></i> Salir
            </a>
        </div>
    </nav>

    <div class="container">
        <div class="card">
            <h1 class="card-title">
                <i class="fas fa-arrow-up"></i>
                Realizar Retiro
            </h1>

            <c:if test="${error != null}">
                <div class="alert alert-danger">
                    <i class="fas fa-exclamation-circle"></i> ${error}
                </div>
            </c:if>

            <c:if test="${success != null}">
                <div class="alert alert-success">
                    <i class="fas fa-check-circle"></i> ${success}
                </div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/Retiro" class="retiro-form">
                <div class="form-group">
                    <label class="form-label" for="cuenta">
                        <i class="fas fa-credit-card"></i> Número de Cuenta
                    </label>
                    <input type="text" id="cuenta" name="cuenta" class="form-control" placeholder="Ingrese el número de cuenta" required>
                </div>

                <div class="form-group">
                    <label class="form-label" for="monto">
                        <i class="fas fa-dollar-sign"></i> Monto
                    </label>
                    <input type="number" id="monto" name="monto" class="form-control" placeholder="Ingrese el monto a retirar" step="0.01" min="0.01" required>
                </div>

                <button type="submit" class="btn btn-primary" style="width: 100%;">
                    <i class="fas fa-check"></i> Realizar Retiro
                </button>
            </form>

            <div style="margin-top: 2rem; text-align: center;">
                <a href="${pageContext.request.contextPath}/Home" class="btn" style="background: #f0f0f0; color: #333;">
                    <i class="fas fa-arrow-left"></i> Volver al Menú
                </a>
            </div>
        </div>
    </div>
</body>
</html>
