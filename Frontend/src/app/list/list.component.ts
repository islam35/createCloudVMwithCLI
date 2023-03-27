import { Component, OnInit } from '@angular/core';
import { getVMMap } from '../vm-select-properties/mapping';
import { HttpClient } from '@angular/common/http';
import { element } from 'protractor';
@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss']
})
export class ListComponent implements OnInit {
  azureVMs: getVMMap[] = []
  awsVMs: getVMMap[] = []
  userName: any = localStorage.getItem("username");
  collapse:boolean=false;
  collapse1:boolean=false;
  collapse2:boolean=false;
  collapse1Detail:boolean=false;
  collapse2Detail:boolean=false;
  collapseData:number=0;
  collapseData1:number=0;
  collapseData2:number=0;
  collapseData1Detail:number=0;
  collapseData2Detail:number=0;

  selectedVM: getVMMap ={
    vmName: '',
    vmInstanceId: '',
    skuOrInstanceName: '',
    region: '',
    os: '',
    creationTime: '',
    groupName: '',
    creationError: ''
  };

  selectedVMArr: getVMMap[]=[]

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    this.getUsersVMs();
  }

  getVMDetails(vmInstanceId: string, vendor: string){
    if(vendor.match("AWS")){
      this.awsVMs.forEach(item => {
        if(item.vmInstanceId.match(vmInstanceId)){
          this.selectedVMArr=[]
          this.selectedVMArr.push(item);
          this.selectedVM = item;
        }
      });

    }
    else{
      this.azureVMs.forEach(item => {
        if(item.vmInstanceId.match(vmInstanceId)){
          this.selectedVMArr=[]
          this.selectedVMArr.push(item);
          this.selectedVM = item;
        }
      });
    }


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
        
        collapseAction(){
          if (this.collapseData%2==0) {
            this.collapse=true;
            this.collapseData=this.collapseData+1;
            console.log(this.collapse,this.collapseData);
          }else{
            this.collapse=false;
            this.collapse1Detail=false;
            this.collapse2Detail=false;
            this.collapseData=this.collapseData+1;
            console.log(this.collapse,this.collapseData);
          }
        }
        collapseAction1(){
          if (this.collapseData1%2==0) {
            this.collapse1=true;
            this.collapseData1=this.collapseData1+1;
            console.log(this.collapse1,this.collapseData1);
          }else{
            this.collapse1=false;
            this.collapseData1=this.collapseData1+1;
            console.log(this.collapse1,this.collapseData1);
          }
        }
        collapseAction2(){
          if (this.collapseData2%2==0) {
            this.collapse2=true;
            this.collapseData2=this.collapseData2+1;
            console.log(this.collapse2,this.collapseData2);
          }else{
            this.collapse2=false;
            this.collapseData2=this.collapseData2+1;
            console.log(this.collapse2,this.collapseData2);
          }
        }collapseAction3(){
          if (this.collapseData1Detail%2==0) {
            this.collapse1Detail=true;
            this.collapseData1Detail=this.collapseData1Detail+1;
            
          }else{
            this.collapse1Detail=false;
            this.collapseData1Detail=this.collapseData1Detail+1;
            
          }
        }collapseAction4(){
          if (this.collapseData2Detail%2==0) {
            this.collapse2Detail=true;
            this.collapseData2Detail=this.collapseData2Detail+1;
            
          }else{
            this.collapse2Detail=false;
            this.collapseData2Detail=this.collapseData2Detail+1;
            
          }
        }
       
       
}
