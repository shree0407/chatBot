package dev.shreeya;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    @Bean
    public CommandLineRunner loadData(AppUserRepository appUserRepository, JobRepository jobRepository) {
        return args -> {
            // Adding sample users
            appUserRepository.save(new App_Users("Alice", "Java, Spring"));
            appUserRepository.save(new App_Users("Bob", "Python, Machine Learning"));

            // Adding sample jobs
            jobRepository.save(new Job("TechCorp", "Looking for a Java and Spring developer"));
            jobRepository.save(new Job("DataWorld", "Python developer with ML experience needed"));
            jobRepository.save(new Job("TechCorp", "Looking for a Java and Spring developer"));
            jobRepository.save(new Job("DataWorld", "Python developer with ML experience needed"));
            jobRepository.save(new Job("DevSolutions", "Looking for a Java and Spring developer"));
            jobRepository.save(new Job("MLTech", "Python developer with ML experience needed"));
            jobRepository.save(new Job("InnovateAI", "Python developer with Machine Learning skills required"));
        };
    }
}
