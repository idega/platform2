/**
 * 
 */
package se.idega.idegaweb.commune.accounting.export.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOHome;

/**
 * @author bluebottle
 *
 */
public interface ExportDataMappingHome extends IDOHome {
	public ExportDataMapping create() throws javax.ejb.CreateException;

	public ExportDataMapping findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;

}
