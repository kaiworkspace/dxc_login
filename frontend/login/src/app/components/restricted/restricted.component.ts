import { Component, OnInit } from '@angular/core';
import { RestrictedService } from './restricted.service';

@Component({
  selector: 'app-restricted',
  templateUrl: './restricted.component.html',
  styleUrls: ['./restricted.component.css']
})
export class RestrictedComponent implements OnInit {

  constructor(private restrictedService: RestrictedService) { }

  ngOnInit(): void {
    this.isLoading = true
    this.accessRestrictedContent()
    this.isLoading = false
  }

  isLoading = false
  permissionGranted = false
  restrictedContent = ""

  accessRestrictedContent(){
    this.restrictedService.fetchData().subscribe(
      (res)=>{
        let data = JSON.parse(res)
        if(data.statusCode == 200){
          this.permissionGranted = true
          this.restrictedContent = data.message
        }
      },
      (error)=>{
        console.log(error)
      }
    )
  }
}
