package is.idega.idegaweb.member.block.importer.business;
import is.idega.idegaweb.member.business.MemberFamilyLogic;

import java.io.LineNumberReader;
import java.io.StringReader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;


import com.idega.block.importer.business.ImportFileHandler;
import com.idega.block.importer.data.ImportFile;
import com.idega.block.process.business.CaseBusiness;
import com.idega.business.IBOServiceBean;
import com.idega.business.IBOSessionBean;
import com.idega.core.business.AddressBusiness;
import com.idega.core.data.Address;
import com.idega.core.data.AddressHome;
import com.idega.core.data.AddressType;
import com.idega.core.data.Country;
import com.idega.core.data.CountryHome;
import com.idega.core.data.PostalCode;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.presentation.IWContext;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Gender;
import com.idega.user.data.GenderHome;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
import com.idega.util.IWTimestamp;
import com.idega.util.Timer;
import com.idega.util.datastructures.HashtableMultivalued;
import com.idega.util.text.TextSoap;

/**
 * <p>Title: PinLookupToGroupImportHandlerBean</p>
 * <p>Description: A simple import file handler that reads file with personalIds and names and if the PIN exists in the database it adds that user to the root group</p>
 * <p>Idega Software Copyright (c) 2002</p>
 * <p>Company: Idega Software</p>
 * @author <a href="mailto:eiki@idega.is">Eirikur Sveinn Hrafnsson</a>
 * @version 1.0
 */



public class PinLookupToGroupImportHandlerBean extends IBOSessionBean implements PinLookupToGroupImportHandler,ImportFileHandler{

	private static final int PIN_COLUMN = 0;
	private static final int NAME_COLUMN = 1;
	
	private List userProperties;
	private UserHome home;
	private AddressBusiness addressBiz;
	private UserBusiness userBiz;
	
	private Group rootGroup;
	private ImportFile file;
	
	
	private UserTransaction transaction;
	
	private ArrayList failedRecords;
  
  private Gender male;
  private Gender female;

  
  public PinLookupToGroupImportHandlerBean(){}

  public boolean handleRecords() throws RemoteException{
  
    transaction =  this.getSessionContext().getUserTransaction();

    Timer clock = new Timer();
    clock.start();

    try {
      //initialize business beans and data homes
      userBiz = (UserBusiness) this.getServiceInstance(UserBusiness.class);
      //addressBiz = (AddressBusiness) this.getServiceInstance(AddressBusiness.class);
			
			failedRecords = new ArrayList();

      //if the transaction failes all the users and their relations are removed
      transaction.begin();

      //iterate through the records and process them
      String item;

      int count = 0;
      while ( !(item=(String)file.getNextRecord()).equals("") ) {
        count++;
        if( ! processRecord(item) ) failedRecords.add(item);
      }

      clock.stop();
      System.out.println("Time to handleRecords: "+clock.getTime()+" ms  OR "+((int)(clock.getTime()/1000))+" s.");

      // System.gc();
      //success commit changes
      transaction.commit();
      return true;
    }
    catch (Exception ex) {
     ex.printStackTrace();

     try {
      transaction.rollback();
     }
     catch (SystemException e) {
       e.printStackTrace();
     }

     return false;
    }

  }

  private boolean processRecord(String record) throws RemoteException{
    userProperties = file.getValuesFromRecordString(record);
    
    User user = null;
    //variables
    String name = (String) userProperties.get(NAME_COLUMN);
    String PIN = (String) userProperties.get(PIN_COLUMN);
    
    if(PIN == null ) return false;
		
   // Gender gender = getGenderFromPin(PIN);
    //IWTimestamp dateOfBirth = getBirthDateFromPin(PIN);
    
    boolean updateName = false;

    try{
     user = userBiz.getUser(PIN);
    }
    catch(Exception e){
      e.printStackTrace();
      return false;
    }
    

		rootGroup.addGroup(user);
    
			/*
    
        String streetName = addressBiz.getStreetNameFromAddressString(addressLine);
        String streetNumber = addressBiz.getStreetNumberFromAddressString(addressLine);
        String postalCode = getUserProperty(POSTAL_CODE_COLUMN);
        String postalName = getUserProperty(POSTAL_NAME_COLUMN);

        Address address = userBiz.getUsersMainAddress(user);
        Country sweden = ((CountryHome)getIDOHome(Country.class)).findByIsoAbbreviation("SE");
        PostalCode code = addressBiz.getPostalCodeAndCreateIfDoesNotExist(postalCode,postalName,sweden);

        boolean addAddress = false;

        if( address == null ){
          AddressHome addressHome = addressBiz.getAddressHome();
          address = addressHome.create();
          AddressType mainAddressType = addressHome.getAddressType1();
          address.setAddressType(mainAddressType);
          addAddress = true;
        }

        address.setCountry(sweden);
        address.setPostalCode(code);
        //address.setProvince("Nacka" );//set as 01 ?
        //address.setCity("Stockholm" );//set as 81?
				address.setProvince(county );
				address.setCity(commune );
        
        
        address.setStreetName(streetName);
        address.setStreetNumber(streetNumber);

        address.store();

        if(addAddress){
          user.addAddress(address);
        }

	    }
	     catch(Exception e){
	      e.printStackTrace();
	      return false;
	    }
	    */


    //finished with this user
    user = null;
    return true;
  }



  public void setImportFile(ImportFile file){
    this.file = file;
  }

  
/**
 * @see com.idega.block.importer.business.ImportFileHandler#setRootGroup(Group)
 */
  public void setRootGroup(Group group){
  	rootGroup = group;
  }
  
  
	  /**
	 * @see com.idega.block.importer.business.ImportFileHandler#getFailedRecords()
	 */
	public List getFailedRecords(){
		return failedRecords;	
	}
	
	
}