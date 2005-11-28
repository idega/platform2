/**
 * 
 */
package is.idega.idegaweb.campus.block.application.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOException;
import com.idega.data.IDOFactory;
import com.idega.data.IDORelationshipException;

/**
 * @author bluebottle
 *
 */
public class CampusApplicationHomeImpl extends IDOFactory implements
		CampusApplicationHome {
	protected Class getEntityInterfaceClass() {
		return CampusApplication.class;
	}

	public CampusApplication create() throws javax.ejb.CreateException {
		return (CampusApplication) super.createIDO();
	}

	public CampusApplication findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException {
		return (CampusApplication) super.findByPrimaryKeyIDO(pk);
	}

	public java.util.Collection findAllByApplicationId(int id)
			throws javax.ejb.FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((CampusApplicationBMPBean) entity)
				.ejbFindAllByApplicationId(id);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySubjectAndStatus(Integer subjectID, String status,
			String order) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((CampusApplicationBMPBean) entity)
				.ejbFindBySubjectAndStatus(subjectID, status, order);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySubjectAndStatus(Integer subjectID, String status,
			String order, int numberOfRecords, int startingIndex)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((CampusApplicationBMPBean) entity)
				.ejbFindBySubjectAndStatus(subjectID, status, order,
						numberOfRecords, startingIndex);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public int getCountBySubjectAndStatus(Integer subjectID, String status)
			throws IDORelationshipException, IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((CampusApplicationBMPBean) entity)
				.ejbHomeGetCountBySubjectAndStatus(subjectID, status);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((CampusApplicationBMPBean) entity)
				.ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySQL(String sql) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((CampusApplicationBMPBean) entity)
				.ejbFindBySQL(sql);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByApartmentTypeAndComplex(Integer typeId,
			Integer complexID) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((CampusApplicationBMPBean) entity)
				.ejbFindByApartmentTypeAndComplex(typeId, complexID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

}
