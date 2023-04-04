import { CartItem } from "../cart/cart-item";

export class Order {
    orderdId = "";
    
    createdAt = Date.now;

    constructor(
        public userId: string,
        public orderTotal: string,
        public deliveryDate: Date,
        public itemList: CartItem[],
        public cartId: string,
        public itemsCount: number,
        public shippingCost: number,
        public estimatedTax: number,
        public orderSubTotal: number
    ) {}
}