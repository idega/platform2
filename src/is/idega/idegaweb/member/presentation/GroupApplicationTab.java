package is.idega.idegaweb.member.presentation;

import is.idega.idegaweb.member.business.GroupApplicationBusiness;
import is.idega.idegaweb.member.data.GroupApplication;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import com.idega.business.IBOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.Text;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.user.presentation.GroupSelectionDoubleBox;
import com.idega.user.presentation.UserTab;


/**
 * Title:        
 * Description:
 * Copyright: Idega Software (c) 2002
 * Company: Idega Software
 * @author <a href="mailto:eiki@idega.is">Eirikur Hrafnsson</a>
 * @version 1.0
 */

public class GroupApplicationTab extends UserTab {

    private Text spouseText;
    private Text childrenText;
    private Text custodiansText;
    private Text siblingsText;

	private User user;
	private Table frameTable;
	private GroupSelectionDoubleBox groupSelection;
	
	private UserBusiness userBiz;
	private GroupBusiness groupBiz;
	private GroupApplicationBusiness appBiz;
	private static final String SELECTED_GROUPS = GroupSelectionDoubleBox.selectedGroupsParameterDefaultValue;
	
	
	


  public GroupApplicationTab() {
    super();
    super.setName("Applications");
  }
  
  public GroupApplicationTab(User user) {
    this();
    this.user = user;
  }

  public void init(){}
  public void updateFieldsDisplayStatus() {}
  public void initializeFields() {}
  public void initializeFieldNames() {}
  public void initializeFieldValues() {}
  public void initializeTexts() {}
  public void lineUpFields() {}
  
  
  public boolean collect(IWContext iwc) { initFieldContents(); return true; }
  public boolean store(IWContext iwc) { 
  	
  	String[] groupsIds = iwc.getParameterValues(SELECTED_GROUPS);
  	
  	if(groupsIds!=null){
	  	for (int i = 0; i < groupsIds.length; i++) {
			String string = groupsIds[i];
			System.out.println("Group id: "+string);
					
		}
  	}
  	
  		
  	return true; 
  }
  
  public void initFieldContents() {
  	this.empty();
  	
  	try{
   
	  	userBiz = getUserBusiness();
	  	groupBiz = getGroupBusiness();
	  	appBiz = getGroupApplicationBusiness();
	  	groupSelection = new GroupSelectionDoubleBox();
	  	if( user == null ) user = userBiz.getUser(this.getUserId());
	  	
	  	
	
		Collection apps = appBiz.getGroupApplicationsByStatusAndUserOrderedByCreationDate(appBiz.getPendingStatusString(),user);
		
		if( apps!=null && !apps.isEmpty()){
			Iterator iter = apps.iterator();
			while (iter.hasNext()) {
				GroupApplication app = (GroupApplication) iter.next();
				add("Status. "+app.getStatus());
				addBreak();
				
				Group parent = (Group)(groupBiz.getGroupByGroupID(app.getApplicationGroupId()).getParentNode());
				add("parent : "+parent.getName());
				
				Collection allGroups = groupBiz.getGroupsContained( parent  );
				Iterator aGroups = allGroups.iterator();
				while (aGroups.hasNext()) {
					Group group = (Group) aGroups.next();
					groupSelection.addToAvailableBox(((Integer)group.getPrimaryKey()).toString(), group.getName());
				}
				
				Collection selectedGroups = app.getGroups();
				Iterator sGroups = selectedGroups.iterator();
				while (sGroups.hasNext()) {
					Group sGroup = (Group) sGroups.next();
					groupSelection.addToSelectedBox(((Integer)sGroup.getPrimaryKey()).toString(), sGroup.getName());	
				}
				
				add( groupSelection );	
				
				//add(new HiddenInput(
				add( "User comment: "+app.getUserComment());
				addBreak();
				
				add("Admin comment: "+app.getAdminComment());
				
			}
	
				
		}
		
	  	//find fall til ad finna applications
	  	//find fall til ad finna alla hopa undir parenti umsoknarhopsins
	
		//hopavalid
		//user commentid
		//admin commentid
		
	  	
	  	//appBiz.getGroupApplicationHome().findAllApplications();//by user
	  	
	   
	    
	    frameTable = new Table(1,1);
	    frameTable.setCellpadding(0);
	    frameTable.setCellspacing(0);
	    
	   // getGroupApplicationBusiness(this.getIWApplicationContext()).
	    
	    //frameTable.add(groupSelection);
	    //this.add(frameTable);
	  	
  	}
  	catch ( Exception e ){
  		e.printStackTrace();	
  	}

  }


  
	private GroupApplicationBusiness getGroupApplicationBusiness() throws RemoteException {
		return (GroupApplicationBusiness) IBOLookup.getServiceInstance(this.getIWApplicationContext(), GroupApplicationBusiness.class);	
	}

	private GroupBusiness getGroupBusiness() throws RemoteException {
		return (GroupBusiness) IBOLookup.getServiceInstance(this.getIWApplicationContext(), GroupBusiness.class);	
	}

	private UserBusiness getUserBusiness() throws RemoteException {
		return (UserBusiness) IBOLookup.getServiceInstance(this.getIWApplicationContext(), UserBusiness.class);	
	}
		
	/**
	 * @see com.idega.presentation.PresentationObject#getBundleIdentifier()
	 */
	public String getBundleIdentifier() {
		return "is.idega.idegaweb.member";
	}
}