/*
 * $Id: SchoolGroupSelector.java,v 1.1 2004/11/26 09:07:12 aron Exp $
 * Created on 25.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.school.presentation;

import java.util.Collection;
import java.util.Iterator;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassHome;
import com.idega.block.school.data.SchoolHome;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolTypeHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.presentation.IWContext;
import com.idega.presentation.remotescripting.RemoteScriptHandler;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.InterfaceObject;
import com.idega.presentation.ui.SelectOption;
import com.idega.presentation.ui.util.SelectorUtility;

/**
 * 
 *  Last modified: $Date: 2004/11/26 09:07:12 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public class SchoolGroupSelector extends InterfaceObject {
    
    public static final String PARAMETER_ACTION = "p_act";
	public static final String ACTION_UPDATE_SCHOOLS = "a_uc";
	public static final String ACTION_UPDATE_GROUPS = "a_upc";
	
	public static final String PARAMETER_TYPE_ID = "pcd_";
    
    private String specifiedTypeID = null;
	private String specifiedSchoolID =  null;
	private String specifiedGroupID = null;
	
	private String parTypeID = null;
	private String parSchoolID = null;
	private String parGroupID = null;
    
    private DropdownMenu typeDrop = null;
	private DropdownMenu schoolDrop = null;
	private DropdownMenu groupDrop = null;
	
	private SchoolCategory category = null;
	private String iframeName = "tmpFrame";
	private Collection types = null;
	
	public SchoolGroupSelector() {
		this("sipc", "sipci", "sipz");
	}
	
	public SchoolGroupSelector(String typeParameterName, String schoolParameterName, String groupParameterName) {
		parTypeID = typeParameterName;
		parSchoolID = schoolParameterName;
		parGroupID = groupParameterName;
		
		iframeName = parTypeID + "_" + parSchoolID + "_" + parGroupID;
		
		setName(iframeName);
		
		typeDrop = new DropdownMenu(parTypeID);
		schoolDrop = new DropdownMenu(parSchoolID);
		groupDrop = new DropdownMenu(parGroupID);
	}
	
	public Object clone() {
	    SchoolGroupSelector inp = (SchoolGroupSelector) super.clone();
		if (typeDrop != null) {
			inp.typeDrop = (DropdownMenu) typeDrop.clone();
		}
		if (schoolDrop != null) {
			inp.schoolDrop = (DropdownMenu) schoolDrop.clone();
		}
		if (groupDrop != null) {
			inp.groupDrop = (DropdownMenu) groupDrop.clone();
		}
		if (category != null) {
			inp.category = category;
		}
		if(types!=null){
		    inp.types = types;
		}
		return inp;
	}
	
	public DropdownMenu getTypeDropdown() {
		return typeDrop;
	}
	
	public DropdownMenu getSchoolDropdown() {
		return schoolDrop;
	}
	
	public DropdownMenu getGroupDropdown() {
		return groupDrop;
	}
	
	public void main(IWContext iwc) throws Exception {

		String usedTypeID = iwc.getParameter(parTypeID);
		String usedSchoolID = iwc.getParameter(parSchoolID);
		String usedGroupID = iwc.getParameter(parGroupID);
		
		if (specifiedTypeID != null) {
		    usedTypeID = specifiedTypeID;
		}
		if (specifiedSchoolID != null) {
		    usedSchoolID = specifiedSchoolID;
		}
		if (specifiedGroupID != null) {
			usedGroupID = specifiedGroupID;
		}

		SchoolTypeHome typeHome = (SchoolTypeHome) IDOLookup.getHome(SchoolType.class);
		SchoolHome schoolHome = (SchoolHome)IDOLookup.getHome(School.class);
		SchoolClassHome groupHome = (SchoolClassHome) IDOLookup.getHome(SchoolClass.class);
		if ( types!=null && category != null ) {
			types = typeHome.findAllByCategory((String)category.getPrimaryKey());
		}
		Collection schools = null;
		Collection groups = null;

		SelectorUtility su = new SelectorUtility();
		
		typeDrop.addMenuElement("-1", "Select a type");
		typeDrop.addMenuElements(types);
		if (usedTypeID != null) {
		    schools = schoolHome.findAllBySchoolType(Integer.valueOf(usedTypeID).intValue());
			typeDrop.setSelectedElement(usedTypeID);
		}
		
		if (schools != null) {
			Iterator iter = schools.iterator();
			while (iter.hasNext()) {
				School school = (School) iter.next();
				schoolDrop.addMenuElement(school.getPrimaryKey().toString(), school.getName());
			}
		}
		if (schools == null) {
			schoolDrop.addFirstOption(new SelectOption("Select a school", "-1"));
		} 
		
		if (usedSchoolID != null) {
			groups = groupHome.findBySchoolAndCategory(Integer.valueOf(usedSchoolID).intValue(),category.getName());
			schoolDrop.setSelectedElement(usedSchoolID);
		}

		groupDrop = (DropdownMenu) su.getSelectorFromIDOEntities(groupDrop, groups, "getName");
		if (schools == null) {
			groupDrop.addFirstOption(new SelectOption("Select a group", "-1"));
		}
		if (usedGroupID != null) {
		    groupDrop.setSelectedElement(usedGroupID);
		}
				
		
		if (typeDrop.getParent() == null) {
			add(typeDrop);
		}
		if (schoolDrop.getParent() == null) {
			add(schoolDrop);
		}
		if (groupDrop.getParent() == null) {
			add(groupDrop);
		}

		RemoteScriptHandler rsh = new RemoteScriptHandler(typeDrop, schoolDrop);
		rsh.setRemoteScriptCollectionClass(SchoolGroupSelectorCollectionHandler.class);
		rsh.addParameter(PARAMETER_ACTION, ACTION_UPDATE_SCHOOLS);
		rsh.setToClear(groupDrop, "Select a group");
		add(rsh);
			
		RemoteScriptHandler rsh2 = new RemoteScriptHandler(schoolDrop, groupDrop);
		rsh2.setRemoteScriptCollectionClass(SchoolGroupSelectorCollectionHandler.class);
		rsh2.addParameter(PARAMETER_ACTION, ACTION_UPDATE_GROUPS);
		rsh2.addParameter(PARAMETER_TYPE_ID, parTypeID);
		add(rsh2);
		
	}
	
	public void setSelectedSchool(Object schoolPK){
	    try {
            SchoolHome schoolHome = (SchoolHome) IDOLookup.getHome(School.class);
            School school = schoolHome.findByPrimaryKey(schoolPK);
            specifiedSchoolID = school.getPrimaryKey().toString();
            types = school.getSchoolTypes();
        } catch (IDOLookupException e) {
            
        } catch (EJBException e) {
           
        } catch (IDORelationshipException e) {
            
        } catch (FinderException e) {
           
        }
	}
	
	/*
	public void setSelectedPostalCode(Object schoolClassPK) throws FinderException {
		try {
			SchoolClassHome pcHome = (SchoolClassHome) IDOLookup.getHome(SchoolClass.class);
		
			SchoolClass schoolClass = pcHome.findByPrimaryKey(schoolClassPK);
			School school = schoolClass.getSchool();
			specifiedTypeID = school.getSchoolTypes();
			specifiedSchoolID = pc.getName();
			specifiedGroupID = postalCodePK.toString();
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} 
	}*/

	public void setStyleClass(String styleClass) {
		typeDrop.setStyleClass(styleClass);
		schoolDrop.setStyleClass(styleClass);
		groupDrop.setStyleClass(styleClass);
	}
	
	public void setSchoolCategory(SchoolCategory category){
	    this.category = category;
	}

    /* (non-Javadoc)
     * @see com.idega.presentation.ui.InterfaceObject#handleKeepStatus(com.idega.presentation.IWContext)
     */
    public void handleKeepStatus(IWContext iwc) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see com.idega.presentation.PresentationObject#isContainer()
     */
    public boolean isContainer() {
        // TODO Auto-generated method stub
        return false;
    }

}
