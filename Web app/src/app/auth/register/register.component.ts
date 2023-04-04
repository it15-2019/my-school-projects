import { group } from '@angular/animations';
import { analyzeAndValidateNgModules, ThrowStmt } from '@angular/compiler';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormControlName, FormGroup, Validators, ValidationErrors, FormBuilder} from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/core/auth/auth.service';
import { AbstractControl, ValidatorFn } from '@angular/forms';
import { BehaviorSubject } from 'rxjs';



@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})

export class RegisterComponent implements OnInit {

  error!: BehaviorSubject<string>;
  userForm = new FormGroup ( 
    {
      fullname : new FormControl('', [Validators.required]),
      email : new FormControl('', [Validators.required, Validators.email]),
      password : new FormControl('', [Validators.required, Validators.minLength(6)]),
      repeatPassword : new FormControl('', [Validators.required, this.passwordsMatch])
    });
  
  constructor(private router: Router, private authService: AuthService) {}

  passwordsMatch(control: FormControl)
  {
    const password = control.root.get('password');
    return password && control.value !== password.value
      ? {
          passwordMatch: true
        }
      : null;
  }

  register() 
  {
    if (!this.userForm.valid) {
      return;
    }

    const user = this.userForm.getRawValue();
    this.setError('');
    this.authService
      .register(user)
      .subscribe( 
        data => this.router.navigate(['/']),
        e => (this.setError(e))
      );
  }


  private setError(msg : any)
  {
    return this.error.next(msg);
  }

  get fullname() : any
  {
    return this.userForm.get('fullname');
  }

  get email() : any
  {
    return this.userForm.get('email');
  }

  get password() : any
  {
    return this.userForm.get('password');
  }

  get repeatPassword() : any 
  {
    return this.userForm.get('repeatPassword');
  }

  ngOnInit() {
    this.error = new BehaviorSubject(''); 
  }
}