package com.eazybytes.customer.command.event;

import lombok.Data;

@Data
public class CustomerUpdatedEvent { // Event = Something has happened

    private String customerId;
    private String name;
    private String email;
    private String mobileNumber;
    private boolean activeSw;

}

// usually, the event will contain the same fields as the command