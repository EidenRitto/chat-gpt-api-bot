package cn.eiden.gpt.model;

import java.util.List;

/**
 * @author Eiden J.P Zhou
 * @date 2023/12/12
 */
public class ChatCompletions {
    private String model;

    private List<ChatMessage> messages;

    public static class ChatMessage{
        private String role;
        private String content;

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }
}
