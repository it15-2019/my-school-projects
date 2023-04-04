import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Subscription } from 'rxjs';
import { Fakultet } from 'src/app/models/fakultet';
import { FakultetService } from 'src/app/servies/fakultet.service';

@Component({
  selector: 'app-fakultet-dialog',
  templateUrl: './fakultet-dialog.component.html',
  styleUrls: ['./fakultet-dialog.component.css']
})
export class FakultetDialogComponent implements OnInit{

  public flag!: number;
  subscription!: Subscription;

  constructor(public snackBar: MatSnackBar, public dialogRef: MatDialogRef<FakultetDialogComponent>, 
              @Inject (MAT_DIALOG_DATA) public data: Fakultet, public fakultetService: FakultetService) { }

  ngOnInit(): void {}

  public add(): void 
  {
    this.fakultetService.addFakultet(this.data).subscribe
    (
      () => {
      this.snackBar.open('Uspešno dodat fakultet: ' + this.data.naziv, 'OK', {duration:2500})
      },
      (error:Error) => {
        this.snackBar.open('Došlo je do greške prilikom dodavanja novog fakulteta!', 'Zatvori', {duration:2500})
      }
    );
  }

  public update(): void 
  {
    this.fakultetService.updateFakultet(this.data).subscribe
    (
      () => {
        this.snackBar.open('Uspešno izmenjen fakultet: ' + this.data.naziv, 'OK', {duration:2500})
      },
      (error:Error) => {
        this.snackBar.open('Došlo je do greške prilikom izmene fakulteta!', 'Zatvori', {duration:2500})
      }
    );
  }

  public delete(): void 
  {
    this.fakultetService.deleteFakultet(this.data.id).subscribe
    (
      () => {
        this.snackBar.open('Uspešno izbrisan fakultet: ' + this.data.naziv, 'OK', {duration:2500})
      },
      (error:Error) => {
        this.snackBar.open('Došlo je do greške prilikom brisanja fakulteta!', 'Zatvori', {duration:2500})
      }
    );
  }

  public cancel(): void
  {
    this.dialogRef.close();
    this.snackBar.open('Odustali ste od izmene.', 'Zatvori', {duration:1000});
  }
}
