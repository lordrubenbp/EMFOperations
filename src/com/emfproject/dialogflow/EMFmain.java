package com.emfproject.dialogflow;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.UUID;

import javax.sound.sampled.LineUnavailableException;

import org.omg.CORBA.Environment;

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
import com.google.cloud.dialogflow.v2.SessionsSettings;
import com.google.cloud.dialogflow.v2.TextInput;
import com.google.cloud.dialogflow.v2.TextInput.Builder;
import com.google.cloud.dialogflow.v2.stub.SessionsStubSettings;
import com.google.protobuf.ByteString;
import com.google.protobuf.Value;

public class EMFmain {

	public static String audioFilePath = "";
	public static String projectId = "emf-api";
	public static String sessionId = UUID.randomUUID().toString();
	public static String languageCode = "es-ES";// en-US
	public static SessionName session = null;
	public static SessionsClient sessionsClient = null;
	public static EMFDialogflowParse parse = null;
	public static final String RESET_CONTEXT_QUERY="reset all context";
	public static void main(String args[]) throws IOException {

		try {
			//String value = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
			//System.out.print(value);
			parse = new EMFDialogflowParse();
			sessionsClient = SessionsClient.create();
			// Set the session name using the sessionId (UUID) and projectID (my-project-id)
			session = SessionName.of(projectId, sessionId);

			//audioClient();
			textClient(false);

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public static void printQueryResultInfo(QueryResult queryResult) {
		// Display the query result

		System.out.println("[DEBUG] ====================");
		System.out.format("[DEBUG] Query Text: '%s'\n", queryResult.getQueryText());
		System.out.format("[DEBUG] Detected Intent: %s (confidence: %f)\n", queryResult.getIntent().getDisplayName(),
				queryResult.getIntentDetectionConfidence());
		System.out.format("[DEBUG] Fulfillment Text: '%s'\n", queryResult.getFulfillmentText());
		System.out.format("[DEBUG] Contexts: '%s'\n", queryResult.getOutputContextsList());
		// System.out.format("Parameters: '%s'\n",
		// queryResult.getParameters().getFieldsMap());
		System.out.format("[DEBUG] Actions: '%s'\n", queryResult.getAction());
		// System.out.println("Parameters required: " + queryResult.ACTION_FIELD_NUMBER
		// + "");
		// System.out.format("Parameters passed: '%s'\n",
		// queryResult.getAllRequiredParamsPresent());
		// System.out.format("Parameters : '%s'\n", queryResult.getParameters());
		// System.out.println("Parameters number passed:
		// "+queryResult.getParameters().getFieldsMap());

	}

	public static void textClient(boolean debug) throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {

		Scanner sc = new Scanner(System.in);
		/*Context context = Context.newBuilder()
				.setName("projects/newagent-31936/agent/sessions/" + sessionId + "/contexts/_empty_")
				.setLifespanCount(2).build();
		QueryParameters parameters = QueryParameters.newBuilder().addContexts(context).build();*/
		EMFAdvices.showAdvice("START");
		String text;
		while (true) {
			
			System.out.print("[INPUT] Consulta: ");
			if (parse.getModelLoadedStatus()) {
				text = sc.nextLine();
			} else {
				parse.resetModelLoadedStatus();
				text = RESET_CONTEXT_QUERY;
			}
			Builder textInput = TextInput.newBuilder().setText(text).setLanguageCode(languageCode);

			// Build the query with the TextInput
			QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();

			DetectIntentRequest request = DetectIntentRequest.newBuilder().setSession(session.toString())
					.setQueryInput(queryInput).build();
			// Performs the detect intent request
			DetectIntentResponse response = sessionsClient.detectIntent(request);

			QueryResult queryResult = response.getQueryResult();

			if(debug==true) 
			{
				printQueryResultInfo(queryResult);

			}

			if (queryResult.getAllRequiredParamsPresent()) {
				parse.parseCode(queryResult, queryResult.getAction());
			}

			// if(parse.getModelLoadedStatus()) {
			// System.out.println("entre");
			// for (int i = 0; i < queryResult.getOutputContextsList().size(); i++) {
			// context = queryResult.getOutputContextsList().get(i);

			// }
			// }else
			// {

			// context = Context.newBuilder()
			// .setName("projects/emf-api/agent/sessions/" + sessionId +
			// "/contexts/model_load")
			// .setLifespanCount(0).build();
			// }
		}

	}

	public static void audioClient() throws LineUnavailableException, IOException, IllegalAccessException,
			ClassNotFoundException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException, InstantiationException {
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

			QueryResult queryResult = response.getQueryResult();

			printQueryResultInfo(queryResult);

			if (queryResult.getAllRequiredParamsPresent()) {
				parse.parseCode(queryResult, queryResult.getAction());
			}

			for (int i = 0; i < queryResult.getOutputContextsList().size(); i++) {
				context = queryResult.getOutputContextsList().get(i);
			}
		}

	}

}
