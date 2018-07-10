package com.emfproject;

import java.util.ResourceBundle;

import com.emfproject.dialogflow.EMFDialogflowParseNew.UserStatus;

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

	public static void printHelp(UserStatus status) {
		
		
		switch(status) 
		{
		case START:
			
			System.out.println(ResourceBundle.getBundle("com.emfproject.lang.Messages").getString("START"));
			
			break;
		case MODEL_CREATED:
			
			System.out.println(ResourceBundle.getBundle("com.emfproject.lang.Messages").getString("MODEL_CREATED_ADVICE"));
			
			break;
		case MODEL_LOADED:
			
			System.out.println(ResourceBundle.getBundle("com.emfproject.lang.Messages").getString("MODEL_LOADED_ADVICE"));
			
			break;
		case NODE_CREATED:
	
			System.out.println(ResourceBundle.getBundle("com.emfproject.lang.Messages").getString("NODE_CREATED_ADVICE"));
			
			break;
		case ELEMENT_FOCUSED:
			
			System.out.println(ResourceBundle.getBundle("com.emfproject.lang.Messages").getString("ELEMENT_FOCUSED_ADVICE"));
			break;
		default:
			break;
		}
		
	}
	
}
