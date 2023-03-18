package sokoban3d;

/**
 * 
 * @author Walter Hipp https://hipp-systems.com/
 */

/**
 * 
 * WAppReadResources<br>
 * reads resources used by the different methods
 * 
 */

public class WAppReadResources
{
   /**
    * 
    * @param location for the resources
    * @param resName name of the resource
    * @param cl Class
    * @param locale java.util.Locale
    * @return resources
    */
   public static java.util.ResourceBundle getResources(String location,
           String resName, Class cl, java.util.Locale locale) //, String woher)
   {
       //System.out.println("woher: " + woher);
       java.util.ResourceBundle resources = null;
       //System.out.println("location " + location + " resName " + resName +
       //        " Class " + cl + " locale " + locale);
       java.net.URL url[] = new java.net.URL[1];
       url[0] = cl.getResource(location);
       //System.out.println("Bundle url: " + url[0]);
       if (url[0] != null)
           PlayerDB.urlToRes = url[0];
       java.net.URLClassLoader uCL =
            new java.net.URLClassLoader(url, null);
       //java.util.ResourceBundle.getBundle("hipp/com/toolbartest/resources/EdidpadPro",locale);
       try {
               resources = java.util.ResourceBundle.getBundle(resName,
               //new java.util.Locale("DE"), cl.getClassLoader());
               //new java.util.Locale("DE"), uCL);
                       locale, uCL);
        } catch (java.util.MissingResourceException _me) { _me.printStackTrace(); }
      
      return resources;
   }
   
   /**
    * printout resources keys
    * @param resources resources bundle
    */
   public static void getKeys(java.util.ResourceBundle resources)
   {
       for (java.util.Enumeration<String> e = resources.getKeys(); e.hasMoreElements();)
          if (Gui.debug) System.out.println(e.nextElement());
   }
   
   /**
    * 
    * @param resources
    * @param key resources key
    * @return String resources value the key points to
    */
   public static String getValue(java.util.ResourceBundle resources, String key)
   {
       //System.out.println("getValueR: " + resources + " Key: " + key);
       try {
           return resources.getString(key);
       } catch (java.util.MissingResourceException _me) { return null; }
   }
   
   /**
    * returns resources data as vector
    * @param cl the main class
    * @param key resources key
    * @return result as vector
    */
   public static java.util.Vector<String> getData(Class cl, String key)
   {
       if (Gui.debug) System.out.println("key " + key);
       String line = null;
       java.util.Vector<String> vec = new java.util.Vector<String>();
           try {
               //java.io.FileInputStream fin = new java.io.FileInputStream("ne");
               //System.out.println("fin " + fin);
               java.io.InputStream in = cl.getResourceAsStream(key); //"/demo/resources/data/db.dat");
               java.io.BufferedReader reader = 
                       new java.io.BufferedReader(new java.io.InputStreamReader(in));
               {
                   System.out.println("reader " + reader);
                   while ((line = reader.readLine()) != null)
                   {
                      if (Gui.debug) System.out.println("line " + line);
                      //vec.add(PlayerDB.decodeCH(line));
                   }
               }
               in.close();
               reader.close();
           }catch(Exception ex) { ex.printStackTrace(); }
       //}
       return vec;
   }
}
