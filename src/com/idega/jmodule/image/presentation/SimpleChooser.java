package com.idega.jmodule.image.presentation;

import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.jmodule.image.business.SimpleImage;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;
import com.idega.jmodule.image.data.ImageEntity;
import com.idega.util.idegaTimestamp;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

 public class SimpleChooser extends PresentationObjectContainer implements SimpleImage{

    private String sessImageParameter = "image_id";
    private boolean includeLinks;

    public void setToIncludeLinks(boolean includeLinks){
      this.includeLinks = includeLinks;
    }

    public void  main(IWContext iwc){
      checkParameterName(iwc);
      Table Frame = new Table();
      Frame.setCellpadding(0);
      Frame.setCellspacing(0);
      IFrame ifList = new IFrame(target1,SimpleLister.class);
      IFrame ifViewer = new IFrame(target2, SimpleViewer.class);
      ifList.setWidth(210);
      ifList.setHeight(410);
      ifViewer.setWidth(500);
      ifViewer.setHeight(410);

      ifList.setBorder(1);
      ifViewer.setBorder(1);
      Frame.add(ifList,1,1);
      Frame.add(ifViewer,2,1);
      Frame.setBorderColor("#00FF00");
      if(includeLinks)
        Frame.add(getLinkTable(),2,2);

      add(Frame);
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
        //add(sessImageParameter);
        iwc.setSessionAttribute(sessImageParameterName,sessImageParameter);
      }
      else if(iwc.getSessionAttribute(sessImageParameterName)!=null)
        sessImageParameter = (String) iwc.getSessionAttribute(sessImageParameterName);
    }

    public PresentationObject getLinkTable(){
      Table T = new Table();
      Link btnAdd = getNewImageLink("add");
        btnAdd.setFontStyle("text-decoration: none");
        btnAdd.setFontColor("#FFFFFF");
        btnAdd.setBold();
      Link btnDelete = getDeleteLink("delete");
        btnDelete.setFontStyle("text-decoration: none");
        btnDelete.setFontColor("#FFFFFF");
        btnDelete.setBold();
      Link btnSave = getSaveLink("save");
        btnSave.setFontStyle("text-decoration: none");
        btnSave.setFontColor("#FFFFFF");
        btnSave.setBold();
      Link btnReload = getReloadLink("reload");
        btnReload.setFontStyle("text-decoration: none");
        btnReload.setFontColor("#FFFFFF");
        btnReload.setBold();
      T.add(btnAdd,1,1);
      T.add(btnSave,2,1);
      T.add(btnDelete,3,1);
      T.add(btnReload,4,1);

      return T;
    }

    public Link getNewImageLink(String mo){
      Link L = new Link(mo,SimpleUploaderWindow.class);
      L.addParameter("action","upload");
      L.addParameter("submit","new");
      L.setTarget(target2);
      return L;
    }

    public Link getSaveLink(String mo){
      Link L = new Link(mo,SimpleViewer.class);
      L.addParameter(prmAction,actSave);
      L.setOnClick("window.close()");
      L.setTarget(target2);
      return L;
    }

    public Link getDeleteLink(String mo){
      Link L = new Link(mo,SimpleViewer.class);
      L.addParameter(prmAction,actDelete);
      L.setOnClick("top.setTimeout('top.frames.lister.location.reload()',150)");
      L.setTarget(target2);
      return L;
    }

    public Link getReloadLink(String mo){
      Link L = new Link(mo,SimpleLister.class);
      L.setTarget(target1);
      return L;
    }
}