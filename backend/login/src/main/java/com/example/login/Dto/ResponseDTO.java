package com.example.login.Dto;

public class ResponseDTO {
	
	private String message;
	private Integer statusCode;
	private UserDTO userDto;
	
	public ResponseDTO(String message, UserDTO userDto, Integer statusCode) {
		this.message = message;
		this.userDto = userDto;
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public UserDTO getUserDto() {
		return userDto;
	}

	public void setUserDto(UserDTO userDto) {
		this.userDto = userDto;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}
}
