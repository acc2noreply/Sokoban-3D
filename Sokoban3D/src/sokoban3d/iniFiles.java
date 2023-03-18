package sokoban3d;

import java.io.*;
import java.util.*;

/**
 * 
 * defines values
 *
 */
class unterBegriff {
    String Begriff = "";
    String Eintrag = "";
    /**
     * 
     * @param B String value
     * @param E String value
     */
    public unterBegriff( String B, String E ) {
    	//System.out.println("unter begriff " + B + " eintrag " + E);
        Begriff = B;
        Eintrag = E;
    }
}

/**
 * 
 * defines values int, or boolean
 *
 */
class oberBegriff {
    String Begriff = "";
    Vector<Object> UB = new Vector<Object>();
    /**
     * 
     * @param B 5ring value
     */
    public oberBegriff( String B ) {
    	System.out.println("Ober begriff " + B);
        Begriff = B;
    }
}

/**
 * 
 * Class for load and save application initial settings
 *
 */
class iniFiles {
    private String inifile = "";
    private Vector<Object> INIFILE = new Vector<Object>();
    /**
     * main class
     */
    protected Class mainClass;
    String bis( String s, String w ) {
        if( s.indexOf( w ) != -1 ) {
            return s.substring( 0, s.indexOf( w ) );
        }
        return "";
    }
    String hinter( String s, String w ) {
        if( s.indexOf( w ) != -1 ) {
            return s.substring( s.indexOf( w ) + w.length() );
        }
        else {
            return "";
        }
    }
    public void loadIni( String f ) {
        inifile = f;
        loadIni();
    }
    public void loadIni() {
        INIFILE = new Vector<Object>();
        String L = "";
        try {
            BufferedReader f = new BufferedReader( new FileReader( inifile ) );
            do {
                L = f.readLine();
                //System.out.println("ini " + L);
                if( L != null ) {
                    L = L.trim();
                    if( !L.equals( "" ) && !L.startsWith( ";" ) ) {
                        
                        //System.out.println(L);
                        if( L.startsWith( "[" ) ) { // OberBegriff !
                            L = hinter( L, "[" );
                            L = bis( L, "]" );
                            oberBegriff ob = new oberBegriff( L );
                            INIFILE.addElement( ob );
                        }
                        else { // UnterBegriff !
                            String B = bis( L, "=" );
                            String E = hinter( L, "=" );
                            unterBegriff ub = new unterBegriff( B, E );
                            ( (oberBegriff)INIFILE.lastElement() ).UB.addElement( ub );
                        }
                    }
                }
            }while( L != null );
            f.close();
        }
        catch( IOException ioe ) {
            
        }
    }
    public void saveIni( String file ) {
        inifile = file;
        saveIni();
    }
    public void saveIni() {
        try {
            PrintStream f = new PrintStream( new FileOutputStream( inifile ) );
            f.print( ";  INI-Files-CLASS (c) by Aresch Yavari" + (char)13 + (char)10 );
            f.print( ";                         22.11.1997   " + (char)13 + (char)10 );
            f.print( ";  Aresch.Yavari@post.rwth-aachen.de   " + (char)13 + (char)10 );
            f.print( ";  http://www.geocities.com/Area51/9851" + (char)13 + (char)10 );
            f.print( "" + (char)13 + (char)10 );
            for( int i = 0;i < INIFILE.size();i++ ) {
                oberBegriff ob = (oberBegriff)INIFILE.elementAt( i );
                f.print( "[" + ob.Begriff + "]" + (char)13 + (char)10 );
                for( int j = 0;j < ob.UB.size();j++ ) {
                    unterBegriff ub = (unterBegriff)ob.UB.elementAt( j );
                    f.print( ub.Begriff + "=" + ub.Eintrag + (char)13 + (char)10 );
                }
                f.print( "" + (char)13 + (char)10 );
            }
            f.close();
        }
        catch( IOException ioe ) {
            
        }
    }
    public void setValue( String OB, String Begriff, boolean Eintrag ) {
        String tf = "FALSE";
        if( Eintrag ) {
            tf = "TRUE";
        }
        setValue( OB, Begriff, tf );
    }
    public void setValue( String OB, String Begriff, int Eintrag ) {
        setValue( OB, Begriff, "" + Eintrag );
    }
    public void setValue( String OB, String Begriff, long Eintrag ) {
        setValue( OB, Begriff, "" + Eintrag );
    }
    public void setValue( String OB, String Begriff, String Eintrag ) {
        for( int i = 0;i < INIFILE.size();i++ ) {
            oberBegriff ob = (oberBegriff)INIFILE.elementAt( i );
            if( ob.Begriff.equals( OB ) ) {
                for( int j = 0;j < ob.UB.size();j++ ) {
                    unterBegriff ub = (unterBegriff)ob.UB.elementAt( j );
                    if( ub.Begriff.equals( Begriff ) ) {
                        ub.Eintrag = Eintrag;
                        return ;
                    }
                }
                unterBegriff ub = new unterBegriff( Begriff, Eintrag );
                ob.UB.addElement( ub );
                return ;
            }
        }
        oberBegriff ob = new oberBegriff( OB );
        unterBegriff ub = new unterBegriff( Begriff, Eintrag );
        ob.UB.addElement( ub );
        INIFILE.addElement( ob );
    }
    public boolean getBoolValue( String OB, String Begriff ) {
        String E = getValue( OB, Begriff );
        if( E.equals( "TRUE" ) ) {
            return true;
        }
        else {
            return false;
        }
    }
    public int getIntValue( String OB, String Begriff ) {
        String E = getValue( OB, Begriff );
        if( E.equals( "" ) ) {
            E = "-1";
        }
        //return ( new Integer( E ) ).intValue();
        return Integer.valueOf(E);
    }
    public long getLongValue( String OB, String Begriff ) {
        String E = getValue( OB, Begriff );
        if( E.equals( "" ) ) {
            E = "-1";
        }
        //return ( new Long( E ) ).longValue();
        return Long.valueOf(E);
    }
    public String getValue( String OB, String Begriff ) {
        for( int i = 0;i < INIFILE.size();i++ ) {
            oberBegriff ob = (oberBegriff)INIFILE.elementAt( i );
            if( ob.Begriff.equals( OB ) ) {
                for( int j = 0;j < ob.UB.size();j++ ) {
                    unterBegriff ub = (unterBegriff)ob.UB.elementAt( j );
                    if( ub.Begriff.equals( Begriff ) ) {
                        return ub.Eintrag;
                    }
                }
                return "";
            }
        }
        return "";
    }
       //02.06.12 changed to by Walter Hipp
    public iniFiles(String inifile, Class mC)
    {    
        this.inifile = System.getProperty("user.home")
                       + System.getProperty("file.separator") +".Sokoban3D"
                       + System.getProperty("file.separator") + inifile;
        mainClass = mC;
    }
}
