package is.idega.idegaweb.campus.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Vector;

import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.ejb.EntityContext;

import com.idega.util.database.ConnectionBroker;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class AccountEntryReportBMPBean implements AccountEntryReport{


	public final static String COLUMN_CONTRACT_ID = "CONTRACT_ID";
  public final static String COLUMN_ACCOUNT_ID = "ACC_ID";
  public final static String COLUMN_BUILDING_ID = "BUILD_ID";
  public final static String COLUMN_BUILDING = "BUILDING";
  public final static String COLUMN_FIRST_NAME = "FIRST_NAME";
  public final static String COLUMN_MIDDLE_NAME = "MIDDLE_NAME";
  public final static String COLUMN_LAST_NAME = "LAST_NAME";
  public final static String COLUMN_PERSONAL_ID = "PERSONAL_ID";
  public final static String COLUMN_APRT_ID = "APRT_ID";
  public final static String COLUMN_APARTMENT = "APARTMENT";
  public final static String COLUMN_KEYID= "KEYID";
  public final static String COLUMN_KEYCODE= "KEYCODE";
  public final static String COLUMN_KEYINFO = "KEYINFO";
  public final static String COLUMN_TOTAL = "TOTAL";
  public final static String COLUMN_VALID_FROM = "VALID_FROM";
  public final static String COLUMN_VALID_TO = "VALID_TO";
  
 
  private Integer accountID;
  private Integer buildingId;
  private String building;
  private String firstName;
  private String middleName;
  private String lastName;
  private String personalID;
 
  private Integer keyID;
  private String keyCode;
  private String keyInfo;
  private Float total;
  

  private EntityContext _entityContext;
  private EJBHome _ejbHome;
  private EJBLocalHome _ejbLocalHome;
  private Object _primaryKey;


  public Class getPrimaryKeyClass(){return Integer.class;}
  public void setEJBHome(EJBHome home){this._ejbHome = home;}
  public void setEJBLocalHome(EJBLocalHome home){this._ejbLocalHome = home;}
  public Object ejbFindByPrimaryKey(Object primaryKey){return null;}
  public Object ejbCreate(){return null;}
  public void unsetEntityContext(){_entityContext = null;}
  public void setEntityContext(EntityContext context){_entityContext = context;}
  public void ejbStore(){}
  public void ejbPassivate(){}
  public void ejbRemove(){}
  public void ejbLoad(){}
  public void ejbActivate(){}

  public static Collection findAllBySearch(String[] buildingIds,String[] accountKeys,Timestamp from,Timestamp to,boolean byAccountCode)throws SQLException{
     Connection conn= null;
    Statement Stmt= null;
    ResultSetMetaData metaData;
    Vector vector=null;
    String sql = getFindSql(buildingIds,accountKeys,from,to,byAccountCode);
    if(buildingIds!=null){
    try{
      conn = ConnectionBroker.getConnection();
      Stmt = conn.createStatement();

      System.err.println(sql);
      ResultSet RS = Stmt.executeQuery(sql);
      metaData = RS.getMetaData();
      int count = 1;
      while (RS.next() ){
        AccountEntryReportBMPBean tempObj= new AccountEntryReportBMPBean() ;
        //ACC_ID 	BUILD_ID 	BUILDING 	FIRST_NAME 	MIDDLE_NAME 	LAST_NAME 	PERSONAL_ID 	KEYID 	KEYCODE 	KEYINFO 	TOTAL
        if(tempObj != null){
          String columnName = null;
          tempObj.setAccountID(new Integer(RS.getInt(1)));//COLUMN_ACCOUNT_ID)));
          tempObj.setBuildingId(new Integer(RS.getInt(2)));//COLUMN_BUILDING_ID)));
          tempObj.setBuilding(RS.getString(3));//COLUMN_BUILDING));
          tempObj.setFirstName(RS.getString(4));//COLUMN_FIRST_NAME));
          tempObj.setMiddleName(RS.getString(5));//COLUMN_MIDDLE_NAME));
          tempObj.setLastName(RS.getString(6));//COLUMN_LAST_NAME));
          tempObj.setPersonalID(RS.getString(7));//COLUMN_PERSONAL_ID));
          tempObj.setKeyID(new Integer(RS.getInt(8)));//RS.getInt(COLUMN_KEYID)));
          tempObj.setKeyCode(RS.getString(9));//COLUMN_KEYCODE));
          tempObj.setKeyInfo(RS.getString(10));//COLUMN_KEYINFO));
          tempObj.setTotal(new Float(RS.getFloat(11)));//RS.getFloat(COLUMN_TOTAL)));
        
        }
        if(vector==null){
          vector=new Vector();
        }
        vector.addElement(tempObj);

      }
      RS.close();
    }
    catch(SQLException ex){
    	ex.printStackTrace();
      throw new SQLException("SQL : "+sql);
    }
    finally{
      if(Stmt != null){
        Stmt.close();
      }
      if (conn != null){
        ConnectionBroker.freeConnection(conn);
      }
    }

    if (vector != null){
      vector.trimToSize();
      return vector;
    }
   
    }
  return null;
  }
  
  

  private static String getFindSql(String[] buildingIds,String[] keyIds,java.sql.Timestamp from,java.sql.Timestamp to,boolean byAccountKeyCode){
    
    StringBuffer sql = new StringBuffer();
    
    /*
	 select a.FIN_ACCOUNT_ID ACC_ID ,b.BU_BUILDING_ID BUILD_ID,b.NAME BUILDING,
	ap.bu_apartment_id APRT_ID,ap.NAME APARTMENT,
	u.FIRST_NAME,u.MIDDLE_NAME,u.LAST_NAME,u.PERSONAL_ID, k.fin_acc_key_id KEYID,
	k.NAME KEYCODE,k.INFO KEYINFO ,sum(e.TOTAL) TOTAL 
	from FIN_ACC_ENTRY e, FIN_ACCOUNT a,IC_USER u, FIN_ACC_KEY k,FIN_TARIFF_KEY tk ,
	CAM_APRT_ACC_ENTRY ce,BU_APARTMENT ap, BU_FLOOR f,BU_BUILDING b
	where e.FIN_ACCOUNT_ID = a.FIN_ACCOUNT_ID  
	and a.IC_USER_ID = u.IC_USER_ID  
	and k.FIN_ACC_KEY_ID = e.FIN_ACC_KEY_ID  
	and k.FIN_TARIFF_KEY_ID = tk.FIN_TARIFF_KEY_ID 
	and e.payment_date >= '2004-03-01' 
	and e.payment_date <= '2004-04-16'  
	and e.fin_acc_entry_id = ce.entry_id
	and ce.APRT_ID = ap.BU_APARTMENT_ID
	and ap.BU_FLOOR_ID = f.BU_FLOOR_ID
	and f.bu_building_id = b.bu_building_id
	and b.BU_BUILDING_ID = 2
	group by a.FIN_ACCOUNT_ID,b.BU_BUILDING_ID,b.name,ap.bu_apartment_id,ap.NAME
	,u.FIRST_NAME, u.MIDDLE_NAME, u.LAST_NAME, u.PERSONAL_ID, k.fin_acc_key_id, k.NAME ,k.INFO
    
    */
    sql.append("  select a.FIN_ACCOUNT_ID ACC_ID,b.BU_BUILDING_ID BUILD_ID,b.NAME BUILDING, ");
    sql.append("  ");
    sql.append(" u.FIRST_NAME,u.MIDDLE_NAME,u.LAST_NAME,u.PERSONAL_ID, ");
    sql.append(" ");
    if(!byAccountKeyCode)
    	sql.append(" k.FIN_ACC_KEY_ID KEYID,k.NAME KEYCODE,k.INFO KEYINFO ");
    else
    	sql.append(" tk.FIN_TARIFF_KEY_ID  KEYID,tk.NAME KEYCODE,tk.INFO KEYINFO ");
    sql.append( ",sum(e.TOTAL) TOTAL "); 
    sql.append(" from FIN_ACC_ENTRY e, FIN_ACCOUNT a,IC_USER u, FIN_ACC_KEY k,FIN_TARIFF_KEY tk , ");
	sql.append(" CAM_APRT_ACC_ENTRY ce,BU_APARTMENT ap, BU_FLOOR f,BU_BUILDING b ");
	sql.append(" where  e.FIN_ACCOUNT_ID = a.FIN_ACCOUNT_ID ");
	sql.append(" and a.IC_USER_ID = u.IC_USER_ID ");
	sql.append(" and k.FIN_ACC_KEY_ID = e.FIN_ACC_KEY_ID ");
	sql.append(" and k.FIN_TARIFF_KEY_ID = tk.FIN_TARIFF_KEY_ID ");
	sql.append(" and e.FIN_ACC_ENTRY_ID = ce.ENTRY_ID ");
	sql.append(" and ce.APRT_ID = ap.BU_APARTMENT_ID ");
	sql.append(" and ap.BU_FLOOR_ID = f.BU_FLOOR_ID ");
	sql.append(" and f.BU_BUILDING_ID = b.BU_BUILDING_ID ");
	
    boolean and = false;

    if(buildingIds !=null){
      sql.append(" and ");
      sql.append(" b.bu_building_id ");
      sql.append( " in (");
      for (int i = 0; i < buildingIds.length; i++) {
      	if(i>0 && i< buildingIds.length)
      		sql.append(",");
		sql.append(buildingIds[i]);
      }
      sql.append(" ) ");
      and = true;
    }
    if(keyIds!=null ){
      sql.append(" and ");
      sql.append(" k.fin_acc_key_id ");
      sql.append( " in (");
      for (int i = 0; i < keyIds.length; i++) {
      	if(i>0 && i< keyIds.length)
      		sql.append(",");
		sql.append(keyIds[i]);
      }
      sql.append(" ) ");
    }
    if(from!=null){
      sql.append(" and e.payment_date >= '");
      sql.append(from.toString());
      sql.append("'");
    }
    if(to!=null){
      sql.append(" and e.payment_date <= '");
      sql.append(to.toString());
      sql.append("'");
    }

    sql.append(" group by a.FIN_ACCOUNT_ID,b.BU_BUILDING_ID,b.name,ap.bu_apartment_id,ap.NAME, ");
	sql.append(" u.FIRST_NAME,u.MIDDLE_NAME,u.LAST_NAME,u.PERSONAL_ID, ");

	sql.append(!byAccountKeyCode?" k.fin_acc_key_id,k.NAME,k.INFO ":"tk.fin_tariff_key_id,tk.NAME,tk.INFO");
	sql.append(" order by u.FIRST_NAME,u.LAST_NAME,a.FIN_ACCOUNT_ID ");
	//System.out.println(sql.toString());
    return sql.toString();
  }
  

/**
 * @return Returns the accountID.
 */
public Integer getAccountID() {
	return accountID;
}
/**
 * @param accountID The accountID to set.
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
 * @param building The building to set.
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
 * @param buildingId The buildingId to set.
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
 * @param firstName The firstName to set.
 */
public void setFirstName(String firstName) {
	this.firstName = firstName;
}
/**
 * @return Returns the keyCode.
 */
public String getKeyCode() {
	return keyCode;
}
/**
 * @param keyCode The keyCode to set.
 */
public void setKeyCode(String keyCode) {
	this.keyCode = keyCode;
}
/**
 * @return Returns the keyInfo.
 */
public String getKeyInfo() {
	return keyInfo;
}
/**
 * @param keyInfo The keyInfo to set.
 */
public void setKeyInfo(String keyInfo) {
	this.keyInfo = keyInfo;
}
/**
 * @return Returns the lastName.
 */
public String getLastName() {
	return lastName;
}
/**
 * @param lastName The lastName to set.
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
 * @param middleName The middleName to set.
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
 * @param personalID The personalID to set.
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
 * @param total The total to set.
 */
public void setTotal(Float total) {
	this.total = total;
}
/**
 * @return Returns the keyID.
 */
public Integer getKeyID() {
	return keyID;
}
/**
 * @param keyID The keyID to set.
 */
public void setKeyID(Integer keyID) {
	this.keyID = keyID;
}

public String getName(){
	StringBuffer name = new StringBuffer();
	if(getFirstName()!=null)
		name.append(getFirstName()).append(" ");
	if(getMiddleName()!=null)
		name.append(getMiddleName()).append(" ");
	if(getLastName()!=null)
		name.append(getLastName());
	return name.toString().trim();
}
}