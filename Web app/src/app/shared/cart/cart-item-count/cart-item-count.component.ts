import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { CartStore } from 'src/app/core/cart/cart-store';
import { getCartItemCount } from 'src/app/core/cart/cart-selectors';

@Component({
  selector: 'app-cart-item-count',
  templateUrl: './cart-item-count.component.html',
  styleUrls: ['./cart-item-count.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CartItemCountComponent implements OnInit {

  totalItemsInCart$!: Observable<number>;

  constructor(private cartStore: CartStore) { }

  ngOnInit()
  {  
    this.totalItemsInCart$ = this.cartStore.select(getCartItemCount);
  }
 
}
