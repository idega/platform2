package is.idega.idegaweb.golf.block.image.presentation;

import is.idega.idegaweb.golf.block.image.business.SimpleImage;

import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.PresentationObjectContainer;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.IFrame;

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
    private IWBundle iwb;
    private String IW_BUNDLE_IDENTIFIER="com.idega.block.image";

    public void setToIncludeLinks(boolean includeLinks){
      this.includeLinks = includeLinks;
    }
    public String getBundleIdentifier(){
      return IW_BUNDLE_IDENTIFIER;
    }

    public void  main(IWContext modinfo){
      iwb = getBundle(modinfo);
      checkParameterName(modinfo);
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
        Frame.add(getLinkTable(iwb),2,2);

      add(Frame);
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
        //add(sessImageParameter);
        modinfo.setSessionAttribute(sessImageParameterName,sessImageParameter);
      }
      else if(modinfo.getSessionAttribute(sessImageParameterName)!=null)
        sessImageParameter = (String) modinfo.getSessionAttribute(sessImageParameterName);
    }

    public PresentationObject getLinkTable(IWBundle iwb){
      Table T = new Table();

      Text add = new Text("add");
      add.setFontStyle("text-decoration: none");
      add.setFontColor("#FFFFFF");
      add.setBold();
      Link btnAdd = getNewImageLink(add);

      Text del = new Text("delete");
      del.setFontStyle("text-decoration: none");
      del.setFontColor("#FFFFFF");
      del.setBold();
      Link btnDelete = getDeleteLink(del);

      Text save = new Text("use");
      save.setFontStyle("text-decoration: none");
      save.setFontColor("#FFFFFF");
      save.setBold();
      Link btnSave = getSaveLink(save);

      Text reload = new Text("reload");
      reload.setFontStyle("text-decoration: none");
      reload.setFontColor("#FFFFFF");
      reload.setBold();
      Link btnReload = getReloadLink(reload);

      T.add(btnAdd,1,1);
      T.add(btnSave,2,1);
      T.add(btnDelete,3,1);
      T.add(btnReload,4,1);

      return T;
    }

    public Link getNewImageLink(PresentationObject mo){
      Link L = new Link(mo,SimpleUploaderWindow.class);
      L.addParameter("action","upload");
      L.addParameter("submit","new");
      L.setTarget(target2);
      return L;
    }

    public Link getSaveLink(PresentationObject mo){
      Link L = new Link(mo,SimpleViewer.class);
      L.addParameter(prmAction,actSave);
      L.setOnClick("window.close()");
      L.setTarget(target2);
      return L;
    }

    public Link getDeleteLink(PresentationObject mo){
      Link L = new Link(mo,SimpleViewer.class);
      L.addParameter(prmAction,actDelete);
      L.setOnClick("top.setTimeout('top.frames.lister.location.reload()',150)");
      L.setTarget(target2);
      return L;
    }

    public Link getReloadLink(PresentationObject mo){
      Link L = new Link(mo,SimpleLister.class);
      L.setTarget(target1);
      return L;
    }
}