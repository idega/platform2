package com.idega.block.survey.data;


import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.core.category.data.ICInformationCategory;
import com.idega.core.category.data.ICInformationFolder;
import com.idega.core.localisation.data.ICLocale;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOQuery;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.util.IWTimestamp;
import com.idega.user.data.User;


/**
 * Title:		SurveyBMPBean
 * Description:
 * Copyright:	Copyright (c) 2004
 * Company:		idega Software
 * @author		2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */

public class SurveyEntityBMPBean extends com.idega.data.GenericEntity implements SurveyEntity {

	private HashMap storeMap = new HashMap();

	public static final String COLUMNNAME_CREATION_LOCALE = "CREATION_LOCALE";	

	private final static String DELETED_COLUMN = "DELETED";
	private final static String DELETED_BY_COLUMN = "DELETED_BY";
	private final static String DELETED_WHEN_COLUMN = "DELETED_WHEN";
	public final static String DELETED = "Y";
	public final static String NOT_DELETED = "N";


	public SurveyEntityBMPBean() {
		super();
	}

	public SurveyEntityBMPBean(int id) throws SQLException {
		super(id);
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(getColumnNameName(), "Name", true, true, String.class);
		addAttribute(getColumnNameDescription(), "Description", true, true, String.class);
		addAttribute(getColumnNameStartTime(), "Begins", true, true, Timestamp.class);
		addAttribute(getColumnNameEndTime(), "Ends", true, true, Timestamp.class);
		
		addManyToOneRelationship(COLUMNNAME_CREATION_LOCALE, "Locale id", ICLocale.class);

		addAttribute(DELETED_COLUMN, "Deleted", true, true, String.class, 1);
		addAttribute(DELETED_BY_COLUMN, "Deleted by", true, true, Integer.class, "many-to-one", User.class);
		addAttribute(DELETED_WHEN_COLUMN, "Deleted when", true, true, Timestamp.class);
		
		this.addManyToOneRelationship(getColumnNameFolderID(), "Info Folder", ICInformationFolder.class);
		this.addManyToOneRelationship(getColumnNameCatID(), "Category", ICInformationCategory.class);

		
		addManyToManyRelationShip(SurveyQuestion.class, "SU_SURVEY_SURVEY_QUESTION");
	}

	public static String getEntityTableName() {
		return "SU_SURVEY";
	}

	public static String getColumnNameID() {
		return "SU_SURVEY_ID";
	}
	
	public static String getColumnNameName() {
		return "NAME";
	}
	
	public static String getColumnNameDescription() {
		return "DESCRIPTION";
	}

	public static String getColumnNameStartTime() {
		return "START_TIME";
	}

	public static String getColumnNameEndTime() {
		return "END_TIME";
	}
	
	public static String getColumnNameFolderID() { 
		return "IC_INFO_FOLDER_ID"; 
	}

	public static String getColumnNameCatID() {
		return "IC_INFO_CATEGORY_ID";
	}


	public String getIDColumnName() {
		return getColumnNameID();
	}

	public String getEntityName() {
		return getEntityTableName();
	}
	
	public String getName() {
		return getStringColumnValue(getColumnNameName());
	}
	
	public String getDescription() {
		return getStringColumnValue(getColumnNameDescription());
	}
		
	public void setName(String name) {
		setColumn(getColumnNameName(), name);
	}
	
	public void setDescription(String description) {
		setColumn(getColumnNameDescription(), description);
	}

	public Timestamp getStartTime() {
		return (Timestamp)getColumnValue(getColumnNameStartTime());
	}

	public Timestamp getEndTime() {
		return (Timestamp)getColumnValue(getColumnNameEndTime());
	}
	
	public void setStartTime(Timestamp time) {
		setColumn(getColumnNameStartTime(),time);
	}

	public void setEndTime(Timestamp time) {
		setColumn(getColumnNameEndTime(),time);
	}
	
	public void addQuestion(SurveyQuestion question) throws IDOAddRelationshipException{
		idoAddTo(question);
	}
	
	public void removeQuestion(SurveyQuestion question) throws IDORemoveRelationshipException{
		idoRemoveFrom(question);
	}
	
	public int getFolderID() {
		return getIntColumnValue(getColumnNameFolderID());
	}

	public int getCategoryID() {
		return getIntColumnValue(getColumnNameCatID());
	}
	
	public void setFolder(ICInformationFolder folder) {
		setColumn(getColumnNameFolderID(),folder);
	}

	public void setCategoryID(ICInformationCategory categoryID) {
		setColumn(getColumnNameCatID(),categoryID);
	}


	public Collection ejbFindActiveSurveys(ICInformationFolder folder, Timestamp time) throws FinderException{
		IDOQuery query = idoQueryGetSelect();
		
		query.appendWhereEquals(getColumnNameFolderID(),folder);
		query.appendAnd();
		query.append(getColumnNameStartTime());
		query.appendLessThanOrEqualsSign();
		query.append(time);
		query.appendAnd();
			IDOQuery querypart2 = idoQuery();
			querypart2.append(getColumnNameEndTime());
			querypart2.appendIsNull();
			querypart2.appendOr();
			querypart2.append(getColumnNameEndTime());
			querypart2.appendGreaterThanOrEqualsSign();
			querypart2.append(time);
			/*querypart2.appendOr();
			querypart2.append(getColumnNameEndTime());
			querypart2.appendLessThanOrEqualsSign();
			querypart2.append("0000-00-00");*/				//MySQL Fix ??	
			query.appendWithinParentheses(querypart2);
		query.appendOrderBy(getColumnNameStartTime());
		
		//System.out.println(query.toString());
		
		return idoFindPKsByQuery(query);		
	}
	
	public Collection getSurveyQuestions() throws IDORelationshipException{
		return idoGetRelatedEntities(SurveyQuestion.class);
	}

	public void setCreationLocale(ICLocale locale){
		setColumn(COLUMNNAME_CREATION_LOCALE,locale);
	}
	
	public ICLocale getCreationLocale(){
		return (ICLocale)getColumnValue(COLUMNNAME_CREATION_LOCALE);
	}
	
	
	public void store(){
		super.store();
		Collection translations = storeMap.values();
		for (Iterator iter = translations.iterator(); iter.hasNext();) {
			SurveyEntityTranslation element = (SurveyEntityTranslation)iter.next();
			element.setTransletedEntity(this);
			element.store();
		}
	}



	/**
	 *
	 */
	public void setRemoved(User user){
		setColumn(DELETED_COLUMN, DELETED);
		setDeletedWhen(IWTimestamp.getTimestampRightNow());
		setDeletedBy(user);

		super.store();
	}

	/**
	 *
	 */
	private void setDeletedBy(User user) {
		setColumn(DELETED_BY_COLUMN, user);
	}
	
	/**
	 *
	 */
	private void setDeletedWhen(Timestamp when) {
		setColumn(DELETED_WHEN_COLUMN, when);
	}

}
