package com.emfproject;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

public class TestNew {
	
	public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException 
	{
		EMFOperationsNew op= new EMFOperationsNew();
		

		//op.createModelInstance("maquina/prueba15.xmi");
		op.loadModelInstance("maquina/prueba15.xmi");
		
		//op.createElement("state", "name", "pruebita");
		//op.createElement("pene");
		//op.createElement("state", "name", "gawrg");
		//op.createElement("pene", "state", "name", "pruebita", "transitions");
		//op.createElement("transition");
		//op.createElementWithAtributeAndReference("state", "name", "mivida", "statemachine", "states");
		//op.createElementWithAtributeAndReference("command", "name", "pichita", "state", "name", "testeo", "actions");
		//op.createSimpleElementWithReference("transition", "state", "name", "testeo", "transitions");
		//op.setFocusElement("statemachine");
		//op.createSimpleElement("transition");
		//op.createElementWithAtribute("state", "name", "pepe");
		//op.createSimpleElement("statemachine");
		//op.getPropertiesFromFocusedElement();
		//op.focusElementReferenceSimpleElement("transition", "state", "name", "hola3", "state");
		//op.focusElementContentSimpleElement("transition", "state", "name", "hola3", "state");
		//op.focusElement("state","name","hola");
		//op.focusSimpleElement("statemachine");
		op.focusSimpleElement("statemachine");
		op.focusElementContentFocusedElement("state", "name", "hola", "states");
		op.focusSimpleElementContentOrderFocusElement("transition", "transitions", 1);
		//op.addElementAsReferenceFocusElement("state", "name", "hola1", "state");
		//op.addElementAsReferenceFocusElement("command", "name", "comando_1", "actions");
		//op.deleteElementContentFocusElement("state", "name", "hola3", "states");
		//op.focusElementContentFocusedElement("state", "name", "hola1", "states");
		//op.deleteSimpleElementContentOrderFocusElement("transition", "transitions", 1);
		//op.focusElement("state", "name", "hola1");
		//op.focusSimpleElementContentFocusElement("transition", "transitions");
		//op.clearReferenceFocusElement("transitions");
		//op.focusElementContentFocusedElement("state", "name", "active", "states");
		//op.focusSimpleElementContentOrderFocusElement("state", "states", 2);
		//op.createElementContentFocusElement("state", "name", "joseluis11", "states");
		
		op.getPropertiesFocusElement();
		//op.deleteSimpleElementReferencedInToFocusedElement("transition", "transitions");
		//op.clearReferenceFromFocusedElement("state");
		//op.updateElementAtribute("state", "name", "sielito", "hola");
		
		//op.createElement("command", "name", "comandito", "state", "name", "stadito", "actions");
		op.saveModelInstance();
	}
	
	

}
