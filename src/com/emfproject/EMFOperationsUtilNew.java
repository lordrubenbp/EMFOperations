package com.emfproject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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

public class EMFOperationsUtilNew {

	public static String normalizedString(String word) {
		String wordNormalized = word.substring(0, 1).toUpperCase() + word.substring(1);
		return wordNormalized;

	}

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

	public static boolean checkElementInsertion(String nameElement) {
		for (int i = 0; i < getMetaModelPackage().eContents().size(); i++) {

			if (getMetaModelPackage().eContents().get(i).getClass().getSimpleName().equals("EClassImpl")) {
				EClass myEclass = (EClass) getMetaModelPackage().eContents().get(i);

				if (myEclass.getName().toLowerCase().equals(nameElement.toLowerCase())) {
					// System.out.println(EMFOperationsMessages.ELEMENT_EXITS_METAMODEL);
					return true;

				}
			}
		}
		return false;

	}

	// TODO hacer que cada getElementFromResource me devuelve el numero de objetos
	// encontrados y en caso de ser mas de uno, me permita elegir cual
	public static Object getSimpleElementEmpty(String nameElement, Resource resource) {
		String nameNormalized = EMFOperationsUtil.normalizedString(nameElement) + "Impl";
		// System.out.println(nameNormalized);

		TreeIterator<EObject> i = resource.getAllContents();

		while (i.hasNext()) {
			EObject o = i.next();

			if (o.toString().contains(nameNormalized))

			{

				// comprueba que los valores de los atributos son nulos, asi devolvera solo un
				// elemento vacio
				boolean isNull = true;
				EList<EAttribute> eAllAttributes = o.eClass().getEAllAttributes();
				for (EAttribute eAttribute : eAllAttributes) {

					// System.out.println(eAttribute.getName()+": "+o.eGet(eAttribute));

					if (o.eGet(eAttribute) == null) {
						isNull = true;
					} else {
						isNull = false;
					}

				}

				if (o.eContents().size() == 0 && o.eCrossReferences().size() == 0 && isNull) {
					// if ( isNull) {

					return o;
				}

			}
			// System.out.println(o);
		}
		return null;
	}

	// CHEQUEADO
	public static Object getSimpleElementOrder(String nameElement, Resource resource, int order) {

		String nameNormalized = EMFOperationsUtil.normalizedString(nameElement) + "Impl";
		// System.out.println(nameNormalized);

		TreeIterator<EObject> allEObjects = resource.getAllContents();
		ArrayList<EObject> allEObjectsWithName = new ArrayList<EObject>();

		while (allEObjects.hasNext()) {

			EObject o = allEObjects.next();
			if (o.toString().contains(nameNormalized))

			{

				// boolean isNull = true;
				// EList<EAttribute> eAllAttributes = o.eClass().getEAllAttributes();
				// for (EAttribute eAttribute : eAllAttributes) {
				//
				// if (o.eGet(eAttribute) == null) {
				// isNull = true;
				// } else {
				// isNull = false;
				// }
				//
				// }
				//
				// if (isNull) {

				allEObjectsWithName.add(o);

				// return o;
				// }

			}
			// System.out.println(o);
		}

		if (allEObjectsWithName.size() > 0) {
			return allEObjectsWithName.get(order - 1);
		}
		return null;

	}

	public static Object getElementInAllModel(String nameElement, String atributeName, Object atributeValue,
			Resource resource) {

		String nameNormalized = EMFOperationsUtil.normalizedString(nameElement);

		TreeIterator<EObject> allEObjects = resource.getAllContents();
		ArrayList<EObject> allEObjectsWithName = new ArrayList<EObject>();

		while (allEObjects.hasNext()) {

			EObject o = allEObjects.next();

			if (o.eClass().getName().equals(nameNormalized)) {

				EList<EAttribute> eAllAttributes = o.eClass().getEAllAttributes();

				for (EAttribute eAttribute : eAllAttributes) {

					if (eAttribute.getName().equals(atributeName) && o.eGet(eAttribute) != null
							&& o.eGet(eAttribute).equals(atributeValue)) {

						allEObjectsWithName.add(o);

					}
				}
			}

		}
		if (allEObjectsWithName.size() == 1) {
			return allEObjectsWithName.get(0);
		} else if (allEObjectsWithName.size() > 1) {
			
			EMFOperationsMessages.printMessage("ELEMENT_REPETED");
			
		}
		return null;

	}

	public static Object getElement(String nameElement, String atributeName, Object atributeValue, Resource resource) {

		String nameNormalized = EMFOperationsUtil.normalizedString(nameElement);

		TreeIterator<EObject> allEObjects = resource.getAllContents();
		ArrayList<EObject> allEObjectsWithName = new ArrayList<EObject>();

		while (allEObjects.hasNext()) {

			EObject o = allEObjects.next();
			if (o.eClass().getName().equals(nameNormalized)) {

				EList<EAttribute> eAllAttributes = o.eClass().getEAllAttributes();

				for (EAttribute eAttribute : eAllAttributes) {

					if (eAttribute.getName().equals(atributeName) && o.eGet(eAttribute) != null
							&& o.eGet(eAttribute).equals(atributeValue)) {

						allEObjectsWithName.add(o);

					}
				}
			}

		}
		if (allEObjectsWithName.size() == 1) {
			return allEObjectsWithName.get(0);
		} else if (allEObjectsWithName.size() > 1) {
			
			EMFOperationsMessages.printMessage("ELEMENT_REPETED");
		}
		return null;

	}

	// public static Object getElementReferencedSimpleElement(String parentElement,
	// String childElement, String childAtributeName,
	// Object childAtributeValue, String relationName, Resource resource) {
	//
	// String childNameNormalized =
	// EMFOperationsUtil.normalizedString(childElement);
	// String parentNameNormalized =
	// EMFOperationsUtil.normalizedString(parentElement);
	// TreeIterator<EObject> i = resource.getAllContents();
	//
	// while (i.hasNext()) {
	// EObject eObjectParent = i.next();
	//
	// if (eObjectParent.eClass().getName().equals(parentNameNormalized)) {
	// System.out.println("bep padre");
	//
	// EList<EReference> eAllReferences =
	// eObjectParent.eClass().getEAllReferences();
	//
	// boolean referenceExists = false;
	// for (EReference eReference : eAllReferences) {
	//
	// if (eReference.getName().equals(relationName)) {
	// System.out.println("bep relacion");
	//
	// referenceExists = true;
	// EList<EObject> h = eObjectParent.eCrossReferences();
	//
	// for(EObject obj:h)
	// {
	// System.out.println(obj.eClass().getName());
	// if (obj.eClass().getName().equals(childNameNormalized)) {
	// System.out.println("bep hijo");
	//
	//
	// EList<EAttribute> eAllAttributes = obj.eClass().getEAllAttributes();
	//
	// for (EAttribute eAttribute : eAllAttributes) {
	//
	// if (eAttribute.getName().equals(childAtributeName) && obj.eGet(eAttribute) !=
	// null
	// && obj.eGet(eAttribute).equals(childAtributeValue)) {
	//
	// System.out.println("bep atributo");
	//
	//
	// return obj;
	//
	// }
	// }
	// }
	// }
	//
	// }
	//
	// }
	// if (!referenceExists) {
	// EMFOperationsMessages.printMessage("REFERENCE_NOT_EXISTS");
	// }
	// }
	//
	// }
	// return null;
	//
	// }
	// public static Object getElementContentSimpleElement(String parentElement,
	// String childElement, String childAtributeName,
	// Object childAtributeValue, String relationName, Resource resource) {
	//
	// String childNameNormalized =
	// EMFOperationsUtil.normalizedString(childElement);
	// String parentNameNormalized =
	// EMFOperationsUtil.normalizedString(parentElement);
	// TreeIterator<EObject> i = resource.getAllContents();
	//
	// while (i.hasNext()) {
	// EObject eObjectParent = i.next();
	//
	// if (eObjectParent.eClass().getName().equals(parentNameNormalized)) {
	// System.out.println("bep padre");
	//
	// EList<EReference> eAllReferences =
	// eObjectParent.eClass().getEAllReferences();
	//
	// boolean referenceExists = false;
	// for (EReference eReference : eAllReferences) {
	//
	// if (eReference.getName().equals(relationName)) {
	// System.out.println("bep relacion");
	//
	// referenceExists = true;
	// TreeIterator<EObject> y = eObjectParent.eAllContents();
	// EList<EObject> h = eObjectParent.eCrossReferences();
	//
	//
	// while (y.hasNext()) {
	//
	// EObject o = y.next();
	//
	//
	// if (o.eClass().getName().equals(childNameNormalized)) {
	// System.out.println("bep hijo");
	//
	//
	// EList<EAttribute> eAllAttributes = o.eClass().getEAllAttributes();
	//
	// for (EAttribute eAttribute : eAllAttributes) {
	//
	// if (eAttribute.getName().equals(childAtributeName) && o.eGet(eAttribute) !=
	// null
	// && o.eGet(eAttribute).equals(childAtributeValue)) {
	//
	// System.out.println("bep atributo");
	//
	//
	// return o;
	//
	// }
	// }
	// }
	//
	// }
	//
	//
	// }
	//
	// }
	// if (!referenceExists) {
	// EMFOperationsMessages.printMessage("REFERENCE_NOT_EXISTS");
	// }
	// }
	//
	// }
	// return null;
	//
	// }
	//
	// public static Object getSimpleElementContentElement(String childElement,
	// String parentElement, String parentAtributeName,
	// Object parentAtributeValue, String relationName, Resource resource) {
	//
	// String childNameNormalized =
	// EMFOperationsUtil.normalizedString(childElement);
	// String parentNameNormalized =
	// EMFOperationsUtil.normalizedString(parentElement);
	// TreeIterator<EObject> i = resource.getAllContents();
	//
	// while (i.hasNext()) {
	// EObject eObjectParent = i.next();
	//
	// if (eObjectParent.eClass().getName().equals(parentNameNormalized)) {
	// System.out.println("bep padre");
	//
	// EList<EReference> eAllReferences =
	// eObjectParent.eClass().getEAllReferences();
	//
	// boolean referenceExists = false;
	// for (EReference eReference : eAllReferences) {
	//
	// if (eReference.getName().equals(relationName)) {
	// System.out.println("bep relacion");
	//
	// referenceExists = true;
	//
	// EList<EAttribute> eAllAttributes =
	// eObjectParent.eClass().getEAllAttributes();
	//
	// for (EAttribute eAttribute : eAllAttributes) {
	//
	// if (eAttribute.getName().equals(parentAtributeName) &&
	// eObjectParent.eGet(eAttribute) != null
	// && eObjectParent.eGet(eAttribute).equals(parentAtributeValue)) {
	//
	// System.out.println("bep atributo");
	//
	// TreeIterator<EObject> y = eObjectParent.eAllContents();
	//
	// while (y.hasNext()) {
	//
	// EObject o = y.next();
	//
	// System.out.println(o.eClass().getName());
	// if (o.eClass().getName().equals(childNameNormalized)) {
	// System.out.println("bep hijo");
	// return o;
	//
	// }
	//
	// }
	//
	// }
	// }
	//
	//
	// }
	//
	// }
	// if (!referenceExists) {
	// EMFOperationsMessages.printMessage("REFERENCE_NOT_EXISTS");
	// }
	// }
	//
	// }
	// return null;
	//
	// }
	// public static Object getSimpleElementReferencedElement(String childElement,
	// String parentElement, String parentAtributeName,
	// Object parentAtributeValue, String relationName, Resource resource) {
	//
	// String childNameNormalized =
	// EMFOperationsUtil.normalizedString(childElement);
	// String parentNameNormalized =
	// EMFOperationsUtil.normalizedString(parentElement);
	// TreeIterator<EObject> i = resource.getAllContents();
	//
	// while (i.hasNext()) {
	// EObject eObjectParent = i.next();
	//
	// if (eObjectParent.eClass().getName().equals(parentNameNormalized)) {
	// System.out.println("bep padre");
	//
	// EList<EReference> eAllReferences =
	// eObjectParent.eClass().getEAllReferences();
	//
	// boolean referenceExists = false;
	// for (EReference eReference : eAllReferences) {
	//
	// if (eReference.getName().equals(relationName)) {
	// System.out.println("bep relacion");
	//
	// referenceExists = true;
	//
	// EList<EAttribute> eAllAttributes =
	// eObjectParent.eClass().getEAllAttributes();
	//
	// for (EAttribute eAttribute : eAllAttributes) {
	//
	// if (eAttribute.getName().equals(parentAtributeName) &&
	// eObjectParent.eGet(eAttribute) != null
	// && eObjectParent.eGet(eAttribute).equals(parentAtributeValue)) {
	//
	// System.out.println("bep atributo");
	//
	// EList<EObject> h = eObjectParent.eCrossReferences();
	//
	// for(EObject obj:h)
	// {
	// System.out.println(obj.eClass().getName());
	// if (obj.eClass().getName().equals(childNameNormalized)) {
	// System.out.println("bep hijo");
	//
	// return obj;
	//
	// }
	// }
	//
	// }
	// }
	//
	//
	// }
	//
	// }
	// if (!referenceExists) {
	// EMFOperationsMessages.printMessage("REFERENCE_NOT_EXISTS");
	// }
	// }
	//
	// }
	// return null;
	//
	// }
	//

	// TEST
	public static Object getElementContentOrderFocusedElement(String nameElement, String atributeName,
			Object atributeValue, EObject focusObject, String relationName, int order) {
		ArrayList<EObject> allMatchElements = new ArrayList<EObject>();

		String nameNormalized = EMFOperationsUtil.normalizedString(nameElement);

		EList<EReference> eAllReferences = focusObject.eClass().getEAllReferences();

		boolean referenceExists = false;
		for (EReference eReference : eAllReferences) {

			if (eReference.getName().equals(relationName)) {

				referenceExists = true;

				TreeIterator<EObject> y = focusObject.eAllContents();

				while (y.hasNext()) {

					EObject o = y.next();

					EList<EAttribute> eAllAttributes = o.eClass().getEAllAttributes();

					//System.out.println(o.eClass().getName());
					if (o.eClass().getName().equals(nameNormalized)) {

						for (EAttribute eAttribute : eAllAttributes) {

							if (eAttribute.getName().equals(atributeName) && o.eGet(eAttribute) != null
									&& o.eGet(eAttribute).equals(atributeValue)) {

								allMatchElements.add(o);
							}

						}

					}

				}

			}

		}
		if (!referenceExists) {
			EMFOperationsMessages.printMessage("REFERENCE_NOT_EXISTS");
		}
		if (allMatchElements.size() > 0) {
			return allMatchElements.get(order - 1);
		}
		return null;

	}

	public static Object getSimpleElementContentOrderFocusedElement(String nameElement, EObject focusObject,
			String relationName, int order) {
		ArrayList<EObject> allMatchElements = new ArrayList<EObject>();

		String nameNormalized = EMFOperationsUtil.normalizedString(nameElement);

		EList<EReference> eAllReferences = focusObject.eClass().getEAllReferences();

		boolean referenceExists = false;
		for (EReference eReference : eAllReferences) {

			if (eReference.getName().equals(relationName)) {

				referenceExists = true;

				TreeIterator<EObject> y = focusObject.eAllContents();

				while (y.hasNext()) {

					EObject o = y.next();

					// System.out.println(o.eClass().getName());
					if (o.eClass().getName().equals(nameNormalized)) {

						allMatchElements.add(o);

					}

				}

			}

		}
		if (!referenceExists) {
			EMFOperationsMessages.printMessage("REFERENCE_NOT_EXISTS");
		}
		if (allMatchElements.size() > 0) {
			return allMatchElements.get(order - 1);
		}
		return null;

	}

	// NUEVO
	public static Object getSimpleElementContentFocusedElement(String nameElement, EObject focusObject,
			String relationName) {

		String nameNormalized = EMFOperationsUtil.normalizedString(nameElement);

		EList<EReference> eAllReferences = focusObject.eClass().getEAllReferences();

		boolean referenceExists = false;
		for (EReference eReference : eAllReferences) {

			if (eReference.getName().equals(relationName)) {
				// System.out.println("bep relacion");

				referenceExists = true;

				// System.out.println("bep atributo");

				TreeIterator<EObject> y = focusObject.eAllContents();

				while (y.hasNext()) {

					EObject o = y.next();

					//System.out.println(o.eClass().getName());
					if (o.eClass().getName().equals(nameNormalized)) {
						// System.out.println("bep hijo");
						return o;

					}

				}

			}

		}
		if (!referenceExists) {
			EMFOperationsMessages.printMessage("REFERENCE_NOT_EXISTS");
		}

		return null;

	}

	public static void getParentsObject(EObject o) {
		// System.out.println("aquiii"+ o.eContainer());
		boolean start = true;
		EObject object = o;
		String spaces = " ";
		System.out.println("[element]: " + o.eClass().getName());
		while (start) {
			if (o.eContainer() != null) {
				System.out.println(spaces + "[container]: " + o.eContainer().eClass().getName());
				o = o.eContainer();
				spaces = spaces + " ";
			} else {
				start = false;
			}
		}
		System.out.println("\n\n");
	}

	public static Object getElementContentFocusedElement(String nameElement, String atributeName, String atributeValue,
			EObject focusObject, String relationName) {

		String nameNormalized = EMFOperationsUtil.normalizedString(nameElement);

		EList<EReference> eAllReferences = focusObject.eClass().getEAllReferences();
		ArrayList<EObject> allMatchObjects = new ArrayList<EObject>();

		boolean referenceExists = false;
		for (EReference eReference : eAllReferences) {

			if (eReference.getName().equals(relationName)) {

				referenceExists = true;

				TreeIterator<EObject> y = focusObject.eAllContents();

				while (y.hasNext()) {

					EObject o = y.next();

					if (o.eClass().getName().equals(nameNormalized)) {

						EList<EAttribute> eAllAttributes = o.eClass().getEAllAttributes();

						for (EAttribute eAttribute : eAllAttributes) {

							if (eAttribute.getName().equals(atributeName) && o.eGet(eAttribute) != null
									&& o.eGet(eAttribute).equals(atributeValue)) {

								allMatchObjects.add(o);

							}
						}

					}

				}

			}

		}
		if (!referenceExists) {
			EMFOperationsMessages.printMessage("REFERENCE_NOT_EXISTS");
		}

		if (allMatchObjects.size() == 1) {
			return allMatchObjects.get(0);
		} else if (allMatchObjects.size() > 1) {
			EMFOperationsMessages.printMessage("ELEMENT_REPETED");
		}
		return null;

	}

	public static String getRootElement() {
		ArrayList<String> elementosName = new ArrayList<String>();
		ArrayList<EClass> elementosEClass = new ArrayList<EClass>();

		for (int i = 0; i < getMetaModelPackage().eContents().size(); i++) {

			if (getMetaModelPackage().eContents().get(i).getClass().getSimpleName().equals("EClassImpl")) {
				EClass myEclass = (EClass) getMetaModelPackage().eContents().get(i);

				elementosEClass.add(myEclass);
				elementosName.add(myEclass.getName());

			}

		}
		for (EClass myEclass : elementosEClass) {
			for (int d = 0; d < myEclass.getEAllContainments().size(); d++) {

				EReference reference = myEclass.getEAllContainments().get(d);

				if (elementosName.contains(reference.getEType().getName())) {
					elementosName.remove(reference.getEType().getName());
				}
//				System.out.println(
//						myEclass.getName() + ": " + reference.getName() + ": " + reference.getEType().getName());

			}
		}
		return elementosName.get(0);
	}

	public static Object getElementReferenceFocusedElement(String nameElement, String atributeName,
			Object atributeValue, EObject focusObject, String relationName) {

		String nameNormalized = EMFOperationsUtil.normalizedString(nameElement);

		EList<EReference> eAllReferences = focusObject.eClass().getEAllReferences();

		boolean referenceExists = false;
		for (EReference eReference : eAllReferences) {

			if (eReference.getName().equals(relationName)) {
				// System.out.println("bep relacion");

				referenceExists = true;

				// System.out.println("bep atributo");

				EList<EObject> y = focusObject.eCrossReferences();

				for (EObject o : y) {

					if (o.eClass().getName().equals(nameNormalized)) {

						EList<EAttribute> eAllAttributes = o.eClass().getEAllAttributes();

						for (EAttribute eAttribute : eAllAttributes) {

							if (eAttribute.getName().equals(atributeName) && o.eGet(eAttribute) != null
									&& o.eGet(eAttribute).equals(atributeValue)) {

								return o;

							}
						}

					}

				}

			}

		}
		if (!referenceExists) {
			EMFOperationsMessages.printMessage("REFERENCE_NOT_EXISTS");
		}

		return null;

	}
}
