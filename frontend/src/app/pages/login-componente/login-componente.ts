import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router'; // ← importante agregar ActivatedRoute
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
    private activatedRoute: ActivatedRoute // ← Necesario para leer queryParams
  ) {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });

    // Si ya está autenticado → redirigimos (muy buena práctica)
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
        text: 'Por favor completa todos los campos',
        timer: 2200,
        showConfirmButton: false,
        toast: true,
        position: 'top-end'
      });
      return;
    }

    this.isLoading = true;

    const credentials = this.loginForm.value;

    this.authService.login(credentials).subscribe({
      next: () => {
        const username = this.authService.currentUser?.username || 'Usuario';

        Swal.fire({
          title: '¡Bienvenido!',
          text: `Hola ${username}`,
          icon: 'success',
          timer: 1800,
          showConfirmButton: false,
          toast: true,
          position: 'top-end',
          background: '#d4edda',
          color: '#155724',
          iconColor: '#155724'
        });

        // ← Aquí está la solución principal !!
        this.redirectToReturnUrlOrDashboard();
      },
      error: err => {
        this.isLoading = false;

        Swal.fire({
          title: 'Error',
          text: err.message || 'Credenciales incorrectas',
          icon: 'error',
          confirmButtonColor: '#dc3545'
        });
      },
      complete: () => (this.isLoading = false)
    });
  }

  /**
   * Redirige al returnUrl si existe, sino va a dashboard
   */
  private redirectToReturnUrlOrDashboard() {
    const returnUrl = this.activatedRoute.snapshot.queryParamMap.get('returnUrl');

    // Si hay returnUrl → vamos ahí
    // Si no → dashboard por defecto
    if (returnUrl) {
      this.router.navigateByUrl(returnUrl);
    } else {
      this.router.navigate(['/dashboard']);
    }
  }
}
