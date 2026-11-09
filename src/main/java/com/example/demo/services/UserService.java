package com.example.demo.services;

import com.example.demo.dtos.RegisterSupplierDTO;
import com.example.demo.dtos.RegisterTouristDTO;
import com.example.demo.models.Supplier;
import com.example.demo.models.Tourist;
import com.example.demo.models.User;
import com.example.demo.repositories.SupplierRepository;
import com.example.demo.repositories.TouristRepository;
import com.example.demo.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final TouristRepository touristRepo;
    private final SupplierRepository supplierRepo;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepo, TouristRepository touristRepo, SupplierRepository supplierRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.touristRepo = touristRepo;
        this.supplierRepo = supplierRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public User getById(Long id) {
        return this.userRepo.findById(id).orElse(null);
    }

    public User getByEmail(String email) {
        return this.userRepo.findByEmail(email).orElse(null);
    }

    @Transactional
    public String registerTourist(RegisterTouristDTO dto) {
        if (dto == null) {
            return "Registration data is null";
        }

        if (this.userRepo.findByEmail(dto.getEmail()).isPresent()) {
            return "User already exists";
        }

        User user = new User();
        user.setFirstname(dto.getFirstname());
        user.setLastname(dto.getLastname());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(User.Role.TURISTA);
        user.setIsActive(true);

        this.userRepo.save(user);

        Tourist tourist = new Tourist();
        tourist.setUser(user);
        tourist.setCountry(dto.getCountry());
        tourist.setPassport(dto.getPassport());
        tourist.setPhone(dto.getPhone());

        this.touristRepo.save(tourist);

        return "Registration successful";
    }

    @Transactional
    public String registerSupplier(RegisterSupplierDTO dto) {

        if (dto == null) {
            return "Registration data is null";
        }

        if (this.userRepo.findByEmail(dto.getEmail()).isPresent()) {
            return "User already exists";
        }

        User user = new User();
        user.setFirstname(dto.getFirstname());
        user.setLastname(dto.getLastname());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(User.Role.PROVEEDOR);
        user.setIsActive(true);

        this.userRepo.save(user);

        Supplier supplier = new Supplier();
        supplier.setUser(user);
        supplier.setCountry(dto.getCountry());
        supplier.setType(dto.getType());
        supplier.setBrand(dto.getBrand());
        supplier.setGuideIdentifier(dto.getGuideIdentifier());        
        supplier.setDocumentNumber(dto.getDocumentNumber());
        supplier.setRuc(dto.getRuc());
        supplier.setPhone(dto.getPhone());
        supplier.setBusinessUrl(dto.getBusinessUrl());
        
        this.supplierRepo.save(supplier);

        return "Registration successful";
    }

}
