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
	public Object focusedElement = null;
	public String rootNodeName = null;
	public boolean rootNodeCreated=false;
	
	public EMFOperationsNew() throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {

		rootNodeName = EMFOperationsUtilNew.getRootElement();
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

	public boolean isRootNodeCreated() 
	{
		if (EMFOperationsUtilNew.getSimpleElementOrder(rootNodeName, inst_resource, 1) == null) {
			
			return false;
			
		}else 
		{
			return true;
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
			rootNodeCreated=isRootNodeCreated();
			

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
			if (rootNodeName.toLowerCase().equals(nameElement.toLowerCase())) {
				if (EMFOperationsUtilNew.getSimpleElementOrder(nameElement, inst_resource, 1) == null) {
					try {

						Class<?> pfcl = Class.forName(EMFOperationsUtilNew.getMetaModelPackage().getName() + "."
								+ packageNameNormalized + "Factory");
						eObj = (EObject) pfcl.getMethod("create" + nameNormalized).invoke(factory);
						inst_resource.getContents().add(eObj);

					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					
					EMFOperationsMessages.printMessage("ELEMENT_NODE_EXISTS");
				}
			} else {
				
				if (EMFOperationsUtilNew.getSimpleElementOrder(rootNodeName, inst_resource, 1) != null) {
					try {

						Class<?> pfcl = Class.forName(EMFOperationsUtilNew.getMetaModelPackage().getName() + "."
								+ packageNameNormalized + "Factory");
						eObj = (EObject) pfcl.getMethod("create" + nameNormalized).invoke(factory);
						inst_resource.getContents().add(eObj);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				//EMFOperationsMessages.printMessage("ELEMENT_NOT_NODE");
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

								return eObject;
							} catch (Exception e) {
								EcoreUtil.delete(eObject);
								EMFOperationsMessages.printMessage("NEW_ATRIBUTE_VALUE_INCORRECT");

							}
						} else {

							EMFOperationsMessages.printMessage("NEW_ELEMENT_ALREADY_EXITS");

						}
					} else {
						try {
							eObject.eSet(eAtribute, atributeValue);

							return eObject;
						} catch (Exception e) {
							EcoreUtil.delete(eObject);
							EMFOperationsMessages.printMessage("NEW_ATRIBUTE_VALUE_INCORRECT");

						}
					}

				}
			}
			if (!atributeExists) {
				EMFOperationsMessages.printMessage("NEW_ATRIBUTE_INCORRECT");

			}
		}

		return null;

	}

	// DELETE FUNCTIONS

	// CHEQUEADA
	// TODO hacer una deleteSimpleElement
	public void deleteSimpleElementOrder(String nameElement, int order) {
		if (EMFOperationsUtil.getElementFromResource(nameElement, inst_resource) != null) {
			EcoreUtil.delete((EObject) EMFOperationsUtilNew.getSimpleElementOrder(nameElement, inst_resource, order));

			EMFOperationsMessages.printMessage("ELEMENT_DELETED");
		} else {
			EMFOperationsMessages.printMessage("ELEMENT_TO_DELETE_NOT_EXITS");
		}
	}

	// CHEQUEADA
	public void deleteElement(String nameElement, String atributeName, Object atributeValue) {

		if (EMFOperationsUtilNew.getElement(nameElement, atributeName, atributeValue, inst_resource) != null) {
			EcoreUtil.delete((EObject) EMFOperationsUtil.getElementFromResource(nameElement, atributeName,
					atributeValue, inst_resource));
			EMFOperationsMessages.printMessage("ELEMENT_DELETED");

		} else {
			EMFOperationsMessages.printMessage("ELEMENT_TO_DELETE_NOT_EXITS");

		}

	}

	// CHEQUEADO //CAMBIADO
	public boolean focusElement(String nameElement, String atributeName, Object atributeValue) {

		focusedElement = EMFOperationsUtilNew.getElement(nameElement, atributeName, atributeValue, inst_resource);
		if (focusedElement == null) {

			EMFOperationsMessages.printMessage("ELEMENT_TO_FOCUS_NOT_EXITS");
			return false;

		} else {
			EMFOperationsMessages.printMessage("ELEMENT_FOCUSED");
			return true;
		}

	}

	// CHEQUEADO
	public boolean focusSimpleElementOrder(String nameElement, int order) {

		focusedElement = EMFOperationsUtilNew.getSimpleElementOrder(nameElement, inst_resource, order);
		if (focusedElement == null) {

			EMFOperationsMessages.printMessage("ELEMENT_TO_FOCUS_NOT_EXITS");
			return false;
		} else {
			EMFOperationsMessages.printMessage("ELEMENT_FOCUSED");
			return true;
		}

	}

	// FUNCTIONS OVER FOCUSED ELEMENT

	// CHEQUEADO
	public boolean focusSimpleElementContentOrderFocusElement(String nameElement, String relationName, int order) {
		focusedElement = EMFOperationsUtilNew.getSimpleElementContentOrderFocusedElement(nameElement,
				(EObject) focusedElement, relationName, order);
		if (focusedElement == null) {

			EMFOperationsMessages.printMessage("ELEMENT_TO_FOCUS_NOT_EXITS");
			return false;
		} else {
			EMFOperationsMessages.printMessage("ELEMENT_FOCUSED");
			return true;
		}

	}

	// CHEQUEADO
	public boolean focusElementContentFocusedElement(String nameElement, String atributeName, String atributeValue,
			String relationName) {
		focusedElement = EMFOperationsUtilNew.getElementContentFocusedElement(nameElement, atributeName, atributeValue,
				(EObject) focusedElement, relationName);
		if (focusedElement == null) {

			EMFOperationsMessages.printMessage("ELEMENT_TO_FOCUS_NOT_EXITS");
			return false;
		} else {
			EMFOperationsMessages.printMessage("ELEMENT_FOCUSED");
			return true;
		}
	}

	// CHEQUEADA
	public EObject createSimpleElementContentFocusElement(String nameElement, String relationName) {

		String nameNormalized = EMFOperationsUtil.normalizedString(nameElement);

		if (focusedElement != null) {

			EList<EReference> eAllReferences = ((EObject) focusedElement).eClass().getEAllReferences();

			boolean referenceExists = false;
			boolean isContentReference = false;

			EObject pepe = (EObject) focusedElement;

			for (EReference eReference : eAllReferences) {

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
									return eObjectChildren;
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
				EMFOperationsMessages.printMessage("NO_CONTENT_REFERENCE");
			}
		} else {

			EMFOperationsMessages.printMessage("NOT_FOCUS_ELEMENT");
		}
		return null;

	}

	// CHEQUEADO
	public void deleteSimpleElementContentOrderFocusElement(String nameElement, String relationName, int order) {
		Object objectToDelete = null;
		objectToDelete = EMFOperationsUtilNew.getSimpleElementContentOrderFocusedElement(nameElement,
				(EObject) focusedElement, relationName, order);
		if (objectToDelete == null) {

			EMFOperationsMessages.printMessage("ELEMENT_TO_FOCUS_NOT_EXITS");

		} else {
			EcoreUtil.delete((EObject) objectToDelete);
			EMFOperationsMessages.printMessage("ELEMENT_DELETED");
		}
	}

	// CHEQUEADO
	public void deleteElementContentFocusElement(String nameElement, String atributeName, String atributeValue,
			String relationName) {
		Object objectToDelete = null;

		objectToDelete = EMFOperationsUtilNew.getElementContentFocusedElement(nameElement, atributeName, atributeValue,
				(EObject) focusedElement, relationName);
		if (objectToDelete == null) {

			EMFOperationsMessages.printMessage("ELEMENT_TO_FOCUS_NOT_EXITS");
		} else {
			EcoreUtil.delete((EObject) objectToDelete);

			EMFOperationsMessages.printMessage("ELEMENT_DELETED");
		}
	}

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
						EMFOperationsMessages.printMessage("CLEAN_ATRIBUTE");

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

	public void removeElementAsReferenceFocusElement(String nameElement, String atributeName, Object atributeValue,
			String relationName) {

		EObject elementToRemove = (EObject) EMFOperationsUtilNew.getElementReferenceFocusedElement(nameElement,
				atributeName, atributeValue, (EObject) focusedElement, relationName);

		if (focusedElement != null ) {
			
			if(elementToRemove != null) {

			EList<EReference> eAllReferences = ((EObject) focusedElement).eClass().getEAllReferences();

			boolean referenceExists = false;
			boolean isContaintReference = false;
			for (EReference eReference : eAllReferences) {
				if (eReference.getName().equals(relationName)) {
					referenceExists = true;

					if (!eReference.isContainment()) {

						if (eReference.getUpperBound() > 1 || eReference.getUpperBound() == -1) {

							EList<EObject> list = (EList<EObject>) ((EObject) focusedElement).eGet(eReference);
							list.remove(elementToRemove);
							EMFOperationsMessages.printMessage("REMOVE_ELEMENT");

						} else {
							((EObject) focusedElement).eSet(eReference, null);
							EMFOperationsMessages.printMessage("REMOVE_ELEMENT");
						}

					} else {
						isContaintReference = true;
					}

				}
			}
			if (!referenceExists) {
				EMFOperationsMessages.printMessage("REFERENCE_NOT_EXISTS");
			}
			if (isContaintReference) {

				EMFOperationsMessages.printMessage("NO_REFERENCE");
			}
		}else 
		{
			
			EMFOperationsMessages.printMessage("ELEMENT_TO_REMOVE_AS_REFERENCE_NOT_EXITS");
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
						EMFOperationsMessages.printMessage("CLEAN_REFERENCE");

					} else {
						isContaintReference = true;
					}

				}
			}
			if (!referenceExists) {
				EMFOperationsMessages.printMessage("REFERENCE_NOT_EXISTS");
			}
			if (isContaintReference) {

				EMFOperationsMessages.printMessage("NO_REFERENCE");
			}
		} else {
			EMFOperationsMessages.printMessage("NOT_FOCUS_ELEMENT");
		}

	}

	public  String getElementNameFromRelation(String relationName) 
	{
		EList<EReference> eAllReferences = ((EObject) focusedElement).eClass().getEAllReferences();
		for (EReference eReference : eAllReferences) {

			if (eReference.getName().equals(relationName)) {

				
				return eReference.getEType().getName().toLowerCase();

			}

		}
		return "";
	}
	
	// CHEQUEADA
	public void addElementAsReferenceFocusElement(String nameElement, String atributeName, Object atributeValue,
			String relationName) {
		EObject eObjectToAdd = null;
		boolean isContaintReference = true;

		eObjectToAdd = (EObject) EMFOperationsUtilNew.getElementInAllModel(nameElement, atributeName, atributeValue,
				inst_resource);

		if (eObjectToAdd != null) {

			EList<EReference> eAllReferences = ((EObject) focusedElement).eClass().getEAllReferences();

			boolean referenceExists = false;
			for (EReference eReference : eAllReferences) {

				if (eReference.getName().equals(relationName)) {

					referenceExists = true;

					if (!eReference.isContainment()) {
						isContaintReference = false;
						//  TODO PONER RESTRICCIONES PARA NO SUPERAR EL NUMERO DE INSERCCIONES
						if (eReference.getUpperBound() > 1 || eReference.getUpperBound() == -1) {

							EList<EObject> hola = (EList<EObject>) ((EObject) focusedElement).eGet(eReference);
							hola.add(eObjectToAdd);
							EMFOperationsMessages.printMessage("ADD_ELEMENT");

						} else {
							((EObject) focusedElement).eSet(eReference, eObjectToAdd);
							EMFOperationsMessages.printMessage("ADD_ELEMENT");
						}

					}

				}

			}
			if (isContaintReference) {

				EMFOperationsMessages.printMessage("NO_REFERENCE");
			}
			if (!referenceExists) {
				EMFOperationsMessages.printMessage("REFERENCE_NOT_EXISTS");
			}
		}else 
		{
			EMFOperationsMessages.printMessage("ELEMENT_TO_ADD_AS_REFERENCE_NOT_EXITS");
		}

	}

	// CHEQUEADO
	public EObject createElementContentFocusElement(String nameElement, String atributeName, String atributeValue,
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
				// System.out.println(eReference.getName() + ":" + eReference.isContainment());
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
									return eObjectChildren;
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
				EMFOperationsMessages.printMessage("NO_CONTENT_REFERENCE");
			}
		} else {

			EMFOperationsMessages.printMessage("NOT_FOCUS_ELEMENT");
		}
		return null;

	}

	// CHEQUEADO
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

				if (eReference.getUpperBound() > 1 || (eReference.getUpperBound() == -1)) {
					EList<EObject> allChildrenInReference = (EList<EObject>) o.eGet(eReference);

					for (EObject eChildrenInReference : allChildrenInReference) {
						EList<EAttribute> eAllAttributesChildren = eChildrenInReference.eClass().getEAllAttributes();
						System.out.println("\t*Element: " + eChildrenInReference.eClass().getName());
						for (EAttribute eAttributeChildren : eAllAttributesChildren) {

							System.out.println("\t" + eAttributeChildren.getName() + ": "
									+ eChildrenInReference.eGet(eAttributeChildren));

						}
					}

				} else {

					EObject obj = (EObject) o.eGet(eReference);
					if (obj != null) {
						EList<EAttribute> eAllAttributesChildren = obj.eClass().getEAllAttributes();
						System.out.println("\t*Element: " + obj.eClass().getName());
						for (EAttribute eAttributeChildren : eAllAttributesChildren) {

							System.out
									.println("\t" + eAttributeChildren.getName() + ": " + obj.eGet(eAttributeChildren));

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
							// EMFOperationsMessages.printMessage("NEW_ATRIBUTE_ADDED_CORRECTLY");
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
