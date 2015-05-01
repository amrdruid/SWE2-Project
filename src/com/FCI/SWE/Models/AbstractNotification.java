package com.FCI.SWE.Models;

import java.util.ArrayList;

public abstract class AbstractNotification {
	
	public AbstractNotification() {
		// TODO Auto-generated constructor stub
	}
	public abstract ArrayList<String> getNotification(String uname);
}