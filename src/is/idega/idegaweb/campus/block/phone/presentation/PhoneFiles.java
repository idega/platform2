package is.idega.idegaweb.campus.block.phone.presentation;

import is.idega.idegaweb.campus.presentation.Edit;
import is.idega.idegaweb.campus.block.phone.business.*;
import is.idega.idegaweb.campus.block.phone.data.CampusPhone;
import is.idega.idegaweb.campus.block.phone.data.PhoneFileInfo;
import com.idega.data.EntityFinder;
import com.idega.business.IWEventListener;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.Table;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.block.finance.data.AccountPhoneEntry;
import com.idega.block.finance.business.FinanceObject;
import com.idega.block.finance.presentation.Finance;
import com.idega.util.IWTimestamp;
import com.idega.idegaweb.presentation.BusyBar;
import java.io.File;
import java.io.FilenameFilter;
import java.sql.SQLException;
import java.util.List;
import java.util.Hashtable;
import java.util.Map;
import java.util.Iterator;
import java.util.Vector;

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
  private String dir = "/phone/upload/";

  private String sessConPrm = "sess_con_status";

  public String getLocalizedNameKey(){
    return "phonefiles";
  }

  public String getLocalizedNameValue(){
    return "Phonefiles";
  }
  protected void control(IWContext iwc){
    iwrb = getResourceBundle(iwc);
    iwb = getBundle(iwc);
    Table T = new Table();
    T.setCellpadding(0);
    T.setCellspacing(0);
    T.setWidth("100%");
    if(isAdmin){
      //T.add(Edit.headerText(iwrb.getLocalizedString("phone_files","Phone Files"),3),1,1);
      T.add(makeLinkTable(  1),1,2);
      int iAction = 0;
      if(iwc.getParameter(sAction )!= null){
        iAction = Integer.parseInt(iwc.getParameter(sAction ));
      }
      switch (iAction) {
        case ACT1 : T.add(getReadTable(iwc),1,3);     break;
        case ACT2 : T.add(getProcessTable(iwc),1,3);  break;
        default: T.add(getFileTable(iwc),1,3);        break;
      }
    }
    else
      T.add(Edit.formatText(iwrb.getLocalizedString("access_denied","Access denied")));
    //add(String.valueOf(iSubjectId));
    add(T);
  }

   protected PresentationObject makeLinkTable(int menuNr){
    Table LinkTable = new Table(3,1);
    int last = 3;
    LinkTable.setWidth("100%");
    LinkTable.setCellpadding(2);
    LinkTable.setCellspacing(1);
    LinkTable.setColor(Edit.colorDark);
    LinkTable.setWidth(last,"100%");

    return LinkTable;
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  private PresentationObject getReadTable(IWContext iwc){
    Form form = new Form();
    Table T = new Table();
    String fileName = iwc.getParameter("filename");
    T.add(Edit.formatText(iwrb.getLocalizedString("filename","Filename")),1,1);
    T.add(Edit.formatText(iwrb.getLocalizedString("filesize","Filesize")),2,1);
    if(fileName != null){
      try {
        File F = new File(dir,fileName);
        T.add(Edit.formatText(F.getName()),1,2);
        T.add(Edit.formatText(new Long(F.length()).toString()),2,2);
        T.add(new HiddenInput(sAction,String.valueOf(ACT2)));
        T.add(new HiddenInput("filename",fileName));
        SubmitButton read = new SubmitButton("read",iwrb.getLocalizedString("read","Read"));
        read.setOnClick("this.form.submit()");
        BusyBar busy = new BusyBar("phonebusy");
        busy.setInterfaceObject(read);
        Edit.setStyle(read);

        T.add(read,3,2);
        T.add(busy,3,2);
      }
      catch (Exception ex) {
        T.add(Edit.formatText(iwrb.getLocalizedString("no_file","No file")),1,2);
      }

    }
    form.add(T);
    return form;
  }

  private PresentationObject getProcessTable(IWContext iwc){

    String fileName = iwc.getParameter("filename");
    if(fileName != null){
      String filePath = iwc.getApplication().getRealPath(dir+fileName);
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
    T.addTitle(iwrb.getLocalizedString("phone_files","Phone files"));
    T.setTitlesHorizontal(true);
    int col = 2;
    T.add(Edit.formatText(iwrb.getLocalizedString("files","Files")),2,1);
    T.add(Edit.formatText(iwrb.getLocalizedString("size","Size")),3,1);
    T.add(Edit.formatText(iwrb.getLocalizedString("status","Status")),4,1);
    T.add(Edit.formatText(iwrb.getLocalizedString("time_read","Read")),5,1);
    T.add(Edit.formatText(iwrb.getLocalizedString("updated","Updated")),6,1);
    T.add(Edit.formatText(iwrb.getLocalizedString("line_count","Count")),7,1);
    T.add(Edit.formatText(iwrb.getLocalizedString("phone_numbers","Numbers")),8,1);
    T.add(Edit.formatText(iwrb.getLocalizedString("amount_read","Amount")),9,1);
    Map M = mapOfReadFilesByFileName() ;
    try{
      File F = new File(iwc.getApplication().getRealPath("/phone/upload"));
      PhoneFilenameFilter filter = new PhoneFilenameFilter();
      File[] Fs = F.listFiles(filter);
      if(Fs.length > 0){
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
        int row = 2;
        Link V;


        // Read files
        Iterator read = all.iterator();
        while(read.hasNext()){
          F = (File) read.next();
          name = F.getName();
          V = new Link("V");
          Window W = new Window("","/phone/upload/"+name);
          W.setResizable(true);
          V.setWindow(W);
          T.add(V,1,row);
          if(M!= null && M.containsKey(name)){
            info = (PhoneFileInfo) M.get(name);

            T.add(Edit.formatText(name),2,row);
            T.add(Edit.formatText(iwrb.getLocalizedString("read","Read")),4,row);
            T.add(Edit.formatText(info.getReadTime().toString()),5,row);
            T.add(Edit.formatText(info.getLineCount()),7,row);
            T.add(Edit.formatText(info.getNumberCount()),8,row);
            T.add(Edit.formatText(NF.format(info.getTotalAmount())),9,row);
          }

          T.add(Edit.formatText(Long.toString(F.length()/1000)+" KB"),3,row);
          T.add(Edit.formatText(new IWTimestamp(F.lastModified()).getLocaleDate(iwc.getCurrentLocale())),6,row);
          row++;
        }

        Iterator unread = unreadFiles.iterator();
        while(unread.hasNext()){
          F = (File) unread.next();
          name = F.getName();
          V = new Link("V");
          Window W = new Window("","/phone/upload/"+name);
          W.setResizable(true);
          V.setWindow(W);
          T.add(V,1,row);

          Link L = new Link(name);
          L.addParameter(sAction,ACT1);
          L.addParameter("filename",name);
          L.setFontSize(Edit.textFontSize);
          T.add(L,2,row);
          T.add(Edit.formatText(iwrb.getLocalizedString("unread","Unread")),4,row);
          T.add(Edit.formatText(Long.toString(F.length()/1000)+" KB"),3,row);
          T.add(Edit.formatText(new IWTimestamp(F.lastModified()).getLocaleDate(iwc.getCurrentLocale())),6,row);

          row++;
        }
      }
      else{
        T.add(iwrb.getLocalizedString("no_files","No files"));
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


  public void main(IWContext iwc){
    //isStaff = com.idega.core.accesscontrol.business.AccessControl
    isAdmin = iwc.hasEditPermission(this);
    control(iwc);
  }
}