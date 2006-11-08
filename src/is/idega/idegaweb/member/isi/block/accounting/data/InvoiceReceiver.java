package is.idega.idegaweb.member.isi.block.accounting.data;


import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.data.IDOEntity;

public interface InvoiceReceiver extends IDOEntity {
	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.InvoiceReceiverBMPBean#getUser
	 */
	public User getUser();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.InvoiceReceiverBMPBean#getInvoiceReceiver
	 */
	public User getInvoiceReceiver();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.InvoiceReceiverBMPBean#getClub
	 */
	public Group getClub();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.InvoiceReceiverBMPBean#getDivision
	 */
	public Group getDivision();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.InvoiceReceiverBMPBean#getGroup
	 */
	public Group getGroup();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.InvoiceReceiverBMPBean#setUser
	 */
	public void setUser(User user);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.InvoiceReceiverBMPBean#setInvoiceReceiver
	 */
	public void setInvoiceReceiver(User invoiceReceiver);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.InvoiceReceiverBMPBean#setClub
	 */
	public void setClub(Group club);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.InvoiceReceiverBMPBean#setDivision
	 */
	public void setDivision(Group division);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.InvoiceReceiverBMPBean#setGroup
	 */
	public void setGroup(Group group);
}