import { Injectable } from '@angular/core';
import { ActivatedRoute, ActivatedRouteSnapshot, CanActivate, Router } from '@angular/router';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate 
{
  path!: ActivatedRouteSnapshot;
  route!: ActivatedRouteSnapshot;

  constructor(private authService: AuthService, private router: Router) { }

  canActivate()
  {
    if (this.authService.isUserLoggedIn)
    {
      return true;
    }
    this.router.navigate(['auth']);
    return false;
  }
}
