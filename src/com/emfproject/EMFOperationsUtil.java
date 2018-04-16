package com.emfproject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

public class EMFOperationsUtil {

	public static String getMetaModelFilePath(final File folder) {
		String metaModelFilePath = "";
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				getMetaModelFilePath(fileEntry);
			} else {
				if (fileEntry.getName().contains(".ecore")) {
					metaModelFilePath = fileEntry.getPath();

				}

			}
		}
		return metaModelFilePath;
	}

	public static EPackage getMetaModelPackage() {

		ResourceSet rs = new ResourceSetImpl();
		rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore", new XMIResourceFactoryImpl());
		Resource res = rs.createResource(URI.createFileURI(getMetaModelFilePath(new File("./model"))));
		try {
			res.load(null);
		} catch (IOException e) {

			e.printStackTrace();
		}
		EPackage metapackage = (EPackage) res.getContents().get(0);

		return metapackage;

	}

	public static String normalizedString(String word) 
	{
		String wordNormalized = word.substring(0, 1).toUpperCase() + word.substring(1);
		return wordNormalized;
		
	}
	public static Object castAtributeValue(String classParameterName, Object atributeValue) {

		if (classParameterName.equals("int")) {
			atributeValue = Integer.valueOf((String) atributeValue);
		} else if (classParameterName.equals("String")) {
			atributeValue = String.valueOf((String) atributeValue);
		}

		return atributeValue;

	}

	public static boolean checkDataTypeInsertion(Object dataToInsert, String dataType) {
		// TODO añadir todos los tipos de EDataType disponibles
		try {
			if (dataType.equals("EString")) {
				dataToInsert = (String) dataToInsert;
			} else if (dataType.equals("EInt")) {
				dataToInsert = Integer.valueOf((String) dataToInsert);

			}
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}

		return true;

	}

	// TODO tengo qye hacer un chequeador de Referencias, este solo sirve para
	// atributos
	public static boolean checkElementInsertion(String nameElement) {
		for (int i = 0; i < getMetaModelPackage().eContents().size(); i++) {

			if (getMetaModelPackage().eContents().get(i).getClass().getSimpleName().equals("EClassImpl")) {
				EClass myEclass = (EClass) getMetaModelPackage().eContents().get(i);

				if (myEclass.getName().toLowerCase().equals(nameElement.toLowerCase())) {
					System.out.println(EMFOperationsMessages.ELEMENT_EXITS_METAMODEL);
					return true;
					
				}
			}
		}
		return false;
					
	}
	public static boolean checkElementInsertion(String nameElement, String atributeName, Object atributeValue) {

		for (int i = 0; i < getMetaModelPackage().eContents().size(); i++) {

			if (getMetaModelPackage().eContents().get(i).getClass().getSimpleName().equals("EClassImpl")) {
				EClass myEclass = (EClass) getMetaModelPackage().eContents().get(i);

				if (myEclass.getName().toLowerCase().equals(nameElement.toLowerCase())) {
					System.out.println(EMFOperationsMessages.ELEMENT_EXITS_METAMODEL);
					for (int y = 0; y < myEclass.getEAllAttributes().size(); y++) {
						//System.out.println(myEclass.getEAllAttributes().get(y).getName());
						//TODO aqui puedo obtener informacion de las referencias que tiene el elemento en el metamodelo
						//System.out.println(myEclass.getEAllReferences());

						if (myEclass.getEAllAttributes().get(y).getName().equals(atributeName)) {
							System.out.println(EMFOperationsMessages.ELEMENT_ATRIBUTE_EXITS);

							EAttribute atribute = myEclass.getEAllAttributes().get(y);

							//System.out.println(atribute.getEAttributeType().getName());

							if (checkDataTypeInsertion(atributeValue, atribute.getEAttributeType().getName())) {
								System.out.println(EMFOperationsMessages.ATRIBUTE_VALUE_CORRECTLY);
								return true;
							}
							// TODO de momento solo chequea dos tipos de EDataType

						}
					}

				}

			}

		}
		return false;

	}

	public static boolean checkIfElementExists(String nameElement, String atributeName, Object atributeValue,Resource resource) {

		String nameNormalized = EMFOperationsUtil.normalizedString(nameElement);

		for (int i = 0; i < resource.getContents().size(); i++) {
			//System.out.println(resource.getContents().get(i).toString());
			if (resource.getContents().get(i).toString().contains(atributeName + ": " + atributeValue + ")")
					|| resource.getContents().get(i).toString().contains(atributeName + ": " + atributeValue + ",")
							&& resource.getContents().get(i).toString().contains(nameNormalized))

			{
				return true;
			}
		}
		return false;

	}
	public static boolean checkIfElementExists(String nameElement,Resource resource) {

		String nameNormalized = EMFOperationsUtil.normalizedString(nameElement);

		for (int i = 0; i < resource.getContents().size(); i++) {
			//System.out.println(resource.getContents().get(i).toString());
			if (resource.getContents().get(i).toString().contains(nameNormalized))

			{
				return true;
			}
		}
		return false;

	}
	
	public static  Object getElementFromResource(String nameElement, String atributeName, Object atributeValue,Resource resource) {

		String nameNormalized = EMFOperationsUtil.normalizedString(nameElement);

		for (int i = 0; i < resource.getContents().size(); i++) {
			//System.out.println(resource.getContents().get(i).toString());
			if (resource.getContents().get(i).toString().contains(atributeName + ": " + atributeValue + ")")
					|| resource.getContents().get(i).toString().contains(atributeName + ": " + atributeValue + ",")
							&& resource.getContents().get(i).toString().contains(nameNormalized))

			{
				return resource.getContents().get(i);
			}
		}
		return null;

	}
	public static ArrayList getPackageClasses(String packageName) {
		ArrayList<String> classesName = new ArrayList<String>();
		List<Class<?>> classes = ClassFinder.find(packageName);

		for (int i = 0; i < classes.size(); i++) {
			// System.out.println(classes.get(i).getSimpleName());
			classesName.add(classes.get(i).getSimpleName());
		}

		return classesName;

	}

}
