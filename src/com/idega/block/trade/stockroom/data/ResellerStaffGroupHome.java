package com.idega.block.trade.stockroom.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOHome;


/**
 * @author gimmi
 */
public interface ResellerStaffGroupHome extends IDOHome {

	public ResellerStaffGroup create() throws javax.ejb.CreateException;

	public ResellerStaffGroup findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ResellerStaffGroupBMPBean#ejbFindGroupsByName
	 */
	public Collection findGroupsByName(String name) throws FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ResellerStaffGroupBMPBean#ejbFindGroupsByNameAndDescription
	 */
	public Collection findGroupsByNameAndDescription(String name, String description) throws FinderException;
}
