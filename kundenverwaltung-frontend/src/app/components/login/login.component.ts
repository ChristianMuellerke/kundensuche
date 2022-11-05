import { Component, Input, OnInit } from '@angular/core';
import { TENANTS } from '../../mock-tenants';
import { Router, ActivatedRoute, ParamMap, Params } from '@angular/router';
import { switchMap } from 'rxjs/operators';
import { Tenant } from 'src/app/tenant';
import { TenantService } from 'src/app/service/tenant.service';
import { convertFromMaybeForwardRefExpression } from '@angular/compiler/src/render3/util';
import { FormControl } from '@angular/forms';
import { runInThisContext } from 'vm';
import { EMPTY, Observable } from 'rxjs';
import { UserService } from 'src/app/service/user.service';
import { JwtData } from 'src/app/authorization';
import { timeStamp } from 'console';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  tenants: Tenant[] = [];
  selectedTenant: Tenant | undefined;
  tenantServiceResult$!: Observable<Tenant>;
  navigated = false; // true if navigated here
  

  name = new FormControl('');
  password = new FormControl('');

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private tenantService: TenantService,
    private userService: UserService
  ) {}

    /*
      Hier weitermachen
      Ich wollte dass man auf der Login-Seite zu allererst aus der DropDown Liste den Tenant wählt. 
      Wenn der gewählt wird auf /login/TENANT_ID gewechselt und man kann dann Username/ Passwort eingeben.

      Hier war ich stehengeblieben:
      * https://angular.io/guide/router-tutorial-toh
      * https://angular.io/tutorial/toh-pt3
      * 
      * https://github.com/johnpapa/angular-tour-of-heroes
      * 
      * https://angular.io/guide/forms-overview
      * 
      Damit das Beispiel funktioniert, muss ich doch schon mal einen Service für die Tenants realisieren?
    */

  ngOnInit() {
    
    if (!this.navigated){
      this.name.disable();
      this.password.disable();
    } else {
      this.name.enable();
      this.password.enable();
    }

    this.tenantService.findAll().subscribe(data => {
      this.tenants = data;
    }, err => {
      console.log("Tenants konnten nicht geladen werden");
    });

    this.tenantServiceResult$ = this.route.paramMap.pipe(
      switchMap((params: ParamMap) => {
        if (params.has('tenantId')) {
          const tenantId = params.get('tenantId');
          return this.tenantService.getTenant(Number(tenantId))
        }
        this.selectedTenant = new Tenant();
        this.selectedTenant.name = 'Tenant auswählen';
        this.navigated = false;
        this.name.disable();
        this.password.disable();

        return EMPTY
      }
    ));

    this.tenantServiceResult$.subscribe(tenant => {
      this.selectedTenant = tenant;
      this.navigated = true;
      this.name.enable();
      this.password.enable();        
      
      this.name.setValue('user1');
      this.password.setValue('11112222');
    });
  }
  
  login() {
    console.log('Logging in with Tenant=' + this.selectedTenant?.id + ' Username=' + this.name.value + ' Password=' + this.password.value );
    
    const tenantId : number = Number(this.selectedTenant?.id);
    
    this.userService.login(this.name.value, this.password.value, tenantId).subscribe(jwtData => {
      console.log('Got Token: ' + jwtData.token);
    });

    // Jetzt müssten wir uns auch noch um das JWT Geraffel kümmern
    // https://jasonwatmore.com/post/2021/09/24/angular-http-interceptor-to-set-auth-header-for-api-requests-if-user-logged-in#:~:text=Angular%20JWT%20Interceptor&text=The%20JWT%20Interceptor%20intercepts%20HTTP,apiUrl%20).

    hier weitermachen

    this.router.navigate(['dashboard']);
  }
}


