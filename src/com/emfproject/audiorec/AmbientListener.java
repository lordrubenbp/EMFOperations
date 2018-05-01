package com.emfproject.audiorec;

import javax.sound.sampled.AudioFileFormat;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.emfproject.audiorec.*;
import com.google.cloud.speech.v1p1beta1.RecognitionAudio;
import com.google.cloud.speech.v1p1beta1.RecognitionConfig;
import com.google.cloud.speech.v1p1beta1.RecognizeResponse;
import com.google.cloud.speech.v1p1beta1.SpeechClient;
import com.google.cloud.speech.v1p1beta1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1p1beta1.SpeechRecognitionResult;
import com.google.cloud.speech.v1p1beta1.RecognitionConfig.AudioEncoding;
import com.google.protobuf.ByteString;

import net.sourceforge.javaflacencoder.FLACFileWriter;

/**
 * @author MasterEjay
 */
public class AmbientListener {

	static RecognitionConfig config = null;

	public static void main(String args[]) throws Exception {
		config = RecognitionConfig.newBuilder().setEncoding(AudioEncoding.LINEAR16).setSampleRateHertz(8000)
				.setLanguageCode("es-ES").build();
		ambientListening();
	}

	private static final MicrophoneAnalyzer microphoneAnalyzer = new MicrophoneAnalyzer(FLACFileWriter.FLAC);

	public static void ambientListening() throws Exception {
		File file = new File("./resources/test11.flac");
		int THRESHOLD = 30;
		boolean isSpeaking = true;
		//Thread.sleep(1000);
		System.out.println("Habla.");
		microphoneAnalyzer.open();
		microphoneAnalyzer.captureAudioToFile(file);
		//Thread.sleep(500);
	
		
		int numberOfSilents = 0;

		
		while(isSpeaking) {
		
		//microphoneAnalyzer.close();
		
		
		//System.out.println(microphoneAnalyzer.getState());
	
				//System.out.println(microphoneAnalyzer.getAudioFile().length());
				//System.out.println(microphoneAnalyzer.getState());
				//System.out.println(microphoneAnalyzer.getAudioVolume());
				if (microphoneAnalyzer.getAudioVolume() < THRESHOLD) {

					numberOfSilents++;
					System.out.println("Silencios..." + numberOfSilents);
					if (numberOfSilents >= 10) {
						isSpeaking = false;
						microphoneAnalyzer.close();//mirar esto
						System.out.println("Recording stopped.");
						try {
							SpeechClient speechClient = SpeechClient.create();
							// The path to the audio file to transcribe
							String fileName = "./resources/test11.wav";
							//System.out.println("bep");
							// Reads the audio file into memory
							Path path = Paths.get(fileName);
							byte[] data = Files.readAllBytes(path);
							ByteString audioBytes = ByteString.copyFrom(data);

							// Builds the sync recognize request
							if (audioBytes.size() != 0) {
								RecognitionAudio audio = RecognitionAudio.newBuilder().setContent(audioBytes).build();

								//System.out.println("bep3");

								// Performs speech recognition on the audio file
								RecognizeResponse response = speechClient.recognize(config, audio);
								//System.out.println("bep4");
								List<SpeechRecognitionResult> results = response.getResultsList();
								//System.out.println(results);

								for (SpeechRecognitionResult result : results) {
									// There can be several alternative transcripts for a given chunk of speech.
									// Just use the
									// first (most likely) one here.
									SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
									System.out.printf("Transcription: %s%n", alternative.getTranscript());
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						//ambientListening();

					}
				}else 
				{
					numberOfSilents=0;
					System.out.println("Silencios prueba..." + numberOfSilents);
				}
			} // while{}
		
		
		}
	}
	
