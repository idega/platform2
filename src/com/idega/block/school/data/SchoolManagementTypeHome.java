package com.idega.block.school.data;


public interface SchoolManagementTypeHome extends com.idega.data.IDOHome
{
 public SchoolManagementType create() throws javax.ejb.CreateException;
 public SchoolManagementType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllManagementTypes()throws javax.ejb.FinderException;
 public SchoolManagementType findCooperativeManagementType()throws javax.ejb.FinderException;
 public SchoolManagementType findManagementType(java.lang.String p0)throws javax.ejb.FinderException;
 public SchoolManagementType findPrivateManagementType()throws javax.ejb.FinderException;
 public SchoolManagementType findPublicManagementType()throws javax.ejb.FinderException;

}