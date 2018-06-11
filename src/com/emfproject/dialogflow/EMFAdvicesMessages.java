package com.emfproject.dialogflow;

public class EMFAdvicesMessages {
	
	public static final String START="[ADVICE] Bienvenido a el chatbot para crear maquinas de estado. Si no tiene creado anteriormente ningún modelo, escriba \"crear el modelo\" seguido del nombre que desea darle a su modelo.Si por el contrario ya tenia anteriormente creado un modelo, para cargarlo escriba \"cargar el modelo\" seguido del nombre de su modelo";
	public static final String NEW_MODEL_ADVICE="[ADVICE] Para empezar a crear una maquina de estados, primero deberas crear una en el modelo. Prueba introduciendo \"crear maquina de estados\"";
	public static final String NEW_STATEMACHINE_ADVICE="[ADVICE] Una vez creada su maquina de estados, puede empezar a añadir otros elementos como: estados, comandos, eventos y transiciones.\n para añadir cualquiera de estos elementos debe introducir \" \"añadir\" seguido del elemento que desea crear dentro de la maquina de estados, el nombre que desea darle a este elemento y por ultimo el nombre de la relacion que une al elemento con la maquina de estos. Por ejemplo: \n \"añadir estado prueba dentro de estados \"";
	public static final String NEW_ELEMENT_ORPHAN="[ADVICE] Recuerde que para validar el modelo todos los elementos que cree deben estar asociados de alguna manera a el elemento maquina de estados, o no sera valido";
	

	
	public static final String MODEL_CREATED="[OUTPUT] el modelo se ha creado correctamente";
	public static final String MODEL_LOADED="[OUTPUT] el modelo se ha cargado correctamente";
	public static final String ELEMENT_CREATED="[OUTPUT] el elemento se ha creado correctamente";
	
	
	public static final String NO_TEXT="[ERROR] No ha introducido ningun texto";
	
	


}
