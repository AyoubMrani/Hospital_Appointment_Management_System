package com.hendisantika;

import com.hendisantika.entity.User;
import com.hendisantika.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringbootAdminlte3TemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootAdminlte3TemplateApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(UserRepository userRepository) {
        return (args) -> {
            userRepository.save(new User("1", "hendi", "hendi@yopmail.com", "hendi123"));
            userRepository.save(new User("2", "kakashi", "kakashi@yopmail.com", "hendi123"));
            userRepository.save(new User("3", "naruto", "naruto@yopmail.com", "hendi123"));
            userRepository.save(new User("4", "sasuke", "sasuke@yopmail.com", "hendi123"));
            userRepository.save(new User("5", "sakura", "sakura@yopmail.com", "hendi123"));
        };
    }
}
