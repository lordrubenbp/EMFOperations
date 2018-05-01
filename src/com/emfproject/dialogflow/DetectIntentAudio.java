/*
  Copyright 2017, Google, Inc.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/

package com.emfproject.dialogflow;

import com.emfproject.EMFOperations;
import com.emfproject.audiorec.MicrophoneGestor;
// [START dialogflow_import_libraries]
// Imports the Google Cloud client library
import com.google.cloud.dialogflow.v2.AudioEncoding;
import com.google.cloud.dialogflow.v2.Context;
import com.google.cloud.dialogflow.v2.DetectIntentRequest;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.InputAudioConfig;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.QueryParameters;
import com.google.cloud.dialogflow.v2.QueryResult;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.protobuf.ByteString;
import com.google.protobuf.Value;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;
// [END dialogflow_import_libraries]


/**
 * DialogFlow API Detect Intent sample with audio files.
 */
public class DetectIntentAudio {

	 static MicrophoneGestor micg= new MicrophoneGestor();
  // [START dialogflow_detect_intent_audio]
  /**
   * Returns the result of detect intent with an audio file as input.
   *
   * Using the same `session_id` between requests allows continuation of the conversation.
   * @param projectId Project/Agent Id.
   * @param audioFilePath Path to the audio file.
   * @param sessionId Identifier of the DetectIntent session.
   * @param languageCode Language code of the query.
   */
	
  public static void detectIntentAudio(String projectId, String audioFilePath, String sessionId,
      String languageCode)
      throws Exception {
    // Instantiates a client
    try (SessionsClient sessionsClient = SessionsClient.create()) {
      // Set the session name using the sessionId (UUID) and projectID (my-project-id)
      SessionName session = SessionName.of(projectId, sessionId);
      System.out.println("Session Path: " + session.toString());

      // Note: hard coding audioEncoding and sampleRateHertz for simplicity.
      // Audio encoding of the audio content sent in the query request.
      AudioEncoding audioEncoding = AudioEncoding.AUDIO_ENCODING_LINEAR_16;
      int sampleRateHertz = 16000;

      // Instructs the speech recognizer how to process the audio content.
      InputAudioConfig inputAudioConfig = InputAudioConfig.newBuilder()
          .setAudioEncoding(audioEncoding) // audioEncoding = AudioEncoding.AUDIO_ENCODING_LINEAR_16
          .setLanguageCode(languageCode) // languageCode = "en-US"
          .setSampleRateHertz(sampleRateHertz) // sampleRateHertz = 16000
          .build();

      // Build the query with the InputAudioConfig
      QueryInput queryInput = QueryInput.newBuilder().setAudioConfig(inputAudioConfig).build();

  	Context context= Context.newBuilder().setName("projects/newagent-31936/agent/sessions/"+sessionId+"/contexts/listar-bloc-followup").setLifespanCount(2).build();

      while(true) 
	    {
	    	audioFilePath=micg.recordAudio();
	    	
	   
      // Read the bytes from the audio file
      byte[] inputAudio = Files.readAllBytes(Paths.get(audioFilePath));

   
	QueryParameters parameters=QueryParameters.newBuilder().addContexts(context).build();
	// Build the DetectIntentRequest
	DetectIntentRequest request = DetectIntentRequest.newBuilder()
          .setSession(session.toString())
          .setQueryInput(queryInput)
          .setInputAudio(ByteString.copyFrom(inputAudio))
          .setQueryParams(parameters)
          .build();
      
      

      // Performs the detect intent request
      DetectIntentResponse response = sessionsClient.detectIntent(request);

      // Display the query result
      QueryResult queryResult = response.getQueryResult();
      System.out.println("====================");
      System.out.format("Query Text: '%s'\n", queryResult.getQueryText());
      System.out.format("Detected Intent: %s (confidence: %f)\n",
          queryResult.getIntent().getDisplayName(), queryResult.getIntentDetectionConfidence());
      System.out.format("Fulfillment Text: '%s'\n", queryResult.getFulfillmentText());
      //System.out.format("Contexts: '%s'\n", queryResult.getOutputContextsList());
      //System.out.format("Parameters: '%s'\n", queryResult.getParameters().getFieldsMap());
      System.out.format("Actions: '%s'\n", queryResult.getAction());
      
    
      String element=queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
      String atribute=queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
      String value=queryResult.getParameters().getFieldsMap().get("value").getStringValue().toLowerCase();
      System.out.format(queryResult.getParameters().getFieldsMap().get("element").getStringValue());
      System.out.format(queryResult.getParameters().getFieldsMap().get("atribute").getStringValue());
      System.out.format(queryResult.getParameters().getFieldsMap().get("value").getStringValue());
      EMFOperations op= new EMFOperations();
	
      if (element.equals("estado")) 
      {
    	  element="state";
      }
      if (atribute.equals("nombre")) 
      {
    	  atribute="name";
      }
      
	  op.loadModelInstance("maquina/testDialog.xmi");
	  op.addElement(element, atribute, value);
	  //op.addElement("state","name","sleep2");
	  op.saveModelInstance();
      /*for (Map.Entry<String, Value> entry : queryResult.getParameters().getFieldsMap().entrySet())
      {
    	
          System.out.println(entry.getKey() + "/" + entry.getValue().getStringValue());
      }*/
      for (int i=0;i<queryResult.getOutputContextsList().size();i++) 
      {
    	  context=queryResult.getOutputContextsList().get(i);
      }
	    }
     
    }
  }
  // [END dialogflow_detect_intent_audio]

  // [START run_application]
  public static void main(String[] args) throws Exception {
	  String audioFilePath = "1";
	    String projectId = "emf-api";
	    String sessionId = UUID.randomUUID().toString();
	    String languageCode = "es-ES";//en-US
	    
	  
	   detectIntentAudio(projectId, audioFilePath, sessionId, languageCode);
	   
   
  }
  // [END run_application]
}
