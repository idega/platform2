package is.idega.idegaweb.campus.block.phone.presentation;

import is.idega.idegaweb.campus.block.phone.business.PhoneFileHandler;
import is.idega.idegaweb.campus.block.phone.data.PhoneFileInfo;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.idega.block.finance.presentation.Finance;
import com.idega.data.EntityFinder;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.BusyBar;
import com.idega.io.UploadFile;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.FileInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.Window;
import com.idega.util.IWTimestamp;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class PhoneFiles extends Finance {
  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5;
  private final static String sAction = "cam.ph.file.action";
  private final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.campus";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;
  protected boolean isAdmin = false;
  private String dir = "phone"+File.separator+"upload";

  public String getLocalizedNameKey(){
    return "phonefiles";
  }

  public String getLocalizedNameValue(){
    return "Phonefiles";
  }
  protected void control(IWContext iwc){
   
    Table T = new Table();
    T.setCellpadding(0);
    T.setCellspacing(0);
    T.setWidth(T.HUNDRED_PERCENT);
    if(isAdmin){
      //T.add(Edit.headerText(iwrb.getLocalizedString("phone_files","Phone Files"),3),1,1);
      T.add(makeLinkTable(  1),1,2);
      int iAction = 0;
      if(iwc.getParameter(sAction )!= null){
        iAction = Integer.parseInt(iwc.getParameter(sAction ));
      }
      switch (iAction) {
        case ACT1 : T.add(getReadTable(iwc),1,3);    break;
        case ACT2 : T.add(getProcessTable(iwc),1,3); T.add(getFileUploader(iwc),1,4); break;
        case ACT3 : uploadFile(iwc);
        default: T.add(getFileTable(iwc),1,3);T.add(getFileUploader(iwc),1,4);        break;
      }
      
    }
    else
      T.add(getText(localize("access_denied","Access denied")));
    //add(String.valueOf(iSubjectId));
    add(T);
  }

   protected PresentationObject makeLinkTable(int menuNr){
    Table LinkTable = new Table(3,1);
    int last = 3;
    LinkTable.setWidth(LinkTable.HUNDRED_PERCENT);
    LinkTable.setCellpadding(2);
    LinkTable.setCellspacing(1);
    LinkTable.setColor(getHeaderColor());
    LinkTable.setWidth(last,LinkTable.HUNDRED_PERCENT);

    return LinkTable;
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  private PresentationObject getReadTable(IWContext iwc){
    Form form = new Form();
    Table T = new Table();
    String fileName = iwc.getParameter("filename");
    T.add(getHeader(localize("filename","Filename")),1,1);
    T.add(getHeader(localize("filesize","Filesize")),2,1);
    if(fileName != null){
      try {
        File F = new File(dir,fileName);
        T.add(getText(F.getName()),1,2);
        T.add(getText(new Long(F.length()).toString()),2,2);
        T.add(new HiddenInput(sAction,String.valueOf(ACT2)));
        T.add(new HiddenInput("filename",fileName));
        SubmitButton read = new SubmitButton("read",localize("read","Read"));
        read.setOnClick("this.form.submit()");
        BusyBar busy = new BusyBar("phonebusy");
        busy.setInterfaceObject(read);
      

        T.add(read,3,2);
        T.add(busy,3,2);
      }
      catch (Exception ex) {
        T.add(getText(localize("no_file","No file")),1,2);
      }

    }
    form.add(T);
    return form;
  }

  private PresentationObject getProcessTable(IWContext iwc){

    String fileName = iwc.getParameter("filename");
    if(fileName != null){
      String filePath = iwc.getIWMainApplication().getApplicationRealPath()+dir+File.separator+fileName;
      new PhoneFileHandler().processFile(filePath);
    }
    return getFileTable(iwc);
  }

  private class PhoneFilenameFilter implements FilenameFilter {

    public boolean accept(File dir,String name){
      return (name.endsWith(".del"));
    }
  }

  private PresentationObject getFileTable(IWContext iwc){
    DataTable T = new DataTable();
    T.addTitle(localize("phone_files","Phone files"));
    T.setTitlesHorizontal(true);
    int col = 2;
    T.add(getHeader(localize("files","Files")),2,1);
    T.add(getHeader(localize("size","Size")),3,1);
    T.add(getHeader(localize("status","Status")),4,1);
    T.add(getHeader(localize("time_read","Read")),5,1);
    T.add(getHeader(localize("updated","Updated")),6,1);
    T.add(getHeader(localize("line_count","Count")),7,1);
    T.add(getHeader(localize("phone_numbers","Numbers")),8,1);
    T.add(getHeader(localize("amount_read","Amount")),9,1);
	int row = 2;
    Map M = mapOfReadFilesByFileName() ;
    try{
      File F = new File(iwc.getIWMainApplication().getApplicationRealPath()+dir);
      PhoneFilenameFilter filter = new PhoneFilenameFilter();
      File[] Fs = F.listFiles(filter);
      
      if(Fs!=null && Fs.length > 0){
        List allFiles = java.util.Arrays.asList(Fs);
        Vector all = new Vector(allFiles);
        Vector unreadFiles = new Vector(allFiles);
        Iterator iter = unreadFiles.iterator();
        while(iter.hasNext()){
          F = (File) iter.next();
          if(M!= null && M.containsKey(F.getName()))
            iter.remove();
        }
        all.removeAll(unreadFiles);
        String name;
        PhoneFileInfo info;
        java.text.NumberFormat NF = java.text.NumberFormat.getCurrencyInstance(iwc.getCurrentLocale());
        
        Link V;


        // Read files
        Iterator read = all.iterator();
        while(read.hasNext()){
          F = (File) read.next();
          name = F.getName();
          V = new Link(iwb.getImage("view.gif"));
          Window W = new Window("",dir+File.separator+name);
          W.setResizable(true);
          V.setWindow(W);
          T.add(V,1,row);
          if(M!= null && M.containsKey(name)){
            info = (PhoneFileInfo) M.get(name);

            T.add(getText(name),2,row);
            T.add(getText(localize("read","Read")),4,row);
            T.add(getText(info.getReadTime().toString()),5,row);
            T.add(getText(String.valueOf(info.getLineCount())),7,row);
            T.add(getText(String.valueOf(info.getNumberCount())),8,row);
            T.add(getAmountText((info.getTotalAmount())),9,row);
          }

          T.add(getText(Long.toString(F.length()/1000)+" KB"),3,row);
          T.add(getText(new IWTimestamp(F.lastModified()).getLocaleDate(iwc.getCurrentLocale())),6,row);
          row++;
        }

        Iterator unread = unreadFiles.iterator();
        while(unread.hasNext()){
          F = (File) unread.next();
          name = F.getName();
          V = new Link(iwb.getImage("view.gif"));
          Window W = new Window("",dir+File.separator+name);
          W.setResizable(true);
          V.setWindow(W);
          T.add(V,1,row);

          Link L = new Link(name);
          L.addParameter(sAction,ACT1);
          L.addParameter("filename",name);
          //L.setFontSize(Edit.textFontSize);
          T.add(L,2,row);
          T.add(getHeader(localize("unread","Unread")),4,row);
          T.add(getText(Long.toString(F.length()/1000)+" KB"),3,row);
          T.add(getText(new IWTimestamp(F.lastModified()).getLocaleDate(iwc.getCurrentLocale())),6,row);

          row++;
        }
      }
      else{
      	T.getContentTable().mergeCells(1,row,T.getContentTable().getColumns(),row);
        T.add(getText(localize("no_files","No files")),1,row);
      }
    }
    catch(Exception e){
      e.printStackTrace();
    }
    /*
    T.setColumnAlignment(4,"right");
    T.setColumnAlignment(5,"right");
    T.setColumnAlignment(6,"right");
    */
    T.setWidth("100%");

    return T;
  }

  private Map mapOfReadFilesByFileName(){
    List L = null;
    Hashtable H = null;
    try {
      L = EntityFinder.getInstance().findAll(PhoneFileInfo.class);
      if(L!= null){
        int len = L.size();
        H = new Hashtable(len);
        for (int i = 0; i < len; i++) {
          PhoneFileInfo info = (PhoneFileInfo) L.get(i);
          H.put(info.getName(),info);
        }
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return H;
  }
  
  private PresentationObject getFileUploader(IWContext iwc) {
	Form form = new Form();
	form.setMultiPart();
	form.add(new HiddenInput(sAction, String.valueOf(ACT3)));
	FileInput chooser = new FileInput();
	SubmitButton confirm = new SubmitButton(localize("commit","commit"));
  	Table T = new Table();
  	T.add(new Text(localize("upload_file","Upload file")),1,1);
  	T.add(chooser,2,1);
  	T.add(new Text(localize("new_file_name","New file name")),3,1);
  	T.add(new TextInput("new_file_name"),4,1);
  	T.add(confirm,5,1);
  	form.add(T);
	return form;
	}
	
	public void uploadFile(IWContext iwc){
		UploadFile file = iwc.getUploadedFile();
		String fileName  = file.getName(); 
		String newParent = iwc.getIWMainApplication().getApplicationRealPath()+dir;
		String newFileName =	newParent+File.separator+fileName; 
		String changedFileName = iwc.getMultipartParameter("new_file_name");
		if(changedFileName !=null && !"".equals(changedFileName)){
			fileName = changedFileName;
			debug("New phone file name was supplied");	
		}
			
		if(file.renameTo(new File(newParent,fileName)))
			debug("New phone file was uploaded");
		
	}
	

  public void main(IWContext iwc){
    //isStaff = com.idega.core.accesscontrol.business.AccessControl
    isAdmin = iwc.hasEditPermission(this);
    control(iwc);
  }
}