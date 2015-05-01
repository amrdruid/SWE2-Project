package com.FCI.SWE.Models;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class NotificationHandler {
	//ArrayList<AbstractNotification> notifications;
	public String execute(AbstractNotification N,String uname){
		//System.out.println(N.getNotification(uname).toString());
		return N.getNotification(uname).toString();
	}
}