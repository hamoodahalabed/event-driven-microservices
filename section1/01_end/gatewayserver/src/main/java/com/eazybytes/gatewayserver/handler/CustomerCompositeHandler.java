package com.eazybytes.gatewayserver.handler;

import com.eazybytes.gatewayserver.dto.*;
import com.eazybytes.gatewayserver.service.client.CustomerSummaryClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CustomerCompositeHandler {

    private final CustomerSummaryClient customerSummaryClient;

    // this is only handler method not a controller method so  it will not handle any http request
    public Mono<ServerResponse> fetchCustomerSummary(ServerRequest serverRequest) {
      // This is an object representing an incoming HTTP request (often in reactive Spring WebFlux).
      // It contains all the info about the request, like headers, path, query parameters, etc.
      String mobileNumber = serverRequest.queryParam("mobileNumber").get();

        Mono<ResponseEntity<CustomerDto>> customerDetails = customerSummaryClient.fetchCustomerDetails(mobileNumber);
        Mono<ResponseEntity<AccountsDto>> accountDetails = customerSummaryClient.fetchAccountDetails(mobileNumber);
        Mono<ResponseEntity<LoansDto>> loanDetails = customerSummaryClient.fetchLoanDetails(mobileNumber);
        Mono<ResponseEntity<CardsDto>> cardDetails = customerSummaryClient.fetchCardDetails(mobileNumber);

        // Waits for all four async requests (customer, accounts, loans, and cards) to finish at the same time.
        return Mono.zip(customerDetails, accountDetails, loanDetails, cardDetails)
                .flatMap(tuple -> {
                    CustomerDto customerDto = tuple.getT1().getBody();
                    AccountsDto accountsDto = tuple.getT2().getBody();
                    LoansDto loansDto = tuple.getT3().getBody();
                    CardsDto cardsDto = tuple.getT4().getBody();

                    CustomerSummaryDto customerSummaryDto = new CustomerSummaryDto(customerDto, accountsDto, loansDto, cardsDto);
                    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                            .body(BodyInserters.fromValue(customerSummaryDto));
                });


    }

}
/*
      Mono.zip(...)
      Waits for all four async requests (customer, accounts, loans, and cards) to finish at the same time.

      Once all responses are ready, it gives them to you as a tuple (like a box with 4 values inside).

      You open the box using tuple.getT1(), getT2(), etc. to get each response:

      getT1() → Customer data

      getT2() → Accounts data

      getT3() → Loans data

      getT4() → Cards data

      You then create a CustomerSummaryDto by combining all four pieces.

      Finally, you return an HTTP response:

      Status: 200 OK

      Type: JSON

      Body: the combined CustomerSummaryDto


* */


