package se.idega.idegaweb.commune.block.importer.presentation;

import java.io.File;
import com.idega.presentation.*;


/**
 * <p>Title: IdegaWeb classes</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Idega Software</p>
 * @author <a href="mailto:eiki@idega.is"> Eirikur Sveinn Hrafnsson</a>
 * @version 1.0
 */

public class Importer extends Block {
  String folderPath;
  boolean selectFiles,importFiles,selectFolder = false;

  private final String ACTION_PARAMETER = "se_im_ac"; //action
  private final String SELECT_FILES = "se_im_sf"; //select files action
  private final String IMPORT_FILES = "se_im_if"; //import files action
  private final String IMPORT_FILE_PATHS = "se_im_fp"; //list of files
  private final String SELECT_NEW_FOLDER = "se_im_snf"; //new folder overrides builder parameter action
  private final String NEW_FOLDER_PATH = "se_im_nfp"; //new folder path

  public final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune";

  public Importer() {
  }

  public void setFolderPath(String path){
    this.folderPath = path;
  }

  public String getFolderPath(){
    return this.folderPath;
  }

  private void parseAction(IWContext iwc){
    if( iwc.isParameterSet(ACTION_PARAMETER) ){
      if( iwc.getParameter(ACTION_PARAMETER).equals(IMPORT_FILES) ){
        importFiles = true;
      }
      else if( iwc.getParameter(ACTION_PARAMETER).equals(SELECT_FILES) ){
        if( iwc.isParameterSet(NEW_FOLDER_PATH) ){
          this.setFolderPath(iwc.getParameter(NEW_FOLDER_PATH));
        }
        selectFiles = true;
      }
      else if(  iwc.getParameter(ACTION_PARAMETER).equals(SELECT_NEW_FOLDER) ){
        selectFolder = true;
      }
    }
    else{
     selectFiles = true;
    }

  }

  public void main(IWContext iwc) throws Exception {

    if( selectFiles ){
      if( this.getFolderPath()!=null ){
        File folder = new File(getFolderPath());

        if( folder.isDirectory() ){



        }
        else{

        }


      }

    }
    else if( selectFolder ){

    }
    else if( importFiles ){


    }



  }





}