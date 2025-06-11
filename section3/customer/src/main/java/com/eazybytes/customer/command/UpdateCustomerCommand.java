package com.eazybytes.customer.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class UpdateCustomerCommand { // Command = Request to do something

    // This annotation tells Axon which aggregate instance should handle the command.
    // It acts like a primary key in the context of event sourcing,
    // helping Axon to route the command to the correct aggregate.
    @TargetAggregateIdentifier // Send this command to the object (aggregate) with this ID.
    // This must match the field in the aggregate
    private final String customerId;
    private final String name;
    private final String email;
    private final String mobileNumber;
    private final boolean activeSw;

}
//@TargetAggregateIdentifier → aggregate ID (e.g., customerId)
//Other fields → data needed for the command’s action