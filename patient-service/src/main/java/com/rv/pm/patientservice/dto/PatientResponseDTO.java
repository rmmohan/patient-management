package com.rv.pm.patientservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class PatientResponseDTO {
    public PatientResponseDTO(UUID id, @NotNull String name, @NotNull @Email String email, @NotNull String address, @NotNull LocalDate dateOfBirth) {
        this.id = id.toString();
        this.name = name;
        this.email = email;
        this.address = address;
        this.dateOfBirth = dateOfBirth.toString();
    }

    private String id;
    private String name;
    private String email;
    private String address;
    private String dateOfBirth;
}