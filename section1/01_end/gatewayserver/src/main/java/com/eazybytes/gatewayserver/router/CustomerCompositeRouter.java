package com.eazybytes.gatewayserver.router;

import com.eazybytes.gatewayserver.handler.CustomerCompositeHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

//"If someone sends a GET request to /api/composite/fetchCustomerSummary,
// and accepts JSON, and includes a query parameter mobileNumber,
// then handle it using fetchCustomerSummary()."
@Configuration(proxyBeanMethods = false)
public class CustomerCompositeRouter {

    @Bean
    public RouterFunction<ServerResponse> route(CustomerCompositeHandler customerCompositeHandler) {
        return RouterFunctions.route(RequestPredicates.GET("/api/composite/fetchCustomerSummary")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON))
                        .and(RequestPredicates.queryParam("mobileNumber", param -> true)),
                customerCompositeHandler::fetchCustomerSummary);
    }
}
//This is a Spring @Configuration class that sets up routes
// for handling HTTP requests (similar to @RequestMapping but using a functional
// style with Spring WebFlux).



/*
        The two modes explained simply:
        Default mode (proxyBeanMethods = true):

        Spring creates a special version of your configuration class
        Tracks all bean creations
        Ensures you always get the same bean instance when methods call each other
        This tracking adds overhead
        Lite mode (proxyBeanMethods = false):

        Spring uses your configuration class as-is
        No special tracking
        Faster and uses less memory
        Perfect when your @Bean methods don't call each other
*/
//------------------------------------------------------------------------------------------//
//CustomerCompositeRouterdoes the job of a controller,
// but using functional routing instead of @RestController and @RequestMapping

/*
    In Spring WebFlux, instead of using @RestController and @RequestMapping like traditional MVC,
    we define routes using functional style with RouterFunctions.

    This class sets up a route that listens for:
      - HTTP GET requests to the path "/api/composite/fetchCustomerSummary"
      - Accepts "application/json" media type
      - Requires a query parameter "mobileNumber"

    When all these conditions are met, the request is handled by the
    fetchCustomerSummary() method in the CustomerCompositeHandler class.

    This is a common pattern in reactive applications to keep things lightweight and fully non-blocking.
*/
