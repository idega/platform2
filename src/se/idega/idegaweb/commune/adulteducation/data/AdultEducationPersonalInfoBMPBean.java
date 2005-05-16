/*
 * Created on 2005-maj-03
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package se.idega.idegaweb.commune.adulteducation.data;


import com.idega.core.localisation.data.ICLanguage;
import com.idega.core.location.data.Country;
import com.idega.data.GenericEntity;

import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;
import com.idega.user.data.User;


/**
 * @author Malin
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class AdultEducationPersonalInfoBMPBean extends GenericEntity implements AdultEducationPersonalInfo{
	public static final String TABLE_NAME = "COMM_VUX_PERSONAL_INFO";
	
	public static final String COLUMN_IC_USER_ID = "IC_USER_ID";
		
	public static final String COLUMN_NAT_IC_COUNTRY_ID = "NAT_IC_COUNTRY_ID";
	public static final String COLUMN_IC_LANGUAGE_ID = "IC_LANGUAGE_ID";
	public static final String COLUMN_EDU_G_IC_COUNTRY_ID = "EDU_G_IC_COUNTRY_ID";
	
	public static final String COLUMN_NAT_THIS_COUNTRY = "NAT_THIS_COUNTRY";
	public static final String COLUMN_CIT_THIS_COUNTRY = "CIT_THIS_COUNTRY";	
	public static final String COLUMN_CIT_OTHER_COUNTRY = "CIT_OTHER_COUNTRY";
	public static final String COLUMN_EDU_A = "EDU_A";
	public static final String COLUMN_EDU_B = "EDU_B";
	public static final String COLUMN_EDU_C = "EDU_C";
	public static final String COLUMN_EDU_D = "EDU_D";
	public static final String COLUMN_EDU_E = "EDU_E";
	public static final String COLUMN_EDU_F = "EDU_F";
	
	public static final String COLUMN_EDU_G = "EDU_G";
	public static final String COLUMN_EDU_G_YEARS = "EDU_G_YEARS";
	public static final String COLUMN_EDU_H_A = "EDU_H_A";
	public static final String COLUMN_EDU_H_B = "EDU_H_B";
	public static final String COLUMN_EDU_H_C = "EDU_H_C";
	public static final String COLUMN_EDU_H_COMMUNE = "EDU_H_COMMUNE";
	public static final String COLUMN_FULL_TIME = "FULL_TIME";
	public static final String COLUMN_LANG_SFI = "LANG_SFI";
	public static final String COLUMN_LANG_SAS = "LANG_SAS";
	public static final String COLUMN_LANG_OTHER = "LANG_OTHER";
	public static final String COLUMN_STUDY_SUPPORT = "STUDY_SUPPORT";
	public static final String COLUMN_WORK_UNEMP = "WORK_UNEMP";
	public static final String COLUMN_WORK_EMP = "WORK_EMP";
	public static final String COLUMN_WORK_KICKED = "WORK_KICKED";
	public static final String COLUMN_WORK_OTHER = "WORK_OTHER";
	
	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
	addAttribute(getIDColumnName());

	//addAttribute(COLUMN_IC_USER_ID, "user id", true, true, Integer.class);
	
	addManyToOneRelationship(COLUMN_IC_USER_ID, User.class);
	addManyToOneRelationship(COLUMN_NAT_IC_COUNTRY_ID, Country.class);
	addManyToOneRelationship(COLUMN_IC_LANGUAGE_ID, ICLanguage.class);
	addManyToOneRelationship(COLUMN_EDU_G_IC_COUNTRY_ID, Country.class);
	//addAttribute(COLUMN_IC_LANGUAGE_ID, "language id", true, true, Integer.class);
	//addAttribute(COLUMN_EDU_G_IC_COUNTRY_ID, "studied country id", true, true, Integer.class);
	
	addAttribute(COLUMN_NAT_THIS_COUNTRY, "native this country", true, true, Boolean.class);
	addAttribute(COLUMN_CIT_THIS_COUNTRY, "citizen this country", true, true, Boolean.class);	
	addAttribute(COLUMN_CIT_OTHER_COUNTRY, "citizen other country", true, true, Boolean.class);
	
	addAttribute(COLUMN_EDU_A, "education A", true, true, Boolean.class);
	addAttribute(COLUMN_EDU_B, "education B", true, true, Boolean.class);
	addAttribute(COLUMN_EDU_C, "education C", true, true, Boolean.class);
	addAttribute(COLUMN_EDU_D, "education D", true, true, Boolean.class);
	addAttribute(COLUMN_EDU_E, "education E", true, true, Boolean.class);
	addAttribute(COLUMN_EDU_F, "education F", true, true, String.class);
	addAttribute(COLUMN_EDU_G, "education G", true, true, String.class);
	addAttribute(COLUMN_EDU_G_YEARS, "education years", true, true, Integer.class);
	addAttribute(COLUMN_EDU_H_A, "education H A", true, true, Boolean.class);
	addAttribute(COLUMN_EDU_H_B, "education H B", true, true, Boolean.class);
	addAttribute(COLUMN_EDU_H_C, "education H C", true, true, Boolean.class);
	addAttribute(COLUMN_EDU_H_COMMUNE, "education H Commune", true, true, String.class);
	addAttribute(COLUMN_FULL_TIME, "full time", true, true, Boolean.class);
	addAttribute(COLUMN_LANG_SFI, "language sfi", true, true, Boolean.class);
	addAttribute(COLUMN_LANG_SAS, "language sas", true, true, Boolean.class);
	addAttribute(COLUMN_LANG_OTHER, "language other", true, true, Boolean.class);
	addAttribute(COLUMN_STUDY_SUPPORT, "study support", true, true, Boolean.class);
	addAttribute(COLUMN_WORK_UNEMP, "work unemployed", true, true, Boolean.class);
	addAttribute(COLUMN_WORK_EMP, "work employed", true, true, Boolean.class);
	addAttribute(COLUMN_WORK_KICKED, "work fired", true, true, Boolean.class);
	addAttribute(COLUMN_WORK_OTHER, "work other", true, true, String.class);
	
	}
	
	public String getIDColumnName(){
		return TABLE_NAME;
	}
	
	public String getEntityName() {
		return TABLE_NAME;
	}

	public Integer ejbFindByUserId(Integer userId) throws javax.ejb.FinderException {
		//return idoFindPKsBySQL("select * from " + getEntityName() + " where " + COLUMN_IC_USER_ID + " = " + userId);
		Table table = new Table(this);		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn());
		query.addCriteria(new MatchCriteria(table, COLUMN_IC_USER_ID, MatchCriteria.EQUALS, userId));
		return (Integer) idoFindOnePKByQuery(query);		
	}
	
	
	public int getIcUserID() {
		return getIntColumnValue(COLUMN_IC_USER_ID);
	}

	public void setIcUserID(int icUserID) {
		setColumn(COLUMN_IC_USER_ID, icUserID);
	}
	
	public int getNativeCountryID() {
		return getIntColumnValue(COLUMN_NAT_IC_COUNTRY_ID);
	}
	
	public Country getNativeCountry() {
		return (Country) getColumnValue(COLUMN_NAT_IC_COUNTRY_ID);
	}

	public void setNativeCountryID(int nativeCountryId) {
		setColumn(COLUMN_NAT_IC_COUNTRY_ID, nativeCountryId);
	}
	
	
	
	public int getIcLanguageID() {
		return getIntColumnValue(COLUMN_IC_LANGUAGE_ID);
	}
	
	public ICLanguage getICLanguage() {
		return (ICLanguage) getColumnValue(COLUMN_IC_LANGUAGE_ID);
	}

	public void setIcLanguageID(int icLanguageId) {
		setColumn(COLUMN_IC_LANGUAGE_ID, icLanguageId);
	}
	
	public int getEducationCountryID() {
		return getIntColumnValue(COLUMN_EDU_G_IC_COUNTRY_ID);
	}
	
	public Country getEducationCountry() {
		return (Country) getColumnValue(COLUMN_EDU_G_IC_COUNTRY_ID);
	}

	public void setEducationCountryID(int educationCountryId) {
		setColumn(COLUMN_EDU_G_IC_COUNTRY_ID, educationCountryId);
	}
	
	public boolean getNativeThisCountry() {
		return getBooleanColumnValue(COLUMN_NAT_THIS_COUNTRY);
	}

	public void setNativeThisCountry(boolean nativeThisCountry) {
		setColumn(COLUMN_NAT_THIS_COUNTRY, nativeThisCountry);
	}
	
	public boolean getCitizenThisCountry() {
		return getBooleanColumnValue(COLUMN_CIT_THIS_COUNTRY);
	}

	public void setCitizenThisCountry(boolean citizenThisCountry) {
		setColumn(COLUMN_CIT_THIS_COUNTRY, citizenThisCountry);
	}
	
	public boolean getCitizenOtherCountry() {
		return getBooleanColumnValue(COLUMN_CIT_OTHER_COUNTRY);
	}

	public void setCitizenOtherCountry(boolean citizenOtherCountry) {
		setColumn(COLUMN_CIT_OTHER_COUNTRY, citizenOtherCountry);
	}
	
	public boolean getEduA() {
		return getBooleanColumnValue(COLUMN_EDU_A);
	}

	public void setEduA(boolean edu_A) {
		setColumn(COLUMN_EDU_A, edu_A);
	}
	
	public boolean getEduB() {
		return getBooleanColumnValue(COLUMN_EDU_B);
	}

	public void setEduB(boolean edu_B) {
		setColumn(COLUMN_EDU_B, edu_B);
	}
	
	public boolean getEduC() {
		return getBooleanColumnValue(COLUMN_EDU_C);
	}

	public void setEduC(boolean edu_C) {
		setColumn(COLUMN_EDU_C, edu_C);
	}
	
	public boolean getEduD() {
		return getBooleanColumnValue(COLUMN_EDU_D);
	}

	public void setEduD(boolean edu_D) {
		setColumn(COLUMN_EDU_D, edu_D);
	}
	public boolean getEduE() {
		return getBooleanColumnValue(COLUMN_EDU_E);
	}

	public void setEduE(boolean edu_E) {
		setColumn(COLUMN_EDU_E, edu_E);
	}
	
	public String getEduF() {
		return getStringColumnValue(COLUMN_EDU_F);
	}

	public void setEduF(String edu_F) {
		setColumn(COLUMN_EDU_F, edu_F);
	}
	
	public String getEduG() {
		return getStringColumnValue(COLUMN_EDU_G);
	}

	public void setEduG(String edu_G) {
		setColumn(COLUMN_EDU_G, edu_G);
	}
	
	public int getEduGCountryID() {
		return getIntColumnValue(COLUMN_EDU_G_IC_COUNTRY_ID);
	}

	public Country getEduGCountry() {
		return (Country) getColumnValue(COLUMN_EDU_G_IC_COUNTRY_ID);
	}

	public void setEduGCountryID(int edu_countryid) {
		setColumn(COLUMN_EDU_G_IC_COUNTRY_ID, edu_countryid);
	}
	
	public int getEduGYears() {
		return getIntColumnValue(COLUMN_EDU_G_YEARS);
	}

	public void setEduGYears(int edu_years) {
		setColumn(COLUMN_EDU_G_YEARS, edu_years);
	}
	
	public boolean getEduHA() {
		return getBooleanColumnValue(COLUMN_EDU_H_A);
	}

	public void setEduHA(boolean edu_HA) {
		setColumn(COLUMN_EDU_H_A, edu_HA);
	}
	
	public boolean getEduHB() {
		return getBooleanColumnValue(COLUMN_EDU_H_B);
	}

	public void setEduHB(boolean edu_HB) {
		setColumn(COLUMN_EDU_H_B, edu_HB);
	}
	
	public boolean getEduHC() {
		return getBooleanColumnValue(COLUMN_EDU_H_C);
	}

	public void setEduHC(boolean edu_HC) {
		setColumn(COLUMN_EDU_H_C, edu_HC);
	}
	
	public String getEduHCommune() {
		return getStringColumnValue(COLUMN_EDU_H_COMMUNE);
	}

	public void setEduHCommune(String edu_HCommune) {
		setColumn(COLUMN_EDU_H_COMMUNE, edu_HCommune);
	}
	
	public boolean getFulltime() {
		return getBooleanColumnValue(COLUMN_FULL_TIME);
	}

	public void setFulltime(boolean fulltime) {
		setColumn(COLUMN_FULL_TIME, fulltime);
	}
	
	public boolean getLangSFI() {
		return getBooleanColumnValue(COLUMN_LANG_SFI);
	}

	public void setLangSFI(boolean langsfi) {
		setColumn(COLUMN_LANG_SFI, langsfi);
	}
	
	public boolean getLangSAS() {
		return getBooleanColumnValue(COLUMN_LANG_SAS);
	}

	public void setLangSAS(boolean langsas) {
		setColumn(COLUMN_LANG_SAS, langsas);
	}
	
	public boolean getLangOTHER() {
		return getBooleanColumnValue(COLUMN_LANG_OTHER);
	}

	public void setLangOTHER(boolean langother) {
		setColumn(COLUMN_LANG_OTHER, langother);
	}
	
	public boolean getStudySupport() {
		return getBooleanColumnValue(COLUMN_STUDY_SUPPORT);
	}

	public void setStudySupport(boolean studySupport) {
		setColumn(COLUMN_STUDY_SUPPORT, studySupport);
	}
	
	public boolean getWorkEmploy() {
		return getBooleanColumnValue(COLUMN_WORK_EMP);
	}

	public void setWorkEmploy(boolean workEmpl) {
		setColumn(COLUMN_WORK_EMP, workEmpl);
	}
	
	public boolean getWorkUnEmploy() {
		return getBooleanColumnValue(COLUMN_WORK_UNEMP);
	}

	public void setWorkUnEmploy(boolean workUnEmpl) {
		setColumn(COLUMN_WORK_UNEMP, workUnEmpl);
	}
	
	public boolean getWorkKicked() {
		return getBooleanColumnValue(COLUMN_WORK_KICKED);
	}

	public void setWorkKicked(boolean workKicked) {
		setColumn(COLUMN_WORK_KICKED, workKicked);
	}
	
	public String getWorkOther() {
		return getStringColumnValue(COLUMN_WORK_OTHER);
	}

	public void setWorkOther(String workOther) {
		setColumn(COLUMN_WORK_OTHER, workOther);
	}
	
	
	
}
