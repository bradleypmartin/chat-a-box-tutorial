package com.bradleypmartinsandbox.chat_a_box_tutorial;

public class ChatMessage {

    String chatSender;
    String chatSendTime;
    String chatText;

    public ChatMessage() {}

    public String getChatSender() {
        return chatSender;
    }

    public String getChatSendTime() {
        return chatSendTime;
    }

    public String getChatText() {
        return chatText;
    }

    @Override
    public String toString() {
        String result = "Chat Message : Sender [" + chatSender + "] Time [" + chatSendTime + "]" +
                " Chat Text : [" + chatText + "]";
        return result;
    }
}
