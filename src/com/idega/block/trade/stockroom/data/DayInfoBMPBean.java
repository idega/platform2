/*
 * Created on Oct 29, 2004
 *
 */
package com.idega.block.trade.stockroom.data;

import java.sql.Date;
import javax.ejb.FinderException;
import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;


/**
 * @author birna
 *
 */
public class DayInfoBMPBean extends GenericEntity implements DayInfo{

	public void initializeAttributes() {
		addAttribute(getColumnNameDayInfoId(), "info id", true, true, Integer.class);
		addAttribute(getColumnNameSupplyPoolId(), "supply pool id", true, true, Integer.class);
		addAttribute(getColumnNameDayInfoDate(), "info date", true, true, Date.class);
		addAttribute(getColumnNameDayInfoCount(), "info count", true, true, Integer.class);
		addManyToOneRelationship(getColumnNameSupplyPoolId(), SupplyPool.class);
	}
	
	public static String getEntityTableName() { return "sr_day_info"; }
	public static String getColumnNameDayInfoId() { return "day_info_id"; }
	public static String getColumnNameSupplyPoolId() { return "sr_supply_pool_id"; }
	public static String getColumnNameDayInfoDate() { return "day_info_date"; }
	public static String getColumnNameDayInfoCount() { return "day_info_count"; }
		
	public String getEntityName() {
		return getEntityTableName();
	}
	
	public void setDate(Date date) {
		setColumn(getColumnNameDayInfoDate(), date);
	}
	
	public void setCount(int count) {
		setColumn(getColumnNameDayInfoCount(), count);
	}
	
	public void setSupplyPoolId(int id) {
		setColumn(getColumnNameSupplyPoolId(), id);
	}
	
	public Date getDate() {
		return getDateColumnValue(getColumnNameDayInfoDate());		
	}
	
	public int getCount() {
		return getIntColumnValue(getColumnNameDayInfoCount());
	}
	
	public int getSupplyPoolId() {
		return getIntColumnValue(getColumnNameSupplyPoolId());
	}
	
	public Integer ejbFindBySupplyPoolIdAndDate(int supplyPoolId, Date date) throws FinderException{
		IDOQuery query = idoQueryGetSelect();
		query.appendWhereEquals(getColumnNameSupplyPoolId(), supplyPoolId);
		query.appendAndEquals(getColumnNameDayInfoDate(), date);
		return (Integer)idoFindOnePKByQuery(query);
	}
}
