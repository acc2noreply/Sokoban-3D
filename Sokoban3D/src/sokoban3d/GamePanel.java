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
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

 /**
 * 
 * GamePanel<br>
 * loads the grafics to buld the Gui<br>
 * and the 3D View.<br>
 * 
 */
class GamePanel extends JPanel implements ActionListener {
    
    //zwischenspeicher f�r UNDO-Funktion
    private Vector<String> undoBufferPlayerX = new Vector<String>();
    private Vector<String> undoBufferPlayerY = new Vector<String>();
    private Vector<String> undoBufferPlayerDirection = new Vector<String>();
    private Vector<String> undoBufferBlockbeforeX = new Vector<String>();
    private Vector<String> undoBufferBlockbeforeY = new Vector<String>();
    private Vector<String> undoBufferBlockafterX = new Vector<String>();
    private Vector<String> undoBufferBlockafterY = new Vector<String>();
    private Vector<String> undoBufferPlayerhasMoved = new Vector<String>();
    
    // MISC VALUES
    private WallImg[][][] wallImages; //Einzelbilder für die Wände
    private Image doorimg[][][]; //Einzelbilder f�r die TürRahmen
    private Image guiback, background, back0, back1, back2, back3, welldone, backdrop, selected, sleep, full, block, goal, reached, player0, player1, player2, player3, fade0, fade1, fade2, fade3, empty;
    private Vector<Image> imgDungeonBuffer = new Vector<Image>(); //BildPuffer
    private iniFiles INI; //CFG-File-Leser
    private Sokoban3D world; //Referenz zu Sokoban3D.java
    
    // Bilder f�r Buttons
    private JButton undo_b, reset_b, exit_b, wire_b, music_b, sound_b, no3d_b, left_b, right_b, classic_b, savecfg_b;
    private Random r = new Random(); // ZufallsObjekt
    
    /**
     * 3dim matrix of the objevct cube
     */
    public Cube matrix[][][]; //umfasst Objekte des Typus "Cube"
    
    
    // INT VALUES
    private int ranPos; //varianz beim Zeichnen der BlockerObjekte (wenn verschoben)
   
    /**
     * value int, holds the current level
     */
    public int currentLevel = 0; // aktuelles Level
    
    /**
     * int array, holds the players X position
     */
    public int matrixPlayerXPos[] = new int[ 100 ]; // Position des Spielers in (X)
    
    /**
     * int array, holds the players Y position
     */
    public int matrixPlayerYPos[] = new int[ 100 ]; // Position des Spielers in (Y)
    private int maximumReached[] = new int[ 100 ]; // Maximal "ZielZone" Objekte f�r jeden Level
    private int blockmode = 2; //transparente "BlockerObjekte"
    private int blockmode1 = 5; //transparente "BlockerObjekte"
    private int backgroundFlag = 0; // Hintergrund ID (Boden&Decke)
    private int moves = 0; // wie oft hat sich der Spieler bewegt?
    private int num = 0; //varianz beim Zeichnen der BlockerObjekte (wenn verschoben)
    private int xr, yr; // small variations when painting a block if moving
    /**
     * int array, initial line of sight
     */
    public int directions[] =  {
        0, 3, 3, 3, 3, 2, 2, 2, 1, 2, 1, 2, 0, 0, 1, 2, 1, 
        3, 3, 2, 2, 0, 3, 0, 2, 3, 1, 1, 1, 3, 1, 1, 2, 3, 
        2, 3, 0, 3, 3, 3, 3, 3, 0, 3, 3, 3, 3, 0, 1, 1, 0, 
        0, 1, 2, 3, 3, 1, 3, 2, 3, 2, 0, 0, 1, 2, 1, 2, 1, 
        1, 2, 2, 0, 2, 3, 2, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 
        2, 2, 0, 0, 1
    }; // AnfangsBlickrichtung
    
    // BOOL VALUES
    /**
     * boolean value, music on or off
     */
    public boolean music = true; //Musik an/aus
    
    private boolean sound_on = true; // SoundFX an/aus
    private boolean no3d_on = true; // 3D-Fenster an/aus
    private boolean gamesolved = false; //aktueller Level gelöst?
    private boolean classic_on = false; // klassische Steuerung an/aus
    private boolean randomBlockPaint = false; //zeichne "Blocker" versetzt
    /**
     * referenz to Gui Class
     */
    protected Class mainClass;
    
    //------------------------------------------------------
    
    // Konstruktor
    /**
     * 
     * @param world_
     * referenz of Sokoban3d class
     */
    public GamePanel( Sokoban3D world_ ) 
    {
        world = world_;
        mainClass =  world_.returnMainClass();
        readCFG();
        loadGraphics();
        buildCubes();
        createButtons();
        setLayout( null );
        setBounds( 0, 0, 794, 438 );
        setBackground( Color.black );
        setVisible( true );
    }
    
    //------------------------------------------------------
    
    // erstelle Buttons, lade ButtonBilder, setzte Actions f�r Tastatur und Buttons
    private void createButtons() {
        
        Icon reset0 = ImageLoader.loadaIcon("pix/gui/reset0.res",   mainClass  );
        Icon reset1 = ImageLoader.loadaIcon("pix/gui/reset1.res",   mainClass  );
        Icon undo0 = ImageLoader.loadaIcon("pix/gui/undo0.res",   mainClass  );
        Icon undo1 = ImageLoader.loadaIcon("pix/gui/undo1.res",   mainClass  );
        Icon exit0 =  ImageLoader.loadaIcon("pix/gui/exit0.res",   mainClass  );
        Icon exit1 =  ImageLoader.loadaIcon("pix/gui/exit1.res",   mainClass  );
        Icon left0 =  ImageLoader.loadaIcon("pix/gui/left0.res",  mainClass );
        Icon left1 =  ImageLoader.loadaIcon("pix/gui/left1.res",  mainClass );
        Icon right0 =  ImageLoader.loadaIcon("pix/gui/right0.res",  mainClass );
        Icon right1 = ImageLoader.loadaIcon("pix/gui/right1.res",  mainClass );
        Icon wire = ImageLoader.loadaIcon("pix/gui/wire.res",  mainClass );
        Icon music = ImageLoader.loadaIcon("pix/gui/music.res",  mainClass );
        Icon sound = ImageLoader.loadaIcon("pix/gui/sound.res",  mainClass );
        Icon no3d = ImageLoader.loadaIcon("pix/gui/3d.res",  mainClass );
        Icon classic = ImageLoader.loadaIcon("pix/gui/classic.res",  mainClass );
        Icon savecfg = ImageLoader.loadaIcon("pix/gui/savecfg.res",  mainClass );        

        
        KeyStroke go_north = KeyStroke.getKeyStroke( KeyEvent.VK_UP, 0 );
        KeyStroke go_south = KeyStroke.getKeyStroke( KeyEvent.VK_DOWN, 0 );
        KeyStroke go_left = KeyStroke.getKeyStroke( KeyEvent.VK_LEFT, 0 );
        KeyStroke go_right = KeyStroke.getKeyStroke( KeyEvent.VK_RIGHT, 0 );
        AbstractAction go_north_Action = new AbstractAction()  {
            public void actionPerformed( ActionEvent e ) {
                if( !gamesolved ) {
                    if( !classic_on ) {
                        goNorth();
                    }
                    else {
                        goNorthClassic();
                    }
                }
            }
        };
        AbstractAction go_south_Action = new AbstractAction()  {
            public void actionPerformed( ActionEvent e ) {
                if( !gamesolved ) {
                    if( !classic_on ) {
                        goSouth();
                    }
                    else {
                        goSouthClassic();
                    }
                }
            }
        };
        AbstractAction go_left_Action = new AbstractAction()  {
            public void actionPerformed( ActionEvent e ) {
                if( !gamesolved ) {
                    if( !classic_on ) {
                        goLeft();
                    }
                    else {
                        goWestClassic();
                    }
                }
            }
        };
        AbstractAction go_right_Action = new AbstractAction()  {
            public void actionPerformed( ActionEvent e ) {
                if( !gamesolved ) {
                    if( !classic_on ) {
                        goRight();
                    }
                    else {
                        goEastClassic();
                    }
                }
            }
        };
        InputMap inputMap = this.getInputMap( WHEN_ANCESTOR_OF_FOCUSED_COMPONENT );
        ActionMap actionMap = this.getActionMap();
        inputMap.put( go_north, "go_north" );
        actionMap.put( "go_north", go_north_Action );
        inputMap.put( go_south, "go_south" );
        actionMap.put( "go_south", go_south_Action );
        inputMap.put( go_left, "go_left" );
        actionMap.put( "go_left", go_left_Action );
        inputMap.put( go_right, "go_right" );
        actionMap.put( "go_right", go_right_Action );
        undo_b = new JButton( "", undo0 );
        undo_b.setBorder( null );
        undo_b.setFocusPainted( false );
        undo_b.setPressedIcon( undo1 );
        undo_b.setBounds( 464, 5, 67, 26 );
        undo_b.setActionCommand( "undo" );
        undo_b.addActionListener( this );
        undo_b.setToolTipText( "undo" );
        add( undo_b );
        reset_b = new JButton( "", reset0 );
        reset_b.setBorder( null );
        reset_b.setFocusPainted( false );
        reset_b.setPressedIcon( reset1 );
        reset_b.setBounds( 534, 5, 67, 26 );
        reset_b.setActionCommand( "reset" );
        reset_b.addActionListener( this );
        reset_b.setToolTipText( "reset Level" );
        add( reset_b );
        exit_b = new JButton( "", exit0 );
        exit_b.setBorder( null );
        exit_b.setFocusPainted( false );
        exit_b.setPressedIcon( exit1 );
        exit_b.setBounds( 604, 5, 67, 26 );
        exit_b.setActionCommand( "exit" );
        exit_b.addActionListener( this );
        exit_b.setToolTipText( "MainMenu" );
        add( exit_b );
        left_b = new JButton( "", left0 );
        left_b.setBorder( null );
        left_b.setFocusPainted( false );
        left_b.setPressedIcon( left1 );
        left_b.setBounds( 674, 5, 34, 26 );
        left_b.setActionCommand( "left_button" );
        left_b.addActionListener( this );
        left_b.setVisible( false );
        left_b.setToolTipText( "previous Level" );
        add( left_b );
        right_b = new JButton( "", right0 );
        right_b.setBorder( null );
        right_b.setFocusPainted( false );
        right_b.setPressedIcon( right1 );
        right_b.setBounds( 712, 5, 34, 26 );
        right_b.setActionCommand( "right_button" );
        right_b.addActionListener( this );
        right_b.setVisible( false );
        right_b.setToolTipText( "next Level" );
        add( right_b );
        wire_b = new JButton( "", wire );
        wire_b.setBorder( null );
        wire_b.setFocusPainted( false );
        wire_b.setBounds( 30 + 237, 310 + 14, 22, 22 );
        wire_b.setActionCommand( "wire" );
        wire_b.addActionListener( this );
        wire_b.setToolTipText( "empty Cubes" );
        add( wire_b );
        music_b = new JButton( "", music );
        music_b.setBorder( null );
        music_b.setFocusPainted( false );
        music_b.setBounds( 60 + 237, 310 + 14, 22, 22 );
        music_b.setActionCommand( "music" );
        music_b.addActionListener( this );
        music_b.setToolTipText( "Music" );
        add( music_b );
        sound_b = new JButton( "", sound );
        sound_b.setBorder( null );
        sound_b.setFocusPainted( false );
        sound_b.setBounds( 90 + 237, 310 + 14, 22, 22 );
        sound_b.setActionCommand( "sound" );
        sound_b.addActionListener( this );
        sound_b.setToolTipText( "ambient Sound" );
        add( sound_b );
        no3d_b = new JButton( "", no3d );
        no3d_b.setBorder( null );
        no3d_b.setFocusPainted( false );
        no3d_b.setBounds( 120 + 237, 310 + 14, 22, 22 );
        no3d_b.setActionCommand( "no3d" );
        no3d_b.addActionListener( this );
        no3d_b.setToolTipText( "3D-View" );
        add( no3d_b );
        classic_b = new JButton( "", classic );
        classic_b.setBorder( null );
        classic_b.setFocusPainted( false );
        classic_b.setBounds( 150 + 237, 310 + 14, 22, 22 );
        classic_b.setActionCommand( "classic" );
        classic_b.addActionListener( this );
        classic_b.setToolTipText( "classic Movement" );
        add( classic_b );
        savecfg_b = new JButton( "", savecfg );
        savecfg_b.setBorder( null );
        savecfg_b.setFocusPainted( false );
        savecfg_b.setBounds( 180 + 237, 310 + 14, 22, 22 );
        savecfg_b.setActionCommand( "savecfg" );
        savecfg_b.addActionListener( this );
        savecfg_b.setToolTipText( "save Configuration" );
        add( savecfg_b );
    }
    
    //-------------------------------------------------------------------
    
    //lies die Einstellungen für die kleinen Buttons in der Mitte des Panels.
    private void readCFG() {
        INI = new iniFiles( "cfg/dungeon.ini", mainClass );
        INI.loadIni();
        blockmode = INI.getIntValue( "INTVALUES", "blockmode" );
        music = INI.getBoolValue( "BOOLVALUES", "music" );
        sound_on = INI.getBoolValue( "BOOLVALUES", "sound" );
        no3d_on = INI.getBoolValue( "BOOLVALUES", "3DPanel" );
        classic_on = INI.getBoolValue( "BOOLVALUES", "classicMovement" );
    }
    
    //-------------------------------------------------------------------
    
    //registriere die Kommandos die von den Buttons kommen. 
    /**
     * @param event
     * ActionEvent, the game commands
     */
    public void actionPerformed( ActionEvent event ) {
        String cmd = event.getActionCommand();
        if( cmd.equals( "exit" ) ) {
            exitGame();
        }
        if( !gamesolved ) {
            if( cmd.equals( "undo" ) ) {
                undo();
            }
            if( cmd.equals( "right_button" ) ) {
                jump2NextLevel();
            }
            if( cmd.equals( "left_button" ) ) {
                jump2PreviousLevel();
            }
            if( cmd.equals( "reset" ) ) {
                resetLevel();
            }
            if( cmd.equals( "wire" ) ) {
                if( blockmode == 2 ) {
                    blockmode = 4;
                    blockmode1 = 4;
                }
                else {
                    blockmode = 2;
                    blockmode1 = 5;
                }
                randomBlockPaint = true;
                repaint();
            }
            if( cmd.equals( "music" ) ) {
                if( music == false ) {
                    world.gui.music0.startSound();
                    music = true;
                }
                else {
                    music = false;
                    world.gui.music0.stopSound();
                }
                randomBlockPaint = true;
                repaint();
            }
            if( cmd.equals( "sound" ) ) {
                if( sound_on == false ) {
                    sound_on = true;
                }
                else {
                    sound_on = false;
                }
                randomBlockPaint = true;
                repaint();
            }
            if( cmd.equals( "no3d" ) ) {
                if( no3d_on == false ) {
                    no3d_on = true;
                }
                else {
                    no3d_on = false;
                }
                randomBlockPaint = true;
                repaint();
            }
            if( cmd.equals( "classic" ) ) {
                if( classic_on == false ) {
                    classic_on = true;
                }
                else {
                    classic_on = false;
                }
                randomBlockPaint = true;
                repaint();
            }
            if( cmd.equals( "savecfg" ) ) {
                saveCFG();
            }
        }
    }
    
    //-------------------------------------------------------------------
    
    //lade verschiedene Grafiken.	
    private void loadGraphics() {
        String sep = System.getProperty( "file.separator" );
        System.out.println( "-------- Init --------------" );
        MediaTracker mt = new MediaTracker( this );
        Image img;
        int imgCounter = 0;
        doorimg = new Image[ 6 ][ 9 ][ 5 ];
        wallImages = new WallImg[ 6 ][ 9 ][ 5 ];
        String Walltype[] =  {
            "A", "B", "B", "B", "C", "D", "D", "D", "A", "A", "B", 
            "B", "C", "D", "D", "A", "A", "B", "C", "D", "A", "A", 
            "C", "A", "A", "C", "A"
        };
       
        back0 = ImageLoader.loadaImage("pix/level/background/background0.res", mainClass);
        back1 = ImageLoader.loadaImage("pix/level/background/background1.res", mainClass);
        back2 = ImageLoader.loadaImage("pix/level/background/al_background0.res", mainClass);
        back3 = ImageLoader.loadaImage("pix/level/background/al_background1.res", mainClass);
        guiback = ImageLoader.loadaImage("pix/gui/background.res", mainClass);
        selected = ImageLoader.loadaImage("pix/gui/selected.res", mainClass);
        sleep = ImageLoader.loadaImage("pix/gui/no3d.res", mainClass); 

        mt.addImage( back0, 0 );
        mt.addImage( back1, 1 );
        mt.addImage( back2, 2 );
        mt.addImage( back3, 3 );
        mt.addImage( guiback, 4 );
        mt.addImage( sleep, 5 );
        try {
            mt.waitForAll();
        }
        catch( InterruptedException e ) {
            System.out.println( "BackgroundImages not found!" );
        }
        
        //lade Grafiken f�r W�nde						
        for( int i = 0;i < 2;i++ ) {
            System.out.println( "| Loading WallSet #" + i + "       |" );
            int x = 0;
            int xMax = 9;
            int typeCounter = 0;
            imgCounter = 0;
            String nullchar = "0";
            for( int y = 0;y < 5;y++ ) {
                if( y == 1 ) {
                    x = 1;
                    xMax = 8;
                }
                if( y == 2 ) {
                    x = 2;
                    xMax = 7;
                }
                if( ( y == 3 ) || ( y == 4 ) ) {
                    x = 3;
                    xMax = 6;
                }
                for( int dx = x;dx < xMax;dx++ ) {
                    wallImages[ i ][ dx ][ y ] = new WallImg();
                    if( imgCounter < 10 ) {
                        nullchar = "0";
                    }
                    else {
                        nullchar = "";
                    }
                     if(Walltype[typeCounter].equals("A")) {
                        //img = getToolkit().getImage("pix/level/set" + i + "/wall00" + nullchar + imgCounter + ".res"); //02.06.12 Walter Hipp
                        img = ImageLoader.loadaImage("pix/level/set" + i + "/wall00" + nullchar + imgCounter + ".res", mainClass);
                        wallImages[i][dx][y].setWallImg(0, img);
                        mt.addImage(wallImages[i][dx][y].getWallImg(0), imgCounter);
                        imgCounter++;
                    }
                    if(Walltype[typeCounter].equals("B")) {
                        //img = getToolkit().getImage("pix/level/set" + i + "/wall00" + nullchar + imgCounter + ".res");  //02.06.12 Walter Hipp
                        img = ImageLoader.loadaImage("pix/level/set" + i + "/wall00" + nullchar + imgCounter + ".res", mainClass);
                        wallImages[i][dx][y].setWallImg(0, img);
                        mt.addImage(wallImages[i][dx][y].getWallImg(0), imgCounter);
                        imgCounter++;
                        //img = getToolkit().getImage("pix/level/set" + i + "/wall00" + nullchar + imgCounter + ".res");  //02.06.12 Walter Hipp
                        img = ImageLoader.loadaImage("pix/level/set" + i + "/wall00" + nullchar + imgCounter + ".res", mainClass);
                        wallImages[i][dx][y].setWallImg(3, img);
                        mt.addImage(wallImages[i][dx][y].getWallImg(3), imgCounter);
                        imgCounter++;
                    }
                    if(Walltype[typeCounter].equals("C")) {
                        //img = getToolkit().getImage("pix/level/set" + i + "/wall00" + nullchar + imgCounter + ".res"); //02.06.12 Walter Hipp
                        img = ImageLoader.loadaImage("pix/level/set" + i + "/wall00" + nullchar + imgCounter + ".res", mainClass);
                        wallImages[i][dx][y].setWallImg(0, img);
                        mt.addImage(wallImages[i][dx][y].getWallImg(0), imgCounter);
                        imgCounter++;
                        //img = getToolkit().getImage("pix/level/set" + i + "/wall00" + nullchar + imgCounter + ".res"); //02.06.12 Walter Hipp
                        img = ImageLoader.loadaImage("pix/level/set" + i + "/wall00" + nullchar + imgCounter + ".res", mainClass);
                        wallImages[i][dx][y].setWallImg(3, img);
                        mt.addImage(wallImages[i][dx][y].getWallImg(3), imgCounter);
                        imgCounter++;
                        //img = getToolkit().getImage("pix/level/set" + i + "/wall00" + nullchar + imgCounter + ".res"); //02.06.12 Walter Hipp
                        img = ImageLoader.loadaImage("pix/level/set" + i + "/wall00" + nullchar + imgCounter + ".res", mainClass);
                        wallImages[i][dx][y].setWallImg(1, img);
                        mt.addImage(wallImages[i][dx][y].getWallImg(1), imgCounter);
                        imgCounter++;
                    }
                    if(Walltype[typeCounter].equals("D")) {
                        //img = getToolkit().getImage("pix/level/set" + i + "/wall00" + nullchar + imgCounter + ".res"); //02.06.12 Walter Hipp
                        img = ImageLoader.loadaImage("pix/level/set" + i + "/wall00" + nullchar + imgCounter + ".res", mainClass);
                        wallImages[i][dx][y].setWallImg(0, img);
                        mt.addImage(wallImages[i][dx][y].getWallImg(0), imgCounter);
                        imgCounter++;
                        //img = getToolkit().getImage("pix/level/set" + i + "/wall00" + nullchar + imgCounter + ".res"); //02.06.12 Walter Hipp
                        img = ImageLoader.loadaImage("pix/level/set" + i + "/wall00" + nullchar + imgCounter + ".res", mainClass);
                        wallImages[i][dx][y].setWallImg(1, img);
                        mt.addImage(wallImages[i][dx][y].getWallImg(1), imgCounter);
                        imgCounter++;
                    }
                    typeCounter++;
                }
            }
            try {
                mt.waitForAll();
            }
            catch( InterruptedException e ) {
                System.out.println( "WallImages not found!" );
            }
        }
        for( int i = 0;i < 6;i++ ) {
            System.out.println( "| Loading XtraSet #" + i + "       |" );
            int x = 0;
            int xMax = 9;
            imgCounter = 0;
            String nullchar = "0";
            for( int y = 0;y < 5;y++ ) {
                if( y == 1 ) {
                    x = 1;
                    xMax = 8;
                }
                if( y == 2 ) {
                    x = 2;
                    xMax = 7;
                }
                if( ( y == 3 ) || ( y == 4 ) ) {
                    x = 3;
                    xMax = 6;
                }
                for( int dx = x;dx < xMax;dx++ ) {
                    if( imgCounter < 10 ) {
                        nullchar = "0";
                    }
                    else {
                        nullchar = "";
                    }
                    doorimg[i][dx][y] = ImageLoader.loadaImage("pix/level/extra/extra" + i + "/wall00" + nullchar + imgCounter + ".res", mainClass);

                    mt.addImage( doorimg[ i ][ dx ][ y ], imgCounter );
                    imgCounter++;
                }
            }
            try {
                mt.waitForAll();
            }
            catch( InterruptedException e ) {
                System.out.println( "DoorImages not found!" );
            }
        }
        
        // lade Bilder f�r die AutoMap
        System.out.println( "| Loading AutoMapGFX       |" );
                  //changed 02.06.12 by Walter Hipp
        backdrop = ImageLoader.loadaImage("pix/automap/backdrop.res", mainClass);
        full = ImageLoader.loadaImage("pix/automap/full.res", mainClass);
        block = ImageLoader.loadaImage("pix/automap/block.res", mainClass);
        goal = ImageLoader.loadaImage("pix/automap/goal.res", mainClass);
        reached = ImageLoader.loadaImage("pix/automap/reached.res", mainClass);
        player0 = ImageLoader.loadaImage("pix/automap/player0.res", mainClass);
        player1 = ImageLoader.loadaImage("pix/automap/player1.res", mainClass);
        player2 = ImageLoader.loadaImage("pix/automap/player2.res", mainClass);
        player3 = ImageLoader.loadaImage("pix/automap/player3.res", mainClass);
        fade0 = ImageLoader.loadaImage("pix/automap/fade0.res", mainClass);
        fade1 = ImageLoader.loadaImage("pix/automap/fade1.res", mainClass);
        fade2 = ImageLoader.loadaImage("pix/automap/fade2.res", mainClass);
        fade3 = ImageLoader.loadaImage("pix/automap/fade3.res", mainClass);
        empty = ImageLoader.loadaImage("pix/automap/empty.res", mainClass);
        welldone = ImageLoader.loadaImage("pix/gui/welldone.res", mainClass);

        mt.addImage( backdrop, 0 );
        mt.addImage( full, 1 );
        mt.addImage( player0, 2 );
        mt.addImage( player1, 3 );
        mt.addImage( player2, 4 );
        mt.addImage( player3, 5 );
        mt.addImage( fade0, 6 );
        mt.addImage( fade1, 7 );
        mt.addImage( fade2, 8 );
        mt.addImage( fade3, 9 );
        mt.addImage( empty, 10 );
        mt.addImage( block, 11 );
        mt.addImage( reached, 12 );
        mt.addImage( welldone, 13 );
        try {
            mt.waitForAll();
        }
        catch( InterruptedException e ) {
            System.out.println( "Graphics not found!" );
        }
    }
    
    //------------------------------------------------------
    
    // fülle Matrix mit CubeObjekten
    private void buildCubes() {
        System.out.println( "| Building DungeonMatrix   |" );
        System.out.println( "-------- Done --------------\n" );
        System.out.println( "Starting Sokoban3D v1.05" );
        matrix = new Cube[ 90 ][ 21 ][ 21 ];
        int walltexture_a = 0;
        int walltexture_b = 1;
        int walltexture_temp = 1;
        int text_count;
        for( int i = 0;i < 90;i++ ) {
            text_count = 0;
            for( int j = 0;j < 21;j++ ) {
                for( int k = 0;k < 21;k++ ) {
                    matrix[ i ][ j ][ k ] = new Cube( false );
                    matrix[ i ][ j ][ k ].setTexture( walltexture_a, 0 );
                    matrix[ i ][ j ][ k ].setTexture( walltexture_b, 1 );
                    matrix[ i ][ j ][ k ].setTexture( walltexture_a, 2 );
                    matrix[ i ][ j ][ k ].setTexture( walltexture_b, 3 );
                    text_count++;
                    if( text_count % 2 == 0 ) {
                        walltexture_a = 0;
                        walltexture_b = 1;
                    }
                    if( text_count % 2 == 1 ) {
                        walltexture_a = 1;
                        walltexture_b = 0;
                    }
                }
            }
        }
    }
    
    // lies LevelDaten und weise diese den CubeObjekten zu
    private void fillCubes() {
        String sep = System.getProperty( "file.separator" );
        int counter = 0;
        maximumReached[ currentLevel ] = 0;
        for( int i = 0;i < 90;i++ ) {
            maximumReached[ i ] = 0;
        }
        String path = System.getProperty("user.home")
                       + System.getProperty("file.separator") + ".Sokoban3D"
                       + System.getProperty("file.separator") 
                       + "cfg" + System.getProperty("file.separator") + "level.dat";  //included 02.06.12 by Walter Hipp
        try {
            //BufferedReader f = new BufferedReader(new FileReader("cfg/level.dat")); // changed 02.06.12 by Walter Hipp
            BufferedReader f = new BufferedReader(new FileReader(path));
            int nextLevel = 0;
            int row = 1;
            String line;
            int cubetype;
            while( ( ( line = f.readLine() ) != null ) ) {
                if( line.length() != 0 ) {
                    line = " " + line;
                    for( int column = 1;column < line.length();column++ ) {
                        if( !( line.substring( column, column + 1 ).equals( " " ) ) ) {
                            cubetype = Integer.parseInt( line.substring( column, column + 1 ) );
                            switch( cubetype ) {
                              case 1:
                                   {
                                      matrix[ nextLevel ][ column ][ row ].setZone( true );
                                      matrix[ nextLevel ][ column ][ row ].setGoal( false );
                                      matrix[ nextLevel ][ column ][ row ].setReached( false );
                                      matrix[ nextLevel ][ column ][ row ].setDoor( false );
                                      matrix[ nextLevel ][ column ][ row ].setBlocked( false );
                                      break;
                                  }
                              
                              case 2:
                                   {
                                      matrix[ nextLevel ][ column ][ row ].setZone( true );
                                      matrix[ nextLevel ][ column ][ row ].setGoal( true );
                                      matrix[ nextLevel ][ column ][ row ].setReached( false );
                                      matrix[ nextLevel ][ column ][ row ].setDoor( false );
                                      matrix[ nextLevel ][ column ][ row ].setBlocked( false );
                                      maximumReached[ nextLevel ] = maximumReached[ nextLevel ] + 1;
                                      break;
                                  }
                              
                              case 3:
                                   {
                                      matrix[ nextLevel ][ column ][ row ].setZone( true );
                                      matrix[ nextLevel ][ column ][ row ].setGoal( false );
                                      matrix[ nextLevel ][ column ][ row ].setReached( false );
                                      matrix[ nextLevel ][ column ][ row ].setDoor( true );
                                      matrix[ nextLevel ][ column ][ row ].setBlocked( false );
                                      break;
                                  }
                              
                              case 4:
                                   {
                                      matrix[ nextLevel ][ column ][ row ].setZone( true );
                                      matrix[ nextLevel ][ column ][ row ].setGoal( false );
                                      matrix[ nextLevel ][ column ][ row ].setReached( false );
                                      matrix[ nextLevel ][ column ][ row ].setDoor( true );
                                      matrix[ nextLevel ][ column ][ row ].setBlocked( true );
                                      break;
                                  }
                              
                              case 5:
                                   {
                                      matrix[ nextLevel ][ column ][ row ].setZone( true );
                                      matrix[ nextLevel ][ column ][ row ].setGoal( false );
                                      matrix[ nextLevel ][ column ][ row ].setReached( false );
                                      matrix[ nextLevel ][ column ][ row ].setDoor( false );
                                      matrix[ nextLevel ][ column ][ row ].setBlocked( true );
                                      break;
                                  }
                              
                              case 6:
                                   {
                                      matrix[ nextLevel ][ column ][ row ].setZone( true );
                                      matrix[ nextLevel ][ column ][ row ].setGoal( true );
                                      matrix[ nextLevel ][ column ][ row ].setReached( true );
                                      matrix[ nextLevel ][ column ][ row ].setDoor( false );
                                      matrix[ nextLevel ][ column ][ row ].setBlocked( true );
                                      maximumReached[ nextLevel ] = maximumReached[ nextLevel ] + 1;
                                      break;
                                  }
                              
                              case 9:
                                   {
                                      matrixPlayerXPos[ nextLevel ] = column;
                                      matrixPlayerYPos[ nextLevel ] = row;
                                      matrix[ nextLevel ][ column ][ row ].setZone( true );
                                      matrix[ nextLevel ][ column ][ row ].setGoal( false );
                                      matrix[ nextLevel ][ column ][ row ].setReached( false );
                                      matrix[ nextLevel ][ column ][ row ].setDoor( false );
                                      matrix[ nextLevel ][ column ][ row ].setBlocked( false );
                                      break;
                                  }
                            }
                        }
                    }
                    row++;
                }
                else {
                    nextLevel++;
                    counter++;
                    row = 1;
                }
            }
            f.close();
        }
        catch( IOException e ) {
            System.out.println( "Fehler beim Lesen der Datei: " +  path );
        }
        world.player.setXPosAbs( matrixPlayerXPos[ currentLevel ] );
        world.player.setYPosAbs( matrixPlayerYPos[ currentLevel ] );
    }
    
    //------------------------------------------------------//
    
    // entscheide wo T�rRahmen sinvoll sind und weise dieses Flag den CubeObjekten zu
    private void createDoors() {
        for( int i = 0;i < 90;i++ ) {
            for( int y = 0;y < 18;y++ ) {
                for( int x = 0;x < 18;x++ ) {
                    if( !matrix[ i ][ x ][ y ].isGoal() ) {
                        if( ( matrix[ i ][ x ][ y ].isZone() ) && ( matrix[ i ][ x ][ y - 1 ].isZone() ) && ( matrix[ i ][ x ][ y + 1 ].isZone() ) && ( matrix[ i ][ x + 1 ][ y + 1 ].isZone() ) && ( matrix[ i ][ x - 1 ][ y + 1 ].isZone() ) && !( matrix[ i ][ x - 1 ][ y ].isZone() ) && !( matrix[ i ][ x + 1 ][ y ].isZone() ) ) {
                            matrix[ i ][ x ][ y ].setDoor( true );
                        }
                        if( ( matrix[ i ][ x ][ y ].isZone() ) && ( matrix[ i ][ x - 1 ][ y ].isZone() ) && ( matrix[ i ][ x + 1 ][ y - 1 ].isZone() ) && ( matrix[ i ][ x + 1 ][ y ].isZone() ) && ( matrix[ i ][ x + 1 ][ y + 1 ].isZone() ) && !( matrix[ i ][ x ][ y - 1 ].isZone() ) && !( matrix[ i ][ x ][ y + 1 ].isZone() ) ) {
                            matrix[ i ][ x ][ y ].setDoor( true );
                        }
                        if( ( matrix[ i ][ x ][ y ].isZone() ) && ( matrix[ i ][ x + 1 ][ y ].isZone() ) && ( matrix[ i ][ x - 1 ][ y ].isZone() ) && ( matrix[ i ][ x - 1 ][ y - 1 ].isZone() ) && ( matrix[ i ][ x - 1 ][ y + 1 ].isZone() ) && !( matrix[ i ][ x ][ y - 1 ].isZone() ) && !( matrix[ i ][ x ][ y + 1 ].isZone() ) ) {
                            matrix[ i ][ x ][ y ].setDoor( true );
                        }
                        if( ( matrix[ i ][ x ][ y ].isZone() ) && ( matrix[ i ][ x ][ y + 1 ].isZone() ) && ( matrix[ i ][ x ][ y - 1 ].isZone() ) && ( matrix[ i ][ x - 1 ][ y - 1 ].isZone() ) && ( matrix[ i ][ x + 1 ][ y - 1 ].isZone() ) && !( matrix[ i ][ x + 1 ][ y ].isZone() ) && !( matrix[ i ][ x - 1 ][ y ].isZone() ) ) {
                            matrix[ i ][ x ][ y ].setDoor( true );
                        }
                    }
                }
            }
        }
    }
    
    //------------------------------------------------------
    
    // fülle den BildPuffer mit der 3DGrafik
    private void buildDungeonGFX() {
        int playerXPos = world.player.getXPos();
        int playerYPos = world.player.getYPos();
        int maxCubes = 27;
        int playerDirection = world.player.getDirection();
        int textureID[] = new int[ 6 ];
        int textureID0[] =  {
            0, 3, 1, 0, 3, 1
        };
        int textureID1[] =  {
            1, 0, 2, 0, 3, 1
        };
        int textureID2[] =  {
            3, 2, 0, 0, 3, 1
        };
        int textureID3[] =  {
            2, 1, 3, 0, 3, 1
        };
        int wallID[] = new int[ 6 ];
        int wallID0[] =  {
            0, -1, -1, 0, 1, 0
        };
        int wallID1[] =  {
            1, 0, 0, -1, 0, 1
        };
        int wallID2[] =  {
            -1, 0, 0, 1, 0, -1
        };
        int wallID3[] =  {
            0, 1, 1, 0, -1, 0
        };
        int x[] = new int[ maxCubes ];
        int y[] = new int[ maxCubes ];
        int imgx[] =  {
            0, 1, 2, 3, 8, 7, 6, 5, 4, 1, 2, 3, 7, 6, 5, 4, 2, 
            3, 6, 5, 4, 3, 5, 4, 3, 5, 4
        };
        int imgy[] =  {
            0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 2, 
            2, 2, 2, 2, 3, 3, 3, 4, 4, 4
        };
        int x0[] =  {
            -4, -3, -2, -1, 4, 3, 2, 1, 0, -3, -2, -1, 3, 2, 1, 
            0, -2, -1, 2, 1, 0, -1, 1, 0, -1, 1, 0
        };
        int y0[] =  {
            -4, -4, -4, -4, -4, -4, -4, -4, -4, -3, -3, -3, -3, 
            -3, -3, -3, -2, -2, -2, -2, -2, -1, -1, -1, 0, 0, 0
        };
        int x1[] =  {
            4, 4, 4, 4, 4, 4, 4, 4, 4, 3, 3, 3, 3, 3, 3, 3, 2, 
            2, 2, 2, 2, 1, 1, 1, 0, 0, 0
        };
        int y1[] =  {
            -4, -3, -2, -1, 4, 3, 2, 1, 0, -3, -2, -1, 3, 2, 1, 
            0, -2, -1, 2, 1, 0, -1, 1, 0, -1, 1, 0
        };
        int x2[] =  {
            -4, -4, -4, -4, -4, -4, -4, -4, -4, -3, -3, -3, -3, 
            -3, -3, -3, -2, -2, -2, -2, -2, -1, -1, -1, 0, 0, 0
        };
        int y2[] =  {
            4, 3, 2, 1, -4, -3, -2, -1, 0, 3, 2, 1, -3, -2, -1, 
            0, 2, 1, -2, -1, 0, 1, -1, 0, 1, -1, 0
        };
        int x3[] =  {
            4, 3, 2, 1, -4, -3, -2, -1, 0, 3, 2, 1, -3, -2, -1, 
            0, 2, 1, -2, -1, 0, 1, -1, 0, 1, -1, 0
        };
        int y3[] =  {
            4, 4, 4, 4, 4, 4, 4, 4, 4, 3, 3, 3, 3, 3, 3, 3, 2, 
            2, 2, 2, 2, 1, 1, 1, 0, 0, 0
        };
        if( playerDirection == 0 ) {
            for( int i = 0;i < maxCubes;i++ ) {
                x[ i ] = x0[ i ];
                y[ i ] = y0[ i ];
            }
            for( int i = 0;i < 6;i++ ) {
                textureID[ i ] = textureID0[ i ];
                wallID[ i ] = wallID0[ i ];
            }
        }
        if( playerDirection == 1 ) {
            for( int i = 0;i < maxCubes;i++ ) {
                x[ i ] = x1[ i ];
                y[ i ] = y1[ i ];
            }
            for( int i = 0;i < 6;i++ ) {
                textureID[ i ] = textureID1[ i ];
                wallID[ i ] = wallID1[ i ];
            }
        }
        if( world.player.getDirection() == 3 ) {
            for( int i = 0;i < maxCubes;i++ ) {
                x[ i ] = x2[ i ];
                y[ i ] = y2[ i ];
            }
            for( int i = 0;i < 6;i++ ) {
                textureID[ i ] = textureID2[ i ];
                wallID[ i ] = wallID2[ i ];
            }
        }
        if( world.player.getDirection() == 2 ) {
            for( int i = 0;i < maxCubes;i++ ) {
                x[ i ] = x3[ i ];
                y[ i ] = y3[ i ];
            }
            for( int i = 0;i < 6;i++ ) {
                textureID[ i ] = textureID3[ i ];
                wallID[ i ] = wallID3[ i ];
            }
        }
        for( int i = 0;i < maxCubes;i++ ) {
            try {
                
                // north
                if( matrix[ currentLevel ][ playerXPos + x[ i ] ][ playerYPos + y[ i ] ].isZone() && !matrix[ currentLevel ][ playerXPos + x[ i ] + wallID[ 0 ] ][ playerYPos + y[ i ] + wallID[ 1 ] ].isZone() ) {
                    if( wallImages[ matrix[ currentLevel ][ playerXPos + x[ i ] ][ playerYPos + y[ i ] ].getTexture( textureID[ 0 ] ) ][ imgx[ i ] ][ imgy[ i ] ].getWallImg( textureID[ 3 ] ) != null ) {
                        imgDungeonBuffer.addElement( wallImages[ matrix[ currentLevel ][ playerXPos + x[ i ] ][ playerYPos + y[ i ] ].getTexture( textureID[ 0 ] ) ][ imgx[ i ] ][ imgy[ i ] ].getWallImg( textureID[ 3 ] ) );
                    }
                }
                
                // west
                if( matrix[ currentLevel ][ playerXPos + x[ i ] ][ playerYPos + y[ i ] ].isZone() && !matrix[ currentLevel ][ playerXPos + x[ i ] + wallID[ 2 ] ][ playerYPos + y[ i ] + wallID[ 3 ] ].isZone() ) {
                    if( wallImages[ matrix[ currentLevel ][ playerXPos + x[ i ] ][ playerYPos + y[ i ] ].getTexture( textureID[ 1 ] ) ][ imgx[ i ] ][ imgy[ i ] ].getWallImg( textureID[ 4 ] ) != null ) {
                        imgDungeonBuffer.addElement( wallImages[ matrix[ currentLevel ][ playerXPos + x[ i ] ][ playerYPos + y[ i ] ].getTexture( textureID[ 1 ] ) ][ imgx[ i ] ][ imgy[ i ] ].getWallImg( textureID[ 4 ] ) );
                    }
                }
                
                //east
                if( matrix[ currentLevel ][ playerXPos + x[ i ] ][ playerYPos + y[ i ] ].isZone() && !matrix[ currentLevel ][ playerXPos + x[ i ] + wallID[ 4 ] ][ playerYPos + y[ i ] + wallID[ 5 ] ].isZone() ) {
                    if( wallImages[ matrix[ currentLevel ][ playerXPos + x[ i ] ][ playerYPos + y[ i ] ].getTexture( textureID[ 2 ] ) ][ imgx[ i ] ][ imgy[ i ] ].getWallImg( textureID[ 5 ] ) != null ) {
                        imgDungeonBuffer.addElement( wallImages[ matrix[ currentLevel ][ playerXPos + x[ i ] ][ playerYPos + y[ i ] ].getTexture( textureID[ 2 ] ) ][ imgx[ i ] ][ imgy[ i ] ].getWallImg( textureID[ 5 ] ) );
                    }
                }
                
                //goal
                if( matrix[ currentLevel ][ playerXPos + x[ i ] ][ playerYPos + y[ i ] ].isGoal() ) {
                    imgDungeonBuffer.addElement( doorimg[ 3 ][ imgx[ i ] ][ imgy[ i ] ] );
                }
                
                //door
                if( matrix[ currentLevel ][ playerXPos + x[ i ] ][ playerYPos + y[ i ] ].isDoor() ) {
                    if( world.player.getDirection() == 0 || world.player.getDirection() == 2 ) {
                        if( matrix[ currentLevel ][ playerXPos + x[ i ] ][ playerYPos + y[ i ] + 1 ].isZone() && matrix[ currentLevel ][ playerXPos + x[ i ] ][ playerYPos + y[ i ] - 1 ].isZone() ) {
                            imgDungeonBuffer.addElement( doorimg[ 1 ][ imgx[ i ] ][ imgy[ i ] ] );
                        }
                        else {
                            imgDungeonBuffer.addElement( doorimg[ 0 ][ imgx[ i ] ][ imgy[ i ] ] );
                        }
                    }
                    if( world.player.getDirection() == 1 || world.player.getDirection() == 3 ) {
                        if( matrix[ currentLevel ][ playerXPos + x[ i ] + 1 ][ playerYPos + y[ i ] ].isZone() && matrix[ currentLevel ][ playerXPos + x[ i ] - 1 ][ playerYPos + y[ i ] ].isZone() ) {
                            imgDungeonBuffer.addElement( doorimg[ 1 ][ imgx[ i ] ][ imgy[ i ] ] );
                        }
                        else {
                            imgDungeonBuffer.addElement( doorimg[ 0 ][ imgx[ i ] ][ imgy[ i ] ] );
                        }
                    }
                }
                
                //blocker
                if( matrix[ currentLevel ][ playerXPos + x[ i ] ][ playerYPos + y[ i ] ].isBlocked() && !matrix[ currentLevel ][ playerXPos + x[ i ] ][ playerYPos + y[ i ] ].isReached() ) {
                    imgDungeonBuffer.addElement( doorimg[ blockmode ][ imgx[ i ] ][ imgy[ i ] ] );
                    if( ( imgx[ i ] == 4 ) && ( imgy[ i ] == 3 ) ) {
                        ranPos = imgDungeonBuffer.size() - 1;
                    }
                }
                if( matrix[ currentLevel ][ playerXPos + x[ i ] ][ playerYPos + y[ i ] ].isBlocked() && matrix[ currentLevel ][ playerXPos + x[ i ] ][ playerYPos + y[ i ] ].isReached() ) {
                    imgDungeonBuffer.addElement( doorimg[ blockmode1 ][ imgx[ i ] ][ imgy[ i ] ] );
                    if( ( imgx[ i ] == 4 ) && ( imgy[ i ] == 3 ) ) {
                        ranPos = imgDungeonBuffer.size() - 1;
                    }
                }
            }
            catch( ArrayIndexOutOfBoundsException e ) {
                
            }
        }
    }
    
    //--------------------------------------------------  
    
    /*
    * Zeichne alles im BildPuffer sowie alle anderen Elemente im JPanel
    * draws the images an d all the other elements included tin he javax.swingJpanel
    */
    /**
     * @param g
     * Draw everything in the ImageBuffer as well as all other elements in the JPanel
     * draws the images and all the other elements included tin he javax.swingJpanel
     */
    public void paintComponent( Graphics g ) {
        super.paintComponent( g );
        g.drawImage( guiback, 0, 0, this );
        g.drawString( "Player: " + world.playername, 50, 360 );
        g.drawString( "Level: " + ( currentLevel + 1 ), 50, 360 + 17 );
        g.drawString( "Moves: " + moves, 50, 360 + 34 );
        Image img = null; // 02.06.12 by Walter Hipp
        if( no3d_on ) {
            buildDungeonGFX();
            setBackgroundGraphic();
            g.drawImage( background, 0, 0, this );
        }
        else {
            g.drawImage( sleep, 0, 0, this );
        }
        if( !gamesolved ) {
            int buffersize = imgDungeonBuffer.size();
            if( !randomBlockPaint == true ) {
                xr = 0 + Math.abs( r.nextInt() ) % 2;
                yr = 0 + Math.abs( r.nextInt() ) % 2;
            }
            for( int i = 0;i < buffersize;i++ ) {  // changed 02.07.08 by Walter Hipp
                img = (Image) imgDungeonBuffer.elementAt(i);
                if( ranPos == i  && img != null) {
                      g.drawImage(img, xr, yr, this);
                }
                else if (img != null) {
                    g.drawImage(img, 0, 0, this);
                }
            }
            ranPos = -1;
        }
        if( gamesolved ) {
            g.drawImage( welldone, 0, 0, this );
        }
        if( blockmode == 4 ) {
            g.drawImage( selected, 27 + 237, 307 + 14, this );
        }
        if( music ) {
            g.drawImage( selected, 57 + 237, 307 + 14, this );
        }
        if( sound_on ) {
            g.drawImage( selected, 87 + 237, 307 + 14, this );
        }
        if( no3d_on ) {
            g.drawImage( selected, 117 + 237, 307 + 14, this );
        }
        if( classic_on ) {
            g.drawImage( selected, 147 + 237, 307 + 14, this );
        }
        imgDungeonBuffer.clear();
        int playerXPos = world.player.getXPos();
        int playerYPos = world.player.getYPos();
        int playerDirection = world.player.getDirection();
        int pixX = 0;
        int pixY = 0;
        g.drawImage( backdrop, 459, 35, this );
        for( int y = 0;y < 21;y++ ) {
            for( int x = 0;x < 21;x++ ) {
                if( world.gamePanel.matrix[ currentLevel ][ x ][ y ].isZone() ) {
                    g.drawImage( full, pixX + 38 + 461, pixY + 45 + 32, this );
                    if( ( world.gamePanel.matrix[ currentLevel ][ x ][ y ].isBlocked() ) && !( world.gamePanel.matrix[ currentLevel ][ x ][ y ].isReached() ) ) {
                        g.drawImage( block, pixX + 38 + 461, pixY + 45 + 32, this );
                    }
                    if( world.gamePanel.matrix[ currentLevel ][ x ][ y ].isGoal() ) {
                        g.drawImage( goal, pixX + 38 + 461, pixY + 45 + 32, this );
                    }
                    if( world.gamePanel.matrix[ currentLevel ][ x ][ y ].isReached() ) {
                        g.drawImage( reached, pixX + 38 + 461, pixY + 45 + 32, this );
                    }
                    if( playerXPos == x && playerYPos == y ) {
                        switch( playerDirection ) {
                          case 0:
                               {
                                  g.drawImage( player0, pixX + 38 + 461, pixY + 45 + 32, this );
                                  break;
                              }
                          
                          case 1:
                               {
                                  g.drawImage( player1, pixX + 38 + 461, pixY + 45 + 32, this );
                                  break;
                              }
                          
                          case 2:
                               {
                                  g.drawImage( player2, pixX + 38 + 461, pixY + 45 + 32, this );
                                  break;
                              }
                          
                          case 3:
                               {
                                  g.drawImage( player3, pixX + 38 + 461, pixY + 45 + 32, this );
                                  break;
                              }
                        }
                    }
                    pixX = pixX + 12;
                }
                else {
                    pixX = pixX + 12;
                }
            }
            pixY = pixY + 12;
            pixX = 0;
        }
    }
    
    //------------------------------------------------------
    
    // bei jedem Schritt des Spielers,ver�ndere die Hintergrundgrafik
    private void changeBackgroundGraphic() {
        if( backgroundFlag == 0 ) {
            backgroundFlag++;
        }
        else {
            backgroundFlag--;
        }
    }
    
    //------------------------------------------------------
    
    // verändere die Hintergrundgrafik, bei jeder Richtungsänderung des Spielers
    /**
     * change the background graphic every time the player changes direction
     */
    private void setBackgroundGraphic() {
        if( ( world.player.getDirection() == 0 ) || ( world.player.getDirection() == 2 ) ) {
            if( backgroundFlag == 0 ) {
                background = back0;
            }
            else {
                background = back1;
            }
        }
        if( ( world.player.getDirection() == 1 ) || ( world.player.getDirection() == 3 ) ) {
            if( backgroundFlag == 0 ) {
                background = back2;
            }
            else {
                background = back3;
            }
        }
    }
    
    //------------------------------------------------------
    
    // speichere Configuration
    /**
     * save the game configuration
     */
    private void saveCFG() {
        INI.setValue( "INTVALUES", "blockmode", blockmode );
        INI.setValue( "BOOLVALUES", "music", music );
        INI.setValue( "BOOLVALUES", "sound", sound_on );
        INI.setValue( "BOOLVALUES", "3DPanel", no3d_on );
        INI.setValue( "BOOLVALUES", "classicMovement", classic_on );
        INI.saveIni();
        JOptionPane.showMessageDialog( null, "Configuration saved", "information", JOptionPane.INFORMATION_MESSAGE );
    }
    
    //------------------------------------------------------
    
    // wird aufgerufen wenn Spieler sich gen Norden bewegt
    /**
     * called when player moves north
     */
    private void goNorth()
    {
        int mdx = 0;
        int mdy = 0;
        int mdpx = 0;
        int mdpy = 0;
        switch( world.player.getDirection() ) {
          case 0:
               {
                  mdx = 0;
                  mdy = -1;
                  mdpx = 0;
                  mdpy = -2;
                  break;
              }
          
          case 1:
               {
                  mdx = 1;
                  mdy = 0;
                  mdpx = 2;
                  mdpy = 0;
                  break;
              }
          
          case 2:
               {
                  mdx = 0;
                  mdy = 1;
                  mdpx = 0;
                  mdpy = 2;
                  break;
              }
          
          case 3:
               {
                  mdx = -1;
                  mdy = 0;
                  mdpx = -2;
                  mdpy = 0;
                  break;
              }
        }
        
        //bewege nur den Spieler
        if( ( matrix[ currentLevel ][ world.player.getXPos() + mdx ]
                [ world.player.getYPos() + mdy ].isZone() ) 
                && ( !matrix[ currentLevel ][ world.player.getXPos() + mdx ]
                        [ world.player.getYPos() + mdy ].isBlocked() ) )
        {
            undoBufferPlayerX.addElement( Integer.toString( world.player.getXPos() ) );
            undoBufferPlayerY.addElement( Integer.toString( world.player.getYPos() ) );
            undoBufferPlayerDirection.addElement( Integer.toString( world.player.getDirection() ) );
            undoBufferBlockbeforeX.addElement( Integer.toString( -1 ) );
            undoBufferBlockbeforeY.addElement( Integer.toString( -1 ) );
            undoBufferBlockafterX.addElement( Integer.toString( -1 ) );
            undoBufferBlockafterY.addElement( Integer.toString( -1 ) );
            undoBufferPlayerhasMoved.addElement( "true" );
            world.player.setXPos( mdx );
            world.player.setYPos( mdy );
            moves++;
            if( sound_on ) {
                 Ambient2.playStep();
            }
            changeBackgroundGraphic();
            repaint();
        }
        
        //bewege Spieler + Blocker 
        else {
            if( ( matrix[ currentLevel ][ world.player.getXPos() + mdx ][ world.player.getYPos() + mdy ].isBlocked() ) && ( matrix[ currentLevel ][ world.player.getXPos() + mdpx ][ world.player.getYPos() + mdpy ].isZone() ) && ( !matrix[ currentLevel ][ world.player.getXPos() + mdpx ][ world.player.getYPos() + mdpy ].isGoal() ) && ( !matrix[ currentLevel ][ world.player.getXPos() + mdpx ][ world.player.getYPos() + mdpy ].isBlocked() ) ) {
                undoBufferPlayerX.addElement( Integer.toString( world.player.getXPos() ) );
                undoBufferPlayerY.addElement( Integer.toString( world.player.getYPos() ) );
                undoBufferPlayerDirection.addElement( Integer.toString( world.player.getDirection() ) );
                matrix[ currentLevel ][ world.player.getXPos() + mdx ][ world.player.getYPos() + mdy ].setBlocked( false );
                matrix[ currentLevel ][ world.player.getXPos() + mdpx ][ world.player.getYPos() + mdpy ].setBlocked( true );
                matrix[ currentLevel ][ world.player.getXPos() + mdx ][ world.player.getYPos() + mdy ].setReached( false );
                undoBufferBlockbeforeX.addElement( Integer.toString( world.player.getXPos() + mdx ) );
                undoBufferBlockbeforeY.addElement( Integer.toString( world.player.getYPos() + mdy ) );
                undoBufferBlockafterX.addElement( Integer.toString( world.player.getXPos() + mdpx ) );
                undoBufferBlockafterY.addElement( Integer.toString( world.player.getYPos() + mdpy ) );
                undoBufferPlayerhasMoved.addElement( "true" );
                if( sound_on ) {
                     Ambient2.playBlockMove();
                }
                world.player.setXPos( mdx );
                world.player.setYPos( mdy );
                moves++;
                
                changeBackgroundGraphic();
                randomBlockPaint = false;
                repaint();
            }
            
            //bewege Spieler + Blocker in eine "ZielZone"
            else {
                if( ( matrix[ currentLevel ][ world.player.getXPos() + mdx ][ world.player.getYPos() + mdy ].isBlocked() ) && ( matrix[ currentLevel ][ world.player.getXPos() + mdpx ][ world.player.getYPos() + mdpy ].isZone() ) && ( matrix[ currentLevel ][ world.player.getXPos() + mdpx ][ world.player.getYPos() + mdpy ].isGoal() ) && ( !matrix[ currentLevel ][ world.player.getXPos() + mdpx ][ world.player.getYPos() + mdpy ].isBlocked() ) ) {
                    undoBufferPlayerX.addElement( Integer.toString( world.player.getXPos() ) );
                    undoBufferPlayerY.addElement( Integer.toString( world.player.getYPos() ) );
                    undoBufferPlayerDirection.addElement( Integer.toString( world.player.getDirection() ) );
                    matrix[ currentLevel ][ world.player.getXPos() + mdx ][ world.player.getYPos() + mdy ].setBlocked( false );
                    matrix[ currentLevel ][ world.player.getXPos() + mdx ][ world.player.getYPos() + mdy ].setReached( false );
                    matrix[ currentLevel ][ world.player.getXPos() + mdpx ][ world.player.getYPos() + mdpy ].setBlocked( true );
                    matrix[ currentLevel ][ world.player.getXPos() + mdpx ][ world.player.getYPos() + mdpy ].setReached( true );
                    undoBufferBlockbeforeX.addElement( Integer.toString( world.player.getXPos() + mdx ) );
                    undoBufferBlockbeforeY.addElement( Integer.toString( world.player.getYPos() + mdy ) );
                    undoBufferBlockafterX.addElement( Integer.toString( world.player.getXPos() + mdpx ) );
                    undoBufferBlockafterY.addElement( Integer.toString( world.player.getYPos() + mdpy ) );
                    undoBufferPlayerhasMoved.addElement( "true" );
                    if( sound_on ) {
                         Ambient2.playBlockMove();
                    }
                    world.player.setXPos( mdx );
                    world.player.setYPos( mdy );
                    moves++;
                    
                    changeBackgroundGraphic();
                    checkGoal();
                    randomBlockPaint = false;
                    repaint();
                }
                else {
                    if( sound_on ) {
                        randomBlockPaint = true;
                         Ambient2.playThud();
                        
                        repaint();
                    }
                }
            }
        }
    }
    
    //------
    
    //speichere alte Positionen in den UndoPuffer
    private void saveUndo( String hasMoved ) {
        undoBufferPlayerX.addElement( Integer.toString( world.player.getXPos() ) );
        undoBufferPlayerY.addElement( Integer.toString( world.player.getYPos() ) );
        undoBufferPlayerDirection.addElement( Integer.toString( world.player.getDirection() ) );
        undoBufferBlockbeforeX.addElement( Integer.toString( -1 ) );
        undoBufferBlockbeforeY.addElement( Integer.toString( -1 ) );
        undoBufferBlockafterX.addElement( Integer.toString( -1 ) );
        undoBufferBlockafterY.addElement( Integer.toString( -1 ) );
        undoBufferPlayerhasMoved.addElement( hasMoved );
    }
    
    //------------------------------------------------------
    
    // wird aufgerufen wenn Spieler sich "rückwärts" bewegt
    /**
     * called when player moves backwords
     */
    private void goSouth() {
        if( world.player.getDirection() == 0 ) {
        if( matrix[ currentLevel ][ world.player.getXPos() ][ world.player.getYPos() + 1 ].isSpace() ) {
            
                saveUndo( "true" );
                changeBackgroundGraphic();
                world.player.setYPos( +1 );
                moves++;
                
                if( sound_on ) {
            	 Ambient2.playStep();
        		}
                repaint();
            
            
        }
        else{
            if( sound_on ) { Ambient2.playThud();}	
        }
    }
    
    if( world.player.getDirection() == 1 ) {
        if( matrix[ currentLevel ][ world.player.getXPos() - 1 ][ world.player.getYPos() ].isSpace() ) {
            
                saveUndo( "true" );
                changeBackgroundGraphic();
                world.player.setXPos( -1 );
                moves++;
                
                if( sound_on ) {
            	 Ambient2.playStep();
        		}
                repaint();
            
            
        }
        else{
            if( sound_on ) { Ambient2.playThud();}	
        }
    }
        
        
        
        
            
        if( world.player.getDirection() == 2 ) {    
        if( matrix[ currentLevel ][ world.player.getXPos() ][ world.player.getYPos() - 1 ].isSpace() ) {
            
                saveUndo( "true" );
                changeBackgroundGraphic();
                world.player.setYPos( -1 );
                moves++;
                
                if( sound_on ) {
            	 Ambient2.playStep();
        		}
                repaint();
            
            
        }
        else{
            if( sound_on ) { Ambient2.playThud();}	
        }
    }
            
        
        if( world.player.getDirection() == 3 )
        {    
            if( matrix[ currentLevel ][ world.player.getXPos() + 1 ][ world.player.getYPos() ].isSpace() ) 
            {
            
                saveUndo( "true" );
                changeBackgroundGraphic();
                world.player.setXPos( +1 );
                moves++;
                
                if( sound_on )
                {
                    Ambient2.playStep();
        	}
                repaint();
            
            
            }
            else
            {
                if( sound_on ) { Ambient2.playThud();}	
            }
    	}
    }
    
    //------------------------------------------------------
    
    // wird aufgerufen wenn Spieler sich nach Links dreht
    /**
     * called when player turnes "left".
     */
    private void goLeft() {
        if( sound_on ) {
             Ambient2.playStep();
        }
        saveUndo( "false" );
        world.player.setDirection( -1 );
        
        changeBackgroundGraphic();
        repaint();
    }
    
    //------------------------------------------------------
    
    // wird aufgerufen wenn Spieler sich nach Rechts dreht
    private void goRight() {
        if( sound_on ) {
             Ambient2.playStep();
        }
        saveUndo( "false" );
        world.player.setDirection( 1 );
        
        changeBackgroundGraphic();
        repaint();
    }
    
    //------------------------------------------------------
    
    // wird aufgerufen wenn der klassische BewegungsModus eingeschaltet ist
    private void goNorthClassic() {
        world.player.setDirectionAbs( 0 );
        goNorth();
    }
    
    private void goEastClassic() {
        world.player.setDirectionAbs( 1 );
        goNorth();
    }
    private void goWestClassic() {
        world.player.setDirectionAbs( 3 );
        goNorth();
    }
    private void goSouthClassic() {
        world.player.setDirectionAbs( 2 );
        goNorth();
    }
    
    //------------------------------------------------------
    
    // wird aufgerufen wenn der Button UNDO gedr�ckt wird
    private void undo() {
        if( undoBufferPlayerX.size() != 0 ) {
            try {
                world.player.setXPosAbs( Integer.parseInt( (String)( undoBufferPlayerX.lastElement() ) ) );
                world.player.setYPosAbs( Integer.parseInt( (String)( undoBufferPlayerY.lastElement() ) ) );
                world.player.setDirectionAbs( Integer.parseInt( (String)( undoBufferPlayerDirection.lastElement() ) ) );
                undoBufferPlayerX.removeElementAt( undoBufferPlayerX.size() - 1 );
                undoBufferPlayerY.removeElementAt( undoBufferPlayerY.size() - 1 );
                undoBufferPlayerDirection.removeElementAt( undoBufferPlayerDirection.size() - 1 );
                if( ( Integer.parseInt( (String)( undoBufferBlockbeforeX.lastElement() ) ) ) != -1 ) {
                    matrix[ currentLevel ][ Integer.parseInt( (String)( undoBufferBlockbeforeX.lastElement() ) ) ][ Integer.parseInt( (String)( undoBufferBlockbeforeY.lastElement() ) ) ].setBlocked( true );
                    matrix[ currentLevel ][ Integer.parseInt( (String)( undoBufferBlockafterX.lastElement() ) ) ][ Integer.parseInt( (String)( undoBufferBlockafterY.lastElement() ) ) ].setBlocked( false );
                    matrix[ currentLevel ][ Integer.parseInt( (String)( undoBufferBlockafterX.lastElement() ) ) ][ Integer.parseInt( (String)( undoBufferBlockafterY.lastElement() ) ) ].setReached( false );
                    matrix[ currentLevel ][ Integer.parseInt( (String)( undoBufferBlockbeforeX.lastElement() ) ) ][ Integer.parseInt( (String)( undoBufferBlockbeforeY.lastElement() ) ) ].setReached( true );
                }
                undoBufferBlockbeforeX.removeElementAt( undoBufferBlockbeforeX.size() - 1 );
                undoBufferBlockbeforeY.removeElementAt( undoBufferBlockbeforeY.size() - 1 );
                undoBufferBlockafterX.removeElementAt( undoBufferBlockafterX.size() - 1 );
                undoBufferBlockafterY.removeElementAt( undoBufferBlockafterY.size() - 1 );
                if( ( (String)( undoBufferPlayerhasMoved.lastElement() ) ).equals( "true" ) ) {
                    moves++;
                }
                undoBufferPlayerhasMoved.removeElementAt( undoBufferPlayerhasMoved.size() - 1 );
                changeBackgroundGraphic();
                repaint();
            }
            catch( ArrayIndexOutOfBoundsException e ) {
                System.out.println( e );
            }
        }
    }
    
    //------------------------------------------------------
    
    // prüft ob alle "Blocker" in jeweils einer "ZielZone" sind
    private void checkGoal() {
        int currentlyReached = 0;
        for( int j = 0;j < 21;j++ ) {
            for( int k = 0;k < 21;k++ ) {
                if( matrix[ currentLevel ][ j ][ k ].isReached() ) {
                    currentlyReached++;
                }
            }
        }
        if( currentlyReached == maximumReached[ currentLevel ] ) {
            if( !( world.playername.equals( "training-mode" ) ) ) {
                world.playerDB.addScore( world.playername, currentLevel, moves );
                world.playerDB.saveDB(getClass(), 
                WAppReadResources.getValue(PlayerDB.bundle, "dataBaseOut"));
                world.playerDB.updateScoreTable();
                if( currentLevel != 89 ) {
                    JOptionPane.showMessageDialog( null, "Congratulations!\nPrepare for the next Level!\n\nTotal Moves: " + moves + "\n\n", "info", JOptionPane.INFORMATION_MESSAGE );
                }
            }
            goNextLevel();
        }
    }
    
    //------------------------------------------------------
    
    // startet den nächsten Level
    private void goNextLevel() {
        if( !( world.playername.equals( "training-mode" ) ) ) {
            if( currentLevel != 89 ) {
                currentLevel++;
                world.player.setXPosAbs( matrixPlayerXPos[ currentLevel ] );
                world.player.setYPosAbs( matrixPlayerYPos[ currentLevel ] );
                world.player.setDirectionAbs( directions[ currentLevel ] );
                changeBackgroundGraphic();
                moves = 0;
                resetUndoBuffer();
            }
            else {
                
                // falls der Abspann erscheinen soll
                gamesolved = true;
                undo_b.setVisible( false );
                reset_b.setVisible( false );
                left_b.setVisible( false );
                right_b.setVisible( false );
                world.gui.music0.stopSound();
                world.gui.music1.startSound();
            }
        }
        else {
            JOptionPane.showMessageDialog( null, "Congratulations!\nLevel finished!\n\nTotal Moves: " + moves + "\n\n", "info", JOptionPane.INFORMATION_MESSAGE );
            resetLevel();
        }
        repaint();
    }
    
    //------------------------------------------------------
    
    // springt zum nächsten Level falls "training-mode"
    private void jump2NextLevel() {
        if( currentLevel != 89 ) {
            resetLevel();
            currentLevel++;
            world.player.setXPosAbs( matrixPlayerXPos[ currentLevel ] );
            world.player.setYPosAbs( matrixPlayerYPos[ currentLevel ] );
            world.player.setDirectionAbs( directions[ currentLevel ] );
            changeBackgroundGraphic();
            moves = 0;
            resetUndoBuffer();
            repaint();
        }
    }
    
    //------------------------------------------------------
    
    // springt zum vorherigen Level falls "training-mode"
    private void jump2PreviousLevel() {
        if( currentLevel != 0 ) {
            resetLevel();
            currentLevel--;
            world.player.setXPosAbs( matrixPlayerXPos[ currentLevel ] );
            world.player.setYPosAbs( matrixPlayerYPos[ currentLevel ] );
            world.player.setDirectionAbs( directions[ currentLevel ] );
            changeBackgroundGraphic();
            moves = 0;
            resetUndoBuffer();
            repaint();
        }
    }
    
    //------------------------------------------------------
    
    // setzt den Level zurück
    private void resetLevel() {
        fillCubes();
        createDoors();
        world.player.setDirectionAbs( directions[ currentLevel ] );
        if( undoBufferPlayerhasMoved.size() != 0 ) {
            changeBackgroundGraphic();
        }
        moves = 0;
        resetUndoBuffer();
        repaint();
    }
    
    //------------------------------------------------------
 // leert den UndoPuffer
    private void resetUndoBuffer() {
        undoBufferPlayerX.removeAllElements();
        undoBufferPlayerY.removeAllElements();
        undoBufferPlayerDirection.removeAllElements();
        undoBufferBlockbeforeX.removeAllElements();
        undoBufferBlockbeforeY.removeAllElements();
        undoBufferBlockafterX.removeAllElements();
        undoBufferBlockafterY.removeAllElements();
        undoBufferPlayerhasMoved.removeAllElements();
    }
    
    //------------------------------------------------------
    
    // setzt das Spiel samt aller 90 Levels zurück
    /**
     * method resets the game
     */
    public void resetGame() {
        currentLevel = 0;
        moves = 0;
        fillCubes();
        createDoors();
        world.player.setDirectionAbs( 0 );
        changeBackgroundGraphic();
        world.player.setXPosAbs( matrixPlayerXPos[ currentLevel ] );
        world.player.setYPosAbs( matrixPlayerYPos[ currentLevel ] );
        resetUndoBuffer();
        undo_b.setVisible( true );
        reset_b.setVisible( true );
        if( world.playername.equals( "training-mode" ) ) {
            left_b.setVisible( true );
            right_b.setVisible( true );
        }
        else {
            left_b.setVisible( false );
            right_b.setVisible( false );
        }
        gamesolved = false;
    }
    
    //------------------------------------------------------
    
    // springt zum MainMenu
    private void exitGame() {
        world.gui.music0.stopSound();
        world.gui.music1.stopSound();
        world.gui.music.restartSound();
        world.gui.music.startSound();
        world.setVisible( false );
        world.gui.hideAllPanels();
        world.gui.showMainPanels();
        world.gui.setVisible( true );
    }
}