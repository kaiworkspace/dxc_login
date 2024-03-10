import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router'
import { DashboardService } from './dashboard.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  constructor(private router: Router, private dashBoardService: DashboardService) { 
    // const navigation = this.router.getCurrentNavigation()
    // const state = navigation?.extras.state as{
    //   accessToken: string,
    //   tokenType: string
    // }
    // // save state

    // if(state){
    //   this.accessToken = state.accessToken
    //   this.tokenType = state.tokenType
    // }
  }

  ngOnInit(): void {
    console.log("Calling service")
    this.isLoading = true
    this.dashBoardService.loadUserInformation().subscribe(
      (res)=>{
        let data = JSON.parse(res)
        if(data.statusCode == 200){
          this.username = data.userDto.username
          this.name = data.userDto.name
          this.roles = data.userDto.roles
          for(let i=0; i<this.roles.length; i++){
            if(this.roles[i]['name'] == "manager"){
              this.isManager = true
            }
          }
          this.isLoading = false
        }
      },
      (error)=>{
        console.log(error)
        // redirect back to login
        this.router.navigate(["/login"])
      }
  )}
  isLoading = false
  username = ""
  name = ""
  roles = []
  isManager = false

  logout(){
    this.dashBoardService.logout().subscribe(res=>{
      console.log(res)
      this.router.navigate(["/login"])
  })
  }
}
