/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/ClientSide/javascript.js to edit this template
 */

import { showModal } from '/controller/modal.js';

document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('searchButton').addEventListener('click', searchMovements);
});

async function searchMovements() {
    const accountNumber = document.getElementById('accountNumber').value;

    if (!accountNumber) {
        showModal('Error', 'Por favor, ingrese un número de cuenta.');
        return;
    }

    try {
        const movementsResponse = await fetch('/movementsR', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ numcuenta: accountNumber })
        });

        const movementsResult = await movementsResponse.json();

        const accountResponse = await fetch('/accountDetails', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ numcuenta: accountNumber })
        });

        const accountResult = await accountResponse.json();

        if (movementsResult.success) {
            populateMovements(movementsResult.movements);
        } else {
            populateMovements([]); 
            showModal('Error', 'Fallo en recuperar los movimientos.');
        }

        if (accountResult.success && accountResult.account) {
            populateSummaryBox(accountResult.account);
        } else {
            clearSummaryBox();
            showModal('Error', 'Fallo en recuperar los datos de la cuenta.');
        }
    } catch (error) {
        console.error('Search error:', error);
        populateMovements([]); 
        clearSummaryBox();
        showModal('Error', 'Un error ha ocurrido durante la búsqueda. Por favor, revise la conexión.');
    }
}

function populateMovements(movements) {
    const movementsList = document.getElementById('movements-list');
    movementsList.innerHTML = '';

    if (movements.length === 0) {
        const noResults = document.createElement('div');
        noResults.className = 'movement-item';
        noResults.textContent = 'No se encontraron movimientos para la cuenta.';
        movementsList.appendChild(noResults);
        return;
    }

    movements.forEach(movement => {
        const movementItem = document.createElement('div');
        movementItem.className = 'movement-item';
        
        const importeClass = movement.importe >= 0 ? 'text-green-600' : 'text-red-600';
        const importeSign = movement.importe >= 0 ? '+' : '';
        
        movementItem.innerHTML = `
            <div class="movement-info">
                <div class="movement-header">
                    <div class="font-bold text-black">Cuenta: ${movement.cuenta}</div>
                    <div class="font-bold text-blue-600">Saldo: $${movement.saldo ? movement.saldo.toFixed(2) : '0.00'}</div>
                </div>
                <div class="movement-details">
                    <div class="font-normal text-gray-700">Fecha: ${movement.fecha}</div>
                    <div class="font-normal text-gray-700">Número: ${movement.numero}</div>
                </div>
            </div>
            <div class="movement-amount">
                <div class="font-normal text-gray-700">Tipo: ${movement.tipo}</div>
                <div class="font-normal text-gray-700">Acción: ${movement.accion}</div>
                <div class="font-bold ${importeClass}">${importeSign}$${Math.abs(movement.importe).toFixed(2)}</div>
            </div>
        `;
        movementsList.appendChild(movementItem);
    });
}

function populateSummaryBox(account) {
    const summaryBox = document.getElementById('summaryBox');
    const accountBalance = document.getElementById('accountBalance');
    const totalMovements = document.getElementById('totalMovements');

    accountBalance.textContent = account.decCuenSaldo !== undefined ? `$${account.decCuenSaldo.toFixed(2)}` : 'N/A';
    totalMovements.textContent = account.intCuenContMov !== undefined ? account.intCuenContMov : 'N/A';
    summaryBox.style.display = 'block';
}

function clearSummaryBox() {
    const summaryBox = document.getElementById('summaryBox');
    const accountBalance = document.getElementById('accountBalance');
    const totalMovements = document.getElementById('totalMovements');

    accountBalance.textContent = 'N/A';
    totalMovements.textContent = 'N/A';
    summaryBox.style.display = 'none';
}

