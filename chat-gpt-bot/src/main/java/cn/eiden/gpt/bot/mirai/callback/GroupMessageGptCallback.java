package cn.eiden.gpt.bot.mirai.callback;

import cn.eiden.gpt.model.ChatCompletionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.*;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * @author Eiden J.P Zhou
 * @date 2023/12/13
 */
public class GroupMessageGptCallback implements Callback {
    private final Group group;
    private final long userId;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GroupMessageGptCallback(Group group, long userId) {
        this.group = group;
        this.userId = userId;
    }

    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {
        group.sendMessage(new MessageChainBuilder()
                .append(new At(userId))
                .append(" ")
                .append(new PlainText("gpt服务异常"))
                .build());
    }

    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        // 处理响应流
        try (ResponseBody responseBody = response.body()) {
            if (responseBody != null) {
                BufferedReader reader = new BufferedReader(responseBody.charStream());
                String line;
                while ((line = reader.readLine()) != null) {
                    // 处理每一行数据
                    ChatCompletionResponse chatCompletionResponse = objectMapper.readValue(line, ChatCompletionResponse.class);
                    String text;
                    if (chatCompletionResponse.getError() != null){
                        text = chatCompletionResponse.getError().getMessage();
                    }else {
                        text = chatCompletionResponse.getChoices().stream()
                                .map(ChatCompletionResponse.Choice::getMessage)
                                .map(ChatCompletionResponse.Choice.Message::getContent)
                                .collect(Collectors.joining("\n"));
                    }
                    group.sendMessage(new MessageChainBuilder().append(new At(userId)).append(" ").append(new PlainText(text)).build());
                }
            }
        }
    }
}
