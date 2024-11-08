package dev.shreeya;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class JobRecommendationService {

    private final AppUserRepository appUserRepository;
    private final JobRepository jobRepository;
    private final ChatClient chatClient;

    public JobRecommendationService(AppUserRepository appUserRepository, JobRepository jobRepository, ChatClient.Builder chatClientBuilder) {
        this.appUserRepository = appUserRepository;
        this.jobRepository = jobRepository;
        this.chatClient = chatClientBuilder.build();
    }

//    public String getJobRecommendations(Long userId) {
//        // Retrieve the user by their ID
//        App_Users user = appUserRepository.findById(userId).orElse(null);
//        if (user == null) {
//            return "User not found.";
//        }
//
//        // Split the user's skills into an array
//        String[] userSkills = user.getSkills().split(", ");
//
//        // Retrieve all jobs from the repository
//        List<Job> jobs = jobRepository.findAll();
//        StringBuilder recommendations = new StringBuilder("Here are the recommended jobs for you:\n");
//
//        // Loop through the jobs and check if the job description contains any of the user's skills
//        boolean foundJob = false;
//        for (Job job : jobs) {
//            for (String skill : userSkills) {
//                if (job.getJobDescription().toLowerCase().contains(skill.toLowerCase())) {
//                    recommendations.append("📌 Company: ").append(job.getCompanyName())
//                            .append(" 💼 Role: ").append(job.getJobDescription()).append("\n");
//                    foundJob = true;
//                    break;  // Exit the inner loop once a match is found
//                }
//            }
//        }
//
//        if (foundJob) {
//            return recommendations.toString();
//        } else {
//            return "No job found for your skills.";
//
//        }
//    }


    public List<Job> getJobRecommendations(Long userId) {
        Optional<App_Users> userOpt = appUserRepository.findById(userId);
        if (!userOpt.isPresent()) {
            return Collections.emptyList();  // Return an empty list if user not found
        }

        App_Users user = userOpt.get();
        String userSkills = user.getSkills(); // Assuming skills are comma or space-separated.

        // Split skills and filter out any empty strings that may result from splitting
        String[] skillList = userSkills.split(",\\s*|\\s+");
        List<Job> jobs = jobRepository.findAll();
        List<Job> matchingJobs = new ArrayList<>();

        for (Job job : jobs) {
            String jobDescription = job.getJobDescription().toLowerCase();
            boolean matches = false;

            // Check if the job description contains any skill
            for (String skill : skillList) {
                skill = skill.trim().toLowerCase(); // Trim and convert to lowercase for uniformity
                if (!skill.isEmpty() && jobDescription.contains(skill)) {
                    matches = true;
                    break; // Stop checking further skills if one match is found
                }
            }

            if (matches) {
                matchingJobs.add(job); // Add only matching jobs
            }
        }

        return matchingJobs;  // Return only the jobs matching the user's skills
    }
}





