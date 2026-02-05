import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'trabajadorModelTs'
})
export class TrabajadorModelTsPipe implements PipeTransform {

  transform(value: unknown, ...args: unknown[]): unknown {
    return null;
  }

}
