package cn.eiden.gpt.bot.mirai.events;

import cn.eiden.gpt.api.ChatCompletionApi;
import cn.eiden.gpt.bot.mirai.callback.GroupMessageGptCallback;
import cn.eiden.gpt.bot.mirai.constant.BotSystemMessageConstant;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Eiden J.P Zhou
 * @date 2023/12/12
 */
public class BotGroupEvent extends SimpleListenerHost {
    private final Map<Long, List<String>> groupMemberMessageContext = new HashMap<>(16);

    @EventHandler
    public ListeningStatus onGroupMessage(GroupMessageEvent event) {
        Consumer<String> sendMessageAction = message -> event.getGroup().sendMessage(new MessageChainBuilder()
                .append(new At(event.getSender().getId()))
                .append(" ")
                .append(new PlainText(message))
                .build());
        this.handlerGroup(event, event.getGroup(), sendMessageAction);
        //保持监听
        return ListeningStatus.LISTENING;
    }

    @EventHandler
    public ListeningStatus onFriendMessage(FriendMessageEvent event) {
        Consumer<String> sendMessageAction = message -> event.getFriend().sendMessage(new MessageChainBuilder()
                .append(new PlainText(message))
                .build());
        this.handlerGroup(event, event.getFriend(), sendMessageAction);
        //保持监听
        return ListeningStatus.LISTENING;
    }


    private void handlerGroup(MessageEvent event, Contact contact, Consumer<String> sendMessageAction) {
        long sendId = event.getSender().getId();
        String nick = event.getSender().getNick();
        MessageChain messageChain = event.getMessage();

        At atMessage = (At) messageChain.stream().filter(At.class::isInstance).findFirst().orElse(null);
        // 如果消息中提及了机器人
        if (event instanceof FriendMessageEvent || (atMessage != null && atMessage.getTarget() == event.getBot().getId())) {
            String message = messageChain.contentToString();
            if (message.contains(BotSystemMessageConstant.NEW_SESSION_KEY)) {
                groupMemberMessageContext.computeIfAbsent(sendId, k -> new ArrayList<>()).clear();
                contact.sendMessage(new MessageChainBuilder()
                        .append(new At(sendId))
                        .append(" ")
                        .append(new PlainText(BotSystemMessageConstant.NEW_SESSION))
                        .build());
                return;
            }
            // 上下文联想
            List<String> contextMessage = groupMemberMessageContext.computeIfAbsent(sendId, k -> new ArrayList<>());

            ChatCompletionApi.sendMessageWithCallback(new GroupMessageGptCallback(sendMessageAction), message, contextMessage.toArray(new String[0]));
            contextMessage.add(message);
            if (contextMessage.size() > 15) {
                contextMessage.clear();
                contact.sendMessage(new MessageChainBuilder()
                        .append(new At(sendId))
                        .append(" ")
                        .append(new PlainText(BotSystemMessageConstant.CHAT_SESSION_FULL))
                        .build());
            }
        }
    }

}