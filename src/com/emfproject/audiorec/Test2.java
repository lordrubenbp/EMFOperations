package com.emfproject.audiorec;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import javax.sound.sampled.AudioFileFormat;

import com.google.cloud.speech.v1p1beta1.RecognitionAudio;
import com.google.cloud.speech.v1p1beta1.RecognitionConfig;
import com.google.cloud.speech.v1p1beta1.RecognizeResponse;
import com.google.cloud.speech.v1p1beta1.SpeechClient;
import com.google.cloud.speech.v1p1beta1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1p1beta1.SpeechRecognitionResult;
import com.google.cloud.speech.v1p1beta1.RecognitionConfig.AudioEncoding;
import com.google.protobuf.ByteString;

import net.sourceforge.javaflacencoder.FLACFileWriter;

public class Test2 {
	private final static MicrophoneAnalyzer microphone = new MicrophoneAnalyzer(AudioFileFormat.Type.WAVE);
	static RecognitionConfig config=null;


	public static void main(String[] args){
		
		 config = RecognitionConfig.newBuilder()
		          .setEncoding(AudioEncoding.LINEAR16)
		          .setSampleRateHertz(16000)
		          .setLanguageCode("es-ES")
		          .build();
		ambientListening();
	}

	public static void ambientListening(){
		String filename = "./resources/test3.wav";
		try{
			microphone.captureAudioToFile(filename);
		}
		catch(Exception ex){
			ex.printStackTrace();
			return;
		}
		final int SILENT = microphone.getAudioVolume();
		boolean hasSpoken = false;
		boolean[] speaking = new boolean[10];
		Arrays.fill(speaking, false);
		for(int i = 0; i<100; i++){
			for(int x = speaking.length-1; x>1; x--){
				speaking[x] = speaking[x-1];
			}
			int frequency = microphone.getFrequency();
			int volume = microphone.getAudioVolume();
			speaking[0] = frequency<255 && volume>SILENT && frequency>85;
			System.out.println(speaking[0]);
			boolean totalValue = false;
			for(boolean bool: speaking){
				totalValue = totalValue || bool;
			}
			//if(speaking[0] && speaking[2] && speaking[3] && microphone.getAudioVolume()>10){
			if(totalValue && microphone.getAudioVolume()>20){	
				hasSpoken = true;
			}
			if(hasSpoken && !totalValue){
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
		}
		if(hasSpoken){
			microphone.close();
	    //Recognizer rec = new Recognizer(Recognizer.Languages.ENGLISH_US);
	    //GoogleResponse out = rec.getRecognizedDataForWave(filename);
			System.out.println("Hablando");
			 try{
		    	  SpeechClient speechClient=SpeechClient.create();
		      // The path to the audio file to transcribe
		      //String fileName = "./resources/wav.test";
		      System.out.println("bep");
		      // Reads the audio file into memory
		      Path path = Paths.get(filename);
		      byte[] data = Files.readAllBytes(path);
		      ByteString audioBytes = ByteString.copyFrom(data);
		    
		      // Builds the sync recognize request
		      if (audioBytes.size()!=0) {
		      RecognitionAudio audio = RecognitionAudio.newBuilder()
		          .setContent(audioBytes)
		          .build();
		      
		      System.out.println("bep3");

		      // Performs speech recognition on the audio file
		      RecognizeResponse response = speechClient.recognize(config, audio);
		      System.out.println("bep4");
		      List<SpeechRecognitionResult> results = response.getResultsList();
		      System.out.println(results);

		      for (SpeechRecognitionResult result : results) {
		        // There can be several alternative transcripts for a given chunk of speech. Just use the
		        // first (most likely) one here.
		        SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
		        System.out.printf("Transcription: %s%n", alternative.getTranscript());
		      }
		      }
		      }
		      catch(Exception e) 
		      {
		    	  e.printStackTrace();
		      }
		}
		ambientListening();
	}
}