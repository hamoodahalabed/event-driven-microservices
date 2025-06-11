package com.eazybytes.customer.command.aggregate;

import com.eazybytes.customer.command.CreateCustomerCommand;
import com.eazybytes.customer.command.DeleteCustomerCommand;
import com.eazybytes.customer.command.UpdateCustomerCommand;
import com.eazybytes.customer.command.event.CustomerCreatedEvent;
import com.eazybytes.customer.command.event.CustomerDeletedEvent;
import com.eazybytes.customer.command.event.CustomerUpdatedEvent;
import com.eazybytes.customer.entity.Customer;
import com.eazybytes.customer.exception.CustomerAlreadyExistsException;
import com.eazybytes.customer.exception.ResourceNotFoundException;
import com.eazybytes.customer.repository.CustomerRepository;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Optional;

// Marks this class as an Aggregate in Axon Framework.
// An Aggregate represents a consistency boundary for handling commands and applying events.
// It contains business logic and maintains the state of an entity using event sourcing or standard persistence.
@Aggregate
public class CustomerAggregate { //the aggregate should have the same fields as the Customer entity

    @AggregateIdentifier //should match this
    private String customerId;
    private String name;
    private String email;
    private String mobileNumber;
    private boolean activeSw;

    public CustomerAggregate() {

    }

    @CommandHandler // handle the command in the parameter
    public CustomerAggregate(CreateCustomerCommand createCustomerCommand, CustomerRepository customerRepository) {
        /*Optional<Customer> optionalCustomer = customerRepository.
                findByMobileNumberAndActiveSw(createCustomerCommand.getMobileNumber(), true);
        if (optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer already registered with given mobileNumber "
                    + createCustomerCommand.getMobileNumber());
        }*/
        CustomerCreatedEvent customerCreatedEvent = new CustomerCreatedEvent();
        // This copies all matching fields (with the same names and compatible types)
        // from the createCustomerCommand object into the customerCreatedEvent object.
        BeanUtils.copyProperties(createCustomerCommand, customerCreatedEvent);
        // This applies the event and also triggers the @EventSourcingHandler in this class
        // and the @EventHandler in the projection class to update the database
        AggregateLifecycle.apply(customerCreatedEvent);
    }

    @EventSourcingHandler // updates the object in memory then handle the event in the projection
    public void on(CustomerCreatedEvent customerCreatedEvent) {
        this.customerId = customerCreatedEvent.getCustomerId();
        this.name = customerCreatedEvent.getName();
        this.email= customerCreatedEvent.getEmail();
        this.mobileNumber = customerCreatedEvent.getMobileNumber();
        this.activeSw = customerCreatedEvent.isActiveSw();
    }

    @CommandHandler
    public void handle(UpdateCustomerCommand updateCustomerCommand, EventStore eventStore) {
        /*List<?>   commands = eventStore.readEvents(updateCustomerCommand.getCustomerId()).asStream().toList();
        if(commands.isEmpty()) {
            throw new ResourceNotFoundException("Customer", "customerId", updateCustomerCommand.getCustomerId());
        }*/
        CustomerUpdatedEvent customerUpdatedEvent = new CustomerUpdatedEvent();
        BeanUtils.copyProperties(updateCustomerCommand, customerUpdatedEvent);
        AggregateLifecycle.apply(customerUpdatedEvent);
    }

    @EventSourcingHandler
    public void on(CustomerUpdatedEvent customerUpdatedEvent) {
        this.name = customerUpdatedEvent.getName();
        this.email= customerUpdatedEvent.getEmail();
    }

    @CommandHandler
    public void handle(DeleteCustomerCommand deleteCustomerCommand) {
        CustomerDeletedEvent customerDeletedEvent = new CustomerDeletedEvent();
        BeanUtils.copyProperties(deleteCustomerCommand, customerDeletedEvent);
        AggregateLifecycle.apply(customerDeletedEvent);
    }

    @EventSourcingHandler
    public void on(CustomerDeletedEvent customerDeletedEvent) {
        this.activeSw = customerDeletedEvent.isActiveSw();
    }

}
