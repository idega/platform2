package is.idega.idegaweb.travel.data;



import java.sql.*;

import com.idega.data.*;

import com.idega.core.data.*;

import com.idega.block.trade.stockroom.data.Supplier;



/**

 * Title:        IW Travel

 * Description:

 * Copyright:    Copyright (c) 2001

 * Company:      idega.is

 * @author 2000 - idega team - <br><a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a><br><a href="mailto:gimmi@idega.is">Grímur Jónsson</a>

 * @version 1.0

 */



public class HotelPickupPlaceBMPBean extends com.idega.data.GenericEntity implements is.idega.idegaweb.travel.data.HotelPickupPlace {



  public HotelPickupPlaceBMPBean(){

          super();

  }

  public HotelPickupPlaceBMPBean(int id)throws SQLException{

          super(id);

  }

  public void initializeAttributes(){

    addAttribute(getIDColumnName());

    addAttribute(getNameColumnName(), "Name", true, true, String.class);

    addAttribute(getAddressIDColumnName(), "Heimilisfang", true, true, Integer.class ,"many_to_one",Address.class);

    addAttribute(getDeletedColumnName(), "Hent", true, true, Boolean.class);



    this.addManyToManyRelationShip(Supplier.class,"TB_HOTEL_PICKUP_PL_SR_SUPPLIER");

  }



  public void insertStartData()throws Exception{

  }



  public static String getHotelPickupPlaceTableName(){return "TB_HOTEL_PICKUP_PLACE";}

  public static String getNameColumnName() {return "NAME";}

  public static String getAddressIDColumnName() {return "IC_ADDRESS_ID";}

  public static String getDeletedColumnName() {return "DELETED";}



  public void setDefaultValues() {

    setColumn(getDeletedColumnName(),false);

  }



  public String getEntityName(){

    return getHotelPickupPlaceTableName();

  }

  public String getName(){

    return getStringColumnValue(getNameColumnName());

  }



  public void delete() {

    try {

      setColumn(getDeletedColumnName(),true);

      this.update();

    }catch (SQLException sql) {

      sql.printStackTrace(System.err);

    }

  }



  public void setName(String name){

    setColumn(getNameColumnName(),name);

  }



  public Address getAddress() {

      return (Address) getColumnValue(getAddressIDColumnName());

  }



  public void setAddress(Address address) {

      setColumn(getAddressIDColumnName(), address.getID());

  }



  public void setAddressId(int addressId) {

      setColumn(getAddressIDColumnName(), addressId);

  }



}

