import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Subscription } from 'rxjs';
import { Status } from 'src/app/models/status';
import { StatusService } from 'src/app/servies/status.service';
import { StatusDialogComponent } from '../dialogs/status-dialog/status-dialog.component';

@Component({
  selector: 'app-status',
  templateUrl: './status.component.html',
  styleUrls: ['./status.component.css']
})
export class StatusComponent implements OnInit, OnDestroy {

  displayedColumns = ['id', 'naziv', 'oznaka', 'actions'];
  dataSource!: MatTableDataSource<Status>;
  subscription!: Subscription;

  @ViewChild(MatSort, {static:false}) sort!: MatSort;
  @ViewChild(MatPaginator, {static:false}) paginator!: MatPaginator;

  constructor(private statusService: StatusService, public dialog: MatDialog) {}

  ngOnInit(): void 
  {
    this.loadData();
  }

  ngOnDestroy(): void 
  {
    this.subscription.unsubscribe();
  }

  public loadData() 
  {
    this.subscription = this.statusService.getAllStatus().subscribe
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

  public openDialog(flag: number, id?: number, naziv?: string, oznaka?: string): void 
  {
    const dialogRef = this.dialog.open(StatusDialogComponent, {data: {id, naziv, oznaka}});

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
