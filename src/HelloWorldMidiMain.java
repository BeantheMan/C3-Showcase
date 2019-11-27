/*
 * c2017-2019 Courtney Brown 
 * 
 * Class: H
 * Description: Demonstration of MIDI file manipulations, etc. & 'MelodyPlayer' sequencer
 * 
 */

import processing.core.*;

import java.util.*; 

//importing the JMusic stuff
import jm.music.data.*;
import jm.JMC;
import jm.util.*;
import jm.midi.*;

import java.io.UnsupportedEncodingException;
import java.net.*;

import javax.sound.midi.*;
import themidibus.*;

			//make sure this class name matches your file name, if not fix.
public class HelloWorldMidiMain extends PApplet {

	MelodyPlayer player; //play a midi sequence
	MidiFileToNotes midiNotes; //read a midi file
	MarkovGenerator<Integer> predictP = new MarkovGenerator<Integer>();
	MarkovGenerator<Double> predictR = new MarkovGenerator<Double>();
	ArrayList<Integer> pitches = new ArrayList<Integer>();
	ArrayList<Integer> pitch = new ArrayList<Integer>();
	ArrayList<Integer> rhythms = new ArrayList<Integer>();
	int instance = 0;
	
	
	float lastTimeKeyDown;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PApplet.main("HelloWorldMidiMain"); //change this to match above class & file name 		System.out.println(midiNotes.getPitchArray());		System.out.println(midiNotes.getPitchArray());
	}

	MidiBus myBus; // The MidiBus
	PImage img;
	
	public void settings() {
		size(970, 450);
	}

	public void setup() {
		img = loadImage("MUSIC.jpg");
		background(0);
		noStroke();
		MidiBus.list(); // List all available Midi devices on STDOUT. This will show each device's index and name.
		myBus = new MidiBus(this, 0, 1);
	  
		// Either you can
	  //                   Parent In Out
	  //                     |    |  |
	  //myBus = new MidiBus(this, 0, 1); // Create a new MidiBus using the device index to select the Midi input and output devices respectively.

	  // or you can ...
	  //                   Parent         In                   Out
	  //                     |            |                     |
	  //myBus = new MidiBus(this, "IncomingDeviceName", "OutgoingDeviceName"); // Create a new MidiBus using the device names to select the Midi input and output devices respectively.

	  // or for testing you could ...
	  //                 Parent  In        Out
	  //                   |     |          |
	  //myBus = new MidiBus(this, -1, "Java Sound Synthesizer"); // Create a new MidiBus with no input device and the default Java Sound Synthesizer as the output device.
	}

	public void draw() {
		image(img, 0, 0);
		textSize(30);
		fill(255, 255, 0);
		text("Press '1' to train melody", 520, 180);
		if (instance == 1) {
			  int channel = 0;
			  int velocity = 127;
			  int number = 0;
			  int value = 9;
			  for (int i = 0; i < pitch.size(); i++) {
				  myBus.sendNoteOn(channel, pitch.get(i), velocity); // Send a Midi noteOn
				  delay(150);
				  myBus.sendNoteOff(channel, pitch.get(i), 0); // Send a Midi nodeOff
				  myBus.sendControllerChange(channel, number, value); // Send a controllerChange
				  delay(rhythms.get(i));
			  }
			  pitch.clear();
			  pitches.clear();
			  rhythms.clear();
			  instance = 0;
		}
//	  int channel = 0;
//	  int pitch = 64;
//	  int velocity = 127;
//	  //int[] notes = {64,76,73,71,68,71,66,64,76,73,71,68,71,73,72,73,68,69,71,73,69,66,73,73,73,75,76,78,75,73,71,68,66,64,76,73,71,68,71,66,66,64,66,68,69,71,73,73,75,76,76,76,75,73,71,70,71,73,75,76};
//	  //int[] delay = {800,400,400,400,400,1200,1200,800,400,400,400,400,2400,400,400,400,400,400,400,800,400,1200,800,400,400,400,400,400,400,400,400,400,400,800,400,400,400,400,1200,800,400,800,400,400,400,400,1600,400,400,1200,1200,400,400,400,400,400,400,1200,1200,2400};
//	  
//	  
//	  //for (int n = 0; n < notes.length; n++ ) { //transpose to F major
////		  notes[n] = notes[n] - 4;
////		  delay[n] = delay[n]/2;
////	  }
//	  
//	  //for (int i = 0; i < notes.length; i++ ) {
//	  //pitch = notes[i];
//	  myBus.sendNoteOn(channel, pitch, velocity); // Send a Midi noteOn
//	  delay(50);
//	  myBus.sendNoteOff(channel, pitch, velocity); // Send a Midi nodeOff
//
//	  int number = 0;
//	  int value = 90;
//
//	  myBus.sendControllerChange(channel, number, value); // Send a controllerChange
//	  delay(50);
//	  //}
	}

	public void noteOn(int channel, int pitch, int velocity) {
	  // Receive a noteOn
	  println();
	  println("Note On:");
	  println("--------");
	  println("Channel:"+channel);
	  println("Pitch:"+pitch);
	  println("Velocity:"+velocity);
	  pitches.add(pitch);
	}

	public void noteOff(int channel, int pitch, int velocity) {
	  // Receive a noteOff
	  println();
	  println("Note Off:");
	  println("--------");
	  println("Channel:"+channel);
	  println("Pitch:"+pitch);
	  println("Veloacity:"+velocity);
	}

	public void controllerChange(int channel, int number, int value) {
	  // Receive a controllerChange
//	  println();
//	  println("Controller Change:");
//	  println("--------");
//	  println("Channel:"+channel);
//	  println("Number:"+number);
//	  println("Value:"+value);
	}

	public void delay(int time) {
	  int current = millis();
	  while (millis () < current+time) Thread.yield();
	}
	
	public void keyPressed() {
		if (key == '1') {
			ProbabilityGenerator<Integer> probGen = new ProbabilityGenerator<Integer>();
			ProbabilityGenerator<Integer> probGen2 = new ProbabilityGenerator<Integer>();
			MarkovGeneratorOrderN<Integer> markGen = new MarkovGeneratorOrderN<Integer>();
			
			int[] rhythm = {200,200,200,200,200,200,200,200,200,200,100,100,100,100,100,400,400,400,400,400};
			int[] notes = {64,76,73,71,68,71,66,64,76,73,71,68,71,73,72,73,68,69,71,73,69,66,73,73,73,75,76,78,75,73,71,68,66,64,76,73,71,68,71,66,66,64,66,68,69,71,73,73,75,76,76,76,75,73,71,70,71,73,75,76};
			int[] delay = {800,400,400,400,400,1200,1200,800,400,400,400,400,2400,400,400,400,400,400,400,800,400,1200,800,400,400,400,400,400,400,400,400,400,400,800,400,400,400,400,1200,800,400,800,400,400,400,400,1600,400,400,1400,1400,400,400,400,400,400,400,1400,1400,2400};
			
			System.out.println(notes.length);
			for (int i = 0; i < rhythm.length; i++) {
				rhythms.add(rhythm[i]);
			} 
			
			if (pitches.isEmpty()) {
				rhythms.clear();
				  for (int n = 0; n < notes.length; n++) {
					  pitch.add(notes[n] - 4);
					  rhythms.add(delay[n]/2);
				  }
			}
			else if (!pitches.isEmpty()){
			
				probGen2.train(rhythms);
				
				markGen.SetOrder(1);
				markGen.train(pitches);
				//ArrayList<Double> rhythms = new ArrayList<Double>();
				//System.out.print(pitches);
				
	//			for (int i = 0; i < pitches.size(); i++) {
	//				rhythms.add(1.0);
	//			}
				ArrayList<Integer> song = markGen.getGeneration();
				rhythms = probGen2.generate(20);
				
				//System.out.println(song);
				pitch = song;
			}
			
			instance = 1;
		}
		if (key == '9') {
			pitches.clear();
		}
	}

	
}

