document.addEventListener('DOMContentLoaded', function() {
    const sendButton = document.getElementById('send-btn');
    const userInputField = document.getElementById('user-input');
    const chatWindow = document.getElementById('chatWindow');

    let awaitingUserId = false;
    let awaitingChatResponse = false; // New flag to redirect queries after recommendations to chat API

    sendButton.addEventListener('click', function() {
        let userInput = userInputField.value.trim();
        if (userInput !== "") {
            addMessage(userInput, 'user');
            handleUserInput(userInput);
            userInputField.value = ""; // Clear input
        }
    });

    userInputField.addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            sendButton.click();
        }
    });

    function addMessage(message, sender) {
        const messageDiv = document.createElement('div');
        messageDiv.classList.add('message');
        messageDiv.classList.add(sender === 'user' ? 'user-message' : 'bot-message');
        messageDiv.textContent = message;
        chatWindow.appendChild(messageDiv);
        chatWindow.scrollTop = chatWindow.scrollHeight; // Scroll to the bottom
    }

    function handleUserInput(userMessage) {
        const recommendJobRegex = /\b(recommend|suggest|find|search|get|show|help with|look for)\b.*\b(job|position|role|opportunity)\b|\b(job|position|role|opportunity)\b.*\b(recommend|suggest|find|search|get|show|help with|look for)\b/i;
        const userIdRegex = /user\s*id\s*=\s*(\d+)/i;

        if (awaitingUserId) {
            const match = userMessage.match(userIdRegex);
            if (match && match[1]) {
                const userId = match[1]; // Extract the user ID
                fetchJobRecommendations(userId);
                awaitingUserId = false; // Reset the flag
                awaitingChatResponse = true; // Enable chat mode after recommendations
            } else {
                addMessage("Please provide your user ID in the correct format: `user id = <your_user_id>`.", 'bot');
            }
        } else if (awaitingChatResponse) {
            // Any new input after job recommendations should go to the chatbot API
            sendToChatBotAPI(userMessage);
        } else if (recommendJobRegex.test(userMessage)) {
            addMessage("Please provide your user ID in the format: `user id = <your_user_id>`.", 'bot');
            awaitingUserId = true; // Set flag to expect user ID next
        } else {
            // Initial state or no job-related queries, go to chatbot API
            sendToChatBotAPI(userMessage);
        }
    }

    function fetchJobRecommendations(userId) {
        fetch(`http://localhost:8080/recommendations/${encodeURIComponent(userId)}`)
            .then(response => response.json())
            .then(data => {
                if (data.length === 0) {
                    addMessage("No matching jobs found for your skills. Please try again later.", 'bot');
                } else {
                    addMessage("Here are some recommended jobs for you:", 'bot');
                    data.forEach(job => {
                        addMessage(`📌 Company: ${job.companyName} 💼 Role: ${job.jobDescription}`, 'bot');
                    });
                }
                awaitingChatResponse = true; // Enable normal chat after recommendations
            })
            .catch(error => {
                console.error('Error fetching job recommendations:', error);
                addMessage("Sorry, there was an error fetching job recommendations. Please try again.", 'bot');
                awaitingChatResponse = true; // Ensure normal chat mode is resumed on error
            });
    }

    function sendToChatBotAPI(userMessage) {
        // The user sends normal text like "Hello, bot! Can you help me?"
        // This message is automatically formatted as JSON when sent to the backend.

        fetch('http://localhost:8080/chat', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'  // Ensure JSON is sent
            },
            body: JSON.stringify({  // Wrap the user message in a JSON object
                userMessage: userMessage  // This is the user input
            })
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`Network response was not ok (${response.statusText})`);
                }
                return response.json();
            })
            .then(data => {
                console.log('Chatbot API Response:', data);  // Log the response from the chatbot API
                if (data && data.response) {
                    addMessage(data.response, 'bot');  // Display the bot's response
                } else {
                    addMessage("I'm here to help! Could you please clarify your request?", 'bot');
                }
            })
            .catch(error => {
                console.error('Error in chatbot API:', error);
                addMessage("Sorry, I couldn’t process your request. Please try again.", 'bot');
            });
    }



});
