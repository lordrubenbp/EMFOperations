package com.emfproject;

import java.util.ResourceBundle;

public class EMFOperationsMessages {
	
	
	public static void printMessage(String code) 
	{
		System.out.println(ResourceBundle.getBundle("com.emfproject.lang.Messages").getString(code));
	}
	
}
