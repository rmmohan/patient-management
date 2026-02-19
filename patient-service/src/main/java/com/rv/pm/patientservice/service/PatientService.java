package com.rv.pm.patientservice.service;

import com.rv.pm.patientservice.dto.PatientRequestDTO;
import com.rv.pm.patientservice.dto.PatientResponseDTO;
import com.rv.pm.patientservice.exception.EmailAlreadyExistsException;
import com.rv.pm.patientservice.exception.PatientNotFoundException;
import com.rv.pm.patientservice.model.Patient;
import com.rv.pm.patientservice.repository.PatientRepository;
import com.rv.pm.patientservice.service.grpc.BillingServiceGrpcClient;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PatientService {

    private PatientRepository patientRepository;
    private BillingServiceGrpcClient billingServiceGrpcClient;

    private static @NonNull PatientResponseDTO mapPatientToPatientResponseDTO(Patient patient) {
        return new PatientResponseDTO(patient.getId(), patient.getName(), patient.getEmail(), patient.getAddress(), patient.getDateOfBirth());
    }

    public List<PatientResponseDTO> getAllPatients() {
        return patientRepository.findAll().stream().map(patient -> mapPatientToPatientResponseDTO(patient)).collect(Collectors.toList());
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        Patient patient = mapPatientRequestDTOToPatient(patientRequestDTO);
        billingServiceGrpcClient.createBillingAccount(patient.getId().toString(), patient.getName(), patient.getEmail());

        return mapPatientToPatientResponseDTO(patient);
    }

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException("Patient not found with id: " + id));

        if (!patient.getEmail().equals(patientRequestDTO.getEmail()) && patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        patient = mapPatientRequestDTOToPatient(patientRequestDTO);
        patient = patientRepository.save(patient);

        return mapPatientToPatientResponseDTO(patient);
    }

    public void deletePatient(UUID id) {
        if (!patientRepository.existsById(id)) {
            throw new PatientNotFoundException("Patient not found with id: " + id);
        }
        patientRepository.deleteById(id);
    }

    private @NonNull Patient mapPatientRequestDTOToPatient(PatientRequestDTO patientRequestDTO) {
        Patient patient = new Patient();
        patient.setName(patientRequestDTO.getName());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));
        if (patientRequestDTO.getRegisteredDate() != null && !patientRequestDTO.getRegisteredDate().isEmpty()) {
            patient.setRegisteredDate(LocalDate.parse(patientRequestDTO.getRegisteredDate()));
        }
        patient = patientRepository.save(patient);
        return patient;
    }
}