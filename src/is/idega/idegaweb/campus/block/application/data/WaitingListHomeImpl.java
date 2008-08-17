package is.idega.idegaweb.campus.block.application.data;


import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.block.application.data.Application;
import javax.ejb.FinderException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class WaitingListHomeImpl extends IDOFactory implements WaitingListHome {
	public Class getEntityInterfaceClass() {
		return WaitingList.class;
	}

	public WaitingList create() throws CreateException {
		return (WaitingList) super.createIDO();
	}

	public WaitingList findByPrimaryKey(Object pk) throws FinderException {
		return (WaitingList) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findByApartmentSubcategoryForApplicationType(int subcatId)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((WaitingListBMPBean) entity)
				.ejbFindByApartmentSubcategoryForApplicationType(subcatId);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByApartmentSubcategoryForTransferType(int subcatId)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((WaitingListBMPBean) entity)
				.ejbFindByApartmentSubcategoryForTransferType(subcatId);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByApartmentSubcategory(int subcatId)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((WaitingListBMPBean) entity)
				.ejbFindByApartmentSubcategory(subcatId);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findNextForTransferByApartmentSubcategory(int subcatId,
			int orderedFrom, String setTranserferToPriorityLevel)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((WaitingListBMPBean) entity)
				.ejbFindNextForTransferByApartmentSubcategory(subcatId,
						orderedFrom, setTranserferToPriorityLevel);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByApartmentSubcategory(int[] subcatId)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((WaitingListBMPBean) entity)
				.ejbFindByApartmentSubcategory(subcatId);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByApplicantID(Integer ID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((WaitingListBMPBean) entity).ejbFindByApplicantID(ID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByApplication(Application application)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((WaitingListBMPBean) entity)
				.ejbFindByApplication(application);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySQL(String sql) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((WaitingListBMPBean) entity).ejbFindBySQL(sql);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}