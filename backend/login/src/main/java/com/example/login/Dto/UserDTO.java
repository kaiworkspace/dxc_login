package com.example.login.Dto;

import java.util.List;

import com.example.login.Models.RoleEntity;

public class UserDTO {
	
	private String username;
	private String name;
	private List<RoleEntity> roles;
	
	public UserDTO(String username, String name, List<RoleEntity> roles) {
		this.username = username;
		this.name = name;
		this.roles = roles;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<RoleEntity> getRoles() {
		return roles;
	}
	public void setRoles(List<RoleEntity> roles) {
		this.roles = roles;
	}
}
