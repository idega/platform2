package is.idega.idegaweb.golf.service;

import is.idega.idegaweb.golf.entity.AccountYear;
import is.idega.idegaweb.golf.entity.AccountYearHome;
import is.idega.idegaweb.golf.entity.Card;
import is.idega.idegaweb.golf.entity.CardHome;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.Payment;
import is.idega.idegaweb.golf.entity.PaymentType;
import is.idega.idegaweb.golf.entity.PaymentTypeHome;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.entity.UnionHome;
import is.idega.idegaweb.golf.entity.UnionMemberInfo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.util.IWTimestamp;

public class PaymentFiler {
	private int bankoffice;
	private int finalpayday;
	private String giroB1;
	private String giroB2;
	private String giroB3;
	private String giroB4;
	private int roundid;
	private String sFileLink;

	public PaymentFiler() {
		sFileLink = "";
	}

	public void makeFile(IWContext modinfo, String filepath, int payment_type_id, int account_year_id, int union_id) throws IOException, SQLException, FinderException {
		StringBuffer SB = new StringBuffer();
		PaymentType payType = ((PaymentTypeHome) IDOLookup.getHomeLegacy(PaymentType.class)).findByPrimaryKey(payment_type_id);
		AccountYear accYear = ((AccountYearHome) IDOLookup.getHomeLegacy(AccountYear.class)).findByPrimaryKey(account_year_id);
		StringBuffer sql = new StringBuffer("select p.* from payment p, account a , union_member_info umi");
		sql.append(" where umi.member_id = a.member_id and umi.union_id = ");
		sql.append(union_id);
		sql.append(" and umi.member_status = 'A' ");
		sql.append(" and a.account_id = p.account_id and a.account_year_id = ");
		sql.append(account_year_id);
		sql.append(" and p.payment_type_id = ");
		sql.append(payment_type_id);
		sql.append(" order by p.member_id ");
		System.err.println(sql.toString());
		Payment payments[] = (Payment[]) ((Payment) IDOLookup.instanciateEntity(Payment.class)).findAll(sql.toString());
		if (payments.length > 0) {
			Union U = ((UnionHome) IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKey(union_id);
			IWTimestamp datenow = new IWTimestamp();
			StringBuffer fileName = new StringBuffer(U.getAbbrevation());
			fileName.append("_");
			fileName.append(payType.getName());
			fileName.append("_");
			fileName.append(accYear.getName());
			fileName.append("_");
			fileName.append(accYear.getID());
			fileName.append(".dat");
			File outputFile = new File(String.valueOf(filepath) + String.valueOf(fileName.toString()));
			FileWriter out = new FileWriter(outputFile);
			SB.append("Nafn\tKennitala\tFj.Gr.\tUpph\346\360\tKort");
			SB.append("\n");
			char c[] = SB.toString().toCharArray();
			out.write(c);
			int newMemberID = -1;
			int oldMemberID = 0;
			Vector payms = new Vector();
			oldMemberID = payments[0].getMemberId();
			for (int i = 0; i < payments.length; i++) {
				newMemberID = payments[i].getMemberId();
				if (newMemberID != oldMemberID) {
					out.write(getMemberPaymentsString(oldMemberID, union_id, payms));
					payms.clear();
				}
				payms.add(payments[i]);
				oldMemberID = newMemberID;
			}

			out.close();
		}
	}

	private String getMemberPaymentsString(int member_id, int un_id, List payments) throws SQLException, FinderException {
		Member M = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(member_id);
		UnionMemberInfo UMI = M.getUnionMemberInfo(un_id);
		Card card = null;
		if (UMI != null && UMI.getCardId() > 0)
			card = ((CardHome) IDOLookup.getHomeLegacy(Card.class)).findByPrimaryKey(UMI.getCardId());
		int installMents = payments.size();
		int total = 0;
		if (payments != null) {
			for (int i = 0; i < installMents; i++) {
				Payment p = (Payment) payments.get(i);
				total += p.getPrice();
			}

		}
		StringBuffer SB = new StringBuffer(M.getName());
		SB.append("\t");
		SB.append(M.getSocialSecurityNumber());
		SB.append("\t");
		SB.append(installMents);
		SB.append("\t");
		SB.append(total);
		SB.append("\t");
		if (card != null)
			SB.append(card.getCardNumber());
		SB.append("\n");
		if (payments != null) {
			for (int i = 0; i < installMents; i++) {
				Payment p = (Payment) payments.get(i);
				SB.append(p.getInstallmentNr());
				SB.append("\t");
				SB.append((new IWTimestamp(p.getPaymentDate())).getISLDate(".", true));
				SB.append("\t");
				SB.append(p.getPrice());
				SB.append("\t");
				SB.append("\n");
			}

		}
		return SB.toString();
	}

	private void writeFile(String filename, String content) throws IOException {
		File outputFile = new File(filename);
		FileWriter out = new FileWriter(outputFile);
		char c[] = content.toCharArray();
		out.write(c);
		out.close();
	}
}