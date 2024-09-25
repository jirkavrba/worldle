package dev.vrba.discord.worldle.api;

import org.springframework.boot.SpringApplication;

public class TestWorldleApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(WorldleApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
