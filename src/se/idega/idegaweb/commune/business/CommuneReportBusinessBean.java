/*
 * Created on 8.8.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.business;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import javax.ejb.FinderException;

import com.idega.block.datareport.util.ReportableCollection;
import com.idega.block.datareport.util.ReportableData;
import com.idega.block.datareport.util.ReportableField;
import com.idega.business.IBOSessionBean;
import com.idega.data.IDOEntityDefinition;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.data.GroupRelation;
import com.idega.user.data.GroupRelationHome;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;

/**
 * Title:		CommuneReportBusinessBean
 * Description:
 * Copyright:	Copyright (c) 2003
 * Company:		idega Software
 * @author		2003 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */
public class CommuneReportBusinessBean extends IBOSessionBean implements CommuneReportBusiness{

	/**
	 * 
	 */
	public CommuneReportBusinessBean() {
		super();
	}
	
	public ReportableCollection getChildAndItsParentsRegisteredInCommune(Date firstBirthDateInPeriode, Date lastBirthDateInPeriode, Timestamp firstRegistrationDateInPeriode, Timestamp lastRegistrationDateInPeriode) throws IDOLookupException, FinderException{
		ReportableCollection reportData = new ReportableCollection();
		//find the main nacka group
		//TODO Find CommuneGroup - TMP
		Group communeGroup = ((GroupHome)IDOLookup.getHome(Group.class)).findByPrimaryKey(new Integer(3));
		
		//find all childs by conditions
		Collection childs = ((UserHome)IDOLookup.getHome(User.class)).findByDateOfBirthAndGroupRelationInitiationTimeAndStatus(firstBirthDateInPeriode,lastBirthDateInPeriode,communeGroup,firstRegistrationDateInPeriode,lastRegistrationDateInPeriode,null);
		
		GroupRelationHome gRelationHome = ((GroupRelationHome)IDOLookup.getHome(GroupRelation.class));
		
		//initializing fields
		IDOEntityDefinition userDef = IDOLookup.getEntityDefinitionForClass(User.class);
		IDOEntityDefinition grRelDef = IDOLookup.getEntityDefinitionForClass(GroupRelation.class);
		Locale currentLocale = this.getUserContext().getCurrentLocale();
		
		ReportableField childPersonalID = new ReportableField(userDef.findFieldByUniqueName(User.FIELD_PERSONAL_ID));
		childPersonalID.setCustomMadeFieldName("child_ssn");
		//TMP - localization
		childPersonalID.setLocalizedName("Personal ID",currentLocale);
		reportData.addField(childPersonalID);
		
		ReportableField childLastName = new ReportableField(userDef.findFieldByUniqueName(User.FIELD_LAST_NAME));
		childLastName.setCustomMadeFieldName("child_last_name");
		//TMP
		childLastName.setLocalizedName("LastName",currentLocale);
		reportData.addField(childLastName);

		ReportableField childFirstName = new ReportableField(userDef.findFieldByUniqueName(User.FIELD_FIRST_NAME));
		childFirstName.setCustomMadeFieldName("child_first_name");
		//TMP
		childFirstName.setLocalizedName("FirstName",currentLocale);
		reportData.addField(childFirstName);
		
		ReportableField childGroupInvitationDate = new ReportableField(grRelDef.findFieldByUniqueName(GroupRelation.FIELD_INITIATION_DATE));
		childGroupInvitationDate.setCustomMadeFieldName("child_gr_initiation_date");
		//TMP
		childGroupInvitationDate.setLocalizedName("Invitiation date",currentLocale);
		reportData.addField(childGroupInvitationDate);

		
		//Creating report data and adding to collection 
		Iterator iter = childs.iterator();
		while (iter.hasNext()) {
			User child = (User)iter.next();
			ReportableData data = new ReportableData();
			data.addData(childPersonalID,child.getPersonalID());
			data.addData(childLastName,child.getLastName());
			data.addData(childFirstName,child.getFirstName());
			reportData.add(data);
			
			Collection coll = gRelationHome.findGroupsRelationshipsContaining(communeGroup,child);
			Iterator iterator = coll.iterator();
			if(iterator.hasNext()){
				GroupRelation rel = (GroupRelation)iterator.next();
				data.addData(childGroupInvitationDate,rel.getInitiationDate());
			}
			
			
		}
		
		return reportData;
	}

}
