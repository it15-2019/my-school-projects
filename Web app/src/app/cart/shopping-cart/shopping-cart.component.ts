import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { getCartItemCount } from 'src/app/core/cart/cart-selectors';
import { CartStore } from 'src/app/core/cart/cart-store';

@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrls: ['./shopping-cart.component.scss']
})
export class ShoppingCartComponent implements OnInit {

  cartItemsCount!: Observable<number>;
  constructor(private cartStore: CartStore) { }

  ngOnInit()
  {
    this.cartItemsCount = this.cartStore.select(getCartItemCount);
  }

}
