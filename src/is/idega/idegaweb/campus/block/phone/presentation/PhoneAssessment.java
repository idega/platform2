package is.idega.idegaweb.campus.block.phone.presentation;

import is.idega.idegaweb.campus.block.phone.business.PhoneFileHandler;
import is.idega.idegaweb.campus.block.phone.data.PhoneFileInfo;

import java.io.File;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.idega.block.finance.presentation.Finance;
import com.idega.data.EntityFinder;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.util.Edit;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class PhoneAssessment extends Finance {

  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5;
  private final static String sAction = "cam.ph.file.action";
  private final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.campus";
 
  protected boolean isAdmin = false;
  private String dir = "/phone/upload/";


  protected void control(IWContext iwc){
   
    Table T = new Table();
    T.setCellpadding(0);
    T.setCellspacing(0);
    T.setWidth("100%");
    if(isAdmin){
      T.add(Edit.headerText(iwrb.getLocalizedString("phone_files","Phone Files"),3),1,1);
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
        Edit.setStyle(read);
        T.add(read,3,2);
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
      String filePath = iwc.getIWMainApplication().getRealPath(dir+fileName);
      new PhoneFileHandler().processFile(filePath);
    }
    return getFileTable(iwc);
  }

  private PresentationObject getFileTable(IWContext iwc){
    Table T = new Table();
    T.setCellpadding(0);
    T.setCellspacing(0);
    T.add(Edit.formatText(iwrb.getLocalizedString("files","Files")),1,1);
    T.add(Edit.formatText(iwrb.getLocalizedString("status","Status")),2,1);
    T.add(Edit.formatText(iwrb.getLocalizedString("time_read","Time read")),3,1);
    T.add(Edit.formatText(iwrb.getLocalizedString("line_count","Line Count")),4,1);
    T.add(Edit.formatText(iwrb.getLocalizedString("phone_numbers","PhoneNumbers")),5,1);
    T.add(Edit.formatText(iwrb.getLocalizedString("amount_read","Amount read")),6,1);
    Map M = mapOfReadFilesByFileName() ;
    try{
      File F = new File(iwc.getIWMainApplication().getRealPath("/phone/upload"));
      File[] Fs = F.listFiles();
      if(Fs.length > 0){
        String name;
        PhoneFileInfo info;
        java.text.NumberFormat NF = java.text.NumberFormat.getInstance();
        int row = 2;
        for (int i = 0; i < Fs.length; i++) {
          name = Fs[i].getName();

          if(M!= null && M.containsKey(name)){
            info = (PhoneFileInfo) M.get(name);
            T.add(Edit.formatText(name),1,row);
            T.add(Edit.formatText(iwrb.getLocalizedString("read","Read")),2,row);
            T.add(Edit.formatText(info.getReadTime().toString()),3,row);
            T.add(Edit.formatText(info.getLineCount()),4,row);
            T.add(Edit.formatText(info.getNumberCount()),5,row);
            T.add(Edit.formatText(NF.format(info.getTotalAmount())),6,row);
          }
          else{
            Link L = new Link(name);
            L.addParameter(sAction,ACT1);
            L.addParameter("filename",name);
            L.setFontSize(Edit.textFontSize);
            T.add(L,1,row);
            T.add(Edit.formatText(iwrb.getLocalizedString("unread","Unread")),2,row);
          }
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
    T.setColumnAlignment(4,"right");
    T.setColumnAlignment(5,"right");
    T.setColumnAlignment(6,"right");
    T.setWidth("100%");
    T.setCellpadding(2);
    T.setCellspacing(1);
    T.setHorizontalZebraColored(Edit.colorLight,Edit.colorWhite);
    T.setRowColor(1,Edit.colorMiddle);

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
