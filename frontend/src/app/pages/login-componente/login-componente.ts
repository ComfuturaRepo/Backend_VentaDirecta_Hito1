import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import Swal from 'sweetalert2';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login-componente.html',
  styleUrl: './login-componente.css'
})
export class LoginComponent {
  loginForm: FormGroup;
  isLoading = false;
  showPassword = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });

    // Redirige si ya está autenticado
    if (this.authService.isAuthenticatedSync) {
      this.redirectToReturnUrlOrDashboard();
    }
  }

  togglePassword() {
    this.showPassword = !this.showPassword;
  }

  onSubmit() {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      Swal.fire({
        icon: 'warning',
        title: 'Datos incompletos',
        text: 'Por favor completa todos los campos correctamente',
        timer: 2200,
        showConfirmButton: false,
        toast: true,
        position: 'top-end',
        background: '#fef3c7',
        color: '#92400e',
        iconColor: '#f59e0b'
      });
      return;
    }

    this.isLoading = true;

    const credentials = this.loginForm.value;

    this.authService.login(credentials).subscribe({
      // En LoginComponent.onSubmit()
next: () => {
  const username = this.authService.currentUser?.username || 'Usuario';

  Swal.fire({
    title: '¡Acceso Concedido!',
    text: `Bienvenido ${username} al sistema Comfutura`,
    icon: 'success',
    timer: 1800,
    showConfirmButton: false
  });

  // ✅ Espera que el estado se estabilice
  setTimeout(() => {
    console.log('✅ LoginComponent: Redirigiendo después de login');
    console.log('✅ Estado actual:', {
      isAuthenticatedSync: this.authService.isAuthenticatedSync,
      currentUser: this.authService.currentUser,
      token: this.authService.token
    });
    this.redirectToReturnUrlOrDashboard();
  }, 300); // Aumenta a 300ms si es necesario
},
      error: err => {
        this.isLoading = false;

        Swal.fire({
          title: 'Error de Acceso',
          text: err.message || 'Credenciales incorrectas. Verifica tus datos e intenta nuevamente.',
          icon: 'error',
          confirmButtonColor: '#dc2626',
          background: '#fef2f2',
          color: '#991b1b',
          iconColor: '#dc2626',
          confirmButtonText: 'Reintentar',
          showCancelButton: true,
          cancelButtonText: '¿Necesitas ayuda?',
          cancelButtonColor: '#6b7280'
        }).then((result) => {
          if (result.dismiss === Swal.DismissReason.cancel) {
            // Aquí podrías redirigir a una página de ayuda
            console.log('Usuario necesita ayuda');
          }
        });
      },
      complete: () => (this.isLoading = false)
    });
  }

  private redirectToReturnUrlOrDashboard() {
    const returnUrl = this.activatedRoute.snapshot.queryParamMap.get('returnUrl');

    if (returnUrl) {
      this.router.navigateByUrl(returnUrl);
    } else {
      this.router.navigate(['/dashboard']);
    }
  }
}
