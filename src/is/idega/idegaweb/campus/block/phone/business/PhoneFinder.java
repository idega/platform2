package is.idega.idegaweb.campus.block.phone.business;

import is.idega.idegaweb.campus.block.phone.data.CampusPhone;
import is.idega.idegaweb.campus.block.phone.data.CampusPhoneHome;
import is.idega.idegaweb.campus.data.AccountPhone;
import is.idega.idegaweb.campus.data.AccountPhoneHome;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.block.finance.data.Account;
import com.idega.block.finance.data.AccountHome;
import com.idega.data.EntityFinder;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.util.IWTimestamp;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public abstract class PhoneFinder {
	public final static int NAME = 0, SSN = 1, APARTMENT = 2, FLOOR = 3, BUILDING = 4, COMPLEX = 5, CATEGORY = 6, TYPE = 7;

	
/*
	public static Map mapOfPhonesByPhoneNumber() {
		List L = listOfPhones();
		if (L != null) {
			Hashtable H = new Hashtable();
			int len = L.size();
			for (int i = 0; i < len; i++) {
				CampusPhone C = (CampusPhone) L.get(i);
				H.put(C.getPhoneNumber(), C);
			}
			return H;
		}
		else
			return null;
	}
	*/
/*
	public static List listOfPhonesInUse() {
		StringBuffer sql = new StringBuffer("select pho.* ");
		sql.append(" from cam_phone pho,cam_contract con ");
		sql.append(" where pho.bu_apartment_id = con.bu_apartment_id");
		sql.append(" and con.status = '");
		sql.append(is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean.statusSigned);
		sql.append("'");
		try {
			return EntityFinder.getInstance().findAll(CampusPhone.class, sql.toString());
		}
		catch (Exception ex) {
			return null;
		}
	}
*/
	
	public static Collection listOfAccountWithPhoneNumber() {
		StringBuffer sql = new StringBuffer("Select a.* from ");
		sql.append(com.idega.block.finance.data.AccountBMPBean.getEntityTableName());
		sql.append(" a ,");
		sql.append(is.idega.idegaweb.campus.data.AccountPhoneBMPBean.getEntityTableName());
		sql.append(" p where a.fin_account_id = p.fin_account_id");
		//System.err.println (sql.toString());
		try {
			//AccountHome ahome = com.idega.data.IDOLookup.getHome(Account.class)
			return new Vector(((AccountHome) com.idega.data.IDOLookup.getHome(Account.class)).findBySQL(sql.toString()));
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Collection listOfAccountWithPhoneNumber(IWTimestamp from) {
		StringBuffer sql = new StringBuffer("Select a.* from ");
		sql.append(com.idega.block.finance.data.AccountBMPBean.getEntityTableName());
		sql.append(" a ,");
		sql.append(is.idega.idegaweb.campus.data.AccountPhoneBMPBean.getEntityTableName());
		sql.append(" p where a.fin_account_id = p.fin_account_id");
		if (from != null) {
			sql.append(" and ");
			sql.append(is.idega.idegaweb.campus.data.AccountPhoneBMPBean.getColumnNameValidTo());
			sql.append(" >= '");
			sql.append(from.getDate());
			sql.append("'");
		}
		//System.err.println (sql.toString());
		try {
			return new Vector(((AccountHome) com.idega.data.IDOLookup.getHome(Account.class)).findBySQL(sql.toString()));
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public static AccountPhoneHome getAccountPhoneHome() throws RemoteException{
		return (AccountPhoneHome) IDOLookup.getHome(AccountPhone.class);
	}

	public static Map mapOfAccountIdsByPhoneNumber()throws java.rmi.RemoteException,FinderException {
		Hashtable H = null;
		Collection L = getAccountPhoneHome().findAll();
		if (L != null) {
			H = new Hashtable(L.size());
			Iterator I = L.iterator();
			AccountPhone A;
			while (I.hasNext()) {
				A = (AccountPhone) I.next();
				H.put(A.getPhoneNumber(), A.getAccountId());
			}
		}
		return H;
	}

	public static Map mapOfAccountsByPhoneNumber() throws java.rmi.RemoteException,FinderException {
		Hashtable H = null;
		Collection L = getAccountPhoneHome().findAll();
		Map M = mapOfAccountsWithPhoneNumber();
		if (L != null && M != null) {
			H = new Hashtable(L.size());
			Iterator I = L.iterator();
			AccountPhone A;
			Account a;
			Integer id;
			while (I.hasNext()) {
				A = (AccountPhone) I.next();
				id = A.getAccountId();
				if (M.containsKey(id))
					H.put(A.getPhoneNumber(), M.get(id));
			}
		}
		return H;
	}

	public static Map mapOfAccountPhoneListsByPhoneNumber(IWTimestamp from)throws java.rmi.RemoteException,FinderException {
		Hashtable H = null;
		Collection L = getAccountPhoneHome().findAll();
		if (L != null) {
			H = new Hashtable(L.size());
			Iterator I = L.iterator();
			String number;
			AccountPhone A;
			while (I.hasNext()) {
				List apList;
				A = (AccountPhone) I.next();
				number = A.getPhoneNumber();
				if (H.containsKey(number))
					apList = (List) H.get(number);
				else
					apList = new Vector();
				apList.add(A);
				H.put(number, apList);
			}
		}

		return H;
	}

	/*
	public static Map mapOfAccountsListsByPhoneNumber(IWTimestamp from) throws java.rmi.RemoteException,FinderException {
		Hashtable H = null;
		Collection L = getAccountPhoneHome().findValid(from.getDate());
		Map M = mapOfAccountsWithPhoneNumber(from);
		if (L != null && M != null) {
			H = new Hashtable(L.size());
			Iterator I = L.iterator();
			AccountPhone A;
			Account a;
			Integer id;
			Vector V;
			String number;
			while (I.hasNext()) {
				A = (AccountPhone) I.next();
				id = A.getAccountId();
				number = A.getPhoneNumber();
				if (M.containsKey(id)) {
					if (H.containsKey(number)) {
						V = (Vector) H.get(number);
					}
					else {
						V = new Vector();
					}
					V.add(M.get(id));
					H.put(number, V);
				}
			}
		}
		return H;
	}
*/
	public static Map mapOfAccountsWithPhoneNumber() throws java.rmi.RemoteException {
		Hashtable H = null;
		Collection L = listOfAccountWithPhoneNumber();
		if (L != null) {
			H = new Hashtable(L.size());
			Iterator I = L.iterator();
			Account A;
			while (I.hasNext()) {
				A = (Account) I.next();
				H.put((Integer) A.getPrimaryKey(), A);
			}
		}
		return H;
	}
/*
	public static Map mapOfAccountsWithPhoneNumber(IWTimestamp from) throws java.rmi.RemoteException {
		Hashtable H = null;
		List L = listOfAccountWithPhoneNumber(from);
		if (L != null) {
			H = new Hashtable(L.size());
			Iterator I = L.iterator();
			Account A;
			while (I.hasNext()) {
				A = (Account) I.next();
				H.put((Integer) A.getPrimaryKey(), A);
			}
		}
		return H;
	}
*/
	/*
	public static Map mapOfPhonesInUse() {
		Hashtable H = null;
		List L = listOfPhonesInUse();
		if (L != null) {
			int len = L.size();
			H = new Hashtable(len);
			Iterator I = L.iterator();
			CampusPhone P;
			while (I.hasNext()) {
				P = (CampusPhone) I.next();
				H.put(P.getPhoneNumber(), P);
			}
		}
		return H;
	}
*/
	/*
	public static Map mapOfPhonesInContractByPhoneNumber() {
		List L = listOfPhones();
		if (L != null) {
			Hashtable H = new Hashtable();
			int len = L.size();
			for (int i = 0; i < len; i++) {
				CampusPhone C = (CampusPhone) L.get(i);
				H.put(C.getPhoneNumber(), C);
			}
			return H;
		}
		else
			return null;
	}
*/
	/*
	private static String getOrderString(int type) {
		String order = null;
		switch (type) {
			case NAME :
				order = " p.first_name,p.middle_name,p.last_name ";
				break;
			case SSN :
				order = " p.ssn ";
				break;
			case BUILDING :
				order = " b.name ";
				break;
			case COMPLEX :
				order = " c.name ";
				break;
			case FLOOR :
				order = " f.name ";
				break;
			case APARTMENT :
				order = " a.name ";
				break;
			case CATEGORY :
				order = " y.name ";
				break;
			case TYPE :
				order = " t.name ";
				break;
			default :
				order = " con.bu_apartment_id ";

		}
		return order;
	}
*/
	public static Map mapOfPhones(Collection listOfPhones) {
		if (listOfPhones != null) {
			int len = listOfPhones.size();
			Hashtable H = new Hashtable(len);
			for (Iterator iter = listOfPhones.iterator(); iter.hasNext();) {
				CampusPhone P = (CampusPhone) iter.next();
				H.put((Integer)P.getPrimaryKey(), P);
			}
			return H;
		}
		else
			return null;
	}

	public static Map mapOfPhonesByApartmentId(Collection listOfPhones) {
		if (listOfPhones != null) {
			int len = listOfPhones.size();
			Hashtable H = new Hashtable(len);
			for (Iterator iter = listOfPhones.iterator(); iter.hasNext();) {
				CampusPhone P = (CampusPhone) iter.next();
				H.put(new Integer(P.getApartmentId()), P);
			}
			return H;
		}
		else
			return null;
	}
/*
	public static List listOfPhones(String sComplexId, String sBuildingId, String sFloorId, String sType, String sCategory, int iOrder) {

		StringBuffer sql = new StringBuffer("select pho.* ");
		sql.append(" from bu_apartment a,bu_floor f,bu_building b");
		sql.append(",bu_complex c,bu_aprt_type t,bu_aprt_cat y,cam_phone pho ");
		sql.append(" where a.bu_aprt_type_id = t.bu_aprt_type_id ");
		sql.append(" and t.bu_aprt_cat_id = y.bu_aprt_cat_id");
		sql.append(" and a.bu_floor_id = f.bu_floor_id ");
		sql.append(" and f.bu_building_id = b.bu_building_id ");
		sql.append(" and b.bu_complex_id = c.bu_complex_id ");
		sql.append(" and a.bu_apartment_id = pho.bu_apartment_id");

		if (sComplexId != null && !"-1".equals(sComplexId)) {
			sql.append(" and bu_complex_id  = ");
			sql.append(sComplexId);
		}
		if (sBuildingId != null && !"-1".equals(sBuildingId)) {
			sql.append(" and bu_building_id = ");
			sql.append(sBuildingId);
		}
		if (sFloorId != null && !"-1".equals(sFloorId)) {
			sql.append(" and bu_floor_id = ");
			sql.append(sFloorId);
		}
		if (sType != null && !"-1".equals(sType)) {
			sql.append(" and bu_aprt_type_id = ");
			sql.append(sType);
		}
		if (sCategory != null && !"-1".equals(sCategory)) {
			sql.append(" and bu_aprt_cat_id = ");
			sql.append(sCategory);
		}
		String order = getOrderString(iOrder);
		if (order != null) {
			sql.append(" order by ");
			sql.append(order);
		}
		String sSQL = sql.toString();
		//System.err.println(sSQL);
		try {
			return EntityFinder.getInstance().findAll(CampusPhone.class, sql.toString());
		}
		catch (Exception ex) {
			return null;
		}
	}
*/	
	public static Collection listOfPhoneContracts(String phoneNumber){
		StringBuffer sql = new StringBuffer("select c.* from cam_contract c,cam_phone p");
		sql.append(" where p.bu_apartment_id = c.bu_apartment_id ");
		sql.append(" and p.phone_number = '");
		sql.append(phoneNumber);
		sql.append("'");
		sql.append(" order by c.cam_contract_id");	
		try {
			return EntityFinder.getInstance().findAll(is.idega.idegaweb.campus.block.allocation.data.Contract.class, sql.toString());
		}
		catch (Exception ex) {
			return null;
		}
	}

	/**
	 * @return
	 */
	public static Collection listOfPhones() {
		try {
			return ((CampusPhoneHome)IDOLookup.getHome(CampusPhone.class)).findAll();
		} catch (IDOLookupException e) {
			
			e.printStackTrace();
		} catch (FinderException e) {
	
		}
		return null;
	}

}
