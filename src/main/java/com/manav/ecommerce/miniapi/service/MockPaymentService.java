package com.manav.ecommerce.miniapi.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class MockPaymentService {

    public boolean process(BigDecimal amount) {
        try {
            // Simulate network latency
            Thread.sleep(2000);

            // Simulate successful payment processing
            // In a real mock, we might randomly fail or check specific amounts.
            // Requirement says "returns a boolean 'true' for success".
            return true;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
}
