package com.dktechhub.mnnit.ee.whatswebcloner;

public class RecentChat {
    String name,lastMessageTime,lastMessage;
    int unseenMessages;
    public RecentChat(String name,String lastMessageTime,String lastMessage,int unseenMessages)
    {
        this.name=name;
        this.lastMessage=lastMessage;
        this.lastMessageTime=lastMessageTime;
        this.unseenMessages=unseenMessages;
    }
}
