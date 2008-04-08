package is.idega.idegaweb.campus.block.finance.business;

import is.idega.idegaweb.campus.data.BuildingAccountEntry;

import java.util.List;

import com.idega.block.finance.data.Account;
import com.idega.data.EntityFinder;
import com.idega.data.IDOFinderException;

/**
 * 
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class CampusAccountFinder {
	public static List getSSNAccounts(String ssn, String type) {
		StringBuffer sql = new StringBuffer("select f.* ");
		sql.append(" from cam_contract c,fin_account f,app_applicant a ");
		sql.append(" where a.app_applicant_id = c.app_applicant_id ");
		sql.append(" and f.ic_user_id = c.ic_user_id ");
		sql.append(" and f.account_type = ").append(type);
		sql.append(" and a.ssn = '").append(ssn).append("'");

		try {
			return EntityFinder.getInstance().findAll(Account.class,
					sql.toString());
		} catch (IDOFinderException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static List getAccountEntryReport(int building_id,
			int account_key_id, java.sql.Timestamp from, java.sql.Timestamp to) {
		StringBuffer sql = new StringBuffer(" select ");
		sql.append(" b.bu_building_id building_id, ");
		sql.append(" b.name building_name, ");
		sql.append(" k.fin_acc_key_id key_id, ");
		sql.append(" k.name key_name, ");
		sql.append(" k.info key_info, ");
		sql.append(" sum(e.total) total, ");
		sql.append(" count(acc.fin_account_id) number ");
		sql.append(" from ");
		sql.append(" bu_apartment a,bu_building b,bu_floor f, ");
		sql
				.append(" cam_contract c,fin_account acc,fin_acc_entry e,fin_acc_key k ");
		sql.append(" where b.bu_building_id = f.bu_building_id ");
		sql.append(" and f.bu_floor_id = a.bu_floor_id ");
		sql.append(" and a.bu_apartment_id = c.bu_apartment_id ");
		sql.append(" and c.ic_user_id = acc.ic_user_id ");
		sql.append(" and e.fin_account_id = acc.fin_account_id ");
		sql.append(" and k.fin_acc_key_id = e.fin_acc_key_id ");

		boolean and = false;

		if (building_id > 0) {
			sql.append(" and ");
			sql.append(" b.bu_building_id ");
			sql.append(" = ");
			sql.append(building_id);
			and = true;
		}
		if (account_key_id > 0) {
			sql.append(" and ");
			sql.append(" k.fin_acc_key_id ");
			sql.append(" = ").append(account_key_id);
		}
		if (from != null) {
			sql.append(" and e.payment_date >= '");
			sql.append(from.toString());
			sql.append("'");
		}
		if (to != null) {
			sql.append(" and e.payment_date <= '");
			sql.append(to.toString());
			sql.append("'");
		}

		sql
				.append(" group by b.bu_building_id,b.name,k.fin_acc_key_id,k.name,k.info ");
		sql.append(" order by b.bu_building_id ");

		try {
			return EntityFinder.getInstance().findAll(
					BuildingAccountEntry.class, sql.toString());
		} catch (IDOFinderException ex) {
			ex.printStackTrace();
			System.err.println(sql.toString());
		}
		return null;
	}
}