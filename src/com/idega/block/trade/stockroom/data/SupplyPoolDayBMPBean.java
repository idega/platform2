package com.idega.block.trade.stockroom.data;

import java.util.Collection;
import java.util.GregorianCalendar;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.GenericEntity;
import com.idega.data.query.Column;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;


/**
 * @author gimmi
 */
public class SupplyPoolDayBMPBean extends GenericEntity  implements SupplyPoolDay{

  public static final int SUNDAY = GregorianCalendar.SUNDAY;
  public static final int MONDAY = GregorianCalendar.MONDAY;
  public static final int TUESDAY = GregorianCalendar.TUESDAY;
  public static final int WEDNESDAY = GregorianCalendar.WEDNESDAY;
  public static final int THURSDAY = GregorianCalendar.THURSDAY;
  public static final int FRIDAY = GregorianCalendar.FRIDAY;
  public static final int SATURDAY = GregorianCalendar.SATURDAY;
	
	
	private static final String TABLENAME = "SR_SUPPLY_POOL_DAY";
	static final String COLUMN_POOL_ID = "SR_SUPPLY_POOL_ID";
	static final String COLUMN_DAY_OF_WEEK = "DAY_OF_WEEK";
	static final String COLUMN_MAX = "SPD_MAX";
	static final String COLUMN_MIN = "SPD_MIN";
	static final String COLUMN_ESTIMATED = "SPD_ESTIMATED";
	
	public String getEntityName() {
		return TABLENAME;
	}
	
	public void ejbLoad() {
		super.ejbLoad();
	}
	
	public void initializeAttributes() {
		addManyToOneRelationship(COLUMN_POOL_ID, SupplyPool.class);
		addAttribute(COLUMN_DAY_OF_WEEK, "vikudagur", true, true, Integer.class);
		addAttribute(COLUMN_MAX, "max", true, true, Integer.class);
		addAttribute(COLUMN_MIN, "min", true, true, Integer.class);
		addAttribute(COLUMN_ESTIMATED, "estimated", true, true, Integer.class);
		
		setAsPrimaryKey(COLUMN_POOL_ID, true);
		setAsPrimaryKey(COLUMN_DAY_OF_WEEK, true);
	}
	
	public Object ejbFindByPrimaryKey(SupplyPoolDayPK primaryKey) throws FinderException {
		return super.ejbFindByPrimaryKey(primaryKey);
	}
	
	public Object ejbCreate(SupplyPoolDayPK primaryKey) throws CreateException {
		setPrimaryKey(primaryKey);
		return super.ejbCreate();
	}
	
	public Class getPrimaryKeyClass() {
		return SupplyPoolDayPK.class;
	}
	
	protected boolean doInsertInCreate() {
		return true;
	}
	
	public void setMax(int max) {
		setColumn(COLUMN_MAX, max);
	}
	
	public void setMin(int min) {
		setColumn(COLUMN_MIN, min);
	}
	
	public void setEstimated(int estimated) {
		setColumn(COLUMN_ESTIMATED, estimated);
	}
	
	public int getMax() {
		return getIntColumnValue(COLUMN_MAX);
	}
	
	public int getMin() {
		return getIntColumnValue(COLUMN_MIN);
	}
	
	public int getEstimated() {
		return getIntColumnValue(COLUMN_ESTIMATED);
	}
	
	public Collection ejbFindBySupplyPool(SupplyPool supplyPool) throws FinderException {
		Table table = new Table(this);
		Column supplyPoolCol = new Column(table, COLUMN_POOL_ID);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn(table));
		query.addCriteria(new MatchCriteria(supplyPoolCol, MatchCriteria.EQUALS, supplyPool));
		
		return this.idoFindPKsByQuery(query);
	}
	
}
