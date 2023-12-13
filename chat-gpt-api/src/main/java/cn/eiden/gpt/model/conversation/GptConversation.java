package cn.eiden.gpt.model.conversation;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author Eiden J.P Zhou
 * @date 2023/12/12
 */
public class GptConversation {
    private String action = "next";

    @JsonProperty("parent_message_id")
    private String parentMessageId;
    private String model = "text-davinci-002-render-sha";

    @JsonProperty("timezone_offset_min")
    private int timezoneOffsetMin = -480;

    private String[] suggestions;

    @JsonProperty("history_and_training_disabled")
    private boolean historyAndTrainingDisabled = false;

    @JsonProperty("arkose_token")
    private String arkoseToken;

    @JsonProperty("conversation_mode")
    private ConversationMode conversationMode = new ConversationMode();

    @JsonProperty("force_paragen")
    private boolean forceParagen;

    @JsonProperty("force_rate_limit")
    private boolean forceRateLimit;

    private List<GptMessage> messages;

    public static class ConversationMode{
        private String kind = "primary_assistant";

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getParentMessageId() {
        return parentMessageId;
    }

    public void setParentMessageId(String parentMessageId) {
        this.parentMessageId = parentMessageId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getTimezoneOffsetMin() {
        return timezoneOffsetMin;
    }

    public void setTimezoneOffsetMin(int timezoneOffsetMin) {
        this.timezoneOffsetMin = timezoneOffsetMin;
    }

    public String[] getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(String[] suggestions) {
        this.suggestions = suggestions;
    }

    public boolean isHistoryAndTrainingDisabled() {
        return historyAndTrainingDisabled;
    }

    public void setHistoryAndTrainingDisabled(boolean historyAndTrainingDisabled) {
        this.historyAndTrainingDisabled = historyAndTrainingDisabled;
    }

    public String getArkoseToken() {
        return arkoseToken;
    }

    public void setArkoseToken(String arkoseToken) {
        this.arkoseToken = arkoseToken;
    }

    public ConversationMode getConversationMode() {
        return conversationMode;
    }

    public void setConversationMode(ConversationMode conversationMode) {
        this.conversationMode = conversationMode;
    }

    public boolean isForceParagen() {
        return forceParagen;
    }

    public void setForceParagen(boolean forceParagen) {
        this.forceParagen = forceParagen;
    }

    public boolean isForceRateLimit() {
        return forceRateLimit;
    }

    public void setForceRateLimit(boolean forceRateLimit) {
        this.forceRateLimit = forceRateLimit;
    }

    public List<GptMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<GptMessage> messages) {
        this.messages = messages;
    }
}
