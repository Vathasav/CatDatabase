package controllers;

import play.*;
import play.data.*;
import play.mvc.*;
import play.mvc.Http.MultipartFormData.*;
import play.mvc.Http.MultipartFormData.FilePart;

import views.html.*;
import models.Cat;
import com.avaje.ebean.Ebean;
import java.io.*;

public class Application extends Controller {

    static play.data.Form<Cat> catForm = Form.form(Cat.class);

    public Result index() {
        return redirect(routes.Application.cats());
    }

    public Result cats(){
        return ok(views.html.index.render(Cat.all(), catForm));
    }


    public Result create(){
        
        Form<Cat> newCatForm = catForm;
        return ok(
            addcat.render(catForm)
        );
    }

    public Result getImage(long id) {
        
        Cat cat = Cat.find.byId(id);
        
        if (cat != null) {
            
            /*** here happens the magic ***/
            return ok(cat.picture).as("image");
            /************************** ***/
            
        } else {
            flash("error", "Picture not found.");
            return redirect(routes.Application.index());
        }
    }

    public Result newCat(){
        Form<Cat> filledForm = catForm.bindFromRequest();

        Cat newCat = filledForm.get();
        if(filledForm.hasErrors()){
            return badRequest(views.html.index.render(Cat.all(),filledForm));
        }
        else
        {
            
            play.mvc.Http.MultipartFormData body = request().body().asMultipartFormData();

            play.mvc.Http.MultipartFormData.FilePart imageFile = body.getFile("picture");
            
                   
              if(imageFile != null){

                File file = imageFile.getFile();
            
            
                
                
                if(file != null){
                newCat.imageFile = file;
                    
                newCat.fileName = file.getName();    
                }
                
                
            }
            
            Cat.create(newCat);
            return redirect(routes.Application.cats());
        
        }

    }

    public Result deleteCat(Long id){
        Cat.delete(id);
        return redirect(routes.Application.cats());
    }

    public Result editCat(Long id){
        
        Form<Cat> filledForm = catForm.fill(Cat.find.byId(id));
       
        
        return ok(
            editcat.render(id, filledForm)
        );
        
       // return TODO;
    }
    
     public  Result updateCat(Long id) {
        Form<Cat> filledForm = catForm.bindFromRequest();
        if(filledForm.hasErrors()) {
            return badRequest(editcat.render(id, filledForm));
        }
        
        Cat catFromDB = Ebean.find(Cat.class, id);
        
        Cat catFromForm = filledForm.get();
        
            catFromDB.name = catFromForm.name;
            catFromDB.color = catFromForm.color;
            catFromDB.race = catFromForm.race;
            catFromDB.gender = catFromForm.gender;
            
            play.mvc.Http.MultipartFormData body = request().body().asMultipartFormData();

            play.mvc.Http.MultipartFormData.FilePart imageFile = body.getFile("picture");
            
                   
              if(imageFile != null){

                File file = imageFile.getFile();
            
            
                
                
                if(file != null){
                catFromDB.imageFile = file;
                    
                catFromDB.fileName = file.getName();    
                }
                
                Cat.fillImageData(catFromDB)   ; 
                
            }
        
          
        Ebean.update(catFromDB);

        Cat updatedBudget = Ebean.find(Cat.class, id);
     //   assertThat(updatedBudget.id).isEqualTo(id);
       
    
        
          /*  Cat catFromDB = Cat.find.byId(id);
            catFromDB.name = filledForm.get("name");
            catFromDB.color = filledForm.get("color");
            catFromDB.race = filledForm.get("race");
            catFromDB.gender = filledForm.get("gender");
            catFromDB.picture = filledForm.get("picture");*/
            
          

      //  Cat catFromDB = filledForm.get();
    //    Cat.find.ref(id).update(catFromDB.class);
       // catFromDB..update(catFromDB.id.toString());
        
//        filledForm.get().update(id);
        flash("success", "Cat " + filledForm.get().name + " has been updated");
        return redirect(routes.Application.cats());
    }
    
    public Result upload() {
    play.mvc.Http.MultipartFormData body = request().body().asMultipartFormData();
    play.mvc.Http.MultipartFormData.FilePart picture = body.getFile("picture");
    if (picture != null) {
        String fileName = picture.getFilename();
        String contentType = picture.getContentType();
        java.io.File file = picture.getFile();
        return ok("File uploaded");
    } else {
        flash("error", "Missing file");
        return badRequest();
    }
}
}
