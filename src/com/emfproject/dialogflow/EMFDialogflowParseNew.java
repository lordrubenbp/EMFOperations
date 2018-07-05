package com.emfproject.dialogflow;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.ecore.EObject;

import com.emfproject.EMFOperations;
import com.emfproject.EMFOperationsMessages;
import com.emfproject.EMFOperationsNew;
import com.google.cloud.dialogflow.v2.QueryResult;
import com.google.protobuf.Value;

public class EMFDialogflowParseNew {
	EMFOperationsNew op = null;
	boolean autoFocus=false;
	EObject objectCreated = null;
	public boolean modelLoadedCorrectly = true;

	public EMFDialogflowParseNew() throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		op = new EMFOperationsNew();
		
		// op.loadModelInstance("maquina/testDialog.xmi");
	}

	public int getNumberOfParameters(QueryResult queryResult) {
		queryResult.getParameters().getFieldsMap();
		int count = 0;
		Map<String, Value> map = queryResult.getParameters().getFieldsMap();
		for (Entry<String, Value> entry : map.entrySet()) {
			
			if (!entry.getValue().getStringValue().equals("")||entry.getValue().getNumberValue()>0) {
				count++;
			}
		}
		return count;
	}

	public boolean getModelLoadedStatus() {
		return modelLoadedCorrectly;

	}

	public void resetModelLoadedStatus() {
		modelLoadedCorrectly = true;
	}

	public void parseCode(QueryResult queryResult, String actionCode)
			throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException {

		int numberOfParameters = getNumberOfParameters(queryResult);
		System.out.println("[DEBUG] parameters recived: " + numberOfParameters);
		String element;
		String atribute;
		String value;
		String relationship;
		String modelName;
		int order;

		switch (actionCode) {

		// validateModel
		case "VM":
			// op.validateModel();

			break;
		// setRootNode
		case "SRN":
			element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
			// op.setNodeElement(element);
			break;
		// createModel
		case "CM":
			modelName = queryResult.getParameters().getFieldsMap().get("modelName").getStringValue().toLowerCase();
			System.out.println(modelName);
			modelLoadedCorrectly = op.createModelInstance("maquina/" + modelName + ".xmi");
			op.saveModelInstance();
			// EMFOperationsMessages.printMessage("NEW_MODEL_ADVICE");
			break;
		case "LM":
			modelName = queryResult.getParameters().getFieldsMap().get("modelName").getStringValue().toLowerCase();
			modelLoadedCorrectly = op.loadModelInstance("maquina/" + modelName + ".xmi");

			break;

		// resetAllContext
		case "RAC":
			return;
		// createElement
		case "CE":
			
			 objectCreated = null;
			element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
			atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
			value = queryResult.getParameters().getFieldsMap().get("value").getStringValue().toLowerCase();
			
			switch (numberOfParameters) {
			case 2:
				
				objectCreated=op.createSimpleElement(element);

				break;
			case 3:
				objectCreated=op.createElement(element, atribute, value);

				break;
			}

			op.saveModelInstance();
			if(objectCreated!=null && autoFocus) {op.focusedElement=objectCreated;}
			

			break;
			
		case "CEFE":
			
			 objectCreated = null;
			
			element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
			atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
			value = queryResult.getParameters().getFieldsMap().get("value").getStringValue().toLowerCase();
			relationship = queryResult.getParameters().getFieldsMap().get("relationship").getStringValue();
			
			switch (numberOfParameters) {
			case 3:
				
				objectCreated=op.createSimpleElementContentFocusElement(element, relationship);

				break;
			case 4:
				
				objectCreated=op.createElementContentFocusElement(element, atribute, value, relationship);

				break;
			}

			op.saveModelInstance();
			if(objectCreated!=null && autoFocus) {op.focusedElement=objectCreated;}
			break;
			
		case "FE":
			
			element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
			atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
			value = queryResult.getParameters().getFieldsMap().get("value").getStringValue().toLowerCase();
			order=(int) queryResult.getParameters().getFieldsMap().get("order").getNumberValue();
			if(order==0) {order=1;}
			switch (numberOfParameters) {
			case 3:
				
				op.focusSimpleElementOrder(element, order);

				break;
			case 4:
				
				op.focusElement(element, atribute, value);

				break;
			}

			op.saveModelInstance();

			break;
			
		case "FEFE":
			
			element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
			atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
			value = queryResult.getParameters().getFieldsMap().get("value").getStringValue().toLowerCase();
			relationship = queryResult.getParameters().getFieldsMap().get("relationship").getStringValue();
			order=(int) queryResult.getParameters().getFieldsMap().get("order").getNumberValue();
			if(order==0) {order=1;}

			System.out.println("ORDER: "+order);
			switch (numberOfParameters) {
			case 4:
				
				op.focusSimpleElementContentOrderFocusElement(element, relationship, order);

				break;
			case 5:
				
				op.focusElementContentFocusedElement(element, atribute, value, relationship);

				break;
			}

			op.saveModelInstance();

			break;
			
		case "DE":
			
			element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
			atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
			value = queryResult.getParameters().getFieldsMap().get("value").getStringValue().toLowerCase();
			order=(int) queryResult.getParameters().getFieldsMap().get("order").getNumberValue();
			if(order==0) {order=1;}
			switch (numberOfParameters) {
			case 3:
				
				op.deleteSimpleElementOrder(element, order);

				break;
			case 4:
				
				op.deleteElement(element, atribute, value);

				break;
			}

			op.saveModelInstance();

			break;
			
		case "DEFE":
			
			element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
			atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
			value = queryResult.getParameters().getFieldsMap().get("value").getStringValue().toLowerCase();
			relationship = queryResult.getParameters().getFieldsMap().get("relationship").getStringValue();
			order=(int) queryResult.getParameters().getFieldsMap().get("order").getNumberValue();
			if(order==0) {order=1;}

			switch (numberOfParameters) {
			case 4:
				
				op.deleteSimpleElementContentOrderFocusElement(element, relationship, order);

				break;
			case 5:
				
				op.deleteElementContentFocusElement(element, atribute, value, relationship);

				break;
			}

			op.saveModelInstance();

			break;
		
		case "AEFE":
			
			element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
			atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
			value = queryResult.getParameters().getFieldsMap().get("value").getStringValue().toLowerCase();
			relationship = queryResult.getParameters().getFieldsMap().get("relationship").getStringValue();
			

			switch (numberOfParameters) {
			case 4:
				
				op.addElementAsReferenceFocusElement(element, atribute, value, relationship);

				break;
			
			}

			op.saveModelInstance();

			break;
			
		case "REFE":
			
			element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
			atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
			value = queryResult.getParameters().getFieldsMap().get("value").getStringValue().toLowerCase();
			relationship = queryResult.getParameters().getFieldsMap().get("relationship").getStringValue();
			

			switch (numberOfParameters) {
			case 4:
				
				op.removeElementAsReferenceFocusElement(element, atribute, value, relationship);

				break;
			
			}

			op.saveModelInstance();

			break;

		case "UAFE":
			
		
			atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
			value = queryResult.getParameters().getFieldsMap().get("value").getStringValue().toLowerCase();
			
			
			switch (numberOfParameters) {
			case 2:
				
				op.updateAtributeFocusElement(atribute, value);

				break;
			
			}

			op.saveModelInstance();

			break;
			
		case "CAFE":
			
			atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
			
			switch (numberOfParameters) {
			case 1:
				
				op.clearAtributeFocusElement(atribute);

				break;
			
			}

			op.saveModelInstance();

			break;
		case "CRFE":
			
			relationship = queryResult.getParameters().getFieldsMap().get("relationship").getStringValue();
			
			switch (numberOfParameters) {
			case 1:
				
				op.clearReferenceFocusElement(relationship);

				break;
			
			}

			op.saveModelInstance();

			break;
			
		case "AF":
			
			if(!autoFocus) 
			{
				autoFocus=true;
				System.out.println("autofocus on");
			}else 
			{
				autoFocus=false;
				System.out.println("autofocus off");
			}
			
			break;
		case "EXIT":
			System.exit(0);
		default:
			EMFOperationsMessages.printMessage("INVALID_CODE");
			break;
		}

	}

}
