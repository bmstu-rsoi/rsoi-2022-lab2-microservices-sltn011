package ru.RSOI.Gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
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
