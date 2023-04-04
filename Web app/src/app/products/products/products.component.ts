import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { CartService } from 'src/app/core/cart/cart.service';
import { Product } from 'src/app/core/products/product';
import { ProductDataService } from '../../core/products/product-data.service';


@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss']
})

export class ProductsComponent {


  displayedColumns = ['imgUrl', 'name', 'price', 'action'];

  dataSource: MatTableDataSource<Product>;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  
  constructor(private productDataService: ProductDataService, private cartService: CartService) 
  { 
    const products: Product[] = [
      {
        imgUrl: "https://glassesshop-res.cloudinary.com/c_fill,f_auto,fl_lossy,q_auto,w_800,h_400,c_pad/products/202109/614a9acf803df.jpg",
        name: "NERD",
        price: 24.99,
        id: 1
      },
      {
        imgUrl: "https://glassesshop-res.cloudinary.com/c_fill,f_auto,fl_lossy,q_auto,w_800,h_400,c_pad/products/202108/6107bdcd1997d.jpg",
        name: "B&W",
        price: 16.99,
        id: 2
      },
      {
        imgUrl: "https://glassesshop-res.cloudinary.com/c_fill,f_auto,fl_lossy,q_auto,w_800,h_400,c_pad/products/202109/614a9aa76f075.jpg",
        name: "BACK TO 70S",
        price: 19.99,
        id: 3
      },
      {
        imgUrl: "https://glassesshop-res.cloudinary.com/c_fill,f_auto,fl_lossy,q_auto,w_800,h_400,c_pad/products/201906/5d03189634ede.jpg",
        name: "MATH TEACHER",
        price: 22.99,
        id: 4
      },
      {
        imgUrl: "https://glassesshop-res.cloudinary.com/c_fill,f_auto,fl_lossy,q_auto,w_800,h_400,c_pad/products/202102/603c4e48ab639.jpg",
        name: "KAREN",
        price: 7.99,
        id: 5
      }
    ];

    this.dataSource = new MatTableDataSource(products);
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  addItemToCart(products: Product)
  {
    this.cartService.addToCart(products, 2);
  }

  /* ngOnInit() 
  {
    this.subscriptions.push(this.productDataService.getAllProducts().subscribe(products => this.onDataLoad(products)));
  }
  
  ngOnDestroy()
  {
    this.subscriptions.forEach(s => s.unsubcribe());
  }

  onDataLoad(products: | Product[])
  {
    this.loading = false;
    this.dataSource.data = products;
  } */
}
