import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms'
import {HttpClient} from "@angular/common/http";
import { Observable } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  hide: boolean = true;
  loginFailed: boolean  = false;
  constructor(private fb: FormBuilder, private http: HttpClient,private router: Router) {
  }

  ngOnInit() {
  }

  onChange(){
    this.loginFailed = false;
  }
  loginForm: FormGroup = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(4)]]
  })
  
  

  onLogin() {
    if (!this.loginForm.valid) {
      return;
    }
    let url = 'http://localhost:8080/api/login';
  
    this.http.post<Observable<boolean>>(url, {
      email: this.loginForm.controls['email'].value,
      password: this.loginForm.controls['password'].value
  }).subscribe(isValid => {
      if (isValid) {
        this.loginFailed = false;
        localStorage.setItem("username", this.loginForm.controls['email'].value)
        this.router.navigate(['/mainmenu']);
      } else {
        this.loginFailed = true;
      }
  });
    console.log(this.loginForm.value);
  }

}
