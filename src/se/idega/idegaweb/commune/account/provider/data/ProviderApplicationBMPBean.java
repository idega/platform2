package se.idega.idegaweb.commune.account.provider.data;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;
import se.idega.idegaweb.commune.account.data.AccountApplication;

import com.idega.core.location.data.PostalCode;
import com.idega.data.*;
import com.idega.util.ListUtil;
import com.idega.block.process.data.*;
import com.idega.block.school.data.SchoolArea;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolTypeHome;
/**
 * Title:        idegaWeb
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      idega software
 * @author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */
public class ProviderApplicationBMPBean
	extends AbstractCaseBMPBean
	implements ProviderApplication, Case, AccountApplication
{
	private static final String CASE_CODE_KEY = "ANANSKO";
	private static final String TABLE_NAME = "COMM_ACC_PROV";
	private static final String COLUMN_EMAIL = "PROV_EMAIL";
	private static final String COLUMN_NAME = "PROV_NAME";
	private static final String COLUMN_ADDRESS = "PROV_ADDRESS";
	private static final String COLUMN_PLACES = "PROV_PLACES";
	private static final String COLUMN_PHONE = "PROV_PHONE";
	private static final String COLUMN_ADDITIONAL_INFO = "PROV_ADD_INFO";
	private static final String COLUMN_MANAGER_NAME = "PROV_MANAGER_NAME";
	//private static final String COLUMN_SCHOOL_TYPE = "SCH_TYPE";
	private static final String COLUMN_SCHOOL_AREA = "SCH_AREA";
	private static final String COLUMN_POSTAL_CODE = "POSTAL_CODE";
		
	public void initializeAttributes()
	{
		addGeneralCaseRelation();
		this.addAttribute(COLUMN_NAME, "Provider name", String.class);
		this.addAttribute(COLUMN_ADDRESS, "Provider address", String.class);
		this.addAttribute(COLUMN_PHONE, "Telephone", String.class, 30);
		this.addAttribute(COLUMN_MANAGER_NAME, "Manager Name", String.class);
		this.addAttribute(COLUMN_EMAIL, "Manager email", String.class);
		this.addAttribute(COLUMN_PLACES, "Provider places", Integer.class);
		this.addAttribute(COLUMN_ADDITIONAL_INFO, "Additional info", String.class, 2000);
		
		//this.addManyToOneRelationship(COLUMN_SCHOOL_TYPE,SchoolType.class);
		this.addManyToManyRelationShip(SchoolType.class);
		
		this.addManyToOneRelationship(COLUMN_SCHOOL_AREA,SchoolArea.class);
		this.addManyToOneRelationship(COLUMN_POSTAL_CODE,PostalCode.class);
		
	}
	public String getEntityName()
	{
		return this.TABLE_NAME;
	}
	public String getCaseCodeKey()
	{
		return CASE_CODE_KEY;
	}
	public String getCaseCodeDescription()
	{
		return "Application for School Administrators";
	}
	public String getEmailAddress()
	{
		return this.getStringColumnValue(COLUMN_EMAIL);
	}
	public void setEmailAddress(String emailAddress)
	{
		this.setColumn(COLUMN_EMAIL, emailAddress);
	}
	public String getName()
	{
		return this.getStringColumnValue(COLUMN_NAME);
	}
	public void setName(String providerName)
	{
		this.setColumn(COLUMN_NAME, providerName);
	}
	public String getAddress()
	{
		return this.getStringColumnValue(COLUMN_ADDRESS);
	}
	public void setAddress(String providerAddress)
	{
		this.setColumn(COLUMN_ADDRESS, providerAddress);
	}
	public String getPhone()
	{
		return this.getStringColumnValue(COLUMN_PHONE);
	}
	public void setPhone(String phoneString)
	{
		this.setColumn(COLUMN_PHONE, phoneString);
	}
	public int getNumberOfPlaces()
	{
		return this.getIntColumnValue(COLUMN_PLACES);
	}
	public void setNumberOfPlaces(int places)
	{
		this.setColumn(COLUMN_PLACES, places);
	}
	public String getManagerName()
	{
		return this.getStringColumnValue(COLUMN_MANAGER_NAME);
	}
	public void setManagerName(String name)
	{
		this.setColumn(COLUMN_MANAGER_NAME, name);
	}
	public String getAdditionalInfo()
	{
		return this.getStringColumnValue(COLUMN_ADDITIONAL_INFO);
	}
	public void setAdditionalInfo(String info)
	{
		this.setColumn(COLUMN_ADDITIONAL_INFO, info);
	}
	/**
	 * @see se.idega.idegaweb.commune.account.data.AccountApplication#getApplicantName()
	 */
	public String getApplicantName() throws RemoteException
	{
		return getManagerName();
	}
	/**
	 * @see se.idega.idegaweb.commune.account.data.AccountApplication#getEmail()
	 */
	public String getEmail() throws RemoteException
	{
		return this.getEmailAddress();
	}
	/**
	 * @see se.idega.idegaweb.commune.account.data.AccountApplication#setApplicantName(String)
	 */
	public void setApplicantName(String p0) throws RemoteException
	{
		setManagerName(p0);
	}
	/**
	 * @see se.idega.idegaweb.commune.account.data.AccountApplication#setEmail(String)
	 */
	public void setEmail(String p0) throws RemoteException
	{
		setEmailAddress(p0);
	}

/**

	//
	 //Gets the school type according to this application
	 //
	public SchoolType getSchoolType()
	{
		return (SchoolType)this.getColumnValue(COLUMN_SCHOOL_TYPE);
	}

	//
	 //Sets the school type according to this application
	 //
	public void setSchoolType(SchoolType type) throws RemoteException
	{
		setColumn(COLUMN_SCHOOL_TYPE,type);
	}

	//
	//Sets the school type according to this application
	//
	public void setSchoolType(int typeID) throws RemoteException
	{
		setColumn(COLUMN_SCHOOL_TYPE,typeID);
	}
**/

	//
	 //Gets the school type according to this application
	 //
	public Collection getSchoolTypes()
	{
		//return (SchoolType)this.getColumnValue(COLUMN_SCHOOL_TYPE);
		try{
			return this.idoGetRelatedEntities(SchoolType.class);
		}
		catch(Exception e){
			e.printStackTrace();
			return ListUtil.getEmptyList();
		}
	}

	//
	 //Sets the school type according to this application
	 //
	public void setSchoolTypes(Collection types) 
	{
		//setColumn(COLUMN_SCHOOL_TYPE,type);
		for (Iterator iterator = types.iterator(); iterator.hasNext();)
		{
			try{
				SchoolType element = (SchoolType) iterator.next();
				addSchoolType(element);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	//
	//Sets the school type according to this application
	//
	public void setSchoolTypes(int[] typeIDs)
	{
		//setColumn(COLUMN_SCHOOL_TYPE,typeID);
		for (int i = 0; i < typeIDs.length; i++)
		{
			int id = typeIDs[i];
			try{
				SchoolType type  = getSchoolTypeHome().findByPrimaryKey(new Integer(id));
				addSchoolType(type);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	/**
	 * Method getSchoolTypeHome.
	 */
	private SchoolTypeHome getSchoolTypeHome()throws RemoteException
	{
		return (SchoolTypeHome)IDOLookup.getHome(SchoolType.class);
	}


	/**
	 * Adds a schooltype to this application
	 **/
	public void addSchoolType(SchoolType type){
			try{
				this.idoAddTo(type);
			}
			catch(Exception e){
				e.printStackTrace();
			}	
	}

	/**
	 *Gets the school area according to this application
	 */
	public SchoolArea getSchoolArea()
	{
		return (SchoolArea)this.getColumnValue(COLUMN_SCHOOL_AREA);
	}

	/**
	 *Sets the postal code according to this application
	 */
	public void setPostalCode(PostalCode code) throws RemoteException
	{
		setColumn(COLUMN_POSTAL_CODE,code);
	}

	/**
	 *Gets the postal code according to this application
	 */
	public void setPostalCode(int postalCodeID) throws RemoteException
	{
		setColumn(COLUMN_POSTAL_CODE,postalCodeID);
	}

	/**
	 *Gets the postal code according to this application
	 */
	public PostalCode getPostalCode()
	{
		return (PostalCode)this.getColumnValue(COLUMN_POSTAL_CODE);
	}

	/**
	 *Sets the school area according to this application
	 */
	public void setSchoolArea(SchoolArea area) throws RemoteException
	{
		setColumn(COLUMN_SCHOOL_AREA,area);
	}

	/**
	 *Sets the school area according to this application
	 */
	public void setSchoolArea(int areaID) throws RemoteException
	{
		setColumn(COLUMN_SCHOOL_AREA,areaID);
	}

	/**
	 * Mandates EJB standard behaviour
	 **/
	protected boolean doInsertInCreate(){
		return true;
	}


	public Collection ejbFindAllPendingApplications() throws FinderException
	{
		try
		{
			return super.ejbFindAllCasesByStatus(this.getCaseStatusOpen().toString());
		}
		catch (RemoteException e)
		{
			throw new IDOFinderException(e);
		}
		//return null;
	}
	public Collection ejbFindAllRejectedApplications() throws FinderException
	{
		try
		{
			return super.ejbFindAllCasesByStatus(this.getCaseStatusDenied().toString());
		}
		catch (RemoteException e)
		{
			throw new IDOFinderException(e);
		}
		//return null;
	}
	public Collection ejbFindAllApprovedApplications() throws FinderException
	{
		try
		{
			return super.ejbFindAllCasesByStatus(this.getCaseStatusGranted().toString());
		}
		catch (RemoteException e)
		{
			throw new IDOFinderException(e);
		}
		//return null;
	}
}