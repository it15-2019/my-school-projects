import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedRoutingModule } from './shared-routing.module';
import { RouterModule } from '@angular/router';
import { ProjekatMaterialModule } from './material-module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FlexLayoutModule } from "@angular/flex-layout";
import { CartItemCountComponent } from './cart/cart-item-count/cart-item-count.component';
import { AddToCartComponent } from './cart/add-to-cart/add-to-cart.component';


@NgModule({
  declarations: [
    CartItemCountComponent,
    AddToCartComponent
  ],
  imports: [
    CommonModule,
    SharedRoutingModule,
    RouterModule,
    ProjekatMaterialModule
  ],
  exports: [
    ProjekatMaterialModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
    FlexLayoutModule,
    CartItemCountComponent,
    AddToCartComponent
  ]
})
export class SharedModule { }
