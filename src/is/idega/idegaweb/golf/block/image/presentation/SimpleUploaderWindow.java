package is.idega.idegaweb.golf.block.image.presentation;


import is.idega.idegaweb.golf.block.image.business.ImageBusiness;
import is.idega.idegaweb.golf.block.image.business.ImageProperties;
import is.idega.idegaweb.golf.block.image.business.SimpleImage;

import java.sql.Connection;
import java.sql.SQLException;

import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.ui.FileInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.Window;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class SimpleUploaderWindow extends Window implements SimpleImage{

    String dataBaseType;
    private String sessImageParameter = "image_id";
    Connection Conn = null;

    public SimpleUploaderWindow(){

    }

    public void setSessionSaveParameterName(String prmName){
      sessImageParameter = prmName;
    }
    public String getSessionSaveParameterName(){
      return sessImageParameter;
    }
     public void checkParameterName(IWContext modinfo){
       if(modinfo.getParameter(sessImageParameterName)!=null){
        sessImageParameter = modinfo.getParameter(sessImageParameterName);
        modinfo.setSessionAttribute(sessImageParameterName,sessImageParameter);
      }
      else if(modinfo.getSessionAttribute(sessImageParameterName)!=null)
        sessImageParameter = (String) modinfo.getSessionAttribute(sessImageParameterName);
       //add(sessImageParameter);
    }

    public void main(IWContext modinfo){
      checkParameterName(modinfo);
      this.setBackgroundColor("white");
      this.setTitle("Idega Uploader");
      control(modinfo);
    }

    public void control(IWContext modinfo){
      //add(sessImageParameter);
      String sContentType = modinfo.getRequest().getContentType();
      if(sContentType !=null && sContentType.indexOf("multipart")!=-1){
        //add(sContentType);
        add(parse(modinfo));
      }
      else{
        if(modinfo.getParameter("save")!=null){
          save(modinfo);
        }
        else
          add(getMultiForm(modinfo));
      }

    }
    public Form getMultiForm(IWContext modinfo){
      Form f = new Form();
      f.setMultiPart();
      String s = modinfo.getRequestURI()+"?"+com.idega.presentation.Page.IW_FRAME_CLASS_PARAMETER+"="+com.idega.idegaweb.IWMainApplication.getEncryptedClassName(this.getClass());
      f.setAction(s);
      add(s);
      f.add(new FileInput());
      f.add(new SubmitButton());
      return f;
    }

    public PresentationObject parse(IWContext modinfo){
      ImageProperties ip = null;
      try {
        ip = ImageBusiness.doUpload(modinfo);
        modinfo.setSessionAttribute("image_props",ip);
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }

      if(ip!=null){
        Form form = new Form();
        Table T = new Table();
        T.add(new Image(ip.getWebPath()),1,1);
        T.add(new SubmitButton("save","Save"),1,2);
        T.add(new SubmitButton("newimage","New"),1,2);
        form.add(T);
        return form;
      }
      else{
        return getMultiForm(modinfo);
      }

    }

    public void save(IWContext modinfo){
      ImageProperties ip = null;
      if(modinfo.getSessionAttribute("image_props")!=null){
        ip = (ImageProperties) modinfo.getSessionAttribute("image_props");
        modinfo.removeSessionAttribute("image_props");
      }
      if(ip !=null){
        int i = ImageBusiness.SaveImage(ip);
        modinfo.setSessionAttribute(sessImageParameter,String.valueOf(i));
        setParentToReload();
        try {
          add(new GolfImage(i));
        }
        catch (SQLException ex) {

        }
      }
    }
}