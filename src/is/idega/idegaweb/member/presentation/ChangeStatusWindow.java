package is.idega.idegaweb.member.presentation;

import is.idega.idegaweb.member.business.GroupApplicationBusiness;
import is.idega.idegaweb.member.data.GroupApplicationHome;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Vector;

import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.ConfirmWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.Window;
import com.idega.user.business.UserBusiness;


 
  public class ChangeStatusWindow extends Window{

    public Text question;
    public Form myForm;

    public SubmitButton confirm;
    public CloseButton close;
    public Table myTable = null;

    public static final String PARAMETER_CONFIRM = "confirm";
    
 	public static final String CHANGE_STATUS_PARAM = "iw_me_c_st";
 	public static final String GROUP_APPLICATION_ID_PARAM = "iw_me_c_gap";

    public Vector parameters;

    public ChangeStatusWindow(){
      super("Change application status",300,130);
      //super.setBackgroundColor("#d4d0c8");
      super.setScrollbar(false);
      super.setAllMargins(0);
    }
 

    public void lineUpElements(){
      myTable = new Table(2,2);
      myTable.setWidth("100%");
      myTable.setHeight("100%");
      myTable.setCellpadding(5);
      myTable.setCellspacing(5);


      myTable.mergeCells(1,1,2,1);
      myTable.add(question,1,1);
      myTable.add(confirm,1,2);
      myTable.add(close,2,2);
      myTable.setAlignment(1,1,"center");
      myTable.setAlignment(1,2,"right");
      myTable.setAlignment(2,2,"left");

      myTable.setVerticalAlignment(1,1,"middle");
      myTable.setVerticalAlignment(1,2,"middle");
      myTable.setVerticalAlignment(2,2,"middle");

      myTable.setHeight(2,"30%");

      myForm.add(myTable);

    }

    public void setQuestion(Text Question){
      question = Question;
    }


    public void initialize(){
      setQuestion(new Text("Change status of application?"));
      myForm.maintainParameter(CHANGE_STATUS_PARAM);
      myForm.maintainParameter(GROUP_APPLICATION_ID_PARAM);
    }


    public void actionPerformed(IWContext iwc)throws Exception{
      String status = iwc.getParameter(CHANGE_STATUS_PARAM);
        
      if(status != null){
      	int appId = Integer.parseInt(iwc.getParameter(GROUP_APPLICATION_ID_PARAM));
      	
      	GroupApplicationBusiness biz = getGroupApplicationBusiness(iwc);
      	
      	boolean success = biz.changeGroupApplicationStatus(appId, status);
		
		if(!success) add("Change status failed!");
		
        
      }
    }


    public void main(IWContext iwc) throws Exception {
    		
      myForm = new Form();
      confirm = new SubmitButton(ConfirmWindow.PARAMETER_CONFIRM,"   Yes   ");
      close = new CloseButton("   No    ");
      
      initialize();
      
      
      String confirmThis = iwc.getParameter(ConfirmWindow.PARAMETER_CONFIRM);

      if(confirmThis != null){
        this.actionPerformed(iwc);
        this.setParentToReload();
        this.close();
      } else{
        this.empty();
        if(myTable == null){
          lineUpElements();
        }
        this.add(myForm);
      }

    }

    public UserBusiness getUserBusiness(IWApplicationContext iwc) throws RemoteException{
    	return  (UserBusiness)com.idega.business.IBOLookup.getServiceInstance(iwc,UserBusiness.class);

    }
    
    public GroupApplicationBusiness getGroupApplicationBusiness(IWApplicationContext iwc) throws RemoteException{
    	return (GroupApplicationBusiness)com.idega.business.IBOLookup.getServiceInstance(iwc,GroupApplicationBusiness.class);
    }


  }