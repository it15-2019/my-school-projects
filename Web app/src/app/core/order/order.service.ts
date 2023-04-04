import { HttpClient } from "@angular/common/http";
import { AuthService } from "../auth/auth.service";
import { LogService } from "../log.service";
import { Order } from "./order";

export class OrderService {
    private apiUrl = 'http://localhost:4050/api/orders/';

    constructor(private httpClinet: HttpClient, private authService: AuthService, private logService: LogService) {}

    submitOrder(cartId: number, orderTotal: string, cartItems: string, shippingCost: any, itemsCount: any, estimatedTax: any, orderSubTotal: any)
    {
        // calculate shipping date
        var today = new Date();
        var after70Days = new Date();
        after70Days.setDate(today.getDate() + 7);

        // get logged in user
        const user = this.authService.loggedInUser;

        // create order object to save in db
    }
}