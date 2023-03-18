/*
 * Sokoban3D - Ambient2
 * Copyright (C) 2023 walter
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


 package sokoban3d;

 //import java.io.BufferedReader;
 //import java.io.File;
 import java.io.IOException;
 import java.io.InputStream;
 //import java.io.InputStreamReader;
 import java.io.BufferedInputStream;
 import javax.sound.sampled.AudioFormat;
 import javax.sound.sampled.AudioInputStream;
 import javax.sound.sampled.AudioSystem;
 import javax.sound.sampled.DataLine;
 import javax.sound.sampled.LineUnavailableException;
 import javax.sound.sampled.SourceDataLine;
 import javax.sound.sampled.UnsupportedAudioFileException;

 /**
  * 
  * Abient2<br>
  * reads midi resources and play sound
  * 
  */
public class Ambient2 
 {
     // size of the byte buffer used to read/write the audio stream
	
     
     private static final int BUFFER_SIZE = 1024; //4096;
     private String step = "sound/step.wav";
     private String thud = "sound/thud.wav";
     private String move = "sound/move.wav";
     
     /**
      * step is the sound played when player makes a step
      */
     
     public static java.net.URL stepURL;
     /**
      * thud is the sound played when player can`t move
      */
     public static java.net.URL thudURL;
     /**
      * move is the sound played when player moves a block
      */
     public static java.net.URL moveURL;
     
     
     /**
      * class constructor 
      */
     public Ambient2()
     {
         /*String audioFilePath  = "sound/step.wav";
         java.net.URL url = getClass().getResource(audioFilePath);
         if (url != null)
         {
             String pth = url.getPath();
             //WavSound player = new WavSound();
             player.play(pth); //audioFilePath);
         }
         else System.out.println("url null");*/
         for (int i = 1; i <=3; i++)
             setWavePaths(i);
          /*if (stepPath != null)
          {
                 /*WaveSound2 player = new WaveSound2();
                 player.play(stepPath);/
                 play(stepPath);
          }
          if (thudPath != null)
          {
                 /*WaveSound2 player = new WaveSound2();
                 player.play(thudPath);
                /
                 play(thudPath);
          }
          if (movePath != null)
          {
                 /*WaveSound2 player = new WaveSound2();
                 player.play(movePath);/
                 play(movePath);
          }*/
     }
     
     private void setWavePaths(int what)
     {
         java.net.URL url = null;
         Class c = getClass();
         switch (what)
         {
             case 1: 
                     url = getClass().getResource(step);
                     //url = getClass().getResource("sound/step.wav");
                         if (url != null) {
                             //System.out.println("step url = " + url.toString());
                             stepURL =  url; } //.getPath();}
                        break;
             case 2: url = getClass().getResource(thud);
                         if (url != null) 
                             thudURL =  url;
                        break;
             case 3: url = getClass().getResource(move);
                          if (url != null) 
                             moveURL =  url;
                        break;
         }
        /* if (url != null)
         {
             String pth = url.getPath();
             WavSound player = new WavSound();
             player.play(pth); //audioFilePath);
         }
         else System.out.println("url null");*/
     }
     
     /**
      * Play a given step audio file.
      * 
      */
     public static void playStep() //String woher)
     {
         //System.out.println(woher);
         if (stepURL != null)
         {
             Thread cSound1 = new Thread()
             {
                 public void run()
                 {
                     play(stepURL);
                 }
             };
             cSound1.start();
             //play(movePath)
         }
         else if (Gui.debug)System.out.println("Error: No step sound");
         
         
     }
     
     /**
      * Play a given blockmove  audio file.
      * 
      */
     
     public static void playBlockMove()
     {
         //System.out.println(woher);
         if (moveURL != null)
         {
             Thread cSound3 = new Thread()
             {
                 public void run()
                 {
                     play(moveURL);
                 }
             };
             cSound3.start();
             
         }
         else if (Gui.debug)System.out.println("Error: No Block Move Sound");
     }
     
     /**
      * Play a given thud audio file.
      * 
      */
     public static void playThud()
     {
         //System.out.println(woher);
         if (thudURL != null)
         {
             Thread cSound2 = new Thread()
             {
                 public void run()
                 {
                     play(thudURL);
                 }
             };
             cSound2.start();
             
         }
         else if (Gui.debug) System.out.println("Error: No thud sound");
     }
     
     /**
      * 
      * @param url
      * method to play sounds
      */
     public static void play(java.net.URL  url) 
     {
         //System.out.println("sU " + url.toString());
         try {
             InputStream dataInput = url.openStream();
             BufferedInputStream db = new BufferedInputStream(dataInput);
             AudioInputStream audioStream = AudioSystem.getAudioInputStream(db);
             
             AudioFormat format = audioStream.getFormat();
             DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
             SourceDataLine audioLine = (SourceDataLine) AudioSystem.getLine(info);
             audioLine.open(format);
             audioLine.start();
             //System.out.println("Playback started.");
              
             byte[] bytesBuffer = new byte[BUFFER_SIZE];
             int bytesRead = -1;
             while ((bytesRead = audioStream.read(bytesBuffer)) != -1) {
                 audioLine.write(bytesBuffer, 0, bytesRead);
             }
             dataInput.close();
             db.close();
             audioLine.drain();
             audioLine.close();
             audioStream.close();
             //System.out.println("Playback completed.");
              
         } catch (UnsupportedAudioFileException ex) {
             System.out.println("The specified audio file is not supported.");
             ex.printStackTrace();
         } catch (LineUnavailableException ex) {
             System.out.println("Audio line for playing back is unavailable.");
             ex.printStackTrace();
         } catch (IOException ex) {
             System.out.println("Error playing the audio file.");
             ex.printStackTrace();
         }      
     }
      
     /*public static void main(String[] args) 
     {
        new Ambient2();    
     }*/
 }