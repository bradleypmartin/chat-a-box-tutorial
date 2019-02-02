package com.bradleypmartinsandbox.chat_a_box_tutorial;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Comparator;

public class ChatComparator implements Comparator<ChatMessage> {

    private DateTimeFormatter mFormatter = DateTimeFormat.forPattern("dd:MM:yyyy HH:mm:ss");

    public int compare(ChatMessage left, ChatMessage right) {
        try {
            DateTime leftDT = mFormatter.parseDateTime(left.getChatSendTime());
            DateTime rightDT = mFormatter.parseDateTime(right.getChatSendTime());
            if (leftDT.isBefore(rightDT)) {
                return 1;
            } else if (rightDT.isBefore(leftDT)) {
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
