package com.idega.block.school.business;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassHome;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolClassMemberHome;
import com.idega.block.school.data.SchoolHome;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.*;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOCreateException;
import com.idega.data.IDOLookup;
import com.idega.data.IDORelationshipException;
import com.idega.idegaweb.IWBundle;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.GroupType;
import com.idega.user.data.GroupTypeHome;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */
public class SchoolBusinessBean extends IBOServiceBean implements SchoolBusiness
{
	public SchoolHome getSchoolHome() throws java.rmi.RemoteException
	{
		return (SchoolHome) IDOLookup.getHome(School.class);
	}

  public SchoolClassMemberHome getSchoolClassMemberHome()throws java.rmi.RemoteException{
    return (SchoolClassMemberHome) IDOLookup.getHome(SchoolClassMember.class);
  }
   public SchoolClassHome getSchoolClassHome()throws java.rmi.RemoteException{
    return (SchoolClassHome) IDOLookup.getHome(SchoolClass.class);
  }
   public SchoolYearHome getSchoolYearHome()throws java.rmi.RemoteException{
    return (SchoolYearHome) IDOLookup.getHome(SchoolYear.class);
  }
   public SchoolAreaHome getSchoolAreaHome()throws java.rmi.RemoteException{
    return (SchoolAreaHome) IDOLookup.getHome(SchoolArea.class);
  }
  public SchoolTypeHome getSchoolTypeHome()throws java.rmi.RemoteException{
    return (SchoolTypeHome) IDOLookup.getHome(SchoolType.class);
  }
	public School getSchool(Object primaryKey)
	{
		try
		{
			SchoolHome shome = getSchoolHome();
			return shome.findByPrimaryKey(primaryKey);
		}
		catch (Exception ex)
		{
			return null;
		}
	}
	public void removeSchool(int id)
	{
		try
		{
			School school = getSchool(new Integer(id));
			school.addSchoolTypesRemoveOther(new int[0]);
			school.addSchoolYearsRemoveOther(new int[0]);
			school.remove();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	public Collection findAllSchools()
	{
		try
		{
			SchoolHome shome = getSchoolHome();
			return shome.findAllSchools();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return new java.util.Vector();
		}
	}
	public Collection findAllSchoolsByAreaAndType(int area, int type)
	{
		try
		{
			SchoolHome shome = getSchoolHome();
			return shome.findAllByAreaAndType(area, type);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return new java.util.Vector();
		}
	}
	public School createSchool(
		String name,
		String address,
		String zipcode,
		String ziparea,
		String phone,
		int school_type)
		throws CreateException
	{
		/**
		 * @todo figure out how to implement
		 */
		int area_id=-1;
		int sch_types[] = { school_type };
		return createSchool(name, null, address, zipcode, ziparea, phone, null, null, null, area_id, sch_types);
	}
	public School createSchool(
		String name,
		String address,
		String zipcode,
		String ziparea,
		String phone,
		int area_id,
		int[] sch_types)
		throws CreateException
	{
		return createSchool(name, null, address, zipcode, ziparea, phone, null, null, null, area_id, sch_types);
	}

	public School createSchool(
		String name,
		String info,
		String address,
		String zipcode,
		String ziparea,
		String phone,
		String keycode,
		String latitude,
		String longitude,
		int area_id,
		int[] type_ids)
		throws CreateException
	{
		return createSchool(
			name,
			info,
			address,
			zipcode,
			ziparea,
			phone,
			keycode,
			latitude,
			longitude,
			area_id,
			type_ids,
			null);
	}
	public School createSchool(
		String name,
		String info,
		String address,
		String zipcode,
		String ziparea,
		String phone,
		String keycode,
		String latitude,
		String longitude,
		int area_id,
		int[] type_ids,
		int[] year_ids)
		throws CreateException
	{
		try
		{
			return storeSchool(
				-1,
				name,
				info,
				address,
				zipcode,
				ziparea,
				phone,
				keycode,
				latitude,
				longitude,
				area_id,
				type_ids,
				year_ids);
		}
		catch (RemoteException rme)
		{
			throw new IDOCreateException(rme);
		}
	}
	public School storeSchool(
		int id,
		String name,
		String info,
		String address,
		String zipcode,
		String ziparea,
		String phone,
		String keycode,
		String latitude,
		String longitude,
		int area_id,
		int[] type_ids,
		int[] year_ids)
		throws java.rmi.RemoteException
	{
		SchoolHome shome = getSchoolHome();
		School newSchool;
		try
		{
			if (id > 0)
			{
				newSchool = shome.findByPrimaryKey(new Integer(id));
			}
			else
			{
				newSchool = shome.create();
			}
			if(newSchool.getHeadmasterGroupId() < 0){

				newSchool.setHeadmasterGroupId( ( (Integer) getNewSchoolGroup(name,name).getPrimaryKey()).intValue() );

			}
		}
		catch (javax.ejb.FinderException fe)
		{
			throw new java.rmi.RemoteException(fe.getMessage());
		}
		catch (javax.ejb.CreateException ce)
		{
			throw new java.rmi.RemoteException(ce.getMessage());
		}

		if(area_id > 0)
			newSchool.setSchoolAreaId(area_id);
		if(address!=null)
			newSchool.setSchoolAddress(address);
		if(info!=null)
			newSchool.setSchoolInfo(info);
		if(keycode!=null)
			newSchool.setSchoolKeyCode(keycode);
		if(latitude!=null)
			newSchool.setSchoolLatitude(latitude);
		if(longitude!=null)
			newSchool.setSchoolLongitude(longitude);
		if(name!=null)
			newSchool.setSchoolName(name);
		if(phone!=null)
			newSchool.setSchoolPhone(phone);
		if(ziparea!=null)
			newSchool.setSchoolZipArea(ziparea);
		if(zipcode!=null)
			newSchool.setSchoolZipCode(zipcode);

		newSchool.store();
		if (type_ids != null)
			newSchool.addSchoolTypesRemoveOther(type_ids);
		if (year_ids != null)
			newSchool.addSchoolYearsRemoveOther(year_ids);
		return newSchool;
	}
	public Map getSchoolRelatedSchoolTypes(School school)
	{
		try
		{
			Collection types = school.findRelatedSchoolTypes();
			if (types != null && !types.isEmpty())
			{
				HashMap map = new HashMap(types.size());
				Iterator iter = types.iterator();
				SchoolType type;
				while (iter.hasNext())
				{
					type = (SchoolType) iter.next();
					map.put((Integer) type.getPrimaryKey(), type);
				}
				return map;
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return null;
	}
	public Map getSchoolRelatedSchoolYears(School school)
	{
		try
		{
			Collection years = school.findRelatedSchoolYears();
			if (years != null && !years.isEmpty())
			{
				HashMap map = new HashMap(years.size());
				Iterator iter = years.iterator();
				SchoolYear year;
				while (iter.hasNext())
				{
					year = (SchoolYear) iter.next();
					map.put(year.getPrimaryKey(), year);
				}
				return map;
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return null;
	}
	public Map getMapOfSchools()
	{
		try
		{
			Collection schools = findAllSchools();
			if (schools != null && !schools.isEmpty())
			{
				HashMap map = new HashMap(schools.size());
				Iterator iter = schools.iterator();
				School school;
				while (iter.hasNext())
				{
					school = (School) iter.next();
					map.put(school.getPrimaryKey(), school);
				}
				return map;
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return null;
	}

	public Group getNewSchoolGroup(String name, String info)throws CreateException,FinderException,RemoteException{
		GroupTypeHome typeHome = (GroupTypeHome) this.getIDOHome(GroupType.class);
		GroupType type = typeHome.create();

		Group rootGroup = this.getRootSchoolGroup();
		Group schoolGroup = getUserBusiness().getGroupBusiness().createGroup(name,info,type.getGeneralGroupTypeString());
		rootGroup.addGroup(schoolGroup);
		return schoolGroup;
	}

	public Group getRootSchoolGroup()throws CreateException,FinderException,RemoteException{
		String ROOT_SCHOOL_GROUP_ID_PARAMETER = "root_school_group_id";
	    Group rootGroup = null;
	    //create the default group
	    IWBundle bundle = this.getIWApplicationContext().getApplication().getBundle("com.idega.block.school");
	   String groupId =  bundle.getProperty(ROOT_SCHOOL_GROUP_ID_PARAMETER);
	    //String groupId = (String) this.getIWApplicationContext().getApplicationSettings().getProperty(ROOT_CITIZEN_GROUP_ID_PARAMETER_NAME);
	    if( groupId!=null ){
	      rootGroup = getUserBusiness().getGroupHome().findByPrimaryKey(new Integer(groupId));

	    }
	    else{
	      System.err.println("trying to store School Root group");
	      /**@todo this seems a wrong way to do things**/
	      GroupTypeHome typeHome = (GroupTypeHome) this.getIDOHome(GroupType.class);
	      GroupType type = typeHome.create();


	      rootGroup = getUserBusiness().getGroupBusiness().createGroup("School Root Group","The School Root Group.",type.getGeneralGroupTypeString());
		  bundle.setProperty(ROOT_SCHOOL_GROUP_ID_PARAMETER, rootGroup.getPrimaryKey().toString());

	    }

    return rootGroup;
  }

  protected UserBusiness getUserBusiness()throws RemoteException{
    return (UserBusiness)this.getServiceInstance(UserBusiness.class);
  }

  public SchoolClass createSchoolClass(String schoolClassName,School school, SchoolYear year,SchoolSeason season) throws CreateException, RemoteException{
  	SchoolClassHome sClassHome = (SchoolClassHome) this.getIDOHome(SchoolClass.class);
  	SchoolClass sClass = sClassHome.create();

  	sClass.setSchoolClassName(schoolClassName);
  	sClass.setSchoolId( ((Integer)school.getPrimaryKey()).intValue() );
  	sClass.setSchoolSeasonId( ((Integer)season.getPrimaryKey()).intValue() );
  	sClass.setSchoolYearId( ((Integer)year.getPrimaryKey()).intValue() );

 	return sClass;
  }

  public SchoolClassMember createSchoolClassMember(SchoolClass sClass, User user) throws CreateException,java.rmi.RemoteException{
	SchoolClassMemberHome sClassMemberHome = (SchoolClassMemberHome) this.getIDOHome(SchoolClassMember.class);
  	SchoolClassMember sClassMember = sClassMemberHome.create();
  	sClassMember.setSchoolClassId(((Integer)sClass.getPrimaryKey()).intValue() );
  	sClassMember.setClassMemberId(((Integer)user.getPrimaryKey()).intValue());
  	sClassMember.setRegisterDate(IWTimestamp.getTimestampRightNow());
  	//NEEDS THE CURRENT USER ID FOR REGISTERING USER

  	return sClassMember;

  }


	public Collection findAllSchoolsByType(int type)
	{
		try
		{
			SchoolHome shome = getSchoolHome();
			return shome.findAllBySchoolType(type);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return new java.util.Vector();
		}
	}

	public Collection findAllSchoolYearsInSchool(int schoolID) throws IDORelationshipException,RemoteException {
		School school = this.getSchool(new Integer(schoolID));
		return school.findRelatedSchoolYears();
	}

}