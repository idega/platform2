/*
 * $Id: ImageInserter.java,v 1.1 2001/06/06 11:29:36 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.service;

import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.login.business.*;
import com.idega.jmodule.object.*;
import java.sql.SQLException;
import java.io.IOException;
import java.io.File;
import com.oreilly.servlet.multipart.*;
import com.oreilly.servlet.*;

/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class ImageInserter extends JModuleObject{

  private int iAct;
  private String sAct;
  private static final String strAction = "imageinsert_action";
  private static final String sUplDirPar = "upload_dir";
  private static final String sUploadDir = "/temp";
  private boolean isAdmin;
  private final int NOACT = 0,UPLOAD = 1, SAVE = 2,  NEW = 3, QUIT = 4;

  public ImageInserter(){

  }

  private void control(ModuleInfo modinfo){
/*
    try{

      if(modinfo.getParameter(strAction) == null){
        if(modinfo.getSession().getAttribute(strAction) !=null){
          sAct = (String)modinfo.getSession().getAttribute(strAction);

        }

      }
      else if(modinfo.getParameter(strAction) != null){
        sAct = modinfo.getParameter(strAction);
      }

      if(sAct !=null)
        iAct = Integer.parseInt(sAct);

      switch (iAct) {
        case UPLOAD : doUpload(modinfo);    break;
        case SAVE : doSave(modinfo);        break;
        case NEW : doMain(modinfo);         break;
        case QUIT : doQuit(modinfo);        break;
        default: doMain(modinfo);           break;
      }
    }
    catch(SQLException S){	S.printStackTrace();	}
    */
}

    private void doMain(ModuleInfo modinfo) throws SQLException {
      add(this.makeUploadForm(modinfo));
    }
/*
    private void doUpload(ModuleInfo modinfo) throws SQLException{
      String fileSeperator = System.getProperty("file.separator");
      Table MainTable = new Table();
      try {
      MultipartParser mp = new MultipartParser(modinfo.getRequest(), 10*1024*1024); // 10MB
      Part part;
      File dir = null;
      String value = null;
      while ((part = mp.readNextPart()) != null) {
        String name = part.getName();
        if(part.isParam() && part.getName().equalsIgnoreCase(sUplDirPar)){

          ParamPart paramPart = (ParamPart) part;
          dir = new File(paramPart.getStringValue());
          value = paramPart.getStringValue();
          add("<br>");
          add("\nparam; name=" + name + ", value=" + value);
        }
        if (part.isFile() && dir != null) {
          // it's a file part
          FilePart filePart = (FilePart) part;
          String fileName = filePart.getFileName();
          if (fileName != null) {
            //dir = new File(value+fileSeperator+fileName);
            //the part actually contained a file
            System.err.println(dir);
            long size = filePart.writeTo(dir);
            add("<br>");
            add("\nfile; name=" + name + "; filename=" + fileName +", content type=" + filePart.getContentType() + " size=" + size);
            String l = fileSeperator+"temp"+fileSeperator;
            if(filePart.getContentType().equalsIgnoreCase("image/pjpeg")){
            MainTable.add(new Image(l+fileName),1,3);
            }
          }
        }
      }
    }
    catch (IOException io) {
      io.printStackTrace();
      System.err.print( "error reading or saving file");
    }

    modinfo.getSession().removeAttribute(strAction);
    }
*/
    private void doQuit(ModuleInfo modinfo) throws SQLException{

    }

    private void doSave(ModuleInfo modinfo) throws SQLException{

    }

    private ModuleObject makeUploadForm(ModuleInfo modinfo){

      Form myForm = new Form();
      myForm.maintainAllParameters();
      myForm.setMultiPart();
      modinfo.getSession().setAttribute(strAction,String.valueOf(UPLOAD) );
      myForm.add(new HiddenInput(sUplDirPar,sUploadDir ));
      myForm.add(new FileInput());
      myForm.add(new SubmitButton());

      Table MainTable = new Table();
      MainTable.add("Veldu skjal",1,2);
      MainTable.add(myForm,1,3);
      MainTable.add("<br><br><br>",1,4);
      return MainTable;
    }

  public void main(ModuleInfo modinfo)  {
    try{
    isAdmin = com.idega.jmodule.login.business.AccessControl.isAdmin(modinfo);
    }
    catch(SQLException sql){ isAdmin = false;}
    /** @todo: fixa Admin*/
    control(modinfo);
  }
}// class PriceCatalogueMaker


