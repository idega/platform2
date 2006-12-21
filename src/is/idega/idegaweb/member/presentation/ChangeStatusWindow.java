package is.idega.idegaweb.member.presentation;

import is.idega.idegaweb.member.business.GroupApplicationBusiness;

import java.rmi.RemoteException;
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
      this.myTable = new Table(2,2);
      this.myTable.setWidth("100%");
      this.myTable.setHeight("100%");
      this.myTable.setCellpadding(5);
      this.myTable.setCellspacing(5);


      this.myTable.mergeCells(1,1,2,1);
      this.myTable.add(this.question,1,1);
      this.myTable.add(this.confirm,1,2);
      this.myTable.add(this.close,2,2);
      this.myTable.setAlignment(1,1,"center");
      this.myTable.setAlignment(1,2,"right");
      this.myTable.setAlignment(2,2,"left");

      this.myTable.setVerticalAlignment(1,1,"middle");
      this.myTable.setVerticalAlignment(1,2,"middle");
      this.myTable.setVerticalAlignment(2,2,"middle");

      this.myTable.setHeight(2,"30%");

      this.myForm.add(this.myTable);

    }

    public void setQuestion(Text Question){
      this.question = Question;
    }


    public void initialize(){
      setQuestion(new Text("Change status of application?"));
      this.myForm.maintainParameter(CHANGE_STATUS_PARAM);
      this.myForm.maintainParameter(GROUP_APPLICATION_ID_PARAM);
    }


    public void actionPerformed(IWContext iwc)throws Exception{
      String status = iwc.getParameter(CHANGE_STATUS_PARAM);
        
      if(status != null){
      	int appId = Integer.parseInt(iwc.getParameter(GROUP_APPLICATION_ID_PARAM));
      	
      	GroupApplicationBusiness biz = getGroupApplicationBusiness(iwc);
      	
      	boolean success = biz.changeGroupApplicationStatus(appId, status);
		
		System.out.println("Changed status : "+success);
		
        
      }
    }


    public void main(IWContext iwc) throws Exception {
    		
      this.myForm = new Form();
      this.confirm = new SubmitButton(ConfirmWindow.PARAMETER_CONFIRM,"   Yes   ");
      this.close = new CloseButton("   No    ");
      
      initialize();
      
      
      String confirmThis = iwc.getParameter(ConfirmWindow.PARAMETER_CONFIRM);

      if(confirmThis != null){
      	System.out.println("CONFIRMING");
        this.actionPerformed(iwc);
        this.setParentToReload();
      	this.close();
      } else{
        this.empty();
        if(this.myTable == null){
          lineUpElements();
        }
        this.add(this.myForm);
      }

    }

    public UserBusiness getUserBusiness(IWApplicationContext iwc) throws RemoteException{
    	return  (UserBusiness)com.idega.business.IBOLookup.getServiceInstance(iwc,UserBusiness.class);

    }
    
    public GroupApplicationBusiness getGroupApplicationBusiness(IWApplicationContext iwc) throws RemoteException{
    	return (GroupApplicationBusiness)com.idega.business.IBOLookup.getServiceInstance(iwc,GroupApplicationBusiness.class);
    }


  }