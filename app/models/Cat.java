package models;



import java.util.ArrayList;
import java.util.List;

import play.data.validation.Constraints.*;
import play.data.*;

import play.db.ebean.*;
import javax.persistence.*;
import java.io.*;

/**
 * Created by Vathasav on 15/07/15.
 */
@Entity
public class Cat extends Model {

    @Id
    public Long id;

    @Required
    public String name;

    @Required
    public String color;

    @Required
    public String race;


    public enum Gender {Male, Female};

    @Required
    public Gender gender;
    
    @Lob
    public byte[ ] picture = null;
    
    public File imageFile = null;
    
    public String fileName = null;

    public static Finder<Long,Cat> find = new Finder(Long.class, Cat.class);

    public static List<Cat> all(){
        return find.all();//new ArrayList<Cat>();
    }
    
    public static void fillImageData(Cat cat){
        
            if(cat.imageFile != null){
            
            File image = cat.imageFile;
            
            cat.picture = new byte[(int)image.length()];
        
        
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

    public static void create (Cat cat){
        
        fillImageData(cat);
    
        
        cat.save();

    }

    public static void delete(Long id){
        find.ref(id).delete();

    }

    public static void edit(Long id){

        find.ref(id).update();

    }
    
    public static void save(Long id){

        find.ref(id).update();

    }
    
  /*  public Cat(Cat newCat, File image){
        
        picture = new byte[(int)image.length()];
        
        
        InputStream inStream = null;
        try {
            inStream = new BufferedInputStream(new FileInputStream(image));
            inStream.read(picture);
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
        
        
    if(picture != null)
       newCat.picture = picture;
    
     create(newCat);
        
    }
    
   public Image(File image) {
        
        this.picture = new byte[(int)image.length()];
        
        
        InputStream inStream = null;
        try {
            inStream = new BufferedInputStream(new FileInputStream(image));
            inStream.read(this.picture);
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
        
        
    }*/
}
