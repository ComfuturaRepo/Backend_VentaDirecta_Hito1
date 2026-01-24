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

    // Si ya está autenticado → redirigimos
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
      next: () => {
        const username = this.authService.currentUser?.username || 'Usuario';

        Swal.fire({
          title: '¡Acceso concedido!',
          text: `Bienvenido ${username}`,
          icon: 'success',
          timer: 1800,
          showConfirmButton: false,
          toast: true,
          position: 'top-end',
          background: 'linear-gradient(135deg, #dc2626 0%, #2563eb 100%)',
          color: '#ffffff',
          iconColor: '#ffffff',
          customClass: {
            popup: 'swal2-popup-login'
          }
        });

        this.redirectToReturnUrlOrDashboard();
      },
      error: err => {
        this.isLoading = false;

        Swal.fire({
          title: 'Error de acceso',
          text: err.message || 'Credenciales incorrectas. Verifica tus datos.',
          icon: 'error',
          confirmButtonColor: '#dc2626',
          background: '#fef2f2',
          color: '#991b1b',
          iconColor: '#dc2626',
          confirmButtonText: 'Reintentar'
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