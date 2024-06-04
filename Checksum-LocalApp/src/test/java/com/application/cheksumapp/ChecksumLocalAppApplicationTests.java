package com.application.cheksumapp;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class ChecksumLocalAppApplicationTests 
{
	@BeforeAll
	public static void setupHeadlessMode() 
	{
	    System.setProperty("java.awt.headless", "false");
	}
	@Test
	void contextLoads() 
	{
	}

}