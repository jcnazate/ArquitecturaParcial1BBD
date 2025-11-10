<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Movimientos - Eureka Bank</title>
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
                <i class="fas fa-list-alt"></i>
                Consultar Movimientos
            </h1>

            <c:if test="${error != null}">
                <div class="alert alert-danger">
                    <i class="fas fa-exclamation-circle"></i> ${error}
                </div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/Movimiento" class="movimiento-form">
                <div class="form-group">
                    <label class="form-label" for="cuenta">
                        <i class="fas fa-credit-card"></i> Número de Cuenta
                    </label>
                    <input type="text" id="cuenta" name="cuenta" class="form-control" placeholder="Ingrese el número de cuenta" required>
                </div>

                <button type="submit" class="btn btn-primary" style="width: 100%;">
                    <i class="fas fa-search"></i> Consultar Movimientos
                </button>
            </form>

            <c:if test="${movimientos != null}">
                <div class="stats-grid" style="margin-top: 2rem;">
                    <div class="stat-card">
                        <h4>Saldo Actual</h4>
                        <div class="value">$<fmt:formatNumber value="${saldoActual}" type="number" minFractionDigits="2" maxFractionDigits="2" /></div>
                    </div>
                    <div class="stat-card" style="background: #11998e;">
                        <h4>Total Ingresos</h4>
                        <div class="value">$<fmt:formatNumber value="${totalIngresos}" type="number" minFractionDigits="2" maxFractionDigits="2" /></div>
                    </div>
                    <div class="stat-card" style="background: #f5576c;">
                        <h4>Total Egresos</h4>
                        <div class="value">$<fmt:formatNumber value="${totalEgresos}" type="number" minFractionDigits="2" maxFractionDigits="2" /></div>
                    </div>
                    <div class="stat-card" style="background: #4facfe;">
                        <h4>Saldo Neto</h4>
                        <div class="value">$<fmt:formatNumber value="${saldoNeto}" type="number" minFractionDigits="2" maxFractionDigits="2" /></div>
                    </div>
                </div>

                <c:if test="${not empty movimientos}">
                    <div class="card" style="margin-top: 2rem; overflow-x: auto;">
                        <h2 style="margin-bottom: 1.5rem;">
                            <i class="fas fa-history"></i> Historial de Movimientos
                        </h2>
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>#</th>
                                    <th>Fecha</th>
                                    <th>Tipo</th>
                                    <th>Descripción</th>
                                    <th>Importe</th>
                                    <th>Saldo</th>
                                    <th>Cuenta Referencia</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="mov" items="${movimientos}">
                                    <tr>
                                        <td>${mov.numeroMovimiento}</td>
                                        <td>${mov.fechaMovimiento != null ? mov.fechaMovimiento : 'N/A'}</td>
                                        <td>${mov.codigoTipoMovimiento != null ? mov.codigoTipoMovimiento : 'N/A'}</td>
                                        <td>${mov.tipoDescripcion != null ? mov.tipoDescripcion : 'N/A'}</td>
                                        <td style="font-weight: 600; color: ${mov.importeMovimiento >= 0 ? '#11998e' : '#f5576c'};">
                                            $<fmt:formatNumber value="${mov.importeMovimiento}" type="number" minFractionDigits="2" maxFractionDigits="2" />
                                        </td>
                                        <td>$<fmt:formatNumber value="${mov.saldo}" type="number" minFractionDigits="2" maxFractionDigits="2" /></td>
                                        <td>${mov.cuentaReferencia != null ? mov.cuentaReferencia : '-'}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:if>
                <c:if test="${empty movimientos}">
                    <div class="alert" style="background: #fff3cd; color: #856404; border-left: 4px solid #ffc107; margin-top: 2rem;">
                        <i class="fas fa-info-circle"></i> No se encontraron movimientos para esta cuenta.
                    </div>
                </c:if>
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
