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

/**
 * @author walter2
 *
 */

/**
 * Cube handles the game grafics with boolean data
 * can enter zone, is there a door, is way blocked,
 * is target zone reached and is there a target zone
 * id's for walls n,e,s,w
 * door texture id's d(oor) n, d(oor) e, d(oor) s, d(oor) w
 *
 */
class Cube 
{
    private boolean zone; // betretbar?
    private boolean door; // ist hier ein TüRahmen?
    private boolean blocked; //ist blockiert?
    private boolean reached; //ZielZone erreicht?
    private boolean goal; // ist eine ZielZone?
    private int n, e, s, w; //texture ID's für Wände
    private int dn, de, ds, dw; //texture ID's für Türen
    
    // Konstruktor
    
    /**
     * @param zone_
     * the param zone indicates if a zone can be entered<br>or not
     */
    public Cube( boolean zone_ ) {
        n = 0;
        e = 0;
        s = 0;
        w = 0;
        zone = zone_;
        blocked = false;
        door = false;
    }
    
    // ist betretbar?
    
    /**
     * @return
     * boolean value if a zone can be entered or not
     */
    public boolean isZone() {
        return zone;
    }
    
    // ist blockiert?
    /**
     * @return
     * boolean value inticates if way is blocked or not
     */
     public boolean isBlocked() {
        return blocked;
    }
     
     /**
      * @return
      * boolean value inticates if is a door or not
      */
    // ist hier ein TürRahmen?
     public boolean isDoor() {
        return door;
    }
    
     /**
      * @return
      * boolean value inticates if player has reached target zone or not
      */
    // ist die ZielZone erreicht
     public boolean isReached() {
        return reached;
    }
    
     /**
      * @return
      * boolean value inticates if zone is a target zone or not
      */
    // ist die Zone eine ZielZone?
    public boolean isGoal() {
        return goal;
    }
    
    /**
     * @return
     * boolean value inticates if zone can be entered or not
     */
    //ist betretbar?
    public boolean isSpace() {
        boolean result = false;
        if( ( zone ) && ( !blocked ) ) {
            result = true;
        }
        return result;
    }
    
    /**
     * @param temp
     * boolean value set door flag
     */
    // setzte Flag für TürRahmen
    public void setDoor( boolean temp ) {
        door = temp;
    }
    
    // setzte Flag für "blockiert"#
    /**
     * @param temp
     * boolean value set blocked flag
     */
    public void setBlocked( boolean temp ) {
        blocked = temp;
    }
    
    // setzte Flag für "ziel erreicht"
    /**
     * @param temp
     * boolean value set reached flag
     */
    public void setReached( boolean temp ) {
        if( goal ) {
            reached = temp;
        }
    }
    
    // setzte Flag f�r "ZielZone"
    /**
     * @param temp
     * boolean value set goal flag
     */
    public void setGoal( boolean temp ) {
        goal = temp;
    }
    
    // setzte Flag f�r "Zone"
    /**
     * @param temp
     * boolean value set zone flag
     */
    public void setZone( boolean temp ) {
        zone = temp;
    }
    
    // setze Textur ID's für Wände(bei Richtungsänderung des Spielers notwendig)
    /**
     * @param temp
     * integer value n(orth), e(ast), s(outh), w(est)
     * @param id
     * integer texture id
     * set texture ID's for walls(required when changing direction of player)
     */
    public void setTexture( int temp, int id ) {
        if( id == 0 ) {
            n = temp;
        }
        if( id == 1 ) {
            e = temp;
        }
        if( id == 2 ) {
            s = temp;
        }
        if( id == 3 ) {
            w = temp;
        }
    }
    
    // setze Textur ID's für TürRahmen(bei Richtungsänderung des Spielers notwendig)
    /**
     * set texture ID's for door frames (required when changing direction of player)
     * @param temp
     * integer value d(oor) dn(orth), de(ast), ds(outh), dw(est)
     * @param id
     * integer texture id
     */
    public void setDoorTexture( int temp, int id ) {
        if( id == 0 ) {
            dn = temp;
        }
        if( id == 1 ) {
            de = temp;
        }
        if( id == 2 ) {
            ds = temp;
        }
        if( id == 3 ) {
            dw = temp;
        }
    }
    
    // frage die Textur ID für Wände ab (je nach Position des Spielers)
    /**
     * get texture id for walls (depending on player position)
     * @param id
     * texture id
     * @return direction
     */
    public int getTexture( int id ) {
        int temp = -1;
        if( id == 0 ) {
            temp = n;
        }
        if( id == 1 ) {
            temp = e;
        }
        if( id == 2 ) {
            temp = s;
        }
        if( id == 3 ) {
            temp = w;
        }
        return temp;
    }
    
    // frage die Textur ID für TürRahmen ab (je nach Position des Spielers)
    /**
     * get texture id for door frame (depending on player position)
     * @param id
     * texture id
     * @return door texture
     */
    public int getDoorTexture( int id ) {
        int temp = -1;
        if( id == 0 ) {
            temp = 1;
        }
        if( id == 1 ) {
            temp = 0;
        }
        if( id == 2 ) {
            temp = 1;
        }
        if( id == 3 ) {
            temp = 0;
        }
        return temp;
    }
}
