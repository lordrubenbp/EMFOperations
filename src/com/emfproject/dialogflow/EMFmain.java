package com.emfproject.dialogflow;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import javax.sound.sampled.LineUnavailableException;

import com.emfproject.EMFOperations;
import com.emfproject.audiorec.MicrophoneGestor;
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

public class EMFmain {

	public static String audioFilePath = "";
	public static String projectId = "emf-api";
	public static String sessionId = UUID.randomUUID().toString();
	public static String languageCode = "es-ES";// en-US
	public static SessionName session = null;
	public static SessionsClient sessionsClient = null;

	public static void main(String args[]) throws IOException {

		try {
			sessionsClient = SessionsClient.create();
			// Set the session name using the sessionId (UUID) and projectID (my-project-id)
			session = SessionName.of(projectId, sessionId);

			audioClient();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public static void textClient() {

	}

	public static void audioClient()
			throws LineUnavailableException, IOException, IllegalAccessException, ClassNotFoundException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		MicrophoneGestor micg = new MicrophoneGestor();
		AudioEncoding audioEncoding = AudioEncoding.AUDIO_ENCODING_LINEAR_16;
		int sampleRateHertz = 16000;

		// Instructs the speech recognizer how to process the audio content.
		InputAudioConfig inputAudioConfig = InputAudioConfig.newBuilder().setAudioEncoding(audioEncoding) // audioEncoding
																											// =
																											// AudioEncoding.AUDIO_ENCODING_LINEAR_16
				.setLanguageCode(languageCode) // languageCode = "en-US"
				.setSampleRateHertz(sampleRateHertz) // sampleRateHertz = 16000
				.build();

		// Build the query with the InputAudioConfig
		QueryInput queryInput = QueryInput.newBuilder().setAudioConfig(inputAudioConfig).build();

		Context context = Context.newBuilder()
				.setName("projects/newagent-31936/agent/sessions/" + sessionId + "/contexts/listar-bloc-followup")
				.setLifespanCount(2).build();

		while (true) {
			
			audioFilePath = micg.recordAudio();

			// Read the bytes from the audio file
			byte[] inputAudio = Files.readAllBytes(Paths.get(audioFilePath));

			QueryParameters parameters = QueryParameters.newBuilder().addContexts(context).build();
			// Build the DetectIntentRequest
			DetectIntentRequest request = DetectIntentRequest.newBuilder().setSession(session.toString())
					.setQueryInput(queryInput).setInputAudio(ByteString.copyFrom(inputAudio)).setQueryParams(parameters)
					.build();

			// Performs the detect intent request
			DetectIntentResponse response = sessionsClient.detectIntent(request);

			// Display the query result
			QueryResult queryResult = response.getQueryResult();
			System.out.println("====================");
			System.out.format("Query Text: '%s'\n", queryResult.getQueryText());
			System.out.format("Detected Intent: %s (confidence: %f)\n", queryResult.getIntent().getDisplayName(),
					queryResult.getIntentDetectionConfidence());
			System.out.format("Fulfillment Text: '%s'\n", queryResult.getFulfillmentText());
			// System.out.format("Contexts: '%s'\n", queryResult.getOutputContextsList());
			// System.out.format("Parameters: '%s'\n",
			// queryResult.getParameters().getFieldsMap());
			System.out.format("Actions: '%s'\n", queryResult.getAction());
			System.out.println("Parameters required: "+queryResult.ACTION_FIELD_NUMBER+"");
			System.out.format("Parameters passed: '%s'\n", queryResult.getAllRequiredParamsPresent());
			
			if(queryResult.getAllRequiredParamsPresent()) {
			EMFDialogflowParse.parseCode(queryResult, queryResult.getAction());
			}

			/*String element = queryResult.getParameters().getFieldsMap().get("element").getStringValue().toLowerCase();
			String atribute = queryResult.getParameters().getFieldsMap().get("atribute").getStringValue().toLowerCase();
			String value = queryResult.getParameters().getFieldsMap().get("value").getStringValue().toLowerCase();
			System.out.format(queryResult.getParameters().getFieldsMap().get("element").getStringValue());
			System.out.format(queryResult.getParameters().getFieldsMap().get("atribute").getStringValue());
			System.out.format(queryResult.getParameters().getFieldsMap().get("value").getStringValue());
			EMFOperations op = new EMFOperations();

			if (element.equals("estado")) {
				element = "state";
			}
			if (atribute.equals("nombre")) {
				atribute = "name";
			}

			op.loadModelInstance("maquina/testDialog.xmi");
			op.addElement(element, atribute, value);
			// op.addElement("state","name","sleep2");
			op.saveModelInstance();*/
			/*
			 * for (Map.Entry<String, Value> entry :
			 * queryResult.getParameters().getFieldsMap().entrySet()) {
			 * 
			 * System.out.println(entry.getKey() + "/" + entry.getValue().getStringValue());
			 * }
			 */
			for (int i = 0; i < queryResult.getOutputContextsList().size(); i++) {
				context = queryResult.getOutputContextsList().get(i);
			}
		}

	}

}
