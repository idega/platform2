package is.idega.idegaweb.golf.entity;

import javax.ejb.*;

import com.idega.data.IDOLegacyEntity;

public interface Union extends com.idega.data.TreeableEntity,IDOLegacyEntity,com.idega.util.datastructures.idegaTreeNode,com.idega.core.data.ICTreeNode
{
 public java.util.Enumeration children();
 public void delete()throws java.sql.SQLException;
 public is.idega.idegaweb.golf.entity.Union[] findAllGolfClubs()throws java.sql.SQLException;
 public java.lang.String getAbbrevation();
 public java.util.List getActiveMembers();
 public java.util.List getAllActiveMembers();
 public java.util.List getAllGolfClubs()throws java.sql.SQLException;
 public java.util.List getAllInActiveMembers();
 public java.util.List getAllMembersInUnion();
 public boolean getAllowsChildren();
 public com.idega.util.datastructures.idegaTreeNode getChildAt(int p0);
 public com.idega.core.data.ICTreeNode getChildAtIndex(int p0);
 public int getChildCount();
 public java.util.Iterator getChildren();
 public java.util.List getGroups(java.lang.String p0);
 public java.lang.String getIDColumnName();
 public java.util.List getInActiveMembers();
 public int getIndex(com.idega.core.data.ICTreeNode p0);
 public int getIndex(com.idega.util.datastructures.idegaTreeNode p0);
 public java.util.List getMembersInUnion();
 public java.lang.String getName();
 public int getNodeID();
 public java.lang.String getNodeName();
 public int getNumber();
 public java.util.List getOwningFields()throws java.sql.SQLException;
 public com.idega.util.datastructures.idegaTreeNode getParent();
 public com.idega.core.data.ICTreeNode getParentNode();
 public int getSiblingCount();
 public java.util.List getTournamentGroups();
 public java.util.List getTournamentGroupsRecursive();
 public java.util.List getUnionGroups();
 public java.util.List getUnionGroupsRecursive();
 public java.lang.String getUnionType();
 public java.util.List getUnionsBetweenZipcodes(is.idega.idegaweb.golf.entity.Union p0,int p1,int p2,int p3,int p4,int p5)throws java.sql.SQLException;
 public java.util.List getUnionsBetweenZipcodes(is.idega.idegaweb.golf.entity.Union p0,int p1,int p2)throws java.sql.SQLException;
 public java.util.List getUnionsBetweenZipcodes(is.idega.idegaweb.golf.entity.Union p0,int p1,int p2,int p3,int p4)throws java.sql.SQLException;
 public java.util.List getUnionsBetweenZipcodes(is.idega.idegaweb.golf.entity.Union p0,int p1,int p2,int p3,int p4,int p5,int p6)throws java.sql.SQLException;
 public java.util.List getUnionsBetweenZipcodesOrderBy(is.idega.idegaweb.golf.entity.Union p0,int p1,int p2,int p3,int p4,int p5,int p6,java.lang.String p7)throws java.sql.SQLException;
 public boolean isLeaf();
 public void setAbbrevation(java.lang.String p0);
 public void setName(java.lang.String p0);
 public void setNumber(java.lang.Integer p0);
 public void setNumber(int p0);
 public void setUnionType(java.lang.String p0);
}
