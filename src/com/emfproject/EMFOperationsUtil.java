package com.emfproject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
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

	public static String normalizedString(String word) {
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

	// TODO tengo que hacer un chequeador de Referencias, este solo sirve para
	// atributos, debe de comprobar las cardinalidades de la relacion

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
						// System.out.println(myEclass.getEAllAttributes().get(y).getName());
						// TODO aqui puedo obtener informacion de las referencias que tiene el elemento
						// en el metamodelo
						// System.out.println(myEclass.getEAllReferences());

						if (myEclass.getEAllAttributes().get(y).getName().equals(atributeName)) {
							System.out.println(EMFOperationsMessages.ELEMENT_ATRIBUTE_EXITS);

							EAttribute atribute = myEclass.getEAllAttributes().get(y);

							// System.out.println(atribute.getEAttributeType().getName());

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

	public static Object getElementFromResource(String nameElement, String atributeName, Object atributeValue,
			Resource resource) {

		String nameNormalized = EMFOperationsUtil.normalizedString(nameElement) + "Impl";
		// System.out.println(nameNormalized);

		TreeIterator<EObject> i = resource.getAllContents();

		while (i.hasNext()) {
			EObject o = i.next();

			if (o.toString().contains(atributeName + ": " + atributeValue + ")")
					|| o.toString().contains(atributeName + ": " + atributeValue + ",")
					|| o.toString().contains("(" + atributeName + ": " + atributeValue + ",")
							&& o.toString().contains(nameNormalized))

			{
				return o;
			}
			// System.out.println(o);
		}
		return null;

	}

	public static Object getElementFromResource(String nameElement, Resource resource) {

		String nameNormalized = EMFOperationsUtil.normalizedString(nameElement) + "Impl";
		// System.out.println(nameNormalized);

		TreeIterator<EObject> i = resource.getAllContents();

		while (i.hasNext()) {
			EObject o = i.next();

			if (o.toString().contains(nameNormalized))

			{
				return o;
			}
			// System.out.println(o);
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

	public static void showAllModelData(EList<EObject> list) {

		for (int i = 0; i < list.size(); i++) {

			System.out.println(list.get(i).toString());
			// System.out.println(list.get(i).eContents().size());
			if (list.get(i).eContents().size() > 0) {
				showAllModelData(list.get(i).eContents());
			}

		}
	}

	public static int getLowerBound(String nameElement, String referenceName) {
		nameElement=EMFOperationsUtil.normalizedString(nameElement);
		for (int i = 0; i < getMetaModelPackage().eContents().size(); i++) {

			if (getMetaModelPackage().eContents().get(i).getClass().getSimpleName().equals("EClassImpl")) {
				EClass myEclass = (EClass) getMetaModelPackage().eContents().get(i);

				//System.out.println(myEclass.getName());
				
				if (myEclass.getName().equals(nameElement)) {

					for (int d = 0; d < myEclass.getEAllReferences().size(); d++) {
						// System.out.println(myEclass.getEAllAttributes().get(y).getName());
						// TODO aqui puedo obtener informacion de las referencias que tiene el elemento
						// en el metamodelo
						// System.out.println(myEclass.getEAllReferences());

						EReference reference = myEclass.getEAllReferences().get(d);

						//System.out.println("Reference name: " + reference.getName());
						//System.out.println("Min elements: " + reference.getLowerBound());
						//System.out.println("Max elements: " + reference.getUpperBound());

						if (reference.getName().equals(referenceName)) {
							return reference.getLowerBound();
						}

					}
				}

			}

		}
		return -99;
	}

	public static int getUpperBound(String nameElement, String referenceName) {
		nameElement=EMFOperationsUtil.normalizedString(nameElement);
		for (int i = 0; i < getMetaModelPackage().eContents().size(); i++) {

			if (getMetaModelPackage().eContents().get(i).getClass().getSimpleName().equals("EClassImpl")) {
				EClass myEclass = (EClass) getMetaModelPackage().eContents().get(i);

				//System.out.println(myEclass.getName());
				
				if (myEclass.getName().equals(nameElement)) {

					for (int d = 0; d < myEclass.getEAllReferences().size(); d++) {
						// System.out.println(myEclass.getEAllAttributes().get(y).getName());
						// TODO aqui puedo obtener informacion de las referencias que tiene el elemento
						// en el metamodelo
						// System.out.println(myEclass.getEAllReferences());

						EReference reference = myEclass.getEAllReferences().get(d);

						//System.out.println("Reference name: " + reference.getName());
						//System.out.println("Min elements: " + reference.getLowerBound());
						//System.out.println("Max elements: " + reference.getUpperBound());

						if (reference.getName().equals(referenceName)) {
							return reference.getUpperBound();
						}

					}
				}

			}

		}
		return -99;
	}
	public static void showAllMetaModelData() {

		for (int i = 0; i < getMetaModelPackage().eContents().size(); i++) {

			if (getMetaModelPackage().eContents().get(i).getClass().getSimpleName().equals("EClassImpl")) {
				EClass myEclass = (EClass) getMetaModelPackage().eContents().get(i);

				System.out.println(myEclass.getName());
				for (int y = 0; y < myEclass.getEAllAttributes().size(); y++) {
					// System.out.println(myEclass.getEAllAttributes().get(y).getName());
					// TODO aqui puedo obtener informacion de las referencias que tiene el elemento
					// en el metamodelo
					// System.out.println(myEclass.getEAllReferences());

					EAttribute atribute = myEclass.getEAllAttributes().get(y);

					// System.out.println(atribute.getEAttributeType().getName());

				}
				for (int d = 0; d < myEclass.getEAllReferences().size(); d++) {
					// System.out.println(myEclass.getEAllAttributes().get(y).getName());
					// TODO aqui puedo obtener informacion de las referencias que tiene el elemento
					// en el metamodelo
					// System.out.println(myEclass.getEAllReferences());

					EReference reference = myEclass.getEAllReferences().get(d);

					System.out.println("Reference name: " + reference.getName());
					System.out.println("Min elements: " + reference.getLowerBound());
					System.out.println("Max elements: " + reference.getUpperBound());

				}

			}

		}

	}

}
