package com.rv.pm.patientservice.service;

import com.rv.pm.patientservice.dto.PatientRequestDTO;
import com.rv.pm.patientservice.dto.PatientResponseDTO;
import com.rv.pm.patientservice.exception.EmailAlreadyExistsException;
import com.rv.pm.patientservice.exception.PatientNotFoundException;
import com.rv.pm.patientservice.model.Patient;
import com.rv.pm.patientservice.repository.PatientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PatientService {

    private PatientRepository patientRepository;

    public List<PatientResponseDTO> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(patient -> new PatientResponseDTO(
                        patient.getId(),
                        patient.getName(),
                        patient.getEmail(),
                        patient.getAddress(),
                        patient.getDateOfBirth()
                ))
                .collect(Collectors.toList());
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        if(patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        Patient patient = new Patient();
        patient.setName(patientRequestDTO.getName());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));
        patient.setRegisteredDate(LocalDate.parse(patientRequestDTO.getRegisteredDate()));
        patient = patientRepository.save(patient);

        return new PatientResponseDTO(
                patient.getId(),
                patient.getName(),
                patient.getEmail(),
                patient.getAddress(),
                patient.getDateOfBirth()
        );
    }

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {
            Patient patient = patientRepository.findById(id)
                    .orElseThrow(() -> new PatientNotFoundException("Patient not found with id: " + id));

            if(!patient.getEmail().equals(patientRequestDTO.getEmail()) && patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
                throw new EmailAlreadyExistsException("Email already exists");
            }

            patient.setName(patientRequestDTO.getName());
            patient.setAddress(patientRequestDTO.getAddress());
            patient.setEmail(patientRequestDTO.getEmail());
            patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));
            patient = patientRepository.save(patient);

            return new PatientResponseDTO(
                    patient.getId(),
                    patient.getName(),
                    patient.getEmail(),
                    patient.getAddress(),
                    patient.getDateOfBirth()
            );
    }

    public void deletePatient(UUID id) {
        if(!patientRepository.existsById(id)) {
            throw new PatientNotFoundException("Patient not found with id: " + id);
        }
        patientRepository.deleteById(id);
    }
}