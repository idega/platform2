package is.idega.idegaweb.travel.data;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductHome;
import com.idega.core.location.data.Address;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;


/**
 * Title:        IW Travel
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <br><a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a><br><a href="mailto:gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */

public class ServiceBMPBean extends com.idega.data.GenericEntity implements is.idega.idegaweb.travel.data.Service {
  private Product product;

  public ServiceBMPBean(){
          super();
  }
  public ServiceBMPBean(int id)throws SQLException{
          super(id);
  }
  public void initializeAttributes(){
    addAttribute(getIDColumnName());
//    addAttribute(getIDColumnName(),"Service_id",true,true,Integer.class,"one-to-one",Product.class);
    addAttribute(getArrivalTimeColumnName(), "Arrival time", true, true, Timestamp.class);
    addAttribute(getDepartureTimeColumnName(), "Departure time", true, true, Timestamp.class);
    addAttribute(getIsValidColumnName(), "is valid", true, true, Boolean.class);

//    this.setAsPrimaryKey(getIDColumnName(), true);
    this.addManyToManyRelationShip(PickupPlace.class, "TB_SERVICE_HOTEL_PICKUP_PLACE");
   this.addManyToManyRelationShip(Address.class, "TB_SERVICE_IC_ADDRESS");

   addIndex("IDX_SERV_VALID", new String[] {getIDColumnName(), getIsValidColumnName()});
 }

  public void setDefaultValues() {
    setColumn(getIsValidColumnName(),true);
  }

  public void delete() throws SQLException {
      setColumn(getIsValidColumnName(),false);
      this.update();
      try {
        Product product = this.getProduct();
        product.invalidate();
      }catch (IDOException ido) {
        throw new SQLException(ido.getMessage());
      }catch (RemoteException re) {
        throw new RuntimeException(re.getMessage());
      }
  }

  public Product getProduct() throws RemoteException{
    if (this.product == null) {
      try {
        ProductHome pHome = (ProductHome) IDOLookup.getHome(Product.class);
        product = pHome.findByPrimaryKey(this.getPrimaryKey());
//        product = ProductBusiness.getProduct(this.getID());
      }catch (FinderException sql) {
        sql.printStackTrace(System.err);
      }
    }
    return product;
  }

  public String getEntityName(){
    return getServiceTableName();
  }

  public String getName(int localeId) throws RemoteException{
    return getProduct().getProductName(localeId);
  }

  public String getDescription(int localeId) throws RemoteException{
    return getProduct().getProductDescription(localeId);
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

  public Collection getAddressesColl() throws IDORelationshipException{
    return this.idoGetRelatedEntities(Address.class);
  }

  /**
   * @deprecated
   */
  public Address[] getAddresses() throws SQLException  {
    Address[] addresses = new Address[]{};
    try {
      Collection coll =  getAddressesColl();
      if (coll != null) {
        addresses = new Address[coll.size()];
        Iterator iter = coll.iterator();
        int counter = 0;
        while (iter.hasNext()) {
          addresses[counter] = (Address) iter.next();
          ++counter;
        }
      }
    }catch (IDORelationshipException re){
      throw new SQLException(re.getMessage());
    }
    //return (Address[]) this.findRelated(com.idega.core.data.AddressBMPBean.getStaticInstance(Address.class));
    return addresses;
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
  public static String getServiceIDColumnName() {return getServiceTableName()+"_ID";}


  public Collection getHotelPickupPlaces() throws IDORelationshipException{
    return this.idoGetRelatedEntities(PickupPlace.class);
  }


  public void setPrimaryKey(Integer id) {
    this.setID(id);
  }

  public int getID() {
    return new Integer(getPrimaryKey().toString()).intValue();
  }

  public void removeAllHotelPickupPlaces() throws IDORemoveRelationshipException{
    this.idoRemoveFrom(PickupPlace.class);
  }

}

