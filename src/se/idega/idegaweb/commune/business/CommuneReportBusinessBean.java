/*
 * Created on 8.8.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.business;

import is.idega.idegaweb.member.business.MemberFamilyLogic;
import is.idega.idegaweb.member.business.NoParentFound;

import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.datareport.util.ReportableCollection;
import com.idega.block.datareport.util.ReportableData;
import com.idega.block.datareport.util.ReportableField;
import com.idega.business.IBOLookup;
import com.idega.business.IBOService;
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
	
	CommuneUserBusiness _communeUserService = null;
	MemberFamilyLogic _familyLogic = null;
	
	/**
	 * 
	 */
	public CommuneReportBusinessBean() {
		super();
	}
	
	private void initializeCommuneUserBusinessIfNeeded() throws RemoteException{
		if(_communeUserService==null){
			_communeUserService = (CommuneUserBusiness)IBOLookup.getServiceInstance(this.getIWApplicationContext(),CommuneUserBusiness.class);
		}
	}
	
	private void initializeMemberFamilyLogicIfNeeded() throws RemoteException{
		if(_familyLogic==null){
			_familyLogic = (MemberFamilyLogic)IBOLookup.getServiceInstance(this.getIWApplicationContext(),MemberFamilyLogic.class);
		}
	}
	
	public ReportableCollection getChildAndItsParentsRegisteredInCommune(Date firstBirthDateInPeriode, Date lastBirthDateInPeriode, Timestamp firstRegistrationDateInPeriode, Timestamp lastRegistrationDateInPeriode) throws RemoteException, CreateException, FinderException{
		initializeMemberFamilyLogicIfNeeded();
		initializeCommuneUserBusinessIfNeeded();
		
		ReportableCollection reportData = new ReportableCollection();
		
		//find the main nacka group
		Group communeGroup = _communeUserService.getRootCitizenGroup(); //((GroupHome)IDOLookup.getHome(Group.class)).findByPrimaryKey(new Integer(3));
		
		//find all childs by conditions
		Collection childs = ((UserHome)IDOLookup.getHome(User.class)).findByDateOfBirthAndGroupRelationInitiationTimeAndStatus(firstBirthDateInPeriode,lastBirthDateInPeriode,communeGroup,firstRegistrationDateInPeriode,lastRegistrationDateInPeriode,null);
		
		GroupRelationHome gRelationHome = ((GroupRelationHome)IDOLookup.getHome(GroupRelation.class));
		
		//initializing fields
		IDOEntityDefinition userDef = IDOLookup.getEntityDefinitionForClass(User.class);
		IDOEntityDefinition grRelDef = IDOLookup.getEntityDefinitionForClass(GroupRelation.class);
		Locale currentLocale = this.getUserContext().getCurrentLocale();
		
		
		//Child - Fields
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
		childGroupInvitationDate.setValueClass(String.class);
		childGroupInvitationDate.setCustomMadeFieldName("child_gr_initiation_date");
		//TMP
		childGroupInvitationDate.setLocalizedName("Invitiation date",currentLocale);
		reportData.addField(childGroupInvitationDate);
		
		
		
		//Parent1 - Fields
		ReportableField parent1PersonalID = new ReportableField(userDef.findFieldByUniqueName(User.FIELD_PERSONAL_ID));
		parent1PersonalID.setCustomMadeFieldName("parent1_ssn");
		//TMP - localization
		parent1PersonalID.setLocalizedName("Personal ID",currentLocale);
		reportData.addField(parent1PersonalID);
		
		ReportableField parent1LastName = new ReportableField(userDef.findFieldByUniqueName(User.FIELD_LAST_NAME));
		parent1LastName.setCustomMadeFieldName("parent1_last_name");
		//TMP
		parent1LastName.setLocalizedName("LastName",currentLocale);
		reportData.addField(parent1LastName);

		ReportableField parent1FirstName = new ReportableField(userDef.findFieldByUniqueName(User.FIELD_FIRST_NAME));
		parent1FirstName.setCustomMadeFieldName("parent1_first_name");
		//TMP
		parent1FirstName.setLocalizedName("FirstName",currentLocale);
		reportData.addField(parent1FirstName);
		
		ReportableField parent1GroupInvitationDate = new ReportableField(grRelDef.findFieldByUniqueName(GroupRelation.FIELD_INITIATION_DATE));
		parent1GroupInvitationDate.setValueClass(String.class);
		parent1GroupInvitationDate.setCustomMadeFieldName("parent1_gr_initiation_date");
		//TMP
		parent1GroupInvitationDate.setLocalizedName("Invitiation date",currentLocale);
		reportData.addField(parent1GroupInvitationDate);
		
		
		//Parent2 - Fields
		ReportableField parent2PersonalID = new ReportableField(userDef.findFieldByUniqueName(User.FIELD_PERSONAL_ID));
		parent2PersonalID.setCustomMadeFieldName("parent2_ssn");
		//TMP - localization
		parent2PersonalID.setLocalizedName("Personal ID",currentLocale);
		reportData.addField(parent2PersonalID);
		
		ReportableField parent2LastName = new ReportableField(userDef.findFieldByUniqueName(User.FIELD_LAST_NAME));
		parent2LastName.setCustomMadeFieldName("parent2_last_name");
		//TMP
		parent2LastName.setLocalizedName("LastName",currentLocale);
		reportData.addField(parent2LastName);

		ReportableField parent2FirstName = new ReportableField(userDef.findFieldByUniqueName(User.FIELD_FIRST_NAME));
		parent2FirstName.setCustomMadeFieldName("parent2_first_name");
		//TMP
		parent2FirstName.setLocalizedName("FirstName",currentLocale);
		reportData.addField(parent2FirstName);
		
		ReportableField parent2GroupInvitationDate = new ReportableField(grRelDef.findFieldByUniqueName(GroupRelation.FIELD_INITIATION_DATE));
		parent2GroupInvitationDate.setValueClass(String.class);
		parent2GroupInvitationDate.setCustomMadeFieldName("parent2_gr_initiation_date");
		//TMP
		parent2GroupInvitationDate.setLocalizedName("Invitiation date",currentLocale);
		reportData.addField(parent2GroupInvitationDate);
		
		
		

		
		//Creating report data and adding to collection 
		Iterator iter = childs.iterator();
		while (iter.hasNext()) {
			User child = (User)iter.next();
			ReportableData data = new ReportableData();
			
			//ChildData
			data.addData(childPersonalID,child.getPersonalID());
			data.addData(childLastName,child.getLastName());
			data.addData(childFirstName,child.getFirstName());
			reportData.add(data);
			
			Collection coll = gRelationHome.findGroupsRelationshipsContaining(communeGroup,child);
			Iterator iterator = coll.iterator();
			if(iterator.hasNext()){
				GroupRelation rel = (GroupRelation)iterator.next();
				Timestamp time = rel.getInitiationDate();
				if(time != null){
					data.addData(childGroupInvitationDate,SimpleDateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.DEFAULT,currentLocale).format(time));
				} else {
					//TODO localize
					data.addData(childGroupInvitationDate,"No time specified");
				}
				
			}
			
			try {
				//Parent data
				Collection parents = _familyLogic.getParentsFor(child);
				Iterator pIter = parents.iterator();
				//parent1
				if (pIter.hasNext()) {
					User parent = (User)pIter.next();
					ReportableData pData = new ReportableData();
				
					//ChildData
					data.addData(parent1PersonalID,parent.getPersonalID());
					data.addData(parent1LastName,parent.getLastName());
					data.addData(parent1FirstName,parent.getFirstName());
					reportData.add(pData);
				
					Collection pColl = gRelationHome.findGroupsRelationshipsContaining(communeGroup,parent);
					Iterator pIterator = pColl.iterator();
					if(pIterator.hasNext()){
						GroupRelation rel = (GroupRelation)iterator.next();
						Timestamp time = rel.getInitiationDate();
						if(time != null){
							data.addData(parent1GroupInvitationDate,SimpleDateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.DEFAULT,currentLocale).format(time));
						} else {
							//TODO localize
							data.addData(parent1GroupInvitationDate,"No time specified");
						}
				
					}
				}
				
				//Parent2
				if (pIter.hasNext()) {
					User parent = (User)pIter.next();
					ReportableData pData = new ReportableData();
				
					//ChildData
					data.addData(parent2PersonalID,parent.getPersonalID());
					data.addData(parent2LastName,parent.getLastName());
					data.addData(parent2FirstName,parent.getFirstName());
					reportData.add(pData);
				
					Collection pColl = gRelationHome.findGroupsRelationshipsContaining(communeGroup,parent);
					Iterator pIterator = pColl.iterator();
					if(pIterator.hasNext()){
						GroupRelation rel = (GroupRelation)iterator.next();
						Timestamp time = rel.getInitiationDate();
						if(time != null){
							data.addData(parent2GroupInvitationDate,SimpleDateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.DEFAULT,currentLocale).format(time));
						} else {
							//TODO localize
							data.addData(parent2GroupInvitationDate,"No time specified");
						}
				
					}
				}
			} catch (NoParentFound e) {
				System.out.println("["+this.getClass()+"]: "+e.getMessage());
				//e.printStackTrace();
			} 
			
			
		}
		
		return reportData;
	}

}
