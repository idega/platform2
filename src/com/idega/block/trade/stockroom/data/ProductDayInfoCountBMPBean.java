/*
 * Created on Nov 17, 2004
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
public class ProductDayInfoCountBMPBean extends GenericEntity implements ProductDayInfoCount {

	public void initializeAttributes() {
		addAttribute(getIDColumnName(), "id", true, true, Integer.class);
		addAttribute(getColumnNameProductId(), "product id", true, true, Integer.class);
		addAttribute(getColumnNameProductInfoDate(), "product date", true, true, Date.class);
		addAttribute(getColumnNameProductInfoCount(), "product count", true, true, Integer.class);
		addManyToOneRelationship(getColumnNameProductId(), Product.class);
	}
	
	public static String getEntityTableName() { return "sr_pr_info_count"; }
	public static String getColumnNameProductId() { return "sr_product_id"; }
	public static String getColumnNameProductInfoDate() { return "sr_pr_info_date"; }
	public static String getColumnNameProductInfoCount() { return "sr_pr_info_count"; }

	public String getEntityName() {
		return getEntityTableName();
	}
	
	public int getProductId() {
		return getIntColumnValue(getColumnNameProductId());
	}
	
	public Date getDate() {
		return getDateColumnValue(getColumnNameProductInfoDate());
	}
	
	public int getCount() {
		return getIntColumnValue(getColumnNameProductInfoCount());
	}
	
	public void setProductId(int productId) {
		setColumn(getColumnNameProductId(), productId);
	}
	
	public void setDate(Date date) {
		setColumn(getColumnNameProductInfoDate(), date);
	}
	
	public void setCount(int count) {
		setColumn(getColumnNameProductInfoCount(), count);
	}
	public Integer ejbFindByProductIdAndDate(int productId, Date date)throws FinderException {
		IDOQuery query = idoQueryGetSelect();
		query.appendWhereEquals(getColumnNameProductId(), productId);
		query.appendAndEquals(getColumnNameProductInfoDate(), date);
		return (Integer)idoFindOnePKByQuery(query);
	}
}
