package is.idegaweb.campus.service;

import com.idega.jmodule.object.ModuleObjectContainer;
import com.idega.jmodule.object.ModuleObjectContainer;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.image.data.ImageEntity;
import com.idega.util.idegaTimestamp;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

 public class ImageChooser extends ModuleObjectContainer{
    public String prmImageView = "img_view_id";
    public String target1 = "lister",target2 = "viewer",target3 = "buttons";
    public String viewUrl = "/image/singleview.jsp";
    public String newUrl = "/image/insertimage.jsp";
    public String listUrl = "/image/imagelist.jsp";
    public String buttonUrl = "/image/imagebuttons.jsp";
    public String sessionSaveParameter = "image_id";
    public static final String prmAction = "img_view_action";
    public static final String actSave = "save",actDelete = "delete";
    public String sessImageParameterName = "im_image_session_name";
    public String sessImageParameter = "image_id";


    public void  main(ModuleInfo modinfo){
      checkParameterName(modinfo);
      Table outerTable = new Table(1,2);
      outerTable.setBorder(1);
      outerTable.setCellpadding(0);
      outerTable.setCellspacing(0);
      outerTable.setWidth("100%");
      outerTable.setColor(1,1,"#0E2456");
      outerTable.add(getLinkTable());
      Table Frame = new Table();
      Frame.setCellpadding(0);
      Frame.setCellspacing(0);
      IFrame ifList = new IFrame(target1,listUrl,220,400);
      IFrame ifViewer = new IFrame(target2,viewUrl,500,400);
      IFrame ifButtons = new IFrame(target3,buttonUrl,500,50);
      ifList.setBorder(1);
      ifViewer.setBorder(1);
      ifButtons.setBorder(1);
      //ifViewer.setScrolling(IFrame.SCROLLING_NO);
      Frame.add(ifList,1,1);
      Frame.add(ifViewer,2,1);
      Frame.setBorderColor("#00FF00");
      Frame.add(getButtonTable(),2,2);
      outerTable.add(Frame,1,2);
      add(outerTable);
    }
    public void setSessionSaveParameterName(String prmName){
      sessionSaveParameter = prmName;
    }
    public String getSessionSaveParameterName(){
      return sessionSaveParameter;
    }
     public void checkParameterName(ModuleInfo modinfo){
       if(modinfo.getParameter(sessImageParameterName)!=null){
        sessImageParameter = modinfo.getParameter(sessImageParameterName);
        modinfo.setSessionAttribute(sessImageParameterName,sessImageParameter);
      }
      else if(modinfo.getSessionAttribute(sessImageParameterName)!=null)
        sessImageParameter = (String) modinfo.getSessionAttribute(sessImageParameterName);
    }

    public ModuleObject getLinkTable(){
      Table headerTable = new Table(2,2);
        headerTable.setCellpadding(0);
        headerTable.setCellspacing(0);
        headerTable.setWidth("100%");
        headerTable.mergeCells(1,2,2,2);
        headerTable.setAlignment(1,2,"center");
        headerTable.setAlignment(2,1,"right");

      Image idegaweb = new Image("/pics/idegaweb.gif","idegaWeb");
        headerTable.add(idegaweb,1,1);
      Text tEditor = new Text("Image Chooser&nbsp;&nbsp;");
        tEditor.setBold();
        tEditor.setFontColor("#FFFFFF");
        tEditor.setFontSize("3");
        headerTable.add(tEditor,2,1);

      Table LinkTable = new Table();
        LinkTable.setBorder(0);
        LinkTable.setCellpadding(3);
        LinkTable.setCellspacing(3);
        headerTable.add(LinkTable,1,2);

      return headerTable;
    }

    public ModuleObject getButtonTable(){
      Link btnAdd = getNewImageLink("add");
      Table T = new Table();
      T.add(btnAdd,1,1);
      //if(sImageId!= null){
        Link btnDelete = getDeleteLink("delete");
        Link btnSave = getSaveLink("save");
        Link btnReload = getReloadLink("reload");
        T.add(btnSave,3,1);
        T.add(btnDelete,4,1);
        T.add(btnReload,5,1);
      //}
      return T;
    }

    public Link getNewImageLink(String name){
      Link L = new Link(name,newUrl);
      L.addParameter("submit","new");
      L.addParameter(sessImageParameterName,sessImageParameter);

      L.setTarget(target2);
      return L;
    }

    public Link getSaveLink(String name){
      Link L = new Link(name,viewUrl);
      L.addParameter(prmAction,actSave);
      L.setOnClick("window.close()");
      L.setTarget(target2);
      return L;
    }

    public Link getDeleteLink(String name){
      Link L = new Link(name,viewUrl);
      L.addParameter(prmAction,actDelete);
      L.setOnClick("top.setTimeout('top.frames.lister.location.reload()',150)");
      L.setTarget(target2);
      return L;
    }

    public Link getReloadLink(String name){
      Link L = new Link(name,listUrl);
      L.setTarget(target1);
      return L;
    }
}