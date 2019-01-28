package com.bradleypmartinsandbox.chat_a_box_tutorial;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class ChatComparator implements Comparator<ChatMessage> {

    public int compare(ChatMessage left, ChatMessage right) {
        try {
            // comparing two ChatMessages
            Date leftDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH)
                    .parse(left.getChatSendTime());
            Date rightDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH)
                    .parse(right.getChatSendTime());
            if (leftDate.before(rightDate)) {
                return 1;
            } else if (leftDate.after(rightDate)) {
                return -1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
