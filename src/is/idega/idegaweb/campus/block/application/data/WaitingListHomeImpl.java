/**
 * 
 */
package is.idega.idegaweb.campus.block.application.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOFactory;

/**
 * @author bluebottle
 *
 */
public class WaitingListHomeImpl extends IDOFactory implements WaitingListHome {
	protected Class getEntityInterfaceClass() {
		return WaitingList.class;
	}

	public WaitingList create() throws javax.ejb.CreateException {
		return (WaitingList) super.createIDO();
	}

	public WaitingList findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException {
		return (WaitingList) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findByApartmentTypeAndComplexForApplicationType(
			int aprtId, int complexId) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((WaitingListBMPBean) entity)
				.ejbFindByApartmentTypeAndComplexForApplicationType(aprtId,
						complexId);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByApartmentTypeAndComplexForTransferType(int aprtId,
			int complexId) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((WaitingListBMPBean) entity)
				.ejbFindByApartmentTypeAndComplexForTransferType(aprtId,
						complexId);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByApartmentTypeAndComplex(int aprtId, int complexId)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((WaitingListBMPBean) entity)
				.ejbFindByApartmentTypeAndComplex(aprtId, complexId);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findNextForTransferByApartmentTypeAndComplex(int aprtId,
			int complexId, int orderedFrom) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((WaitingListBMPBean) entity)
				.ejbFindNextForTransferByApartmentTypeAndComplex(aprtId,
						complexId, orderedFrom);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByApartmentType(int[] aprtId) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((WaitingListBMPBean) entity)
				.ejbFindByApartmentType(aprtId);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByApplicantID(Integer ID) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((WaitingListBMPBean) entity)
				.ejbFindByApplicantID(ID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySQL(String sql) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((WaitingListBMPBean) entity)
				.ejbFindBySQL(sql);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

}
