import { Component, Input, OnChanges, OnDestroy, OnInit, SimpleChanges, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Subscription } from 'rxjs';
import { Departman } from 'src/app/models/departman';
import { Status } from 'src/app/models/status';
import { Student } from 'src/app/models/student';
import { StudentService } from 'src/app/servies/student.service';
import { StudentDialogComponent } from '../dialogs/student-dialog/student-dialog.component';

@Component({
  selector: 'app-student',
  templateUrl: './student.component.html',
  styleUrls: ['./student.component.css']
})
export class StudentComponent implements OnInit, OnDestroy, OnChanges {

  displayedColumns = ['id', 'ime', 'prezime', 'brojIndeksa', 'status', 'departman', 'actions'];
  subscription!: Subscription;
  dataSource!: MatTableDataSource<Student>;
  @Input() selektovanDepartman!: Departman;

  @ViewChild(MatSort, {static:false}) sort!: MatSort;
  @ViewChild(MatPaginator, {static:false}) paginator!: MatPaginator;

  constructor(private studentService: StudentService, private dialog: MatDialog) { }

  ngOnChanges(changes: SimpleChanges): void 
  {
    if (this.selektovanDepartman != null)
    {
      this.loadData();
    }
  }

  ngOnDestroy(): void 
  {
    this.subscription.unsubscribe();
  }

  ngOnInit(): void 
  {
    this.loadData();
  }

  loadData()
  {
    this.subscription = this.studentService.getStudentZaDepartman(this.selektovanDepartman.id).subscribe
    (
      (data) =>
      {
        console.log(data);
        this.dataSource = new MatTableDataSource(data);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
      }, 
      (error: Error) =>
      {
        console.log(error.name + ' ' + error.message);
      }
    )
  }

  openDialog(flag: number, id?: number, ime?: string, prezime?: string, brojIndeksa?: string, status?: Status, departman?: Departman)
  {
    const dialogRef = this.dialog.open(StudentDialogComponent, {data: {id, ime, prezime, brojIndeksa, status, departman}});

    dialogRef.componentInstance.flag = flag;
    if (flag == 1) {
      dialogRef.componentInstance.data.departman = this.selektovanDepartman;
    }

    dialogRef.afterClosed().subscribe
    (
    (res) =>
    {  
      if (res === 1) 
      this.loadData()
    });
  }

  public applyFilter(filterValue: any)
  {
    filterValue = filterValue.target.value;
    filterValue = filterValue.trim();
    filterValue = filterValue.toLocaleLowerCase();

    this.dataSource.filter = filterValue;
  } 
}
