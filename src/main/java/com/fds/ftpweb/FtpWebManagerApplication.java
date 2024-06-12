package com.fds.ftpweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class FtpWebManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FtpWebManagerApplication.class, args);
		System.out.println("| ---------------------------------------------------------------------------------- |");
		System.out.println("|                                    Started Success                                 |");
		System.out.println("| ---------------------------------------------------------------------------------- |");
	}

}
