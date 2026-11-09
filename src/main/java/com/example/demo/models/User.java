package com.example.demo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Firstname is mandatory")
    @Size(min = 2, max = 255, message = "Firstname must have between 2 and 255 characters")
    @Column(nullable = false, length = 255)
    private String firstname;

    @NotBlank(message = "Lastname is mandatory")
    @Size(min = 2, max = 255, message = "Lastname must have between 2 and 255 characters")
    @Column(nullable = false, length = 255)
    private String lastname;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Enter a valid email")
    @Size(max = 100)
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, max = 1000, message = "Password must have at least 8 characters")
    @Column(nullable = false)
    private String password;

    @NotNull(message = "User state is mandatory")
    @Column(nullable = false)
    private Boolean isActive;

    public enum Role {
        ADMIN,
        TURISTA,
        PROVEEDOR
    };
    @NotNull(message = "Role is mandatory")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
   
    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }


}
