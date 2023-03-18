/**
 * 
 */
package sokoban3d;

/**
 * @author walter2
 *
 */

import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

/**
 * 
 * Gui<br>
 * extends javx.swing.JFrame
 * load sounds and grafics, and build the gui
 * 
 */

public class Gui extends JFrame implements ActionListener,ItemListener 
{
	/**
	 * different sounds
	 */
    public Music music, music0, music1;
    private String selectedPlayer;
    private String selectedLevel;
    private Sokoban3D sokoban3d = new Sokoban3D( this );
    private Image gui0, gui1, gui2;
    private Icon newgame0, newgame1, loadgame0, loadgame1, highscore0, highscore1, about, about0, about1, start0, start1, cancel0, cancel1, delete0, delete1, training0, training1;
    private JButton b_0, b_1, b_2, b_3, b_4, b_5, b_6, b_7, b_8, b_9, b_10, b_11;
    private JTable scoretable;
    private JTextField tf;
    private JComboBox cb, cb_levels;
    private BlackPanel bp0, bp1, bp2, bp3, bp4, bp5, bp6, bp7;
    private Gui0Panel gui0panel;
    private Gui1Panel gui1panel;
    private Gui2Panel gui2panel;
    private ButtonPanel buttonpanel;
    private CreatePanel createpanel;
    private OkPanel okpanel0;
    private OkPanel okpanel1;
    private InputPanel inputpanel;
    private ComboPanel combopanel;
    private ComboPanel combopanel_levels;
    private ScorePanel scorepanel;
    private boolean soundcard=true;
    private Class thisClass;
    
    /**
     * 
     */
    public static boolean debug = false;
    
    //------------------------------------------------------
    /**
     * 
     * @param m sound1
     * @param m0 sound2
     * @param m1 sound3
     * set the available sounds
     */
    public Gui(Music m, Music m0, Music m1)
    {
       super( "Sokoban3D - Main Menu" );
       thisClass = getClass();
       music = m;
       music0 = m0;
       music1 = m1;
    }

    /**
     * create, load and play sound
     */
    public void createSound()
    {
      music.setClass(thisClass);
      music0.setClass(thisClass);
      music1.setClass(thisClass);
      loadSound(music, music0, music1);
    }

    /**
     * 
     * @param m
     * @param m0
     * @param m1 
     */
    //Load midi sounds in a separate thread.
    private void loadSound(final Music m, final Music m0, final Music m1)
    {
      final SwingWorker worker = new SwingWorker()
      {
        final Music mc = m;
        final Music mc0 = m0;
        final Music mc1 = m1;
        
        public Object construct()
        {
            mc.initSound("MusicIntro.mid");
            mc0.initSound("MusicIntro.mid");
            mc1.initSound("MusicIntro.mid");
            /*mc.initSound1("MusicIntro.mid");
            mc0.initSound1("LetItBe.mid");
            mc1.initSound1("2001.mid");*/
            new Ambient2();
            return null; //return value not used by this program
        }

        //Runs on the event-dispatching thread.
        public void finished()
        {
            if(mc.soundcard)
              mc.startSound();	
            else
              if (Gui.debug) System.out.println("\nSoundcard(MIDI) not found...Game will run without Sound");
        }
      };
      worker.start();  //required for SwingWorker 3
    }
    
    /**
     *  thread to create the gui
     */
    public void startCreate()
    {
       Thread cReate = new Thread()
       {
           public void run()
           {
             createGui();
           }
       };
       cReate.start();
    }

    /**
     * load the images needed to create the gui
     */
    private void createGui()
    {
        getContentPane().setLayout( null );
        this.addWindowListener( new WindowAdapter()  {
            public void windowClosing( WindowEvent e ) {
                exit();
            }
        } );

        MediaTracker mt = new MediaTracker( this );
        thisClass = getClass();
        // changed 02.06.12 by Walter Hipp
        gui0=ImageLoader.loadaImage( "pix/gui/gui0.res", thisClass );
        gui1=ImageLoader.loadaImage( "pix/gui/gui1.res", thisClass );
        gui2=ImageLoader.loadaImage( "pix/gui/gui2.res", thisClass );
        mt.addImage( gui0, 0 );
        mt.addImage( gui1, 1 );
        mt.addImage( gui2, 2 );
        try {
            mt.waitForAll();
        }
        catch( InterruptedException e ) {
            System.out.println( "BackgroundImages not found!" );
        }

        //changed 02.06.12 by Walter Hipp
        newgame0 = ImageLoader.loadaIcon( "pix/gui/newgame0.res", thisClass );
        newgame1 = ImageLoader.loadaIcon( "pix/gui/newgame1.res", thisClass );
        loadgame0 = ImageLoader.loadaIcon( "pix/gui/loadgame0.res", thisClass );
        loadgame1 = ImageLoader.loadaIcon( "pix/gui/loadgame1.res", thisClass );
        highscore0 = ImageLoader.loadaIcon( "pix/gui/highscore0.res", thisClass );
        highscore1 = ImageLoader.loadaIcon( "pix/gui/highscore1.res", thisClass );
        training0 = ImageLoader.loadaIcon( "pix/gui/training0.res", thisClass );
        training1 = ImageLoader.loadaIcon( "pix/gui/training1.res", thisClass );
        about = ImageLoader.loadaIcon( "pix/gui/about.res", thisClass );
        about0 = ImageLoader.loadaIcon( "pix/gui/about0.res", thisClass );
        about1 = ImageLoader.loadaIcon( "pix/gui/about1.res", thisClass );
        start0 = ImageLoader.loadaIcon( "pix/gui/start0.res", thisClass );
        start1 = ImageLoader.loadaIcon( "pix/gui/start1.res", thisClass );
        cancel0 = ImageLoader.loadaIcon( "pix/gui/cancel0.res", thisClass );
        cancel1 = ImageLoader.loadaIcon( "pix/gui/cancel1.res", thisClass );
        delete0 = ImageLoader.loadaIcon( "pix/gui/delete0.res", thisClass );
        delete1 = ImageLoader.loadaIcon( "pix/gui/delete1.res", thisClass );
        inputpanel = new InputPanel();
        inputpanel.setBounds( 27, 96, 152, 30 );
        scorepanel = new ScorePanel();
        scorepanel.setBounds( 220, 10, 311, 219 );
        combopanel = new ComboPanel();
        combopanel.setBounds( 27, 96, 152, 30 );
        combopanel_levels = new ComboPanel();
        combopanel_levels.setBounds( 27, 96, 152, 30 );
        bp0 = new BlackPanel();
        bp0.setBounds( 0, 52, 27, 170 );
        bp1 = new BlackPanel();
        bp1.setBounds( 179, 52, 26, 170 );
        bp2 = new BlackPanel();
        bp2.setBounds( 0, 222, 205, 17 );
        bp3 = new BlackPanel();
        bp3.setBounds( 0, 0, 547, 20 );
        bp4 = new BlackPanel();
        bp4.setBounds( 205, 0, 15, 239 );
        bp5 = new BlackPanel();
        bp5.setBounds( 531, 0, 10, 239 );
        bp6 = new BlackPanel();
        bp6.setBounds( 220, 0, 311, 10 );
        bp7 = new BlackPanel();
        bp7.setBounds( 220, 229, 311, 10 );
        gui0panel = new Gui0Panel();
        gui0panel.setBounds( 0, 0, 205, 52 );
        gui1panel = new Gui1Panel();
        gui1panel.setBounds( 205, 0, 342, 246 );
        gui2panel = new Gui2Panel();
        gui2panel.setBounds( 27, 52, 152, 44 );
        buttonpanel = new ButtonPanel();
        buttonpanel.setBounds( 27, 52, 152, 170 );
        createpanel = new CreatePanel();
        createpanel.setBounds( 27, 126, 152, 96 );
        okpanel0 = new OkPanel();
        okpanel0.setBounds( 27, 126, 152, 96 );
        okpanel1 = new OkPanel();
        okpanel1.setBounds( 27, 126, 152, 96 );
        b_0 = new JButton( "", newgame0 );
        b_0.setBorder( null );
        b_0.setFocusPainted( false );
        b_0.setPressedIcon( newgame1 );
        b_0.setBounds( 10, 15, 138, 24 );
        b_0.setActionCommand( "newgame" );
        b_0.addActionListener( this );
        buttonpanel.add( b_0 );
        b_1 = new JButton( "", loadgame0 );
        b_1.setBorder( null );
        b_1.setFocusPainted( false );
        b_1.setPressedIcon( loadgame1 );
        b_1.setBounds( 9, 49, 138, 24 );
        b_1.setActionCommand( "loadgame" );
        b_1.addActionListener( this );
        buttonpanel.add( b_1 );
        b_2 = new JButton( "", highscore0 );
        b_2.setBorder( null );
        b_2.setFocusPainted( false );
        b_2.setPressedIcon( highscore1 );
        b_2.setBounds( 9, 82, 138, 24 );
        b_2.setActionCommand( "highscore" );
        b_2.addActionListener( this );
        buttonpanel.add( b_2 );
        b_3 = new JButton( "", training0 );
        b_3.setBorder( null );
        b_3.setFocusPainted( false );
        b_3.setPressedIcon( training1 );
        b_3.setBounds( 10, 116, 138, 24 );
        b_3.setActionCommand( "training" );
        b_3.addActionListener( this );
        buttonpanel.add( b_3 );
        b_4 = new JButton( "", start0 );
        b_4.setBorder( null );
        b_4.setFocusPainted( false );
        b_4.setPressedIcon( start1 );
        b_4.setBounds( 10, 7, 138, 24 );
        b_4.setActionCommand( "create" );
        b_4.addActionListener( this );
        createpanel.add( b_4 );
        b_5 = new JButton( "", start0 );
        b_5.setBorder( null );
        b_5.setFocusPainted( false );
        b_5.setPressedIcon( start1 );
        b_5.setBounds( 10, 7, 138, 24 );
        b_5.setActionCommand( "loadgame_ok" );
        b_5.addActionListener( this );
        okpanel0.add( b_5 );
        b_6 = new JButton( "", cancel0 );
        b_6.setBorder( null );
        b_6.setFocusPainted( false );
        b_6.setPressedIcon( cancel1 );
        b_6.setBounds( 10, 36, 138, 24 );
        b_6.setActionCommand( "cancel" );
        b_6.addActionListener( this );
        createpanel.add( b_6 );
        b_7 = new JButton( "", cancel0 );
        b_7.setBorder( null );
        b_7.setFocusPainted( false );
        b_7.setPressedIcon( cancel1 );
        b_7.setBounds( 10, 36, 138, 24 );
        b_7.setActionCommand( "cancel" );
        b_7.addActionListener( this );
        okpanel0.add( b_7 );
        b_8 = new JButton( "", delete0 );
        b_8.setBorder( null );
        b_8.setFocusPainted( false );
        b_8.setPressedIcon( delete1 );
        b_8.setBounds( 10, 65, 138, 24 );
        b_8.setActionCommand( "loadgame_deleteplayer" );
        b_8.addActionListener( this );
        okpanel0.add( b_8 );
        b_9 = new JButton( "", start0 );
        b_9.setBorder( null );
        b_9.setFocusPainted( false );
        b_9.setPressedIcon( start1 );
        b_9.setBounds( 10, 7, 138, 24 );
        b_9.setActionCommand( "training_ok" );
        b_9.addActionListener( this );
        okpanel1.add( b_9 );
        b_10 = new JButton( "", cancel0 );
        b_10.setBorder( null );
        b_10.setFocusPainted( false );
        b_10.setPressedIcon( cancel1 );
        b_10.setBounds( 10, 36, 138, 24 );
        b_10.setActionCommand( "cancel" );
        b_10.addActionListener( this );
        okpanel1.add( b_10 );
        b_11 = new JButton( "", about0 );
        b_11.setBorder( null );
        b_11.setFocusPainted( false );
        b_11.setPressedIcon( about1 );
        b_11.setBounds( 36, 150, 81, 20 );
        b_11.setActionCommand( "about" );
        b_11.addActionListener( this );
        buttonpanel.add( b_11 );
        cb = new JComboBox<Object>( sokoban3d.playerDB.players );
        cb.addItemListener( this );
        cb.setMaximumRowCount( 5 );
        Object LEVELS[] = new Object[ 90 ];
        for( int i = 0;i < 90;i++ ) {
            LEVELS[ i ] = "" + ( i + 1 );
        }
        cb_levels = new JComboBox<Object>( LEVELS );
        cb_levels.addItemListener( this );
        cb_levels.setMaximumRowCount( 5 );
        cb_levels.setEditable( true );
        combopanel.add( cb );
        combopanel_levels.add( cb_levels );
        getContentPane().add( bp0 );
        getContentPane().add( bp1 );
        getContentPane().add( bp2 );
        getContentPane().add( bp3 );
        getContentPane().add( bp4 );
        getContentPane().add( bp5 );
        getContentPane().add( bp6 );
        getContentPane().add( bp7 );
        getContentPane().add( gui0panel );
        getContentPane().add( gui1panel );
        getContentPane().add( gui2panel );
        getContentPane().add( buttonpanel );
        getContentPane().add( createpanel );
        getContentPane().add( inputpanel );
        getContentPane().add( combopanel );
        getContentPane().add( combopanel_levels );
        getContentPane().add( okpanel0 );
        getContentPane().add( okpanel1 );
        getContentPane().add( scorepanel );
        showMainPanels();

        //
        //setSize( 547, 64 );
        setSize( 560, 280);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();
        setLocation( ( screenSize.width - frameSize.width ) / 2, ( screenSize.height - frameSize.height ) / 2 );
        setResizable( false );
        setVisible( true );
        
    }
    
    //------------------------------------------------------
    /**
     * 
     * @param event
     * used to call different actions
     */
    public void actionPerformed( ActionEvent event ) {
        String cmd = event.getActionCommand();
        if( cmd.equals( "newgame" ) ) {
            newGame();
        }
        if( cmd.equals( "loadgame" ) ) {
            if( sokoban3d.playerDB.players.size() != 0 ) {
                hideAllPanels();
                showComboPanels();
            }
            else {
                JOptionPane.showMessageDialog( null, "No Savegames available. Create a Player first.", "information", JOptionPane.INFORMATION_MESSAGE );
            }
        }
        if( cmd.equals( "highscore" ) ) {
            highscore();
        }
        if( cmd.equals( "create" ) ) {
            createGame();
        }
        if( cmd.equals( "loadgame_ok" ) ) {
            continueGame();
        }
        if( cmd.equals( "loadgame_deleteplayer" ) ) {
            deletePlayer();
        }
        if( cmd.equals( "cancel" ) ) {
            hideAllPanels();
            showMainPanels();
        }
        if( cmd.equals( "training" ) ) {
            hideAllPanels();
            showComboPanels_Levels();
        }
        if( cmd.equals( "training_ok" ) ) {
            trainingGame();
        }
        if( cmd.equals( "about" ) ) {
            JOptionPane.showMessageDialog( null, "", "About", JOptionPane.INFORMATION_MESSAGE, about );
        }
    }
    
    //------------------------------------------------------
    public void itemStateChanged( ItemEvent event ) {
        JComboBox cbtemp = (JComboBox)event.getSource();
        selectedPlayer = (String)cbtemp.getSelectedItem();
        selectedLevel = (String)cbtemp.getSelectedItem();
    }
    
    //------------------------------------------------------
    private void setDefaultPlayer() {
        if( sokoban3d.playerDB.players.size() != 0 ) {
            selectedPlayer = (String)sokoban3d.playerDB.players.elementAt( 0 );
        }
    }
    
    //------------------------------------------------------
    /**
     * 
     * Panel for game messages
     *
     */
    class InputPanel extends JPanel {
    	/**
    	 * create the panel
    	 */
        public InputPanel() {
            setLayout( new GridLayout( 1, 1 ) );
            tf = new JTextField( "", 10 );
            tf.setBorder( null );
            add( tf );
            setVisible( false );
        }
    }
    
    //------------------------------------------------------
    /**
     * 
     * panel show score Table
     *
     */
    class ScorePanel extends JPanel {
    	/**
    	 * create the panel
    	 */
        public ScorePanel() {
            setLayout( new GridLayout( 1, 1 ) );
            sokoban3d.playerDB.updateScoreTable();
            //scoretable = new JTable( sokoban3d.playerDB.tabelle[ 0 ], sokoban3d.playerDB.tabelle[ 1 ] );
            scoretable = new JTable(sokoban3d.playerDB.rowData,
                sokoban3d.playerDB.coldata);
            scoretable.setDefaultEditor( Object.class, null );
            scoretable.getTableHeader().setReorderingAllowed( false );
            add( new JScrollPane( scoretable ), "Center" );
            setVisible( false );
        }
        private void updateTable() {
            scoretable.updateUI();
        }
    }
    
    //------------------------------------------------------
    private class ComboPanel extends JPanel {
        public ComboPanel() {
            setLayout( new GridLayout( 1, 1 ) );
            setVisible( false );
        }
    }
    
    //------------------------------------------------------
   private  class BlackPanel extends JPanel {
        public BlackPanel() {
            setLayout( null );
            setBackground( Color.black );
            setVisible( false );
        }
    }
    
    //------------------------------------------------------
   /**
    * 
    * panel for drawing images
    *
    */
    class Gui0Panel extends JPanel {
    	/**
    	 * create the panel
    	 */
        public Gui0Panel() {
            setLayout( null );
            setVisible( false );
        }
        public void paintComponent( Graphics g ) {
            super.paintComponent( g );
            g.drawImage( gui0, 0, 0, this );
        }
    }
    
    //------------------------------------------------------
    private class Gui1Panel extends JPanel {
        public Gui1Panel() {
            setLayout( null );
            setVisible( false );
        }
        public void paintComponent( Graphics g ) {
            super.paintComponent( g );
            g.drawImage( gui1, 0, 0, this );
            g.setColor( Color.gray );
            g.drawString( "version 1.05, Copyright (C) 2002 Nurdogan Erdem", 55, 234 );
        }
    }
    
    //------------------------------------------------------
    /**
     * 
     * panel for graphics
     *
     */
    class Gui2Panel extends JPanel {
    	/**
    	 * create the panel
    	 */
        public Gui2Panel() {
            setLayout( null );
            setVisible( false );
        }
        public void paintComponent( Graphics g ) {
            super.paintComponent( g );
            g.drawImage( gui2, 0, 0, this );
        }
    }
    
    //------------------------------------------------------
    /**
     * 
     * Buttons Panel
     *
     */
    class ButtonPanel extends JPanel {
    	/**
    	 * create the panel
    	 */
        public ButtonPanel() {
            setLayout( null );
            setBackground( Color.black );
            setVisible( false );
        }
    }
    
    //-----------------------------------------------------
    /**
     * 
     * Panel
     *
     */
    private class CreatePanel extends JPanel {
    	/**
    	 * create the panel
    	 */
        public CreatePanel() {
            setLayout( null );
            setBackground( Color.blue );
            setVisible( false );
        }
    }
    
    //------------------------------------------------------
    private class OkPanel extends JPanel {
    	/**
    	 * create the panel
    	 */
        public OkPanel() {
            setLayout( null );
            setBackground( Color.black );
            setVisible( false );
        }
    }
    
    //------------------------------------------------------
    /**
     * Method make all Panels visible
     */
    public void showMainPanels() {
        bp0.setVisible( true );
        bp1.setVisible( true );
        bp2.setVisible( true );
        gui0panel.setVisible( true );
        gui1panel.setVisible( true );
        buttonpanel.setVisible( true );
    }
    
    //------------------------------------------------------
    /**
     * Method make hide all Panels
     */
    public void hideAllPanels() {
        bp0.setVisible( false );
        bp1.setVisible( false );
        bp2.setVisible( false );
        bp4.setVisible( false );
        bp5.setVisible( false );
        bp6.setVisible( false );
        bp7.setVisible( false );
        gui0panel.setVisible( false );
        gui1panel.setVisible( false );
        gui2panel.setVisible( false );
        buttonpanel.setVisible( false );
        inputpanel.setVisible( false );
        createpanel.setVisible( false );
        combopanel.setVisible( false );
        combopanel_levels.setVisible( false );
        okpanel0.setVisible( false );
        okpanel1.setVisible( false );
    }
    
    //------------------------------------------------------
    private void showInputPanels() {
        bp0.setVisible( true );
        bp1.setVisible( true );
        bp2.setVisible( true );
        gui0panel.setVisible( true );
        gui1panel.setVisible( true );
        gui2panel.setVisible( true );
        inputpanel.setVisible( true );
        createpanel.setVisible( true );
        tf.setText( "" );
        tf.requestFocus();
    }
    
    //------------------------------------------------------
    private void showComboPanels() {
        bp0.setVisible( true );
        bp1.setVisible( true );
        bp2.setVisible( true );
        gui0panel.setVisible( true );
        gui1panel.setVisible( true );
        gui2panel.setVisible( true );
        combopanel.setVisible( true );
        okpanel0.setVisible( true );
        cb.updateUI();
        setDefaultPlayer();
        cb.setSelectedIndex( 0 );
    }
    
    //------------------------------------------------------	
    private void showComboPanels_Levels() {
        bp0.setVisible( true );
        bp1.setVisible( true );
        bp2.setVisible( true );
        gui0panel.setVisible( true );
        gui1panel.setVisible( true );
        gui2panel.setVisible( true );
        okpanel1.setVisible( true );
        combopanel_levels.setVisible( true );
        selectedLevel = "1";
        cb_levels.updateUI();
        cb_levels.setSelectedIndex( 0 );
    }
    
    //------------------------------------------------------
    private void showScorePanels() {
        hideAllPanels();
        bp0.setVisible( true );
        bp1.setVisible( true );
        bp2.setVisible( true );
        bp4.setVisible( true );
        bp5.setVisible( true );
        bp6.setVisible( true );
        bp7.setVisible( true );
        gui0panel.setVisible( true );
        buttonpanel.setVisible( true );
        scorepanel.setVisible( true );
    }
    
    //------------------------------------------------------
    /**
     * Method reset all Panels
     */
    public void newGame() {
        hideAllPanels();
        showInputPanels();
    }
    
    //------------------------------------------------------
    private void highscore() {
        if( sokoban3d.playerDB.highreached ) {
            scorepanel.updateTable();
            showScorePanels();
        }
        else {
            JOptionPane.showMessageDialog( null, "Currently no Highscores reached", "information", JOptionPane.INFORMATION_MESSAGE );
        }
    }
    
    //------------------------------------------------------
    private void deletePlayer() {
        int result = JOptionPane.showConfirmDialog( null, "Would you really delete the Player named " + selectedPlayer + " including all Savegames?", "Delete Player", JOptionPane.YES_NO_OPTION );
        if( result == 0 ) {
            int ind = sokoban3d.playerDB.getIndex( selectedPlayer );
            sokoban3d.playerDB.players.remove( ind );
            sokoban3d.playerDB.scores.remove( ind );
            sokoban3d.playerDB.updateScoreTable();
            sokoban3d.playerDB.saveDB(getClass(), 
                WAppReadResources.getValue(PlayerDB.bundle, "dataBaseOut"));
            if( sokoban3d.playerDB.players.size() != 0 ) {
                cb.setSelectedIndex( 0 );
            }
            else {
                hideAllPanels();
                showMainPanels();
            }
        }
    }
    
    //------------------------------------------------------
    private boolean checkReg_name() {
        boolean result = false;
        String pattern[] =  {
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", 
            "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", 
            "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", 
            "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", 
            "S", "T", "U", "V", "W", "X", "Y", "Z"
        };
        String source = tf.getText();
        int counter = 0;
        for( int i = 0;i < source.length();i++ ) {
            for( int j = 0;j < pattern.length;j++ ) {
                if( ( source.substring( i, i + 1 ) ).equals( pattern[ j ] ) ) {
                    counter++;
                }
            }
        }
        if( counter == source.length() ) {
            result = true;
        }
        return result;
    }
    
    //------------------------------------------------------
    private boolean checkReg_level() {
        boolean result = false;
        String pattern[] =  {
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
        };
        String source = selectedLevel;
        int counter = 0;
        for( int i = 0;i < source.length();i++ ) {
            for( int j = 0;j < pattern.length;j++ ) {
                if( ( source.substring( i, i + 1 ) ).equals( pattern[ j ] ) ) {
                    counter++;
                }
            }
        }
        if( counter == source.length() ) {
            result = true;
        }
        return result;
    }
    
    //------------------------------------------------------
    /**
     * create the game, reset the game, set sound, set game visible to true
     */
    public void createGame() {
        if( checkReg_name() && ( ( tf.getText() ).length() != 0 ) && ( ( tf.getText() ).length() < 15 ) ) {
            if( sokoban3d.playerDB.getIndex( tf.getText() ) == -1 ) {
                sokoban3d.playername = tf.getText();
                if( !( sokoban3d.playername.equals( "training-mode" ) ) ) {
                    sokoban3d.playerDB.addPlayer( tf.getText() );
                    sokoban3d.playerDB.saveDB(getClass(), 
                WAppReadResources.getValue(PlayerDB.bundle, "dataBaseOut"));
                }
                sokoban3d.gamePanel.resetGame();
                music.stopSound();
                if( sokoban3d.gamePanel.music ) {
                    music0.startSound();
                }
                this.setVisible( false );
                sokoban3d.setVisible( true );
            }
            else {
                JOptionPane.showMessageDialog( null, "Player already exist. Please enter another name.", "information", JOptionPane.INFORMATION_MESSAGE );
            }
        }
        else {
            JOptionPane.showMessageDialog( null, "Please use only Character Range a-z and A-Z,\nwith maximal 14 chars for your playername, without whitespace", "information", JOptionPane.INFORMATION_MESSAGE );
        }
    }
    
    //------------------------------------------------------
    private void continueGame() {
        if( ( sokoban3d.playerDB.getScore( selectedPlayer, 89 ) ) == 0 ) {
            music.stopSound();
            if( sokoban3d.gamePanel.music ) {
                music0.startSound();
            }
            sokoban3d.playername = selectedPlayer;
            sokoban3d.gamePanel.resetGame();
            sokoban3d.gamePanel.currentLevel = sokoban3d.playerDB.getLastSolvedLevel( selectedPlayer );
            sokoban3d.player.setXPosAbs( sokoban3d.gamePanel.matrixPlayerXPos[ sokoban3d.playerDB.getLastSolvedLevel( selectedPlayer ) ] );
            sokoban3d.player.setYPosAbs( sokoban3d.gamePanel.matrixPlayerYPos[ sokoban3d.playerDB.getLastSolvedLevel( selectedPlayer ) ] );
            sokoban3d.player.setDirectionAbs( sokoban3d.gamePanel.directions[ sokoban3d.gamePanel.currentLevel ] );
            this.setVisible( false );
            sokoban3d.setVisible( true );
        }
        else {
            JOptionPane.showMessageDialog( null, "Player already finished the last Level.", "information", JOptionPane.INFORMATION_MESSAGE );
        }
    }
    
    //------------------------------------------------------
    private void trainingGame() {
        if( selectedLevel.length() != 0 ) {
            if( checkReg_level() ) {
                if( ( Integer.parseInt( selectedLevel ) > 0 ) && ( Integer.parseInt( selectedLevel ) < 91 ) ) {
                    music.stopSound();
                    if( sokoban3d.gamePanel.music ) {
                        music0.startSound();
                    }
                    ;
                    sokoban3d.playername = "training-mode";
                    sokoban3d.gamePanel.resetGame();
                    int realLevel = Integer.parseInt( selectedLevel );
                    sokoban3d.gamePanel.currentLevel = realLevel - 1;
                    sokoban3d.player.setXPosAbs( sokoban3d.gamePanel.matrixPlayerXPos[ realLevel - 1 ] );
                    sokoban3d.player.setYPosAbs( sokoban3d.gamePanel.matrixPlayerYPos[ realLevel - 1 ] );
                    sokoban3d.player.setDirectionAbs( sokoban3d.gamePanel.directions[ sokoban3d.gamePanel.currentLevel ] );
                    this.setVisible( false );
                    sokoban3d.setVisible( true );
                }
                else {
                    JOptionPane.showMessageDialog( null, "please enter/select a LevelNumber between 0-90", "information", JOptionPane.INFORMATION_MESSAGE );
                }
            }
            else {
                JOptionPane.showMessageDialog( null, "please enter/select a LevelNumber between 0-90", "information", JOptionPane.INFORMATION_MESSAGE );
            }
        }
        else {
            JOptionPane.showMessageDialog( null, "please enter/select a LevelNumber between 0-90", "information", JOptionPane.INFORMATION_MESSAGE );
        }
    }
    
    //------------------------------------------------------
    /**
     * set game Panel visibility to false and exit
     */
    private void exit() {
        try {
            setVisible( false );
            System.exit( 0 );
        }
        catch( Exception e ) {
            
        }
    }
}

