import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms'
import {HttpClient} from "@angular/common/http";
import { Observable } from 'rxjs';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss']
})
export class SignUpComponent implements OnInit {

  hide: boolean = true;
  success: boolean = false;
  constructor(private fb: FormBuilder, private http: HttpClient) {
  }

  ngOnInit() {
  }

  signUpForm: FormGroup = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]]
  })

  onChange(){
    this.success = false;
  }

  signUp() {
    if (!this.signUpForm.valid) {
      return;
    }
    let url = 'http://localhost:8080/api/signup';
    this.http.post<Observable<boolean>>(url, {
      email: this.signUpForm.controls['email'].value,
      password: this.signUpForm.controls['password'].value
  }).subscribe(isValid => {
      if (isValid) {
        this.success = true;    
      } else {
        alert("Signup Failed. Please change your information and retry Signup")
      }
  });
    console.log(this.signUpForm.value);
  }

}
