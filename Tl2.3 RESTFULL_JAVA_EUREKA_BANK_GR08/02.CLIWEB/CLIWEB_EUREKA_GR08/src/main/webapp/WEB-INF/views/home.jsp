<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Menú Principal - Eureka Bank</title>
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
            <span><i class="fas fa-user"></i> ${username}</span>
            <a href="${pageContext.request.contextPath}/Logout" class="btn-logout">
                <i class="fas fa-sign-out-alt"></i> Salir
            </a>
        </div>
    </nav>

    <div class="container">
        <div class="card">
            <h1 class="card-title">
                <i class="fas fa-home"></i>
                Bienvenido, ${username}
            </h1>
            <p style="color: #666; margin-bottom: 2rem; font-size: 1.1rem;">
                Seleccione una opción del menú para continuar
            </p>

            <div class="menu-grid">
                <a href="${pageContext.request.contextPath}/Deposito" class="menu-card">
                    <i class="fas fa-arrow-down"></i>
                    <h3>Depósito</h3>
                    <p>Realizar un depósito a una cuenta</p>
                </a>

                <a href="${pageContext.request.contextPath}/Retiro" class="menu-card">
                    <i class="fas fa-arrow-up"></i>
                    <h3>Retiro</h3>
                    <p>Realizar un retiro de una cuenta</p>
                </a>

                <a href="${pageContext.request.contextPath}/Transferencia" class="menu-card">
                    <i class="fas fa-exchange-alt"></i>
                    <h3>Transferencia</h3>
                    <p>Transferir dinero entre cuentas</p>
                </a>

                <a href="${pageContext.request.contextPath}/Cuenta" class="menu-card">
                    <i class="fas fa-info-circle"></i>
                    <h3>Datos de Cuenta</h3>
                    <p>Consultar información de una cuenta</p>
                </a>

                <a href="${pageContext.request.contextPath}/Movimiento" class="menu-card">
                    <i class="fas fa-list-alt"></i>
                    <h3>Movimientos</h3>
                    <p>Ver historial de movimientos</p>
                </a>
            </div>
        </div>

        <div style="text-align: center; margin-top: 2rem;">
            <img src="${pageContext.request.contextPath}/imgs/mikeysullivan.png" alt="Mike y Sullivan" class="mikeysullivan-image" />
        </div>
    </div>
</body>
</html>
