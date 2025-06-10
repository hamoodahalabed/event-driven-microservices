package com.eazybytes.gatewayserver.config;

import com.eazybytes.gatewayserver.service.client.CustomerSummaryClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/**
 * Configuration class for setting up HTTP clients used in the gateway server.
 * This class creates and configures the necessary beans for making HTTP requests
 * to other microservices.
 */
@Configuration
public class ClientConfig {

    /**
     * Base URL for the API endpoints, loaded from application properties.
     */
    @Value("${app.base-url}")
    private String baseUrl;

    /**
     * Creates and configures a CustomerSummaryClient bean.
     * 
     * This method:
     * 1. Creates a WebClient with the configured base URL
     * 2. Wraps it in a WebClientAdapter
     * 3. Uses HttpServiceProxyFactory to create a client proxy
     * 4. Returns a fully configured CustomerSummaryClient ready for making API calls
     * 
     * @return A configured CustomerSummaryClient instance
     */
    @Bean
    CustomerSummaryClient customerClient() {
        // 1. Create a WebClient instance configured with the base URL
        WebClient webClient = WebClient.builder().baseUrl(baseUrl).build();

        // 2. Adapt WebClient so HttpServiceProxyFactory can use it
        WebClientAdapter adapter = WebClientAdapter.create(webClient);

        // 3. HttpServiceProxyFactory reads your interface and creates
        //    the actual HTTP calling code behind the scenes.
        //    It will generate the code to build URLs, send requests, handle responses, etc.
        //    so it read the annotation etc in the class and create the code for each
        //    insted of repeating the .url or .block or .bla bla of each method it do that
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        // 4. Create a real implementation of your interface 'CustomerSummaryClient'.
        //    When you call methods on this object, it will perform the HTTP calls automatically.
        return factory.createClient(CustomerSummaryClient.class);
    }

}
