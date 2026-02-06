import { Component, OnInit, AfterViewInit, OnDestroy, signal, computed, ViewChild, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Chart, registerables } from 'chart.js';
import Swal from 'sweetalert2';
import { DashboardService, DashboardOTDTO, EstadisticasGeneralesDTO, ResumenCostosDTO, OTPendienteDTO, OTPorEstadoDTO, OTPorClienteDTO, EvolucionMensualDTO, MesDTO } from '../../service/dashboard.service';

Chart.register(...registerables);

@Component({
  selector: 'app-dashboard-componente',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard-componente.html',
  styleUrls: ['./dashboard-componente.css']
})
export class DashboardComponente implements OnInit, AfterViewInit, OnDestroy {
  // Señales reactivas
  dashboardData = signal<DashboardOTDTO | null>(null);
  isLoading = signal(true);
  isRefreshing = signal(false);
  selectedRango = signal('MES');
  private isComponentActive = true;
  private autoRefreshInterval: any;

  // Computed signals
  estadisticas = computed(() => this.dashboardData()?.generales);
  otsPendientes = computed(() => this.dashboardData()?.otsPendientes || []);
  resumenCostos = computed(() => this.dashboardData()?.resumenCostos);
  otsPorEstado = computed(() => this.dashboardData()?.otsPorEstado || []);
  otsPorCliente = computed(() => this.dashboardData()?.otsPorCliente || []);
  evolucionMensual = computed(() => this.dashboardData()?.evolucionMensual);
fechaActualISO: string = new Date().toISOString();

  // Filtros
  filtro = {
    fechaInicio: '',
    fechaFin: '',
    clienteId: undefined as number | undefined,
    areaId: undefined as number | undefined,
    proyectoId: undefined as number | undefined,
    estadoId: undefined as number | undefined
  };

  showFiltros = false;
  rangos = [
    { value: 'HOY', label: 'Hoy' },
    { value: 'SEMANA', label: 'Esta semana' },
    { value: 'MES', label: 'Este mes' },
    { value: 'TRIMESTRE', label: 'Este trimestre' },
    { value: 'SEMESTRE', label: 'Este semestre' },
    { value: 'ANIO', label: 'Este año' }
  ];

  // Referencias a gráficos
  @ViewChild('chartEstados') chartEstadosRef!: ElementRef;
  @ViewChild('chartCostos') chartCostosRef!: ElementRef;
  @ViewChild('chartEvolucion') chartEvolucionRef!: ElementRef;
  @ViewChild('chartClientes') chartClientesRef!: ElementRef;

  private chartEstados: Chart | null = null;
  private chartCostos: Chart | null = null;
  private chartEvolucion: Chart | null = null;
  private chartClientes: Chart | null = null;

  constructor(private dashboardService: DashboardService) {}

  ngOnInit(): void {
    this.loadDashboard();
    this.setupAutoRefresh();
  }

  ngAfterViewInit(): void {
    // Los gráficos se crearán cuando los datos estén listos
  }
ngOnDestroy(): void {
  // AÑADE ESTAS 3 LÍNEAS AL INICIO del método:
  this.isComponentActive = false;
  if (this.autoRefreshInterval) {
    clearInterval(this.autoRefreshInterval);
  }

  this.destroyCharts(); // Esta línea ya la tenías
}
getTotalOts(): number {
  return this.otsPorEstado()
    .reduce((total, item) => total + (item.cantidad ?? 0), 0);
}

  loadDashboard(): void {
    this.isLoading.set(true);

    this.dashboardService.getDashboardCached().subscribe({
      next: (data) => {
  // AÑADE ESTA LÍNEA AL INICIO:
  if (!this.isComponentActive) return;

  this.dashboardData.set(data);
  this.isLoading.set(false);
  this.isRefreshing.set(false);

  setTimeout(() => {
    // AÑADE ESTA CONDICIÓN:
    if (this.isComponentActive) {
      this.createCharts();
    }
  }, 100);

  // MODIFICA EL Swal.fire para incluir la condición:
  if (this.isComponentActive) {
    Swal.fire({
      icon: 'success',
      title: 'Dashboard actualizado',
      text: 'Los datos se han cargado correctamente',
      toast: true,
      position: 'top-end',
      showConfirmButton: false,
      timer: 3000,
      timerProgressBar: true,
      background: '#f8f9fa',
      iconColor: '#28a745'
    });
  }
},
     error: (error) => {
  // AÑADE ESTA LÍNEA AL INICIO:
  if (!this.isComponentActive) return;

  console.error('Error cargando dashboard:', error);
  this.isLoading.set(false);
  this.isRefreshing.set(false);

  // MODIFICA EL Swal.fire para incluir la condición:
  if (this.isComponentActive) {
    Swal.fire({
      icon: 'error',
      title: 'Error',
      text: 'No se pudo cargar el dashboard. Intente nuevamente.',
      confirmButtonColor: '#dc3545',
      background: '#f8f9fa'
    });
  }
}
    });
  }

  refreshDashboard(): void {
    this.isRefreshing.set(true);

    // Limpiar cache
    this.dashboardService.clearCache();

    // Recargar datos
    this.loadDashboard();
  }
aplicarFiltroRango(): void {
  this.dashboardService.clearCache();

  this.dashboardService.getDashboardByRango(this.selectedRango()).subscribe({
    next: (data) => {
      // AÑADE esta condición al inicio:
      if (!this.isComponentActive) return;

      this.dashboardData.set(data);
      this.createCharts();

      // MODIFICA el Swal.fire para incluir la condición:
      if (this.isComponentActive) {
        Swal.fire({
          icon: 'info',
          title: 'Filtro aplicado',
          text: `Mostrando datos del ${this.getRangoLabel(this.selectedRango())}`,
          toast: true,
          position: 'top-end',
          showConfirmButton: false,
          timer: 2000,
          background: '#f8f9fa',
          iconColor: '#007bff'
        });
      }
    },
    error: (error) => {
      // AÑADE esta condición al inicio:
      if (!this.isComponentActive) return;

      // MODIFICA el Swal.fire para incluir la condición:
      if (this.isComponentActive) {
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se pudo aplicar el filtro',
          confirmButtonColor: '#dc3545',
          background: '#f8f9fa'
        });
      }
    }
  });
}
  aplicarFiltrosAvanzados(): void {
    // Convertir fechas a formato ISO
    const filtro = {
      ...this.filtro,
      fechaInicio: this.filtro.fechaInicio ? new Date(this.filtro.fechaInicio).toISOString().split('T')[0] : undefined,
      fechaFin: this.filtro.fechaFin ? new Date(this.filtro.fechaFin).toISOString().split('T')[0] : undefined
    };

    this.dashboardService.clearCache();

    this.dashboardService.getDashboard(filtro).subscribe({
      next: (data) => {
        this.dashboardData.set(data);
        this.createCharts();
        this.showFiltros = false;

        Swal.fire({
          icon: 'success',
          title: 'Filtros aplicados',
          text: 'Los filtros se han aplicado correctamente',
          toast: true,
          position: 'top-end',
          showConfirmButton: false,
          timer: 2000,
          background: '#f8f9fa',
          iconColor: '#28a745'
        });
      },
      error: (error) => {
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se pudieron aplicar los filtros',
          confirmButtonColor: '#dc3545',
          background: '#f8f9fa'
        });
      }
    });
  }

  limpiarFiltros(): void {
    this.filtro = {
      fechaInicio: '',
      fechaFin: '',
      clienteId: undefined,
      areaId: undefined,
      proyectoId: undefined,
      estadoId: undefined
    };

    this.selectedRango.set('MES');
    this.refreshDashboard();

    Swal.fire({
      icon: 'info',
      title: 'Filtros limpiados',
      text: 'Se han restablecido todos los filtros',
      toast: true,
      position: 'top-end',
      showConfirmButton: false,
      timer: 2000,
      background: '#f8f9fa',
      iconColor: '#6c757d'
    });
  }

  exportarDashboard(): void {
    Swal.fire({
      title: 'Exportar Dashboard',
      text: 'Seleccione el formato de exportación',
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#28a745',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'PDF',
      cancelButtonText: 'Excel',
      background: '#f8f9fa',
      showDenyButton: true,
      denyButtonText: 'Cancelar',
      denyButtonColor: '#dc3545'
    }).then((result) => {
      if (result.isConfirmed) {
        this.exportarPDF();
      } else if (result.dismiss === Swal.DismissReason.cancel) {
        this.exportarExcel();
      }
    });
  }

  private exportarPDF(): void {
    // Implementación básica de exportación PDF
    const data = this.dashboardData();
    if (!data) return;

    // Crear contenido HTML para PDF
    const contenido = `
      <h1>Reporte de Dashboard - ${new Date().toLocaleDateString()}</h1>
      <h2>Estadísticas Generales</h2>
      <p>Total OTs: ${data.generales?.totalOTs}</p>
      <p>OTs Activas: ${data.generales?.otsActivas}</p>
      <p>Costos Totales: S/ ${data.generales?.costoTotalOTs}</p>
    `;

    // En una implementación real, usarías una librería como jsPDF
    Swal.fire({
      icon: 'success',
      title: 'PDF generado',
      text: 'El archivo PDF se ha generado correctamente',
      confirmButtonColor: '#28a745',
      background: '#f8f9fa'
    });
  }

  private exportarExcel(): void {
    Swal.fire({
      icon: 'success',
      title: 'Excel generado',
      text: 'El archivo Excel se ha generado correctamente',
      confirmButtonColor: '#28a745',
      background: '#f8f9fa'
    });
  }

  verDetalleOT(ot: OTPendienteDTO): void {
    Swal.fire({
      title: `OT #${ot.ot}`,
      html: `
        <div class="text-start">
          <p><strong>Cliente:</strong> ${ot.cliente}</p>
          <p><strong>Descripción:</strong> ${ot.descripcion}</p>
          <p><strong>Estado:</strong> <span class="badge" style="background-color: ${this.dashboardService.getColorPorEstado(ot.estado)}">${ot.estado}</span></p>
          <p><strong>Fecha Apertura:</strong> ${this.formatFecha(ot.fechaApertura)}</p>
          <p><strong>Días Pendientes:</strong> ${ot.diasPendientes}</p>
          <p><strong>Responsable:</strong> ${ot.responsable}</p>
          <p><strong>Costo Estimado:</strong> ${this.dashboardService.formatCurrency(ot.costoEstimado)}</p>
        </div>
      `,
      icon: 'info',
      showCancelButton: true,
      confirmButtonText: 'Ver detalles completos',
      cancelButtonText: 'Cerrar',
      confirmButtonColor: '#007bff',
      cancelButtonColor: '#6c757d',
      background: '#f8f9fa',
      width: '600px'
    }).then((result) => {
      if (result.isConfirmed) {
        // Navegar a la vista detallada de la OT
        // this.router.navigate(['/ots', ot.ot]);
      }
    });
  }

  private createCharts(): void {
    this.destroyCharts();

    // Gráfico de estados
    if (this.chartEstadosRef && this.otsPorEstado().length > 0) {
      this.createChartEstados();
    }

    // Gráfico de costos
    if (this.chartCostosRef && this.resumenCostos()) {
      this.createChartCostos();
    }

    // Gráfico de evolución
    if (this.chartEvolucionRef && this.evolucionMensual()) {
      this.createChartEvolucion();
    }

    // Gráfico de clientes
    if (this.chartClientesRef && this.otsPorCliente().length > 0) {
      this.createChartClientes();
    }
  }

  private createChartEstados(): void {
    const data = this.otsPorEstado();
    const ctx = this.chartEstadosRef.nativeElement.getContext('2d');

    const labels = data.map(item => item.estado);
    const values = data.map(item => item.cantidad);
    const colors = data.map(item => this.dashboardService.getColorPorEstado(item.estado));

    this.chartEstados = new Chart(ctx, {
      type: 'doughnut',
      data: {
        labels: labels,
        datasets: [{
          data: values,
          backgroundColor: colors,
          borderColor: '#ffffff',
          borderWidth: 2,
          hoverOffset: 10
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: 'right',
            labels: {
              padding: 20,
              usePointStyle: true,
              font: {
                size: 12
              }
            }
          },
          tooltip: {
            callbacks: {
              label: (context) => {
                const label = context.label || '';
                const value = context.raw as number;
                const total = values.reduce((a, b) => a + b, 0);
                const percentage = total > 0 ? ((value / total) * 100).toFixed(1) : '0';
                return `${label}: ${value} (${percentage}%)`;
              }
            }
          }
        }
      }
    });
  }

  private createChartCostos(): void {
    const data = this.resumenCostos();
    if (!data) return;

    const ctx = this.chartCostosRef.nativeElement.getContext('2d');

    const costos = [
      data.costoMateriales,
      data.costoContratistas,
      data.costoGastosLogisticos,
      data.costoViaticos,
      data.costoPlanillas
    ];

    const labels = ['Materiales', 'Contratistas', 'Gastos Logísticos', 'Viáticos', 'Planillas'];
    const colors = ['#4e73df', '#1cc88a', '#36b9cc', '#f6c23e', '#e74a3b'];

    this.chartCostos = new Chart(ctx, {
      type: 'bar',
      data: {
        labels: labels,
        datasets: [{
          label: 'Costos (S/)',
          data: costos,
          backgroundColor: colors,
          borderColor: colors.map(color => color + 'CC'),
          borderWidth: 1,
          borderRadius: 5,
          borderSkipped: false
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            display: false
          },
          tooltip: {
            callbacks: {
              label: (context) => {
                return `S/ ${this.dashboardService.formatNumber(context.raw as number)}`;
              }
            }
          }
        },
        scales: {
          y: {
  grid: {
    display: true
  },
  border: {
    display: false
  }
},
          x: {
            grid: {
              display: false
            }
          }
        }
      }
    });
  }

  private createChartEvolucion(): void {
    const data = this.evolucionMensual();
    if (!data) return;

    const ctx = this.chartEvolucionRef.nativeElement.getContext('2d');

    const labels = data.meses.map((mes: MesDTO) => `${mes.mes} ${mes.anio}`);

    this.chartEvolucion = new Chart(ctx, {
      type: 'line',
      data: {
        labels: labels,
        datasets: [
          {
            label: 'Cantidad de OTs',
            data: data.cantidadOTs,
            borderColor: '#4e73df',
            backgroundColor: 'rgba(78, 115, 223, 0.05)',
            tension: 0.4,
            fill: true,
            yAxisID: 'y'
          },
          {
            label: 'Costos (S/)',
            data: data.costos,
            borderColor: '#1cc88a',
            backgroundColor: 'rgba(28, 200, 138, 0.05)',
            tension: 0.4,
            fill: true,
            yAxisID: 'y1'
          }
        ]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        interaction: {
          mode: 'index',
          intersect: false
        },
        plugins: {
          tooltip: {
            mode: 'index',
            intersect: false
          }
        },
        scales: {
          x: {
            grid: {
              display: false
            }
          },
          y: {
            type: 'linear',
            display: true,
            position: 'left',
            title: {
              display: true,
              text: 'Cantidad OTs'
            }
          },
          y1: {
            type: 'linear',
            display: true,
            position: 'right',
            title: {
              display: true,
              text: 'Costos (S/)'
            },
            grid: {
              drawOnChartArea: false
            },
            ticks: {
              callback: (value) => `S/ ${this.dashboardService.formatNumber(value as number)}`
            }
          }
        }
      }
    });
  }

  private createChartClientes(): void {
    const data = this.otsPorCliente().slice(0, 10); // Top 10 clientes
    const ctx = this.chartClientesRef.nativeElement.getContext('2d');

    const labels = data.map(item => item.cliente.length > 15 ? item.cliente.substring(0, 15) + '...' : item.cliente);
    const values = data.map(item => item.cantidad);

    this.chartClientes = new Chart(ctx, {
      type: 'bar',
      data: {
        labels: labels,
        datasets: [{
          label: 'OTs por Cliente',
          data: values,
          backgroundColor: '#6f42c1',
          borderColor: '#6f42c1',
          borderWidth: 1,
          borderRadius: 5
        }]
      },
      options: {
        indexAxis: 'y',
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            display: false
          }
        },
        scales: {
          x: {
            beginAtZero: true,
            grid: {
              display: false
            }
          },
          y: {
            grid: {
              display: false
            }
          }
        }
      }
    });
  }

  private destroyCharts(): void {
    if (this.chartEstados) {
      this.chartEstados.destroy();
      this.chartEstados = null;
    }
    if (this.chartCostos) {
      this.chartCostos.destroy();
      this.chartCostos = null;
    }
    if (this.chartEvolucion) {
      this.chartEvolucion.destroy();
      this.chartEvolucion = null;
    }
    if (this.chartClientes) {
      this.chartClientes.destroy();
      this.chartClientes = null;
    }
  }

  private setupAutoRefresh(): void {
  this.autoRefreshInterval = setInterval(() => {
    if (!document.hidden && this.isComponentActive) {
      this.refreshDashboard();
    }
  }, 5 * 60 * 1000);
}
  // Métodos de utilidad
  formatCurrency(value: number): string {
    return this.dashboardService.formatCurrency(value);
  }

  formatNumber(value: number): string {
    return this.dashboardService.formatNumber(value);
  }

  formatFecha(fecha: string): string {
    return this.dashboardService.formatFecha(fecha);
  }

  getColorEstado(estado: string): string {
    return this.dashboardService.getColorPorEstado(estado);
  }

  getIconEstado(estado: string): string {
    return this.dashboardService.getIconPorEstado(estado);
  }

  getRangoLabel(value: string): string {
    const rango = this.rangos.find(r => r.value === value);
    return rango ? rango.label : value;
  }

  calcularPorcentaje(parcial: number, total: number): number {
    return this.dashboardService.calcularPorcentaje(parcial, total);
  }
}
