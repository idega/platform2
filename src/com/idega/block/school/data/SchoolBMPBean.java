package com.idega.block.school.data;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.idega.block.text.business.TextBusiness;
import com.idega.block.text.business.TextFinder;
import com.idega.block.text.data.LocalizedText;
import com.idega.block.text.data.TxText;
import com.idega.block.text.data.TxTextHome;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileHome;
import com.idega.core.location.data.Commune;
import com.idega.core.location.data.CommuneHome;
import com.idega.core.location.data.Country;
import com.idega.data.GenericEntity;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOException;
import com.idega.data.IDOLegacyEntity;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDOQuery;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.data.User;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class SchoolBMPBean extends GenericEntity implements School, IDOLegacyEntity {

	public final static String SCHOOL = "sch_school";
	public final static String NAME = "school_name";
	public final static String ADDRESS = "school_address";
	public final static String INFO = "school_info";
	public final static String HEADMASTER = "headmaster_group_id";
	public final static String SCHOOLTYPE = "sch_school_type_id";
	public final static String SCHOOLAREA = "sch_school_area_id";
	public final static String ZIPCODE = "zip_code";
	public final static String ZIPAREA = "zip_area";
	public final static String PHONE = "phone";
	public final static String KEYCODE = "key_kode";
	public final static String LONGITUDE = "longitude";
	public final static String LATITUDE = "latitude";
	/** Gimmi 4-5 Nov 2002 */
	public final static String FAX = "fax_nr";
	public final static String WEB_PAGE = "web_page";
	//public final static String MANAGEMENT_TYPE_ID = "managment_type";
	public final static String MANAGEMENT_TYPE = "management_type";
	public final static String HEADMASTER_USER_ID = "headmaster_user_id";
	public final static String ASSISTANT_HEADMASTER_GROUP_ID = "assistant_hm_grp_id";
	public final static String MAP_URL = "map_url";
	/** Kelly 13-14 May 2003 */
	public final static String ACTIVITY = "activity";
	public final static String OPEN_HOURS = "open_hours";
		/** Laddi 3 Sep 2003 */
	public final static String COMMUNE = "commune";
	/** Anders 15 Sep 2003 */
	public final static String ORGANIZATION_NUMBER = "organization_number";
	public final static String EXTRA_PROVIDER_ID = "extra_provider_id";
	public final static String TERMINATION_DATE = "termination_date";
	public final static String COUNTRY = "country"; // Not connected to commune
	public final static String CENTRALIZED_ADMINISTRATION = "centralized_administration";
	public final static String COMPENSATION_BY_INVOICE = "comp_by_invoice";

	public void initializeAttributes() {
		this.addAttribute(getIDColumnName());
		//this.addAttribute(SCHOOLTYPE,"Schooltype",true,true,Integer.class,this.MANY_TO_ONE,SchoolType.class);
		this.addAttribute(SCHOOLAREA, "Schoolarea", true, true, Integer.class, MANY_TO_ONE, SchoolArea.class);
		this.addAttribute(NAME, "Schoolname", true, true, String.class);
		this.addAttribute(INFO, "Info", true, true, String.class, 4000);
		this.addAttribute(ADDRESS, "Address", true, true, String.class, 100);
		this.addAttribute(ZIPAREA, "Ziparea", true, true, String.class, 20);
		this.addAttribute(ZIPCODE, "Zipcode", true, true, String.class, 20);
		this.addAttribute(PHONE, "phone", true, true, String.class, 20);
		this.addAttribute(KEYCODE, "keycode", true, true, String.class, 20);
		this.addAttribute(LATITUDE, "latitude", true, true, String.class, 20);
		this.addAttribute(LONGITUDE, "longitude", true, true, String.class, 20);
		this.addAttribute(HEADMASTER, "Headmaster", true, true, Integer.class, MANY_TO_ONE, Group.class);
		/** Gimmi 4-5 Nov 2002 */
		this.addAttribute(FAX, "fax", true, true, String.class, 20);
		this.addAttribute(WEB_PAGE, "web_page", true, true, String.class, 500);
		/** Laddi 3 Sep 2003 */
		//this.addAttribute(MANAGEMENT_TYPE_ID, "management_type", true, true, Integer.class);
		addManyToOneRelationship(MANAGEMENT_TYPE, SchoolManagementType.class);		
		this.addAttribute(HEADMASTER_USER_ID, "headmaster user id", true, true, Integer.class, MANY_TO_ONE, User.class);
		this.addAttribute(ASSISTANT_HEADMASTER_GROUP_ID, "assistant headmaster group id", true, true, Integer.class, MANY_TO_ONE, Group.class);
		this.addAttribute(MAP_URL, "url to map", true, true, String.class,500);
		/** Kelly 13-14 May 2003 */
		this.addAttribute(ACTIVITY, "The schools activity", true, true, String.class, 256);
		this.addAttribute(OPEN_HOURS, "The school open hours", true, true, String.class, 256);
		this.addManyToManyRelationShip(SchoolType.class);
		this.addManyToManyRelationShip(SchoolYear.class);
		// Grimur 16.10.2002
		this.addManyToManyRelationShip(LocalizedText.class); // in for backwards compatability
		this.addManyToManyRelationShip(ICFile.class);
		// Gimmi 27.12.2002
		this.addManyToManyRelationShip(TxText.class);		
		addManyToOneRelationship(COMMUNE, Commune.class);		
		// Anders 15 Sep 2003
		this.addAttribute(ORGANIZATION_NUMBER, "organisationsnummer", true, true, String.class, 20);
		this.addAttribute(EXTRA_PROVIDER_ID, "user supplied provider id", true, true, String.class, 20);
		this.addAttribute(TERMINATION_DATE, "termination date", true, true, Date.class);
		addManyToOneRelationship(COUNTRY, Country.class);
		this.addAttribute(CENTRALIZED_ADMINISTRATION, "has provider centralized administration", true, true, Boolean.class);		
		this.addAttribute(COMPENSATION_BY_INVOICE, "Compensation by invoice", true, true, Boolean.class);
	}
	public String getEntityName() {
		return SCHOOL;
	}

	public String getName() {
		return getSchoolName();
	}

	public int getSchoolAreaId() {
		return this.getIntColumnValue(SCHOOLAREA);
	}
	public SchoolArea getSchoolArea() {
		return (SchoolArea) getColumnValue(SCHOOLAREA);
	}
	public void setSchoolAreaId(int id) {
		this.setColumn(SCHOOLAREA, id);
	}
	public Object getCommunePK() {
		Object communeId = getColumnValue(COMMUNE);
		if (communeId == null) {
			try {
				System.out.print("[SchoolBMPBean] communeId not found ...");
				CommuneHome cHome = (CommuneHome) IDOLookup.getHome(Commune.class);
				Commune comm = cHome.findDefaultCommune();
				if (comm != null) {
					this.setCommunePK(comm.getPrimaryKey());
					this.store();
					System.out.print("set as defaul (pk="+comm.getPrimaryKey()+")");
					return comm.getPrimaryKey();
				}
				System.out.print("NOT set. (Default not found)");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return communeId;
	}
	public Commune getCommune() {
		return (Commune) getColumnValue(COMMUNE);
	}
	public void setCommunePK(Object pk) {
		this.setColumn(COMMUNE, pk);
	}
	public int getCountryId() {
		return this.getIntColumnValue(COUNTRY);
	}
	public Country getCountry() {
		return (Country) getColumnValue(COUNTRY);
	}
	public void setCountryId(int id) {
		this.setColumn(COUNTRY, id);
	}
	public String getManagementTypeId() {
		return this.getStringColumnValue(MANAGEMENT_TYPE);
	}
	public SchoolManagementType getManagementType() {
		return (SchoolManagementType) getColumnValue(MANAGEMENT_TYPE);
	}
	public void setManagementTypeId(String id) {
		this.setColumn(MANAGEMENT_TYPE, id);
	}
	public String getSchoolName() {
		return this.getStringColumnValue(NAME);
	}
	public void setSchoolName(String name) {
		this.setColumn(NAME, name);
	}
	public String getSchoolInfo() {
		return this.getStringColumnValue(INFO);
	}
	public void setSchoolInfo(String info) {
		this.setColumn(INFO, info);
	}
	public String getSchoolAddress() {
		return this.getStringColumnValue(ADDRESS);
	}
	public void setSchoolAddress(String address) {
		this.setColumn(ADDRESS, address);
	}

	public String getSchoolZipArea() {
		return this.getStringColumnValue(ZIPAREA);
	}
	public void setSchoolZipArea(String ziparea) {
		this.setColumn(ZIPAREA, ziparea);
	}

	public String getSchoolZipCode() {
		return this.getStringColumnValue(ZIPCODE);
	}
	public void setSchoolZipCode(String zipcode) {
		this.setColumn(ZIPCODE, zipcode);
	}

	public String getSchoolPhone() {
		return this.getStringColumnValue(PHONE);
	}
	public void setSchoolPhone(String phone) {
		this.setColumn(PHONE, phone);
	}

	public int getHeadmasterGroupId() {
		return this.getIntColumnValue(HEADMASTER);
	}

	public Group getHeadmasterGroup() throws RemoteException, FinderException {
		return ((GroupHome) IDOLookup.getHome(Group.class)).findByPrimaryKey(new Integer(getHeadmasterGroupId()));
	}

	public void setHeadmasterGroupId(int masterGroupId) {
		this.setColumn(HEADMASTER, masterGroupId);
	}

	public String getSchoolKeyCode() {
		return this.getStringColumnValue(KEYCODE);
	}
	public void setSchoolKeyCode(String code) {
		this.setColumn(KEYCODE, code);
	}
	public String getSchoolLatitude() {
		return this.getStringColumnValue(LATITUDE);
	}
	public void setSchoolLatitude(String lat) {
		this.setColumn(LATITUDE, lat);
	}
	public String getSchoolLongitude() {
		return this.getStringColumnValue(LONGITUDE);
	}
	public void setSchoolLongitude(String lon) {
		this.setColumn(LONGITUDE, lon);
	}
	public String getOrganizationNumber() {
		return this.getStringColumnValue(ORGANIZATION_NUMBER);
	}
	public void setOrganizationNumber(String orgNo) {
		this.setColumn(ORGANIZATION_NUMBER, orgNo);
	}
	public String getExtraProviderId() {
		return this.getStringColumnValue(EXTRA_PROVIDER_ID);
	}
	public void setExtraProviderId(String id) {
		this.setColumn(EXTRA_PROVIDER_ID, id);
	}
	public Date getTerminationDate() {
		return (Date) this.getColumnValue(TERMINATION_DATE);
	}
	public void setTerminationDate(Date date) {
		this.setColumn(TERMINATION_DATE, date);
	}
	public boolean getCentralizedAdministration() {
		boolean isCA = false;
		Boolean b = (Boolean) this.getColumnValue(CENTRALIZED_ADMINISTRATION);
		if (b != null) {
			isCA = b.booleanValue();
		}
		return isCA;
	}
	public void setCentralizedAdministration(boolean b) {
		this.setColumn(CENTRALIZED_ADMINISTRATION, b);
	}
	
	private Date getCurrentDate() {
		return new Date(System.currentTimeMillis());
	}
	
	public Collection ejbFindAllBySchoolType(Collection typeIds) throws javax.ejb.FinderException {
		if (typeIds == null || typeIds.size() < 1) {
			return null;
		}
		else {
			StringBuffer select = new StringBuffer("select distinct s.* from " + SCHOOL + " s,sch_school_sch_school_type m where m.sch_school_type_id in (");
			select.append(getIDOUtil().convertListToCommaseparatedString(typeIds));
			select.append(") and m.sch_school_id = s.sch_school_id");
			select.append(" and (termination_date is null or termination_date > '" + getCurrentDate() + "')");
			select.append(" order by s.").append(NAME);
			return super.idoFindPKsBySQL(select.toString());
		}
	}

	public Collection ejbFindAllBySchoolType(int typeId) throws javax.ejb.FinderException {
		String select = "select s.* from " + SCHOOL + " s,sch_school_sch_school_type m where m.sch_school_type_id = " + typeId + " and m.sch_school_id = s.sch_school_id " +
				" and (termination_date is null or termination_date > '" + getCurrentDate() + "')" +
				" order by s."+NAME;
		return super.idoFindPKsBySQL(select);
	}

	public Collection ejbFindAllBySchoolName(String schoolName) throws javax.ejb.FinderException {
		String select = "select * from " + SCHOOL + " where " + NAME + " = '" + schoolName + "'" +
				" and (termination_date is null or termination_date > '" + getCurrentDate() + "')";
		return super.idoFindPKsBySQL(select);
	}

	public Integer ejbFindBySchoolName(String schoolName) throws javax.ejb.FinderException {
		String select = "select * from " + SCHOOL + " where " + NAME + " = '" + schoolName + "'" +
				" and (termination_date is null or termination_date > '" + getCurrentDate() + "')";
		
		return (Integer)super.idoFindOnePKBySQL(select);
	}

	public Collection ejbFindAllCentralizedAdministrated() throws javax.ejb.FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this.getEntityName()).appendWhereEqualsQuoted(CENTRALIZED_ADMINISTRATION, "Y");
		sql.append(" and (termination_date is null or termination_date > '" + getCurrentDate() + "')");
		return super.idoFindPKsByQuery(sql);
	}
	
	public Collection ejbFindAllCentralizedAdministratedByType(Collection typeIds) throws javax.ejb.FinderException {
		if (typeIds == null || typeIds.size() < 1) {
			return null;
		}
		else {
			StringBuffer select = new StringBuffer("select distinct s.* from " + SCHOOL + " s,sch_school_sch_school_type m where m.sch_school_type_id in (");
			select.append(getIDOUtil().convertListToCommaseparatedString(typeIds));
			select.append(") and m.sch_school_id = s.sch_school_id");
			select.append(" and s."+CENTRALIZED_ADMINISTRATION+" like 'Y'");			
			select.append(" and (termination_date is null or termination_date > '" + getCurrentDate() + "')");
			select.append(" order by s.").append(NAME);
			return super.idoFindPKsBySQL(select.toString());
		}
	}

	public Collection ejbFindAllBySchoolArea(int areaId) throws javax.ejb.FinderException {
		String sql = "select * from " + SCHOOL + " where " + SCHOOLAREA + " = " + areaId + 
				" and (termination_date is null or termination_date > '" + getCurrentDate() + "')" +
				" order by " + NAME;
		return super.idoFindPKsBySQL(sql);
	}

	public Collection ejbFindAllSchools() throws javax.ejb.FinderException {
//		String sql = "select * from " + SCHOOL + " where " + 
//				" (termination_date is null or termination_date > '" + getCurrentDate() + "')" +
//				" order by upper(" + NAME + ")";
		return super.idoFindPKsBySQL("select * from " + SCHOOL + " order by upper("+NAME+")");
	}

	public Collection ejbFindAllSchoolsIncludingTerminated() throws javax.ejb.FinderException {
//		String sql = "select * from " + SCHOOL + " where " + 
//				" order by upper(" + NAME + ")";
		return super.idoFindPKsBySQL("select * from " + SCHOOL + " order by upper("+NAME+")");
	}

	public void addSchoolTypes(int[] ids) {
		try {
			super.addTo(SchoolType.class, ids);
		}
		catch (java.sql.SQLException sql) {
			sql.printStackTrace();
		}
	}

	public void addSchoolYears(int[] ids) {
		try {
			super.addTo(SchoolYear.class, ids);
		}
		catch (java.sql.SQLException sql) {

		}
	}

	public void addSchoolYear(SchoolYear year) throws IDOAddRelationshipException {
		super.idoAddTo(year);
	}

	public void addSchoolType(SchoolType type) throws IDOAddRelationshipException {
		super.idoAddTo(type);
	}
	
	public void addSchoolYearsRemoveOther(int[] ids) {
		try {
			super.removeFrom(SchoolYear.class);
		}
		catch (java.sql.SQLException ex) {
		}
		this.addSchoolYears(ids);
	}

	public void addSchoolTypesRemoveOther(int[] ids) {
		try {
			super.removeFrom(SchoolType.class);
		}
		catch (java.sql.SQLException ex) {
		}
		this.addSchoolTypes(ids);
	}

	public Collection findRelatedSchoolTypes() throws com.idega.data.IDORelationshipException {
		return super.idoGetRelatedEntities(SchoolType.class);
	}

	public Collection findRelatedSchoolYears() throws com.idega.data.IDORelationshipException {
		return super.idoGetRelatedEntities(SchoolYear.class);
	}

	public Collection ejbFindAllByAreaAndType(int area, int type) throws javax.ejb.FinderException {
		StringBuffer sql = new StringBuffer("select s.* ");
		sql.append(" from sch_school_area a, sch_school s, sch_school_type t, sch_school_sch_school_type m ");
		sql.append(" where a.sch_school_area_id = s.sch_school_area_id ");
		sql.append(" and t.sch_school_type_id = m.sch_school_type_id ");
		sql.append(" and s.sch_school_id = m.sch_school_id ");
		sql.append(" and t.sch_school_type_id = ");
		sql.append(type);
		sql.append(" and a.sch_school_area_id = ");
		sql.append(area);
		sql.append(" and (termination_date is null or termination_date > '" + getCurrentDate() + "')");
		sql.append(" order by s.").append(NAME);

		return super.idoFindPKsBySQL(sql.toString());

	}
	
	public Collection ejbFindAllByAreaAndTypeAndYear(int areaID, int typeID, int yearID) throws javax.ejb.FinderException {
		StringBuffer sql = new StringBuffer("select s.* ");
		sql.append(" from sch_school_sch_school_year ssy,sch_school_area a, sch_school s, sch_school_type t, sch_school_sch_school_type m ");
		sql.append(" where a.sch_school_area_id = s.sch_school_area_id ");
		sql.append(" and t.sch_school_type_id = m.sch_school_type_id ");
		sql.append(" and s.sch_school_id = m.sch_school_id ");
		sql.append(" and m.sch_school_id = ssy.sch_school_id ");
		sql.append(" and t.sch_school_type_id = ");
		sql.append(typeID);
		sql.append(" and a.sch_school_area_id = ");
		sql.append(areaID);
		sql.append(" and ssy.sch_school_year_id = ");
		sql.append(yearID);
		sql.append(" order by s.").append(NAME);

		return super.idoFindPKsBySQL(sql.toString());
		
	}

	public Collection ejbFindAllByAreaAndTypes(int area, Collection types) throws javax.ejb.FinderException {
		StringBuffer sql = new StringBuffer("select s.* ");
		sql.append(" from sch_school_area a, sch_school s, sch_school_type t, sch_school_sch_school_type m ");
		sql.append(" where a.sch_school_area_id = s.sch_school_area_id ");
		sql.append(" and t.sch_school_type_id = m.sch_school_type_id ");
		sql.append(" and s.sch_school_id = m.sch_school_id ");
		if (types != null && !types.isEmpty()) {
			sql.append(" and t.sch_school_type_id in (");
			sql.append(getIDOUtil().convertListToCommaseparatedString(types));
			sql.append(") ");
		}
		sql.append(" and a.sch_school_area_id = ");
		sql.append(area);
		sql.append(" and (termination_date is null or termination_date > '" + getCurrentDate() + "')");
		sql.append(" order by s.").append(NAME);

		return super.idoFindPKsBySQL(sql.toString());

	}

	public Collection ejbFindAllByCategory(SchoolCategory schoolCategory) throws javax.ejb.FinderException {
		StringBuffer sql = new StringBuffer("select s.* ");
		sql.append(" sch_school s, sch_school_type t, sch_school_sch_school_type m, sch_school_category c ");
		sql.append(" where s.sch_school_id = m.sch_school_id ");
		sql.append(" and m.sch_school_type_id = t.sch_school_type_id");
		sql.append(" and t.school_category = c.sch_school_category_id");
		sql.append(" and c.sch_school_category_id = "+schoolCategory.getPrimaryKey().toString());
		return super.idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindAllInHomeCommuneByCategory(SchoolCategory schoolCategory) throws IDOLookupException, EJBException, FinderException, CreateException {
		int homeCommunePK = ((Integer)((CommuneHome) IDOLookup.create(Commune.class)).findDefaultCommune().getPrimaryKey()).intValue();
		StringBuffer sql = new StringBuffer("select s.* ");
		sql.append(" sch_school s, sch_school_type t, sch_school_sch_school_type m, sch_school_category c ");
		sql.append(" where s.sch_school_id = m.sch_school_id ");
		sql.append(" and m.sch_school_type_id = t.sch_school_type_id");
		sql.append(" and t.school_category = c.sch_school_category_id");
		sql.append(" and c.sch_school_category_id = "+schoolCategory.getPrimaryKey().toString());
		sql.append("and s.commune = "+homeCommunePK);
		return super.idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindAllBySchoolGroup(Group schoolGroup) throws javax.ejb.FinderException, RemoteException {
		StringBuffer sql = new StringBuffer("Select s.* ");
		sql.append("  from sch_school s ");
		sql.append(" where s.headmaster_group_id in ( ");
		sql.append(" select r.ic_group_id from ic_group_relation r ");
		sql.append(" where r.ic_group_id in(select headmaster_group_id from sch_school ) ");
		sql.append(" and r.related_ic_group_id = ");
		sql.append(schoolGroup.getPrimaryKey().toString());
		sql.append(" ) ");
		sql.append(" and (termination_date is null or termination_date > '" + getCurrentDate() + "')");
		sql.append(" order by s.").append(NAME);
		return super.idoFindPKsBySQL(sql.toString());
	}
	
	public int ejbHomeGetNumberOfRelations(School school, SchoolYear year) throws IDOException {
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) from sch_school s, sch_school_sch_school_year middle");
		sql.append(" where middle.sch_school_ID = ").append(school.getPrimaryKey().toString());
		sql.append(" and middle.sch_school_year_ID = ").append(year.getPrimaryKey().toString());
		sql.append(" and middle.sch_school_id = s.sch_school_id");
		sql.append(" and (termination_date is null or termination_date > '" + getCurrentDate() + "')");
		return super.idoGetNumberOfRecords(sql.toString());
	}

	public Collection ejbFindAllPrivate() throws IDOLookupException, EJBException, FinderException{
		Integer managementType = (Integer)((SchoolManagementTypeHome) IDOLookup.getHome(SchoolManagementType.class)).findPrivateManagementType().getPrimaryKey();
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhereEquals(MANAGEMENT_TYPE,managementType.intValue());
		return idoFindPKsBySQL(sql.toString());

	}

	public LocalizedText getLocalizedText(int localeId) throws IDORelationshipException, RemoteException{
		Collection coll = getText();
		TxTextHome textHome = (TxTextHome) IDOLookup.getHome(TxText.class);
		if (coll != null && !coll.isEmpty()) {
			Iterator iter = coll.iterator();
			TxText text;
			try {
				text = textHome.findByPrimaryKey(iter.next());
				/* May want to use this...
				if (iter.hasNext()) {
					System.out.println("[SchoolBMPBean] Found too many localizedTexts, removing this one ("+text.getID()+") and trying again..");
					TextBusiness.deleteText(text.getID());
					return getLocalizedText(localeId);
				}*/
			} catch (FinderException e) {
				e.printStackTrace(System.err);
				return null;
			}
			LocalizedText lText = TextFinder.getContentHelper(text.getID(), localeId).getLocalizedText();
			return lText;
		}else {
			/** trying backwards compatability */
			try {
				LocalizedText text = TextFinder.getLocalizedText(this, localeId);
				if (text != null) {
					int lTextId = text.getID();
					TxText txText = TextBusiness.saveText(-1, lTextId, localeId, -1, -1, null, null, text.getHeadline(), text.getTitle(), text.getBody(), null, null);
					setText(txText);
					System.out.println("[SchoolBMPBean] : Backwards compatability for localized text (School = '"+getSchoolName()+"')");
					return TextFinder.getContentHelper(txText.getID(), localeId).getLocalizedText();
				}else {
					return TextFinder.getLocalizedText(this, localeId);
				}
//				return TextFinder.getLocalizedText(text, localeId);
			} catch (Exception e) {
				System.out.println("[SchoolBMPBean] : Backwards compatability for localized text (School = '"+getSchoolName()+"') - FAILED !!");
				e.printStackTrace(System.err);
				return TextFinder.getLocalizedText(this, localeId);
			}
		}

	}
	
	private Collection getText() throws IDORelationshipException {
		return this.idoGetRelatedEntities(TxText.class);	
	}
	
	private void setText(TxText text) throws IDOAddRelationshipException {
		this.idoAddTo(text);	
	}

	public void setLocalizedText(String text, int localeId) throws IDORelationshipException {
		Collection coll= getText();
		if (coll != null && !coll.isEmpty()) {
			try {
				Iterator iter = coll.iterator();
				TxTextHome textHome = (TxTextHome) IDOLookup.getHome(TxText.class);
				TxText txText = textHome.findByPrimaryKey(iter.next());
				LocalizedText lText = TextFinder.getContentHelper(txText.getID(), localeId).getLocalizedText();
				int lTextId = -1;
				if (lText != null) {
					lTextId = lText.getID();	
				}
//				boolean added = com.idega.block.text.business.TextBusiness.addLocalizedTextToTxText(text, txText);
				TextBusiness.saveText(txText.getID(), lTextId, localeId, -1, -1, null, null, null, null, text, null, null);
			} catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}else {
			System.out.println("[SchoolBMPBean] : setLocalizedText : backwards compatability");
			TxText txText = TextBusiness.saveText(-1, -1, localeId, -1, -1, null, null, null, null, text, null, null);
			idoAddTo(txText);
			this.idoRemoveFrom(LocalizedText.class);
		}
		//this.idoAddTo(text);
	}

	public String getSchoolFax() {
		return getStringColumnValue(FAX);
	}

	public void setSchoolFax(String fax) {
		setColumn(FAX, fax);
	}

	public String getSchoolWebPage() {
		return getStringColumnValue(WEB_PAGE);
	}

	public void setSchoolWebPage(String webPage) {
		setColumn(WEB_PAGE, webPage);
	}

	public String getMapUrl() {
		return getStringColumnValue(MAP_URL);
	}

	public void setMapUrl(String url) {
		setColumn(MAP_URL, url);
	}

	public String getActivity() {
		return getStringColumnValue(ACTIVITY);
	}

	public void setActivity(String activity) {
		setColumn(ACTIVITY, activity);
	}

	public String getOpenHours() {
		return getStringColumnValue(OPEN_HOURS);
	}

	public void setOpenHours(String openHours) {
		setColumn(OPEN_HOURS, openHours);
	}

	public String getSchoolManagementTypeString() {
		return getStringColumnValue(MANAGEMENT_TYPE);
	}

	public SchoolManagementType getSchoolManagementType() {
		return (SchoolManagementType) getColumnValue(MANAGEMENT_TYPE);
	}

	public void setSchoolManagementType(String managementType) {
		setColumn(MANAGEMENT_TYPE, managementType);
	}
	
	public boolean getHasCompensationByInvoice() {
		return getBooleanColumnValue(COMPENSATION_BY_INVOICE,false);
	}
	
	public void setHasCompensationByInvoice(boolean hasCompensation) {
		setColumn(COMPENSATION_BY_INVOICE,hasCompensation);
	}

	public Collection getImages() throws IDORelationshipException {
		return this.idoGetRelatedEntities(ICFile.class);
	}
	
	public Collection getSchoolTypes() throws IDORelationshipException {
		return this.idoGetRelatedEntities(SchoolType.class);
	}

	public void removeImages() throws IDORelationshipException {
		this.idoRemoveFrom(ICFile.class);
	}

	public void setImage(ICFile image) throws IDORelationshipException {
		removeImages();
		this.idoAddTo(image);
	}

	public void addImage(ICFile image) throws IDORelationshipException {
		this.idoAddTo(image);
	}
	
	public void setImages(Collection images) throws IDORelationshipException, RemoteException {
		this.idoRemoveFrom(ICFile.class);
		if (images != null) {
			Iterator iter = images.iterator();
			ICFile file;
			while (iter.hasNext()) {
				try {
					file = ((ICFileHome) IDOLookup.getHome(ICFile.class)).findByPrimaryKey(iter.next());
					addImage(file);
				}
				catch (FinderException e) {
					e.printStackTrace(System.err);
				}
				catch (IDORelationshipException e) {
				}
			}
		}
	}

	public int getHeadmasterUserId() {
		return getIntColumnValue(HEADMASTER_USER_ID);
	}

	public void setHeadmasterUserId(int userId) {
		setColumn(HEADMASTER_USER_ID, userId);
	}

	public int getAssistantHeadmasterGroupId() {
		return getIntColumnValue(ASSISTANT_HEADMASTER_GROUP_ID);
	}

	public void setAssistantHeadmasterGroupId(int groupId) {
		setColumn(ASSISTANT_HEADMASTER_GROUP_ID, groupId);
	}

	public static void main(String[] args) {
		System.out.println("hellu there");
	}
	
	public void removeFromClass(Class entityInterfaceClass) throws IDORemoveRelationshipException {
		this.idoRemoveFrom(entityInterfaceClass);	
	}
}