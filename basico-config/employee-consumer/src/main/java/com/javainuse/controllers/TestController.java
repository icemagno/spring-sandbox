package com.javainuse.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class TestController {

	@Autowired
	private LoadBalancerClient loadBalancer;	
	
	@RequestMapping(value = "/consume", method = RequestMethod.GET)
	public String firstPage() {

		ServiceInstance serviceInstance=loadBalancer.choose("employee-producer");
		System.out.println(serviceInstance.getUri());
		
		String baseUrl=serviceInstance.getUri().toString();
		
		baseUrl=baseUrl+"/employee";
		
		System.out.println("Encontrei o serviço em: " + baseUrl );
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response=null;
		
		try{
			response = restTemplate.exchange(baseUrl, HttpMethod.GET, getHeaders(), String.class );
		}catch (Exception ex) {
			System.out.println(ex);
		}

		return response.getBody();
	}	
	
	private static HttpEntity<?> getHeaders() throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		return new HttpEntity<>(headers);
	}		
	
}
