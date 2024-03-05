package org.example.other;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import lombok.SneakyThrows;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class ChatJippity {

    private String apiKey;

    record MessageHistory(String role, String content) {}

    List<MessageHistory> messageHistories = new ArrayList<>();

    @SneakyThrows
    public ChatJippity(){
        Files.lines(Paths.get("jippity.txt")).limit(1).forEach(line -> apiKey = line);
    }

    @SneakyThrows
    public void sendOpenaiApiRequest(String message) {

        try(HttpClient client = HttpClient.newHttpClient()){
            Map<String, Object> map = new HashMap<>();
            map.put("model", "gpt-3.5-turbo");
            ArrayList<Map<String, String>> list = new ArrayList<>();
            list.add(Map.of(
                    "role", "user",
                    "content", "Ты бот-администратор на miencraft (название RusCrafting) сервере" +
                            "и тебе нужно ответить на вопросы игроков, а также развлекать их шутками" +
                            "и интересными фактами. Пиши одним предложением, почти без пунктуации, без заглавных букв. Разговаривай неофициально, пиши как будто ты в чате." +
                            "Побольше эмоций. Можешь использовать маты"));
            messageHistories.forEach(mh -> list.add(Map.of("role", mh.role, "content", mh.content)));
            list.add(Map.of(
                    "role", "user",
                    "content", message));
            map.put("messages", list);
            map.put("max_tokens", 100);
            map.put("temperature", 0.7);
            String json = new ObjectMapper().writeValueAsString(map);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.proxyapi.ru/openai/v1/chat/completions"))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonResponse jsonResponse = new ObjectMapper().readValue(response.body(), JsonResponse.class);
            System.out.println(jsonResponse.choices.get(0).message.content);
            messageHistories.add(new MessageHistory("user", message));
            messageHistories.add(new MessageHistory("assistant", jsonResponse.choices.get(0).message.content));
        }
    }

}

class JsonResponse {
    public String id;
    public String object;
    public long created;
    public String model;
    public List<Choice> choices;
    public Usage usage;
    @JsonProperty("system_fingerprint")
    public String systemFingerprint;
}

class Choice {
    public int index;
    public Message message;
    public Object logprobs; // Use specific type if needed
    @JsonProperty("finish_reason")
    public String finishReason;
}

class Message {
    public String role;
    public String content;
}

class Usage {
    @JsonProperty("prompt_tokens")
    public int promptTokens;
    @JsonProperty("completion_tokens")
    public int completionTokens;
    @JsonProperty("total_tokens")
    public int totalTokens;
}
