package com.idega.jmodule.image.presentation;

import com.idega.jmodule.image.data.*;
import com.idega.presentation.ui.Window;
import com.idega.jmodule.image.business.SimpleImage;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.Image;
import com.idega.util.*;
import java.sql.*;
import java.io.*;
import java.util.*;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.*;
import com.idega.jmodule.image.data.*;
import com.idega.jmodule.image.business.*;
import com.idega.idegaweb.IWMainApplication;
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
     public void checkParameterName(IWContext iwc){
       if(iwc.getParameter(sessImageParameterName)!=null){
        sessImageParameter = iwc.getParameter(sessImageParameterName);
        iwc.setSessionAttribute(sessImageParameterName,sessImageParameter);
      }
      else if(iwc.getSessionAttribute(sessImageParameterName)!=null)
        sessImageParameter = (String) iwc.getSessionAttribute(sessImageParameterName);
       //add(sessImageParameter);
    }

    public void main(IWContext iwc){
      checkParameterName(iwc);
      this.setBackgroundColor("white");
      this.setTitle("Idega Uploader");
      control(iwc);
    }

    public void control(IWContext iwc){
      //add(sessImageParameter);
      String sContentType = iwc.getRequest().getContentType();
      if(sContentType !=null && sContentType.indexOf("multipart")!=-1){
        //add(sContentType);
        add(parse(iwc));
      }
      else{
        if(iwc.getParameter("save")!=null){
          save(iwc);
        }
        else {
          if(iwc.getParameter("new")!=null)
            deleteImage(iwc);
          add(getMultiForm(iwc));
        }
      }

    }
    public Form getMultiForm(IWContext iwc){
      Form f = new Form();
      f.setMultiPart();
      //f.setAction(iwc.getRequestURI()+"?"+com.idega.presentation.Page.IW_FRAME_CLASS_PARAMETER+"="+com.idega.idegaweb.IWMainApplication.getEncryptedClassName(this.getClass()));
      String s = iwc.getRequestURI()+"?"+IWMainApplication.classToInstanciateParameter+"="+IWMainApplication.getEncryptedClassName(this.getClass());
      f.setAction(s);
      f.add(new FileInput());
      f.add(new SubmitButton());
      return f;
    }

    public PresentationObject parse(IWContext iwc){
      ImageProperties ip = null;
      try {
        ip = ImageBusiness.doUpload(iwc);
        iwc.setSessionAttribute("image_props",ip);
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
        return getMultiForm(iwc);
      }

    }

    public void deleteImage(IWContext iwc){
      ImageProperties ip = null;
      if(iwc.getSessionAttribute("image_props")!=null){
        ip = (ImageProperties) iwc.getSessionAttribute("image_props");
        iwc.removeSessionAttribute("image_props");
      }
      if(ip !=null){
        try {
           new File(ip.getRealPath()).delete();
        }
        catch (Exception ex) {
          ex.printStackTrace();
        }
        setParentToReload();

      }
    }

    public void save(IWContext iwc){
      ImageProperties ip = null;
      if(iwc.getSessionAttribute("image_props")!=null){
        ip = (ImageProperties) iwc.getSessionAttribute("image_props");
        iwc.removeSessionAttribute("image_props");
      }
      if(ip !=null){
        String mmProp = iwc.getApplication().getSettings().getProperty(com.idega.block.media.servlet.MediaServlet.USES_OLD_TABLES);
        int i = -1;
        if(mmProp!=null) {
          i = ImageBusiness.SaveImage(ip);
        }
        else{
          com.idega.block.media.business.ImageProperties ip2 =
          new com.idega.block.media.business.ImageProperties( ip.getName(),ip.getContentType(),ip.getRealPath(),ip.getWebPath(),ip.getSize());
          i = com.idega.block.media.business.ImageBusiness.SaveImage(ip2);
        }

        iwc.setSessionAttribute(sessImageParameter,String.valueOf(i));
        setParentToReload();
        try {
          add(new Image(i));
        }
        catch (SQLException ex) {

        }
      }
    }
}