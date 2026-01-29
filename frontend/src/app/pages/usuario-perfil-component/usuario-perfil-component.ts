// src/app/pages/usuario-perfil/usuario-perfil.component.ts
import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Subscription } from 'rxjs';
import { ChangePasswordRequestDTO, PerfilResponseDTO, PerfilService, UpdatePerfilRequestDTO } from '../../service/perfil.service';


@Component({
  selector: 'app-usuario-perfil',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule
  ],
 templateUrl: './usuario-perfil-component.html',
  styleUrls: ['./usuario-perfil-component.css']
})
export class UsuarioPerfilComponent implements OnInit, OnDestroy {
  // Datos del perfil
  perfil: PerfilResponseDTO | null = null;

  // Estados
  isLoading: boolean = false;
  isEditing: boolean = false;
  isChangingPassword: boolean = false;

  // Formularios
  perfilForm: FormGroup;
  passwordForm: FormGroup;

  // Mostrar/ocultar contraseñas
  showCurrentPassword: boolean = false;
  showNewPassword: boolean = false;
  showConfirmPassword: boolean = false;

  // Suscripciones
  private subscriptions: Subscription[] = [];

  constructor(
    public perfilService: PerfilService,
    private fb: FormBuilder
  ) {
    this.perfilForm = this.createPerfilForm();
    this.passwordForm = this.createPasswordForm();
  }

  ngOnInit(): void {
    this.loadPerfil();
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  // ========== CARGAR DATOS ==========

  loadPerfil(): void {
    this.isLoading = true;

    const sub = this.perfilService.getMiPerfil().subscribe({
      next: (perfil) => {
        this.perfil = perfil;
        this.patchFormWithPerfilData();
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error al cargar perfil:', error);
        this.isLoading = false;
        this.perfilService.showError('Error', 'No se pudo cargar el perfil');
      }
    });

    this.subscriptions.push(sub);
  }

  // ========== FORMULARIOS ==========

  private createPerfilForm(): FormGroup {
    return this.fb.group({
      nombres: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
      apellidos: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
      correoCorporativo: ['', [Validators.required, Validators.email]],
      celular: ['', [Validators.required, Validators.pattern('^[0-9]{9}$')]]
    });
  }

  private createPasswordForm(): FormGroup {
    return this.fb.group({
      currentPassword: ['', [Validators.required]],
      newPassword: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(100)]],
      confirmPassword: ['', [Validators.required]]
    }, { validators: this.passwordMatchValidator });
  }

  private passwordMatchValidator(g: FormGroup) {
    const newPassword = g.get('newPassword')?.value;
    const confirmPassword = g.get('confirmPassword')?.value;

    if (newPassword !== confirmPassword) {
      g.get('confirmPassword')?.setErrors({ mismatch: true });
      return { mismatch: true };
    }

    g.get('confirmPassword')?.setErrors(null);
    return null;
  }

  private patchFormWithPerfilData(): void {
    if (this.perfil) {
      this.perfilForm.patchValue({
        nombres: this.perfil.nombres,
        apellidos: this.perfil.apellidos,
        correoCorporativo: this.perfil.correoCorporativo,
        celular: this.perfil.celular
      });
    }
  }

  // ========== ACCIONES ==========

  toggleEditMode(): void {
    this.isEditing = !this.isEditing;
    if (!this.isEditing) {
      this.patchFormWithPerfilData();
    }
  }

  toggleChangePassword(): void {
    this.isChangingPassword = !this.isChangingPassword;
    if (!this.isChangingPassword) {
      this.passwordForm.reset();
    }
  }

  savePerfil(): void {
    if (this.perfilForm.invalid) {
      this.marcarCamposInvalidos(this.perfilForm);
      return;
    }

    const formData: UpdatePerfilRequestDTO = this.perfilForm.value;

    this.isLoading = true;

    const sub = this.perfilService.updateMiPerfil(formData).subscribe({
      next: (perfilActualizado) => {
        this.perfil = perfilActualizado;
        this.isEditing = false;
        this.isLoading = false;
        this.perfilService.showSuccess('Éxito', 'Perfil actualizado exitosamente');
      },
      error: (error) => {
        console.error('Error al actualizar perfil:', error);
        this.isLoading = false;
        this.perfilService.showError('Error', 'No se pudo actualizar el perfil');
      }
    });

    this.subscriptions.push(sub);
  }

  changePassword(): void {
    if (this.passwordForm.invalid) {
      this.marcarCamposInvalidos(this.passwordForm);
      return;
    }

    const formData: ChangePasswordRequestDTO = this.passwordForm.value;

    // Validar fortaleza de contraseña
    const validation = this.perfilService.validatePasswordStrength(formData.newPassword);
    if (!validation.valid) {
      this.perfilService.showError('Error', validation.message);
      return;
    }

    this.isLoading = true;

    const sub = this.perfilService.cambiarPassword(formData).subscribe({
      next: (response) => {
        this.passwordForm.reset();
        this.isChangingPassword = false;
        this.isLoading = false;
        this.perfilService.showSuccess('Éxito', response.message || 'Contraseña cambiada exitosamente');
      },
      error: (error) => {
        console.error('Error al cambiar contraseña:', error);
        this.isLoading = false;
        this.perfilService.showError('Error', 'No se pudo cambiar la contraseña');
      }
    });

    this.subscriptions.push(sub);
  }

  // ========== HELPERS ==========

  private marcarCamposInvalidos(formGroup: FormGroup): void {
    Object.keys(formGroup.controls).forEach(key => {
      const control = formGroup.get(key);
      if (control?.invalid) {
        control.markAsTouched();
      }
    });
  }

  isFieldInvalid(form: FormGroup, fieldName: string): boolean {
    const field = form.get(fieldName);
    return field ? field.invalid && field.touched : false;
  }

  getErrorMessage(form: FormGroup, fieldName: string): string {
    const field = form.get(fieldName);
    if (!field || !field.errors) return '';

    if (field.errors['required']) {
      return 'Este campo es obligatorio';
    }

    if (field.errors['email']) {
      return 'Debe ser un email válido';
    }

    if (field.errors['minlength']) {
      return `Mínimo ${field.errors['minlength'].requiredLength} caracteres`;
    }

    if (field.errors['maxlength']) {
      return `Máximo ${field.errors['maxlength'].requiredLength} caracteres`;
    }

    if (field.errors['pattern']) {
      if (fieldName === 'celular') {
        return 'El celular debe tener 9 dígitos';
      }
      return 'Formato inválido';
    }

    if (field.errors['mismatch']) {
      return 'Las contraseñas no coinciden';
    }

    return '';
  }

  getAvatarText(): string {
    if (!this.perfil) return 'U';
    return (this.perfil.nombres?.charAt(0) || 'U') + (this.perfil.apellidos?.charAt(0) || '');
  }

  getNivelClass(): string {
    if (!this.perfil) return 'badge bg-secondary';
    return this.perfilService.getNivelBadgeClass(this.perfil.nivelCodigo);
  }

  formatFecha(fecha: string): string {
    return this.perfilService.formatFecha(fecha);
  }

  formatFechaRelativa(fecha: string): string {
    return this.perfilService.formatFechaRelativa(fecha);
  }

  // ========== VALIDACIONES EN TIEMPO REAL ==========

  onPasswordInput(): void {
    const newPassword = this.passwordForm.get('newPassword')?.value;
    if (newPassword) {
      const validation = this.perfilService.validatePasswordStrength(newPassword);
      if (!validation.valid) {
        this.passwordForm.get('newPassword')?.setErrors({ strength: true });
      }
    }
  }
}
