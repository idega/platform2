/*
 * $Id: WaitingListFinder.java,v 1.4 2004/06/05 07:44:41 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.allocation.business;

import is.idega.idegaweb.campus.block.allocation.data.AllocationView;
import is.idega.idegaweb.campus.block.application.data.WaitingList;
import is.idega.idegaweb.campus.block.application.data.WaitingListHome;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.idega.data.EntityFinder;
import com.idega.data.IDOLookup;
import com.idega.util.database.ConnectionBroker;

/**
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public abstract class WaitingListFinder {
	public final static int APPLICANT = 1, APARTMENTTYPE = 2, COMPLEX = 4;

	
	public static Collection listOfWaitingList(int fields, int iApplicantId, int iTypeId, int iComplexId) {
		try {
			return getWaitingListHome().findBySQL(getSQL(fields, iApplicantId, iTypeId, iComplexId));
		}
		catch (Exception e) {
			e.printStackTrace();

			return null;
		}
	}

	public static String getSQL(int fields, int iApplicantId, int iTypeId, int iComplexId) {
		StringBuffer sql = new StringBuffer("select * from ");
		sql.append(is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean.getEntityTableName());

		int count = 0;
		if (fields > 0) {
			sql.append(" where ");

			if ((fields & APPLICANT) == APPLICANT && iApplicantId > 0) {
				sql.append(is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean.getApplicantIdColumnName());
				sql.append(" = ");
				sql.append(iApplicantId);
				count++;
			}

			if ((fields & APARTMENTTYPE) == APARTMENTTYPE && iTypeId > 0) {
				sql.append(count > 0 ? " and " : "  ");
				sql.append(is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean.getApartmentTypeIdColumnName());
				sql.append(" = ");
				sql.append(iTypeId);
				count++;
			}

			if ((fields & COMPLEX) == COMPLEX && iComplexId > 0) {
				sql.append(count > 0 ? " and " : "  ");
				sql.append(is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean.getComplexIdColumnName());
				sql.append(" = ");
				sql.append(iComplexId);
				count++;
			}
		}

		return sql.toString();
	}

	
	/**
	 * Gets a Map of AllocationView objects with apartment category as key
	 * @return map of AllocationView objects
	 */
	public static Map getAllocationView() {
		Hashtable table = new Hashtable();
		Connection Conn = null;

		String sqlString = "select * from v_allocation_view2";

		try {
			Conn = ConnectionBroker.getConnection();
			Statement stmt = Conn.createStatement();
			ResultSet RS = stmt.executeQuery(sqlString);

			while (RS.next()) {
				int catId = RS.getInt("bu_aprt_cat_id");
				int typeId = RS.getInt("bu_aprt_type_id");
				int complexId = RS.getInt("bu_complex_id");
				String typeName = RS.getString("type_name");
				String complexName = RS.getString("complex_name");
				int total = RS.getInt("total_aprt");
				int avail = RS.getInt("avail_aprt");
				int choice1 = RS.getInt("choice1");
				int choice2 = RS.getInt("choice2");
				int choice3 = RS.getInt("choice3");
				
				AllocationView view = new AllocationView(catId,typeId,complexId,typeName,complexName,total,avail,choice1,choice2,choice3);
				Integer category = new Integer(catId);
				List li = (List)table.get(category);
				if (li == null) {
					li = new Vector();
					table.put(category,li);
				}
				li.add(view);
			}

			RS.close();
			stmt.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		finally {
			ConnectionBroker.freeConnection(Conn);
		}

		return table;
	}
	
	public static WaitingListHome getWaitingListHome()throws RemoteException{
		return (WaitingListHome) IDOLookup.getHome(WaitingList.class);
	}
}
