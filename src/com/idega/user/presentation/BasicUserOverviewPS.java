package com.idega.user.presentation;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.ejb.FinderException;
import javax.swing.event.ChangeEvent;
import com.idega.block.entity.event.EntityBrowserEvent;
import com.idega.core.builder.data.ICDomain;
import com.idega.core.contact.data.Email;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.event.IWActionListener;
import com.idega.event.IWPresentationEvent;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWException;
import com.idega.idegaweb.browser.presentation.IWControlFramePresentationState;
import com.idega.presentation.IWContext;
import com.idega.presentation.event.ResetPresentationEvent;
import com.idega.user.block.search.event.UserSearchEvent;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.data.User;
import com.idega.user.event.SelectDomainEvent;
import com.idega.user.event.SelectGroupEvent;

/**
 * <p>
 * Title: idegaWeb
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: idega Software
 * </p>
 * 
 * @author <a href="gummi@idega.is">Gu�mundur �g�st S�mundsson </a>
 * @version 1.0
 */

public class BasicUserOverviewPS extends IWControlFramePresentationState
        implements IWActionListener {

    //  String color1 = "00FF00";
    //  String color2 = "FF0000";
    //  String color = color1;
	
	private GroupBusiness business = null;

    protected Group parentGroupOfSelection = null;

    protected ICDomain parentDomainOfSelection = null;

    protected Group _selectedGroup = null;

    protected ICDomain _selectedDomain = null;

    protected boolean showSearchResult = false;

    private Map resultOfMovingUsers = null;

    private int targetGroupId;

    public BasicUserOverviewPS() {
    }

    public Map getResultOfMovingUsers() {
        return this.resultOfMovingUsers;
    }

    public boolean showSearchResult() {
        return this.showSearchResult;
    }

    public int getTargetGroupId() {
        return this.targetGroupId;
    }

    public Group getSelectedGroup() {
        return this._selectedGroup;
    }

    public ICDomain getSelectedDomain() {
        return this._selectedDomain;
    }

    public void reset() {
        super.reset();
        this._selectedGroup = null;
        this._selectedDomain = null;
        this.resultOfMovingUsers = null;

    }

    //  public String getColor(){
    //    return color;
    //  }

    public void actionPerformed(IWPresentationEvent e) throws IWException {



        try {
			if (e instanceof ResetPresentationEvent) {
			    this.reset();
			    clearSendMail(e.getIWContext());
			    this.fireStateChanged();
			    
			}
			
			if (e instanceof UserSearchEvent) {
			    this._selectedGroup = null;
			    this.resultOfMovingUsers = null;
			    clearSendMail(e.getIWContext());
			} 
			

			if (e instanceof SelectGroupEvent) {
			    this._selectedGroup = ((SelectGroupEvent) e).getSelectedGroup();
			    this._selectedDomain = null;
			    this.parentGroupOfSelection = ((SelectGroupEvent) e).getParentGroupOfSelection();
			    this.parentDomainOfSelection = ((SelectGroupEvent) e).getParentDomainOfSelection();
			    this.resultOfMovingUsers = null;
			    this.showSearchResult = false;
			    clearSendMail(e.getIWContext());
			    this.fireStateChanged();
			}

			if (e instanceof SelectDomainEvent) {
			    this._selectedDomain = ((SelectDomainEvent) e).getSelectedDomain();
			    this._selectedGroup = null;
			    this.resultOfMovingUsers = null;
			    this.showSearchResult = false;
			    clearSendMail(e.getIWContext());
			    this.fireStateChanged();
			}

			if (e instanceof EntityBrowserEvent) {
			    IWContext mainIwc = e.getIWContext();
			    String[] userIds;
			    if (mainIwc.isParameterSet(BasicUserOverview.EMAIL_USERS_KEY) && mainIwc.isParameterSet(BasicUserOverview.SELECTED_USERS_KEY)) {
			    	
			    	
			    	userIds = mainIwc.getParameterValues(BasicUserOverview.SELECTED_USERS_KEY);
			        // email users (if something has been chosen)
			        String toAddresses = "";
		        	for (int i=0; i < userIds.length; i++) {
		        		String userID = userIds[i];
		        		try {
		        			User user = getGroupBusiness(mainIwc).getUserByID(Integer.parseInt(userID));
		        			Collection emails = user.getEmails();
		        			if (emails != null && !emails.isEmpty()) {
		        				if (!toAddresses.equals("")) {
				        			toAddresses = toAddresses + ";";
				        		}
		        				toAddresses = toAddresses + ((Email) emails.iterator().next()).getEmailAddress();
		        			}
		        		}
		        		catch (Exception ex) {
		        			ex.printStackTrace();
		        		}
		        	}
		        	mainIwc.setSessionAttribute(BasicUserOverviewEmailSenderWindow.PARAM_TO_ADDRESS, toAddresses);
		        	mainIwc.setSessionAttribute(BasicUserOverview.OPEN_SEND_MAIL_WINDOW, "true");
		        	
			    } else {
			    	clearSendMail(mainIwc);
			    }
			}
			
			if (e instanceof EntityBrowserEvent) {
			    IWContext mainIwc = e.getIWContext();
			    String[] userIds;
			    if (mainIwc.isParameterSet(BasicUserOverview.DELETE_USERS_KEY) && mainIwc.isParameterSet(BasicUserOverview.SELECTED_USERS_KEY)) {
			        userIds = mainIwc.getParameterValues(BasicUserOverview.SELECTED_USERS_KEY);
			        // delete users (if something has been chosen)

			        if (this._selectedGroup.isAlias()) {
			            BasicUserOverview.removeUsers(Arrays.asList(userIds),this._selectedGroup.getAlias(), mainIwc);
			        } else {
			            BasicUserOverview.removeUsers(Arrays.asList(userIds),this._selectedGroup, mainIwc);
			        }

			    }
			}
			
			if (e instanceof EntityBrowserEvent) {
			    IWContext mainIwc = e.getIWContext();
			    String[] userIds;
			    if ((mainIwc.isParameterSet(BasicUserOverview.MOVE_USERS_KEY) || mainIwc.isParameterSet(BasicUserOverview.COPY_USERS_KEY)) && mainIwc.isParameterSet(BasicUserOverview.SELECTED_USERS_KEY) && mainIwc.isParameterSet(BasicUserOverview.SELECTED_TARGET_GROUP_KEY)) {
			        userIds = mainIwc.getParameterValues(BasicUserOverview.SELECTED_USERS_KEY);

			        String targetGroupNodeString = mainIwc.getParameter(BasicUserOverview.SELECTED_TARGET_GROUP_KEY);
			        //cut it down because it is in the form "domain_id"_"group_id"
			        targetGroupNodeString = targetGroupNodeString.substring(Math.max(targetGroupNodeString.indexOf("_") + 1, 0),targetGroupNodeString.length());
			        int targetGroupId = Integer.parseInt(targetGroupNodeString);
			        
			        this.business = getGroupBusiness(mainIwc);
			        
			        try {
			        	 //move to the real group not the alias!
						Group target = this.business.getGroupByGroupID(targetGroupId);
						if(target.isAlias()){
							targetGroupId = target.getAliasID();
						}
				        // move users to a group
				        if (this._selectedGroup!=null && this._selectedGroup.isAlias()) {
				            this.resultOfMovingUsers = BasicUserOverview.moveUsers(Arrays.asList(userIds), this._selectedGroup.getAlias(),targetGroupId, mainIwc);
				        } else if (mainIwc.isParameterSet(BasicUserOverview.COPY_USERS_KEY)) {
				            this.resultOfMovingUsers = BasicUserOverview.moveUsers(Arrays.asList(userIds), this._selectedGroup.getAlias(),targetGroupId, mainIwc, true);
				        } else {
				            this.resultOfMovingUsers = BasicUserOverview.moveUsers(Arrays.asList(userIds), this._selectedGroup, targetGroupId,mainIwc);
				        }
				       
				        this.targetGroupId = targetGroupId;
			        
			        }
					catch (FinderException e2) {
						e2.printStackTrace();
					}
			    }
			}

			if (e instanceof EntityBrowserEvent && (MassMovingWindow.EVENT_NAME.equals(((EntityBrowserEvent) e).getEventName()))) {
			    IWContext mainIwc = e.getIWContext();
			    String[] groupIds;
			    if (mainIwc.isParameterSet(MassMovingWindow.SELECTED_CHECKED_GROUPS_KEY) && mainIwc.isParameterSet(MassMovingWindow.MOVE_SELECTED_GROUPS)) {
			        groupIds = mainIwc.getParameterValues(MassMovingWindow.SELECTED_CHECKED_GROUPS_KEY);
			        String parentGroupType = mainIwc.getParameter(MassMovingWindow.PRM_PARENT_GROUP_TYPE);
			        
			        try {
						GroupHome grHome = (GroupHome)IDOLookup.getHome(Group.class);
						Collection groupCollection = grHome.findByPrimaryKeyCollection(grHome.decode(groupIds));
						Collection groupTypes = Collections.singleton(MassMovingWindow.GROUP_TYPE_CLUB_PLAYER);
						
						// move users
						if(parentGroupType.equals(MassMovingWindow.GROUP_TYPE_CLUB_DIVISION)){
							this.resultOfMovingUsers = BasicUserOverview.moveContentOfGroups(groupCollection,groupTypes, mainIwc);
						} else {
							this.resultOfMovingUsers = new HashMap();
							for (Iterator iter = groupCollection.iterator(); iter.hasNext();) {
								Group divGroups = (Group) iter.next();
								this.resultOfMovingUsers.putAll(BasicUserOverview.moveContentOfGroups(Collections.singleton(divGroups),groupTypes, mainIwc));
							}
							
						}
					} catch (IDOLookupException e1) {
						e1.printStackTrace();
					} catch (FinderException e1) {
						e1.printStackTrace();
					}
			        this.targetGroupId = -1;
			        fireStateChanged();
			    }
			}
		}
		catch (RemoteException e1) {
			//something really bad happened
			e1.printStackTrace();
		}
    }

	private void clearSendMail(IWContext iwc) {
		iwc.setSessionAttribute(BasicUserOverview.OPEN_SEND_MAIL_WINDOW, null);
		iwc.setSessionAttribute("MAILTO","");
	}

    /**
     * Returns the parentDomainOfSelection.
     * 
     * @return IBDomain
     */
    public ICDomain getParentDomainOfSelection() {
        return this.parentDomainOfSelection;
    }

    /**
     * Returns the parentGroupOfSelection.
     * 
     * @return Group
     */
    public Group getParentGroupOfSelection() {
        return this.parentGroupOfSelection;
    }

    public void stateChanged(ChangeEvent e) {
        Object object = e.getSource();
        if (object instanceof DeleteGroupConfirmWindowPS) {
            // selected group was successfully(!) removed
            // set selected group to null
            this._selectedGroup = null;
        }
    }
    
    public GroupBusiness getGroupBusiness(IWApplicationContext iwc) {
        if (this.business == null) {
            try {
                this.business = (GroupBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, GroupBusiness.class);
            }
            catch (java.rmi.RemoteException rme) {
                throw new RuntimeException(rme.getMessage());
            }
        }
        return this.business;
    }

}