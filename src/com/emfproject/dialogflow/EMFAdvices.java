package com.emfproject.dialogflow;

public class EMFAdvices {
	
	
	public static void showAdvice(String previusAction) 
	{
		switch(previusAction) 
		{
		case "START":
			System.out.println(EMFAdvicesMessages.START+"\n");
			break;
		case "MODEL_CREATED":
			System.out.println(EMFAdvicesMessages.MODEL_CREATED);
			System.out.println(EMFAdvicesMessages.NEW_MODEL_ADVICE);
			break;
			
		case "MODEL_LOADED":
			System.out.println(EMFAdvicesMessages.MODEL_LOADED);
			break;
			
		case "ELEMENT_CREATED":
			
			System.out.println(EMFAdvicesMessages.ELEMENT_CREATED);
			
			break;
			
		case "NEW_STATEMACHINE_CREATED":
			
			System.out.println(EMFAdvicesMessages.NEW_STATEMACHINE_ADVICE);
			System.out.println(EMFAdvicesMessages.ELEMENT_CREATED);
			
			break;
			
		case "NEW_ELEMENT_ORPHAN":
			
			System.out.println(EMFAdvicesMessages.NEW_ELEMENT_ORPHAN);
			System.out.println(EMFAdvicesMessages.ELEMENT_CREATED);
			break;
			
			
		}
	}

}
