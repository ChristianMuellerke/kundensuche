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

  private users: Array<User> = [];

  constructor(private userService: UsersService) {
    this.usersService = userService;
  }

  // FEHLER IST ABSICHT: HIER JETZT EINE LISTE VON IRGENDWAS DARSTELLEN

  ngOnInit(): void {
    this.userService.findAll().subscribe({
      next: (users) => this.users.concat(users),
      error: (e) => console.error("failed retrieving users: " + e),
      complete: () => console.info("users retrieved")
    });
  }

}
