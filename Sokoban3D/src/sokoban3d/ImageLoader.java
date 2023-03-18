///////////////////////////////////////////////////////////////////////////////////
//Sokoban3D - Gui.java                                                            /
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
// this Class was added by Walter Hipp    walter.hipp@hipp-online.de                                        /
///////////////////////////////////////////////////////////////////////////////////

package sokoban3d;


/**
 * returns an Image or ImageIcon from an URL relative to baseClass
 * baseClass we will need, if we are making packages and put them
 * together in a jar archive
 */
public class ImageLoader
{
  /**
   * empty constructor
   */
  public ImageLoader()
  {
  }

  /**
   * 
   * @param path change path from .res to .gif
   * @return path for a gif file
   */
  public static String chkPath(String path)
  {
     if (path.endsWith(".res"))
     return path.substring(0, path.lastIndexOf(".") +1) + "gif";
     else return path;
  }

  /**
   * path is something like "images/info.gif"
   * returns a Image
   * @param path for creating the url
   * @param baseClass Gui.class
   * @return returns an image or null
  **/
  public static java.awt.Image loadaImage(String path, Class baseClass)
  {
    String _path = chkPath(path);
    //System.out.println("base " + baseClass.getName() + " pth " + _path);
    java.net.URL url = baseClass.getResource(_path);
    if (url != null)
      return (java.awt.Toolkit.getDefaultToolkit().getImage(url));
    else return null;
  }

  /**
   * path is something like "images/info.gif"
   * returns a Image
   * @param path for creating the url
   * @param baseClass Gui.class
   * @return returns an icon or null
  **/
  public static javax.swing.ImageIcon loadaIcon(String path, Class baseClass)
  {
   String _path = chkPath(path);
    java.net.URL url = baseClass.getResource(_path);
    //System.out.println("path " +path); // url.getPath());
    if (url != null)
      return new javax.swing.ImageIcon(url);
    else return null;
  }
}