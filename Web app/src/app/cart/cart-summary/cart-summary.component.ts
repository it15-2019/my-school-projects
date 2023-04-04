import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { getCartItemCount, getCartSubTotal, getEstimatedTax, getOrderTotal, getShippingCart } from 'src/app/core/cart/cart-selectors';
import { CartStore } from 'src/app/core/cart/cart-store';

@Component({
  selector: 'app-cart-summary',
  templateUrl: './cart-summary.component.html',
  styleUrls: ['./cart-summary.component.scss']
})
export class CartSummaryComponent implements OnInit {

  cartSubTotal!: Observable<number>;
  cartItemsCount!: Observable<number>;
  shippingCost!: Observable<number>;
  estimatedTax!: Observable<number>;
  orderTotal!: Observable<number>;
  quantity!: number;

  constructor(private cartStore: CartStore, private router: Router) { }

  ngOnInit()
  {
    this.cartSubTotal = this.cartStore.select(getCartSubTotal);
    this.cartItemsCount = this.cartStore.select(getCartItemCount);
    this.shippingCost = this.cartStore.select(getShippingCart);
    this.estimatedTax = this.cartStore.select(getEstimatedTax);
    this.orderTotal = this.cartStore.select(getOrderTotal);
  }
  openDialog()
  {
    alert("Thanks for shopping with us, please continue shopping!");
    this.quantity = 0;
    this.router.navigate([''])
  }

}
