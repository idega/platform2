/*
 * $Id: Tenants.java,v 1.2 2001/11/08 15:40:39 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.entity;


import java.sql.*;
import com.idega.data.*;

/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class Tenants extends GenericEntity {

  public Tenants() {
  }
  public Tenants(int id) throws SQLException {
    super(id);
  }
  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute("member_id","Heiti",true,true,"java.lang.Integer","one-to-one","com.idega.data.genericentity.Member");
    addAttribute("room_id", "Verð", true, true, "java.lang.Integer","one-to-one","com.idega.block.building.data.Room");
    addAttribute("starting_date","Upphafsdags",true,true,"java.sql.Timestamp");
    addAttribute("closing_date","Lokadags",true,true,"java.sql.Timestamp");
    addAttribute("info","Athugasemd",true,true,"java.lang.String");
    addAttribute("contract_id","",true,true,"java.lang.Integer");
  }
  public String getEntityName() {
    return "tenant";
  }
  public String getName(){
    return getStringColumnValue("name");
  }
  public void setName(String name){
    setColumn("name", name);
  }
  public int getMemberId(){
    return getIntColumnValue("member_id");
  }
  public void setMemberId(int member_id){
    setColumn("member_id",member_id);
  }
  public void setMemberId(Integer member_id){
    setColumn("member_id",member_id);
  }
  public int getRoomId(){
    return getIntColumnValue("room_id");
  }
  public void setRoomId(int room_id){
    setColumn("room_id",room_id);
  }
  public void setRoomId(Integer room_id){
    setColumn("room_id",room_id);
  }
  public String getInfo(){
    return getStringColumnValue("info");
  }
  public void setInfo(String info){
    setColumn("info", info);
  }
  public Timestamp getStartingDate(){
    return (Timestamp) getColumnValue("starting_date");
  }
  public void setStarting(Timestamp starting_date){
    setColumn("starting_date",starting_date);
  }
   public Timestamp getClosingDate(){
    return (Timestamp) getColumnValue("closing_date");
  }
  public void setClosingDate(Timestamp closing_date){
    setColumn("closing_date",closing_date);
  }
  public int getContractId(){
    return getIntColumnValue("contract_id");
  }
  public void setContractId(int contract_id){
    setColumn("contract_id",contract_id);
  }
  public void setContractId(Integer contract_id){
    setColumn("contract_id",contract_id);
  }
}
