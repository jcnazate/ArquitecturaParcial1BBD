<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Datos de Cuenta - Eureka Bank</title>
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
                <i class="fas fa-info-circle"></i>
                Consultar Datos de Cuenta
            </h1>

            <c:if test="${error != null}">
                <div class="alert alert-danger">
                    <i class="fas fa-exclamation-circle"></i> ${error}
                </div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/Cuenta" class="cuenta-form">
                <div class="form-group">
                    <label class="form-label" for="cuenta">
                        <i class="fas fa-credit-card"></i> Número de Cuenta
                    </label>
                    <input type="text" id="cuenta" name="cuenta" class="form-control" placeholder="Ingrese el número de cuenta" required>
                </div>

                <button type="submit" class="btn btn-primary" style="width: 100%;">
                    <i class="fas fa-search"></i> Consultar
                </button>
            </form>

            <c:if test="${cuenta != null}">
                <div class="card" style="margin-top: 2rem; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white;">
                    <h2 style="color: white; margin-bottom: 1.5rem;">
                        <i class="fas fa-credit-card"></i> Información de la Cuenta
                    </h2>
                    <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 1.5rem;">
                        <div>
                            <strong><i class="fas fa-hashtag"></i> Código de Cuenta:</strong>
                            <p style="margin-top: 0.5rem; font-size: 1.1rem;">${cuenta.chrCuenCodigo}</p>
                        </div>
                        <div>
                            <strong><i class="fas fa-dollar-sign"></i> Saldo:</strong>
                            <p style="margin-top: 0.5rem; font-size: 1.5rem; font-weight: 700;">$<fmt:formatNumber value="${cuenta.decCuenSaldo}" type="number" minFractionDigits="2" maxFractionDigits="2" /></p>
                        </div>
                        <div>
                            <strong><i class="fas fa-list-ol"></i> Contador de Movimientos:</strong>
                            <p style="margin-top: 0.5rem; font-size: 1.1rem;">${cuenta.intCuenContMov}</p>
                        </div>
                        <div>
                            <strong><i class="fas fa-calendar"></i> Fecha de Creación:</strong>
                            <p style="margin-top: 0.5rem; font-size: 1.1rem;">
                                <c:choose>
                                    <c:when test="${not empty cuenta.dttCuenFechaCreacion}">
                                        ${cuenta.dttCuenFechaCreacion}
                                    </c:when>
                                    <c:otherwise>
                                        N/A
                                    </c:otherwise>
                                </c:choose>
                            </p>
                        </div>
                        <div>
                            <strong><i class="fas fa-info-circle"></i> Estado:</strong>
                            <p style="margin-top: 0.5rem; font-size: 1.1rem;">${cuenta.vchCuenEstado}</p>
                        </div>
                        <div>
                            <strong><i class="fas fa-building"></i> Sucursal:</strong>
                            <p style="margin-top: 0.5rem; font-size: 1.1rem;">${cuenta.chrSucucodigo}</p>
                        </div>
                    </div>
                </div>
            </c:if>

            <div style="margin-top: 2rem; text-align: center;">
                <a href="${pageContext.request.contextPath}/Home" class="btn" style="background: #f0f0f0; color: #333;">
                    <i class="fas fa-arrow-left"></i> Volver al Menú
                </a>
            </div>
        </div>
    </div>
</body>
</html>
