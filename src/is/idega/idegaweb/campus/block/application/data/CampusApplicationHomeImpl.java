package is.idega.idegaweb.campus.block.application.data;


import com.idega.data.IDOFactory;
import javax.ejb.CreateException;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDOEntity;
import javax.ejb.FinderException;
import com.idega.data.IDOException;
import java.util.Collection;

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

	public CampusApplication findByApplicationId(int id) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((CampusApplicationBMPBean) entity)
				.ejbFindByApplicationId(id);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
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