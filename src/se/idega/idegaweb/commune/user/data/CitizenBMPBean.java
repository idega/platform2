/*
 * Created on 19.8.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.user.data;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;
import se.idega.idegaweb.commune.school.data.SchoolChoice;

import com.idega.block.school.data.SchoolSeason;
import com.idega.data.IDOCompositPrimaryKeyException;
import com.idega.data.IDOEntityDefinition;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDOQuery;
import com.idega.data.IDOStoreException;
import com.idega.user.data.UserBMPBean;
import com.idega.util.ListUtil;

/**
 * Title:		CitizenBMPBean
 * Description:
 * Copyright:	Copyright (c) 2003
 * Company:		idega Software
 * @author		2003 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */
public class CitizenBMPBean extends UserBMPBean {

	/* (non-Javadoc)
	 * @see com.idega.data.IDOEntity#store()
	 */
	public void store() throws IDOStoreException {
		super.store();
	}
	
	public Collection ejbFindAllCitizensRegisteredToSchool(SchoolSeason season, Date firstBirthDateInPeriode, Date lastBirthDateInPeriode) throws FinderException, IDOLookupException{
		//select usr.* from ic_user usr, comm_sch_choice sch where usr.DATE_OF_BIRTH>='2002-03-01' AND usr.DATE_OF_BIRTH<='2002-03-02' and sch.child_id=usr.ic_user_id and sch.SCHOOL_SEASON_ID=season.getPrimaryKey()
		try {
			//preparing
		  	
			IDOEntityDefinition schChoiceDef = IDOLookup.getEntityDefinitionForClass(SchoolChoice.class);
			IDOEntityDefinition thisDef = this.getEntityDefinition();
			  	
			String[] tables = new String[2];
			String[] variables = new String[2];
			//table name
			tables[0] = thisDef.getSQLTableName();
			//as variable
			variables[0] = "usr";
			//table name
			tables[1] = schChoiceDef.getSQLTableName();
			//as variable
			variables[1] = "sch";
						  	
			//constructing query
			IDOQuery query = idoQuery();
			//select
			query.appendSelect();
			query.append(variables[0]);
			query.append(".* ");
			//from
			query.appendFrom(tables,variables);
			//where
			query.appendWhere();
			query.append(variables[0]);
			query.append(".");
			query.append(getColumnNameDateOfBirth());
			query.appendGreaterThanOrEqualsSign();
			query.append(firstBirthDateInPeriode);
			//and
			query.appendAnd();
			query.append(variables[0]);
			query.append(".");
			query.append(getColumnNameDateOfBirth());
			query.appendLessThanOrEqualsSign();
			query.append(lastBirthDateInPeriode);
			
			//and sch.child_id=usr.ic_user_id 
			query.appendAnd();
			query.append(variables[1]);
			query.append(".");
			query.append(schChoiceDef.findFieldByUniqueName(SchoolChoice.FIELD_CHILD).getSQLFieldName());
			query.appendEqualSign();
			query.append(variables[0]);
			query.append(".");
			query.append(thisDef.getPrimaryKeyDefinition().getField().getSQLFieldName());
			
			//and sch.SCHOOL_SEASON_ID="season.getPrimaryKey()"
			query.appendAnd();
			query.append(variables[1]);
			query.append(".");
			query.append(schChoiceDef.findFieldByUniqueName(SchoolChoice.FIELD_SCHOOL_SEASON).getSQLFieldName());
			query.appendEqualSign();
			query.append(season.getPrimaryKey());
			  	
			System.out.println("SQL -> "+this.getClass()+":"+query);
			return idoFindPKsByQuery(query); 
		  	
		} catch (IDOCompositPrimaryKeyException e) {
			e.printStackTrace();
			return ListUtil.getEmptyList();
		}
	}
	
	
	public Collection ejbFindAllCitizensRegisteredToChildCare(Date firstBirthDateInPeriode, Date lastBirthDateInPeriode, Date firstDateInRegistrationPeriode, Date lastDateInRegistrationPeriode) throws FinderException, IDOLookupException{
		//select usr.* from ic_user usr, comm_childcare care where usr.DATE_OF_BIRTH>='2002-03-01' AND usr.DATE_OF_BIRTH<='2002-03-02' and care.child_id=usr.ic_user_id and care.from_date <='lastDate' and (care.REJECTION_DATE is null or care.REJECTION_DATE > 'firstDate' )
		try {
			//preparing
		  	
			IDOEntityDefinition childCareAppDef = IDOLookup.getEntityDefinitionForClass(ChildCareApplication.class);
			IDOEntityDefinition thisDef = this.getEntityDefinition();
			  	
			String[] tables = new String[2];
			String[] variables = new String[2];
			//table name
			tables[0] = thisDef.getSQLTableName();
			//as variable
			variables[0] = "usr";
			//table name
			tables[1] = childCareAppDef.getSQLTableName();
			//as variable
			variables[1] = "care";
			  	
			//constructing query
			IDOQuery query = idoQuery();
			//select
			query.appendSelect();
			query.append(variables[0]);
			query.append(".* ");
			//from
			query.appendFrom(tables,variables);
			//where
			query.appendWhere();
			query.append(variables[0]);
			query.append(".");
			query.append(getColumnNameDateOfBirth());
			query.appendGreaterThanOrEqualsSign();
			query.append(firstBirthDateInPeriode);
			//and
			query.appendAnd();
			query.append(variables[0]);
			query.append(".");
			query.append(getColumnNameDateOfBirth());
			query.appendLessThanOrEqualsSign();
			query.append(lastBirthDateInPeriode);
			
			// and care.child_id=usr.ic_user_id 
			query.appendAnd();
			query.append(variables[1]);
			query.append(".");
			query.append(childCareAppDef.findFieldByUniqueName(ChildCareApplication.FIELD_CHILD_ID).getSQLFieldName());
			query.appendEqualSign();
			query.append(variables[0]);
			query.append(".");
			query.append(thisDef.getPrimaryKeyDefinition().getField().getSQLFieldName());
		
			//and care.from_date <='lastDate' 
			query.appendAnd();
			query.append(variables[1]);
			query.append(".");
			query.append(childCareAppDef.findFieldByUniqueName(ChildCareApplication.FIELD_FROM_DATE).getSQLFieldName());
			query.appendLessThanOrEqualsSign();
			query.append(lastDateInRegistrationPeriode);
			
			//and (care.REJECTION_DATE is null 
			query.appendAnd();
			query.appendLeftParenthesis();
			query.append(variables[1]);
			query.append(".");
			query.append(childCareAppDef.findFieldByUniqueName(ChildCareApplication.FIELD_REJECTION_DATE).getSQLFieldName());
			query.appendIsNull();
			
			//or care.REJECTION_DATE > 'firstDate' )
			query.appendOr();
			query.append(variables[1]);
			query.append(".");
			query.append(childCareAppDef.findFieldByUniqueName(ChildCareApplication.FIELD_REJECTION_DATE).getSQLFieldName());
			query.appendGreaterThanSign();
			query.append(firstDateInRegistrationPeriode);
			query.appendRightParenthesis();
			  	
			System.out.println("SQL -> "+this.getClass()+":"+query);
			return idoFindPKsByQuery(query); 
		  	
		} catch (IDOCompositPrimaryKeyException e) {
			e.printStackTrace();
			return ListUtil.getEmptyList();
		}
	}
	
	

}
