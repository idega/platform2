package is.idega.idegaweb.travel.data;



import java.sql.*;

import com.idega.data.*;

import com.idega.core.data.*;

import com.idega.block.trade.stockroom.data.Product;



/**

 * Title:        IW Travel

 * Description:

 * Copyright:    Copyright (c) 2001

 * Company:      idega.is

 * @author 2000 - idega team - <br><a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a><br><a href="mailto:gimmi@idega.is">Grímur Jónsson</a>

 * @version 1.0

 */



public class Service extends GenericEntity{

  private Product product;



  public Service(){

          super();

  }

  public Service(int id)throws SQLException{

          super(id);

  }

  public void initializeAttributes(){

    addAttribute(getIDColumnName());

//    addAttribute(getIDColumnName(),"Service_id",true,true,Integer.class,"one-to-one",Product.class);

    addAttribute(getArrivalTimeColumnName(), "Arrival time", true, true, Timestamp.class);

    addAttribute(getDepartureTimeColumnName(), "Departure time", true, true, Timestamp.class);

    addAttribute(getIsValidColumnName(), "is valid", true, true, Boolean.class);



//    this.setAsPrimaryKey(getIDColumnName(), true);

    this.addManyToManyRelationShip(HotelPickupPlace.class, "TB_SERVICE_HOTEL_PICKUP_PLACE");

    this.addManyToManyRelationShip(Address.class, "TB_SERVICE_IC_ADDRESS");

  }



  public void setDefaultValues() {

    setColumn(getIsValidColumnName(),true);

  }



  public void delete() throws SQLException {

      setColumn(getIsValidColumnName(),false);

      this.update();

      Product product = this.getProduct();

      product.delete();

  }



  public Product getProduct()  {

    if (this.product == null) {

      try {

      product = new Product(this.getID());

      }catch (SQLException sql) {

        sql.printStackTrace(System.err);

      }

    }

    return product;

  }



  public String getEntityName(){

    return getServiceTableName();

  }



  public String getName(){

    return getProduct().getName();

  }



  public String getDescription() {

    return getProduct().getProdcutDescription();

  }



  public Timestamp getArrivalTime() {

    return (Timestamp) getColumnValue(getArrivalTimeColumnName());

  }



  public void setAttivalTime(Timestamp timestamp) {

    setColumn(getArrivalTimeColumnName(),timestamp);

  }



  public Timestamp getDepartureTime() {

    return (Timestamp) getColumnValue(getDepartureTimeColumnName());

  }



  public void setDepartureTime(Timestamp timestamp) {

    setColumn(getDepartureTimeColumnName(),timestamp);

  }





  public Address[] getAddresses() throws SQLException  {

    return (Address[]) this.findRelated(Address.getStaticInstance(Address.class));

  }



  public Address getAddress() throws SQLException{

    Address[] temp = getAddresses();

    if (temp.length > 0) {

      return temp[temp.length -1];

    }

    else {

      return null;

    }

  }



  public static String getServiceTableName(){return "TB_SERVICE";}

  public static String getArrivalTimeColumnName() {return "ARRIVAL_TIME";}

  public static String getDepartureTimeColumnName() {return "DEPARTURE_TIME";}

  public static String getIsValidColumnName() {return "IS_VALID";}









}

