/*
 * Created on 30.7.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.datareport.data;

import java.sql.SQLException;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.dataquery.data.Query;
import com.idega.core.data.ICFile;
import com.idega.core.data.ICObjectInstance;
import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;

/**
 * Title:		Report
 * Description:
 * Copyright:	Copyright (c) 2003
 * Company:		idega Software
 * @author		2003 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */
public class ReportTranscriptionBMPBean extends GenericEntity implements ReportTranscription {
	
	
	public static String getQueryColumnName(){return "query_id";}
	public static String getLayoutColumnName(){return "layout_id";}
	public static String getICObjectInstanceIDColumnName(){return "obj_inst_id";}
	
	/**
	 * 
	 */
	public ReportTranscriptionBMPBean() {
		super();
	}

	/**
	 * @param id
	 * @throws SQLException
	 */
	public ReportTranscriptionBMPBean(int id) throws SQLException {
		super(id);
	}

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return "dr_report_transcription";
	}

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addManyToOneRelationship(getQueryColumnName(),Query.class);
		addManyToOneRelationship(getLayoutColumnName(),ICFile.class);
		addManyToOneRelationship(getICObjectInstanceIDColumnName(),ICObjectInstance.class);
	}
	
	
	//setters//
	public void setQuery(Query query){
		setColumn(getQueryColumnName(),query);
	}
	
	public void setLayout(ICFile layout){
		setColumn(getLayoutColumnName(),layout);
	}
		
	public void setObjectInstance(ICObjectInstance instance){
		setColumn(getICObjectInstanceIDColumnName(),instance);
	}
			
	
	//getters//
	public Query getQuery(){
		return (Query)getColumnValue(getQueryColumnName());
	}

	public ICFile getLayout(){
		return (ICFile)getColumnValue(getLayoutColumnName());
	}
	
	public ICObjectInstance getObjectInstance(){
		return (ICObjectInstance)getColumnValue(getICObjectInstanceIDColumnName());
	}
	
	
	
	
	//Finders//
	/**
	 * @param instance
	 * @return Returns ReportTranscription if some record is found, else null
	 * @throws FinderException
	 */
	public ReportTranscription ejbHomeGetReportTranscriptionForObjectInstance(ICObjectInstance instance) throws FinderException{
		IDOQuery query = idoQueryGetSelect();
		query.appendWhereEquals(getICObjectInstanceIDColumnName(),instance.getPrimaryKey());
		
		Collection coll = idoFindPKsByQuery(query,1);
		if (!coll.isEmpty()) {
			return (ReportTranscription)coll.iterator().next();
		} else {
			return null;
		}
	}

}
