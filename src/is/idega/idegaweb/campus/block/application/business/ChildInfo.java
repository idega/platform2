/*
 * Created on Jul 21, 2003
 *
 */
package is.idega.idegaweb.campus.block.application.business;



/**
 * ChildrenInfo
 * @author aron 
 * @version 1.0
 */

public class ChildInfo {
	
	private Integer ID;
	private String name;
	private String ssn;
	
	public ChildInfo(Integer ID, String name,String ssn){
		this.ID = ID;
		this.name = name;
		this.ssn = ssn;
	}
	
	
	/**
	 * @return
	 */
	public Integer getID() {
		return ID;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return
	 */
	public String getSsn() {
		return ssn;
	}

	/**
	 * @param integer
	 */
	public void setID(Integer integer) {
		ID = integer;
	}

	/**
	 * @param string
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * @param string
	 */
	public void setSsn(String string) {
		ssn = string;
	}

}
