package is.idega.idegaweb.member.presentation;

import is.idega.idegaweb.member.business.GroupApplicationBusiness;
import is.idega.idegaweb.member.data.GroupApplication;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.event.IWPresentationEvent;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.browser.presentation.IWBrowserView;
import com.idega.presentation.IWContext;
import com.idega.presentation.Page;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.user.data.UserGroupPlugIn;
import com.idega.user.presentation.UserPropertyWindow;
import com.idega.util.IWColor;
import com.idega.util.ListUtil;

/**
 * @author <a href="mailto:eiki@idega.is">Eirikur Hrafnsson</a>
 * @version 1.0
 */

public class GroupApplicationOverView extends Page implements IWBrowserView {
	//implements IWBrowserView, StatefullPresentation, UserGroupPlugInPresentable {


  private String _controlTarget = null;
  private IWPresentationEvent _controlEvent = null;
  private IWResourceBundle iwrb = null ;

  //private GroupApplicationOverViewPS _presentationState = null; 
  
  //for debug in Builder
  private Group applicationGroup = null;


  public GroupApplicationOverView(IWContext iwc) throws Exception {
  }
  
  public GroupApplicationOverView(){
    super();
  }


  public void setControlEventModel(IWPresentationEvent model){
    _controlEvent = model;
  }

  public void setControlTarget(String controlTarget){
    _controlTarget = controlTarget;
  }


  public Table getApplications(IWContext iwc, String status) throws Exception{
    this.empty();
    iwrb = this.getResourceBundle(iwc);
    
    Table returnTable = new Table(1,1);
	    returnTable.setCellpaddingAndCellspacing(0);
	    returnTable.setWidth(Table.HUNDRED_PERCENT);
	    returnTable.setHeight(Table.HUNDRED_PERCENT);


    //the event model is not being used in this class when used as a plugin!
    //GroupApplicationOverViewPS ps = (GroupApplicationOverViewPS)this.getPresentationState(iwc);
    Group selectedGroup = applicationGroup;
    /*if( selectedGroup == null ){
    	 selectedGroup= ps.getSelectedGroup();
    }*/
      
    GroupApplicationBusiness gABiz = getGroupApplicationBusiness(iwc);
    String pending = gABiz.getPendingStatusString();
    String approved = gABiz.getApprovedStatusString();
    String denied = gABiz.getDeniedStatusString();
  
    
    int userCount = 0;
    if(selectedGroup  != null){
    	Collection applications = gABiz.getGroupApplicationsByStatusAndApplicationGroup(pending,selectedGroup);
    	
	    Table userTable = null;
	    
	
	      applications = ListUtil.convertCollectionToList(applications);
	
				int size =applications.size();
	      userTable = new Table(7, ((size>33)?size:33)+1  );
	      returnTable.add(userTable,1,1);
	      returnTable.setVerticalAlignment(1,1,Table.VERTICAL_ALIGN_TOP);
	      userTable.setCellpaddingAndCellspacing(0);
	      userTable.setLineAfterColumn(1);
	      userTable.setLineAfterColumn(2);
	      userTable.setLineAfterColumn(3);
	      userTable.setLineAfterColumn(4);
	      userTable.setLineAfterColumn(5);

	      userTable.setLineColor("#DBDCDF");
	
	      userTable.setBackgroundImage(1,1,this.getBundle(iwc).getImage("glass_column_light.gif"));
	      userTable.setBackgroundImage(2,1,this.getBundle(iwc).getImage("glass_column_light.gif"));
	      userTable.setBackgroundImage(3,1,this.getBundle(iwc).getImage("glass_column_light.gif"));
	      userTable.setBackgroundImage(4,1,this.getBundle(iwc).getImage("glass_column_light.gif"));
	      userTable.setBackgroundImage(5,1,this.getBundle(iwc).getImage("glass_column_light.gif"));
	      userTable.setBackgroundImage(6,1,this.getBundle(iwc).getImage("glass_column_light.gif"));
	      userTable.setBackgroundImage(7,1,this.getBundle(iwc).getImage("glass_column_light.gif"));
	            
	      userTable.setHeight(1,16);
	
	      userTable.setWidth(1,"160");
	      
	      
	    //columns start
	
	    Text name = new Text("&nbsp;"+iwrb.getLocalizedString("name","Name"));
	 	  name.setFontFace(Text.FONT_FACE_VERDANA);
	 	  name.setFontSize(Text.FONT_SIZE_7_HTML_1);
	 	  userTable.add(name,1,1);
	
	 	  Text pin = new Text("&nbsp;"+iwrb.getLocalizedString("personal.id.number","Pin"));
	 	  pin.setFontFace(Text.FONT_FACE_VERDANA);
	 	  pin.setFontSize(Text.FONT_SIZE_7_HTML_1);
	 	  userTable.add(pin,2,1);
	
	 	  Text email = new Text("&nbsp;"+iwrb.getLocalizedString("email","Email"));
	 	  email.setFontFace(Text.FONT_FACE_VERDANA);
	 	  email.setFontSize(Text.FONT_SIZE_7_HTML_1);
	 	  userTable.add(email,3,1);
	 	  
	 	  Text phone = new Text("&nbsp;"+iwrb.getLocalizedString("phone","Phone"));
	 	  phone.setFontFace(Text.FONT_FACE_VERDANA);
	 	  phone.setFontSize(Text.FONT_SIZE_7_HTML_1);
	 	  userTable.add(phone,4,1);
	
	 	  Text groups = new Text("&nbsp;"+iwrb.getLocalizedString("groups","Groups"));
	 	  groups.setFontFace(Text.FONT_FACE_VERDANA);
	 	  groups.setFontSize(Text.FONT_SIZE_7_HTML_1);
	 	  userTable.add(groups,5,1);
	
	 	  //approve button header , no need?
	 	  //userTable.add(del,3,1);
	 	  
	 	  //deny button , no need?
	 	  //userTable.add(del,3,1);
	 	   	  
	 	  
	 	  ///columns end
	
	      userTable.setCellspacing(0);
	      userTable.setHorizontalZebraColored("#FFFFFF",IWColor.getHexColorString(246,246,247));
	      userTable.setWidth("100%");
	      for (int i = 1; i <= userTable.getRows() ; i++) {
	        userTable.setHeight(i,"20");
	      }
	
	
	      int line = 2;
	      Iterator iter = applications.iterator();
	      while (iter.hasNext()) {
	      	
	      	GroupApplication app = (GroupApplication)iter.next();
	        User tempUser = app.getUser();
	        
	
	
	        if(tempUser != null){
	
	          boolean userIsSuperAdmin = iwc.getAccessController().getAdministratorUser().equals(tempUser);
	          
	          //name
	          Link aLink = new Link(new Text(tempUser.getName()));
	          aLink.setWindowToOpen(UserPropertyWindow.class);
	          aLink.addParameter(UserPropertyWindow.PARAMETERSTRING_USER_ID, tempUser.getPrimaryKey().toString());
	          userTable.add("&nbsp;",1,line);
	          userTable.add(aLink,1,line);
	          
						//pin
	          userTable.add("&nbsp;"+tempUser.getPersonalID(),2,line);
	          
	          //email
	          Collection emails = tempUser.getEmails();
	          if( emails!=null && !emails.isEmpty() ){
	          	Iterator iterator = emails.iterator();
	          	
	          	while (iterator.hasNext()) {
								Email e_mail = (Email) iterator.next();
								userTable.add("&nbsp;"+e_mail.getEmailAddress() ,3,line);
							}
	          	
	          }

	          
	          //phone
	          Collection phones = tempUser.getPhones();
	          if( phones!=null && !phones.isEmpty() ){
	          	Iterator iterator = phones.iterator();
	          	
	          	while (iterator.hasNext()) {
								Phone _phone = (Phone) iterator.next();
								userTable.add("&nbsp;"+_phone.getNumber() ,4,line);
						}
	          	
	          }
	          
	          //groups
	          
	          //ER EKKI AD FA PARAMETERINN
	          //VERDUR AD GERA FIND FOLL FYRIR EMAIL,PHONE TIL AD DUPPLIST EKKI
	          Collection appGroups = app.getGroups();
	          
	          if( appGroups!=null && !appGroups.isEmpty() ){
	          	Iterator iterator = appGroups.iterator();
	          	
	          	while (iterator.hasNext()) {
					Group appGroup = (Group) iterator.next();
					userTable.add("&nbsp;"+appGroup.getName() ,5,line);
				}
	          	
	          }
	          
	
	          
	          //buttons
	         // if( userIsSuperAdmin && iwc.getAccessController().isAdmin(iwc)){
	            Link approve = new Link(new Text("Approve"));
	            approve.setWindowToOpen(ChangeStatusWindow.class);
	            //geyma i session?
	            approve.addParameter(ChangeStatusWindow.GROUP_APPLICATION_ID_PARAM ,((Integer) app.getPrimaryKey()).intValue());
	            approve.addParameter(ChangeStatusWindow.CHANGE_STATUS_PARAM , approved);
	            approve.setAsImageButton(true);
	            userTable.add("&nbsp;",6,line);
	            userTable.add(approve,6,line);
	      
	      
	            Link deny = new Link(new Text("Deny"));
	            deny.setWindowToOpen(ChangeStatusWindow.class);
	            deny.addParameter(ChangeStatusWindow.GROUP_APPLICATION_ID_PARAM ,((Integer) app.getPrimaryKey()).intValue());
	            deny.addParameter(ChangeStatusWindow.CHANGE_STATUS_PARAM , denied);
	            deny.setAsImageButton(true);
	            userTable.add("&nbsp;",7,line);
	            userTable.add(deny,7,line);
	          
	        //  }
	
	          line++;
	
	          
	        }//end if
	      }//end while
    }
    return returnTable;	          
 }





  public void main(IWContext iwc) throws Exception {

    this.empty();
    //add for other states
    //and use parameter from GroupApplicationHome
    //
    //
    this.add(getApplications(iwc, "pending"));
    this.getParentPage().setAllMargins(0);

  }

/**
 * For debug only
 * 
 * */
	public void setApplicationGroup(Group appGroup){
		this.applicationGroup = appGroup;
	}
/*
  public IWPresentationState getPresentationState(IWUserContext iwuc){
    if(_presentationState == null){
      try {
        IWStateMachine stateMachine = (IWStateMachine)IBOLookup.getSessionInstance(iwuc,IWStateMachine.class);
        _presentationState = (GroupApplicationOverViewPS)stateMachine.getStateFor(this.getLocation(),this.getPresentationStateClass());
      }
      catch (RemoteException re) {
        throw new RuntimeException(re.getMessage());
      }
    }
    return _presentationState;
  }

  public Class getPresentationStateClass(){
    return GroupApplicationOverViewPS.class;
  }
*/
  public String getBundleIdentifier(){
  	return "com.idega.user";
  }
 
    public GroupApplicationBusiness getGroupApplicationBusiness(IWApplicationContext iwc) throws RemoteException{
    	return (GroupApplicationBusiness)com.idega.business.IBOLookup.getServiceInstance(iwc,GroupApplicationBusiness.class);
    }

	/**
	 * @see com.idega.user.presentation.UserGroupPlugInPresentable#getPlugIn()
	 */
	public UserGroupPlugIn getPlugIn() {
		return null;
	}

	/**
	 * @see com.idega.user.presentation.UserGroupPlugInPresentable#initialize(Group)
	 */
	public void initialize(Group group) {
		this.setApplicationGroup(group);
	}

} //Class end

  
