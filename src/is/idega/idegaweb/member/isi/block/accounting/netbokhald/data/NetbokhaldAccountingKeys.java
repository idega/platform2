package is.idega.idegaweb.member.isi.block.accounting.netbokhald.data;


import com.idega.data.IDOEntity;

public interface NetbokhaldAccountingKeys extends IDOEntity {
	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.netbokhald.data.NetbokhaldAccountingKeysBMPBean#getSetup
	 */
	public NetbokhaldSetup getSetup();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.netbokhald.data.NetbokhaldAccountingKeysBMPBean#getType
	 */
	public String getType();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.netbokhald.data.NetbokhaldAccountingKeysBMPBean#getKey
	 */
	public int getKey();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.netbokhald.data.NetbokhaldAccountingKeysBMPBean#getDebetKey
	 */
	public String getDebetKey();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.netbokhald.data.NetbokhaldAccountingKeysBMPBean#getCreditKey
	 */
	public String getCreditKey();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.netbokhald.data.NetbokhaldAccountingKeysBMPBean#setSetup
	 */
	public void setSetup(NetbokhaldSetup setup);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.netbokhald.data.NetbokhaldAccountingKeysBMPBean#setType
	 */
	public void setType(String type);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.netbokhald.data.NetbokhaldAccountingKeysBMPBean#setKey
	 */
	public void setKey(String key);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.netbokhald.data.NetbokhaldAccountingKeysBMPBean#setDebetKey
	 */
	public void setDebetKey(String key);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.netbokhald.data.NetbokhaldAccountingKeysBMPBean#setCreditKey
	 */
	public void setCreditKey(String key);
}