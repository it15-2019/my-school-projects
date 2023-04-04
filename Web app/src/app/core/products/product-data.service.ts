import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Product } from './product';

@Injectable({
  providedIn: 'root'
})
export class ProductDataService {
  constructor(private $http: HttpClient) { }

  getAllProducts() : Observable<Product>
  {
    return this.$http.get('products.json') as Observable<Product>;
  }
}
