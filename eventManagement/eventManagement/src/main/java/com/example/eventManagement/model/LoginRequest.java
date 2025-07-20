package com.example.eventManagement.model;

public class LoginRequest {
    private String email;
    private String password;
    private String role;

    public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	// Getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
