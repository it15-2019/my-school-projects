import { CartState } from "./cart-state";
import { createSelector } from "reselect";

export const getCartItems = (state: CartState) => state.cartItems;

export const getCartItemCount = (state: CartState) =>
{
    const cartItems = state.cartItems;
    const totalCartCount = cartItems.reduce(
        (totalCount, currentItem) => totalCount + currentItem.quantity, 0
    );
    
    return totalCartCount;
}

export const getIsItemAlreadyInCart = (productId: number) => createSelector(
    getCartItems, (items) => items.filter(item => item.id == productId).length > 0
);

export const getCartSubTotal = createSelector(getCartItems, (items) => 
    items.reduce((subTotal, currentItem) => subTotal + currentItem.itemTotal, 0)
);

const SHIPPING = 0.01;
export const getShippingCart = createSelector(
    getCartSubTotal,
    (subTotal) => subTotal * SHIPPING
);

const TAX = 0.2;
export const getEstimatedTax = createSelector(
    getCartSubTotal,
    (subtTotal) => subtTotal * TAX
)

export const getOrderTotal = createSelector(
    getCartSubTotal,
    getShippingCart,
    getEstimatedTax,
    (carSubTotal, shippingCost, estimatedTax) => carSubTotal + shippingCost + estimatedTax
);