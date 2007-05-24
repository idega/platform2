package is.idega.idegaweb.campus.block.application.data;


import com.idega.data.IDOException;
import com.idega.data.IDORelationshipException;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class CampusApplicationHomeImpl extends IDOFactory implements
		CampusApplicationHome {
	public Class getEntityInterfaceClass() {
		return CampusApplication.class;
	}

	public CampusApplication create() throws CreateException {
		return (CampusApplication) super.createIDO();
	}

	public CampusApplication findByPrimaryKey(Object pk) throws FinderException {
		return (CampusApplication) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAllByApplicationId(int id) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CampusApplicationBMPBean) entity)
				.ejbFindAllByApplicationId(id);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySubjectAndStatus(Integer subjectID, String status,
			String order) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CampusApplicationBMPBean) entity)
				.ejbFindBySubjectAndStatus(subjectID, status, order);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySubjectAndStatus(Integer subjectID, String status,
			String order, int numberOfRecords, int startingIndex)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CampusApplicationBMPBean) entity)
				.ejbFindBySubjectAndStatus(subjectID, status, order,
						numberOfRecords, startingIndex);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public int getCountBySubjectAndStatus(Integer subjectID, String status)
			throws IDORelationshipException, IDOException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((CampusApplicationBMPBean) entity)
				.ejbHomeGetCountBySubjectAndStatus(subjectID, status);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public Collection findAll() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CampusApplicationBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySQL(String sql) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CampusApplicationBMPBean) entity).ejbFindBySQL(sql);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByApartmentTypeAndComplex(Integer typeId,
			Integer complexID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CampusApplicationBMPBean) entity)
				.ejbFindByApartmentTypeAndComplex(typeId, complexID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySubcategoryAndComplex(Integer subcatId,
			Integer complexID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CampusApplicationBMPBean) entity)
				.ejbFindBySubcategoryAndComplex(subcatId, complexID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}