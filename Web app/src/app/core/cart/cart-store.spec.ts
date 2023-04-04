import { TestBed } from '@angular/core/testing';
import { CartItem } from './cart-item';
import { CartState, initialState } from './cart-state';
import { CartStore } from './cart-store';

describe('CartStore', () => 
{
  let cartStore : CartStore;

  beforeEach(() =>  
  {
    TestBed.configureTestingModule({
      providers: [CartStore]
    });

    cartStore = TestBed.get(CartStore);
  });

  // Testovi

 /*  it('should create an instance', () => 
  {
    expect(cartStore).toBeTruthy();
  });


 it('can add item into cart state', () =>
  {
    // Arrange
    const currentState = initialState;
    expect(currentState.cartItems.length).toBe(0);
    const cartItem: CartItem =
    {
      id: 1,
      imgUrl: 'img/apple',
      name: 'apple',
      price: 2,
      quantity: 10,
      itemTotal: 20
    };

    // Act
    cartStore.addCartItem(cartItem);
    const expectedState = 
    {
      cartItems: [cartItem]
    };

    // Assert
    expect(cartStore.state).toEqual(expectedState);
  });

  it('can clear cart', () =>
  {
    // Arrange
    const cartItem: CartItem =
    {
      id: 1,
      imgUrl: 'img/apple',
      name: 'apple',
      price: 2,
      quantity: 10,
      itemTotal: 20
    };
    cartStore.addCartItem(cartItem);
    const currentState = 
    {
      cartItems: [cartItem]
    };
    expect(cartStore.state).toEqual(currentState);

    // Act
    cartStore.clearCart();

    // Assert
    expect(cartStore.state).toEqual(initialState);
  });

  it('can restore cart', () =>
  {
    // Arrange
    const currentState = initialState;
    expect(cartStore.state).toEqual(currentState);
    const cartItem: CartItem =
    {
      id: 1,
      imgUrl: 'img/apple',
      name: 'apple',
      price: 2,
      quantity: 10,
      itemTotal: 20
    };
    const expectedState: CartState =
    {
      cartItems: [cartItem]
    }; 

    // Act
    cartStore.restoreCart(expectedState);

    // Assert
    expect(cartStore.state).toEqual(expectedState);
  });

  it('can remove cart item', () =>
  {
    // Arrange
    const cartItem1: CartItem =
    {
      id: 1,
      imgUrl: 'img/apple',
      name: 'apple',
      price: 2,
      quantity: 10,
      itemTotal: 20
    };
    const cartItem2: CartItem =
    {
      id: 2,
      imgUrl: 'img/orange',
      name: 'orange',
      price: 3,
      quantity: 5,
      itemTotal: 15
    };
    const currentState: CartState = 
    {
      cartItems: [cartItem1, cartItem2]
    };
    cartStore.restoreCart(currentState);
    expect(cartStore.state).toEqual(currentState);

    // Act
    cartStore.removeCartItem(cartItem1);
    const expectedState: CartState = {
      cartItems: [cartItem2]
    };

    // Assert
    expect(cartStore.state).toEqual(expectedState);
  }); 

  it('can update cart item', () =>
  {
    // Arrange
    const cartItem1: CartItem =
    {
      id: 1,
      imgUrl: 'img/apple',
      name: 'apple',
      price: 2,
      quantity: 10,
      itemTotal: 20
    };
    const cartItem2: CartItem =
    {
      id: 2,
      imgUrl: 'img/orange',
      name: 'orange',
      price: 3,
      quantity: 5,
      itemTotal: 15
    };
    const currentState: CartState = 
    {
      cartItems: [cartItem1, cartItem2]
    };
    cartStore.restoreCart(currentState);
    expect(cartStore.state).toEqual(currentState);

    // Act
    const updatedCartItem2: CartItem = 
    {
      id: 2,
      imgUrl: 'img/orange',
      name: 'orange',
      price: 3,
      quantity: 20,
      itemTotal: 60
    }
    cartStore.updateCartItem(updatedCartItem2);
    const expectedState: CartState = {
      cartItems: [cartItem1, updatedCartItem2]
    };
    
    // Assert
    expect(cartStore.state).toEqual(expectedState);
  }); */
});
