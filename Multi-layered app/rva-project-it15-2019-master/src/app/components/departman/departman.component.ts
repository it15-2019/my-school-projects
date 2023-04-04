import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Subscription } from 'rxjs';
import { Departman } from 'src/app/models/departman';
import { Fakultet } from 'src/app/models/fakultet';
import { DepartmanService } from 'src/app/servies/departman.service';
import { DepartmanDialogComponent } from '../dialogs/departman-dialog/departman-dialog.component';

@Component({
  selector: 'app-departman',
  templateUrl: './departman.component.html',
  styleUrls: ['./departman.component.css']
})
export class DepartmanComponent implements OnInit, OnDestroy {

  displayedColumns = ['id', 'naziv', 'oznaka', 'fakultet', 'actions'];
  dataSource!: MatTableDataSource<Departman>;
  subscription!: Subscription;
  selektovanDepartman!: Departman;

  @ViewChild(MatSort, {static:false}) sort!: MatSort;
  @ViewChild(MatPaginator, {static:false}) paginator!: MatPaginator;

  constructor(private departmanService: DepartmanService, public dialog: MatDialog) {}

  ngOnInit(): void 
  {
    this.loadData();
  }

  ngOnDestroy(): void 
  {
    this.subscription.unsubscribe();
  }

  selectRow(row: Departman) {
    this.selektovanDepartman = row;
  }

  public loadData() 
  {
    this.subscription = this.departmanService.getAllDepartman().subscribe
    (
      (data) => {
      this.dataSource = new MatTableDataSource(data);
      this.dataSource.sort = this.sort;
      this.dataSource.paginator = this.paginator;
      },
      (error: Error) => {
        console.log(error.name + ' ' + error.message);
      }
    );
  }

  public openDialog(flag: number, id?: number, naziv?: string, oznaka?: string, fakultet?: Fakultet): void 
  {
    const dialogRef = this.dialog.open(DepartmanDialogComponent, {data: {id, naziv, oznaka, fakultet}});

    dialogRef.componentInstance.flag = flag;
    dialogRef.afterClosed().subscribe
    (
      (result) => {
        if(result === 1) {
          this.loadData();
        }
      }
    )
  }

  public applyFilter(filterValue: any)
  {
    filterValue = filterValue.target.value;
    filterValue = filterValue.trim();
    filterValue = filterValue.toLocaleLowerCase();

    this.dataSource.filter = filterValue;
  } 
}
