import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

export class UserInfo{
  constructor(
    public email:string,
    public password:string,
  ) {}
}

@Injectable({
  providedIn: 'root'
})
export class HttpClientService {

  constructor(
    private httpClient:HttpClient
  ) { 
     }

     getCustomers()
  {
    console.log("test call");
    return this.httpClient.get<UserInfo[]>('http://localhost:8080/UserInfo');
  }
  

//public deleteCustomers(customer: UserInfo) {
  //return this.httpClient.delete<UserInfo>("http://localhost:8080" + "/"+ customer.firstName);
//}

public signUp(userInfo: UserInfo) {
  return this.httpClient.post<UserInfo>("http://localhost:8080/addCustomers", userInfo);
}
}