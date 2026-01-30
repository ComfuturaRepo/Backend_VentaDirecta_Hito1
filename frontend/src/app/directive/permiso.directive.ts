import { Directive, Input, TemplateRef, ViewContainerRef, OnDestroy, NgZone } from '@angular/core';
import { Subscription, distinctUntilChanged } from 'rxjs';
import { PermisoService } from '../service/permiso.service';

@Directive({
  selector: '[appPermiso]',
  standalone: true
})
export class PermisoDirective implements OnDestroy {
  private permisos: string[] = [];
  private condicion: 'OR' | 'AND' = 'OR';
  private permisosSubscription: Subscription;
  private tienePermisoAnterior: boolean | null = null;
  private vistaCreada = false;

  constructor(
    private templateRef: TemplateRef<any>,
    private viewContainer: ViewContainerRef,
    private permisoService: PermisoService,
    private ngZone: NgZone
  ) {
    // Usar NgZone.run para asegurar que estemos en el contexto correcto
    this.permisosSubscription = this.permisoService.permisos$
      .pipe(distinctUntilChanged((prev, curr) =>
        JSON.stringify(prev) === JSON.stringify(curr)
      ))
      .subscribe(() => {
        this.ngZone.run(() => {
          this.actualizarVista();
        });
      });
  }

  @Input()
  set appPermiso(val: string | string[]) {
    if (typeof val === 'string') {
      this.permisos = [val];
    } else {
      this.permisos = val;
    }
    // Ejecutar en el próximo ciclo de detección de cambios
    setTimeout(() => {
      this.actualizarVista();
    });
  }

  @Input()
  set appPermisoCondicion(condicion: 'OR' | 'AND') {
    this.condicion = condicion;
    setTimeout(() => {
      this.actualizarVista();
    });
  }

  private actualizarVista(): void {
    const tienePermiso = this.verificarPermisos();

    // Solo actualizar si cambió el estado
    if (this.tienePermisoAnterior !== tienePermiso) {
      this.tienePermisoAnterior = tienePermiso;

      if (tienePermiso) {
        // Verificar que no haya ya una vista creada
        if (!this.vistaCreada) {
          this.viewContainer.createEmbeddedView(this.templateRef);
          this.vistaCreada = true;
        }
      } else {
        if (this.vistaCreada) {
          this.viewContainer.clear();
          this.vistaCreada = false;
        }
      }
    }
  }

  private verificarPermisos(): boolean {
    if (this.permisos.length === 0) {
      return true;
    }

    if (this.condicion === 'AND') {
      return this.permisoService.tieneTodosPermisos(this.permisos);
    } else {
      return this.permisoService.tieneAlgunPermiso(this.permisos);
    }
  }

  ngOnDestroy(): void {
    if (this.permisosSubscription) {
      this.permisosSubscription.unsubscribe();
    }
    this.tienePermisoAnterior = null;
    this.vistaCreada = false;
  }
}
