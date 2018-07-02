package com.emfproject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

public class EMFOperationsNew {

	ResourceSet inst_resourceset;
	Resource inst_resource;
	Object factory;
	String metaModelURI;
	Object focusedElement = null;
	String rootNodeName = null;

	public EMFOperationsNew() throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {

		String packageNameNormalized = EMFOperationsUtil.getMetaModelPackage().getName().substring(0, 1).toUpperCase()
				+ EMFOperationsUtil.getMetaModelPackage().getName().substring(1);
		Class<?> projectFactoryClass = Class.forName(
				EMFOperationsUtil.getMetaModelPackage().getName() + ".impl." + packageNameNormalized + "FactoryImpl");
		factory = projectFactoryClass.getMethod("init").invoke(null);

		Class<?> projectPackageClass = Class.forName(
				EMFOperationsUtil.getMetaModelPackage().getName() + ".impl." + packageNameNormalized + "PackageImpl");
		Object o = projectPackageClass.getMethod("init").invoke(null);

		projectPackageClass.getMethod("eClass").invoke(o);

		// EMFProjectPackage.eINSTANCE.eClass()
		// factory = EMFProjectFactory.eINSTANCE;

	}

	// MODEL FUNCTIONS

	public void saveModelInstance() {

		try {
			inst_resource.save(Collections.EMPTY_MAP);
			EMFOperationsMessages.printMessage("MODEL_INSTANCE_SAVED");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public boolean createModelInstance(String path) {

		try {

			File pathFile = new File(path);

			if (pathFile.exists()) {
				EMFOperationsMessages.printMessage("MODEL_ALREADY_EXITS");
				return false;
			}
			inst_resourceset = new ResourceSetImpl();

			Map<String, Object> extensionFactoryMap = Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap();

			extensionFactoryMap.put("xmi", new XMIResourceFactoryImpl());

			inst_resource = (Resource) inst_resourceset.createResource(URI.createFileURI(path));
			EMFOperationsMessages.printMessage("MODEL_CREATED");

			return true;
		} catch (Exception e) {
			EMFOperationsMessages.printMessage("MODEL_NOT_CREATED");

			inst_resource = null;
			return false;
		}
	}

	public boolean loadModelInstance(String path) {

		inst_resourceset = new ResourceSetImpl();

		Map<String, Object> extensionFactoryMap = Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap();

		extensionFactoryMap.put("xmi", new XMIResourceFactoryImpl());

		inst_resource = (Resource) inst_resourceset.createResource(URI.createFileURI(path));

		try {

			inst_resource.load(null);
			EMFOperationsMessages.printMessage("MODEL_LOADED");

			return true;
		} catch (IOException e) {
			EMFOperationsMessages.printMessage("MODEL_NOT_EXITS");

			inst_resource = null;
			return false;
		}

	}

	// CREATE FUNCTIONS
	// CHEQUEADA
	public EObject createSimpleElement(String nameElement) {

		String packageNameNormalized = EMFOperationsUtilNew
				.normalizedString(EMFOperationsUtilNew.getMetaModelPackage().getName());
		String nameNormalized = EMFOperationsUtilNew.normalizedString(nameElement);
		EObject eObj = null;

		if (EMFOperationsUtilNew.checkElementInsertion(nameElement)) {

			try {

				Class<?> pfcl = Class.forName(
						EMFOperationsUtilNew.getMetaModelPackage().getName() + "." + packageNameNormalized + "Factory");
				eObj = (EObject) pfcl.getMethod("create" + nameNormalized).invoke(factory);
				inst_resource.getContents().add(eObj);
				EMFOperationsMessages.printMessage("NEW_ELEMENT_ADDED_CORRECTLY");

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			EMFOperationsMessages.printMessage("NEW_ELEMENT_INCORRECT");

		}

		return eObj;

	}

	// CHEQUEADA
	public EObject createElement(String nameElement, String atributeName, Object atributeValue)
			throws IllegalAccessException, ClassNotFoundException {

		String nameNormalized = EMFOperationsUtil.normalizedString(nameElement);

		EObject eObject = createSimpleElement(nameElement);

		if (eObject != null) {
			EList<EAttribute> eAllAttributes = eObject.eClass().getEAllAttributes();

			boolean atributeExists = false;
			for (EAttribute eAtribute : eAllAttributes) {
				if (eAtribute.getName().equals(atributeName)) {
					atributeExists = true;

					if (eAtribute.isUnique()) {
						if (EMFOperationsUtilNew.getElement(nameNormalized, atributeName, atributeValue,
								inst_resource) == null) {
							try {
								eObject.eSet(eAtribute, atributeValue);
								EMFOperationsMessages.printMessage("NEW_ATRIBUTE_ADDED_CORRECTLY");
								return eObject;
							} catch (Exception e) {
								EMFOperationsMessages.printMessage("NEW_ATRIBUTE_VALUE_INCORRECT");

							}
						} else {
							// elemento con clave unica ya existe
							EMFOperationsMessages.printMessage("NEW_ELEMENT_ALREADY_EXITS");

						}
					} else {
						try {
							eObject.eSet(eAtribute, atributeValue);
							EMFOperationsMessages.printMessage("NEW_ATRIBUTE_ADDED_CORRECTLY");
							return eObject;
						} catch (Exception e) {
							EMFOperationsMessages.printMessage("NEW_ATRIBUTE_VALUE_INCORRECT");

						}
					}

				}
			}
			if (!atributeExists) {
				EMFOperationsMessages.printMessage("NEW_ATRIBUTE_INCORRECT");

			}
		}

		EcoreUtil.delete(eObject);
		return null;

	}

	// public void createElementInsideSimpleElement(String nameElement, String
	// atributeName, Object atributeValue,
	// String parentNameElement, String relationName) throws IllegalAccessException,
	// ClassNotFoundException {
	// String nameNormalized = EMFOperationsUtil.normalizedString(nameElement);
	// String parentNameNormalized =
	// EMFOperationsUtil.normalizedString(parentNameElement);
	//
	// if (EMFOperationsUtilNew.getElement(nameNormalized, atributeName,
	// atributeValue,
	// inst_resource) == null) {
	//
	// if (EMFOperationsUtilNew.getSimpleElement(parentNameNormalized,
	// inst_resource) != null) {
	//
	// EObject eObjectParent = (EObject)
	// EMFOperationsUtilNew.getSimpleElement(parentNameNormalized,
	// inst_resource);
	// if (eObjectParent != null) {
	// EList<EReference> eAllReferences =
	// eObjectParent.eClass().getEAllReferences();
	//
	// boolean referenceExists = false;
	// for (EReference eReference : eAllReferences) {
	//
	// if (eReference.getName().equals(relationName)) {
	// referenceExists = true;
	//
	// if (eReference.getUpperBound() > 0 || eReference.getUpperBound() == -1) {
	//
	// EObject eObjectChildren = createElement(nameElement, atributeName,
	// atributeValue);
	//
	// if (eObjectChildren != null) {
	// if (eReference.getUpperBound() == 1) {
	//
	// eObjectParent.eSet(eReference, eObjectChildren);
	// } else {
	// EList<EObject> list = (EList) eObjectParent.eGet(eReference);
	// list.add(eObjectChildren);
	// }
	// }
	//
	// }
	//
	// }
	//
	// }
	// if (!referenceExists) {
	// EMFOperationsMessages.printMessage("REFERENCE_NOT_EXISTS");
	// }
	// }
	// } else {
	//
	// EMFOperationsMessages.printMessage("PARENT_NOT_EXIST");
	// }
	// } else {
	// EMFOperationsMessages.printMessage("NEW_ELEMENT_ALREADY_EXITS");
	// }
	//
	// }

	// // estos dos metodos en principio me sobrarian
	// public void createElementInsideElement(String nameElement, String
	// atributeName, Object atributeValue,
	// String parentNameElement, String parentAtributeName, String
	// parentAtributeValue, String relationName)
	// throws IllegalAccessException, ClassNotFoundException {
	//
	// String nameNormalized = EMFOperationsUtil.normalizedString(nameElement);
	// String parentNameNormalized =
	// EMFOperationsUtil.normalizedString(parentNameElement);
	//
	// if (EMFOperationsUtilNew.getElement(parentNameNormalized, parentAtributeName,
	// parentAtributeValue,
	// inst_resource) != null) {
	//
	// if (EMFOperationsUtilNew.getElement(nameNormalized, atributeName,
	// atributeValue,
	// inst_resource) == null) {
	//
	// EObject eObjectParent = (EObject)
	// EMFOperationsUtilNew.getElement(parentNameNormalized,
	// parentAtributeName, parentAtributeValue, inst_resource);
	//
	// if (eObjectParent != null) {
	// EList<EReference> eAllReferences =
	// eObjectParent.eClass().getEAllReferences();
	//
	// boolean referenceExists = false;
	//
	// for (EReference eReference : eAllReferences) {
	// if (eReference.getName().equals(relationName)) {
	// referenceExists = true;
	// if (eReference.getUpperBound() > 0 || eReference.getUpperBound() == -1) {
	//
	// EObject eObjectChildren = createElement(nameElement, atributeName,
	// atributeValue);
	//
	// if (eObjectChildren != null) {
	// if (eReference.getUpperBound() == 1) {
	//
	// eObjectParent.eSet(eReference, eObjectChildren);
	// } else {
	// EList<EObject> list = (EList) eObjectParent.eGet(eReference);
	// list.add(eObjectChildren);
	// }
	// }
	//
	// }
	//
	// }
	//
	// }
	// if (!referenceExists) {
	// EMFOperationsMessages.printMessage("REFERENCE_NOT_EXISTS");
	// }
	// }
	// } else {
	// EMFOperationsMessages.printMessage("NEW_ELEMENT_ALREADY_EXITS");
	// }
	// } else {
	// EMFOperationsMessages.printMessage("PARENT_NOT_EXIST");
	// }
	//
	// }
	//
	// public void createSimpleElementInsideElement(String nameElement, String
	// parentNameElement,
	// String parentAtributeName, String parentAtributeValue, String relationName) {
	//
	// String parentNameNormalized =
	// EMFOperationsUtil.normalizedString(parentNameElement);
	//
	// if (EMFOperationsUtilNew.getElement(parentNameNormalized, parentAtributeName,
	// parentAtributeValue,
	// inst_resource) != null) {
	//
	// EObject eObjectParent = (EObject)
	// EMFOperationsUtilNew.getElement(parentNameNormalized,
	// parentAtributeName, parentAtributeValue, inst_resource);
	// if (eObjectParent != null) {
	// EList<EReference> eAllReferences =
	// eObjectParent.eClass().getEAllReferences();
	// boolean referenceExists = false;
	// for (EReference eReference : eAllReferences) {
	//
	// if (eReference.getName().equals(relationName)) {
	// referenceExists = true;
	//
	// if (eReference.getUpperBound() > 0 || eReference.getUpperBound() == -1) {
	//
	// EObject eObjectChildren = createSimpleElement(nameElement);
	//
	// if (eObjectChildren != null) {
	// if (eReference.getUpperBound() == 1) {
	//
	// eObjectParent.eSet(eReference, eObjectChildren);
	// } else {
	// EList<EObject> list = (EList) eObjectParent.eGet(eReference);
	// list.add(eObjectChildren);
	// }
	// }
	//
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
	// } else {
	// EMFOperationsMessages.printMessage("PARENT_NOT_EXIST");
	// }
	//
	// }
	//
	//

	// DELETE FUNCTIONS

	public void deleteSimpleElement(String nameElement) {
		if (EMFOperationsUtil.getElementFromResource(nameElement, inst_resource) != null) {
			EcoreUtil.delete((EObject) EMFOperationsUtilNew.getSimpleElement(nameElement, inst_resource));
			EMFOperationsMessages.printMessage("ELEMENT_DELETED");
		} else {
			EMFOperationsMessages.printMessage("ELEMENT_TO_DELETE_NOT_EXITS");
		}
	}

	public void deleteElement(String nameElement, String atributeName, Object atributeValue) {

		if (EMFOperationsUtilNew.getElement(nameElement, atributeName, atributeValue, inst_resource) != null) {
			EcoreUtil.delete((EObject) EMFOperationsUtil.getElementFromResource(nameElement, atributeName,
					atributeValue, inst_resource));
			EMFOperationsMessages.printMessage("ELEMENT_DELETED");

		} else {
			EMFOperationsMessages.printMessage("ELEMENT_TO_DELETE_NOT_EXITS");

		}

	}

	// FOCUS FUNCTION

	// public void focusSimpleElementContentElement(String childElement, String
	// parentElement, String parentAtribute, String parentValue,String relationName)
	// {
	//
	// focusedElement =
	// EMFOperationsUtilNew.getSimpleElementContentElement(childElement,
	// parentElement, parentAtribute,
	// parentValue,relationName,inst_resource);
	//
	// if (focusedElement== null) {
	// EMFOperationsMessages.printMessage("ELEMENT_TO_FOCUS_NOT_EXITS");
	//
	// }
	// }
	// public void focusSimpleElementReferenceElement(String childElement, String
	// parentElement, String parentAtribute, String parentValue,String relationName)
	// {
	//
	// focusedElement =
	// EMFOperationsUtilNew.getSimpleElementReferencedElement(childElement,
	// parentElement, parentAtribute,
	// parentValue,relationName,inst_resource);
	//
	// if (focusedElement== null) {
	// EMFOperationsMessages.printMessage("ELEMENT_TO_FOCUS_NOT_EXITS");
	//
	// }
	// }
	// public void focusElementContentSimpleElement(String parentElement, String
	// childElement, String childAtributeName,
	// String childAtributeValue, String relationName) throws
	// IllegalAccessException, IllegalArgumentException,
	// InvocationTargetException, NoSuchMethodException, SecurityException,
	// ClassNotFoundException {
	//
	// focusedElement =
	// EMFOperationsUtilNew.getElementContentSimpleElement(parentElement,
	// childElement, childAtributeName,
	// childAtributeValue, relationName, inst_resource);
	//
	// if (focusedElement == null) {
	//
	// EMFOperationsMessages.printMessage("ELEMENT_TO_FOCUS_NOT_EXITS");
	//
	// }
	// }
	//
	// public void focusElementReferenceSimpleElement(String parentElement, String
	// childElement, String childAtributeName,
	// String childAtributeValue, String relationName) throws
	// IllegalAccessException, IllegalArgumentException,
	// InvocationTargetException, NoSuchMethodException, SecurityException,
	// ClassNotFoundException {
	//
	// focusedElement =
	// EMFOperationsUtilNew.getElementReferencedSimpleElement(parentElement,
	// childElement, childAtributeName,
	// childAtributeValue, relationName, inst_resource);
	//
	// if (focusedElement == null) {
	//
	// EMFOperationsMessages.printMessage("ELEMENT_TO_FOCUS_NOT_EXITS");
	//
	// }
	// }

	// CHEQUEADO
	public void focusElement(String nameElement, String atributeName, Object atributeValue) {

		focusedElement = EMFOperationsUtilNew.getElement(nameElement, atributeName, atributeValue, inst_resource);
		if (focusedElement == null) {

			EMFOperationsMessages.printMessage("ELEMENT_TO_FOCUS_NOT_EXITS");

		}

	}

	// CHEQUEADO
	public void focusSimpleElement(String nameElement) {

		focusedElement = EMFOperationsUtilNew.getSimpleElement(nameElement, inst_resource);
		if (focusedElement == null) {

			EMFOperationsMessages.printMessage("ELEMENT_TO_FOCUS_NOT_EXITS");
		}

	}

	// UPDATE FUNCTIONS
	// public void updateElementAtribute(String nameElement, String atributeName,
	// Object oldAtributeValue,
	// Object newAtributeValue) throws ClassNotFoundException,
	// IllegalAccessException, IllegalArgumentException,
	// InvocationTargetException, NoSuchMethodException, SecurityException {
	//
	// if (EMFOperationsUtilNew.getElement(nameElement, atributeName,
	// oldAtributeValue, inst_resource) != null) {
	//
	// EObject eObject = (EObject) EMFOperationsUtilNew.getElement(nameElement,
	// atributeName, oldAtributeValue,
	// inst_resource);
	//
	// if (eObject != null) {
	// EList<EAttribute> eAllAttributes = eObject.eClass().getEAllAttributes();
	//
	// boolean atributeExists = false;
	// for (EAttribute eAtribute : eAllAttributes) {
	// if (eAtribute.getName().equals(atributeName)) {
	// atributeExists = true;
	// if (eObject.eGet(eAtribute).equals(oldAtributeValue)) {
	// try {
	// eObject.eSet(eAtribute, newAtributeValue);
	// EMFOperationsMessages.printMessage("NEW_ATRIBUTE_ADDED_CORRECTLY");
	// } catch (Exception e) {
	// EMFOperationsMessages.printMessage("NEW_ATRIBUTE_VALUE_INCORRECT");
	//
	// }
	// }
	//
	// }
	// }
	// if (!atributeExists) {
	// EMFOperationsMessages.printMessage("NEW_ATRIBUTE_INCORRECT");
	// deleteSimpleElement(eObject.eClass().getName());
	// }
	// }
	//
	// } else {
	// EMFOperationsMessages.printMessage("ELEMENT_TO_UPDATE_NOT_EXISTS");
	//
	// }
	//
	// }

	// FUNCTIONS OVER FOCUSED ELEMENT

	// CHEQUEADO
	public void focusSimpleElementContentOrderFocusElement(String nameElement, String relationName, int order) {
		focusedElement = EMFOperationsUtilNew.getSimpleElementContentOrderFocusedElement(nameElement,
				(EObject) focusedElement, relationName, order);
		if (focusedElement == null) {

			EMFOperationsMessages.printMessage("ELEMENT_TO_FOCUS_NOT_EXITS");
		}

	}
	// public void focusElementContentOrderFocusedElement(String nameElement, String
	// atributeName, String atributeValue,
	// String relationName,int order) {
	// focusedElement =
	// EMFOperationsUtilNew.getElementContentOrderFocusedElement(nameElement,
	// atributeName, atributeValue,
	// (EObject) focusedElement, relationName, order);
	// if (focusedElement == null) {
	//
	// EMFOperationsMessages.printMessage("ELEMENT_TO_FOCUS_NOT_EXITS");
	// }
	// }

	// public void focusSimpleElementContentFocusElement(String nameElement, String
	// relationName) {
	// focusedElement =
	// EMFOperationsUtilNew.getSimpleElementContentFocusedElement(nameElement,
	// (EObject) focusedElement, relationName);
	// if (focusedElement == null) {
	//
	// EMFOperationsMessages.printMessage("ELEMENT_TO_FOCUS_NOT_EXITS");
	// }
	//
	// }
	// CHEQUEADO
	public void focusElementContentFocusedElement(String nameElement, String atributeName, String atributeValue,
			String relationName) {
		focusedElement = EMFOperationsUtilNew.getElementContentFocusedElement(nameElement, atributeName, atributeValue,
				(EObject) focusedElement, relationName);
		if (focusedElement == null) {

			EMFOperationsMessages.printMessage("ELEMENT_TO_FOCUS_NOT_EXITS");
		}
	}

	// public void focusElementReferenceFocusedElement(String nameElement, String
	// atributeName, String atributeValue,
	// String relationName) {
	// focusedElement =
	// EMFOperationsUtilNew.getElementReferenceFocusedElement(nameElement,
	// atributeName,
	// atributeValue, (EObject) focusedElement, relationName);
	// if (focusedElement == null) {
	//
	// EMFOperationsMessages.printMessage("ELEMENT_TO_FOCUS_NOT_EXITS");
	// }
	// }
	//
	// public void focusSimpleElementReferenceElementFocusElement(String
	// nameElement, String childElement,
	// String childAtributeName, String childAtributeValue, String relationName) {
	//
	// }

	// public void deleteSimpleElementeReferenceElementFocusELement(String
	// nameElement,String childElement,String childAtributeName, String
	// childAtributeValue,String relationName)
	// {
	//
	// }

	// CHEQUEADA
	public void createSimpleElementContentFocusElement(String nameElement, String relationName) {

		String nameNormalized = EMFOperationsUtil.normalizedString(nameElement);

		if (focusedElement != null) {

			EList<EReference> eAllReferences = ((EObject) focusedElement).eClass().getEAllReferences();

			boolean referenceExists = false;
			boolean isContentReference = false;

			EObject pepe = (EObject) focusedElement;
			///
			// System.out.println(pepe.eClass().getEAllContainments());
			// System.out.println(pepe.eClass().getEAllReferences());

			///
			for (EReference eReference : eAllReferences) {
				// con el isContaiment puedo saber si existe la relacion y encima es de
				// referencia o no, debo dejar pasar solo las que son true
				// System.out.println(eReference.getName() + ":" + eReference.isContainment());
				if (eReference.getName().equals(relationName)) {
					referenceExists = true;
					if (eReference.isContainment()) {
						isContentReference = true;
						if (eReference.getUpperBound() > 0 || eReference.getUpperBound() == -1) {

							EObject eObjectChildren = createSimpleElement(nameNormalized);

							if (eObjectChildren != null) {
								if (eReference.getUpperBound() == 1) {

									((EObject) focusedElement).eSet(eReference, eObjectChildren);
								} else {
									EList<EObject> list = (EList) ((EObject) focusedElement).eGet(eReference);
									list.add(eObjectChildren);
								}
							}

						}
					}

				}

			}
			if (!referenceExists) {
				EMFOperationsMessages.printMessage("REFERENCE_NOT_EXISTS");
			}
			if (!isContentReference) {
				// la referencia no es de tipo content
			}
		} else {

			EMFOperationsMessages.printMessage("NOT_FOCUS_ELEMENT");
		}

	}

	public void deleteSimpleElementContentOrderFocusElement(String nameElement, String relationName, int order) {
		Object objectToDelete = null;
		objectToDelete = EMFOperationsUtilNew.getSimpleElementContentOrderFocusedElement(nameElement,
				(EObject) focusedElement, relationName, order);
		if (objectToDelete == null) {

			// EMFOperationsMessages.printMessage("ELEMENT_TO_FOCUS_NOT_EXITS");
			System.out.println("El objeto a borrar no existe en el elemento seleccionado");
		} else {
			EcoreUtil.delete((EObject) objectToDelete);
			System.out.println("Elemento borrado del objeto focus");
		}
	}

	public void deleteElementContentFocusElement(String nameElement, String atributeName, String atributeValue,
			String relationName) {
		Object objectToDelete = null;

		objectToDelete = EMFOperationsUtilNew.getElementContentFocusedElement(nameElement, atributeName, atributeValue,
				(EObject) focusedElement, relationName);
		if (objectToDelete == null) {

			System.out.println("El objeto a borrar no existe en el elemento seleccionado");
		} else {
			EcoreUtil.delete((EObject) objectToDelete);
			System.out.println("Elemento borrado del objeto focus");
		}
	}

	// public void deleteSimpleElementInsideFocusElement(String nameElement, String
	// relationName)
	// throws ClassNotFoundException, IllegalAccessException,
	// IllegalArgumentException, InvocationTargetException,
	// NoSuchMethodException, SecurityException, InstantiationException {
	//
	// if (EMFOperationsUtilNew.getSimpleElementEmpty(nameElement, inst_resource) !=
	// null) {
	//
	// if (focusedElement != null) {
	// EList<EReference> eAllReferences = ((EObject)
	// focusedElement).eClass().getEAllReferences();
	//
	// boolean referenceExists = false;
	// for (EReference eReference : eAllReferences) {
	//
	// if (eReference.getName().equals(relationName)) {
	// referenceExists = true;
	//
	// EObject eObjectChildren = (EObject)
	// EMFOperationsUtilNew.getSimpleElementEmpty(nameElement,
	// inst_resource);
	//
	// if (eObjectChildren != null) {
	// if (eReference.getUpperBound() == 1) {
	//
	// if (((EObject) focusedElement).eGet(eReference).equals(eObjectChildren)) {
	// EcoreUtil.delete(eObjectChildren);
	// EMFOperationsMessages.printMessage("ELEMENT_DELETED");
	// }
	//
	// } else {
	// EList<EObject> list = (EList) ((EObject) focusedElement).eGet(eReference);
	// list.remove(eObjectChildren);
	// EMFOperationsMessages.printMessage("ELEMENT_DELETED");
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
	// } else {
	// EMFOperationsMessages.printMessage("ELEMENT_TO_DELETE_NOT_EXITS");
	//
	// }
	//
	// }

	// // TODO hacer lo mismo para eliminar un elemento sin atributos
	// public void deleteElementContentFocusElement(String nameElement, String
	// atributeName, String atributeValue,
	// String relationName) throws ClassNotFoundException, IllegalAccessException,
	// IllegalArgumentException,
	// InvocationTargetException, NoSuchMethodException, SecurityException,
	// InstantiationException {
	//
	// Object eObjectToDelete=null;
	// eObjectToDelete =
	// EMFOperationsUtilNew.getElementContentFocusedElement(nameElement,
	// atributeName, atributeValue,
	// (EObject) focusedElement, relationName);
	// if (eObjectToDelete == null) {
	//
	// EMFOperationsMessages.printMessage("ELEMENT_TO_FOCUS_NOT_EXITS");
	// }else
	// {
	// EcoreUtil.delete((EObject) eObjectToDelete);
	// }
	//
	// }

	// CHEQUEADO
	public void clearAtributeFocusElement(String atributeName) throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {

		if (focusedElement != null) {
			EList<EAttribute> eAllAttributes = ((EObject) focusedElement).eClass().getEAllAttributes();

			boolean atributeExists = false;
			for (EAttribute eAtribute : eAllAttributes) {
				if (eAtribute.getName().equals(atributeName)) {
					atributeExists = true;

					try {
						((EObject) focusedElement).eSet(eAtribute, null);
						EMFOperationsMessages.printMessage("NEW_ATRIBUTE_ADDED_CORRECTLY");
					} catch (Exception e) {
						EMFOperationsMessages.printMessage("NEW_ATRIBUTE_VALUE_INCORRECT");

					}

				}
			}
			if (!atributeExists) {
				EMFOperationsMessages.printMessage("NEW_ATRIBUTE_INCORRECT");
			}
		} else {
			EMFOperationsMessages.printMessage("NOT_FOCUS_ELEMENT");
		}

	}

	// CHEQUEADO
	public void clearReferenceFocusElement(String referenceName)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException, ClassNotFoundException {

		if (focusedElement != null) {
			EList<EReference> eAllReferences = ((EObject) focusedElement).eClass().getEAllReferences();

			boolean referenceExists = false;
			boolean isContaintReference = false;
			for (EReference eReference : eAllReferences) {
				if (eReference.getName().equals(referenceName)) {
					referenceExists = true;

					if (!eReference.isContainment()) {

						((EObject) focusedElement).eUnset(eReference);
						EMFOperationsMessages.printMessage("REFERENCE_CLEAN");

					} else {
						isContaintReference = true;
					}

				}
			}
			if (!referenceExists) {
				EMFOperationsMessages.printMessage("REFERENCE_NOT_EXISTS");
			}
			if (isContaintReference) {
				// es una relacion de contener, no de referenciar
				System.out.println("fallo");
			}
		} else {
			EMFOperationsMessages.printMessage("NOT_FOCUS_ELEMENT");
		}

	}

	public void addElementAsReferenceFocusElement(String nameElement, String atributeName, Object atributeValue,
			String relationName) {
		EObject eObjectToAdd = null;

		eObjectToAdd = (EObject) EMFOperationsUtilNew.getElementInAllModel(nameElement, atributeName, atributeValue,
				inst_resource);

		if (eObjectToAdd != null) {

			EList<EReference> eAllReferences = ((EObject) focusedElement).eClass().getEAllReferences();

			boolean referenceExists = false;
			for (EReference eReference : eAllReferences) {

				if (eReference.getName().equals(relationName)) {

					referenceExists = true;

					if (!eReference.isContainment()) {
						// PONER RESTRICCIONES PARA NO SUPERAR EL NUMERO DE INSERCCIONES
						if (eReference.getUpperBound() > 1 || eReference.getUpperBound() == -1) {

							EList<EObject> hola = (EList<EObject>) ((EObject) focusedElement).eGet(eReference);
							hola.add(eObjectToAdd);

						} else {
							((EObject) focusedElement).eSet(eReference, eObjectToAdd);
						}

					} // INFORMAR DE QUE LA RELACION QUE SE INTENTA LLENAR ES CONTAIMENT Y NO
						// REFERENCE

				}

			}
			if (!referenceExists) {
				EMFOperationsMessages.printMessage("REFERENCE_NOT_EXISTS");
			}
		}

	}

	// CHEQUEADO
	public void createElementContentFocusElement(String nameElement, String atributeName, String atributeValue,
			String relationName) throws IllegalAccessException, ClassNotFoundException {
		String nameNormalized = EMFOperationsUtil.normalizedString(nameElement);

		if (focusedElement != null) {

			EList<EReference> eAllReferences = ((EObject) focusedElement).eClass().getEAllReferences();

			boolean referenceExists = false;
			boolean isContentReference = false;

			EObject pepe = (EObject) focusedElement;

			for (EReference eReference : eAllReferences) {
				// con el isContaiment puedo saber si existe la relacion y encima es de
				// referencia o no, debo dejar pasar solo las que son true
				System.out.println(eReference.getName() + ":" + eReference.isContainment());
				if (eReference.getName().equals(relationName)) {
					referenceExists = true;
					if (eReference.isContainment()) {
						isContentReference = true;

						if (eReference.getUpperBound() > 0 || eReference.getUpperBound() == -1) {

							// en el metodo createElement ya se testea si el atributo es unico y si ya
							// existe otro elemento de iguales caracteristicas
							EObject eObjectChildren = createElement(nameElement, atributeName, atributeValue);

							if (eObjectChildren != null) {
								if (eReference.getUpperBound() == 1) {

									((EObject) focusedElement).eSet(eReference, eObjectChildren);
								} else {
									EList<EObject> list = (EList) ((EObject) focusedElement).eGet(eReference);
									list.add(eObjectChildren);
								}
							}

						}
					}

				}

			}
			if (!referenceExists) {
				EMFOperationsMessages.printMessage("REFERENCE_NOT_EXISTS");
			}
			if (!isContentReference) {
				// la referencia no es de tipo content
			}
		} else {

			EMFOperationsMessages.printMessage("NOT_FOCUS_ELEMENT");
		}

	}

//	public void getPropertiesRecursive(EObject object) {
//		EObject o = object;
//
//		System.out.println("*Element: " + o.eClass().getName());
//		EList<EAttribute> eAllAttributes = o.eClass().getEAllAttributes();
//		EList<EReference> eAllReferences = o.eClass().getEAllReferences();
//
//		for (EAttribute eAttribute : eAllAttributes) {
//
//			System.out.println(eAttribute.getName() + ": " + o.eGet(eAttribute));
//
//		}
//		
//		for (EReference eReference : eAllReferences) {
//
//			if (!eReference.isContainment()) {
//				
//				System.out.println(eReference.getName() + ": ");
//				
//				if(eReference.getUpperBound()>1||(eReference.getUpperBound()==-1))
//						{
//							EList<EObject> allChildrenInReference=(EList<EObject>) o.eGet(eReference);
//							
//							for(EObject eChildrenInReference:allChildrenInReference) 
//							{
//								EList<EAttribute> eAllAttributesChildren = eChildrenInReference.eClass().getEAllAttributes();
//								System.out.println("\t*Element: "+eChildrenInReference.eClass().getName());
//								for (EAttribute eAttributeChildren : eAllAttributesChildren) {
//
//									System.out.println("\t"+eAttributeChildren.getName() + ": " + eChildrenInReference.eGet(eAttributeChildren));
//
//								}
//							}
//							
//							
//						}else 
//						{
//							
//							EObject obj=(EObject) o.eGet(eReference);
//							if(obj!=null) {
//							EList<EAttribute> eAllAttributesChildren = obj.eClass().getEAllAttributes();
//							System.out.println("\t*Element: "+obj.eClass().getName());
//							for (EAttribute eAttributeChildren : eAllAttributesChildren) {
//
//								System.out.println("\t"+eAttributeChildren.getName() + ": " + obj.eGet(eAttributeChildren));
//
//							}
//							}
//						}
//			}
//
//		}
//
//		
//
////		if (o.eCrossReferences().size() > 0) {
////
////			System.out.println("<<<<<<<<<< REFERENCE >>>>>>>>>>>");
////
////			for (EObject eobject : o.eCrossReferences()) {
////
////				// getPropertiesRecursive(eobject);
////				System.out.println(eobject.eClass().getName());
////
////				EList<EAttribute> eAllAttributesChildren = o.eClass().getEAllAttributes();
////
////				for (EAttribute eAttributeChildren : eAllAttributesChildren) {
////
////					System.out.println(eAttributeChildren.getName() + ": " + o.eGet(eAttributeChildren));
////
////				}
////
////			}
////
////		} else {
////			System.out.println("-----------------------");
////		}
//
//	}

	//CHEQUEADO
	public void getPropertiesFocusElement() {
		EObject o = (EObject) focusedElement;

		
		System.out.println("*Element: " + o.eClass().getName());
		EList<EAttribute> eAllAttributes = o.eClass().getEAllAttributes();
		EList<EReference> eAllReferences = o.eClass().getEAllReferences();

		for (EAttribute eAttribute : eAllAttributes) {

			System.out.println(eAttribute.getName() + ": " + o.eGet(eAttribute));

		}
		
		for (EReference eReference : eAllReferences) {

			if (!eReference.isContainment()) {
				
				System.out.println(eReference.getName() + ": ");
				
				if(eReference.getUpperBound()>1||(eReference.getUpperBound()==-1))
						{
							EList<EObject> allChildrenInReference=(EList<EObject>) o.eGet(eReference);
							
							for(EObject eChildrenInReference:allChildrenInReference) 
							{
								EList<EAttribute> eAllAttributesChildren = eChildrenInReference.eClass().getEAllAttributes();
								System.out.println("\t*Element: "+eChildrenInReference.eClass().getName());
								for (EAttribute eAttributeChildren : eAllAttributesChildren) {

									System.out.println("\t"+eAttributeChildren.getName() + ": " + eChildrenInReference.eGet(eAttributeChildren));

								}
							}
							
							
						}else 
						{
							
							EObject obj=(EObject) o.eGet(eReference);
							if(obj!=null) {
							EList<EAttribute> eAllAttributesChildren = obj.eClass().getEAllAttributes();
							System.out.println("\t*Element: "+obj.eClass().getName());
							for (EAttribute eAttributeChildren : eAllAttributesChildren) {

								System.out.println("\t"+eAttributeChildren.getName() + ": " + obj.eGet(eAttributeChildren));

							}
							}
						}
			}
		}

	}

	// CHEQUEADO
	public void updateAtributeFocusElement(String atributeName, Object atributeValue) {

		if (focusedElement != null) {

			EList<EAttribute> eAllAttributes = ((EObject) focusedElement).eClass().getEAllAttributes();
			EObject focusedElementCast = (EObject) focusedElement;
			boolean atributeExists = false;

			for (EAttribute eAtribute : eAllAttributes) {
				if (eAtribute.getName().equals(atributeName)) {
					atributeExists = true;

					if (eAtribute.isUnique()) {

						if (EMFOperationsUtilNew.getElement(focusedElementCast.eClass().getName(), atributeName,
								atributeValue, inst_resource) == null) {
							try {
								((EObject) focusedElement).eSet(eAtribute, atributeValue);
								EMFOperationsMessages.printMessage("NEW_ATRIBUTE_ADDED_CORRECTLY");
							} catch (Exception e) {
								EMFOperationsMessages.printMessage("NEW_ATRIBUTE_VALUE_INCORRECT");

							}
						} else {
							// ya existe otro elemento con el mismo atributo unico
						}
					} else {
						try {
							((EObject) focusedElement).eSet(eAtribute, atributeValue);
							EMFOperationsMessages.printMessage("NEW_ATRIBUTE_ADDED_CORRECTLY");
						} catch (Exception e) {
							EMFOperationsMessages.printMessage("NEW_ATRIBUTE_VALUE_INCORRECT");

						}
					}

				}
			}
			if (!atributeExists) {
				EMFOperationsMessages.printMessage("NEW_ATRIBUTE_INCORRECT");
			}

		} else {
			EMFOperationsMessages.printMessage("NOT_FOCUS_ELEMENT");
		}

	}

}
