import { Component, OnInit } from '@angular/core';
import { TENANTS } from '../mock-tenants';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  tenants = TENANTS

  constructor() { }

  ngOnInit(): void {
  }

}
