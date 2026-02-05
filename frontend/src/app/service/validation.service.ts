// src/app/services/validation.service.ts
import { Injectable } from '@angular/core';
import { FormControl, ValidationErrors, AbstractControl, ValidatorFn } from '@angular/forms';
import { Observable, of } from 'rxjs';
import { map, catchError, debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';
import { TrabajadorService } from './trabajador.service';

@Injectable({
  providedIn: 'root'
})
export class ValidationService {

  constructor(private trabajadorService: TrabajadorService) {}

  // Validador síncrono para DNI
  dniValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      if (!control.value) {
        return null;
      }

      const dni = control.value.trim();

      // Validar formato: 8 dígitos
      if (!/^\d{8}$/.test(dni)) {
        return { dniFormat: true };
      }

      return null;
    };
  }

  // Validador síncrono para celular
  celularValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      if (!control.value) {
        return null;
      }

      const celular = control.value.trim();

      // Validar formato: 9 dígitos
      if (!/^\d{9}$/.test(celular)) {
        return { celularFormat: true };
      }

      return null;
    };
  }

  // Validador síncrono para email
  emailValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      if (!control.value) {
        return null;
      }

      const email = control.value.trim();

      // Validar formato básico de email
      if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
        return { emailFormat: true };
      }

      return null;
    };
  }

  // Validador asíncrono para DNI único
  dniUniqueValidator(excludeId?: number): ValidatorFn {
    return (control: AbstractControl): Observable<ValidationErrors | null> => {
      if (!control.value || control.errors?.['dniFormat']) {
        return of(null);
      }

      return this.trabajadorService.checkDniExists(control.value).pipe(
        debounceTime(500),
        distinctUntilChanged(),
        map(exists => {
          return exists ? { dniExists: true } : null;
        }),
        catchError(() => of(null))
      );
    };
  }

  // Validador asíncrono para email único
  emailUniqueValidator(excludeId?: number): ValidatorFn {
    return (control: AbstractControl): Observable<ValidationErrors | null> => {
      if (!control.value || control.errors?.['emailFormat']) {
        return of(null);
      }

      return this.trabajadorService.checkEmailExists(control.value).pipe(
        debounceTime(500),
        distinctUntilChanged(),
        map(exists => {
          return exists ? { emailExists: true } : null;
        }),
        catchError(() => of(null))
      );
    };
  }

  // Validador para campos requeridos
  requiredValidator(message: string = 'Este campo es requerido'): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value;

      if (value === null || value === undefined || value === '') {
        return { required: { message } };
      }

      if (typeof value === 'string' && value.trim() === '') {
        return { required: { message } };
      }

      return null;
    };
  }

  // Validador para longitud mínima
  minLengthValidator(min: number, message?: string): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      if (!control.value) {
        return null;
      }

      const value = control.value.toString();

      if (value.length < min) {
        return {
          minlength: {
            requiredLength: min,
            actualLength: value.length,
            message: message || `Mínimo ${min} caracteres`
          }
        };
      }

      return null;
    };
  }

  // Validador para longitud máxima
  maxLengthValidator(max: number, message?: string): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      if (!control.value) {
        return null;
      }

      const value = control.value.toString();

      if (value.length > max) {
        return {
          maxlength: {
            requiredLength: max,
            actualLength: value.length,
            message: message || `Máximo ${max} caracteres`
          }
        };
      }

      return null;
    };
  }

  // Validador para coincidencia de campos (ej: confirmar contraseña)
  matchValidator(controlName: string, matchingControlName: string): ValidatorFn {
    return (formGroup: AbstractControl): ValidationErrors | null => {
      const control = formGroup.get(controlName);
      const matchingControl = formGroup.get(matchingControlName);

      if (!control || !matchingControl) {
        return null;
      }

      if (matchingControl.errors && !matchingControl.errors['mismatch']) {
        return null;
      }

      if (control.value !== matchingControl.value) {
        matchingControl.setErrors({ mismatch: true });
        return { mismatch: true };
      } else {
        matchingControl.setErrors(null);
        return null;
      }
    };
  }
}
