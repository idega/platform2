/*
 * $Id: ConditionBMPBean.java,v 1.4 2003/09/06 22:44:09 kjell Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 * 
 */
package se.idega.idegaweb.commune.accounting.regulations.data;
    
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;

/**
 * Holds Conditions for the Regulation
 * 
 * @see se.idega.idegaweb.commune.accounting.regulations.data.RegulationBMPbean# 
 * <p>
 * $Id: ConditionBMPBean.java,v 1.4 2003/09/06 22:44:09 kjell Exp $
 * 
 * @author <a href="http://www.lindman.se">Kjell Lindman</a>
 * @version $Revision: 1.4 $
 */
public class ConditionBMPBean extends GenericEntity implements Condition {
	
	private static final String ENTITY_NAME = "cacc_conditions";
	private static final String COLUMN_REGULATION_ID = "regulation_id";
	private static final String COLUMN_CONDITION_ID = "condition_id";
	private static final String COLUMN_INTERVAL_ID = "interval_id";
	private static final String COLUMN_INDEX = "condition_index";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_REGULATION_ID, "Regulation ID", true, true, Integer.class);
		addAttribute(COLUMN_CONDITION_ID, "Condition ID", true, true, Integer.class);
		addAttribute(COLUMN_INTERVAL_ID, "Intervall ID", true, true, Integer.class);
		addAttribute(COLUMN_INDEX, "Index", true, true, Integer.class);
//		setAsPrimaryKey (COLUMN_REGULATION_ID, true);
	}

	public void setRegulationID(int id) { 
		setColumn(COLUMN_REGULATION_ID, id); 
	}
	
	public int getRegulationID() {
		return (int) getIntColumnValue(COLUMN_REGULATION_ID);
	}

	public void setConditionID(int id) { 
		setColumn(COLUMN_CONDITION_ID, id); 
	}
	
	public int getConditionID() {
		return (int) getIntColumnValue(COLUMN_CONDITION_ID);
	}

	public void setIntervalID(int id) { 
		setColumn(COLUMN_INTERVAL_ID, id); 
	}
	
	public int getIntervalID() {
		return (int) getIntColumnValue(COLUMN_INTERVAL_ID);
	}

	public void setIndex(int id) { 
		setColumn(COLUMN_INDEX, id); 
	}
	
	public int getIndex() {
		return (int) getIntColumnValue(COLUMN_INDEX);
	}

	public Collection ejbFindAllConditions() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		return idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindAllConditionsByRegulation(Regulation r) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhere();
		sql.append(COLUMN_REGULATION_ID);
		sql.appendEqualSign();
		sql.append(r.getPrimaryKey().toString());
		sql.appendOrderBy(COLUMN_INDEX);
		return idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindAllConditionsByRegulationID(int id) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhere();
		sql.append(COLUMN_REGULATION_ID);
		sql.appendEqualSign();
		sql.append(""+id);
		sql.appendOrderBy(COLUMN_INDEX);
		return idoFindPKsBySQL(sql.toString());
	}

	public Object ejbFindAllConditionsByRegulationAndIndex(int regId, int index) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhere();
		sql.append(COLUMN_REGULATION_ID);
		sql.appendEqualSign();
		sql.append(""+regId);
		sql.appendAnd();
		sql.append(COLUMN_INDEX);
		sql.appendEqualSign();
		sql.append(""+index);
		sql.appendOrderBy(COLUMN_INDEX);
		return idoFindOnePKByQuery(sql);
	}


	public Object ejbFindCondition(int id) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(getIDColumnName(), id);
		return idoFindOnePKByQuery(sql);
	}

}
