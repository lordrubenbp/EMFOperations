package com.emfproject.dialogflow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import com.google.cloud.dialogflow.v2.EntityTypeName;
import com.google.cloud.dialogflow.v2.EntityTypesClient;
import com.google.cloud.dialogflow.v2.EntityType.Entity;
import com.google.protobuf.Empty;

public class EMFMetalModelToDialogflow {

	public static String projectId = "emf-api-v2";
	public static String entityAttributeId ="aa5aff20-d7f8-4294-be5a-3b9f2621568f";
	public static String entityElementId = "46116d21-f814-41ad-b672-7009ccb329b4";
	public static String entityRelationshipId = "4bb5a53f-3168-45b9-8f1f-affddb5ef62c";
	
	
	public static void loadMetadataModelCollection(HashSet<String> listValues, String entityType) throws Exception 
	{
		String entityTypeId = "";
		ArrayList<String> synonyms = new ArrayList<String>();
		
		switch (entityType) 
		{
		case "attribute":
			entityTypeId=entityAttributeId;
			break;
		case "element":
			entityTypeId=entityElementId;
			break;
		case "relationship":
			entityTypeId=entityRelationshipId;
			break;
		}
		for(String value:listValues) 
		{
			synonyms.clear();
			createEntity(entityTypeId,value,synonyms);
		}
	}
	public static  void createEntity(String entityTypeId, String entityValue,
		      List<String> synonyms) throws Exception {
		    // Note: synonyms must be exactly [entityValue] if the
		    // entityTypeId's kind is KIND_LIST
		    if (synonyms.size() == 0) {
		      synonyms.add(entityValue);
		    }

		    // Instantiates a client
		    try (EntityTypesClient entityTypesClient = EntityTypesClient.create()) {
		      // Set the entity type name using the projectID (my-project-id) and entityTypeId (KINDS_LIST)
		      EntityTypeName name = EntityTypeName.of(projectId, entityTypeId);

		      // Build the entity
		      Entity entity = Entity.newBuilder()
		          .setValue(entityValue)
		          .addAllSynonyms(synonyms)
		          .build();

		      // Performs the create entity type request
		      Empty response = entityTypesClient.batchCreateEntitiesAsync(name,
		          Arrays.asList(entity)).get();
		      System.out.println("Entity created: " + entityValue);
		    }


		  }

}
