/*
 * $Id: RegulationBMPBean.java,v 1.8 2003/10/05 11:39:26 kjell Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.regulations.data;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;
import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
import com.idega.block.school.data.SchoolCategory;

/**
 * Entity bean for regulation entries.
 * <p>
 * $Id: RegulationBMPBean.java,v 1.8 2003/10/05 11:39:26 kjell Exp $
 *
 * @author <a href="http://www.lindman.se">Kjell Lindman</a>
 * @version$
 */

public class RegulationBMPBean extends GenericEntity implements Regulation {

	private static final String ENTITY_NAME = "cacc_regulation";

	private static final String COLUMN_PERIOD_FROM = "period_from";
	private static final String COLUMN_PERIOD_TO = "period_to";
	private static final String COLUMN_CHANGED_DATE = "changed_date";
	private static final String COLUMN_CHANGED_SIGN = "changed_sign";
	private static final String COLUMN_DISCOUNT = "discount";
	private static final String COLUMN_NAME = "name";
	private static final String COLUMN_AMOUNT = "amount";
	private static final String COLUMN_OPERATION_ID = "operation_id";
	private static final String COLUMN_PAYMENT_FLOW_TYPE_ID = "flow_type_id";
	private static final String COLUMN_REG_SPEC_TYPE_ID = "reg_spec_type_id";
	private static final String COLUMN_CONDITION_TYPE_ID = "condition_type";
	private static final String COLUMN_VAT_RULE_ID = "vat_rule_id";
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
		
		addAttribute(COLUMN_PERIOD_FROM, "From period", true, true, Date.class);
		addAttribute(COLUMN_PERIOD_TO, "To period", true, true, Date.class);
		addAttribute(COLUMN_CHANGED_DATE, "Ändrings datum", true, true, java.sql.Timestamp.class);
		addAttribute(COLUMN_CHANGED_SIGN, "Ändrings signatur", true, true, String.class);
		addAttribute(COLUMN_NAME, "Name", true, true, java.lang.String.class);
		addAttribute(COLUMN_AMOUNT, "Amount", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_DISCOUNT, "Discount", true, true, java.lang.Float.class);
		addAttribute(COLUMN_CONDITION_ORDER, "Condition order", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_VAT_ELIGIBLE, "VAT Eligible", true, true, java.lang.Integer.class);
		
		addAttribute(COLUMN_OPERATION_ID, "Operation ID", true, true, 
						String.class, "many-to-one", SchoolCategory.class);
						
		addAttribute(COLUMN_PAYMENT_FLOW_TYPE_ID, "Flow type relation ID", true, true,
						Integer.class, "many-to-one", PaymentFlowType.class);
						
		addAttribute(COLUMN_REG_SPEC_TYPE_ID, "Regelspecificationstyp", true, true, 
						Integer.class, "many-to-one", RegulationSpecType.class);
						
		addAttribute(COLUMN_CONDITION_TYPE_ID, "Condition type relation", true, true,
						Integer.class, "many-to-one", ConditionType.class);
						
		addAttribute(COLUMN_SPECIAL_CALCULATION_ID, "Special calculation relation", true, true,
						Integer.class, "many-to-one", SpecialCalculationType.class);
		 
		addAttribute(COLUMN_VAT_RULE_ID, "VAT rule relation", true, true,
						Integer.class, "many-to-one", VATRule.class);
		
		setAsPrimaryKey(getIDColumnName(), true);
		setNullable(COLUMN_OPERATION_ID, true);
		setNullable(COLUMN_PAYMENT_FLOW_TYPE_ID, true);
		setNullable(COLUMN_REG_SPEC_TYPE_ID, true);
		setNullable(COLUMN_CONDITION_TYPE_ID, true);
		setNullable(COLUMN_SPECIAL_CALCULATION_ID, true);
		setNullable(COLUMN_VAT_RULE_ID, true);
		
	}
	
	public float getDiscount() { return getFloatColumnValue(COLUMN_DISCOUNT); }
	public Date getPeriodFrom() { return getDateColumnValue(COLUMN_PERIOD_FROM); }
	public Date getPeriodTo() { return getDateColumnValue(COLUMN_PERIOD_TO); }
	public Timestamp getChangedDate() { return (Timestamp) getColumnValue(COLUMN_CHANGED_DATE); }
	public String getChangedSign() { return (String) getColumnValue(COLUMN_CHANGED_SIGN); }
	public String getName() { return getStringColumnValue(COLUMN_NAME); }
	public String getLocalizationKey() { return getStringColumnValue(COLUMN_NAME); }
	public Integer getAmount() { return getIntegerColumnValue(COLUMN_AMOUNT); }
	public Integer getConditionOrder() { return getIntegerColumnValue(COLUMN_CONDITION_ORDER); }
	public Integer getVATEligible() { return getIntegerColumnValue(COLUMN_VAT_ELIGIBLE); }

	public SchoolCategory getOperation() { 
		return (SchoolCategory) getColumnValue(COLUMN_OPERATION_ID); 
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
	public VATRule getVATRegulation() { 
		return (VATRule) getColumnValue(COLUMN_VAT_RULE_ID); 
	}
		
	public void setPeriodFrom(Date from) {setColumn(COLUMN_PERIOD_FROM, from);}
	public void setPeriodTo(Date to) {setColumn(COLUMN_PERIOD_TO, to);}
	public void setChangedDate(Timestamp date){setColumn(COLUMN_CHANGED_DATE, date);}
	public void setChangedSign(String sign){setColumn(COLUMN_CHANGED_SIGN, sign);}
	public void setName(String name) { setColumn(COLUMN_NAME, name); }	
	public void setDiscount(float discount) { setColumn(COLUMN_DISCOUNT, discount); }	
	public void setLocalizationKey(String name) { setColumn(COLUMN_NAME, name); }	
	public void setAmount(int amount) { setColumn(COLUMN_AMOUNT, amount); }	
	public void setConditionOrder(int value) { setColumn(COLUMN_CONDITION_ORDER, value); }	
	public void setVATEligible(int value) { setColumn(COLUMN_VAT_ELIGIBLE, value); }	
	public void setOperation(String value) { setColumn(COLUMN_OPERATION_ID, value); }	
	public void setPaymentFlowType(int value) { setColumn(COLUMN_PAYMENT_FLOW_TYPE_ID, value); }	
	public void setRegSpecType(int value) { setColumn(COLUMN_REG_SPEC_TYPE_ID, value); }	
	public void setConditionType(int value) { setColumn(COLUMN_CONDITION_TYPE_ID, value); }	
	public void setSpecialCalculation(int value) { setColumn(COLUMN_SPECIAL_CALCULATION_ID, value); }	
	public void setVATRegulation(int value) { setColumn(COLUMN_VAT_RULE_ID, value); }	

	public Collection ejbFindAllRegulations() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		return idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindRegulationsByPeriod(Date from, Date to) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhere(COLUMN_PERIOD_FROM);
		sql.appendGreaterThanOrEqualsSign().append("'"+from+"'");
		sql.appendAnd().append(COLUMN_PERIOD_TO);
		sql.appendLessThanOrEqualsSign().append("'"+to+"'");
		sql.appendOrderByDescending(COLUMN_PERIOD_FROM);
		return idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindRegulationsByPeriod(
			Date from, 
			Date to, 
			String operationID, 
			int flowTypeID, 
			int sortByID
		) throws FinderException {
				
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhere(COLUMN_PERIOD_FROM);
		sql.appendGreaterThanOrEqualsSign().append("'"+from+"'");
		sql.appendAnd().append(COLUMN_PERIOD_TO);
		sql.appendLessThanOrEqualsSign().append("'"+to+"'");
		if(operationID.compareTo("0") != 0) {
			sql.appendAndEquals(COLUMN_OPERATION_ID, "'"+operationID+"'");
			sql.appendAndEquals(COLUMN_PAYMENT_FLOW_TYPE_ID, flowTypeID);
		}
		if(sortByID == 1) {
			sql.appendOrderByDescending(COLUMN_NAME);
		} else {
			sql.appendOrderByDescending(COLUMN_PERIOD_FROM);
		}
		return idoFindPKsBySQL(sql.toString());
	}


	public Object ejbFindRegulation(int id) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(getIDColumnName(), id);
		return idoFindOnePKByQuery(sql);
	}

	public Object ejbFindRegulationOverlap(Date from, Date to, Regulation r) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhere();
		sql.append(" ((");
		sql.append(COLUMN_PERIOD_FROM);
		sql.appendLessThanOrEqualsSign().append("'"+to+"'");
		sql.appendAnd();
		sql.append(COLUMN_PERIOD_TO);
		sql.appendGreaterThanSign().append("'"+to+"'");
		sql.append(") ");

		sql.appendOr();

		sql.append(" (");
		sql.append(COLUMN_PERIOD_FROM);
		sql.appendLessThanOrEqualsSign().append("'"+from+"'");
		sql.appendAnd();
		sql.append(COLUMN_PERIOD_TO);
		sql.appendGreaterThanSign().append("'"+from+"'");
		sql.append(") ");

		sql.appendOr();

		sql.append(" (");
		sql.append(COLUMN_PERIOD_FROM);
		sql.appendLessThanOrEqualsSign().append("'"+to+"'");
		sql.appendAnd();
		sql.append(COLUMN_PERIOD_TO);
		sql.appendGreaterThanSign().append("'"+from+"'");
		sql.append(")) ");

		if (r != null) {
			sql.appendAnd();
			sql.append(getIDColumnName());
			sql.appendNOTEqual();
			sql.append("'"+r.getPrimaryKey().toString()+"'");			
		}

		return idoFindOnePKByQuery(sql);
	}

			  
}
