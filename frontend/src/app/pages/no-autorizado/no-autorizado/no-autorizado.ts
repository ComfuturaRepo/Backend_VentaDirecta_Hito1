import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-no-autorizado',
  template: `
    <div class="container mt-5">
      <div class="row justify-content-center">
        <div class="col-md-6 text-center">
          <div class="card shadow">
            <div class="card-body">
              <i class="bi bi-shield-exclamation display-1 text-warning"></i>
              <h2 class="mt-3">Acceso No Autorizado</h2>
              <p class="text-muted">
                No tienes los permisos necesarios para acceder a esta página.
              </p>
              <button class="btn btn-primary mt-3" (click)="volver()">
                <i class="bi bi-arrow-left"></i> Volver al Dashboard
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .card {
      border: none;
      border-radius: 10px;
    }
  `]
})
export class NoAutorizadoComponent {
  constructor(private router: Router) {}

  volver(): void {
    this.router.navigate(['/dashboard']);
  }
}
