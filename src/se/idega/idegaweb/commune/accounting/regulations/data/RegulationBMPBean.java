/*
 * $Id: RegulationBMPBean.java,v 1.2 2003/09/03 23:32:44 kjell Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.regulations.data;

import java.sql.Date;
import java.util.Collection;
import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
import com.idega.block.school.data.SchoolType;

/**
 * Entity bean for regulation entries.
 * <p>
 * $Id: RegulationBMPBean.java,v 1.2 2003/09/03 23:32:44 kjell Exp $
 *
 * @author <a href="http://www.lindman.se">Kjell Lindman</a>
 * @version$
 */

public class RegulationBMPBean extends GenericEntity implements Regulation {

	private static final String ENTITY_NAME = "cacc_regulation";

	private static final String COLUMN_PERIODE_FROM = "period_from";
	private static final String COLUMN_PERIODE_TO = "period_to";
	private static final String COLUMN_NAME = "name";
	private static final String COLUMN_AMOUNT = "amount";
	private static final String COLUMN_MAIN_ACTIVITY_ID = "flow_type_id";
	private static final String COLUMN_PAYMENT_FLOW_TYPE_ID = "flow_type_id";
	private static final String COLUMN_REG_SPEC_TYPE_ID = "reg_spec_type_id";
	private static final String COLUMN_CONDITION_TYPE_ID = "condition_type";
	private static final String COLUMN_VAT_REGULATION_ID = "vat_regulation_id";
	private static final String COLUMN_SPECIAL_CALCULATION_ID = "special_calc_id";
	private static final String COLUMN_CONDITION_ORDER = "condition_order";
	private static final String COLUMN_VAT_ELIGIBLE = "vat_eligible";
	
	
	/**
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}
	
	/**
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		
		addAttribute(COLUMN_PERIODE_FROM, "From period", true, true, Date.class);
		addAttribute(COLUMN_PERIODE_TO, "To period", true, true, Date.class);
		addAttribute(COLUMN_NAME, "Name", true, true, java.lang.String.class);
		addAttribute(COLUMN_AMOUNT, "Amount", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_CONDITION_ORDER, "Condition order", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_VAT_ELIGIBLE, "VAT Eligible", true, true, java.lang.Integer.class);
		
		addAttribute(COLUMN_MAIN_ACTIVITY_ID, "Main activity ID", true, true, 
						Integer.class, "many-to-one", SchoolType.class);
						
		addAttribute(COLUMN_PAYMENT_FLOW_TYPE_ID, "Flow type relation ID", true, true,
						Integer.class, "many-to-one", PaymentFlowType.class);
						
		addAttribute(COLUMN_REG_SPEC_TYPE_ID, "Regelspecificationstyp", true, true, 
						Integer.class, "many-to-one", RegulationSpecType.class);
						
		addAttribute(COLUMN_CONDITION_TYPE_ID, "Condition type relation", true, true,
						Integer.class, "many-to-one", ConditionType.class);
						
		addAttribute(COLUMN_SPECIAL_CALCULATION_ID, "Special calculation relation", true, true,
						Integer.class, "many-to-one", SpecialCalculationType.class);
		 
		addAttribute(COLUMN_VAT_REGULATION_ID, "VAT regulation relation", true, true,
						Integer.class, "many-to-one", VATRegulation.class);
		
		setAsPrimaryKey(getIDColumnName(), true);
		setNullable(COLUMN_MAIN_ACTIVITY_ID, true);
		setNullable(COLUMN_PAYMENT_FLOW_TYPE_ID, true);
		setNullable(COLUMN_REG_SPEC_TYPE_ID, true);
		setNullable(COLUMN_CONDITION_TYPE_ID, true);
		setNullable(COLUMN_SPECIAL_CALCULATION_ID, true);
		setNullable(COLUMN_VAT_REGULATION_ID, true);
		
	}

	public Date getPeriodeFrom() { return getDateColumnValue(COLUMN_PERIODE_FROM); }
	public Date getPeriodeTo() { return getDateColumnValue(COLUMN_PERIODE_TO); }
	public String getName() { return getStringColumnValue(COLUMN_NAME); }
	public String getLocalizationKey() { return getStringColumnValue(COLUMN_NAME); }
	public Integer getAmount() { return getIntegerColumnValue(COLUMN_AMOUNT); }
	public Integer getConditionOrder() { return getIntegerColumnValue(COLUMN_CONDITION_ORDER); }
	public Integer getVATEligible() { return getIntegerColumnValue(COLUMN_VAT_ELIGIBLE); }

	public SchoolType getMainActivity() { 
		return (SchoolType) getColumnValue(COLUMN_MAIN_ACTIVITY_ID); 
	}
	public PaymentFlowType getPaymentFlowType() { 
		return (PaymentFlowType) getColumnValue(COLUMN_PAYMENT_FLOW_TYPE_ID); 
	}
	public RegulationSpecType getRegSpecType() { 
		return (RegulationSpecType) getColumnValue(COLUMN_REG_SPEC_TYPE_ID); 
	}
	public ConditionType getConditionType() { 
		return (ConditionType) getColumnValue(COLUMN_CONDITION_TYPE_ID); 
	}
	public SpecialCalculationType getSpecialCalculation() { 
		return (SpecialCalculationType) getColumnValue(COLUMN_SPECIAL_CALCULATION_ID); 
	}
	public VATRegulation getVATRegulation() { 
		return (VATRegulation) getColumnValue(COLUMN_VAT_REGULATION_ID); 
	}
		
	public void setPeriodFrom(Date from) {setColumn(COLUMN_PERIODE_FROM, from);}
	public void setPeriodTo(Date to) {setColumn(COLUMN_PERIODE_TO, to);}
	public void setName(String name) { setColumn(COLUMN_NAME, name); }	
	public void setLocalizationKey(String name) { setColumn(COLUMN_NAME, name); }	
	public void setAmount(int amount) { setColumn(COLUMN_AMOUNT, amount); }	
	public void setConditionOrder(int value) { setColumn(COLUMN_CONDITION_ORDER, value); }	
	public void setVATEligible(int value) { setColumn(COLUMN_VAT_ELIGIBLE, value); }	
	public void setMainActivity(int value) { setColumn(COLUMN_MAIN_ACTIVITY_ID, value); }	
	public void setPaymentFlowType(int value) { setColumn(COLUMN_PAYMENT_FLOW_TYPE_ID, value); }	
	public void setRegSpecType(int value) { setColumn(COLUMN_REG_SPEC_TYPE_ID, value); }	
	public void setConditionType(int value) { setColumn(COLUMN_CONDITION_TYPE_ID, value); }	
	public void setSpecialCalculation(int value) { setColumn(COLUMN_SPECIAL_CALCULATION_ID, value); }	
	public void setVATRegulation(int value) { setColumn(COLUMN_VAT_REGULATION_ID, value); }	

	public Collection ejbFindAllRegulations() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		return idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindRegulationsByPeriode(Date from, Date to) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhere(COLUMN_PERIODE_FROM);
		sql.appendGreaterThanOrEqualsSign().append("'"+from+"'");
		sql.appendAnd().append(COLUMN_PERIODE_TO);
		sql.appendLessThanOrEqualsSign().append("'"+to+"'");
		sql.appendOrderByDescending(COLUMN_PERIODE_FROM);
		return idoFindPKsBySQL(sql.toString());
	}

	public Object ejbFindRegulation(int id) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(getIDColumnName(), id);
		return idoFindOnePKByQuery(sql);
	}

			  
}
