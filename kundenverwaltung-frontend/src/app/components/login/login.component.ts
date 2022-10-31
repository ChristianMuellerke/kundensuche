import { Component, Input, OnInit } from '@angular/core';
import { TENANTS } from '../../mock-tenants';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { switchMap } from 'rxjs/operators';
import { Tenant } from 'src/app/tenant';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  tenants = TENANTS;
  @Input() selectedTenant?: Tenant;
  
  constructor(
    private route: ActivatedRoute,
    private router: Router
  ) {}

    /*
      Hier weitermachen
      Ich wollte dass man auf der Login-Seite zu allererst aus der DropDown Liste den Tenant wählt. 
      Wenn der gewählt wird auf /login/TENANT_ID gewechselt und man kann dann Username/ Passwort eingeben.

      Hier war ich stehengeblieben:
      * https://angular.io/guide/router-tutorial-toh
      * https://angular.io/tutorial/toh-pt3
    */

  ngOnInit() {
    this.selectedTenant$ = this.route.paramMap.pipe(
      switchMap((params: ParamMap) =>
        params.get('id')!))
    );
  }
}
