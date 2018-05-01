package com.emfproject.audiorec;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Scanner;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.LineUnavailableException;

public class MicrophoneGestor {
	
	public MicrophoneGestor() 
	{
		
	}
	
	public String recordAudio() throws LineUnavailableException 
	{
		Scanner sc= new Scanner(System.in);
		boolean end=false;
		Microphone mic=new Microphone(AudioFileFormat.Type.WAVE);
		File audioFile= new File("./resources/grabacion.wav");
		while(!end) 
		{
			System.out.println("1: para grabar\n2: para parar");
			try {
			int opt=sc.nextInt();
			
			if(opt==1) 
			{
				System.out.println("Grabando...");
				mic.open();
				
				mic.captureAudioToFile(audioFile);
				
			}else if(opt==2) 
			{
				System.out.println("Terminado...");
				mic.close();
				end=true;
			}
			}catch(Exception e) 
			{
				System.out.println("Numero incorrecto");
				sc.nextLine();
			}
		}
		return audioFile.getAbsolutePath();
	}

}
