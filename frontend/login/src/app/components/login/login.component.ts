import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { LoginService } from './login.service';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private loginService: LoginService) { }

  ngOnInit(): void {
  }

  usernameCheck = false
  passwordCheck = false
  formCheck = false

  loginForm = new FormGroup({
    username: new FormControl("", 
    [
      Validators.required, 
      this.noSymbolsValidator,
    ]),
    password: new FormControl("", 
    [
      Validators.required,
      this.noSymbolsValidator
    ])
  })

  noSymbolsValidator(control: AbstractControl): {[key: string]: boolean} | null {
    const hasSymbols = /[!@#$%^&*(),.?":{}|<>]/.test(control.value)
    return hasSymbols?{'symbolsNotAllowed': true}: null
  }

  uncheckUsername(){
    this.usernameCheck = false
  }

  checkUsername(){
    this.usernameCheck = true
  }

  uncheckPassword(){
    this.passwordCheck = false
  }

  checkPassword(){
    this.passwordCheck = true
  }

  handleResponse(res: any){
    if(res.statusCode == 200){
      
    }
  }

  login(){
    this.formCheck = true
    if(this.loginForm.valid){
      let username = this.loginForm.value.username
      let password = this.loginForm.value.password
      this.loginService.login(username, password).subscribe(res =>{
        console.log(res)
        this.handleResponse(res)
        // let data = JSON.parse(res)
        // if(data.statusCode == 200){
        //   console.log("sign in")
        // }
      })
      
    }
  }
}
