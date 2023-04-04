import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';
import { MAT_SINGLE_DATE_SELECTION_MODEL_FACTORY } from '@angular/material/datepicker';
import { Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { AuthService } from 'src/app/core/auth/auth.service';

@Component
({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})

export class LoginComponent implements OnInit 
{
  email!: string;
  password!: string;
  error!: BehaviorSubject<string>;

  constructor(private router: Router, private authService: AuthService) {}

  login(): void 
  {
    this.setError('');
    this.authService
    .login(this.email, this.password)
    .subscribe
    (
      data => this.router.navigate(['']),
      e => (this.setError(e))
    );
  }

  ngOnInit()
  {  
    this.error = new BehaviorSubject(''); 
  }

  private setError(msg : any)
  {
    return this.error.next(msg);
  }

}
