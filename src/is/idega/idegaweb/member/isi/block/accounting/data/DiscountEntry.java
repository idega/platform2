package is.idega.idegaweb.member.isi.block.accounting.data;


import com.idega.user.data.Group;
import java.sql.Timestamp;
import com.idega.data.IDOEntity;

public interface DiscountEntry extends IDOEntity {
	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.DiscountEntryBMPBean#getFinanceEntry
	 */
	public FinanceEntry getFinanceEntry();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.DiscountEntryBMPBean#getMaxIdOnEntry
	 */
	public int getMaxIdOnEntry();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.DiscountEntryBMPBean#getClub
	 */
	public Group getClub();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.DiscountEntryBMPBean#getDivision
	 */
	public Group getDivision();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.DiscountEntryBMPBean#getGroup
	 */
	public Group getGroup();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.DiscountEntryBMPBean#getDateOfEntry
	 */
	public Timestamp getDateOfEntry();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.DiscountEntryBMPBean#setFinanceEntry
	 */
	public void setFinanceEntry(FinanceEntry entry);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.DiscountEntryBMPBean#setMaxIdOnEntry
	 */
	public void setMaxIdOnEntry(int id);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.DiscountEntryBMPBean#setClub
	 */
	public void setClub(Group club);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.DiscountEntryBMPBean#setClubID
	 */
	public void setClubID(int id);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.DiscountEntryBMPBean#setDivision
	 */
	public void setDivision(Group division);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.DiscountEntryBMPBean#setDivisionID
	 */
	public void setDivisionID(int id);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.DiscountEntryBMPBean#setGroup
	 */
	public void setGroup(Group group);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.DiscountEntryBMPBean#setGroupID
	 */
	public void setGroupID(int id);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.DiscountEntryBMPBean#setDateOfEntry
	 */
	public void setDateOfEntry(Timestamp date);
}