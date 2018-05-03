package com.emfproject.dialogflow;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Map.Entry;

import com.emfproject.EMFOperations;
import com.google.cloud.dialogflow.v2.QueryResult;
import com.google.protobuf.Value;

public class EMFDialogflowParse {
	EMFOperations op = null;

	public EMFDialogflowParse() throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		op = new EMFOperations();
		op.loadModelInstance("maquina/testDialog.xmi");
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

	public void parseCode(QueryResult queryResult, String actionCode)
			throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException {

		int numberOfParameters = getNumberOfParameters(queryResult);
		System.out.println(numberOfParameters);
		String element;
		String atribute;
		String value;
		String parent;
		String relationship;
		String modelName;
		String oldValue;
		String newValue;

		switch (actionCode) {
		// addElement
		case "CE":

			switch (numberOfParameters) {
			case 2:
				element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
				op.addElement(element);
				break;
			case 3:
				element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
				atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
				value = queryResult.getParameters().getFieldsMap().get("value").getStringValue().toLowerCase();
				op.addElement(element, atribute, value);
				break;
			case 5:
				element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
				atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
				value = queryResult.getParameters().getFieldsMap().get("value").getStringValue().toLowerCase();
				parent = queryResult.getParameters().getFieldsMap().get("parent").getStringValue().toLowerCase();
				relationship = queryResult.getParameters().getFieldsMap().get("relationship").getStringValue()
						.toLowerCase();

				op.addElement(element, atribute, value, parent, relationship);
				break;
			}

			break;
		case "FE":
			switch (numberOfParameters) {
			case 2:
				element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
				op.setFocusElement(element);
				break;
			case 3:
				element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
				atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
				value = queryResult.getParameters().getFieldsMap().get("value").getStringValue().toLowerCase();
				op.setFocusElement(element, atribute, value);
				break;
			case 5:
				element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
				atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
				value = queryResult.getParameters().getFieldsMap().get("value").getStringValue().toLowerCase();
				parent = queryResult.getParameters().getFieldsMap().get("parent").getStringValue().toLowerCase();
				relationship = queryResult.getParameters().getFieldsMap().get("relationship").getStringValue()
						.toLowerCase();
				op.setFocusElement(parent, element, atribute, value, relationship);
				
				break;
			}
			break;
		case "DE":
			switch (numberOfParameters) {
			case 2:
				element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
				op.deleteElement(element);
				break;
			case 3:
				element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
				atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
				value = queryResult.getParameters().getFieldsMap().get("value").getStringValue().toLowerCase();
				op.deleteElement(element, atribute, value);
				break;
			case 5:
				element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
				atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
				value = queryResult.getParameters().getFieldsMap().get("value").getStringValue().toLowerCase();
				parent = queryResult.getParameters().getFieldsMap().get("parent").getStringValue().toLowerCase();
				relationship = queryResult.getParameters().getFieldsMap().get("relationship").getStringValue()
						.toLowerCase();
				op.deleteElement(parent, element, atribute, value, relationship);
				break;
			}
			break;
		/*case "CE1":

			element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
			System.out.println(element);
			op.addElement(element);

			break;
		// addElement
		case "CE3":

			element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
			atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
			value = queryResult.getParameters().getFieldsMap().get("value").getStringValue().toLowerCase();

			op.addElement(element, atribute, value);

			break;
		// addElement
		case "CE5":

			element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
			atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
			value = queryResult.getParameters().getFieldsMap().get("value").getStringValue().toLowerCase();
			parent = queryResult.getParameters().getFieldsMap().get("parent").getStringValue().toLowerCase();
			relationship = queryResult.getParameters().getFieldsMap().get("relationship").getStringValue()
					.toLowerCase();

			op.addElement(element, atribute, value, parent, relationship);

			break;*/
		// addReferenceToFocusedElement
		case "ARFE":
			element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
			atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
			value = queryResult.getParameters().getFieldsMap().get("value").getStringValue().toLowerCase();
			relationship = queryResult.getParameters().getFieldsMap().get("relationship").getStringValue()
					.toLowerCase();
			op.addReferenceToFocusedElement(element, atribute, value, relationship);

			break;
		// clearProperty
		case "CR":
			relationship = queryResult.getParameters().getFieldsMap().get("relationship").getStringValue()
					.toLowerCase();
			op.clearProperty(relationship);
			break;
		// clearProperty
		case "CP":
			atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
			op.clearProperty(atribute);
			break;

		/*case "DE1":
			element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
			op.deleteElement(element);
			break;
		// deleteElement
		case "DE3":
			element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
			atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
			value = queryResult.getParameters().getFieldsMap().get("value").getStringValue().toLowerCase();

			op.deleteElement(element, atribute, value);

			break;
		case "DE5":
			element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
			atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
			value = queryResult.getParameters().getFieldsMap().get("value").getStringValue().toLowerCase();
			parent = queryResult.getParameters().getFieldsMap().get("parent").getStringValue().toLowerCase();
			relationship = queryResult.getParameters().getFieldsMap().get("relationship").getStringValue()
					.toLowerCase();
			op.deleteElement(parent, element, atribute, value, relationship);
			break;*/
		// getPropertyFromFocusedElement
		case "GPFE":
			op.getPropertiesFromFocusedElement();
			break;
		// loadModelInstance
		case "LM":
			modelName = queryResult.getParameters().getFieldsMap().get("modelName").getStringValue().toLowerCase();
			op.loadModelInstance("maquina/" + modelName + ".xmi");

			break;

		// removeReferenceFromFocusedElement
		case "RRFE":

			element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
			atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
			value = queryResult.getParameters().getFieldsMap().get("value").getStringValue().toLowerCase();
			relationship = queryResult.getParameters().getFieldsMap().get("relationship").getStringValue()
					.toLowerCase();
			op.removeReferenceFromFocusedElement(element, atribute, value, relationship);

			break;
		// renameElement
		case "RE":

			element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
			atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
			oldValue = queryResult.getParameters().getFieldsMap().get("oldValue").getStringValue().toLowerCase();
			newValue = queryResult.getParameters().getFieldsMap().get("newValue").getStringValue().toLowerCase();
			op.renameElement(element, atribute, oldValue, newValue);

			break;

		// saveModelInstance
		case "SM":
			break;
		// setFocusElement
		/*case "SFE1":

			element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
			op.setFocusElement(element);

			break;
		// setFocusElement
		case "SFE3":

			element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
			atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
			value = queryResult.getParameters().getFieldsMap().get("value").getStringValue().toLowerCase();
			op.setFocusElement(element, atribute, value);
			// op.getPropertiesFromFocusedElement();

			break;
		// setFocusElement
		case "SFE5":

			element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
			atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
			value = queryResult.getParameters().getFieldsMap().get("value").getStringValue().toLowerCase();
			parent = queryResult.getParameters().getFieldsMap().get("parent").getStringValue().toLowerCase();
			relationship = queryResult.getParameters().getFieldsMap().get("relationship").getStringValue()
					.toLowerCase();
			op.setFocusElement(parent, element, atribute, value, relationship);
			break;*/
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
