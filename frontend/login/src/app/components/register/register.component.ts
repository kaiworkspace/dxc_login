import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { RegisterService } from './register.service';
import { Router } from '@angular/router'

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  constructor(private registerService: RegisterService, private router: Router) { }

  ngOnInit(): void {
  }

  registerForm = new FormGroup({
    name: new FormControl("",
    [
      Validators.required,
      Validators.minLength(4),
      Validators.maxLength(36),
      this.noSymbolsValidator,
    ]),
    username: new FormControl("",
    [
      Validators.required,
      Validators.minLength(3),
      Validators.maxLength(36),
      this.noSymbolsValidator,
    ]),
    password: new FormControl("", [
      Validators.required,
      Validators.minLength(8),
      Validators.maxLength(36),
      this.escapeCharValidator,
    ])
  })

  registerSuccess = false
  nameCheck = false
  usernameCheck = false
  passwordCheck = false
  formCheck = false
  errorMessage = ""
  
  register(){
    // TODO:
    this.formCheck = true
    if(this.registerForm.valid){
      let name = this.registerForm.value.name?.trim()
      let username = this.registerForm.value.username?.trim()
      let password = this.registerForm.value.password?.trim()
      
      // call service
      if(name!=undefined && username!=undefined && password != undefined){
        this.registerService.register(name, username, password).subscribe(
          (res)=>{
            let data = JSON.parse(JSON.stringify(res))
            if(data.statusCode == 200){
              console.log(data.message)
              this.registerSuccess = true
              this.handleRedirect()
            }
          },
          (error)=>{
            console.log(error)
            this.errorMessage = error.error.message
          }
        )
      }
    }
  }

  handleRedirect(){
    setTimeout(()=>{
      console.log("redirecting...")
      this.router.navigate(['/login'])
    }, 3000)
  }

  uncheckName(){
    this.nameCheck = false
  }

  checkName(){
    this.nameCheck = true
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
  
}
