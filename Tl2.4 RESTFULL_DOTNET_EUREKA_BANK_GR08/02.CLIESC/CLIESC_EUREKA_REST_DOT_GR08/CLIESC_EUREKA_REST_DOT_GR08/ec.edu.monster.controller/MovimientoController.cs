using CLIESC_EUREKA_REST_DOT_GR08.ec.edu.monster.model;
using CLIESC_EUREKA_REST_DOT_GR08.ec.edu.monster.view;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;

namespace CLIESC_EUREKA_REST_DOT_GR08.ec.edu.monster.controller
{
    public class MovimientosController
    {
        private readonly FrmMovimiento _view;
        private readonly HttpClient _httpClient;
        private readonly string _apiBaseUrl;

        public MovimientosController(FrmMovimiento view)
        {
            _view = view ?? throw new ArgumentNullException(nameof(view));
            _httpClient = ApiClient.Client;
            _apiBaseUrl = ApiClient.ApiBaseUrl;

            _view.BuscarClicked += OnBuscarClicked;
        }

        private async void OnBuscarClicked(object sender, EventArgs e)
        {
            await CargarMovimientosAsync();
        }

        private async Task CargarMovimientosAsync()
        {
            var cuenta = _view.NumeroCuenta?.Trim();

            if (string.IsNullOrEmpty(cuenta))
            {
                _view.MostrarMensaje("Por favor, ingrese un número de cuenta.");
                return;
            }

            try
            {
                // 1) Movimientos
                var movReq = new { numcuenta = cuenta };
                var movJson = JsonConvert.SerializeObject(movReq);
                var movContent = new StringContent(movJson, Encoding.UTF8, "application/json");

                var movUrl = new Uri(new Uri(_apiBaseUrl), "Movimiento");
                var movResp = await _httpClient.PostAsync(movUrl, movContent);

                if (!movResp.IsSuccessStatusCode)
                {
                    _view.MostrarMensaje("No se pudieron obtener los movimientos.");
                    return;
                }

                var movBody = await movResp.Content.ReadAsStringAsync();
                var movimientos = JsonConvert.DeserializeObject<List<MovimientoModel>>(movBody)
                                  ?? new List<MovimientoModel>();

                // 2) Datos de cuenta
                var ctaUrl = new Uri(new Uri(_apiBaseUrl), $"cuenta/{cuenta}");
                var ctaResp = await _httpClient.GetAsync(ctaUrl);
                if (ctaResp.IsSuccessStatusCode)
                {
                    var ctaBody = await ctaResp.Content.ReadAsStringAsync();
                    var cuentaModel = JsonConvert.DeserializeObject<CuentaModel>(ctaBody);
                    if (cuentaModel != null)
                        _view.Saldo = cuentaModel.DecCuenSaldo.ToString("N2");
                }

                // 3) Llenar UI
                if (movimientos.Count == 0)
                {
                    _view.AgregarMovimientos(new List<MovimientosItem>());
                    _view.TotalMovimientos = "0";
                    _view.MostrarMensaje("No se encontraron movimientos para la cuenta ingresada.");
                    return;
                }

                var items = new List<MovimientosItem>();

                foreach (var mov in movimientos)
                {
                    var item = new MovimientosItem();

                    item.Cuenta = cuenta;
                    item.Fecha = mov.FechaMovimientoDt?.ToString("dd/MM/yyyy HH:mm")
                                       ?? mov.FechaMovimiento ?? "";
                    item.Movimiento = mov.TipoDescripcion ?? mov.CodigoTipoMovimiento ?? "";
                    item.Tipo = mov.CodigoTipoMovimiento ?? "";

                    // Deducción de Acción (Débito / Crédito) sin usar Contains con 2 parámetros
                    var desc = mov.TipoDescripcion ?? "";
                    bool esRetiro = desc.IndexOf("retiro", StringComparison.OrdinalIgnoreCase) >= 0;
                    item.Accion = esRetiro ? "Débito" : "Crédito";

                    item.Importe = mov.ImporteMovimiento.ToString("N2");
                    item.SaldoActual = mov.Saldo.ToString("N2");

                    // Colorear según acción
                    if (esRetiro || mov.ImporteMovimiento < 0)
                        item.MarcarComoDebito();
                    else
                        item.MarcarComoCredito();

                    items.Add(item);
                }

                _view.AgregarMovimientos(items);
                _view.TotalMovimientos = movimientos.Count.ToString();
            }
            catch (Exception ex)
            {
                _view.MostrarMensaje("Error al obtener los movimientos: " + ex.Message);
            }
        }
    }
}
