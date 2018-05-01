package com.emfproject.dialogflow;

import java.lang.reflect.InvocationTargetException;

import com.emfproject.EMFOperations;
import com.google.cloud.dialogflow.v2.QueryResult;

public class EMFDialogflowParse {
	
	
	
	public static void parseCode(QueryResult queryResult,String actionCode) throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException 
	{
		EMFOperations op = new EMFOperations();
		String element;
		String atribute;
		String value;
		switch(actionCode) 
		{
		case "CEAV":
			System.out.println("funciona");
			element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
			atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
			value = queryResult.getParameters().getFieldsMap().get("value").getStringValue().toLowerCase();
			
			op.loadModelInstance("maquina/testDialog.xmi");
			op.addElement(element, atribute, value);
			op.saveModelInstance();
			break;
		case "CE":
			System.out.println("funciona");
			element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();

			op.loadModelInstance("maquina/testDialog.xmi");
			op.addElement(element);
			op.saveModelInstance();
			break;
		 default:
			 System.out.println("action code no valido");
         break;
		}
	}

}
