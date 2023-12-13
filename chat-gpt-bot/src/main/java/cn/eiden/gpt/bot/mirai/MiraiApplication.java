package cn.eiden.gpt.bot.mirai;

import cn.eiden.gpt.bot.mirai.events.BotGroupEvent;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.auth.BotAuthorization;
import net.mamoe.mirai.utils.BotConfiguration;

/**
 * @author Eiden J.P Zhou
 * @date 2023/12/12
 */
public class MiraiApplication {
    public static void main(String[] args) {
        BotConfiguration botConfiguration = new BotConfiguration();
        botConfiguration.fileBasedDeviceInfo();
        botConfiguration.setProtocol(BotConfiguration.MiraiProtocol.ANDROID_WATCH);
        Bot bot = BotFactory.INSTANCE.newBot(3025250990L, BotAuthorization.byQRCode(),botConfiguration);
        bot.login();
        //输出好友
        bot.getFriends().forEach(friend -> System.out.println(friend.getId() + ":" + friend.getNick()));
        bot.getEventChannel().registerListenerHost(new BotGroupEvent());
        System.out.println("系统启动完成！");
        //阻塞当前线程
        bot.join();
    }
}
