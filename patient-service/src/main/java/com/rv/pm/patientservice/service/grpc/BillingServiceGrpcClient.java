package com.rv.pm.patientservice.service.grpc;

import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BillingServiceGrpcClient {
    private final BillingServiceGrpc.BillingServiceBlockingStub blockingStub;

    public BillingServiceGrpcClient(
        @Value("${grpc.billing-service.host:localhost}") String host,
        @Value("${grpc.billing-service.port:9001}") int port
    ) {
        log.info("Initializing BillingServiceGrpcClient with host: {} and port: {}", host, port);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
            .usePlaintext()
            .build();

        blockingStub = BillingServiceGrpc.newBlockingStub(channel);
    }

    public BillingResponse createBillingAccount(String patientId, String name, String email) {
        log.info("Creating billing account for patientId: {}, name: {}, email: {}", patientId, name, email);

        billing.BillingRequest request = billing.BillingRequest.newBuilder()
            .setPatientId(patientId)
            .setName(name)
            .setEmail(email)
            .build();

        BillingResponse response = blockingStub.createBillingAccount(request);
        log.info("Received billing response: {}", response);
        return response;
    }
}