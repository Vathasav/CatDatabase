package models;



import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import play.data.validation.Constraints.*;
import play.data.validation.Constraints.*;
import play.data.*;

import play.db.ebean.*;
import javax.persistence.*;
import java.io.*;
import com.avaje.ebean.Ebean;

/**
 * Created by Vathasav on 15/07/15.
 */
@Entity
public class Cat extends Model {

    @Id
    public Long id;

    // pattern to accept only text and to not consider numbers and white spaces
    @Required 
    @Pattern("[\\S]+[a-zA-Z]")
    public String name;

    @Required
    public String color;

    @Required
    public String race;

    public enum Gender {Male, Female};

    @Required
    public Gender gender;
    
    //for storing image data
    @Lob
    public byte[ ] picture = null;
    
    public File imageFile = null;
    
    // collected from web
    public static List<String> colors = new ArrayList<String>(Arrays.asList("aliceblue", "antiquewhite", "aqua", "aquamarine", "azure", "beige", "bisque", "black", "blanchedalmond", "blue", "blueviolet", "brown", "burlywood", "cadetblue", "chartreuse", "chocolate", "coral", "cornflowerblue", "cornsilk", "crimson", "cyan", "darkblue", "darkcyan", "darkgoldenrod", "darkgray", "darkgreen", "darkkhaki", "darkmagenta", "darkolivegreen", "darkorange", "darkorchid", "darkred", "darksalmon", "darkseagreen", "darkslateblue", "darkslategray", "darkturquoise", "darkviolet", "deeppink", "deepskyblue", "dimgray", "dodgerblue", "firebrick", "floralwhite", "forestgreen", "fuchsia", "gainsboro", "ghostwhite", "gold", "goldenrod", "gray", "green", "greenyellow", "honeydew", "hotpink", "indianred", "indigo", "ivory", "khaki", "lavender", "lavenderblush", "lawngreen", "lemonchiffon", "lightblue", "lightcoral", "lightcyan", "lightgoldenrodyellow", "lightgreen", "lightgrey", "lightpink", "lightsalmon", "lightseagreen", "lightskyblue", "lightslategray", "lightsteelblue", "lightyellow", "lime", "limegreen", "linen", "magenta", "maroon", "mediumaquamarine", "mediumblue", "mediumorchid", "mediumpurple", "mediumseagreen", "mediumslateblue", "mediumspringgreen", "mediumturquoise", "mediumvioletred", "midnightblue", "mintcream", "mistyrose", "moccasin", "navajowhite", "navy", "oldlace", "olive", "olivedrab", "orange", "orangered", "orchid", "palegoldenrod", "palegreen", "paleturquoise", "palevioletred", "papayawhip", "peachpuff", "peru", "pink", "plum", "powderblue", "purple", "red", "rosybrown", "royalblue", "saddlebrown", "salmon", "sandybrown", "seagreen", "seashell", "sienna", "silver", "skyblue", "slateblue", "slategray", "snow", "springgreen", "steelblue", "tan", "teal", "thistle", "tomato", "turquoise", "violet", "wheat", "white", "whitesmoke", "yellow", "yellowgreen"));
 
    public static Finder<Long,Cat> find = new Finder(Long.class, Cat.class);

    // return all cats data
    public static List<Cat> all(){
        return find.all();
    }
    
    
    // fill picture data from the image file
    public static void fillImageData(Cat cat){
        
     if(cat.imageFile != null){
            
        File image = cat.imageFile;
            
        cat.picture = new byte[(int)image.length()];
        
        
        // read image data into picture 
        // check if streams are closed or not
        InputStream inStream = null;
        try {
            inStream = new BufferedInputStream(new FileInputStream(image));
            inStream.read(cat.picture);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
            
     }
        
    }

    // create cat into DB
    public static void create (Cat cat){
        
        // file cat's image data
        fillImageData(cat);
    
        cat.save();

    }

    public static void delete(Long id){
        find.ref(id).delete();

    }

    
    //save edited cat information
    public static void save(Long id, Cat editedCat){
        
            Cat catFromDB = Ebean.find(Cat.class, id);

            catFromDB.name = editedCat.name;
            catFromDB.color = editedCat.color;
            catFromDB.race = editedCat.race;
            catFromDB.gender = editedCat.gender;
            catFromDB.imageFile = editedCat.imageFile;

            Cat.fillImageData(catFromDB);

            Ebean.update(catFromDB);

    }
    
    
    /*
    *Popular list of 10 cat breeds obtained from Web
    */
    public static List<String> catBreedsList(){
        
        List<String> breeds = new ArrayList<String>();
        breeds.add("Abyssinian");
        breeds.add("American Shorthair");
        breeds.add("Persian");
        breeds.add("Maine Coon");
        breeds.add("Exotic");
        breeds.add("Siamese");
        breeds.add("Birman");
        breeds.add("Oriental Shorthair");
        breeds.add("Sphynx");
        breeds.add("Ragdoll");
        
       
        return breeds;        
    }
    
}
