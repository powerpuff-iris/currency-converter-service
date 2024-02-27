# Currency Converter Service

This service is a simple tool that provides real-time currency conversion rates using the Exchange Rates API ( https://exchangeratesapi.io/ ).

## Features

1. **Currency Conversion**: The service can convert any amount from one currency to another using real-time exchange rates. It uses the external API to fetch the latest rates.
2. **Caching**: To improve performance and reduce the number of API calls, the service caches the exchange rates. It uses Spring's `@Cacheable` annotation for this purpose.
3. **Error Handling**: In case of a failure in currency conversion, the service provides a fallback response. It calculates the conversion using the latest available rates.

## Libraries and Technologies

1. **Java 17**
2. **Spring Boot**
3. **Spring Cache**
4. **Feign Client**: This is a declarative web service client used to make HTTP requests to the external API for fetching exchange rates.

## Issues Addressed

1. **Performance**: By caching the exchange rates, the service reduces the number of API calls, thereby improving performance.
2. **Reliability**: The service handles failures in currency conversion by providing a fallback response. This ensures that the service remains reliable even when the external API is not available.

## How to Use

The Currency Converter Service exposes a RESTful API endpoint. Here's how you can use it:

**Endpoint**: The endpoint for the conversion service is /converter.

**HTTP Method**: The service uses the GET HTTP method.

**Request Parameters**:

baseCurrency: This is the currency code of the base currency. It should be a valid currency code.
targetCurrency: This is the currency code of the target currency. It should also be a valid currency code.
amount: This is the amount in the base currency that you want to convert to the target currency. It cannot be null.

**Response**: The service returns a ConversionResponse object. This object contains the converted amount.

Example Usage:

To convert 100 USD to EUR, you would make a GET request to the following URL: 
http://localhost:9000/converter?from=USD&to=EUR&amount=100

