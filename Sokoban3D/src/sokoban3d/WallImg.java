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

import java.awt.*;

/**
 * 
 * this class handles the images for the given directions
 *
 */
class WallImg 
{
    private Image north;
    private Image east;
    private Image west;
    public WallImg() {
        
    }
    
    /**
     * 
     * @param id int value the players direction
     * @return the image for the direction temp points to 
     */
    public Image getWallImg( int id ) {
        Image temp = null;
        if( id == 0 ) {
            temp = north;
        }
        if( id == 1 ) {
            temp = east;
        }
        if( id == 3 ) {
            temp = west;
        }
        return temp;
    }
    /**
     * set images for dirctions
     * @param id int value the players direction
     * @param temp the image for the direction id points to
     */
    public void setWallImg( int id, Image temp ) {
        if( id == 0 ) {
            north = temp;
        }
        if( id == 1 ) {
            east = temp;
        }
        if( id == 3 ) {
            west = temp;
        }
    }
}
