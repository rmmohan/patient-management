package com.rv.pm.analyticsservice.service.kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Service
@Slf4j
public class KafkaConsumer {

    @KafkaListener(topics = "patient", groupId = "analytics-service")
    public void consumeEvent(byte[] eventBytes) {
        log.info("Received patient event from Kafka");
        try {
            PatientEvent patientEvent = PatientEvent.parseFrom(eventBytes);
            // Business logic to process the patient event, e.g., update analytics, store in database, etc.

            log.info("Received event [PatientId: {}, PatientName: {}, PatientEmailId: {}]",
                    patientEvent.getPatientId(),
                    patientEvent.getName(),
                    patientEvent.getEmail());
        } catch (InvalidProtocolBufferException e) {
            log.error("Failed to parse patient event: {}", e.getMessage());
        }
    }
}