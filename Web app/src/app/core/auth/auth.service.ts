import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, EMPTY, of, Subject, throwError } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { User } from 'src/app/core/user';
import { LogService } from '../log.service';
import { TokenStorageService } from './token-storage.service';

interface UserDto
{
  user: User;
  token: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private user$ = new BehaviorSubject<User | null>(null);
  private apiUrl = 'http://localhost:4050/api/auth/';
  private redirectUrlAfterLogin = "";

  constructor(private httpClient: HttpClient, private tokenStorage: TokenStorageService, private logService: LogService) { }

  login(email: string, password: string)
  {
    const loginCredentials = {email, password};
    console.log('login credentials', loginCredentials);


    return this.httpClient.post<UserDto>(this.apiUrl + 'login', loginCredentials).pipe
    (
      switchMap(({user, token}) =>
      {
        this.setUser(user);
        this.tokenStorage.setToken(token);
        console.log('user found', user);
        return of(this.redirectUrlAfterLogin);
      }),
      catchError(e =>
      {
        this.logService.log('Server Error Occurred: ' + e.error.message, e);
        return throwError('Your login details could not be verified. Try again.');
      })
    );
  }

  logout() 
  {
    // remove user from subject
    // remove token from local storage
    this.tokenStorage.removeToken();
    this.setUser(null);
    console.log('user logout');
  }

  get user()
  {
    return this.user$.asObservable();
  }

  register(userToSave: any) 
  {
    return this.httpClient.post<any>(this.apiUrl + 'register', userToSave).pipe
    (
      switchMap(({user, token}) =>
      {
        return this.setUserAfterUserFoundServer(user, token);
      }),
      catchError(e =>
      {
        this.logService.log('Server Error Occurred: ' + e.error.message, e);
        return throwError('Your register details could not be verified. Try again.');
      })
    );
  }

  findMe()
  {
    const token = this.tokenStorage.getToken();

    if(!token)
    {
      return EMPTY;
    }
    return this.httpClient.get<any>(this.apiUrl + "findme").pipe
    (
      switchMap(({user, token}) =>
        {
          return this.setUserAfterUserFoundServer(user, token);
        }),
        catchError(e =>
          {
            console.log('Your login details could not be verified. Please try again', e);
            return throwError('Your login details could not be verified. Please try again');
          })
    );
  }

  private setUserAfterUserFoundServer(user: User, token: string)
  {
    this.setUser(user);
    this.tokenStorage.setToken(token);

    return of(user);
  }

  private setUser(user : any)
  {
    if (user != null)
    {
      const newUser = {...user, id: user._id};
      this.user$.next(newUser);
    }
    else
    {
      this.user$.next(null);
    }
  }

  get isUserLoggedIn()
  {
    return this.user$.value != null;
  }

  set redirectUrl(url:string)
  {
    this.redirectUrlAfterLogin = url;
  }

  get loggedInUser()
  {
    return this.user$.value;
  }
}


