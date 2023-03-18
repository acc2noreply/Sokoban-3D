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
 * 
 * player class holds the positions
 */
class Player 
{
    private int xPos; //(X) Position innerhalb der Matrix
    private int yPos; //(Y) Position innerhalb der Matrix
    private int direction; //Rcihtung des Spielers
    private String name;
    
    /**
     * 
     * @param direction_ int value (south, north, west, east)
     * @param xPos_ int value xpos
     * @param yPos_ int value ypos
     */
    public Player( int direction_, int xPos_, int yPos_ ) 
    {
        direction = direction_;
        xPos = xPos_;
        yPos = yPos_;
    }
    
    // fragt die X-Position des Spielers ab
    /**
     * 
     * @return xpos as int value
     */
    public int getXPos() 
    {
        return xPos;
    }
    
    // fragt die Y-Position des Spielers ab
    /**
     * 
     * @return ypos as int value
     */
    public int getYPos() 
    {
        return yPos;
    }
    
    // addiert einen Wert zu der X-Position des Spielers
    /**
     * 
     * @param temp add int value temp to xpos
     */
    public void setXPos( int temp ) 
    {
        xPos = xPos + temp;
    }
    
    // addiert einen Wert zu der Y-Position des Spielers
    /**
     * 
     * @param temp add int value temp to ypos
     */
    public void setYPos( int temp )
    {
        yPos = yPos + temp;
    }
    
    //setzt die X-Position des Spielers absolut
    /**
     * 
     * @param temp set the players absolut xpos
     */
    public void setXPosAbs( int temp ) 
    {
        xPos = temp;
    }
    
    //setzt die Y-Position des Spielers absolut
    /**
     * 
     * @param temp set the players absolut ypos
     */
    public void setYPosAbs( int temp ) 
    {
        yPos = temp;
    }
    
    //in welche Richtung schaut der Spieler?
    /**
     * 
     * @return gets the direction the player is looking
     */
    public int getDirection()
    {
        return direction;
    }
    
    // setzt die Blickrichtung absolut
    /**
     * 
     * @param temp int temp
     * sets the direction the player is lookin absolute
     */
    public void setDirectionAbs( int temp ) 
    {
        direction = temp;
    }
    
    // addiert einen Wert zu der Blickrichtung des Spielers
    /**
     * 
     * @param temp int value
     * set theplayers direction
     */
    public void setDirection( int temp )
    {
        direction = direction + temp;
        if( direction == 4 )
        {
            direction = 0;
        }
        if( direction == -1 ) 
        {
            direction = 3;
        }
    }
}
