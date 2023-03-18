/**
 * 
 */
package sokoban3d;

/**
 * @author walter2
 *
 */
///////////////////////////////////////////////////////////////////////////////////
//Sokoban3D                                                    /
//Copyright (C) 2002 Nurdogan Erdem                                               /
//Contact: http://www.nurdogan.de | nurdogan@t-online.de                          /
///////////////////////////////////////////////////////////////////////////////////
//This program is free software; you can redistribute it and/or modify            /
//it under the terms of the GNU General Public License as published by            /
//the Free Software Foundation; either version 2 of the License, or               /
//(at your option) any later version. This program is distributed in              /
//the hope that it will be useful, but WITHOUT ANY WARRANTY; without              /
//even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR        /
//PURPOSE. See the GNU General Public License for more details. You should        /
//have received a copy of the GNU General Public License along with this          /
//program; if not, write to the Free Software Foundation, Inc., 59 Temple Place,  /
//Suite 330, Boston, MA 02111-1307, USA.                                          /
///////////////////////////////////////////////////////////////////////////////////



import javax.sound.midi.*;
//import java.util.*;
import java.io.*;

/**#
 * 
 * class to load sound files an play sound
 *
 */
class Music 
{
	Sequence midi;
	Sequencer player;
	boolean soundcard=true;
	protected Class mainClass;

	public Music()
	{
	}

	public void setClass(Class mC)
	{
		mainClass = mC;
	}

	/**
	 * 
	 * @param sndfile String name of the soundfile
	 */
	public void initSound( String sndfile )
	{
		java.net.URL url = null;
		try {

		try { 
			url = new java.net.URL( mainClass.getResource("Gui.class"),  "sound/" + sndfile );
			//System.out.println("Sound url = " + url.toString());
		} catch( java.net.MalformedURLException e )  { System.err.println( e.getMessage() ); }
		midi = MidiSystem.getSequence( url );
		player = MidiSystem.getSequencer();
		player.open();
		player.setSequence( midi );
		MetaEventListener listener = new MetaEventListener() 
		{
			public void meta( MetaMessage event )
			{
				if( event.getType() == 47 )
				{
					restartSound();
					startSound();
				}
			}
		};
		player.addMetaEventListener( listener );
	} catch( IOException e ) { System.out.println("Could not open MIDI-File"); }
	catch( InvalidMidiDataException e ) { System.out.println("Invalid Midi Data");	}
	catch( MidiUnavailableException e ) { soundcard=false; }
}

	// stoppt die Musik
	/**
	 * stop playing sound
	 */
	public void stopSound() 
	{
		if(soundcard)
		{
			player.stop();
		}
	}

	//startet die Musik
	/**
	 * start playing sound
	 */
	void startSound() 
	{
		if(soundcard)
		{
			player.start();
		}
	}

	//spult die Musik zu Anfang zurück
	/**
	 * st player to the sound begin
	 */
	void restartSound() 
	{
		if(soundcard)
		{
			player.setTickPosition( 0 );
		}
	}
}

