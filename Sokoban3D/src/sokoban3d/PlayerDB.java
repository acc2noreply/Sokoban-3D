/*
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

/**
 * @author walter2
 *
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * this class manages games database, stored in
 * demo.resources.data//db.dat
 *
 */
class PlayerDB {
  boolean encrypt = true;
  
  public Vector<Object> scores = new Vector();
  public Vector<Object> players = new Vector();
  public Vector<Object> coldata = new Vector();
  public Vector<Vector<Object>> rowData = new Vector<>();
 
  public boolean highreached = false;
  protected Class mainClass;
  private Locale locale;
  public static ResourceBundle bundle = null;
  public static URL urlToRes = null;
  
  public PlayerDB(Class mC) 
  {
    this.mainClass = mC;
    this.coldata.addElement("Level");
    this.coldata.addElement("Player");
    this.coldata.addElement("Moves");
    this.locale = new Locale("en", "EN");
    this.bundle = WAppReadResources.getResources("/demo/resources/", "Demo", 
        getClass(), this.locale);
    /*loadDB(getClass(), 
        WAppReadResources.getValue(this.bundle, "dataBaseIn"));
    int i;
    for (i = 0; i < this.players.size(); i++)
      System.out.println("player[" + i + "] " + this.players.get(i)); 
    for (i = 0; i < this.scores.size(); i++)
      System.out.println("scores[" + i + "] " + this.scores.get(i));*/
    //File f = new File(urlToRes.getPath());
    //System.out.println("DB f: " + f);
    //saveDB(getClass(), 
    //    WAppReadResources.getValue(this.bundle, "dataBaseOut"));
  }
  
  public void addPlayer(String temp) {
    int[] max_Levels = new int[90];
    this.scores.addElement(max_Levels);
    this.players.addElement(temp);
  }
  
  public void removePlayer(String temp, File f) {
    int ind = getIndex(temp);
    System.out.println("ind: " + ind);
    this.players.remove(ind);
    this.scores.remove(ind);
    updateScoreTable();
    saveDB(getClass(), 
        WAppReadResources.getValue(this.bundle, "dataBase"));
  }
  
  public int getIndex(String temp) {
    int result = -1;
    for (int i = 0; i < this.players.size(); i++) {
      if (this.players.elementAt(i).equals(temp)) {
        result = i;
        break;
      } 
    } 
    return result;
  }
  
  public void addScore(String player, int levelIndex, int moves) {
    ((int[])this.scores.elementAt(getIndex(player)))[levelIndex] = moves;
  }
  
  public int getScore(String player, int levelIndex) {
    return ((int[])this.scores.elementAt(getIndex(player)))[levelIndex];
  }
  
  public int getLastSolvedLevel(String player) {
    int result = -1;
    for (int i = 0; i < 90; i++) {
      result = i;
      if (((int[])this.scores.elementAt(getIndex(player)))[i + 1] == 0)
        break; 
    } 
    if (((int[])this.scores.elementAt(getIndex(player)))[0] == 0)
      result--; 
    return result + 1;
  }
  
  // verschlüsselt die zu abspeichernden Daten
    public String encodeCH( String temp ) {
        String newstr = "";
        String charOld[] =  {
            " ", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", 
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", 
            "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", 
            "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", 
            "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", 
            "S", "T", "U", "V", "W", "X", "Y", "Z"
        };
        String charNew[] =  {
            "#", "z", "y", "x", "w", "v", "u", "t", "s", "r", "q", 
            "p", "o", "n", "m", "l", "k", "j", "i", "h", "g", "f", 
            "e", "d", "c", "b", "a", "Z", "Y", "X", "W", "V", "U", 
            "T", "S", "R", "Q", "P", "O", "N", "M", "L", "K", "J", 
            "I", "H", "G", "F", "E", "D", "C", "B", "A", "9", "8", 
            "7", "6", "5", "4", "3", "2", "1", "0"
        };
        for( int i = 0;i < temp.length();i++ ) {
            for( int j = 0;j < charOld.length;j++ ) {
                if( ( temp.substring( i, i + 1 ) ).equals( charOld[ j ] ) ) {
                    newstr = newstr + charNew[ j ];
                }
            }
        }
        return newstr;
    }
  
  // entschlüsselt die zu ladenden Daten
    public String decodeCH( String temp ) {
        String newstr = "";
        String charNew[] =  {
            " ", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", 
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", 
            "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", 
            "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", 
            "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", 
            "S", "T", "U", "V", "W", "X", "Y", "Z"
        };
        String charOld[] =  {
            "#", "z", "y", "x", "w", "v", "u", "t", "s", "r", "q", 
            "p", "o", "n", "m", "l", "k", "j", "i", "h", "g", "f", 
            "e", "d", "c", "b", "a", "Z", "Y", "X", "W", "V", "U", 
            "T", "S", "R", "Q", "P", "O", "N", "M", "L", "K", "J", 
            "I", "H", "G", "F", "E", "D", "C", "B", "A", "9", "8", 
            "7", "6", "5", "4", "3", "2", "1", "0"
        };
        for( int i = 0;i < temp.length();i++ ) {
            for( int j = 0;j < charOld.length;j++ ) {
                if( ( temp.substring( i, i + 1 ) ).equals( charOld[ j ] ) ) {
                    newstr = newstr + charNew[ j ];
                }
            }
        }
        return newstr;
    }
  
  public void updateScoreTable() {
    this.highreached = false;
    this.rowData.removeAllElements();
    int lessMoves = 100000;
    int Moves = 0;
    String p_level = "";
    String p_name = "";
    String p_moves = "";
    boolean add = false;
    for (int j = 0; j < 90; j++) {
      lessMoves = 100000;
      add = false;
      for (int i = 0; i < this.players.size(); i++) {
        Moves = getScore((String)this.players.elementAt(i), j);
        if (Moves != 0 && 
          Moves < lessMoves) {
          p_name = (String)this.players.elementAt(i);
          p_moves = Integer.toString(Moves);
          p_level = Integer.toString(j + 1);
          lessMoves = Moves;
          add = true;
          this.highreached = true;
        } 
      } 
      if (add) {
        Vector<Object> temp = new Vector();
        temp.addElement(p_level);
        temp.addElement(p_name);
        temp.addElement(p_moves);
        this.rowData.add(temp);
        for (int m = 0; m < this.players.size(); m++) {
          if (!p_name.equals(this.players.elementAt(m)) && 
            getScore((String)this.players.elementAt(m), j) == Integer.parseInt(p_moves)) {
            Vector<Object> temp1 = new Vector();
            temp1.addElement(Integer.toString(j + 1));
            temp1.addElement(this.players.elementAt(m));
            temp1.addElement(Integer.toString(Integer.parseInt(p_moves)));
            this.rowData.add(temp1);
          } 
        } 
      } 
    } 
    int padding = 13 - this.rowData.size();
    for (int k = 0; k < padding; k++) {
      Vector<Object> temp1 = new Vector();
      temp1.addElement("");
      temp1.addElement("");
      temp1.addElement("");
      this.rowData.add(temp1);
    } 
  }
  
  public static java.net.URL getUrl(Class cl,
           java.util.ResourceBundle resources, String loc, String key)
   {
       //System.out.println("resourceLocation: " + "/demo/resources/" + " key:" + key);
       //String resourceLocation = loc + //"/com/hipp/wappedidpadpro/resources/" +
        //       WAppReadResources.getValue(resources, key);
       java.net.URL url = cl.getResource(key); //resourceLocation);
       //System.out.println("resourceLocation: " + resourceLocation + " url:" + imageURL);
       if (url != null)
           return url; //javax.swing.ImageIcon(imageURL, "bla");
       else return null;
   }
  
  /*public void saveDB2(Class cl, String key) 
  {
    String output = "";
    //URL url = cl.getClassLoader().getResource(key);
    //File f = new File(url.getPath());
    System.out.println("DB IN Key: " + key);
    //System.out.println("DB In File: " + f);
    java.net.URL url = 
        getUrl(getClass(), bundle, "/demo/resources/", key);
    System.out.println("saveUrl: " + url);
    File f = new File(url.getPath());
    System.out.println("out File: " + f);
  }*/
  
  public void saveDB(Class cl, String key)
  {
    BufferedWriter db;
    String output = "";
    java.net.URL url = 
        getUrl(getClass(), bundle, "/demo/resources/", key);
    File f = new File(url.getPath());
    try {
            db = new BufferedWriter( new FileWriter(url.getPath())); //path) );
            for( int i = 0;i < players.size();i++ )
            {
                //db.write( (String)players.elementAt( i ) );
                output = "";
                output = output + (String)players.elementAt( i );
                for( int j = 0;j < 90;j++ ) 
                {
                    // db.write( " " + getScore( (String)players.elementAt( i ), j ) );
                    output = output + ( " " + 
                    getScore( (String)players.elementAt( i ), j ) );
                }
                if (Gui.debug) System.out.println("out: " + output);
                if (encrypt) 
                {
                    db.write( encodeCH( output ) );
                }
                else 
                {
                    db.write( output );
                }
                db.newLine();
            }
            db.close();
        }catch( IOException e ) { e.printStackTrace(); }
    }
  
  public void loadDB(Class cl, String key)
  {
    //System.out.println("load key " + key);
    String line = null;
    int counter = 0;
    String currentPlayer = "";
    java.net.URL url = 
        getUrl(getClass(), bundle, "/demo/resources/", key);
    //System.out.println("uP " + url.getPath());
    try {
        InputStream in = cl.getResourceAsStream(key); //"/demo/resources/data/db.dat");
        //InputStream in = cl.getResourceAsStream("/demo/resources/data/db.dat");
        //System.out.println("in: " + in);
        //InputStream in = cl.getResourceAsStream(key);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        //System.out.println("reader " + reader);
        while ((line = reader.readLine()) != null)
        {
            //System.out.println("line " + line);
            if (this.encrypt) 
            {
                line = decodeCH(line);
                //System.out.println("line " + line);
            } 
            StringTokenizer st = new StringTokenizer(line);
            while (st.hasMoreTokens())
            {
                if (counter == 0) 
                {
                    currentPlayer = st.nextToken();
                    addPlayer(currentPlayer);
                } 
                if (counter != 0)
                    addScore(currentPlayer, counter - 1, 
                        Integer.parseInt(st.nextToken())); 
                counter++;
            } 
            counter = 0;
        } 
        in.close();
        reader.close();
      } catch (Exception ex) { ex.printStackTrace(); } 
    }
}