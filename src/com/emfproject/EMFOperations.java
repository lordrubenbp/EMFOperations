package com.emfproject;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;



public class EMFOperations {

	ResourceSet inst_resourceset;
	Resource inst_resource;
	Object factory;
	String metaModelURI;
	Object focusedElement;

	public EMFOperations() throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException,
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

	public void loadModelInstance(String path) {

		inst_resourceset = new ResourceSetImpl();

		// Register XML resource as UMLResource.Factory.Instance
		Map extensionFactoryMap = Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap();

		extensionFactoryMap.put("xmi", new XMIResourceFactoryImpl());

		inst_resource = (Resource) inst_resourceset.createResource(URI.createFileURI(path));

		try {
			
			inst_resource.load(null);
			System.out.println(EMFOperationsMessages.MODEL_LOADED);
		} catch (IOException e) {

			System.out.println(EMFOperationsMessages.MODEL_NOT_EXITS);
		}
		//System.out.println(inst_resource.getContents());
		//EMFOperationsUtil.showAllModelData(inst_resource);
		
		

	}

	public void setFocusElement(String parentElement,String childElement,String childAtributeName,String childAtributeValue,String relationName) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException 
	{
		
		if(EMFOperationsUtil.getElementFromResource(parentElement,childElement,childAtributeName,childAtributeValue,relationName,inst_resource)!=null) 
		{
			focusedElement=EMFOperationsUtil.getElementFromResource(parentElement,childElement,childAtributeName,childAtributeValue,relationName,inst_resource);

		}
	}
	public void setFocusElement(String nameElement, String atributeName, Object atributeValue) {


		if(EMFOperationsUtil.getElementFromResource(nameElement, atributeName, atributeValue, inst_resource)!=null) 
		{
			focusedElement=EMFOperationsUtil.getElementFromResource(nameElement, atributeName, atributeValue, inst_resource);

		}

		/*for (int i = 0; i < inst_resource.getContents().size(); i++) {
			// System.out.println(inst_resource.getContents().get(i).toString());

			if (inst_resource.getContents().get(i).toString().contains(atributeName + ": " + atributeValue + ")")
					|| inst_resource.getContents().get(i).toString().contains(atributeName + ": " + atributeValue + ",")
							&& inst_resource.getContents().get(i).toString().contains(nameNormalized)) {
				focusedElement = inst_resource.getContents().get(i);

			}
		}*/
	}

	public void setFocusElement(String nameElement) {

		if(EMFOperationsUtil.getElementFromResource(nameElement, inst_resource)!=null) 
		{
			focusedElement=EMFOperationsUtil.getElementFromResource(nameElement, inst_resource);
		}
		/*for (int i = 0; i < inst_resource.getContents().size(); i++) {
			// System.out.println(inst_resource.getContents().get(i).toString());

			if (inst_resource.getContents().get(i).toString().contains(nameNormalized)) {
				focusedElement = inst_resource.getContents().get(i);

			}
		}*/
	}
	public Object getFocusedElement() {
		// System.out.println(focusedElement);
		return focusedElement;
	}

	public void getPropertiesFromFocusedElement() throws ClassNotFoundException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		// System.out.println(focusedElement.getClass().getSimpleName());
		String sanitalizeElementName[] = focusedElement.getClass().getSimpleName().split("Impl");
		String elementName = sanitalizeElementName[0];
		List list=null;
		// System.out.println(elementName);
		Class cl = Class.forName(EMFOperationsUtil.getMetaModelPackage().getName() + "." + elementName);
		// pillo todas los metodos get y recorro uno a uno llamandolo con el objeto
		Method[] m = cl.getDeclaredMethods();
		// recorro los metodos
		for (int z = 0; z < m.length; z++) {
			// pillo los get
			if (m[z].getName().contains("get")) {

				String propertyName = m[z].getName().substring(3);
				// System.out.println(cl.getMethod(m[z].getName()).invoke(focusedElement).getClass().getSimpleName());
				// si el get devuelve un EList, es un conjunto de objetos del otro elemento
				// referenciado
				if (m[z].getReturnType().getSimpleName().equals("EList")) {
					// lo convierto en lista
					 list = (List) cl.getMethod(m[z].getName())
							.invoke(focusedElement);
					// recorro cada uno de los elementos
					for (int y = 0; y < list.size(); y++) {
						// System.out.println(list.get(y));

						String sanitalizeReferenceName[] = list.get(y).getClass().getSimpleName().split("Impl");
						String referenceName = sanitalizeReferenceName[0];
						// System.out.println(referenceName);
						// hago el mismo proceso que al inicio del metodo pero cada uno de estos objetos
						Class rcl = Class
								.forName(EMFOperationsUtil.getMetaModelPackage().getName() + "." + referenceName);

						Method[] mr = rcl.getDeclaredMethods();

						for (int x = 0; x < mr.length; x++) {
							if (mr[x].getName().contains("get")) {
								String subPropertyName = mr[x].getName().substring(3);
								System.out.println(propertyName + ": " + referenceName + ": " + subPropertyName + ": "
										+ rcl.getMethod(mr[x].getName()).invoke(list.get(y)));
							}
						}
					}

				} else {

					System.out.println(propertyName + ": " + cl.getMethod(m[z].getName()).invoke(focusedElement));
				}

			}

		}

	}

	//TODO borra el elemento que esta en la referencia, quizas seria mas correcto que lo desasociarse
	public void removeReferenceFromFocusedElement(String nameElement,String atributeName, String atributeValue, String referenceName)
			throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException {
		
		String nameNormalized = EMFOperationsUtil.normalizedString(nameElement);
		String referenceNameNormalized = EMFOperationsUtil.normalizedString(referenceName);
	
		
		if (EMFOperationsUtil.getElementFromResource(nameNormalized,atributeName, atributeValue,inst_resource) != null) {
			// TODO chequear que el elemento es una referencia validad para el
			// focusedElement
			// System.out.println(focusedElement.getClass().getSimpleName());
			String sanitalizeElementName[] = focusedElement.getClass().getSimpleName().split("Impl");
			String elementName = sanitalizeElementName[0];
			List list=null;
			// System.out.println(elementName);
			Class cl = Class.forName(EMFOperationsUtil.getMetaModelPackage().getName() + "." + elementName);
			// pillo todas los metodos get y recorro uno a uno llamandolo con el objeto
			Method[] m = cl.getDeclaredMethods();
			// recorro los metodos
			for (int z = 0; z < m.length; z++) {
				// pillo los get

				if (m[z].getName().equals("get" + referenceNameNormalized)) {
					// System.out.println(m[z].getReturnType().getSimpleName());
					// si la referencia es multiple, devolvera un EList
					// System.out.println(cl.getMethod(m[z].getName()).invoke(focusedElement).getClass().getSimpleName());
					// no entras
					if (m[z].getReturnType().getSimpleName().equals("EList")) {
						// TODO me queda comprobar si el listado no es de * elementos, saber el numero
						// de ellos para no hacer una inserccion por encima del valor
						// esta info deberia tenerla en el metamodelo EPackage
						 list = (List) cl.getMethod(m[z].getName())
								.invoke(focusedElement);

						// compruebo que el objeto que se quiere referencia ya no lo estuviese antes
						if (list.contains(EMFOperationsUtil.getElementFromResource(nameNormalized,atributeName, atributeValue,inst_resource))) {
							if(EMFOperationsUtil.getUpperBound(elementName, referenceName)<list.size()) 
							{
								list.remove(EMFOperationsUtil.getElementFromResource(nameNormalized, atributeName, atributeValue,inst_resource));
							}else 
							{
								System.out.println(EMFOperationsMessages.ELEMENT_TO_REMOVE_AS_REFERENCE_MINIMUM);
							}
							
						}
					}
					// si la referencia es unica..
					else if (m[z].getReturnType().getSimpleName().equals(nameNormalized)) {
						Class ccl = Class
								.forName(EMFOperationsUtil.getMetaModelPackage().getName() + "." + nameNormalized);
						Object[] parameters = { null };
						cl.getMethod("set" + referenceNameNormalized, ccl).invoke(focusedElement, parameters);
					}

				}

			}
		} else {
			System.out.println(EMFOperationsMessages.ELEMENT_TO_REMOVE_AS_REFERENCE_NOT_EXITS);
		}
	}

	public void addReferenceToFocusedElement(String nameElement,String atributeName, String atributeValue, String referenceName)
			throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		String nameNormalized = EMFOperationsUtil.normalizedString(nameElement);
		String referenceNameNormalized = EMFOperationsUtil.normalizedString(referenceName);
		//dependiendo si es referencia o es un hijo del elemento me devolvera un objeto lista u otro, por eso se usa la clase generica List que engloba a todas
		List list=null;
		// compruebo que el elemento que quiero añadir existe
		if (EMFOperationsUtil.getElementFromResource(nameNormalized,atributeName, atributeValue,inst_resource) != null) {
			// TODO chequear que el elemento es una referencia validad para el
			// focusedElement
			// System.out.println(focusedElement.getClass().getSimpleName());
			String sanitalizeElementName[] = focusedElement.getClass().getSimpleName().split("Impl");
			String elementName = sanitalizeElementName[0];
			// System.out.println(elementName);
			Class cl = Class.forName(EMFOperationsUtil.getMetaModelPackage().getName() + "." + elementName);
			// pillo todas los metodos get y recorro uno a uno llamandolo con el objeto
			Method[] m = cl.getDeclaredMethods();
			// recorro los metodos
			for (int z = 0; z < m.length; z++) {
				// pillo los get
				if (m[z].getName().equals("get" + referenceNameNormalized)) {
					// si la referencia es multiple, devolvera un EList
					//System.out.println(cl.getMethod(m[z].getName()).invoke(focusedElement).getClass().getSimpleName());
					// no entras
					if (m[z].getReturnType().getSimpleName().equals("EList")) {
						// TODO me queda comprobar si el listado no es de * elementos, saber el numero
						// de ellos para no hacer una inserccion por encima del valor
						// esta info deberia tenerla en el metamodelo EPackage
							
							list = (List) cl.getMethod(m[z].getName())
									.invoke(focusedElement);
						
						// compruebo que el objeto que se quiere referencia ya no lo estuviese antes
						if (list.contains(EMFOperationsUtil.getElementFromResource(nameNormalized,atributeName, atributeValue,inst_resource))) {
							System.out.println(EMFOperationsMessages.ELEMENT_ALREADY_REFERENCED);
						} else {
							
							
							if(EMFOperationsUtil.getUpperBound(elementName, referenceName)>list.size()||EMFOperationsUtil.getUpperBound(elementName, referenceName)==-1) 
							{
								list.add(EMFOperationsUtil.getElementFromResource(nameNormalized,atributeName, atributeValue,inst_resource));
							}else 
							{
								System.out.println(EMFOperationsMessages.ELEMENT_TO_REMOVE_AS_REFERENCE_MAXIMUM);
							}
							
						}
					}
					// si la referencia es unica..
					else if (m[z].getReturnType().getSimpleName().equals(nameNormalized)) {

						Class ccl = Class
								.forName(EMFOperationsUtil.getMetaModelPackage().getName() + "." + nameNormalized);

						cl.getMethod("set" + referenceNameNormalized, ccl).invoke(focusedElement,
								EMFOperationsUtil.getElementFromResource(nameNormalized, atributeName, atributeValue,inst_resource));
					}

				}

			}
		} else {
			System.out.println(EMFOperationsMessages.ELEMENT_TO_ADD_AS_REFERENCE_NOT_EXITS);
		}

	}

	
	//TODO hacer una implementacion mas de delete para elementos que no tienen atributos
	public void deleteElement(String nameElement, String atributeName, Object atributeValue) {
		
		EcoreUtil.delete((EObject) EMFOperationsUtil.getElementFromResource(nameElement, atributeName, atributeValue, inst_resource));
		System.out.println(EMFOperationsMessages.ELEMENT_DELETED);
		
		/*//MEJOR HACERLO EN EL UTILS
		//TENGO TMB QUE TODOS LOS CHECK CAMBIARLOS AL NUEVO METODO
		
		String nameNormalized=EMFOperationsUtil.normalizedString(nameElement);
		
		List<EObject>list=inst_resource.getContents();
		
		
		for (int i = 0; i < list.size(); i++) {
			// System.out.println(inst_resource.getContents().get(i).toString());
			if (list.get(i).toString().contains(atributeName + ": " + atributeValue + ")")
					|| list.get(i).toString().contains(atributeName + ": " + atributeValue + ",")
							&& list.get(i).toString().contains(nameNormalized)) {
				list.remove(list.get(i));
				
			}
			
			
		}*/
		
		
	}

	//TODO cambiar a nuevo metodo de lectura de atributos
	public void renameElement(String nameElement, String atributeName, Object oldAtributeValue, Object newAtributeValue)
			throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		String nameNormalized = EMFOperationsUtil.normalizedString(nameElement);
		String atributeNameNormalized = EMFOperationsUtil.normalizedString(atributeName);
		for (int i = 0; i < inst_resource.getContents().size(); i++) {
			// System.out.println(inst_resource.getContents().get(i).toString());
			// TODO meter mensaje para indicar si existe o no el elemento a renombrar
			if (inst_resource.getContents().get(i).toString().contains(atributeName + ": " + oldAtributeValue + ")")
					|| inst_resource.getContents().get(i).toString()
							.contains(atributeName + ": " + oldAtributeValue + ",")
							&& inst_resource.getContents().get(i).toString().contains(nameNormalized)) {
				Object obj = inst_resource.getContents().get(i);
				// pillo la clase del objeto que deseo crear
				Class cl = Class.forName(EMFOperationsUtil.getMetaModelPackage().getName() + "." + nameNormalized);

				cl.getMethod("set" + atributeNameNormalized, String.class).invoke(obj, (String) newAtributeValue);
			}
		}
	}

	public void setProperty(String atributeName, Object atributeValue)
			throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		Class atributeClassType = null;
		String atributeNameNormalized = EMFOperationsUtil.normalizedString(atributeName);
		// tengo que meter chequeadores de que tanto el atributo como el valor existen
		String sanitalizeElementName[] = focusedElement.getClass().getSimpleName().split("Impl");
		String elementName = sanitalizeElementName[0];
		// System.out.println(elementName);
		if (EMFOperationsUtil.checkElementInsertion(elementName, atributeName, atributeValue)) {

			Class cl = Class.forName(EMFOperationsUtil.getMetaModelPackage().getName() + "." + elementName);
			Method[] m = cl.getDeclaredMethods();
			// recorro los metodos
			for (int z = 0; z < m.length; z++) {
				// System.out.println(m[z].getParameterCount());
				// filtro por aquellos que tienen un parametro de entrada
				if (m[z].getParameterCount() > 0) {
					// filtro por aquel que es un set y encima el nombre es el del parametro mio
					if (m[z].getName().equals("set" + atributeNameNormalized)) {
						// pilla la clase del parametro/s que recibe el metodo
						Class<?>[] pType = m[z].getParameterTypes();
						// Type[] gpType = m[z].getGenericParameterTypes();
						// recorro todas las clases de los parametros de entrada de mi metodo
						for (int i = 0; i < pType.length; i++) {

							// System.out.println("ParameterType "+pType[i].toString());
							// capturo la clase de mi atributo
							atributeClassType = pType[i];
							// convierto mi objeto generico que era el atributeValue, en un objeto
							// especifico en base a la clase del atributo capturada
							atributeValue = EMFOperationsUtil.castAtributeValue(pType[i].toString(), atributeValue);

						}
					}
				}
			}
			cl.getMethod("set" + atributeNameNormalized, atributeClassType).invoke(focusedElement, atributeValue);
			System.out.println(EMFOperationsMessages.PROPERTY_ADDED_CORRECTLY);
		} else {
			System.out.println(EMFOperationsMessages.PROPERTY_NOT_ADDED);
		}
	}

	public void clearProperty(String atributeName) throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		Class atributeClassType = null;
		String atributeNameNormalized = EMFOperationsUtil.normalizedString(atributeName);
		// tengo que meter chequeadores de que tanto el atributo como el valor existen
		String sanitalizeElementName[] = focusedElement.getClass().getSimpleName().split("Impl");
		String elementName = sanitalizeElementName[0];
		List list=null;
	
		Class cl = Class.forName(EMFOperationsUtil.getMetaModelPackage().getName() + "." + elementName);
		Method[] m = cl.getDeclaredMethods();
		// recorro los metodos
		for (int z = 0; z < m.length; z++) {
			
			// filtro por aquellos que tienen un parametro de entrada
			if (m[z].getParameterCount() > 0) {
				// filtro por aquel que es un set y encima el nombre es el del parametro mio
				if (m[z].getName().equals("set" + atributeNameNormalized)) {
					// pilla la clase del parametro/s que recibe el metodo
					Class<?>[] pType = m[z].getParameterTypes();
					// Type[] gpType = m[z].getGenericParameterTypes();
					// recorro todas las clases de los parametros de entrada de mi metodo
					for (int i = 0; i < pType.length; i++) {

						// System.out.println("ParameterType "+pType[i].toString());
						// capturo la clase de mi atributo
						atributeClassType = pType[i];
						// convierto mi objeto generico que era el atributeValue, en un objeto
						// especifico en base a la clase del atributo capturada

					}
					
					Object[] parameters = { null };
					cl.getMethod("set" + atributeNameNormalized, atributeClassType).invoke(focusedElement,parameters);
				}

			} else {
				
				
				if (m[z].getName().equals("get" + atributeNameNormalized)) {
					if (m[z].getReturnType().getSimpleName().equals("EList")) {
						// lo convierto en lista
						 list = (List) cl.getMethod(m[z].getName())
								.invoke(focusedElement);
						list.clear();
					
					}
				}
			}
		}

	}
	
	public void addElement(String nameElement) 
	{
		String packageNameNormalized = EMFOperationsUtil
				.normalizedString(EMFOperationsUtil.getMetaModelPackage().getName());
		String nameNormalized = EMFOperationsUtil.normalizedString(nameElement);
		
		
		//if (EMFOperationsUtil.getElementFromResource(nameNormalized,inst_resource) == null) {
			
			if (EMFOperationsUtil.checkElementInsertion(nameElement)) {
				
				try {
					
					
					Class pfcl = Class.forName(EMFOperationsUtil.getMetaModelPackage().getName() + "."
							+ packageNameNormalized + "Factory");
					Object obj = pfcl.getMethod("create" + nameNormalized).invoke(factory);
					inst_resource.getContents().add((EObject) obj);
					System.out.println(EMFOperationsMessages.NEW_ELEMENT_ADDED_CORRECTLY);
					
				}catch(Exception e) 
				{
					e.printStackTrace();
				}
				
			}
			
		//}
		
	}
	
	public void addElement(String nameElement, String atributeName, Object atributeValue,String parentNameElement,String relationName)
			throws IllegalAccessException, ClassNotFoundException {

		String packageNameNormalized = EMFOperationsUtil
				.normalizedString(EMFOperationsUtil.getMetaModelPackage().getName());
		String nameNormalized = EMFOperationsUtil.normalizedString(nameElement);
		String atributeNameNormalized = EMFOperationsUtil.normalizedString(atributeName);
		String parentNameElementNormalized=EMFOperationsUtil.normalizedString(parentNameElement);
		String relationNameNormalized=EMFOperationsUtil.normalizedString(relationName);
	
	    if (EMFOperationsUtil.getElementFromResource(parentNameElementNormalized,inst_resource)!= null) {
		if (EMFOperationsUtil.getElementFromResource(nameNormalized, atributeName, atributeValue,inst_resource) == null) {

			if (EMFOperationsUtil.checkElementInsertion(nameElement, atributeName, atributeValue)) {

				try {
					// clase del parametro del atributo
					Class atributeClassType = null;
					// asi puedo crear de forma abstracta un nuevo objeto del modelo, conociendo
					// solo el nombre de la clase
					// creo el objeto del elemento que deseo introducir

					Class pfcl = Class.forName(EMFOperationsUtil.getMetaModelPackage().getName() + "."
							+ packageNameNormalized + "Factory");
					Object obj = pfcl.getMethod("create" + nameNormalized).invoke(factory);
					// pillo la clase del objeto que deseo crear
					Class cl = Class.forName(EMFOperationsUtil.getMetaModelPackage().getName() + "." + nameNormalized);
					// creo un array con todos los metodos que tiene la clase
					Method[] m = cl.getDeclaredMethods();
					// recorro los metodos
					for (int z = 0; z < m.length; z++) {
						// System.out.println(m[z].getParameterCount());
						// filtro por aquellos que tienen un parametro de entrada
						if (m[z].getParameterCount() > 0) {
							// filtro por aquel que es un set y encima el nombre es el del parametro mio
							if (m[z].getName().equals("set" + atributeNameNormalized)) {
								// pilla la clase del parametro/s que recibe el metodo
								Class<?>[] pType = m[z].getParameterTypes();
								// Type[] gpType = m[z].getGenericParameterTypes();
								// recorro todas las clases de los parametros de entrada de mi metodo
								for (int i = 0; i < pType.length; i++) {

									// System.out.println("ParameterType " + pType[i].toString());
									// capturo la clase de mi atributo
									atributeClassType = pType[i];
									// convierto mi objeto generico que era el atributeValue, en un objeto
									// especifico en base a la clase del atributo capturada
									atributeValue = EMFOperationsUtil.castAtributeValue(pType[i].toString(),
											atributeValue);

								}
							}
						}
					}
					// hago una llamada al set
					cl.getMethod("set" + atributeNameNormalized, atributeClassType).invoke(obj, atributeValue);
					// System.out.println(cl.getMethod("get" + atributeNameNormalized).invoke(obj));
					// lo introduzco en mi modelo
					inst_resource.getContents().add((EObject) obj);
					
					Class pcl = Class.forName(EMFOperationsUtil.getMetaModelPackage().getName() + "." + parentNameElementNormalized);
					Object parentObj=EMFOperationsUtil.getElementFromResource(parentNameElementNormalized, inst_resource);
					List list=(List) pcl.getMethod("get" + relationNameNormalized).invoke(parentObj);
					
					if(EMFOperationsUtil.getUpperBound(parentNameElement, relationName)>list.size()||EMFOperationsUtil.getUpperBound(parentNameElement, relationName)==-1) 
					{
						list.add((EObject) obj);
						System.out.println(EMFOperationsMessages.NEW_ELEMENT_ADDED_CORRECTLY);
					}else 
					{
						System.out.println("numero maximo ya introducido");
						inst_resource.getContents().remove((EObject)obj);
					}
					
					

					
				} catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException
						| SecurityException e) {
					
					e.printStackTrace();
				}
			}
		} else {
			System.out.println(EMFOperationsMessages.NEW_ELEMENT_ALREADY_EXITS);
		}
	   }else 
	   {
		   System.out.println(EMFOperationsMessages.NODE_NOT_EXIST);
	   }
	}
	
	public void addElement(String nameElement, String atributeName, Object atributeValue)
			throws IllegalAccessException, ClassNotFoundException {

		String packageNameNormalized = EMFOperationsUtil
				.normalizedString(EMFOperationsUtil.getMetaModelPackage().getName());
		String nameNormalized = EMFOperationsUtil.normalizedString(nameElement);
		String atributeNameNormalized = EMFOperationsUtil.normalizedString(atributeName);
		//System.out.println("AQUI "+EMFOperationsUtil.getElementFromResource(nameNormalized, atributeName, atributeValue,inst_resource));
		if (EMFOperationsUtil.getElementFromResource(nameNormalized, atributeName, atributeValue,inst_resource) == null) {
				//System.out.println("entree");
			if (EMFOperationsUtil.checkElementInsertion(nameElement, atributeName, atributeValue)) {

				try {
					// clase del parametro del atributo
					Class atributeClassType = null;
					// asi puedo crear de forma abstracta un nuevo objeto del modelo, conociendo
					// solo el nombre de la clase
					// creo el objeto del elemento que deseo introducir

					Class pfcl = Class.forName(EMFOperationsUtil.getMetaModelPackage().getName() + "."
							+ packageNameNormalized + "Factory");
					Object obj = pfcl.getMethod("create" + nameNormalized).invoke(factory);
					// pillo la clase del objeto que deseo crear
					Class cl = Class.forName(EMFOperationsUtil.getMetaModelPackage().getName() + "." + nameNormalized);
					// creo un array con todos los metodos que tiene la clase
					Method[] m = cl.getDeclaredMethods();
					// recorro los metodos
					for (int z = 0; z < m.length; z++) {
						// System.out.println(m[z].getParameterCount());
						// filtro por aquellos que tienen un parametro de entrada
						if (m[z].getParameterCount() > 0) {
							// filtro por aquel que es un set y encima el nombre es el del parametro mio
							if (m[z].getName().equals("set" + atributeNameNormalized)) {
								// pilla la clase del parametro/s que recibe el metodo
								Class<?>[] pType = m[z].getParameterTypes();
								// Type[] gpType = m[z].getGenericParameterTypes();
								// recorro todas las clases de los parametros de entrada de mi metodo
								for (int i = 0; i < pType.length; i++) {

									// System.out.println("ParameterType " + pType[i].toString());
									// capturo la clase de mi atributo
									atributeClassType = pType[i];
									// convierto mi objeto generico que era el atributeValue, en un objeto
									// especifico en base a la clase del atributo capturada
									atributeValue = EMFOperationsUtil.castAtributeValue(pType[i].toString(),
											atributeValue);

								}
							}
						}
					}
					// hago una llamada al set
					cl.getMethod("set" + atributeNameNormalized, atributeClassType).invoke(obj, atributeValue);
					// System.out.println(cl.getMethod("get" + atributeNameNormalized).invoke(obj));
					// lo introduzco en mi modelo
					inst_resource.getContents().add((EObject) obj);
					System.out.println(EMFOperationsMessages.NEW_ELEMENT_ADDED_CORRECTLY);

				} catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException
						| SecurityException e) {
					
					e.printStackTrace();
				}
			}
		} else {
			System.out.println(EMFOperationsMessages.NEW_ELEMENT_ALREADY_EXITS);
		}

	}

	public void saveModelInstance() {

		try {
			inst_resource.save(Collections.EMPTY_MAP);
			System.out.println(EMFOperationsMessages.MODEL_INSTANCE_SAVED);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}



}
