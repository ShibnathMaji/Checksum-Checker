package com.application.cheksumapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ChecksumLocalAppApplication 
{
	public static void main(String[] args) 
	{
	    new SpringApplicationBuilder(ChecksumLocalAppApplication.class)
	        .headless(false)
	        .web(WebApplicationType.NONE)
	        .run(args);
	}
}