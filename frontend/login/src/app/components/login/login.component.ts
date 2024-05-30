import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { LoginService } from './login.service';
import { HttpResponse } from '@angular/common/http';
import { Router, NavigationExtras } from '@angular/router'

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private loginService: LoginService, private router: Router) { 
    const navigation = this.router.getCurrentNavigation()
    const state = navigation?.extras.state as{
      status: string
    }
    // save state

    if(state){
      // console.log(state.status)
      this.errorMessage = state.status
    }
  }

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
      this.escapeCharValidator,
    ]),
    password: new FormControl("", 
    [
      Validators.required,
      this.escapeCharValidator
    ])
  })

  noSymbolsValidator(control: AbstractControl): {[key: string]: boolean} | null {
    const hasSymbols = /[!@#$%^&*(),.?":{}|<>]/.test(control.value)
    return hasSymbols?{'symbolsNotAllowed': true}: null
  }

  escapeCharValidator(control: AbstractControl):{[key: string]: boolean} | null {
    
    const value: string = control.value

    // Define the escape characters you want to check for
    const escapeChars: string[] = ['\\', '\'', '\"', '\n', '\r', '\t']

    // Check if the value contains any escape characters
    const containsEscapeChars = escapeChars.some(char => value.includes(char))

    // Return an error object if escape characters are found
    return containsEscapeChars?{'containsEscapeChars': true } : null
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
    // console.log("Success")
    const navigationExtras: NavigationExtras = {
      state: {
        accessToken: res.accessToken,
        tokenType: res.tokenType,
      }
    }
    this.router.navigate(["/dashboard"], navigationExtras)    
  }

  handleError(error: any){
    if(error.status == 401){
      this.errorMessage = "Invalid username or password"
    }
  }

  login(){
    this.formCheck = true
    if(this.loginForm.valid){
      let username = this.loginForm.value.username?.trim()
      let password = this.loginForm.value.password?.trim()
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
