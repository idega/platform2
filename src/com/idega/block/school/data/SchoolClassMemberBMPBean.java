package com.idega.block.school.data;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
import com.idega.user.data.User;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class SchoolClassMemberBMPBean extends GenericEntity implements SchoolClassMember {

  public final static String SCHOOLCLASSMEMBER = "sch_class_member";
  public final static String MEMBER = "ic_user_id";
  public final static String SCHOOLCLASS = "sch_school_class_id";
  public final static String REGISTER_DATE = "register_date";
  public final static String REGISTRATOR = "registrator";

  public void initializeAttributes() {
    this.addAttribute(getIDColumnName());
    this.addAttribute(MEMBER,"classmember",true,true,Integer.class,MANY_TO_ONE ,com.idega.core.user.data.User.class);
    this.addAttribute(SCHOOLCLASS,"class",true,true,Integer.class,MANY_TO_ONE,SchoolClass.class);
    this.addAttribute(REGISTER_DATE,"registerdate",true,true,Timestamp.class);
    this.addAttribute(REGISTRATOR,"registrator",true,true,Integer.class,MANY_TO_ONE,com.idega.core.user.data.User.class);
  }
  public String getEntityName() {
    return SCHOOLCLASSMEMBER;
  }
  public void setClassMemberId(int id){
    this.setColumn(MEMBER,id);
  }
  public int getClassMemberId(){
    return this.getIntColumnValue(MEMBER);
  }
  public void setSchoolClassId(int id){
    this.setColumn(SCHOOLCLASS,id);
  }
  public int getSchoolClassId(){
    return this.getIntColumnValue(SCHOOLCLASS);
  }
  public void setRegisterDate(Timestamp stamp){
    this.setColumn(REGISTER_DATE,stamp);
  }
  public Timestamp getRegisterDate(){
    return (Timestamp) this.getColumnValue(REGISTER_DATE);
  }
  public void setRegistratorId(int id){
    this.setColumn(REGISTRATOR,id);
  }
  public int getRegistratorId(){
    return this.getIntColumnValue(REGISTRATOR);
  }

  public Integer ejbFindByUserAndSchoolClass(User user, SchoolClass schoolClass) throws FinderException, RemoteException{
  	IDOQuery sql = new IDOQuery();
  	sql.appendSelectAllFrom(this).appendWhere().append(MEMBER).appendEqualSign().append(((Integer)user.getPrimaryKey()).intValue())
  	.appendAnd().append(SCHOOLCLASS).appendEqualSign().append(((Integer)schoolClass.getPrimaryKey()).intValue());

  	return (Integer)this.idoFindOnePKBySQL(sql.toString());
  }

  public Integer ejbFindByUserAndSeason(User user, SchoolSeason season) throws FinderException, RemoteException{
    IDOQuery sql = new IDOQuery();
    sql.appendSelectAllFrom(this.getTableName()+" mb"+","+SchoolClassBMPBean.SCHOOLCLASS +" cl")
    .appendWhere().append(" mb."+MEMBER).appendEqualSign().append(((Integer)user.getPrimaryKey()).intValue())
    .appendAnd().append("cl."+SchoolClassBMPBean.SEASON).appendEqualSign().append(((Integer)season.getPrimaryKey()).intValue())
    .appendAnd().append(" mb."+SCHOOLCLASS).appendEqualSign().append("cl."+SchoolClassBMPBean.SCHOOLCLASS+"_id");
    //System.err.println(sql.toString());
    return (Integer)this.idoFindOnePKBySQL(sql.toString());
  }


}