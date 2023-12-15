package cn.eiden.gpt.api;

import cn.eiden.gpt.model.ChatCompletions;
import cn.eiden.gpt.model.ChatMessage;
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

    public static Request createChatCompletionRequest(List<ChatMessage> chatMessages) throws JsonProcessingException {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        ChatCompletions chatCompletions = new ChatCompletions();
        chatCompletions.setModel("gpt-3.5-turbo");

        chatMessages.get(0).setRole("system");
        chatCompletions.setMessages(chatMessages);
        String content = objectMapper.writeValueAsString(chatCompletions);

        RequestBody body = RequestBody.create(content.getBytes(StandardCharsets.UTF_8),mediaType);
        return new Request.Builder()
                .url(apiUrl)
                .addHeader("Authorization","Bearer " + shareToken)
                .post(body)
                .build();
    }

    public static void sendMessageWithCallback(Callback callback, List<ChatMessage> chatMessages){
        try {
            Request request = createChatCompletionRequest(chatMessages);
            client.newCall(request).enqueue(callback);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
