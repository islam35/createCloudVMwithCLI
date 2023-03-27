import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { getVMMap } from '../vm-select-properties/mapping';

@Component({
  selector: 'app-main-menu',
  templateUrl: './main-menu.component.html',
  styleUrls: ['./main-menu.component.scss']
})
export class MainMenuComponent implements OnInit {

  userName: any = localStorage.getItem("username");
  azureVMs: getVMMap[] = [];
  awsVMs: getVMMap[] = [];
  
  constructor(private http: HttpClient) { }
 
   ngOnInit(): void {
    this.getUsersVMs();
   } 

  getUsersVMs(){
    let url = 'http://localhost:8080/api/getvms';
    this.http.get<Map<string, getVMMap[]>>(url,  {
      params:{userName: this.userName
      },
      observe: 'response' 
    }).subscribe(resp => {
      if(resp.body != null){
        let res = Object.entries(resp.body);
        
        res.find(([key, value]) => {
          if (key === "AWS") {
            this.awsVMs = [];
            console.log(value);
            value.forEach((element : any) =>{
              this.awsVMs.push(JSON.parse(element))});
          }
          if (key === "Azure") {
            this.azureVMs = [];
            console.log(value);
            value.forEach((element : any) =>{
              this.azureVMs.push(JSON.parse(element))}); 
          }
        });}});
        }
}
