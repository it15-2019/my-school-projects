import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DEPARTMAN_URL, FAKULTET_URL } from '../app.constants';
import { Departman } from '../models/departman';
import { Fakultet } from '../models/fakultet';

@Injectable({
  providedIn: 'root'
})
export class DepartmanService {

  constructor(private httpClient: HttpClient) { }

  public getAllDepartman(): Observable<any> 
  {
    return this.httpClient.get(`${DEPARTMAN_URL}`);
  } 

  public addDepartman(departman: Departman): Observable<any> 
  {
    departman.id = 0;
    return this.httpClient.post(`${DEPARTMAN_URL}`, departman);
  }

  public updateDepartman(departman: Departman): Observable<any> 
  {
    return this.httpClient.put(`${DEPARTMAN_URL}`, departman);
  }

  public deleteDepartman(id: number): Observable<any> 
  {
    return this.httpClient.delete(`${DEPARTMAN_URL}/${id}`,);
  }
}