package is.idega.idegaweb.campus.data;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;

import com.idega.block.finance.data.AccountKey;
import com.idega.block.finance.data.AccountKeyHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.SimpleQuerier;
import com.idega.util.database.ConnectionBroker;
/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir </a> <br>
 * @version 1.0
 */
public class AccountEntriesReportBMPBean {
	public final static String COLUMN_ACCOUNT_ID = "ACC_ID";
	public final static String COLUMN_BUILDING_ID = "BUILD_ID";
	public final static String COLUMN_BUILDING = "BUILDING";
	public final static String COLUMN_FIRST_NAME = "FIRST_NAME";
	public final static String COLUMN_MIDDLE_NAME = "MIDDLE_NAME";
	public final static String COLUMN_LAST_NAME = "LAST_NAME";
	public final static String COLUMN_PERSONAL_ID = "PERSONAL_ID";
	private Integer accountID;
	private Integer buildingId;
	private String building;
	private String firstName;
	private String middleName;
	private String lastName;
	private String personalID;
	private Float total;
	private Map entries;
	private EntityContext _entityContext;
	private EJBHome _ejbHome;
	private EJBLocalHome _ejbLocalHome;
	private Object _primaryKey;
	/**
	 * @return Returns the entries.
	 */
	public Map getEntries() {
		return entries;
	}
	public Class getPrimaryKeyClass() {
		return Integer.class;
	}
	public void setEJBHome(EJBHome home) {
		this._ejbHome = home;
	}
	public void setEJBLocalHome(EJBLocalHome home) {
		this._ejbLocalHome = home;
	}
	public Object ejbFindByPrimaryKey(Object primaryKey) {
		return null;
	}
	public Object ejbCreate() {
		return null;
	}
	public void unsetEntityContext() {
		_entityContext = null;
	}
	public void setEntityContext(EntityContext context) {
		_entityContext = context;
	}
	public void ejbStore() {
	}
	public void ejbPassivate() {
	}
	public void ejbRemove() {
	}
	public void ejbLoad() {
	}
	public void ejbActivate() {
	}
	public static Collection findAllBySearch(String[] buildingIds, String[] accountKeys, Date from, Date to,
			boolean byAccountCode) throws SQLException {
		Connection conn = null;
		Statement Stmt = null;
		ResultSetMetaData metaData;
		Vector vector = null;
		String sql = "";
		if (buildingIds != null) {
			try {
				
				
				
				conn = ConnectionBroker.getConnection();
				sql = getKeyIdsSQL(from,to);
				String[] keys = SimpleQuerier.executeStringQuery(sql,conn);
				//System.out.println("key length "+keys.length);
				Stmt = conn.createStatement();
				
				sql = getFindSql(buildingIds, accountKeys, from, to, byAccountCode,keys);
				ResultSet RS = Stmt.executeQuery(sql);
				
				int count = 1;
				while (RS.next()) {
					AccountEntriesReportBMPBean tempObj = new AccountEntriesReportBMPBean();
					if (tempObj != null) {
						String columnName = null;
						tempObj.setAccountID(new Integer(RS.getInt(COLUMN_ACCOUNT_ID)));
						tempObj.setBuildingId(new Integer(RS.getInt(COLUMN_BUILDING_ID)));
						tempObj.setBuilding(RS.getString(COLUMN_BUILDING));
						tempObj.setFirstName(RS.getString(COLUMN_FIRST_NAME));
						tempObj.setMiddleName(RS.getString(COLUMN_MIDDLE_NAME));
						tempObj.setLastName(RS.getString(COLUMN_LAST_NAME));
						tempObj.setPersonalID(RS.getString(COLUMN_PERSONAL_ID));
						
						for (int i = 0; i < keys.length; i++) {
							Float t = new Float(RS.getFloat("T"+keys[i]));
							tempObj.setAmount(keys[i],t);		
						}
					}
					if (vector == null) {
						vector = new Vector();
					}
					vector.addElement(tempObj);
				}
				RS.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new SQLException("SQL : " + sql);
			} finally {
				if (Stmt != null) {
					Stmt.close();
				}
				if (conn != null) {
					ConnectionBroker.freeConnection(conn);
				}
			}
			if (vector != null) {
				vector.trimToSize();
				return vector;
			}
		}
		return null;
	}
	
	private static String getKeyIdsSQL(java.sql.Date from,Date to){
		StringBuffer sql = new StringBuffer("select distinct fin_acc_key_id from fin_acc_entry ");
		sql.append(" where payment_date >= '").append(from).append("' ");
		sql.append(" and payment_date <= '").append(to).append("' ");
		return sql.toString();
	}
	
	private static String getAllKeySQL(Integer buildingID,Date from,Date to){
		StringBuffer sql = new StringBuffer("select distinct k.* from fin_acc_entry e,fin_acc_key k");
		sql.append(" where e.payment_date >= '").append(from).append("' ");
		sql.append(" and e.payment_date <= '").append(to).append("' ");
		sql.append(" and e.fin_acc_key_id  = k.fin_acc_key_id");
		if(buildingID!=null && buildingID.intValue()>0){
			sql.append(" and e.fin_account_id in (select fin_account_id from fin_account a, cam_contract c, bu_apartment ap, bu_floor f ");
			sql.append(" where a.ic_user_id = c.ic_user_id and c.bu_apartment_id = ap.bu_apartment_id ");
			sql.append(" and ap.bu_floor_id = f.bu_floor_id and f.bu_building_id = "); 
			sql.append(buildingID);
			sql.append(")");
		}
		
		return sql.toString();
	}
	
	
	public static Collection getAccountKeys(Integer buildingID,Date from,Date to){
		try {
			AccountKeyHome aHome =(AccountKeyHome) IDOLookup.getHome(AccountKey.class);
			return aHome.findBySQL(getAllKeySQL(buildingID,from,to));
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return null;
	}
	private static String getFindSql(String[] buildingIds, String[] keyIds, Date from, Date to,
			boolean byAccountKeyCode,String[] keys) {
		StringBuffer sql = new StringBuffer();
		String key;
		
		sql.append(" select distinct b.bu_building_id build_id, b.name building,a.fin_account_id acc_id,a.name,u.first_name,u.middle_name,u.last_name,u.personal_id ");
		for (int i = 0; i < keyIds.length; i++) {
			key = keyIds[i];
			sql.append(", ").append("e").append(key).append(".total t").append(key);
		}
		//sql.append(" ,sum(e1.total) k1,sum(e2.total) k2 ");
		sql.append(" from fin_account a ");
		for (int i = 0; i < keys.length; i++) {
			key = keys[i];
			sql.append(" left outer join fin_acc_entry e").append(key);
			sql.append(" on (a.fin_account_id=e").append(key).append(".fin_account_id and e").append(key).append(".fin_acc_key_id =").append(key)
			.append(" and e").append(key).append(".payment_date >= '").append(from.toString()).append("' ")
			.append(" and e").append(key).append(".payment_date <=  '").append(to.toString()).append("') ");
		}
		sql.append(" ,ic_user u,cam_contract c,bu_building b, bu_floor f, bu_apartment ap ");
		sql.append(" where u.ic_user_id = a.ic_user_id ");
		sql.append(" and u.ic_user_id = c.ic_user_id ");
		sql.append(" and c.bu_apartment_id = ap.bu_apartment_id ");
		sql.append(" and ap.bu_floor_id = f.bu_floor_id ");
		sql.append(" and f.bu_building_id = b.bu_building_id ");
		if(buildingIds!=null ){
			sql.append(" and b.bu_building_id = ");
			 sql.append( " in (");
		      for (int i = 0; i < buildingIds.length; i++) {
		      	if(i>0 && i< buildingIds.length)
		      		sql.append(",");
				sql.append(buildingIds[i]);
		      }
		      sql.append(" ) ");
		}
		sql.append(" and a.ACCOUNT_TYPE = 'FINANCE' ");
		sql.append(" and a.fin_account_id in  ");
		sql.append(" (select fin_account_id from fin_acc_entry  ");
		sql.append(" where payment_date >= '").append(from.toString()).append("' and payment_date <= '").append(to.toString()).append("') ");
		//sql.append(" group by b.bu_building_id,b.name,a.fin_account_id ,a.name,u.first_name,u.middle_name,u.last_name,u.personal_id ");
		sql.append(" order by b.name,u.first_name ");
		
		
		System.out.println(sql.toString());
		return sql.toString();
	}
	
	public void setAmount(String keyID,Float amount){
		if(entries==null)
			entries = new HashMap();
		if(entries.containsKey(keyID)){
			float f = ((Float) entries.get(keyID)).floatValue();
			entries.put(keyID,new Float(amount.floatValue()+f));
		}
		else{
			entries.put(keyID,amount);
		}
	
	}
	/**
	 * @return Returns the accountID.
	 */
	public Integer getAccountID() {
		return accountID;
	}
	/**
	 * @param accountID
	 *                    The accountID to set.
	 */
	public void setAccountID(Integer accountID) {
		this.accountID = accountID;
	}
	/**
	 * @return Returns the building.
	 */
	public String getBuilding() {
		return building;
	}
	/**
	 * @param building
	 *                    The building to set.
	 */
	public void setBuilding(String building) {
		this.building = building;
	}
	/**
	 * @return Returns the buildingId.
	 */
	public Integer getBuildingId() {
		return buildingId;
	}
	/**
	 * @param buildingId
	 *                    The buildingId to set.
	 */
	public void setBuildingId(Integer buildingId) {
		this.buildingId = buildingId;
	}
	/**
	 * @return Returns the firstName.
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName
	 *                    The firstName to set.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return Returns the lastName.
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName
	 *                    The lastName to set.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return Returns the middleName.
	 */
	public String getMiddleName() {
		return middleName;
	}
	/**
	 * @param middleName
	 *                    The middleName to set.
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	/**
	 * @return Returns the personalID.
	 */
	public String getPersonalID() {
		return personalID;
	}
	/**
	 * @param personalID
	 *                    The personalID to set.
	 */
	public void setPersonalID(String personalID) {
		this.personalID = personalID;
	}
	/**
	 * @return Returns the total.
	 */
	public Float getTotal() {
		return total;
	}
	/**
	 * @param total
	 *                    The total to set.
	 */
	public void setTotal(Float total) {
		this.total = total;
	}
	public String getName() {
		return getFirstName() + " " + getMiddleName() + " " + getLastName();
	}
}