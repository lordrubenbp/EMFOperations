package com.emfproject.dialogflow;

import java.lang.reflect.InvocationTargetException;

import com.emfproject.EMFOperations;
import com.google.cloud.dialogflow.v2.QueryResult;

public class EMFDialogflowParse {
	EMFOperations op =null;
	public EMFDialogflowParse() throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException 
	{
		op = new EMFOperations();
		op.loadModelInstance("maquina/testDialog.xmi");
	}
	public  void parseCode(QueryResult queryResult, String actionCode)
			throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException {
	
		String element;
		String atribute;
		String value;
		String parent;
		String relationship;
		String modelName;
		switch (actionCode) {
		// addElement
		case "AE1":

			element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
			element=element.trim();
			System.out.println(element);
			op.addElement(element);

			break;
		// addElement
		case "AE3":

			element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
			atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
			value = queryResult.getParameters().getFieldsMap().get("value").getStringValue().toLowerCase();

			op.addElement(element, atribute, value);

			break;
		// addElement
		case "AE5":
			
			element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
			atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
			value = queryResult.getParameters().getFieldsMap().get("value").getStringValue().toLowerCase();
			parent=queryResult.getParameters().getFieldsMap().get("parent").getStringValue().toLowerCase();
			relationship=queryResult.getParameters().getFieldsMap().get("relationship").getStringValue().toLowerCase();
			
			op.addElement(element, atribute, value, parent, relationship);

			break;
		// addReferenceToFocusedElement
		case "ARFE":
			element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
			atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
			value = queryResult.getParameters().getFieldsMap().get("value").getStringValue().toLowerCase();
			relationship=queryResult.getParameters().getFieldsMap().get("relationship").getStringValue().toLowerCase();
			op.addReferenceToFocusedElement(element, atribute, value, relationship);
			
			break;
		// clearProperty
		case "CR":
			relationship=queryResult.getParameters().getFieldsMap().get("relationship").getStringValue().toLowerCase();
			op.clearProperty(relationship);
			break;
		// clearProperty
		case "CP":
			atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
			op.clearProperty(atribute);
			break;
			
		// deleteElement
		case "DE":
			element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
			atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
			value = queryResult.getParameters().getFieldsMap().get("value").getStringValue().toLowerCase();
			
			op.deleteElement(element, atribute, value);
			
			break;
		// getPropertyFromFocusedElement
		case "GPFE":
			op.getPropertiesFromFocusedElement();
			break;
		// loadModelInstance
		case "LM":
			modelName=queryResult.getParameters().getFieldsMap().get("modelName").getStringValue().toLowerCase();
			op.loadModelInstance("maquina/"+modelName+".xmi");
			
			break;

		// removeReferenceFromFocusedElement
		case "RRFE":
			
			element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
			atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
			value = queryResult.getParameters().getFieldsMap().get("value").getStringValue().toLowerCase();
			relationship=queryResult.getParameters().getFieldsMap().get("relationship").getStringValue().toLowerCase();
			op.removeReferenceFromFocusedElement(element, atribute, value, relationship);
			
			
			break;
		// renameElement
		case "RE":
			break;

		// saveModelInstance
		case "SM":
			break;
		// setFocusElement
		case "SFE1":

			element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
			op.setFocusElement(element);
			
			break;
		// setFocusElement
		case "SFE3":
			
			element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
			atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
			value = queryResult.getParameters().getFieldsMap().get("value").getStringValue().toLowerCase();
			op.setFocusElement(element, atribute, value);
			//op.getPropertiesFromFocusedElement();
			
			break;
		// setFocusElement
		case "SFE5":
			
			element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
			atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
			value = queryResult.getParameters().getFieldsMap().get("value").getStringValue().toLowerCase();
			parent=queryResult.getParameters().getFieldsMap().get("parent").getStringValue().toLowerCase();
			relationship=queryResult.getParameters().getFieldsMap().get("relationship").getStringValue().toLowerCase();
			op.setFocusElement(parent, element, atribute, value, relationship);
			break;
		// setProperty
		case "SP":
			atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
			value = queryResult.getParameters().getFieldsMap().get("value").getStringValue().toLowerCase();
			op.setProperty(atribute, value);
			break;

		case "EXIT":
			System.exit(0);
		default:
			System.out.println("action code no valido");
			break;
		}
		op.saveModelInstance();
	}

}
