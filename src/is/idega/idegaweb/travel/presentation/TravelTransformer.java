package is.idega.idegaweb.travel.presentation;

import java.util.List;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;


/**
 * @author gimmi
 */
public class TravelTransformer extends TravelBlock {
	
	public void main(IWContext iwc) throws Exception{
		super.main(iwc);
//		if (super.isSuperAdmin) {
			List supplierManagers = getSupplierManagerBusiness(iwc).getSupplierManagerGroup().getChildGroups();
			DropdownMenu menu = new DropdownMenu(supplierManagers, "managerID");
			
			Form form = new Form();
			form.add("Select a supplier manager to be owner for everything unowned");
			form.add(menu);
			form.add(new SubmitButton("save", "save", "yes"));
			add(form);
			
			if (iwc.isParameterSet("save")) {
				runFix(iwc.getParameter("managerID"));
			}
			
			showSuppliers(null);
			showResellers(null);
			showSearchEngines(null);
//		}
	}
	
	private void runFix(String managerID) {
		System.out.println("Manager ID = "+managerID);
		
		java.sql.Connection conn = null;
		java.sql.Statement stmt = null;
		javax.transaction.TransactionManager t = com.idega.transaction.IdegaTransactionManager.getInstance();
		try {
			t.begin();
			conn = com.idega.util.database.ConnectionBroker.getConnection();
			stmt = conn.createStatement();
			System.out.println("Executing supplier fix");
			stmt.execute("update sr_supplier set SUPPLIER_MANAGER_ID = "+managerID+" where SUPPLIER_MANAGER_ID is null");
			System.out.println("Executing reseller fix");
			stmt.execute("update sr_reseller set SUPPLIER_MANAGER_ID = "+managerID+" where SUPPLIER_MANAGER_ID is null");
			System.out.println("Executing searchengine fix");
			stmt.execute("update TB_SERVICE_SEARCH_ENGINE set SUPPLIER_MANAGER_ID = "+managerID+" where SUPPLIER_MANAGER_ID is null");
			System.out.print("Committing...");
			t.commit();
			System.out.println("done");
		} catch (Exception e) {
			try {
				add("UPDATE FAILED - check log");
				e.printStackTrace();
				t.rollback();
			}
			catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		finally{
			if (stmt != null) {
				try {
					stmt.close();
				} catch (java.sql.SQLException e1) {
					e1.printStackTrace();
				}
			}	
			if (conn != null) {
				com.idega.util.database.ConnectionBroker.freeConnection(conn);
			}
			
		}
	}
	
	public String getDefaultSupplierSelectSQL(){
		return "select SR_SUPPLIER_ID, NAME from SR_SUPPLIER where SUPPLIER_MANAGER_ID is null";
	}	
	
	public void showSuppliers(String userSelectSQL)throws Exception{
		Table table = new Table();
		String userSQL = userSelectSQL!=null?userSelectSQL:getDefaultSupplierSelectSQL();
		java.sql.Connection conn = null;
		java.sql.Statement stmt = null;
		try{
			conn = com.idega.util.database.ConnectionBroker.getConnection();
			stmt = conn.createStatement();
			java.sql.ResultSet rs = stmt.executeQuery(userSQL);
			table.add("SuppID",1,1);
			table.add("Name",2,1);
			int row = 2;
			while(rs.next()){
				table.add(rs.getString(1),1,row);
				table.add(rs.getString(2),2,row++);
			}
			rs.close();
		}
		finally{
			if (stmt != null) {
				try {
					stmt.close();
				} catch (java.sql.SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}	
			if (conn != null) {
				com.idega.util.database.ConnectionBroker.freeConnection(conn);
			}
			
		}
		table.setHorizontalZebraColored("#F2e1a9","FFFFFF");
		table.setRowColor(1,"#C38536");
		add(table);
	}
	
	public String getDefaultResellerSelectSQL(){
		return "select SR_RESELLER_ID, NAME from SR_RESELLER where SUPPLIER_MANAGER_ID is null";
	}	
	
	public void showResellers(String userSelectSQL)throws Exception{
		Table table = new Table();
		String userSQL = userSelectSQL!=null?userSelectSQL:getDefaultResellerSelectSQL();
		java.sql.Connection conn = null;
		java.sql.Statement stmt = null;
		try{
			conn = com.idega.util.database.ConnectionBroker.getConnection();
			stmt = conn.createStatement();
			java.sql.ResultSet rs = stmt.executeQuery(userSQL);
			table.add("resellerID",1,1);
			table.add("Name",2,1);
			int row = 2;
			while(rs.next()){
				table.add(rs.getString(1),1,row);
				table.add(rs.getString(2),2,row++);
			}
			rs.close();
		}
		finally{
			if (stmt != null) {
				try {
					stmt.close();
				} catch (java.sql.SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}	
			if (conn != null) {
				com.idega.util.database.ConnectionBroker.freeConnection(conn);
			}
			
		}
		table.setHorizontalZebraColored("#F2e1a9","FFFFFF");
		table.setRowColor(1,"#C38536");
		add(table);
	}	
	
	public String getDefaultSearchEngineSelectSQL(){
		return "select TB_SERVICE_SEARCH_ENGINE_ID, OWNER_NAME, BOOKING_CODE from TB_SERVICE_SEARCH_ENGINE where SUPPLIER_MANAGER_ID is null";
	}	
	
	public void showSearchEngines(String userSelectSQL)throws Exception{
		Table table = new Table();
		String userSQL = userSelectSQL!=null?userSelectSQL:getDefaultSearchEngineSelectSQL();
		java.sql.Connection conn = null;
		java.sql.Statement stmt = null;
		try{
			conn = com.idega.util.database.ConnectionBroker.getConnection();
			stmt = conn.createStatement();
			java.sql.ResultSet rs = stmt.executeQuery(userSQL);
			table.add("searchID",1,1);
			table.add("Name",2,1);
			table.add("Code",3,1);
			int row = 2;
			while(rs.next()){
				table.add(rs.getString(1),1,row);
				table.add(rs.getString(2),2,row);
				table.add(rs.getString(3),3,row++);
			}
			rs.close();
		}
		finally{
			if (stmt != null) {
				try {
					stmt.close();
				} catch (java.sql.SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}	
			if (conn != null) {
				com.idega.util.database.ConnectionBroker.freeConnection(conn);
			}
			
		}
		table.setHorizontalZebraColored("#F2e1a9","FFFFFF");
		table.setRowColor(1,"#C38536");
		add(table);
	}	
	
	
}
