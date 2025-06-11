package com.eazybytes.customer.command.event;

import lombok.Data;

@Data
public class CustomerDeletedEvent { // Event = Something has happened

    private String customerId;
    private boolean activeSw;

}
// usually, the event will contain the same fields as the command