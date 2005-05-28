/*
 * $Id: ServiceSearchEngineHome.java,v 1.6 2005/05/28 00:30:05 gimmi Exp $
 * Created on 24.5.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.block.search.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOHome;
import com.idega.user.data.Group;


/**
 * 
 *  Last modified: $Date: 2005/05/28 00:30:05 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.6 $
 */
public interface ServiceSearchEngineHome extends IDOHome {

	public ServiceSearchEngine create() throws javax.ejb.CreateException;

	public ServiceSearchEngine findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#ejbFindByName
	 */
	public ServiceSearchEngine findByName(String name) throws FinderException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#ejbFindByCode
	 */
	public ServiceSearchEngine findByCode(String code) throws FinderException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#ejbFindAll
	 */
	public Collection findAll(Group supplierManager) throws FinderException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#ejbFindByGroupID
	 */
	public ServiceSearchEngine findByGroupID(int groupID) throws FinderException;
}
