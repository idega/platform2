package is.idega.demo.textviewer.presentation;

import com.idega.presentation.*;
import com.idega.presentation.text.*;

import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileHome;

import com.idega.idegaweb.*;
import is.idega.demo.textviewer.business.*;

import java.util.*;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      Idega hf
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */

public class TextFileViewer extends Block {

  private ICFile textFileFolder;
  private static final String IW_BUNDLE_IDENTIFIER="is.idega.demo.textviewer";

  private static String PARAM_VIEW_FILE="is_tev_v_f";
  private static String PARAM_VIEW_FILE_ID="is_tev_v_f_id";

  private boolean aGettingFileContent=false;
  private boolean aGettingFileFolder=false;

  private Table mainTable;
  private int rowIndex = 1;

  public TextFileViewer() {
  }

  public String getBundleIdentifier(){
    return this.IW_BUNDLE_IDENTIFIER;
  }

  public void setFilesFolder(ICFile file){
    this.textFileFolder=file;
  }

  public ICFile getFilesFolder(){
    return this.textFileFolder;
  }

  public void main(IWContext iwc){
    try{
      IWResourceBundle iwrb = this.getResourceBundle(iwc);
      parseAction(iwc);
      if(this.aGettingFileFolder){
        if(this.getFilesFolder()!=null){
          Iterator iter = getFilesFolder().getChildrenIterator();
          while (iter.hasNext()) {
            ICFile file = (ICFile)iter.next();
            Link link = new Link(file.getName());
            link.addParameter(PARAM_VIEW_FILE,"Y");
            link.addParameter(PARAM_VIEW_FILE_ID,file.getPrimaryKey().toString());
            add(link);
          }
        }
        else{
          add(iwrb.getLocalizedString("no_folder_specified","No folder specified"));
        }
      }
      else if(this.aGettingFileContent){
        int fileID = Integer.parseInt(iwc.getParameter(this.PARAM_VIEW_FILE_ID));
        String fileString = getFileToString(fileID,iwc);
        ScrambledText sc = new ScrambledText();
        sc.setText(fileString);
        add(sc);
        if(!this.hasEditPermission()){
          sc.setScrambled(true);
        }
      }
    }
    catch(Exception e){
      add(new ExceptionWrapper(e,this));
    }
  }

  public void add(PresentationObject obj){
    if(mainTable==null){
      mainTable = new Table();
      super.add(mainTable);
    }
    mainTable.add(obj,1,rowIndex++);
  }

  public String getFileToString(int fileID,IWContext iwc)throws Exception{
    //ICFile file = getFileHome().findByPrimaryKey(fileID);
    //return file.getName();
    return getTextViewBusiness(iwc).getFileAsString(fileID);
  }

  protected TextViewBusiness getTextViewBusiness(IWContext iwc)throws java.rmi.RemoteException{
    return (TextViewBusiness)com.idega.business.IBOLookup.getServiceInstance(iwc,TextViewBusiness.class);
  }



  public ICFileHome getFileHome()throws Exception{
    return (ICFileHome)com.idega.data.IDOLookup.getHome(ICFile.class);
  }

  private void parseAction(IWContext iwc){
      String view_file = iwc.getParameter(PARAM_VIEW_FILE);
      if(view_file!=null){
        this.aGettingFileContent=true;
      }
      else{
        this.aGettingFileFolder=true;
      }
  }


}