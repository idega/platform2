package is.idega.idegaweb.member.business.plugins;

import is.idega.idegaweb.member.presentation.GroupAgeGenderTab;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import com.idega.business.IBOServiceBean;
import com.idega.data.GenericEntity;
import com.idega.presentation.PresentationObject;
import com.idega.user.business.UserGroupPlugInBusiness;
import com.idega.user.data.Gender;
import com.idega.user.data.GenderHome;
import com.idega.user.data.Group;
import com.idega.user.data.User;

/**
 *@author     <a href="mailto:thomas@idega.is">Thomas Hilbig</a>
 *@version    1.0
 */
public class AgeGenderPluginBusinessBean extends IBOServiceBean implements AgeGenderPluginBusiness, UserGroupPlugInBusiness {
  
  private static final int LOWER_AGE_LIMIT_DEFAULT = 0;
  private static final int UPPER_AGE_LIMIT_DEFAULT = 110;
  
  public void setMale(Group group, boolean isMale){
    
    try {
			GenderHome home = (GenderHome) this.getIDOHome(Gender.class);
      if (isMale) {
        String maleId = ((Integer) home.getMaleGender().getPrimaryKey()).toString();
        ((GenericEntity) group).setMetaData("gender", maleId);
      } 
      else  {
        String femaleId = ((Integer) home.getFemaleGender().getPrimaryKey()).toString();
        ((GenericEntity) group).setMetaData("gender", femaleId);
      }
    }
		catch (RemoteException e) {
      System.err.println("[GeneralGroupInfoTab] remote error, GroupId : " + group.getPrimaryKey().toString());
      e.printStackTrace(System.err);
		}
    catch (FinderException e) {
      System.err.println("[GeneralGroupInfoTab] find error, GroupId : " + group.getPrimaryKey().toString());
      e.printStackTrace(System.err);
    }
  }  
  
  public void setMale(Group group)  {
    setMale(group, true);
  }
  
  public void setFemale(Group group, boolean isFemale)  {
    setMale(group, (! isFemale));
  }
  
  public void setFemale(Group group)  {
    setFemale(group, true);
  }
  
  public boolean isMale(Group group) throws RemoteException, FinderException {
    String genderIdString = (String) ((GenericEntity) group).getMetaData("gender");
    if (genderIdString == null)
      // meta data was not set
      // male is default value
      return true;
    Integer genderId = new Integer(genderIdString);
    GenderHome home = (GenderHome) this.getIDOHome(Gender.class);
    Integer maleId = ((Integer) home.getMaleGender().getPrimaryKey());
    Integer femaleId = ((Integer) home.getFemaleGender().getPrimaryKey());
    if (genderId.equals(maleId))
      return true;
    else if (genderId.equals(femaleId))
      return false;
    else 
      throw new FinderException("Id of gender was not found"); 
  }
  
  public boolean isFemale(Group group) throws RemoteException, FinderException  {
    return (! isMale(group));
  }
  
  public void setLowerAgeLimit(Group group, int lowerAgeLimit)  {
    if (lowerAgeLimit == LOWER_AGE_LIMIT_DEFAULT)
      ((GenericEntity) group).removeMetaData("lowerAgeLimit");
    else
      ((GenericEntity) group).setMetaData("lowerAgeLimit", Integer.toString(lowerAgeLimit));   
  }
  
  public int getLowerAgeLimit(Group group)  {
    String lowerAgeLimitString = (String) ((GenericEntity) group).getMetaData("lowerAgeLimit");
    if (lowerAgeLimitString == null)
      return LOWER_AGE_LIMIT_DEFAULT;
    else
      return Integer.parseInt(lowerAgeLimitString); 
  }
  
  public void setUpperAgeLimit(Group group, int upperAgeLimit)  {
    if (upperAgeLimit == UPPER_AGE_LIMIT_DEFAULT)
      ((GenericEntity) group).removeMetaData("upperAgeLimit");
    else
      ((GenericEntity) group).setMetaData("upperAgeLimit", Integer.toString(upperAgeLimit));
  }
 
  public int getUpperAgeLimit(Group group)  {
    String upperAgeLimitString = (String) ((GenericEntity) group).getMetaData("lowerAgeLimit");
    if (upperAgeLimitString == null)
      return UPPER_AGE_LIMIT_DEFAULT;
    else
      return Integer.parseInt(upperAgeLimitString); 
  }
  
  
  
	/**
	 * @see com.idega.user.business.UserGroupPlugInBusiness#afterGroupCreate(com.idega.user.data.Group)
	 */
	public void afterGroupCreate(Group group) throws CreateException, RemoteException {
	}

	/**
	 * @see com.idega.user.business.UserGroupPlugInBusiness#afterUserCreate(com.idega.user.data.User)
	 */
	public void afterUserCreate(User user) throws CreateException, RemoteException {
	}

	/**
	 * @see com.idega.user.business.UserGroupPlugInBusiness#beforeGroupRemove(com.idega.user.data.Group)
	 */
	public void beforeGroupRemove(Group group) throws RemoveException, RemoteException {
	}

	/**
	 * @see com.idega.user.business.UserGroupPlugInBusiness#beforeUserRemove(com.idega.user.data.User)
	 */
	public void beforeUserRemove(User user) throws RemoveException, RemoteException {
	}

	/**
	 * @see com.idega.user.business.UserGroupPlugInBusiness#findGroupsByFields(java.util.Collection, java.util.Collection, java.util.Collection)
	 */
	public Collection findGroupsByFields(Collection listViewerFields, Collection finderOperators, Collection listViewerFieldValues) throws RemoteException {
		return null;
	}

	/**
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getGroupPropertiesTabs(com.idega.user.data.Group)
	 */
	public List getGroupPropertiesTabs(Group group) throws RemoteException {
    List list = new ArrayList();
    list.add(new GroupAgeGenderTab(group));  
    return list;  
	}

	/**
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getListViewerFields()
	 */
	public Collection getListViewerFields() throws RemoteException {
		return null;
	}

	/**
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getPresentationObjectClass()
	 */
	public Class getPresentationObjectClass() throws RemoteException {
		return null;
	}

	/**
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getUserPropertiesTabs(com.idega.user.data.User)
	 */
	public List getUserPropertiesTabs(User user) throws RemoteException {
		return null;
	}

	/**
	 * @see com.idega.user.business.UserGroupPlugInBusiness#instanciateEditor(com.idega.user.data.Group)
	 */
	public PresentationObject instanciateEditor(Group group) throws RemoteException {
		return null;
	}

	/**
	 * @see com.idega.user.business.UserGroupPlugInBusiness#instanciateViewer(com.idega.user.data.Group)
	 */
	public PresentationObject instanciateViewer(Group group) throws RemoteException {
		return null;
	}

}
