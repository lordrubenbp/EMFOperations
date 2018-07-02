package com.emfproject;

import java.util.ResourceBundle;

public class EMFOperationsMessages {
	
	
	public static void printMessage(String code) 
	{
		if(!code.equals("QUERY")) {
		System.out.println(ResourceBundle.getBundle("com.emfproject.lang.Messages").getString(code));
		}else 
		{
		System.out.print(ResourceBundle.getBundle("com.emfproject.lang.Messages").getString(code));

		}
	}
	
}
