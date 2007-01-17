package is.idega.idegaweb.member.isi.block.accounting.netbokhald.data;


import com.idega.user.data.Group;
import com.idega.data.IDOEntity;

public interface NetbokhaldSetup extends IDOEntity {
	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.netbokhald.data.NetbokhaldSetupBMPBean#getPrimaryKeyClass
	 */
	public Class getPrimaryKeyClass();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.netbokhald.data.NetbokhaldSetupBMPBean#getExternalID
	 */
	public String getExternalID();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.netbokhald.data.NetbokhaldSetupBMPBean#getClub
	 */
	public Group getClub();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.netbokhald.data.NetbokhaldSetupBMPBean#getDivision
	 */
	public Group getDivision();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.netbokhald.data.NetbokhaldSetupBMPBean#getGroup
	 */
	public Group getGroup();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.netbokhald.data.NetbokhaldSetupBMPBean#setExternalID
	 */
	public void setExternalID(String id);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.netbokhald.data.NetbokhaldSetupBMPBean#setClub
	 */
	public void setClub(Group club);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.netbokhald.data.NetbokhaldSetupBMPBean#setDivision
	 */
	public void setDivision(Group division);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.netbokhald.data.NetbokhaldSetupBMPBean#setGroup
	 */
	public void setGroup(Group group);
}