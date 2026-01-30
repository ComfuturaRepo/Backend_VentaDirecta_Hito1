import { Pipe, PipeTransform } from '@angular/core';
import { PermisoService } from '../service/permiso.service';

@Pipe({
  name: 'tienePermiso',
  pure: false // Para que se actualice cuando cambien los permisos
})
export class TienePermisoPipe implements PipeTransform {
  constructor(private permisoService: PermisoService) {}

  transform(permisosRequeridos: string | string[], condicion: 'OR' | 'AND' = 'OR'): boolean {
    if (!permisosRequeridos) {
      return true;
    }

    const permisos = Array.isArray(permisosRequeridos)
      ? permisosRequeridos
      : [permisosRequeridos];

    if (condicion === 'AND') {
      return this.permisoService.tieneTodosPermisos(permisos);
    } else {
      return this.permisoService.tieneAlgunPermiso(permisos);
    }
  }
}
