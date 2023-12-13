package cn.eiden.gpt.bot.mirai.constant;

/**
 * @author Eiden J.P Zhou
 * @date 2023/12/13
 */
public class BotSystemMessageConstant {
    private BotSystemMessageConstant() {
    }

    public static final String CHAT_SESSION_FULL = "【info】会话数量超过15条，即将重置会话，手动重置会话请发送\"新会话\"";
    public static final String NEW_SESSION = "【info】已开启新会话";
    public static final String NEW_SESSION_KEY = "新会话";
}
