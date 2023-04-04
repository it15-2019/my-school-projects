import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Subscription } from 'rxjs';
import { Departman } from 'src/app/models/departman';
import { Fakultet } from 'src/app/models/fakultet';
import { DepartmanService } from 'src/app/servies/departman.service';
import { FakultetService } from 'src/app/servies/fakultet.service';

@Component({
  selector: 'app-departman-dialog',
  templateUrl: './departman-dialog.component.html',
  styleUrls: ['./departman-dialog.component.css']
})
export class DepartmanDialogComponent implements OnInit {

  public flag!: number;
  subscription!: Subscription;
  fakulteti!: Fakultet[];

  constructor(public snackBar: MatSnackBar, public dialogRef: MatDialogRef<DepartmanDialogComponent>, 
              @Inject (MAT_DIALOG_DATA) public data: Departman, public departmanService: DepartmanService, public fakultetService: FakultetService) { }

  ngOnInit(): void {
    this.subscription = this.fakultetService.getAllFakultet().subscribe
    (
      (data) => {
        this.fakulteti = data;
      }
    );
  }

  compareTo(a: any, b: any) 
  {
    return a.id == b.id;
  }

  public add(): void 
  {
    this.departmanService.addDepartman(this.data).subscribe
    (
      () => {
      this.snackBar.open('Uspešno dodat departman: ' + this.data.naziv, 'OK', {duration:2500})
      },
      (error:Error) => {
        this.snackBar.open('Došlo je do greške prilikom dodavanja novog departmana!', 'Zatvori', {duration:2500})
      }
    );
  }

  public update(): void 
  {
    this.departmanService.updateDepartman(this.data).subscribe
    (
      () => {
        this.snackBar.open('Uspešno izmenjen departman: ' + this.data.naziv, 'OK', {duration:2500})
      },
      (error:Error) => {
        this.snackBar.open('Došlo je do greške prilikom izmene departmana!', 'Zatvori', {duration:2500})
      }
    );
  }

  public delete(): void 
  {
    this.departmanService.deleteDepartman(this.data.id).subscribe
    (
      () => {
        this.snackBar.open('Uspešno izbrisan departman: ' + this.data.naziv, 'OK', {duration:2500})
      },
      (error:Error) => {
        this.snackBar.open('Došlo je do greške prilikom brisanja departmana!', 'Zatvori', {duration:2500})
      }
    );
  }

  public cancel(): void
  {
    this.dialogRef.close();
    this.snackBar.open('Odustali ste od izmene.', 'Zatvori', {duration:1000});
  }

}
