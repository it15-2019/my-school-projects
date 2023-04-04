import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MatTextColumn } from '@angular/material/table';
import { Observable } from 'rxjs';
import { STATUS_URL } from '../app.constants';
import { Status } from '../models/status';

@Injectable({
  providedIn: 'root'
})
export class StatusService {

  constructor(private httpClient: HttpClient) { }

  public getAllStatus(): Observable<any> 
  {
    return this.httpClient.get(`${STATUS_URL}`);
  } 

  public addStatus(status: Status): Observable<any> 
  {
    status.id = 0;
    return this.httpClient.post(`${STATUS_URL}`, status);
  }

  public updateStatus(status: Status): Observable<any> 
  {
    return this.httpClient.put(`${STATUS_URL}`, status);
  }

  public deleteStatus(id: number): Observable<any> 
  {
    return this.httpClient.delete(`${STATUS_URL}/${id}`,);
  }
}
