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

package sokoban3d;

import java.io.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.awt.*;

 /**
 * 
 * Sokoban3D<br>
 * extends javax.swing.JFrame<br>
 * loads resources and create the GamePanel
 * and used to start and exit the Game
 * 
 */

public class Sokoban3D extends JFrame
{
    Player player = new Player( 0, 0, 0 );
    String playername;
    GamePanel gamePanel;
    PlayerDB playerDB;
    Gui gui;
    Music music;
    
    //------------------------------------------------------
    /**
     * 
     * @param gui_ 
     */
    public Sokoban3D( Gui gui_ ) {
        super( "Sokoban3D" );
        gui = gui_;
        playerDB = new PlayerDB(gui_.getClass());
        this.addWindowListener( new WindowAdapter()  {
            public void windowClosing( WindowEvent e ) {
                exit();
            }
        } );
        gamePanel = new GamePanel( this );
        getContentPane().add( gamePanel );
        playerDB.loadDB(getClass(), 
        WAppReadResources.getValue(playerDB.bundle, "dataBaseIn"));
        setLocation( 100, 100 );
        setSize( 800, 464 );
        setResizable( false );
        setVisible( false );
    }

    public Class returnMainClass()
    {
       return gui.getClass();
    }
    
    //------------------------------------------------------
    private void exit() {
        try {
            setVisible( false );
            System.exit( 0 );
        }
        catch( Exception e ) {
            
        }
    }
}
