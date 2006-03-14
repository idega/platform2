/*
 * $Id: SchoolUserChooserWindow.java,v 1.3.2.2 2006/03/14 10:01:14 mariso Exp $ Created on
 * 24.2.2005
 * 
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to
 * license terms.
 */
package com.idega.block.school.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.FinderException;
import javax.faces.context.FacesContext;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.business.SchoolUserBusiness;
import com.idega.block.school.business.SchoolUserBusinessBean;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolHome;
import com.idega.block.school.data.SchoolUser;
import com.idega.block.school.data.SchoolUserHome;
import com.idega.builder.business.BuilderConstants;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWMainApplicationSettings;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
import com.idega.user.presentation.UserChooserWindow;

/**
 * Last modified: 24.2.2005 15:06:52 by: anna
 * 
 * @author <a href="mailto:anna@idega.com">anna </a>
 * @version $Revision: 1.3.2.2 $
 */
public class SchoolUserChooserWindow extends UserChooserWindow {

	private SchoolBusiness getSchoolBusiness(IWContext iwc) {
		try {
			return (SchoolBusiness) IBOLookup.getServiceInstance(iwc, SchoolBusiness.class);
		}
		catch (IBOLookupException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public School getProvider(IWContext iwc) {
		School _provider = null;
		try {
			if (iwc.isParameterSet(SchoolUserChooser.PARAMETER_SCHOOL_ID)) {
				_provider = getSchoolBusiness(iwc).getSchool(iwc.getParameter(SchoolUserChooser.PARAMETER_SCHOOL_ID));
				if (iwc.getSessionAttribute(SchoolUserChooser.PARAMETER_SCHOOL_ID) == null) {
					iwc.setSessionAttribute(SchoolUserChooser.PARAMETER_SCHOOL_ID, _provider);
				}
			} else {
				_provider = (School) iwc.getSessionAttribute(SchoolUserChooser.PARAMETER_SCHOOL_ID);
			}
		}
		catch (RemoteException ex) {
			ex.printStackTrace();
		}
		return _provider;
	}

	private SchoolUserBusiness getSchoolUserBusiness(IWContext iwc) {
		try {
			return (SchoolUserBusiness) IBOLookup.getServiceInstance(iwc, SchoolUserBusiness.class);
		}
		catch (IBOLookupException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
    private int USERS_PER_PAGE=25;
    
    private Text getTitleText(String content)
    {
        Text text = new Text(content);
        text.setFontStyle("font-family:Verdana,Arial,Helvetica,sans-serif;font-size:9pt;font-weight:bold;color:#FFFFFF;");
        return text;
    }

    private String localize(String key, String nullValue)
    {
        return iwrb.getLocalizedString(key, nullValue);
    }
    
    private Text getText(String content)
    {
        Text text = new Text(content);
        text.setFontStyle("font-family:Arial,Helvetica,sans-serif;font-size:8pt;font-weight:bold;color:#000000;");
        return text;
    }

    private Link getLink(Text text, IWContext iwc)
    {
        Link link = new Link(text);
        link.maintainParameter("iw_ch_ch_p", iwc);
        link.maintainParameter("iw_ch_s", iwc);
        link.maintainParameter("iw_ch_d_n", iwc);
        link.maintainParameter("iw_ch_v_n", iwc);
        return link;
    }
    
    
    public Table getListTable(IWContext iwc)
    throws RemoteException
{
    Table table = new Table(3, USERS_PER_PAGE + 1);
    table.setCellspacing(0);
    table.setCellpadding(2);
    table.setWidth("100%");
    table.setHorizontalZebraColored("#C1C3B4", "#FFFFFF");
    int row = 1;
    int rowHeight = 12;
    table.setHeight(table.getRows() * rowHeight);
    table.add(getTitleText(localize("user", "User")), 1, row);
    table.add(getTitleText(localize("school", "School")), 2, row);
    table.add(getTitleText(localize("personal_id", "Personal ID")), 3, row);
    table.setRowColor(row, "#74858D");
    table.setHeight(row, rowHeight);
    
    if(users != null)
    {
        int start = currentPage * USERS_PER_PAGE;
        Iterator iter = users.iterator();
        for(int i = 0; i < start; i++)
            if(iter.hasNext())
                iter.next();

        String pId;
        for(int counter = 0; iter.hasNext() && counter < USERS_PER_PAGE; table.add(getText(pId), 3, row))
        {
            counter++;
            row++;
            table.setHeight(row, rowHeight);
            User user = (User)iter.next();
            pId = user.getPersonalID();
            if(pId == null)
                pId = "-";
            Link link = getLink(getText(user.getName()), iwc);
            
            link.addParameter(PARAMETER_USER_ID, user.getPrimaryKey().toString());
            table.add(link, 1, row);
            
            table.add(getText(getSchoolName(user)), 2, row);
        }

    }
    return table;
}
    
    private String getSchoolName(User user)
    {
        try
        {
            SchoolUserHome home = (SchoolUserHome) IDOLookup.getHome(SchoolUser.class);
            SchoolUser schoolUser = home.findForUser(user);            
            int schoolid = schoolUser.getSchoolId();
            
            SchoolHome home2 = (SchoolHome) IDOLookup.getHome(School.class);
            School school = home2.findByPrimaryKey(new Integer(schoolid));
            
            return school.getName();
        }
        catch (Exception e)
        {
            return "";
        }
    }
    
	protected void init(IWContext iwc) {		
		form = new Form();
		form.maintainParameter(SchoolUserChooser.PARAMETER_SCHOOL_ID);
		
		searchString = iwc.getParameter(PARAMETER_SEARCH);
		iwrb = iwc.getIWMainApplication().getBundle(BuilderConstants.STANDARD_IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc);
		showAll = iwc.isParameterSet(PARAMETER_VIEW_ALL);
		if (iwc.isParameterSet(PARAMETER_CURRENT_PAGE)) {
			currentPage = Integer.parseInt(iwc.getParameter(PARAMETER_CURRENT_PAGE));
		}
		try {
			SchoolUserBusiness biz = getSchoolUserBusiness(iwc);
			int[] userTypes = {
					SchoolUserBusinessBean.USER_TYPE_HEADMASTER, 
					SchoolUserBusinessBean.USER_TYPE_ASSISTANT_HEADMASTER, 
					SchoolUserBusinessBean.USER_TYPE_IB_COORDINATOR,
					SchoolUserBusinessBean.USER_TYPE_STUDY_AND_WORK_COUNCEL,
					SchoolUserBusinessBean.USER_TYPE_TEACHER,
					SchoolUserBusinessBean.USER_TYPE_WEB_ADMIN,
					SchoolUserBusinessBean.USER_TYPE_SCHOOL_MASTER,
					SchoolUserBusinessBean.USER_TYPE_CONTACT_PERSON,
					SchoolUserBusinessBean.USER_TYPE_EXPEDITION,
					SchoolUserBusinessBean.USER_TYPE_PROJECT_MANAGER};			
			
            if (isAdmin())
            {
                SchoolUserHome userHome;
                userHome = (SchoolUserHome)IDOLookup.getHome(com.idega.user.data.User.class);
                users = userHome.findByTypes(userTypes);              
                
            } else
            {
                users = biz.getUsers(getProvider(iwc), userTypes);
            }            			            
		}
		catch (FinderException ex) {
			ex.printStackTrace();
		}
		catch (RemoteException ex) {
			ex.printStackTrace();
		}
	}
	
    private boolean isAdmin()
    {        
        try
        {                       
            IWMainApplicationSettings settings = getIWMainApplication().getSettings();
            String groupId = settings.getProperty("customer_choice_group_id");
            
            if (groupId==null) return false;
            
            GroupHome groupHome;
            groupHome = (GroupHome)IDOLookup.getHome(com.idega.user.data.Group.class);
            Group group = groupHome.findByPrimaryKey(new Integer(groupId));
            
            FacesContext context = FacesContext.getCurrentInstance();
            IWContext iwContext = IWContext.getIWContext(context);
            return iwContext.getCurrentUser().getGroup().getUniqueId().equals(group.getUniqueId());
        }
        catch (Exception e)
        {
                return false;
        }                        
    }	
}
