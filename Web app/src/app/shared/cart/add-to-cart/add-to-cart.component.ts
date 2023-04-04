import { Component, Input, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { getIsItemAlreadyInCart } from 'src/app/core/cart/cart-selectors';
import { CartStore } from 'src/app/core/cart/cart-store';
import { ALLOWED_PRODUCT_QUANTTIES, CartService } from 'src/app/core/cart/cart.service';
import { Product } from 'src/app/core/products/product';


@Component({
  selector: 'app-add-to-cart',
  templateUrl: './add-to-cart.component.html',
  styleUrls: ['./add-to-cart.component.scss']
})
export class AddToCartComponent implements OnInit {

  @Input() 
  product!: Product;
  availableQuantities!: number[];
  quantity!: number;
  isItemAlreadyInCart!: Observable<boolean>;

  constructor(private cartStore: CartStore, private cartService: CartService) { }

  ngOnInit() 
  { 
    this.availableQuantities = ALLOWED_PRODUCT_QUANTTIES;
    this.isItemAlreadyInCart = this.cartStore.select(getIsItemAlreadyInCart(this.product.id));
    this.quantity = 1;
  }

  addItemToCart()
  {
    this.cartService.addToCart(this.product, this.quantity).subscribe(cartItem => 
    console.log('added to cart', cartItem));
  }
}
