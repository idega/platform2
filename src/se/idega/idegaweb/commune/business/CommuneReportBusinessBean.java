/*
 * Created on 8.8.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.business;

import is.idega.block.family.business.FamilyLogic;
import is.idega.idegaweb.member.business.NoCustodianFound;

import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;

import se.idega.idegaweb.commune.user.data.Citizen;
import se.idega.idegaweb.commune.user.data.CitizenHome;

import com.idega.block.datareport.util.ReportableCollection;
import com.idega.block.datareport.util.ReportableData;
import com.idega.block.datareport.util.ReportableField;
import com.idega.business.IBOLookup;
import com.idega.business.IBOSessionBean;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.data.IDOEntityDefinition;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.user.business.UserStatusBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.GroupRelation;
import com.idega.user.data.GroupRelationHome;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
import com.idega.user.data.UserStatus;
import com.idega.util.IWTimestamp;

/**
 * Title:		CommuneReportBusinessBean
 * Description:
 * Copyright:	Copyright (c) 2003
 * Company:		idega Software
 * @author		2003 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */
public class CommuneReportBusinessBean extends IBOSessionBean implements CommuneReportBusiness{
	
	private CommuneUserBusiness _communeUserService = null;
	private FamilyLogic _familyLogic = null;
	private IWBundle _iwb = null;
	private IWResourceBundle _iwrb = null;
	private final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune";
	private final long millisecondsInOneDay = 8640000;

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
			_familyLogic = (FamilyLogic)IBOLookup.getServiceInstance(this.getIWApplicationContext(),FamilyLogic.class);
		}
	}
	
	private void initializeBundlesIfNeeded(){
		if(_iwb==null){
			_iwb = this.getIWApplicationContext().getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER);
		}
		_iwrb = _iwb.getResourceBundle(this.getUserContext().getCurrentLocale());
	}
	
	public  ReportableCollection getChildAndItsParentsRegisteredInCommune(Date firstRegistrationDateInPeriode, Date lastRegistrationDateInPeriode, Date firstBirthDateInPeriode, Date lastBirthDateInPeriode) throws RemoteException, CreateException, FinderException{
		
		Timestamp timestampFRDIP = new Timestamp(firstRegistrationDateInPeriode.getTime());
		final long millisecondsInOneDay = 8640000;
		Timestamp timestampLRDIP = new Timestamp(lastRegistrationDateInPeriode.getTime()+millisecondsInOneDay-1);
		
		return 	getChildAndItsParentsRegisteredInCommune(firstBirthDateInPeriode, lastBirthDateInPeriode, timestampFRDIP, timestampLRDIP);
	}
	
	public ReportableCollection getCitizensRelatedToChildCareOrSchoolAndHaveChangedStatusInSelectedPeriod(Date firstDateOfContitionInPeriode, Date lastDateOfConditionInPeriode, Date firstBirthDateInPeriode, Date lastBirthDateInPeriode) throws RemoteException, CreateException, FinderException{
		ReportableCollection rColl = getCitizensRelatedToSchoolAndHaveChangedStatusInSelectedPeriod(firstBirthDateInPeriode,lastBirthDateInPeriode,firstDateOfContitionInPeriode,lastDateOfConditionInPeriode);
		return rColl;
	}
	
	
	public ReportableCollection getChildAndItsParentsRegisteredInCommune(Date firstBirthDateInPeriode, Date lastBirthDateInPeriode, Timestamp firstRegistrationDateInPeriode, Timestamp lastRegistrationDateInPeriode) throws RemoteException, CreateException, FinderException{
		initializeMemberFamilyLogicIfNeeded();
		initializeCommuneUserBusinessIfNeeded();
		initializeBundlesIfNeeded();
				
		ReportableCollection reportData = new ReportableCollection();
		
		//find the main nacka group
		Group communeGroup = _communeUserService.getRootCitizenGroup(); //((GroupHome)IDOLookup.getHome(Group.class)).findByPrimaryKey(new Integer(3));
		
		//find all childs by conditions
		String[] status = new String[1];
		status[0] = GroupRelation.STATUS_ACTIVE;
		Collection childs = ((UserHome)IDOLookup.getHome(User.class)).findByDateOfBirthAndGroupRelationInitiationTimeAndStatus(firstBirthDateInPeriode,lastBirthDateInPeriode,communeGroup,firstRegistrationDateInPeriode,lastRegistrationDateInPeriode,status);
		
		GroupRelationHome gRelationHome = ((GroupRelationHome)IDOLookup.getHome(GroupRelation.class));
		
		//initializing fields
		IDOEntityDefinition userDef = IDOLookup.getEntityDefinitionForClass(User.class);
		IDOEntityDefinition grRelDef = IDOLookup.getEntityDefinitionForClass(GroupRelation.class);
		IDOEntityDefinition addrDef = IDOLookup.getEntityDefinitionForClass(Address.class);
		Locale currentLocale = this.getUserContext().getCurrentLocale();
		DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT,currentLocale);
		DateFormat dateTimeFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT,currentLocale);
		
		
		//Child - Fields
		ReportableField childPersonalID = new ReportableField(userDef.findFieldByUniqueName(User.FIELD_PERSONAL_ID));
		childPersonalID.setCustomMadeFieldName("child_ssn");
		childPersonalID.setLocalizedName( _iwrb.getLocalizedString("CommuneReportBusiness.child_ssn","Personal ID"),currentLocale);
		reportData.addField(childPersonalID);
		
		ReportableField childLastName = new ReportableField(userDef.findFieldByUniqueName(User.FIELD_LAST_NAME));
		childLastName.setCustomMadeFieldName("child_last_name");
		childLastName.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.child_last_name","LastName"),currentLocale);
		reportData.addField(childLastName);

		ReportableField childFirstName = new ReportableField(userDef.findFieldByUniqueName(User.FIELD_FIRST_NAME));
		childFirstName.setCustomMadeFieldName("child_first_name");
		childFirstName.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.child_first_name","FirstName"),currentLocale);
		reportData.addField(childFirstName);
		
		ReportableField childAddress = new ReportableField(addrDef.findFieldByUniqueName(Address.FIELD_STREET_NAME));
		childAddress.setValueClass(String.class);
		childAddress.setCustomMadeFieldName("child_address");
		childAddress.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.child_address","Address"),currentLocale);
		reportData.addField(childAddress);

		ReportableField childGroupInvitationDate = new ReportableField(grRelDef.findFieldByUniqueName(GroupRelation.FIELD_INITIATION_DATE));
		childGroupInvitationDate.setValueClass(String.class);
		childGroupInvitationDate.setCustomMadeFieldName("child_gr_initiation_date");
		childGroupInvitationDate.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.child_gr_initiation_date","Invitiation date"),currentLocale);
		reportData.addField(childGroupInvitationDate);
		
		
		
		//Parent1 - Fields
		ReportableField parent1PersonalID = new ReportableField(userDef.findFieldByUniqueName(User.FIELD_PERSONAL_ID));
		parent1PersonalID.setCustomMadeFieldName("parent1_ssn");
		parent1PersonalID.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.parent1_ssn","Parent1 Personal ID"),currentLocale);
		reportData.addField(parent1PersonalID);
		
		ReportableField parent1LastName = new ReportableField(userDef.findFieldByUniqueName(User.FIELD_LAST_NAME));
		parent1LastName.setCustomMadeFieldName("parent1_last_name");
		parent1LastName.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.parent1_last_name","Parent1 LastName"),currentLocale);
		reportData.addField(parent1LastName);

		ReportableField parent1FirstName = new ReportableField(userDef.findFieldByUniqueName(User.FIELD_FIRST_NAME));
		parent1FirstName.setCustomMadeFieldName("parent1_first_name");
		parent1FirstName.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.parent1_first_name","Parent1 FirstName"),currentLocale);
		reportData.addField(parent1FirstName);
		
		ReportableField parent1Address = new ReportableField(addrDef.findFieldByUniqueName(Address.FIELD_STREET_NAME));
		parent1Address.setValueClass(String.class);
		parent1Address.setCustomMadeFieldName("parent1_address");
		parent1Address.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.parent1_address","Parent1 Address"),currentLocale);
		reportData.addField(parent1Address);
		
		ReportableField parent1GroupInvitationDate = new ReportableField(grRelDef.findFieldByUniqueName(GroupRelation.FIELD_INITIATION_DATE));
		parent1GroupInvitationDate.setValueClass(String.class);
		parent1GroupInvitationDate.setCustomMadeFieldName("parent1_gr_initiation_date");
		parent1GroupInvitationDate.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.parent1_gr_initiation_date","Parent1 Invitiation date"),currentLocale);
		reportData.addField(parent1GroupInvitationDate);
		
		
		//Parent2 - Fields
		ReportableField parent2PersonalID = new ReportableField(userDef.findFieldByUniqueName(User.FIELD_PERSONAL_ID));
		parent2PersonalID.setCustomMadeFieldName("parent2_ssn");
		parent2PersonalID.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.parent2_ssn","Parent2 Personal ID"),currentLocale);
		reportData.addField(parent2PersonalID);
		
		ReportableField parent2LastName = new ReportableField(userDef.findFieldByUniqueName(User.FIELD_LAST_NAME));
		parent2LastName.setCustomMadeFieldName("parent2_last_name");
		parent2LastName.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.parent2_last_name","Parent2 LastName"),currentLocale);
		reportData.addField(parent2LastName);

		ReportableField parent2FirstName = new ReportableField(userDef.findFieldByUniqueName(User.FIELD_FIRST_NAME));
		parent2FirstName.setCustomMadeFieldName("parent2_first_name");
		parent2FirstName.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.parent2_first_name","Parent2 FirstName"),currentLocale);
		reportData.addField(parent2FirstName);
		
		ReportableField parent2Address = new ReportableField(addrDef.findFieldByUniqueName(Address.FIELD_STREET_NAME));
		parent2Address.setValueClass(String.class);
		parent2Address.setCustomMadeFieldName("parent2_address");
		parent2Address.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.parent2_address","Parent2 Address"),currentLocale);
		reportData.addField(parent2Address);
		
		ReportableField parent2GroupInvitationDate = new ReportableField(grRelDef.findFieldByUniqueName(GroupRelation.FIELD_INITIATION_DATE));
		parent2GroupInvitationDate.setValueClass(String.class);
		parent2GroupInvitationDate.setCustomMadeFieldName("parent2_gr_initiation_date");
		parent2GroupInvitationDate.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.parent2_gr_initiation_date","Parent2 Invitiation date"),currentLocale);
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
			
			Address childAddressEntiy = _communeUserService.getUsersMainAddress(child);

			String childAddressString = this.getAddressString(childAddressEntiy,_iwrb);
			if(childAddressString != null){
				data.addData(childAddress,childAddressString);
			}
				

			
			Collection coll = gRelationHome.findGroupsRelationshipsContainingGroupsAndStatus(communeGroup,child,GroupRelation.STATUS_ACTIVE);
			Iterator iterator = coll.iterator();
			if(iterator.hasNext()){
				GroupRelation rel = (GroupRelation)iterator.next();
				Timestamp time = rel.getInitiationDate();
				if(time != null){
					data.addData(childGroupInvitationDate,dateFormat.format(time));
				} else {
					data.addData(childGroupInvitationDate,"No time specified");
				}
				
			}
			
			try {
				//Parent data
				Collection parents = _familyLogic.getCustodiansFor(child);
				Iterator pIter = parents.iterator();
				//parent1
				if (pIter.hasNext()) {
					User parent = (User)pIter.next();
					ReportableData pData = new ReportableData();
				
					data.addData(parent1PersonalID,parent.getPersonalID());
					data.addData(parent1LastName,parent.getLastName());
					data.addData(parent1FirstName,parent.getFirstName());
					reportData.add(pData);
				
					Address parent1AddressEntiy = _communeUserService.getUsersMainAddress(child);
					String parent1AddressString = this.getAddressString(parent1AddressEntiy,_iwrb);
					if(parent1AddressString!=null) {
						data.addData(parent1Address,parent1AddressString);
					}
								
					Collection pColl = gRelationHome.findGroupsRelationshipsContainingGroupsAndStatus(communeGroup,parent,GroupRelation.STATUS_ACTIVE);
					Iterator pIterator = pColl.iterator();
					if(pIterator.hasNext()){
						GroupRelation rel = (GroupRelation)pIterator.next();
						Timestamp time = rel.getInitiationDate();
						if(time != null){
							data.addData(parent1GroupInvitationDate,dateFormat.format(time));
						} else {
							data.addData(parent1GroupInvitationDate,_iwrb.getLocalizedString("CommuneReportBusiness.no_time_specified","No time specified"));
						}
				
					}
				}
				
				//Parent2
				if (pIter.hasNext()) {
					User parent = (User)pIter.next();
					ReportableData pData = new ReportableData();
				
					data.addData(parent2PersonalID,parent.getPersonalID());
					data.addData(parent2LastName,parent.getLastName());
					data.addData(parent2FirstName,parent.getFirstName());
					reportData.add(pData);
				
					Address parent2AddressEntiy = _communeUserService.getUsersMainAddress(child);
					String parent2AddressString = this.getAddressString(parent2AddressEntiy,_iwrb);
					if(parent2AddressString!=null) {
						data.addData(parent2Address,parent2AddressString);
					}
								
					Collection pColl = gRelationHome.findGroupsRelationshipsContainingGroupsAndStatus(communeGroup,parent,GroupRelation.STATUS_ACTIVE);
					Iterator pIterator = pColl.iterator();
					if(pIterator.hasNext()){
						GroupRelation rel = (GroupRelation)pIterator.next();
						Timestamp time = rel.getInitiationDate();
						if(time != null){
							data.addData(parent2GroupInvitationDate,dateFormat.format(time));
						} else {
							data.addData(parent2GroupInvitationDate,_iwrb.getLocalizedString("CommuneReportBusiness.no_time_specified","No time specified"));
						}
				
					}
				}
			} catch (NoCustodianFound e) {
				//System.out.println("["+this.getClass()+"]: "+e.getMessage());
				//e.printStackTrace();
			} 
			
			
		}
		
		reportData.addExtraHeaderParameter("label_current_date",_iwrb.getLocalizedString("CommuneReportBusiness.label_current_date","Current date"),"current_date",dateTimeFormat.format(IWTimestamp.getTimestampRightNow()));
		
		return reportData;
	}
	
	
	public ReportableCollection getCitizensRelatedToSchoolAndHaveChangedStatusInSelectedPeriod(Date firstBirthDateInPeriode, Date lastBirthDateInPeriode, Date firstDateOfContitionInPeriode, Date lastDateOfConditionInPeriode) throws RemoteException, CreateException, FinderException{
		ReportableCollection reportData = new ReportableCollection();		
		//get all users related to school or child care, that is if user is registered to school or child care or has child that is
		Collection registeredCitizens = ((CitizenHome)IDOLookup.getHome(Citizen.class)).findAllCitizensRegisteredToSchool(firstBirthDateInPeriode, lastBirthDateInPeriode,IWTimestamp.RightNow().getDate());
		return filterOutCitizensAndAddToReportDataSource(reportData,registeredCitizens,firstDateOfContitionInPeriode, lastDateOfConditionInPeriode);
	}
	
	
	private ReportableCollection filterOutCitizensAndAddToReportDataSource(ReportableCollection reportData, Collection citizenCollection,Date firstDateOfContitionInPeriode, Date lastDateOfConditionInPeriode) throws RemoteException, CreateException, FinderException{
		initializeMemberFamilyLogicIfNeeded();
		initializeCommuneUserBusinessIfNeeded();
		initializeBundlesIfNeeded();
		
		//find the main nacka group
		Group communeGroup = _communeUserService.getRootCitizenGroup(); //((GroupHome)IDOLookup.getHome(Group.class)).findByPrimaryKey(new Integer(3));

		GroupRelationHome gRelationHome = ((GroupRelationHome)IDOLookup.getHome(GroupRelation.class));
		//initializing fields
		IDOEntityDefinition userDef = IDOLookup.getEntityDefinitionForClass(User.class);
		IDOEntityDefinition grRelDef = IDOLookup.getEntityDefinitionForClass(GroupRelation.class);
		IDOEntityDefinition addrDef = IDOLookup.getEntityDefinitionForClass(Address.class);
		Locale currentLocale = this.getUserContext().getCurrentLocale();
		DateFormat dataFormat = DateFormat.getDateInstance(DateFormat.SHORT,currentLocale);
		DateFormat dateTimeFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT,currentLocale);
		
	
		//Child - Fields
		ReportableField childPersonalID = new ReportableField(userDef.findFieldByUniqueName(User.FIELD_PERSONAL_ID));
		childPersonalID.setCustomMadeFieldName("child_ssn");
		childPersonalID.setLocalizedName( _iwrb.getLocalizedString("CommuneReportBusiness.child_ssn","Personal ID"),currentLocale);
		reportData.addField(childPersonalID);
	
		ReportableField childLastName = new ReportableField(userDef.findFieldByUniqueName(User.FIELD_LAST_NAME));
		childLastName.setCustomMadeFieldName("child_last_name");
		childLastName.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.child_last_name","LastName"),currentLocale);
		reportData.addField(childLastName);

		ReportableField childFirstName = new ReportableField(userDef.findFieldByUniqueName(User.FIELD_FIRST_NAME));
		childFirstName.setCustomMadeFieldName("child_first_name");
		childFirstName.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.child_first_name","FirstName"),currentLocale);
		reportData.addField(childFirstName);
	
		ReportableField childAddress = new ReportableField(addrDef.findFieldByUniqueName(Address.FIELD_STREET_NAME));
		childAddress.setValueClass(String.class);
		childAddress.setCustomMadeFieldName("child_address");
		childAddress.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.child_address","Address"),currentLocale);
		reportData.addField(childAddress);

		ReportableField childGroupInvitationDate = new ReportableField(grRelDef.findFieldByUniqueName(GroupRelation.FIELD_INITIATION_DATE));
		childGroupInvitationDate.setValueClass(String.class);
		childGroupInvitationDate.setCustomMadeFieldName("child_gr_initiation_date");
		childGroupInvitationDate.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.child_gr_initiation_date","Invitiation date"),currentLocale);
		reportData.addField(childGroupInvitationDate);
	
		//reason
		ReportableField reasonField = new ReportableField("reason",String.class);
		reasonField.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.reason","Reason"),currentLocale);
		reportData.addField(reasonField);
		
		//date of action
		ReportableField actionDateField = new ReportableField("actionDate",String.class);
		actionDateField.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.reg_date","Registration date"),currentLocale);
		reportData.addField(actionDateField);
		
	
		//Parent1 - Fields
		ReportableField parent1PersonalID = new ReportableField(userDef.findFieldByUniqueName(User.FIELD_PERSONAL_ID));
		parent1PersonalID.setCustomMadeFieldName("parent1_ssn");
		parent1PersonalID.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.parent1_ssn","Parent1 Personal ID"),currentLocale);
		reportData.addField(parent1PersonalID);
	
		ReportableField parent1LastName = new ReportableField(userDef.findFieldByUniqueName(User.FIELD_LAST_NAME));
		parent1LastName.setCustomMadeFieldName("parent1_last_name");
		parent1LastName.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.parent1_last_name","Parent1 LastName"),currentLocale);
		reportData.addField(parent1LastName);

		ReportableField parent1FirstName = new ReportableField(userDef.findFieldByUniqueName(User.FIELD_FIRST_NAME));
		parent1FirstName.setCustomMadeFieldName("parent1_first_name");
		parent1FirstName.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.parent1_first_name","Parent1 FirstName"),currentLocale);
		reportData.addField(parent1FirstName);
	
		ReportableField parent1Address = new ReportableField(addrDef.findFieldByUniqueName(Address.FIELD_STREET_NAME));
		parent1Address.setValueClass(String.class);
		parent1Address.setCustomMadeFieldName("parent1_address");
		parent1Address.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.parent1_address","Parent1 Address"),currentLocale);
		reportData.addField(parent1Address);
	
		ReportableField parent1GroupInvitationDate = new ReportableField(grRelDef.findFieldByUniqueName(GroupRelation.FIELD_INITIATION_DATE));
		parent1GroupInvitationDate.setValueClass(String.class);
		parent1GroupInvitationDate.setCustomMadeFieldName("parent1_gr_initiation_date");
		parent1GroupInvitationDate.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.parent1_gr_initiation_date","Parent1 Invitiation date"),currentLocale);
		reportData.addField(parent1GroupInvitationDate);
	
	
		//Parent2 - Fields
		ReportableField parent2PersonalID = new ReportableField(userDef.findFieldByUniqueName(User.FIELD_PERSONAL_ID));
		parent2PersonalID.setCustomMadeFieldName("parent2_ssn");
		parent2PersonalID.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.parent2_ssn","Parent2 Personal ID"),currentLocale);
		reportData.addField(parent2PersonalID);
	
		ReportableField parent2LastName = new ReportableField(userDef.findFieldByUniqueName(User.FIELD_LAST_NAME));
		parent2LastName.setCustomMadeFieldName("parent2_last_name");
		parent2LastName.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.parent2_last_name","Parent2 LastName"),currentLocale);
		reportData.addField(parent2LastName);

		ReportableField parent2FirstName = new ReportableField(userDef.findFieldByUniqueName(User.FIELD_FIRST_NAME));
		parent2FirstName.setCustomMadeFieldName("parent2_first_name");
		parent2FirstName.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.parent2_first_name","Parent2 FirstName"),currentLocale);
		reportData.addField(parent2FirstName);
	
		ReportableField parent2Address = new ReportableField(addrDef.findFieldByUniqueName(Address.FIELD_STREET_NAME));
		parent2Address.setValueClass(String.class);
		parent2Address.setCustomMadeFieldName("parent2_address");
		parent2Address.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.parent2_address","Parent2 Address"),currentLocale);
		reportData.addField(parent2Address);
	
		ReportableField parent2GroupInvitationDate = new ReportableField(grRelDef.findFieldByUniqueName(GroupRelation.FIELD_INITIATION_DATE));
		parent2GroupInvitationDate.setValueClass(String.class);
		parent2GroupInvitationDate.setCustomMadeFieldName("parent2_gr_initiation_date");
		parent2GroupInvitationDate.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.parent2_gr_initiation_date","Parent2 Invitiation date"),currentLocale);
		reportData.addField(parent2GroupInvitationDate);
			

			
		//Creating report data and adding to collection 
		Iterator iter = citizenCollection.iterator();
		String reasonPrifixChild = _iwrb.getLocalizedString("CommuneReportBusiness.child","Child ");
		String reasonPrifixParent1 = _iwrb.getLocalizedString("CommuneReportBusiness.parent1","Parent1 ");
		String reasonPrifixparent2 = _iwrb.getLocalizedString("CommuneReportBusiness.parent2","Parent2 ");
		
		while (iter.hasNext()) {
			boolean addToList = false;
			User child = (User)iter.next();
			Collection parents = null;
			try{
				parents = _familyLogic.getCustodiansFor(child);
			} catch (NoCustodianFound e) {
				//System.out.println("["+this.getClass()+"]: "+e.getMessage());
				//e.printStackTrace();
			} catch (EJBException e){
				System.out.println("["+this.getClass()+"]: "+e.getMessage());
				System.out.println("["+this.getClass()+"]: user:"+child);
				e.printStackTrace();
			}
			
			Object[][] childConditions = getFulFilledConditionsForReportAndTheirOccurrenceTime(child, firstDateOfContitionInPeriode, lastDateOfConditionInPeriode,reasonPrifixChild);
			Object[][] parent1Conditions = null;
			Object[][] parent2Conditions = null;
			if(parents!= null){
				Iterator parConditionIter = parents.iterator();
				if(parConditionIter.hasNext()){
					User parent1 = (User)parConditionIter.next();
					if(parent1 !=null){
						parent1Conditions = getFulFilledConditionsForReportAndTheirOccurrenceTime(parent1,firstDateOfContitionInPeriode, lastDateOfConditionInPeriode,reasonPrifixParent1);
					}
				}
				if(parConditionIter.hasNext()){
					User parent2 = (User)parConditionIter.next();
					if(parent2 !=null){
						parent2Conditions = getFulFilledConditionsForReportAndTheirOccurrenceTime(parent2, firstDateOfContitionInPeriode, lastDateOfConditionInPeriode,reasonPrifixparent2);
					}
				}
			}
			
			
			// add fulfilled conditions to string and mark to put in report (that is set addToList=true)
			boolean firstFulfilledCondition = true;
			String reasonString = "";
			String actionDateString = "";
			if(childConditions != null){
				addToList=true;
				for (int i = 0; i < childConditions.length; i++) {
					if(!firstFulfilledCondition){
						reasonString += " / ";
						actionDateString += " / ";
					} else {
						firstFulfilledCondition=false;
					}
					reasonString += (String)childConditions[i][0];  //Reason
					if(childConditions[i][1] instanceof java.util.Date){
						actionDateString += dataFormat.format((java.util.Date)childConditions[i][1]); //Time
					} else {
						actionDateString += childConditions[i][1].toString();
					}
				}
			}
			
			if(parent1Conditions != null){
				addToList=true;
				for (int i = 0; i < parent1Conditions.length; i++) {
					if(!firstFulfilledCondition){
						reasonString += " / ";
						actionDateString += " / ";
					} else {
						firstFulfilledCondition=false;
					}
					reasonString += (String)parent1Conditions[i][0];  //Reason
					if(parent1Conditions[i][1] instanceof java.util.Date){
						actionDateString += dataFormat.format((java.util.Date)parent1Conditions[i][1]); //Time
					} else {
						actionDateString += parent1Conditions[i][1].toString();
					}				}
			}
			
			if(parent2Conditions != null){
				addToList=true;
				for (int i = 0; i < parent2Conditions.length; i++) {
					if(!firstFulfilledCondition){
						reasonString += " / ";
						actionDateString += " / ";
					} else {
						firstFulfilledCondition=false;
					}
					reasonString += (String)parent2Conditions[i][0];  //Reason
					if(parent2Conditions[i][1] instanceof java.util.Date){
						actionDateString += dataFormat.format((java.util.Date)parent2Conditions[i][1]); //Time
					} else {
						actionDateString += parent2Conditions[i][1].toString();
					}				}
			}
			
			
			// add to report
			if(addToList){ 
				ReportableData data = new ReportableData();
				
				data.addData(reasonField,reasonString);
				data.addData(actionDateField,actionDateString);
				
				//ChildData
				data.addData(childPersonalID,child.getPersonalID());
				data.addData(childLastName,child.getLastName());
				data.addData(childFirstName,child.getFirstName());
				reportData.add(data);
				
				Address childAddressEntiy = _communeUserService.getUsersMainAddress(child);
				String childAddressString = this.getAddressString(childAddressEntiy,_iwrb);
				if(childAddressString != null){
					data.addData(childAddress,childAddressString);
				}

				
				Collection coll = gRelationHome.findGroupsRelationshipsContainingGroupsAndStatus(communeGroup,child,GroupRelation.STATUS_ACTIVE);
				Iterator iterator = coll.iterator();
				if(iterator.hasNext()){
					GroupRelation rel = (GroupRelation)iterator.next();
					Timestamp time = rel.getInitiationDate();
					if(time != null){
						data.addData(childGroupInvitationDate,dataFormat.format(time));
					} else {
						data.addData(childGroupInvitationDate,"No time specified");
					}
					
				}
				
				if(parents != null){
					//Parent data
					//Collection parents = _familyLogic.getCustodiansFor(child);
					Iterator pIter = parents.iterator();
					//parent1
					if (pIter.hasNext()) {
						User parent = (User)pIter.next();
						ReportableData pData = new ReportableData();
					
						data.addData(parent1PersonalID,parent.getPersonalID());
						data.addData(parent1LastName,parent.getLastName());
						data.addData(parent1FirstName,parent.getFirstName());
						reportData.add(pData);
					
						Address parent1AddressEntiy = _communeUserService.getUsersMainAddress(child);
						String parent1AddressString = this.getAddressString(parent1AddressEntiy,_iwrb);
						if(parent1AddressString!=null) {
							data.addData(parent1Address,parent1AddressString);
						}

						Collection pColl = gRelationHome.findGroupsRelationshipsContainingGroupsAndStatus(communeGroup,parent,GroupRelation.STATUS_ACTIVE);
						Iterator pIterator = pColl.iterator();
						if(pIterator.hasNext()){
							GroupRelation rel = (GroupRelation)pIterator.next();
							Timestamp time = rel.getInitiationDate();
							if(time != null){
								data.addData(parent1GroupInvitationDate,dataFormat.format(time));
							} else {
								data.addData(parent1GroupInvitationDate,_iwrb.getLocalizedString("CommuneReportBusiness.no_time_specified","No time specified"));
							}
					
						}
					}
					
					//Parent2
					if (pIter.hasNext()) {
						User parent = (User)pIter.next();
						ReportableData pData = new ReportableData();
					
						data.addData(parent2PersonalID,parent.getPersonalID());
						data.addData(parent2LastName,parent.getLastName());
						data.addData(parent2FirstName,parent.getFirstName());
						reportData.add(pData);
					
						Address parent2AddressEntiy = _communeUserService.getUsersMainAddress(child);
						String parent2AddressString = this.getAddressString(parent2AddressEntiy,_iwrb);
						if(parent2AddressString!=null) {
							data.addData(parent2Address,parent2AddressString);
						}

									
						Collection pColl = gRelationHome.findGroupsRelationshipsContainingGroupsAndStatus(communeGroup,parent,GroupRelation.STATUS_ACTIVE);
						Iterator pIterator = pColl.iterator();
						if(pIterator.hasNext()){
							GroupRelation rel = (GroupRelation)pIterator.next();
							Timestamp time = rel.getInitiationDate();
							if(time != null){
								data.addData(parent2GroupInvitationDate,dataFormat.format(time));
							} else {
								data.addData(parent2GroupInvitationDate,_iwrb.getLocalizedString("CommuneReportBusiness.no_time_specified","No time specified"));
							}
					
						}
					}
				}
			}
		}
		
		reportData.addExtraHeaderParameter("label_current_date",_iwrb.getLocalizedString("CommuneReportBusiness.label_current_date","Current date"),"current_date",dateTimeFormat.format(IWTimestamp.getTimestampRightNow()));

		
		return reportData;
	}
	
	
	/**
	 * @param usr
	 * @return Returns array of Objects that is Object[x][2], null if no conditions are fulfilled
	 * Object[x][0] = condition Key (String), Object[x][1]=date of occurrance (java.util.Date or it's subclasses)
	 */
	private Object[][] getFulFilledConditionsForReportAndTheirOccurrenceTime(User usr,Date firstDateOfContitionInPeriode, Date lastDateOfConditionInPeriode, String resonPrifix) throws RemoteException, CreateException, FinderException{
		initializeBundlesIfNeeded();
//		initializeContitionStrings();
					
		java.util.Date deceased = hasDeceasedInTimePeriode(usr, firstDateOfContitionInPeriode, lastDateOfConditionInPeriode);
		boolean livesNotInCommune = livesNotInCommune(usr, firstDateOfContitionInPeriode, lastDateOfConditionInPeriode);
		java.util.Date hasMoved = hasMovedFromCommune(usr, firstDateOfContitionInPeriode, lastDateOfConditionInPeriode);
		java.util.Date hiddenAddress = hasChangedAddressToProtected(usr, firstDateOfContitionInPeriode, lastDateOfConditionInPeriode);
		
		int arraySize = 0;
		if(deceased != null){
			arraySize++;
		}
		
		if(livesNotInCommune){
			arraySize++;
		}
		
		if(hasMoved != null){
			arraySize++;
		}
		
		if(hiddenAddress != null){
			arraySize++;
		}
		
		Object[][] toReturn = null;
		if(arraySize>0){
			toReturn = new Object[arraySize][2];
		}
		
		
		int index = 0;
		if(deceased != null){
			toReturn[index][0] = resonPrifix +" "+_iwrb.getLocalizedString("CommuneReportBusiness.is_deceased","is deceased");
			toReturn[index][1] = deceased;
			index++;
		}

		if(livesNotInCommune){
			toReturn[index][0] = resonPrifix +" "+_iwrb.getLocalizedString("CommuneReportBusiness.does_not_live_in_the_commune","does not live in the commune");
			toReturn[index][1] = "-";
			index++;
		}

		if(hasMoved != null){
			toReturn[index][0] = resonPrifix +" "+_iwrb.getLocalizedString("CommuneReportBusiness.has_moved","has moved");
			toReturn[index][1] = hasMoved;
			index++;
		}

		if(hiddenAddress != null){
			toReturn[index][0] = resonPrifix +" "+_iwrb.getLocalizedString("CommuneReportBusiness.hidden_address","has protected address");
			toReturn[index][1] = hiddenAddress;
			index++;
		}
		
		return toReturn;
	}
	


//	/**
//	 * 
//	 */
//	private void initializeContitionStrings() {
//		// TODO Auto-generated method stub
//		
//	}

	/**
	 * @param usr
	 * @param firstDateOfContitionInPeriode
	 * @param lastDateOfConditionInPeriode
	 * @return Returns time of the event or null if condition is not fulfilled
	 */
	private java.util.Date hasChangedAddressToProtected(User usr, Date firstDateOfContitionInPeriode, Date lastDateOfConditionInPeriode) throws RemoteException, CreateException, FinderException {
		//Collection protectedUserColl = getProtectedUsersCollection(firstDateOfContitionInPeriode, lastDateOfConditionInPeriode);
		
		GroupRelationHome gRelationHome = ((GroupRelationHome)IDOLookup.getHome(GroupRelation.class));
		initializeCommuneUserBusinessIfNeeded();
		Group protectedUserGroup = _communeUserService.getRootProtectedCitizenGroup();
		Collection relToProtectedGroup = gRelationHome.findGroupsRelationshipsContainingGroupsAndStatus(protectedUserGroup,usr,GroupRelation.STATUS_ACTIVE);
		if(relToProtectedGroup != null){
			long first = firstDateOfContitionInPeriode.getTime();
			long last = lastDateOfConditionInPeriode.getTime()+millisecondsInOneDay-1;
			Iterator iter = relToProtectedGroup.iterator();
			while (iter.hasNext()) {
				GroupRelation element = (GroupRelation)iter.next();
				java.util.Date date = element.getInitiationDate();
				long current = date.getTime();
				if(first <= current && current <= last){
					return date;
				}
			}
		}		
		return null;
	}


	/**
	 * @param usr
	 * @param firstDateOfContitionInPeriode
	 * @param lastDateOfConditionInPeriode
	 * @return Returns time of the event or null if condition is not fulfilled
	 */
	private java.util.Date hasMovedFromCommune(User usr, Date firstDateOfContitionInPeriode, Date lastDateOfConditionInPeriode) throws RemoteException, CreateException, FinderException {
		Group rootCitizenGroup = _communeUserService.getRootCitizenGroup();
		
		Timestamp timestampFDOCIP = new Timestamp(firstDateOfContitionInPeriode.getTime());
		final long millisecondsInOneDay = 8640000;
		Timestamp timestampLDOCIP = new Timestamp(lastDateOfConditionInPeriode.getTime()+millisecondsInOneDay-1);

		
		String[] status = new String[1];
		status[0] = GroupRelation.STATUS_ACTIVE;
		try {
			Collection childs = ((GroupRelationHome)IDOLookup.getHome(GroupRelation.class)).findAllGroupsRelationshipsTerminatedWithinSpecifiedTimePeriod(rootCitizenGroup,usr,timestampFDOCIP,timestampLDOCIP,status);
			
			if(!childs.isEmpty()){
				Iterator iter =  childs.iterator();
				if(iter.hasNext()){
					return ((GroupRelation)iter.next()).getTerminationDate();
				}else {
					return null;
				}
			} else {
				return null;
			}
		} catch (FinderException e) {
			e.printStackTrace();
			return null;
		}
			
		
	}

	/**
	 * @param usr
	 * @param firstDateOfContitionInPeriode
	 * @param lastDateOfConditionInPeriode
	 * @return Returns true if user lives in nacka the whole time periode
	 */
	private boolean livesNotInCommune(User usr, Date firstDateOfContitionInPeriode, Date lastDateOfConditionInPeriode) throws RemoteException, CreateException, FinderException {
		// TODO Auto-generated method stub
		Group rootSpecialCitizenGroup = _communeUserService.getRootOtherCommuneCitizensGroup();
		
		Timestamp timestampFDOCIP = new Timestamp(firstDateOfContitionInPeriode.getTime());
		final long millisecondsInOneDay = 8640000;
		Timestamp timestampLDOCIP = new Timestamp(lastDateOfConditionInPeriode.getTime()+millisecondsInOneDay-1);

		
		String[] status = new String[1];
		status[0] = GroupRelation.STATUS_ACTIVE;
		
		Collection childs;
		try {
			childs = ((GroupRelationHome)IDOLookup.getHome(GroupRelation.class)).findAllGroupsRelationshipsValidWithinSpecifiedTimePeriod(rootSpecialCitizenGroup, usr, timestampFDOCIP, timestampLDOCIP, status);
		} catch (FinderException e) {
			e.printStackTrace();
			return false;
		}
			
		return !childs.isEmpty();
	}

	/**
	 * @param usr
	 * @param firstDateOfContitionInPeriode
	 * @param lastDateOfConditionInPeriode
	 * @return Returns time of the event or null if condition is not fulfilled
	 */
	private java.util.Date hasDeceasedInTimePeriode(User usr, Date firstDateOfConditionInPeriod, Date lastDateOfConditionInPeriod) {
		try {
			UserStatus status = getUserStatusService().getDeceasedUserStatus((Integer)usr.getPrimaryKey());
			if (status == null)
				return null;
			Timestamp deceasedDate = status.getDateFrom();
			if (firstDateOfConditionInPeriod.getTime() <= deceasedDate.getTime() && deceasedDate.getTime() <= lastDateOfConditionInPeriod.getTime())
				return deceasedDate;
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (EJBException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private UserStatusBusiness getUserStatusService() throws RemoteException {
		return (UserStatusBusiness)getServiceInstance(UserStatusBusiness.class);
	}
	
	private String getAddressString(Address addressEntiy, IWResourceBundle iwrb ){
		if(addressEntiy!=null){
			String stName = addressEntiy.getStreetName();
			String number = addressEntiy.getStreetNumber();
			String postalCode = iwrb.getLocalizedString("CommuneReportBusiness.not_available","N/A");
			PostalCode pCodeObj = addressEntiy.getPostalCode();
			String city = addressEntiy.getCity();
			if(pCodeObj!=null){
				postalCode = pCodeObj.getPostalCode();
			} else if(city == null){
				String childAddressString = stName+((number==null)?"":(" "+number));
				return childAddressString;	
			}
			
			String childAddressString = stName+((number==null)?"":(" "+number));
			return childAddressString+", "+postalCode+" "+city;	
									
		}
		return "";
	}
	

}
