import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DEPARTMAN_URL, FAKULTET_URL, STATUS_URL, STUDENT_URL, STUDENT_ZADEPARTMAN_URL } from '../app.constants';
import { Departman } from '../models/departman';
import { Fakultet } from '../models/fakultet';
import { Student } from '../models/student';

@Injectable({
  providedIn: 'root'
})
export class StudentService {

  constructor(private httpClient: HttpClient)  { }

  public getStudentZaDepartman(idDepartman: number): Observable<any> 
  {
    return this.httpClient.get(`${STUDENT_ZADEPARTMAN_URL}/${idDepartman}`);;
  } 

  public addStudent(student: Student): Observable<any> 
  {
    student.id = 0;
    return this.httpClient.post(`${STUDENT_URL}`, student);
  }

  public updateStudent(student: Student): Observable<any> 
  {
    return this.httpClient.put(`${STUDENT_URL}`, student);
  }

  public deleteStudent(id: number): Observable<any> 
  {
    return this.httpClient.delete(`${STUDENT_URL}/${id}`,);
  }
}