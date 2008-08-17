package is.idega.idegaweb.campus.block.application.data;


import com.idega.block.building.data.ApartmentSubcategory;
import com.idega.block.application.data.Application;
import com.idega.block.building.data.Apartment;
import com.idega.block.application.data.Applicant;
import javax.ejb.FinderException;
import java.sql.Timestamp;
import com.idega.data.IDOEntity;

public interface WaitingList extends IDOEntity {
	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#setApartmentSubcategory
	 */
	public void setApartmentSubcategory(int id);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#setApartmentSubcategory
	 */
	public void setApartmentSubcategory(Integer id);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#setApartmentSubcategory
	 */
	public void setApartmentSubcategory(ApartmentSubcategory subcategory);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#getApartmentSubcategoryID
	 */
	public Integer getApartmentSubcategoryID();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#getApartmentSubcategory
	 */
	public ApartmentSubcategory getApartmentSubcategory();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#setApplicantId
	 */
	public void setApplicantId(int id);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#setApplicantId
	 */
	public void setApplicantId(Integer id);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#getApplicantId
	 */
	public Integer getApplicantId();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#getApplicant
	 */
	public Applicant getApplicant();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#setTypeApplication
	 */
	public void setTypeApplication();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#setTypeTransfer
	 */
	public void setTypeTransfer();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#getType
	 */
	public String getType();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#setOrder
	 */
	public void setOrder(int order);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#setOrder
	 */
	public void setOrder(Integer order);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#getOrder
	 */
	public Integer getOrder();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#setLastConfirmationDate
	 */
	public void setLastConfirmationDate(Timestamp date);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#getLastConfirmationDate
	 */
	public Timestamp getLastConfirmationDate();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#setAcceptedDate
	 */
	public void setAcceptedDate(Timestamp date);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#getAcceptedDate
	 */
	public Timestamp getAcceptedDate();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#setNumberOfRejections
	 */
	public void setNumberOfRejections(int count);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#setNumberOfRejections
	 */
	public void setNumberOfRejections(Integer count);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#incrementRejections
	 */
	public void incrementRejections(boolean flagAsRejected);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#getNumberOfRejections
	 */
	public int getNumberOfRejections();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#getRejectFlag
	 */
	public boolean getRejectFlag();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#setRejectFlag
	 */
	public void setRejectFlag(boolean flag);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#setChoiceNumber
	 */
	public void setChoiceNumber(int choice);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#setChoiceNumber
	 */
	public void setChoiceNumber(Integer choice);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#getChoiceNumber
	 */
	public Integer getChoiceNumber();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#getRemovedFromList
	 */
	public boolean getRemovedFromList();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#setRemovedFromList
	 */
	public void setRemovedFromList(String removed);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#getPriorityLevel
	 */
	public String getPriorityLevel();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#setPriorityLevel
	 */
	public void setPriorityLevel(String level);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#setPriorityLevelA
	 */
	public void setPriorityLevelA();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#setPriorityLevelB
	 */
	public void setPriorityLevelB();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#setPriorityLevelC
	 */
	public void setPriorityLevelC();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#setPriorityLevelD
	 */
	public void setPriorityLevelD();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#setPriorityLevelE
	 */
	public void setPriorityLevelE();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#setSamePriority
	 */
	public void setSamePriority(WaitingList listEntry);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#getApartment
	 */
	public Apartment getApartment();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#setApartment
	 */
	public void setApartment(Apartment apartment);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#getApplication
	 */
	public Application getApplication();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#getApplicationID
	 */
	public int getApplicationID();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#setApplication
	 */
	public void setApplication(Application application);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#setApplicationID
	 */
	public void setApplicationID(int id);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#getCountOfRecords
	 */
	public int getCountOfRecords(String sql) throws FinderException;
}