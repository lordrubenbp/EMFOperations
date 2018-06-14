package com.emfproject.dialogflow;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Map.Entry;

import com.emfproject.EMFOperations;
import com.emfproject.EMFOperationsMessages;
import com.google.cloud.dialogflow.v2.QueryResult;
import com.google.protobuf.Value;

public class EMFDialogflowParse {
	EMFOperations op = null;
	public boolean modelLoadedCorrectly=true;

	public EMFDialogflowParse() throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		op = new EMFOperations();
		//op.loadModelInstance("maquina/testDialog.xmi");
	}

	public int getNumberOfParameters(QueryResult queryResult) {
		queryResult.getParameters().getFieldsMap();
		int count = 0;
		Map<String, Value> map = queryResult.getParameters().getFieldsMap();
		for (Entry<String, Value> entry : map.entrySet()) {
			if (!entry.getValue().getStringValue().equals("")) {
				count++;
			}
		}
		return count;
	}
	
	public boolean getModelLoadedStatus() 
	{
		return modelLoadedCorrectly;
		
	}
	public void resetModelLoadedStatus() 
	{
		 modelLoadedCorrectly=true;
	}

	public void parseCode(QueryResult queryResult, String actionCode)
			throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException {

		int numberOfParameters = getNumberOfParameters(queryResult);
		System.out.println(numberOfParameters);
		String element;
		String atribute;
		String value;
		String parent;
		String parentAtribute;
		String parentValue;
		String childElement;
		String childAtribute;
		String childValue;
		String relationship;
		String modelName;
		String oldValue;
		String newValue;
		String childRelationship;

		switch (actionCode) {

		case "CM":
			modelName = queryResult.getParameters().getFieldsMap().get("modelName").getStringValue().toLowerCase();
			modelLoadedCorrectly=op.createModelInstance("maquina/" + modelName + ".xmi");
			EMFOperationsMessages.printMessage("NEW_MODEL_ADVICE");
			
			break;
		case "RAC":
			return;
		case "CE":

			element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
			atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
			value = queryResult.getParameters().getFieldsMap().get("value").getStringValue().toLowerCase();
			relationship = queryResult.getParameters().getFieldsMap().get("relationship").getStringValue().toLowerCase();
			parent = queryResult.getParameters().getFieldsMap().get("parent").getStringValue().toLowerCase();
			parentAtribute=queryResult.getParameters().getFieldsMap().get("parentAtribute").getStringValue().toLowerCase();
			parentValue=queryResult.getParameters().getFieldsMap().get("parentValue").getStringValue().toLowerCase();
			childElement= queryResult.getParameters().getFieldsMap().get("childElement").getStringValue().toLowerCase();
			childAtribute= queryResult.getParameters().getFieldsMap().get("childAtribute").getStringValue().toLowerCase();
			childValue= queryResult.getParameters().getFieldsMap().get("childValue").getStringValue().toLowerCase();
			childRelationship=queryResult.getParameters().getFieldsMap().get("childRelationship").getStringValue().toLowerCase();
			
			switch (numberOfParameters) {
			case 4:
				op.createElement(element);
				if(element.equals("statemachine")) 
				{
					EMFOperationsMessages.printMessage("NEW_ROOT_NODE_ADVICE");
				}
				break;
			case 6:
				op.createElementToFocusedElement(element, atribute, value, relationship);
				//EMFAdvices.showAdvice("ELEMENT_CREATED");
				break;
			case 5:
				op.createElement(element, atribute, value);
				
				EMFOperationsMessages.printMessage("NEW_ELEMENT_ORPHAN_ADVICE");
				break;
			case 7:
				op.createElement(element, atribute, value, parent, relationship);
				
				break;
			case 8:
				op.createElement(element, atribute, value, parent, parentAtribute, parentValue, relationship);
				
				break;
			case 10:
				op.createElement(element, atribute, value, parent, childElement, childAtribute, childValue,childRelationship,relationship);
				
				break;
			}
			
			op.saveModelInstance();
			
			break;
		case "FE":
			
			element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
			atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
			value = queryResult.getParameters().getFieldsMap().get("value").getStringValue().toLowerCase();
			parent = queryResult.getParameters().getFieldsMap().get("parent").getStringValue().toLowerCase();
			relationship = queryResult.getParameters().getFieldsMap().get("relationship").getStringValue()
					.toLowerCase();
			
			switch (numberOfParameters) {
			case 2:
				op.setFocusElement(element);
				
				break;
			case 3:
				op.setFocusElement(element, atribute, value);
				break;
			case 5:
				op.setFocusElement(parent, element, atribute, value, relationship);
				
				break;
			}
			EMFOperationsMessages.printMessage("ELEMENT_FOCUSED_ADVICE");
			op.saveModelInstance();
			break;
		case "DE":
			
			element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
			atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
			value = queryResult.getParameters().getFieldsMap().get("value").getStringValue().toLowerCase();
			parent = queryResult.getParameters().getFieldsMap().get("parent").getStringValue().toLowerCase();
			relationship = queryResult.getParameters().getFieldsMap().get("relationship").getStringValue().toLowerCase();
			
			switch (numberOfParameters) {
			case 2:
				op.deleteElement(element);
				if(element.equals("statemachine")) 
				{
					EMFOperationsMessages.printMessage("DELETE_ROOT_NODE");

				}
				break;
			case 3:
				op.deleteElement(element, atribute, value);
				
				break;
			case 5:
				op.deleteElement(parent, element, atribute, value, relationship);
				
				break;
			}
			op.saveModelInstance();
			break;
	
		// addReferenceToFocusedElement
		case "ARFE":
			element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
			atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
			value = queryResult.getParameters().getFieldsMap().get("value").getStringValue().toLowerCase();
			relationship = queryResult.getParameters().getFieldsMap().get("relationship").getStringValue()
					.toLowerCase();
			op.addReferenceToFocusedElement(element, atribute, value, relationship);
			
			op.saveModelInstance();

			break;
		// clearProperty
		case "CR":
			relationship = queryResult.getParameters().getFieldsMap().get("relationship").getStringValue()
					.toLowerCase();
			op.clearProperty(relationship);
			op.saveModelInstance();
			break;
		// clearProperty
		case "CP":
			atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
			op.clearProperty(atribute);
			op.saveModelInstance();
			break;

		
		// getPropertyFromFocusedElement
		case "GPFE":
			op.getPropertiesFromFocusedElement();
			break;
		// loadModelInstance
		case "LM":
			modelName = queryResult.getParameters().getFieldsMap().get("modelName").getStringValue().toLowerCase();
			modelLoadedCorrectly=op.loadModelInstance("maquina/" + modelName + ".xmi");
			
			break;

		// removeReferenceFromFocusedElement
		case "RRFE":

			element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
			atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
			value = queryResult.getParameters().getFieldsMap().get("value").getStringValue().toLowerCase();
			relationship = queryResult.getParameters().getFieldsMap().get("relationship").getStringValue()
					.toLowerCase();
			op.removeReferenceFromFocusedElement(element, atribute, value, relationship);
			op.saveModelInstance();

			break;
		// renameElement
		case "RE":

			element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
			atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
			oldValue = queryResult.getParameters().getFieldsMap().get("oldValue").getStringValue().toLowerCase();
			newValue = queryResult.getParameters().getFieldsMap().get("newValue").getStringValue().toLowerCase();
			op.renameElement(element, atribute, oldValue, newValue);
			op.saveModelInstance();

			break;

		// saveModelInstance
		case "SM":
			break;
		
		// setProperty
		case "SP":
			atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
			value = queryResult.getParameters().getFieldsMap().get("value").getStringValue().toLowerCase();
			op.setProperty(atribute, value);
			op.saveModelInstance();
			break;

		case "EXIT":
			System.exit(0);
		default:
			System.out.println("action code no valido");
			break;
		}
		
	
		
		
	}

}
