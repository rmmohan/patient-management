package com.rv.pm.patientservice.service.kafka;

import com.rv.pm.patientservice.model.Patient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Service
@AllArgsConstructor
@Slf4j
public class KafkaProducer {
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public void sendEvent(Patient patient) {
        PatientEvent patientEvent = PatientEvent.newBuilder().
                setPatientId(patient.getId().toString()).
                setName(patient.getName()).
                setEmail(patient.getEmail()).
                setEventType("PATIENT_CREATED").
                build();
        try {
            log.info("Sending patient event: {}", patientEvent);
            kafkaTemplate.send("patient", patientEvent.toByteArray());
        } catch (Exception e) {
            log.error("Failed to send patient event: {}", e.getMessage());
        }
    }
}