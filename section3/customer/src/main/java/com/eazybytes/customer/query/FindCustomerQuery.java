package com.eazybytes.customer.query;

import lombok.Value;

/**
 * VERB+NOUN+Query
 */
@Value // generates getters, toString, equals, and hashCode methods
public class FindCustomerQuery {

    private final String mobileNumber;

}
