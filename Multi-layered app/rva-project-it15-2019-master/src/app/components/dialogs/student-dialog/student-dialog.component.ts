import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Subscription } from 'rxjs';
import { Departman } from 'src/app/models/departman';
import { Status } from 'src/app/models/status';
import { Student } from 'src/app/models/student';
import { StudentService } from 'src/app/servies/student.service';
import { StatusService } from 'src/app/servies/status.service';

@Component({
  selector: 'app-student-dialog',
  templateUrl: './student-dialog.component.html',
  styleUrls: ['./student-dialog.component.css']
})
export class StudentDialogComponent implements OnInit {

  public flag!: number;
  statusi!: Status[];
  subscription!: Subscription;

  constructor(private snackBar: MatSnackBar, public dialogRef: MatDialogRef<StudentDialogComponent>,
    @Inject (MAT_DIALOG_DATA) public data: Student, private studentService: StudentService, private statusService: StatusService) { }

  ngOnInit(): void 
  {
    this.statusService.getAllStatus().subscribe
    ((data) =>
    {
      this.statusi = data;
    }
    )
  }

  public compareTo(a: any, b: any) 
  {
    return a.id == b.id
  }

  public add(): void 
  {
    this.studentService.addStudent(this.data).subscribe
    (
      () => {
      this.snackBar.open('Uspešno dodat student: ' + this.data.ime, 'OK', {duration:2500})
      },
      (error:Error) => {
        this.snackBar.open('Došlo je do greške prilikom dodavanja novog studenta', 'Zatvori', {duration:2500})
      }
    );
  }

  public update(): void 
  {
    this.studentService.updateStudent(this.data).subscribe
    (
      () => {
        this.snackBar.open('Uspešno izmenjen student: ' + this.data.ime, 'OK', {duration:2500})
      },
      (error:Error) => {
        this.snackBar.open('Došlo je do greške prilikom izmene studenta!', 'Zatvori', {duration:2500})
      }
    );
  }

  public delete(): void 
  {
    this.studentService.deleteStudent(this.data.id).subscribe
    (
      () => {
        this.snackBar.open('Uspešno izbrisan student: ' + this.data.ime, 'OK', {duration:2500})
      },
      (error:Error) => {
        this.snackBar.open('Došlo je do greške prilikom brisanja studenta!', 'Zatvori', {duration:2500})
      }
    );
  }

  public cancel(): void
  {
    this.dialogRef.close();
    this.snackBar.open('Odustali ste od izmene.', 'Zatvori', {duration:1000});
  }

}
