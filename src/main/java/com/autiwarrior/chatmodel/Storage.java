package com.autiwarrior.chatmodel;
import java.util.ArrayList;
import java.util.List;

public class Storage {

    public static List<ActiveUser> activeUserList = new ArrayList<>(); // 5user

    public static void removeBySession(String session){
        for (int i=0;i<activeUserList.size();i++){ // i=  0  1  2  3  4
            if(activeUserList.get(i).getSession().equals(session)){
                activeUserList.remove(i);
                break;
            }
        }
    }
}