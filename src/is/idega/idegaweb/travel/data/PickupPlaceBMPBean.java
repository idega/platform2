package is.idega.idegaweb.travel.data;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.core.location.data.Address;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDORemoveRelationshipException;

/**
 * Title:        IW Travel
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <br><a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a><br><a href="mailto:gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */

public class PickupPlaceBMPBean extends com.idega.data.GenericEntity implements is.idega.idegaweb.travel.data.PickupPlace {

	public static final int TYPE_PICKUP = 1;
	public static final int TYPE_DROPOFF = 2;

  public PickupPlaceBMPBean(){
          super();
  }
  public PickupPlaceBMPBean(int id)throws SQLException{
          super(id);
  }
  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getNameColumnName(), "Name", true, true, String.class);
    addAttribute(getAddressIDColumnName(), "Heimilisfang", true, true, Integer.class ,"many_to_one",Address.class);
    addAttribute(getDeletedColumnName(), "Hent", true, true, Boolean.class);
    addAttribute(getTypeColumnName(), "Type", true, true, Integer.class);

    this.addManyToManyRelationShip(Supplier.class,"TB_HOTEL_PICKUP_PL_SR_SUPPLIER");
    addIndex("IDX_PLACE_TYPE", new String[]{getIDColumnName(), getTypeColumnName()});
  }

  public void insertStartData()throws Exception{
  }

  public static String getHotelPickupPlaceTableName(){return "TB_HOTEL_PICKUP_PLACE";}
  public static String getNameColumnName() {return "NAME";}
  public static String getAddressIDColumnName() {return "IC_ADDRESS_ID";}
  public static String getDeletedColumnName() {return "DELETED";}
	public static String getTypeColumnName() { return "PLACE_TYPE";}
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

  public Collection ejbFindDropoffPlaces(Service service)throws RemoteException, FinderException {
		return ejbFindHotelPickupPlaces(service, TYPE_DROPOFF);
  }

  public Collection ejbFindHotelPickupPlaces(Service service)throws RemoteException, FinderException {
		return ejbFindHotelPickupPlaces(service, TYPE_PICKUP);
  }

  public Collection ejbFindHotelPickupPlaces(Service service, int PLACE_TYPE)throws RemoteException, FinderException {
    Collection returner = null;
//        HotelPickupPlace hp = (HotelPickupPlace) is.idega.idegaweb.travel.data.HotelPickupPlaceBMPBean.getStaticInstance(HotelPickupPlace.class);

    StringBuffer buffer = new StringBuffer();
      buffer.append("select h.* from ");
      buffer.append(is.idega.idegaweb.travel.data.ServiceBMPBean.getServiceTableName()+" s,");
      buffer.append(com.idega.data.EntityControl.getManyToManyRelationShipTableName(Service.class,PickupPlace.class)+" smh, ");
      buffer.append(getEntityName() +" h ");
      buffer.append(" WHERE ");
      buffer.append("s."+is.idega.idegaweb.travel.data.ServiceBMPBean.getServiceIDColumnName()+" = "+((Integer) service.getPrimaryKey()).intValue());
      buffer.append(" AND ");
      buffer.append("s."+is.idega.idegaweb.travel.data.ServiceBMPBean.getServiceIDColumnName()+" = smh."+is.idega.idegaweb.travel.data.ServiceBMPBean.getServiceIDColumnName());
      buffer.append(" AND ");
      buffer.append(" smh."+getIDColumnName()+" = h."+getIDColumnName());
			if (PLACE_TYPE == TYPE_DROPOFF) {
					  buffer.append(" AND ");
					  buffer.append(" h."+getTypeColumnName()+" = "+PLACE_TYPE);
			}else if  (PLACE_TYPE == TYPE_PICKUP) {
					  buffer.append(" AND ");
						buffer.append(" h."+getTypeColumnName()+" not in ("+TYPE_DROPOFF+")");
			}
      buffer.append(" AND ");
      buffer.append(getDeletedColumnName() +" = 'N'");
      buffer.append(" ORDER BY "+getNameColumnName());

		//System.out.println("[PickupPlaceBMPBean] sql = "+buffer.toString());
    returner = this.idoFindPKsBySQL(buffer.toString());
//        returner = (HotelPickupPlace[]) hp.findAll(buffer.toString());
    return returner;
  }

  public Collection ejbFindDropoffPlaces(Supplier supplier) throws FinderException{
		return ejbFindHotelPickupPlaces(supplier, TYPE_DROPOFF);
  }

  public Collection ejbFindHotelPickupPlaces(Supplier supplier) throws FinderException{
		return ejbFindHotelPickupPlaces(supplier, TYPE_PICKUP);
  }
  
  public Collection ejbFindAllPlaces(int PLACE_TYPE) throws FinderException {
  		return ejbFindHotelPickupPlacesBySupplier(null, PLACE_TYPE);
  }
  
  public Collection ejbFindHotelPickupPlaces(Supplier supplier, int PLACE_TYPE) throws FinderException{
  		return ejbFindHotelPickupPlacesBySupplier(supplier, PLACE_TYPE);
  }
  
  private Collection ejbFindHotelPickupPlacesBySupplier(Supplier supplier, int PLACE_TYPE) throws FinderException{
    Collection returner = null;
    boolean useSupplier = (supplier != null);

    StringBuffer buffer = new StringBuffer();
	  buffer.append("select h.* from ");
	  if (useSupplier) {
	  		buffer.append(com.idega.block.trade.stockroom.data.SupplierBMPBean.getSupplierTableName()+" s,");
	  		buffer.append(com.idega.data.EntityControl.getManyToManyRelationShipTableName(Supplier.class,PickupPlace.class)+" smh, ");
	  }
	  buffer.append(getEntityName() +" h ");
	  buffer.append(" WHERE ");
	  if (useSupplier) {
		  buffer.append("s."+supplier.getIDColumnName()+" = "+supplier.getID());
		  buffer.append(" AND ");
		  buffer.append("s."+supplier.getIDColumnName()+" = smh."+supplier.getIDColumnName());
		  buffer.append(" AND ");
		  buffer.append(" smh."+getIDColumnName()+" = h."+getIDColumnName());
		  buffer.append(" AND ");
	  }
		if (PLACE_TYPE == TYPE_DROPOFF) {
		  buffer.append(" h."+getTypeColumnName()+" = "+PLACE_TYPE);
		}else if  (PLACE_TYPE == TYPE_PICKUP) {
		  buffer.append(" h."+getTypeColumnName()+" not in ("+TYPE_DROPOFF+")");
		}
	  buffer.append(" AND ");
	  buffer.append("h."+is.idega.idegaweb.travel.data.PickupPlaceBMPBean.getDeletedColumnName() +" = 'N'");
	  buffer.append(" ORDER BY h."+is.idega.idegaweb.travel.data.PickupPlaceBMPBean.getNameColumnName());
	
		//System.out.println("[PickupPlaceBMPBean] sql = "+buffer.toString());
	
	  returner = this.idoFindPKsBySQL(buffer.toString());
//                  returner = (HotelPickupPlace[]) hp.findAll(buffer.toString());
    return returner;
  }

  public void addToSupplier(Supplier supplier) throws IDOAddRelationshipException {
    this.idoAddTo(supplier);
  }
  public void removeFromSupplier(Supplier supplier) throws IDORemoveRelationshipException{
    //System.err.println("Trying to remove ID "+this.getID()+" from "+supplier.getName());
    //System.err.println("Trying to remove PK "+this.getPrimaryKey()+" from "+supplier.getName());
    super.idoRemoveFrom(supplier);
  }

  public void addToService(Service service) throws IDOAddRelationshipException {
    this.idoAddTo(service);
  }

  public void removeFromService(Service service) throws IDORemoveRelationshipException{
    this.idoRemoveFrom(service);
  }
  
  public void setAsPickup() {
  	setColumn(getTypeColumnName(), TYPE_PICKUP);	
  }
  
  public void setAsDropoff() {
  	setColumn(getTypeColumnName(), TYPE_DROPOFF);	
  }

	public boolean getIsPickup() {
		return TYPE_PICKUP == getType();	
	}
	
	public boolean getIsDropoff() {
		return TYPE_DROPOFF == getType();	
	}

	public int getType() {
		return getIntColumnValue(getTypeColumnName());	
	}
	
	public void setType(int type) {
		setColumn(getTypeColumnName(), type);	
	}

}

