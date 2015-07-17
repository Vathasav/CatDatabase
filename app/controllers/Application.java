package controllers;

import play.*;
import play.data.*;
import play.mvc.*;
import play.mvc.Http.MultipartFormData.*;
import play.mvc.Http.MultipartFormData.FilePart;

import views.html.*;
import models.Cat;

import java.io.*;

public class Application extends Controller {

    static play.data.Form<Cat> catForm = Form.form(Cat.class);

    public Result index() {
        return redirect(routes.Application.cats());
    }

    //show front page
    public Result cats(){
        return ok(views.html.index.render(Cat.all(), catForm));
    }


    // sends the empty catform
    public Result create(){

        Form<Cat> newCatForm = catForm;
        return ok(
                addcat.render(catForm)
        );
    }

   //returns the image data for a cat based on its ID
    public Result getImage(long id) {

        Cat cat = Cat.find.byId(id);

        if (cat != null) {

            flash("success","Picture found");
            /*** here happens the magic ***/
            return ok(cat.picture).as("image");
            /************************** ***/

        } else {
            flash("error", "Picture not found.");
            return null;//redirect(routes.Application.index());
        }
    }
    
    //extracts image data from multipart form and updates the cat object with it
    private void uploadCatImageData(Cat newCat){
        
            //get multi-part data
            play.mvc.Http.MultipartFormData body = request().body().asMultipartFormData();

            //get image from picture tag
            play.mvc.Http.MultipartFormData.FilePart imageFile = body.getFile("picture");

            // check for null and update newCat

            if(imageFile != null){

                File file = imageFile.getFile();

                if(file != null){
                    newCat.imageFile = file;
                }


            }
        
    }

    //handles the creation of new cat
    public Result newCat(){
        Form<Cat> filledForm = catForm.bindFromRequest();
        
        //if form has errors return

        if(filledForm.hasErrors()){
            return badRequest(addcat.render(filledForm));
        }
        else if(filledForm.get().color.trim().isEmpty()){
            return badRequest(addcat.render(filledForm));
        }
        else
        {
            //if form is OK process it 
            // load image data and save the cat

            Cat newCat = filledForm.get();
            
            uploadCatImageData(newCat);
            
            Cat.create(newCat);
            
            //return to front page
            return redirect(routes.Application.cats());

        }

    }

    // deletes a cat based on its ID
    public Result deleteCat(Long id){
        Cat.delete(id);
        return redirect(routes.Application.cats());
    }

    // sends the edit form for cat based on its data
    public Result editCat(Long id){

        Form<Cat> filledForm = catForm.fill(Cat.find.byId(id));


        return ok(
                editcat.render(Cat.find.byId(id), filledForm)
        );

       
    }

    //updates the cat information based on modified data
    public  Result updateCat(Long id) {
        Form<Cat> filledForm = catForm.bindFromRequest();
       
        // if form is error-prone then send bad request
        if(filledForm.hasErrors()) {
            return badRequest(editcat.render(Cat.find.byId(id), filledForm));
        }
        else{
            
            //process the form and update cat data
            
            Cat catFromForm = filledForm.get();
            uploadCatImageData(catFromForm);
            
            // save cat information to DB
            Cat.save(id,catFromForm);
            
            return redirect(routes.Application.cats());
        }
        
    }


}
