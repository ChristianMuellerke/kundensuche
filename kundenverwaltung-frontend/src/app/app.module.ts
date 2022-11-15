import { HttpClient, HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { TenantService } from './service/tenant.service';
import { ReactiveFormsModule } from '@angular/forms';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { AuthService } from './service/auth.service';
import { LocalStorageService } from './service/localstorage.service';
import { JWTTokenService } from './service/jwttoken.service';
import { AuthorizeGuard } from './authorizeguard';
import { httpInterceptorProviders, JwtInterceptor } from './jwtinterceptor';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    DashboardComponent,
    HeaderComponent,
    FooterComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule
  ],
  providers: [TenantService, AuthService, AuthorizeGuard, httpInterceptorProviders, LocalStorageService, JWTTokenService],
  bootstrap: [AppComponent]
})
export class AppModule { }
