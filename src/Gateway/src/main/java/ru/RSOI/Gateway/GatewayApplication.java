package ru.RSOI.Gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

//@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		//SpringApplication.run(GatewayApplication.class, args);

		RestOperations restOperations = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<?> entity = new HttpEntity<>(headers);

		String urlTemplate = UriComponentsBuilder.fromHttpUrl("http://localhost:443/api/v1/aboba")
				.queryParam("msisdn", "{msisdn}")
				.queryParam("email", "{email}")
				.queryParam("clientVersion", "{clientVersion}")
				.queryParam("clientType", "{clientType}")
				.queryParam("issuerName", "{issuerName}")
				.queryParam("applicationName", "{applicationName}")
				.encode()
				.toUriString();

		try
		{
			HttpEntity<String> response = restOperations.exchange(
					urlTemplate,
					HttpMethod.GET,
					entity,
					String.class
			);
		}
		catch (RestClientException e)
		{

			System.out.println(e);
		}


		//System.out.println(response);
	}

}


/*
public class GatewayApplication
{
	public static void main(String[] args) {
		Date d = Date.valueOf("2021-10-08");
		Timestamp ts = new Timestamp(d.getTime());
		System.out.println(ts);
	}
}
*/
