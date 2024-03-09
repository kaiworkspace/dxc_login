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
  errorMessage = ""

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
    console.log("Handling")
    if(res.statusCode == 200){
      // redirect to landing page and check for roles
    }
  }

  handleError(error: any){
    this.errorMessage = error.error.message
    console.log(error.error.message)
  }

  login(){
    this.formCheck = true
    if(this.loginForm.valid){
      let username = this.loginForm.value.username
      let password = this.loginForm.value.password
      this.loginService.login(username, password).subscribe(
        (res) =>{
          this.handleResponse(res)
        },
        (error) =>{
          this.handleError(error)
        }
      )
    }
  }
}
