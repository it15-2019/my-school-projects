import { Injectable } from '@angular/core';
import { of } from 'rxjs';
import { LogService } from '../log.service';
import { Product } from '../products/product';
import { CartStore } from './cart-store';

export const ALLOWED_PRODUCT_QUANTTIES = Array.from({length: 5}, (v, i) => i + 1);

@Injectable({
  providedIn: 'root'
})
export class CartService {

  constructor(private cartStore: CartStore, private logService: LogService) 
  {}

  addToCart(product: Product, quantity: number)
  {
    this.logService.log('[Cart] Add Item');
    const cartItemToAdd = 
    {
      ...product,
      quantity,
      itemTotal: product.price * quantity
    };
    this.cartStore.addCartItem(cartItemToAdd);  
    return of(cartItemToAdd);
  }
}
