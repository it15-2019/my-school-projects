import { Injectable } from "@angular/core";
import { Store } from "../store";
import { CartItem } from "./cart-item";
import { CartState, initialState } from "./cart-state";


@Injectable ({
    providedIn: 'root'
})

export class CartStore extends Store<CartState>
{
    constructor()
    {
        super(initialState);
    }

    addCartItem(cartItemToAdd: CartItem) 
    {
        console.log('[Cart] Add Cart Item');
        const newState = 
        {
            ...this.state , //cartItems
            cartItems: Array().concat(this.state.cartItems, cartItemToAdd)
        };

        this.setState(newState); 
    }

    clearCart()
    {
        console.log('[Cart] Clear Cart');
        const newState = initialState;
        this.setState(newState); 
    }

    restoreCart(stateToRestore: CartState)
    {
        console.log('[Cart] Restore Cart');

        this.setState(stateToRestore);
    }

    removeCartItem(item: CartItem)
    {
         console.log('[Cart] Remove Cart Item');
         const newState = 
         {
             ...this.state,
             cartItems: this.state.cartItems.filter(cartItem => cartItem.id != item.id)
         };

         this.setState(newState);
    }    

    updateCartItem(item: CartItem)
    {
        console.log('[Cart] Update Cart Item');
         const newState = 
         {
             ...this.state,
             cartItems: this.state.cartItems.map(cartItem => cartItem.id == item.id 
                ?  item : cartItem)
         };

         this.setState(newState);
    }
}
