package cn.eiden.gpt;

import cn.eiden.gpt.model.ChatCompletionResponse;
import cn.eiden.gpt.model.ChatCompletions;
import cn.eiden.gpt.model.ChatMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Eiden J.P Zhou
 * @date 2023/12/12
 */
public class ApiApplication {
    private static String shareToken = "fk-1mmcqWDB8PcpwNXyNwHMVmgAHx4TWvaQaEIw6CR_imA";
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)  // 设置连接超时时间
            .readTimeout(1, TimeUnit.MINUTES)     // 设置读取超时时间
            .writeTimeout(1, TimeUnit.MINUTES)    // 设置写入超时时间
            .build();

    public static void main(String[] args) {


        System.out.println("请输入你的问题：" );
        while (true){
            // 从控制台读取用户输入
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String line = null;
            try {
                line = reader.readLine();
                sendMessage(line);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public static void sendMessage(String messageText) throws JsonProcessingException {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        ChatCompletions chatCompletions = new ChatCompletions();
        chatCompletions.setModel("gpt-3.5-turbo");
        ChatMessage message = new ChatMessage();
        message.setRole("system");
        message.setContent(messageText);
        chatCompletions.setMessages(List.of(message));

        RequestBody body = RequestBody.create(mediaType, objectMapper.writeValueAsString(chatCompletions));


        Request request = new Request.Builder()
                .url("http://localhost:8181/eiden_api_v1/v1/chat/completions")
                .addHeader("Authorization","Bearer " + shareToken)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 请求失败的处理
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    // 请求不成功的处理
                    throw new IOException("Unexpected code " + response);
                } else {
                    // 处理响应流
                    try (ResponseBody responseBody = response.body()) {
                        if (responseBody != null) {
                            BufferedReader reader = new BufferedReader(responseBody.charStream());
                            String line;
                            while ((line = reader.readLine()) != null) {
                                // 处理每一行数据
                                ChatCompletionResponse chatCompletionResponse = objectMapper.readValue(line, ChatCompletionResponse.class);

                                System.out.println(chatCompletionResponse.getChoices().stream()
                                        .map(ChatCompletionResponse.Choice::getMessage)
                                        .map(ChatCompletionResponse.Choice.Message::getContent)
                                        .collect(Collectors.joining("\n")));
                            }
                        }
                    }
                }
            }
        });
    }
}
