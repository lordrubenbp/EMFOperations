package com.emfproject;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

public class TestNew {
	
	public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException 
	{
		EMFOperationsNew op= new EMFOperationsNew();
		
//		
//
////		//crear el modelo statemachine_test
////		op.createModelInstance("maquina/statemachine_test.xmi");
////		// crear maquina de estados
////		op.createSimpleElement("statemachine");
////		// seleccionar maquina de estados
////		op.focusSimpleElementOrder("statemachine",1);
////		
////		// crear el estado idle en estados
////		op.createElementContentFocusElement("state", "name", "idle", "states");
////		op.createElementContentFocusElement("state", "name", "active", "states");
////		op.createElementContentFocusElement("state", "name", "waitingForLight", "states");
////		op.createElementContentFocusElement("state", "name", "waitingForDraw", "states");
////		op.createElementContentFocusElement("state", "name", "unlockedPanel", "states");
////		
////		// crear el comando unlockPanel en comandos
////		op.createElementContentFocusElement("command", "name", "unlockPanel", "commands");
////		op.createElementContentFocusElement("command", "name", "lockPanel", "commands");
////		op.createElementContentFocusElement("command", "name", "lockDoor", "commands");
////		op.createElementContentFocusElement("command", "name", "unlockDoor", "commands");
////		
////		//crear el evento doorClosed en eventos
////		op.createElementContentFocusElement("event", "name", "doorClosed", "events");
////		op.createElementContentFocusElement("event", "name", "drawOpened", "events");
////		op.createElementContentFocusElement("event", "name", "panelClosed", "events");
////		op.createElementContentFocusElement("event", "name", "lightOn", "events");
////		op.createElementContentFocusElement("event", "name", "doorOpened", "events");
////		
////		//seleccionar el estado idle
////		op.focusElement("state", "name", "idle");
////		
////		//crear una transicion en transiciones
////		op.createSimpleElementContentFocusElement("transition", "transitions");
////		
////		//seleccionar la transicion de transiciones
////		op.focusSimpleElementContentOrderFocusElement("transition", "transitions", 1);
////		
////		//añadir en estado, el estado active
////		op.addElementAsReferenceFocusElement("state", "name", "active", "state");
////		
////		//añadir en evento, el evento doorClosed
////		op.addElementAsReferenceFocusElement("event", "name", "doorClosed", "event");
////		
////	
////		//seleccionar el estado active
////		op.focusElement("state", "name", "active");
////		
////		//crear una transicion en transiciones
////		op.createSimpleElementContentFocusElement("transition", "transitions");
////		//crear una transicion en transiciones
////		op.createSimpleElementContentFocusElement("transition", "transitions");
////		
////		
////		//seleccionar la primera transiciones de transiciones
////		op.focusSimpleElementContentOrderFocusElement("transition", "transitions", 1);
////		
////		//añadir en estado, el estado waitingForLight
////		op.addElementAsReferenceFocusElement("state", "name", "waitingForLight", "state");
////		//añadir en evento, el evento drawOpened
////		op.addElementAsReferenceFocusElement("event", "name", "drawOpened", "event");
////		
////		//seleccionar el estado active
////		op.focusElement("state", "name", "active");
////		
////		//seleccionar la segunda transicion de transiciones
////		op.focusSimpleElementContentOrderFocusElement("transition", "transitions", 2);
////		op.addElementAsReferenceFocusElement("state", "name", "waitingForDraw", "state");
////		op.addElementAsReferenceFocusElement("event", "name", "lightOn", "event");
////		
////		op.focusElement("state", "name", "active");
////		op.addElementAsReferenceFocusElement("command", "name", "unlockPanel", "actions");
////		op.addElementAsReferenceFocusElement("command", "name", "lockDoor", "actions");
////		op.removeElementAsReferenceFocusElement("command", "name", "unlockPanel", "actions");
//		
		op.loadModelInstance("maquina/statemachine.xmi");
		op.focusElement("state", "name", "active");
		System.out.println(op.getElementNameFromRelation("transitions"));
//		op.focusSimpleElementContentOrderFocusElement("transition", "transitions", 1);
//		op.getPropertiesFocusElement();

//		op.saveModelInstance();
		
		
	}
	
	

}
