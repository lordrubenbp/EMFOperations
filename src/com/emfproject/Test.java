package com.emfproject;

import java.lang.reflect.InvocationTargetException;

public class Test {

	
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		
		 	/*ResourceSet rs = new ResourceSetImpl();
		    rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore", 
		    new XMIResourceFactoryImpl());
		    Resource res = rs.createResource( URI.createFileURI( 
		    "C:\\Users\\ruben\\EMF-workspace\\EMF-example\\model\\emfapi.ecore" ));
		    try {
				res.load(null);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		    EPackage metapackage = (EPackage)res.getContents().get(0);
		    System.out.println("meta Package "+metapackage.getName());
		    EFactory exampleFactoryInstance = metapackage.getEFactoryInstance();
		    EClass alumnoClass = (EClass)metapackage.getEClassifier("Alumno");
		    EObject alumnoObject = exampleFactoryInstance.create(alumnoClass);
		    
		    EAttribute alumnoName = alumnoClass.getEAllAttributes().get(0);
		    alumnoObject.eSet(alumnoName, "Ruben");
		    
		    ResourceSet resourseSet = new ResourceSetImpl();
		    resourseSet.getPackageRegistry().put(metapackage.getNsURI(), 
		    metapackage); 

		    resourseSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put
		    ("*", new XMIResourceFactoryImpl());
		    Resource resource = 
		    		resourseSet.createResource(URI.createURI("./model/Employee.xmi"));
		    resource.getContents().add(alumnoObject);
		    Map options = new HashMap();
		    options.put(XMIResource.OPTION_SCHEMA_LOCATION, Boolean.TRUE);
		    try 
		    {
		        resource.save(options);
		    } catch (IOException e) {
		        
		        e.printStackTrace();
		    }
		*/
		
		
		/* PARA CREAR UN MODELO INSTANCIADO
		EMFProjectPackage.eINSTANCE.eClass();
        // Retrieve the default factory singleton
		EMFProjectFactory factory = EMFProjectFactory.eINSTANCE;

        // create the content of the model via this program
        Alumno alumno = factory.createAlumno();
        alumno.setName("pepito");

        // As of here we preparing to save the model content

        // Register the XMI resource factory for the .website extension

        Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
        Map<String, Object> m = reg.getExtensionToFactoryMap();
        m.put("alumnos", new XMIResourceFactoryImpl());

        // Obtain a new resource set
        ResourceSet resSet = new ResourceSetImpl();

        // create a resource
        Resource resource = resSet.createResource(URI
                .createURI("alumnos/test.alumnos"));
        // Get the first model element and cast it to the right type, in my
        // example everything is hierarchical included in this first node
        resource.getContents().add(alumno);

        // now save the content.
        try {
            resource.save(Collections.EMPTY_MAP);
        } catch (IOException e) {
          
            e.printStackTrace();
        }
		*/
		
		
		/*//PARA LEER UN MODELO XMI INSTANCIADO Y A�ADIR UNA NUEVA INSTANCIA
		ResourceSet resourceSet = new ResourceSetImpl();

        // register UML
		EMFProjectPackage.eINSTANCE.eClass();

        // Register XML resource as UMLResource.Factory.Instance
        Map extensionFactoryMap = Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap();
        extensionFactoryMap.put("xmi", new XMIResourceFactoryImpl());

        Resource resource = (Resource) resourceSet.createResource(URI.createFileURI("maquina/test.xmi"));
        System.out.println(resource.getContents().size());
        
        
        try {
        	//no puedo saber si el archivo contiene alguna instancia hasta que lo intento cargar
        	//aqui deberia meter alguna variable de control para, si en el caso de que contenga alguna instancia, la almacene
			resource.load(null);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
        for (int i= 0; i <resource.getContents().size(); i++)
        {    	
        	
        	//System.out.println(resource.getContents().get(i));
        	
        	
        }
      
        State state = (State) resource.getContents().get(1);
        state.setActions(null);
        //state.setName("pepeluis");
        EMFProjectFactory factory = EMFProjectFactory.eINSTANCE;
        //Command prueba= factory.createCommand();
        //prueba.setName("prueba_command");
        //resource.getContents().add(prueba);
        //state.getActions().add(prueba);
       // System.out.println(state.getActions().clear(););
        //alumno.setName("juanito");
        //EMFProjectFactory factory = EMFProjectFactory.eINSTANCE;

        // create the content of the model via this program
        //Alumno alumno2 = factory.createAlumno();
        //alumno2.setName("ruben");
        
        
        try {
			resource.save(Collections.EMPTY_MAP);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
        

	
		*/
		

		EMFOperations op= new EMFOperations();
		

		
		op.createModelInstance("maquina/prueba12.xmi");
		op.createElement("state","name","pepe");
		
		
		op.saveModelInstance();
		
		
		}

	}


