/*
 * $Id$
 *
 * Copyright (C) 2000-2003 Idega Software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega Software.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.member.presentation;

import is.idega.idegaweb.member.util.IWMemberConstants;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Page;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.ui.util.EventEntry;
import com.idega.presentation.ui.util.EventHistoryList;
import com.idega.user.data.Group;
import com.idega.user.data.GroupRelation;
import com.idega.user.data.User;
import com.idega.user.data.UserStatus;
import com.idega.util.IWTimestamp;

/**
 * @author palli
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class UserHistoryList extends Page {

	private Collection groups = null;

	public UserHistoryList() {
		super();
	}

	public PresentationObject getGroupTable(IWContext iwc) {
		IWResourceBundle comUserBundle = iwc.getIWMainApplication().getBundle("com.idega.user").getResourceBundle(iwc);
		IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle("is.idega.idegaweb.member").getResourceBundle(iwc);
		System.out.println("Getting group table ");
		Collection history = (Collection) iwc.getSessionAttribute(UserHistoryTab.SESSIONADDRESS_USERGROUPS_HISTORY);
		Collection status = (Collection) iwc.getSessionAttribute(UserHistoryTab.SESSIONADDRESS_USERGROUPS_STATUS);

		Table table = null;
		EventHistoryList eventList = new EventHistoryList();
		//eventList.setShowUser(false);
		try {
			Iterator iter = null;
			int row = 1;
			if (history != null || status != null) {
				int size = 0;
				if (history != null)
					size += history.size();
				if (status != null)
					size += status.size();
				table = new Table(4, size + 1);
				table.add(iwrb.getLocalizedString("plugin.history.group","Group"), 1, row);
				table.add(iwrb.getLocalizedString("plugin.history.status","Status"), 2, row);
				table.add(iwrb.getLocalizedString("plugin.history.start_date","Startdate"), 3, row);
				table.add(iwrb.getLocalizedString("plugin.history.end_date","Enddate"), 4, row++);
				String typeAddedToGroup = iwrb.getLocalizedString("plugin.history.type_addedtogroup","Added to group");
				String typeRemovedFromGroup = iwrb.getLocalizedString("plugin.history.type.removed_fromgroup","Removed from group");
				String typeAddedStatus = iwrb.getLocalizedString("plugin.history.status_added","Added status");
				String typeRemovedStatus = iwrb.getLocalizedString("plugin.history.status_removed","Removed status");
				User doneBy = null;
				if (history != null) {
					iter = history.iterator();
					 
					while (iter.hasNext()) {
						Object item = iter.next();
					
						if (item != null) {
							GroupRelation rel = (GroupRelation) item;

							IWTimestamp from = new IWTimestamp(rel.getInitiationDate());
							IWTimestamp to = null;
							if (rel.getTerminationDate() != null)
								to = new IWTimestamp(rel.getTerminationDate());
							doneBy = rel.getCreatedBy();
							eventList.addEvent(new EventListEntry(from.getDate(),typeAddedToGroup,getGroupName(rel.getGroup()),doneBy!=null?doneBy.getName():"",rel.getPrimaryKey().toString()));
							if(to!=null){
							    doneBy = rel.getPassiveBy();
							    eventList.addEvent(new EventListEntry(to.getDate(),typeRemovedFromGroup,getGroupName(rel.getGroup()),doneBy!=null?doneBy.getName():"",rel.getPrimaryKey().toString()));
							}
							
							/*

							table.add(getGroupName(rel.getGroup()), 1, row);
							table.add(df.format(from.getDate()), 3, row);
							if (to != null)
								table.add(df.format(to.getDate()), 4, row);
							row++;
							*/
							doneBy = null;
						}
					}
				}
				table.setLineAfterRow(row-1);

				if (status != null) {
					iter = status.iterator();
					while (iter.hasNext()) {
						Object item = iter.next();
						if (item != null) {
							UserStatus stat = (UserStatus) item;

							IWTimestamp from = new IWTimestamp(stat.getDateFrom());
							IWTimestamp to = null;
							if (stat.getDateTo() != null)
								to = new IWTimestamp(stat.getDateTo());
							doneBy = stat.getCreatedBy();
							eventList.addEvent(new EventListEntry(from.getDate(),typeAddedStatus,getGroupName(stat.getGroup()),doneBy!=null?doneBy.getName():"",comUserBundle.getLocalizedString(stat.getStatus().getStatusKey())));
							if(to!=null)
							    eventList.addEvent(new EventListEntry(to.getDate(),typeRemovedStatus,getGroupName(stat.getGroup()),doneBy!=null?doneBy.getName():"",comUserBundle.getLocalizedString(stat.getStatus().getStatusKey())));
							
							/*
							table.add(getGroupName(stat.getGroup()), 1, row);
							String statusStr = comUserBundle.getLocalizedString(stat.getStatus().getStatusKey());
							table.add((statusStr), 2, row);
							table.add((df.format(from.getDate())), 3, row);
							if (to != null)
								table.add(df.format(to.getDate()), 4, row);
							row++;
							*/
							doneBy = null;
						}
					}
				}

			}
		}
		catch (Exception e) {
			add("Error: " + e.getMessage());
			e.printStackTrace();
		}

		if (table != null) {
			table.setWidth("100%");
		}

		return eventList;
	}

	public void main(IWContext iwc) throws Exception {
		this.getParentPage().setAllMargins(0);
		PresentationObject tb = getGroupTable(iwc);
		if (tb != null) {
			this.add(tb);
		}
	}
	
	/**
	 * Gets a group name, includes division name if group is flock
	 * @param group The group
	 * @return the groups name
	 */ 
	private String getGroupName(Group group) {
		String name = group.getName();
		if(IWMemberConstants.GROUP_TYPE_CLUB_PLAYER.equals(group.getGroupType())) {
			Collection parents = group.getParentGroups();
			Iterator iter = parents.iterator();
			while(iter.hasNext()) {
				Group parent = (Group) iter.next();
				if(IWMemberConstants.GROUP_TYPE_CLUB_DIVISION.equals(parent.getGroupType())) {
					name = parent.getName() + " - " + name;
					break;
				}
			}
		}
		return name;
	}
	
	private class EventListEntry implements EventEntry{
	    
	    private Date date;
	    private String type,source,user,event;
	    
	    public EventListEntry(Date date,String type,String source,String user,String event){
	        this.date = date;
	        this.type = type;
	        this.source = source;
	        this.user = user;
	        this.event = event;
	    }

        /* (non-Javadoc)
         * @see com.idega.presentation.ui.util.EventEntry#getDate()
         */
        public Date getDate() {
           return date;
        }

        /* (non-Javadoc)
         * @see com.idega.presentation.ui.util.EventEntry#getType()
         */
        public String getType() {
           return type;
        }

        /* (non-Javadoc)
         * @see com.idega.presentation.ui.util.EventEntry#getSource()
         */
        public String getSource() {
            return source;
        }

        /* (non-Javadoc)
         * @see com.idega.presentation.ui.util.EventEntry#getUser()
         */
        public String getUser() {
            return user;
        }

        /* (non-Javadoc)
         * @see com.idega.presentation.ui.util.EventEntry#getEvent()
         */
        public String getEvent() {
            return event;
        }
	    
	}
}