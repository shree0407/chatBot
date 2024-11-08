package dev.shreeya;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:63342")
public class ChatController {

    private final ChatClient chatClient;
    private final JobRecommendationService jobRecommendationService;
    private final JobRepository jobRepository;

    public ChatController(ChatClient.Builder builder, JobRecommendationService jobRecommendationService, JobRepository jobRepository) {
        this.chatClient = builder.build();
        this.jobRecommendationService = jobRecommendationService;
        this.jobRepository = jobRepository;
    }

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chatWithBot(@RequestBody ChatRequest chatRequest) {
        String userMessage = chatRequest.getUserMessage();

        // Process the userMessage and get the response from the chatbot
        String botResponse = chatClient.prompt()
                .user(userMessage)
                .call()
                .content();

        // If the bot response is empty or null, return a default response
        if (botResponse == null || botResponse.trim().isEmpty()) {
            botResponse = "Sorry, I couldn't understand that. Could you please rephrase?";
        }

        // Create a ChatResponse object to send back
        ChatResponse response = new ChatResponse(botResponse);

        // Return the response with the correct status code (HttpStatus.OK)
        return new ResponseEntity<>(response, HttpStatus.OK);
    }




    @GetMapping("recommendations/{userId}")
    public List<Job> getJobRecommendations(@PathVariable Long userId) {
        List<Job> jobs = jobRecommendationService.getJobRecommendations(userId);

        if (jobs.isEmpty()) {
            Job noJobsFound = new Job();
            noJobsFound.setCompanyName("No matching jobs found.");
            noJobsFound.setJobDescription("Try updating your skills or check back later.");
            return Collections.singletonList(noJobsFound);
        }

        return jobs;
    }







}

class ChatRequest {
    private String userMessage;

    // Getters and Setters
    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }
}



