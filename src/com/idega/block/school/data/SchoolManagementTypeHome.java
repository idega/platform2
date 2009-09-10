package com.idega.block.school.data;


public interface SchoolManagementTypeHome extends com.idega.data.IDOHome
{
 public SchoolManagementType create() throws javax.ejb.CreateException;
 public SchoolManagementType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllManagementTypes()throws javax.ejb.FinderException;
 public SchoolManagementType findCommuneManagementType()throws javax.ejb.FinderException;
 public SchoolManagementType findCompanyManagementType()throws javax.ejb.FinderException;
 public SchoolManagementType findCooperativeCommuneLiabilityManagementType()throws javax.ejb.FinderException;
 public SchoolManagementType findCountyCouncilManagementType()throws javax.ejb.FinderException;
 public SchoolManagementType findFoundationManagementType()throws javax.ejb.FinderException;
 public SchoolManagementType findGovernmentManagementType()throws javax.ejb.FinderException;
 public SchoolManagementType findManagementType(java.lang.String p0)throws javax.ejb.FinderException;
 public java.util.Collection findManagementTypesByCategories(java.lang.String[] p0)throws javax.ejb.FinderException;
 public SchoolManagementType findPrivateManagementType()throws javax.ejb.FinderException;

}