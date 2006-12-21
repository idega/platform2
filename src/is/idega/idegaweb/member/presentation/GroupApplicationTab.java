package is.idega.idegaweb.member.presentation;

import is.idega.idegaweb.member.business.GroupApplicationBusiness;
import is.idega.idegaweb.member.data.GroupApplication;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import com.idega.business.IBOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.TextArea;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.user.presentation.GroupSelectionDoubleBox;
import com.idega.user.presentation.UserTab;
import com.idega.util.Timer;


/**
 * Title:        
 * Description:
 * Copyright: Idega Software (c) 2002
 * Company: Idega Software
 * @author <a href="mailto:eiki@idega.is">Eirikur Hrafnsson</a>
 * @version 1.0
 */

public class GroupApplicationTab extends UserTab {

    private GroupApplication app;

	private User user;
	private Table frameTable;
	private GroupSelectionDoubleBox groupSelection;
	
	private GroupBusiness groupBiz;
	private GroupApplicationBusiness appBiz;
	private static final String SELECTED_GROUPS_PARAM = GroupSelectionDoubleBox.selectedGroupsParameterDefaultValue;
	private static final String ADMIN_COMMENT_PARAM = "iwme_admin_comment";
	
	
	


  public GroupApplicationTab() {
    super();
    super.setName("Applications");
  }
  
  public GroupApplicationTab(User user) {
    this();
    this.user = user;
  }

	public void init() {
	}
  
	public void updateFieldsDisplayStatus() {
	}
  
	public void initializeFields() {
	}

	public void initializeFieldNames() {
	}

	public void initializeFieldValues() {
	}

	public void initializeTexts() {
	}

	public void lineUpFields() {
	}

	public boolean collect(IWContext iwc) {
		initFieldContents();
		return true;
	}

  public boolean store(IWContext iwc) { 
  		
  	String adminComment = iwc.getParameter(ADMIN_COMMENT_PARAM);
  	
  	String[] groupIds = iwc.getParameterValues(SELECTED_GROUPS_PARAM);
  	if( this.app!=null ){
  	
	  	try {
			return getGroupApplicationBusiness().changeGroupApplicationAdminCommentAndGroups(this.app,adminComment,groupIds) ;
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}
	}
  	
  	return true;
  }
  
  public void initFieldContents() {
  	this.empty();
  	
  	try{
	  	this.groupBiz = getGroupBusiness();
	  	this.appBiz = getGroupApplicationBusiness();
	  	this.groupSelection = new GroupSelectionDoubleBox();
	  	if( this.user == null ) {
			this.user = getUser();
		}
	  	
	  	
	
		Collection apps = this.appBiz.getGroupApplicationsByStatusAndUserOrderedByCreationDate(this.appBiz.getPendingStatusString(),this.user);
		
		if( apps!=null && !apps.isEmpty()){
			Iterator iter = apps.iterator();
			int counter = 1;
			while (iter.hasNext()) {
				if(counter>1){
					addBreak();
					add("User has more applications pending. Only one can be viewed at a time.");	
					break;
				}
				this.app = (GroupApplication) iter.next();
				add("Status. "+this.app.getStatus());
				addBreak();
				
				Timer time1 = new Timer();

				Timer time2 = new Timer();
				
				
				time1.start();
				Group parent = (Group)(this.groupBiz.getGroupByGroupID(this.app.getApplicationGroupId()).getParentNode());
				time1.stop();
				
				System.out.println("GroupApplicationTab: time to complete getParentNode() : "+time1.getTime()/1000+"s");
				if(parent!=null){
				
					add("parent : "+parent.getName());
					
					time2.start();
					Collection allGroups = this.groupBiz.getChildGroupsRecursive( parent  );
					
					time2.stop();
					System.out.println("GroupApplicationTab: time to complete getChildGroupsRecursive( parent  ) : "+time1.getTime()/1000+"s");
				
					
					Iterator aGroups = allGroups.iterator();
					while (aGroups.hasNext()) {
						Group group = (Group) aGroups.next();
						this.groupSelection.addToAvailableBox(((Integer)group.getPrimaryKey()).toString(), group.getName());
					}
					
					Collection selectedGroups = this.app.getGroups();
					Iterator sGroups = selectedGroups.iterator();
					while (sGroups.hasNext()) {
						Group sGroup = (Group) sGroups.next();
						this.groupSelection.addToSelectedBox(((Integer)sGroup.getPrimaryKey()).toString(), sGroup.getName());	
					}
					
					add( this.groupSelection );	
				}
				
				
				//add(new HiddenInput(
				String userComment = this.app.getUserComment();
				if( userComment!=null ){ 
					add( "User comment: "+userComment); 
				}
				addBreak();
				String adminComment = this.app.getAdminComment();
				
				TextArea comment = new TextArea(ADMIN_COMMENT_PARAM);
				comment.setColumns(32);
				comment.setRows(10);
				
				if( adminComment!=null){
					comment.setContent(adminComment);	
				}
				add("Admin comment:");
				addBreak();
				add(comment);
				counter++;
				
			}
	
				
		}
		
	  	//find fall til ad finna applications
	  	//find fall til ad finna alla hopa undir parenti umsoknarhopsins
	
		//hopavalid
		//user commentid
		//admin commentid
		
	  	
	  	//appBiz.getGroupApplicationHome().findAllApplications();//by user
	  	
	   
	    
	    this.frameTable = new Table(1,1);
	    this.frameTable.setCellpadding(0);
	    this.frameTable.setCellspacing(0);
	    
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

	/**
	 * @see com.idega.presentation.PresentationObject#getBundleIdentifier()
	 */
	public String getBundleIdentifier() {
		return "is.idega.idegaweb.member";
	}
}