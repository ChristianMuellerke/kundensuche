import { Component, OnInit } from '@angular/core';
import { LocalStorageService } from 'src/app/service/localstorage.service';
import { UsersService } from 'src/app/service/users.service';
import { User } from 'src/app/user';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  private usersService: UsersService;

//  private users: Array<User> = [];

  //p: number = 1;
  //collection: any[] = this.users; 

  users: User[] = [];
  currentUser: User = new User();
  currentIndex = -1;
  title = '';

  page = 1;
  count = 0;
  pageSize = 3;
  pageSizes = [3, 6, 9];

  constructor(private userService: UsersService) {
    this.usersService = userService;
  }

  ngOnInit(): void {
    // this.userService.findAll().subscribe({
    //   next: (users) => this.users.concat(users),
    //   error: (e) => console.error("failed retrieving users: " + e),
    //   complete: () => console.info("users retrieved")
    // });

    this.retrieveUsers();
  }

  retrieveUsers(): void {
    const params = this.getRequestParams(this.page, this.pageSize);

    this.userService.findAllPaged(params)
    
    .subscribe( {
      next: (response:any) => {
        const { users, totalItems } = response;
        this.users = users;
        this.count = totalItems;
        console.log(response);
      },
      error: (error) => console.error("retrieving users failed: " + error)
    });

  }  

  handlePageChange(event: number): void {
    this.page = event;
    this.retrieveUsers();
  }

  handlePageSizeChange(event: any): void {
    this.pageSize = event.target.value;
    this.page = 1;
    this.retrieveUsers();
  }

  getRequestParams(page: number, pageSize: number): any {
    let params: any = {};

    if (page) {
      params[`page`] = page - 1;
    }

    if (pageSize) {
      params[`size`] = pageSize;
    }

    return params;
  }  

}
