import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { ReactiveFormsModule } from '@angular/forms';
import { LoginComponent } from './login/login.component'
import { SignUpComponent } from './signup/signup.component';
import { MatIconModule } from '@angular/material/icon';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { MainMenuComponent } from './main-menu/main-menu.component';
import { LoginToCloudAccountsComponent } from './login-to-cloud-accounts/login-to-cloud-accounts.component';
import { VmSelectPropertiesComponent } from './vm-select-properties/vm-select-properties.component';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';
import { SettingComponent } from './setting/setting.component';
import { VmdetailsComponent } from './vmdetails/vmdetails.component';
import { ListComponent } from './list/list.component';
import { FormsModule } from '@angular/forms';



@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    SignUpComponent,
    MainMenuComponent,
    LoginToCloudAccountsComponent,
    VmSelectPropertiesComponent,
    ForgotPasswordComponent,
    SettingComponent,
    VmdetailsComponent,
    ListComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatCardModule,
    MatInputModule,
    ReactiveFormsModule,
    MatIconModule,
    HttpClientModule,
    FormsModule,
    RouterModule.forRoot([
      {path: 'login', component: LoginComponent},
      {path: 'signup', component: SignUpComponent},
      {path: 'forgotpassword', component: ForgotPasswordComponent},
      {path: 'logintocloud', component: LoginToCloudAccountsComponent},
      {path: 'mainmenu', component: MainMenuComponent},
      {path: 'vmcustomise', component: VmSelectPropertiesComponent},
      {path: 'settings', component: SettingComponent},
      {path: 'vmdetails', component: VmdetailsComponent},
    ]),
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
