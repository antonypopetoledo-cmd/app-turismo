package com.example.demo.dtos;

import com.example.demo.models.Supplier;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public class RegisterSupplierDTO {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8)
    private String password;

    private String firstname;

    private String lastname;

    @NotNull
    private Supplier.SupplierType type;

    private String brand;

    private String guideIdentifier;
       
    private String documentNumber;

    @NotBlank
    private String country;

    @NotBlank
    private String ruc;

    @NotBlank
    private String phone;

    @NotBlank
    @URL
    private String businessUrl;

    public RegisterSupplierDTO() {
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

    public Supplier.SupplierType getType() {
        return type;
    }

    public void setType(Supplier.SupplierType type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getGuideIdentifier() {
        return guideIdentifier;
    }

    public void setGuideIdentifier(String guideIdentifier) {
        this.guideIdentifier = guideIdentifier;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBusinessUrl() {
        return businessUrl;
    }

    public void setBusinessUrl(String businessUrl) {
        this.businessUrl = businessUrl;
    }

    @Override
    public String toString() {
        return "RegisterSupplierDTO{" + "email=" + email + ", password=" + password + ", firstname=" + firstname + ", lastname=" + lastname + ", type=" + type + ", brand=" + brand + ", guideIdentifier=" + guideIdentifier + ", documentNumber=" + documentNumber + ", country=" + country + ", ruc=" + ruc + ", phone=" + phone + ", businessUrl=" + businessUrl + '}';
    }
    
    

}
