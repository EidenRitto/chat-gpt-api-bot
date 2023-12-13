package cn.eiden.gpt.api;

import cn.eiden.gpt.model.ChatCompletions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Eiden J.P Zhou
 * @date 2023/12/13
 */
public class ChatCompletionApi {

    private static final String shareToken = "fk-1mmcqWDB8PcpwNXyNwHMVmgAHx4TWvaQaEIw6CR_imA";
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)  // 设置连接超时时间
            .readTimeout(1, TimeUnit.MINUTES)     // 设置读取超时时间
            .writeTimeout(1, TimeUnit.MINUTES)    // 设置写入超时时间
            .build();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String apiUrl = "http://localhost:8181/eiden_api_v1/v1/chat/completions";

    public static Request createChatCompletionRequest(String message,String... messageContext) throws JsonProcessingException {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        ChatCompletions chatCompletions = new ChatCompletions();
        chatCompletions.setModel("gpt-3.5-turbo");

        List<ChatCompletions.ChatMessage> messages = getChatMessages(message, messageContext);
        chatCompletions.setMessages(messages);
        String content = objectMapper.writeValueAsString(chatCompletions);

        RequestBody body = RequestBody.create(content.getBytes(StandardCharsets.UTF_8),mediaType);
        return new Request.Builder()
                .url(apiUrl)
                .addHeader("Authorization","Bearer " + shareToken)
                .post(body)
                .build();
    }

    public static void sendMessageWithCallback(Callback callback, String message, String... messageContext){
        try {
            Request request = createChatCompletionRequest(message, messageContext);
            client.newCall(request).enqueue(callback);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @NotNull
    private static List<ChatCompletions.ChatMessage> getChatMessages(String message, String[] messageContext) {
        List<ChatCompletions.ChatMessage> messages = new ArrayList<>();
        ChatCompletions.ChatMessage chatMessage = new ChatCompletions.ChatMessage();
        chatMessage.setRole("system");
        chatMessage.setContent(message);
        messages.add(chatMessage);

        for (String messageText : messageContext) {
            ChatCompletions.ChatMessage contextMessage = new ChatCompletions.ChatMessage();
            contextMessage.setRole("user");
            contextMessage.setContent(messageText);
            messages.add(contextMessage);
        }
        return messages;
    }
}
