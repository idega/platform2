package com.idega.jmodule.image.presentation;

import com.idega.jmodule.object.interfaceobject.Window;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.image.data.*;
import com.idega.jmodule.image.business.ImageBusiness;
import com.idega.jmodule.image.business.ImageProperties;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company: idega software
 * @author Eirikur Hrafnsson, eiki@idega.is
 * @version 1.0
 */

public class EditWindow extends Window {

  public EditWindow(){
    super();
  }

  public EditWindow(String name){
    super(name);
  }

  public EditWindow(int width, int heigth) {
    super(width,heigth);
  }

  public EditWindow(String name,int width,int height){
    super(name, width, height);
  }

  public EditWindow(String name,String url){
    super(name,url);
  }

  public EditWindow(String name, int width, int height, String url){
    super(name, width, height, url);
  }

  public void main(ModuleInfo modinfo)throws Exception{
    String action = modinfo.getParameter("action");
    if("save_text".equalsIgnoreCase(action)) handleTextSave(modinfo);
    else if("upload".equalsIgnoreCase(action)){
      add(getUploadForm(modinfo));
    }
    else if("text".equalsIgnoreCase(action)){
      add(getEditForm(modinfo));
    }
    else{
      try{
        ImageProperties ip = ImageBusiness.doUpload(modinfo);

        int imageId = ImageBusiness.SaveImage(ip);
        add(new Image(imageId));

            ImageCatagory[] imgCat = (ImageCatagory[]) (new ImageCatagory()).findAll();
    DropdownMenu category = new DropdownMenu("category");
    for (int i = 0 ; i < imgCat.length ; i++ ) {
      category.addMenuElement(imgCat[i].getID(),imgCat[i].getImageCatagoryName());
    }
    add(category);
/*
      Table UploadDoneTable = new Table(2,3);
      UploadDoneTable.mergeCells(1,1,2,1);
      UploadDoneTable.mergeCells(1,2,2,2);
      UploadDoneTable.setBorder(0);
      newImageForm.add(UploadDoneTable);

      UploadDoneTable.add(category,2,3);

      UploadDoneTable.add(new Text("Hér er myndin eins og hún kemur út á vefnum. Veldu aftur ef eitthvað fór úrskeiðis"),1,1);
      UploadDoneTable.add(new Image(Integer.parseInt((String)modinfo.getSessionAttribute("image_id")) ),1,2);

      UploadDoneTable.add(new SubmitButton("submit","Ný mynd"),1,3);
      UploadDoneTable.add(new SubmitButton("submit","Vista"),1,3);*/


      }
      catch(Exception e){
        e.printStackTrace(System.err);
      }
    }

    modinfo.getSession().setAttribute("reload",new String("true"));

  }

  private void handleTextSave(ModuleInfo modinfo) throws Exception{
    String imageId = modinfo.getParameter("image_id");
    String imageText = modinfo.getParameter("image_text");
    ImageEntity image = new ImageEntity(Integer.parseInt(imageId));
    image.setImageText(imageText);
    image.update();

  }

  private void close(ModuleInfo modinfo){
    setParentToReload();
    close();
  }


  private Form getEditForm(ModuleInfo modinfo) throws Exception{
    Table table = new Table(1,2);
    String imageId = modinfo.getParameter("image_id");
    ImageEntity image = new ImageEntity(Integer.parseInt(imageId));
    String imageText = image.getText();
    if( imageText==null ) imageText = "";
    Form form = new Form();
    form.add(new HiddenInput("image_id",imageId));
    form.add(new HiddenInput("action","save_text"));
    TextArea input = new TextArea("image_text",imageText);
    input.setWidth(40);
    input.setWrap(true);
    table.add(input,1,1);
    table.add(new SubmitButton(),1,2);
    table.setAlignment(1,2,"left");
    form.add(table);
    return form;
  }

  private Form getUploadForm(ModuleInfo modinfo) throws Exception{
    Form form = new Form();
    form.setMultiPart();
    form.add(new FileInput());
    form.add(new SubmitButton());
    return form;
  }

}