package se.idega.idegaweb.commune.presentation;

import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.presentation.*;
import com.idega.builder.data.IBPage;
import java.util.Collection;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
import com.idega.user.business.UserBusiness;
import com.idega.user.business.GroupBusiness;
import com.idega.business.IBOLookup;
import com.idega.data.IDOLookup;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class CitizenChildren extends CommuneBlock {

  private IWBundle iwb;
  private IWResourceBundle iwrb;
  private int userID;
  private int pageID;
  private Text buttonLabel;
  private Text ssnLabel;
  private static final String prmChildId = "comm_child_id";
  private static final String prmChildSSN = "comm_child_ssn";
  private String prmSubmitName = "submit_cits_child";

  public CitizenChildren(){
    buttonLabel = getText("");
    ssnLabel = getText("");
  }

  public void main(IWContext iwc)throws java.rmi.RemoteException{
    iwb  = getBundle(iwc);
    iwrb = getResourceBundle(iwc);
    userID = iwc.getUserId();
    Table T = new Table();
    int row = 1;
    int col = 1;
    if(iwc.isParameterSet(prmSubmitName) && iwc.getParameter(prmSubmitName).equals("true")){
      try{
        User child = processSSNRequest(iwc);
        T.add(getChildLink(child),col,row);
      }
      catch(javax.ejb.FinderException fix){
        T.add(fix.getMessage(),col,row);
      }
      row++;
    }
    T.add(getChildrenForm(iwc));
    add(T);
  }

  public static String getChildIDParameterName(){
    return prmChildId;
  }

  public static String getChildSSNParameterName(){
    return prmChildId;
  }

  public static Parameter getChildIDParameter(int child_id){
    return new Parameter(prmChildId,String.valueOf(child_id));
  }

  public static Parameter getChildSSNParameter(int child_ssn){
    return new Parameter(prmChildSSN,String.valueOf(child_ssn));
  }

  public void setPage(IBPage page){
    this.pageID = page.getID();
  }

  public void setLocalizedButtonLabel(String localeString,String text){
    buttonLabel.setLocalizedText(localeString,text);
  }

  private PresentationObject getChildrenForm(IWContext iwc)throws java.rmi.RemoteException{
    Form f = new Form();
    Table T = new Table();
    int row = 1;
      Collection childs = getChilds(this.userID);
      if(!childs.isEmpty()){
        java.util.Iterator iter = childs.iterator();
        User user;
        RadioButton rad;
        while(iter.hasNext()){
          user = (User) iter.next();
          rad = new RadioButton(prmChildId,((Integer) user.getPrimaryKey()).toString());
          T.add(getChildLink(user),1,row);
          row++;
        }

    }
    TextInput inputSSN = new TextInput(prmChildSSN);
    String label = buttonLabel.getLocalizedText(iwc);

    SubmitButton submit = new SubmitButton(label,prmSubmitName,"true");
    T.add(inputSSN,1,row);
    row++;
    T.add(submit,1,row);

    f.add(T);
    return f;
  }

  private Link getChildLink(User child) throws java.rmi.RemoteException{
    Link L = new Link(child.getName());
    if(pageID>0)
      L.setPage(pageID);
    L.addParameter(prmChildId,((Integer)child.getPrimaryKey()).toString());
    return L;
  }

  private User processSSNRequest(IWContext iwc)throws javax.ejb.FinderException,java.rmi.RemoteException{
    String ssn = iwc.getParameter(prmChildSSN);
    if(ssn!=null && !ssn.equals("")){
      UserHome userHome = (UserHome) IDOLookup.getHome(User.class);
      User child = userHome.findByPersonalID(ssn);
      if(child!=null){
        return child;
      }
    }
    throw new javax.ejb.FinderException("No user with that ssn");
  }

  private Collection getChilds(int userID){
    /** @todo familymethods from usersystem */
    return new java.util.Vector();

  }
}