package is.idega.idegaweb.campus.block.application.presentation;



import is.idega.idegaweb.campus.presentation.CampusBlock;
import is.idega.idegaweb.campus.presentation.Edit;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.application.data.ApplicationSubject;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;
import com.idega.util.LocaleUtil;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author  <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */


public class SubjectMaker extends CampusBlock{


  
  private final String strAction = "fin_action";

  protected boolean isAdmin = false;
  


  public String getLocalizedNameKey(){
    return "subjects";
  }

  public String getLocalizedNameValue(){
    return "Subjects";
  }

 

  protected void control(IWContext iwc){


      if(isAdmin){
        if(iwc.getParameter("save") != null){
          doUpdate(iwc);
        }
        else if(iwc.getParameter("delete")!= null){
          doDelete(iwc);
        }
        this.add(makeInputTable(iwc));
      }
      else
        this.add(getNoAccessObject(iwc));

  }

  public PresentationObject makeLinkTable(int menuNr){
    Table LinkTable = new Table(6,1);

    return LinkTable;
  }

  public PresentationObject makeInputTable(IWContext iwc){

    Table Frame = new Table(3,2);
      Frame.setCellpadding(0);
      Frame.setCellspacing(0);
    Table Left = new Table();
      Left.setCellpadding(0);
      Left.setCellspacing(0);
    Table Right = new Table();
      Right.setCellpadding(0);
      Right.setCellspacing(0);
    Frame.add(Left,1,1);
    Frame.add(Right,3,1);
    Collection L = null;
	try {
		L = getApplicationService(iwc).getSubjectHome().findAll();
	}
	catch (RemoteException e) {
		e.printStackTrace();
	}
	catch (FinderException e) {
		e.printStackTrace();
	}  //List L = ApplicationFinder.listOfSubject();
    Table T = new Table();
    TextInput Description = new TextInput("app_subj_desc");
    DateInput ExpireDate = new DateInput("app_subj_xdate",true);
    ExpireDate.setDate(IWTimestamp.RightNow().getSQLDate());
    SubmitButton SaveButton = new SubmitButton("save","Save");

  
    T.add(getHeader(localize("description", "Description")) +" :",1,1);
    T.add(getHeader(localize("expiredate", "Expiredate")) +" :",2,1);

    T.add(Description,1,2);
    T.add(ExpireDate,2,2);
    T.add(SaveButton,3,2);
    if(L != null){
     
      int a = 3;
      for (Iterator iter = L.iterator(); iter.hasNext();) {
		ApplicationSubject AS = (ApplicationSubject) iter.next();
	    T.add(Edit.formatText(AS.getDescription()),1,a);
        T.add(Edit.formatText(new IWTimestamp(AS.getExpires()).getLocaleDate(LocaleUtil.getIcelandicLocale())),2,a);
        T.add((getDeleteLink(AS)),3,a);
        a++;
      }

    }
    Form F = new Form();
    F.add(T);
    Right.add(F);

    return Frame;
  }

  public Link getDeleteLink(ApplicationSubject AS){
    Link L = new Link("X");
    L.addParameter("delete",AS.getPrimaryKey().toString());
    return L;
  }

  public void doDelete(IWContext iwc){
    try {
      int id = Integer.parseInt(iwc.getParameter("delete"));
      ApplicationSubject AS = getApplicationService(iwc).getSubjectHome().findByPrimaryKey(new Integer(id));
      AS.remove();
    }
    catch (Exception ex) {

    }

  }

  public void doUpdate(IWContext iwc){
    String sDesc= iwc.getParameter("app_subj_desc").trim();
    String sDate = iwc.getParameter("app_subj_xdate");
    
	if(sDesc.length() > 0){
    	try {
			getApplicationService(iwc).storeApplicationSubject(sDesc,new IWTimestamp(sDate));
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (CreateException e) {
			e.printStackTrace();
		}
    
    
    }
  }

   public void main(IWContext iwc){
    isAdmin = iwc.hasEditPermission(this);
    control(iwc);
  }
  

}
