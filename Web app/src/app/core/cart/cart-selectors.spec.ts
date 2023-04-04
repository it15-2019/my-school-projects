import { CartItem } from "./cart-item";
import { getCartItemCount, getIsItemAlreadyInCart } from "./cart-selectors";
import { CartState } from "./cart-state";

const given = beforeEach;
const when = beforeEach;
const then = it;

describe('Cart Store Selectors', () => 
{
    describe('Get Cart Item Count', () =>
    {
        let cartState: CartState;
        let result: number;

        given(() => 
        {
            const tenApples : CartItem =
            {
                id: 1,
                quantity: 10,
                imgUrl: 'img/apple',
                itemTotal: 20,
                name: 'apple', 
                price: 2
            };
            const threeOranges : CartItem =
            {
                id: 2,
                quantity: 3,
                imgUrl: 'img/orange',
                itemTotal: 6,
                name: 'apple',
                price: 2
            };
            cartState = 
            {
                cartItems: [tenApples, threeOranges]
            };
        });
        when(() => 
        {
            result = getCartItemCount(cartState);
        });
        then("I can see my total cart items count", () => 
        {
            expect(result).toBe(13);
        });      
    });

    it('can find cart item', () => 
    {
        const itemInCart: CartItem = 
        {
            id: 1,
            quantity: 10,
            imgUrl: 'img/apple',
            itemTotal: 20,
            name: 'apple', 
            price: 2
        };
        const itemInCart2: CartItem =
        {
            id: 2,
            quantity: 5,
            imgUrl: 'img/orange',
            itemTotal: 10,
            name: 'apple',
            price: 2
        };
        const state: CartState = {
            cartItems: [itemInCart, itemInCart2]
        };
        const itemExist = getIsItemAlreadyInCart(2)(state);
        expect(itemExist).toBeTruthy();

        const itemExist2 = getIsItemAlreadyInCart(4)(state);
        expect(itemExist2).toBeFalsy();
    });
});