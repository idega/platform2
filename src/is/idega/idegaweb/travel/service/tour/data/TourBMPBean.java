package is.idega.idegaweb.travel.service.tour.data;

import is.idega.idegaweb.travel.data.Service;
import is.idega.idegaweb.travel.data.ServiceBMPBean;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductBMPBean;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.block.trade.stockroom.data.SupplierBMPBean;
import com.idega.block.trade.stockroom.data.TravelAddress;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.data.EntityControl;
import com.idega.data.GenericEntity;
import com.idega.data.IDOCompositePrimaryKeyException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.util.IWTimestamp;


/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class TourBMPBean extends GenericEntity implements Tour {

  public TourBMPBean() {
    super();
  }

  public TourBMPBean(int id) throws SQLException{
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName(),"Service_id",true,true,Integer.class,"one-to-one",Service.class);
    addAttribute(getHotelPickupColumnName(), "Hotel pick-up", true, true, Boolean.class);
    addAttribute(getHotelPickupTimeColumnName(), "Hotel pick-up time", true, true, Timestamp.class);
    addAttribute(getTotalSeatsColumnName(), "Total seats", true, true, Integer.class);
    addAttribute(getMinimumSeatsColumnName(), "L�gmark s�ta", true, true, Integer.class);
    addAttribute(getNumberOfDaysColumnName(), "Fj�ldi daga", true, true, Integer.class);
    addAttribute(getLengthColumnName(), "Lengd", true, true, Float.class);
    addAttribute(getEstimatedSeatsUsedColumnName(), "estimated seats used", true, true, Integer.class);
    
    addManyToManyRelationShip(TourTypeBMPBean.class, "TB_TOUR_TOUR_TYPE");
  }
  public String getEntityName() {
    return getTripTableName();
  }


  public void setDefaultValues() {
      this.setLength(0);
      this.setTotalSeats(0);
      this.setMinumumSeats(0);
      this.setNumberOfDays(1);
  }

  public boolean getIsHotelPickup() throws RemoteException{
    return getHotelPickup();
  }

  public boolean getHotelPickup() throws RemoteException{
    try {
      Service service = ((is.idega.idegaweb.travel.data.ServiceHome) com.idega.data.IDOLookup.getHome(Service.class)).findByPrimaryKey(this.getPrimaryKey());
      Collection coll = service.getHotelPickupPlaces();
//      HotelPickupPlace[] rugl = (HotelPickupPlace[]) (()).findRelated((HotelPickupPlace) (is.idega.idegaweb.travel.data.HotelPickupPlaceBMPBean.getStaticInstance(HotelPickupPlace.class)));
      if (coll.size() == 0) {
        return false;
      }else {
        return true;
      }
    }catch (FinderException sql) {
      sql.printStackTrace(System.err);
    }catch (IDORelationshipException re) {
      re.printStackTrace(System.err);
    }
    return false;
    //return getBooleanColumnValue(getHotelPickupColumnName());
  }

  public void setIsHotelPickup(boolean pickup) {
    setHotelPickup(pickup);
  }

  public void setHotelPickup(boolean pickup) {
    setColumn(getHotelPickupColumnName(),pickup);
  }

  public void setHotelPickupTime(Timestamp pickupTime) {
    setColumn(getHotelPickupTimeColumnName(), pickupTime);
  }

  public Timestamp getHotelPickupTime() {
    return (Timestamp) getColumnValue(getHotelPickupTimeColumnName());
  }

  public int getTotalSeats() {
    return getIntColumnValue(getTotalSeatsColumnName());
  }

  public void setTotalSeats(int totalSeats) {
    setColumn(getTotalSeatsColumnName(), totalSeats);
  }

  public int getMinimumSeats() {
    return getIntColumnValue(getMinimumSeatsColumnName());
  }

  public void setMinumumSeats(int seats) {
    setColumn(getMinimumSeatsColumnName(), seats);
  }

  public void setNumberOfDays(int numberOfSeats) {
    setColumn(getNumberOfDaysColumnName(), numberOfSeats);
  }

  public int getNumberOfDays() {
    return getIntColumnValue(getNumberOfDaysColumnName());
  }

  public void setLength(float length) {
    setColumn(getLengthColumnName(), length);
  }

  public float getLength() {
    return getFloatColumnValue(getLengthColumnName());
  }

  public void setEstimatedSeatsUsed(int seats) {
    setColumn(getEstimatedSeatsUsedColumnName(), seats);
  }

  public int getEstimatedSeatsUsed() {
    return getIntColumnValue(getEstimatedSeatsUsedColumnName());
  }

  public Collection getTourTypes() throws IDORelationshipException {
  	return this.idoGetRelatedEntities(TourType.class);
  }
  
  public void setTourTypes(Object[] tourTypePKs) throws IDORelationshipException {
  	this.idoRemoveFrom(TourType.class);
  	if (tourTypePKs != null) {
  		for (int i = 0; i < tourTypePKs.length; i++) {
  			this.idoAddTo(TourType.class, tourTypePKs[i]);
  		}
  	}
  }

	public Collection ejbFind(IWTimestamp fromStamp, IWTimestamp toStamp, Object[] tourTypeId, Object[] postalCodeId, Object[] supplierId, String supplierName) throws FinderException {
		
		boolean postalCode = (postalCodeId != null && postalCodeId.length > 0); 
		boolean timeframe = (fromStamp != null && toStamp != null);
		boolean tourType = (tourTypeId != null && tourTypeId.length > 0);
		boolean supplier = (supplierId != null && supplierId.length > 0);
		boolean name = (supplierName != null && !supplierName.equals(""));

		try {		
			String addressSupplierMiddleTableName = EntityControl.getManyToManyRelationShipTableName(Address.class, Supplier.class);
			String addressProductMiddleTableName = EntityControl.getManyToManyRelationShipTableName(TravelAddress.class, Product.class);
			String tourTypeTourMiddleTableName = EntityControl.getManyToManyRelationShipTableName(TourType.class, Tour.class);
			
			String postalCodeTableName = IDOLookup.getEntityDefinitionForClass(PostalCode.class).getSQLTableName();//  PostalCodeBMPBean.getEntityName();
			String addressTableName = IDOLookup.getEntityDefinitionForClass(Address.class).getSQLTableName();
			String travelAddressTableName = IDOLookup.getEntityDefinitionForClass(TravelAddress.class).getSQLTableName();
			String serviceTableName = ServiceBMPBean.getServiceTableName();
			String productTableName = ProductBMPBean.getProductEntityName();
			String supplierTableName = SupplierBMPBean.getSupplierTableName();
			String tourTypeTableName = IDOLookup.getEntityDefinitionForClass(TourType.class).getSQLTableName();
	
			String postalCodeTableIDColumnName = postalCodeTableName+"_id";
			String addressTableIDColumnName = addressTableName+"_id";
			String travelAddressTableIDColumnName = travelAddressTableName+"_id";
			String serviceTableIDColumnName = serviceTableName+"_id";
			String productTableIDColumnName = productTableName+"_id";
			String supplierTableIDColumnName = supplierTableName+"_id";
			String tourTypeTableIDColumnName = null;
			try {
				tourTypeTableIDColumnName = IDOLookup.getEntityDefinitionForClass(TourType.class).getPrimaryKeyDefinition().getField().getSQLFieldName();
			} catch (IDOCompositePrimaryKeyException e1) {
				tourTypeTableIDColumnName = tourTypeTableName+"_id";
			}
			
			StringBuffer sql = new StringBuffer();
			sql.append("select distinct h.* from ").append(getEntityName()).append(" h, ")
			.append(serviceTableName).append(" s, ")
			.append(productTableName).append(" p");
			
			if (postalCode || supplier) {
				sql.append(", ").append(supplierTableName).append(" su");
			}	
			if (tourType) {
				sql.append(", ").append(tourTypeTourMiddleTableName).append(" rth");
				sql.append(", ").append(tourTypeTableName).append(" rt");
			}

			if (postalCode) {
				sql.append(", ").append(addressSupplierMiddleTableName).append(" asm, ")
				.append(addressTableName).append(" a, ")
				.append(postalCodeTableName).append(" pc, ")
				.append(addressProductMiddleTableName).append(" pa, ")
				.append(travelAddressTableName).append(" addr, ")
				.append(addressTableName).append(" ica ");
			}
			
			sql.append(" where ")
			.append("p.").append(productTableIDColumnName).append(" = pa.").append(productTableIDColumnName)
			.append(" AND pa.").append(travelAddressTableIDColumnName).append(" = addr.").append(travelAddressTableIDColumnName)
			.append(" AND addr.").append(addressTableIDColumnName).append(" = ica.").append(addressTableIDColumnName)
			.append(" AND h.").append(getIDColumnName()).append(" = s.").append(serviceTableIDColumnName)
			.append(" AND s.").append(serviceTableIDColumnName).append(" = p.").append(productTableIDColumnName)
			.append(" AND p.").append(ProductBMPBean.getColumnNameIsValid()).append(" = 'Y'");
			
			if (supplier) {
				sql.append(" AND su.").append(supplierTableIDColumnName).append(" in (");
				for (int i = 0; i < supplierId.length; i++) {
					if (i != 0) {
						sql.append(", ");
					}
					sql.append(supplierId[i].toString());
				}
				sql.append(")");
			}

			if (postalCode) {
				sql.append(" AND asm.").append(supplierTableIDColumnName).append(" = su.").append(supplierTableIDColumnName)
				.append(" AND asm.").append(addressTableIDColumnName).append(" = a.").append(addressTableIDColumnName)
				.append(" AND p.").append(ProductBMPBean.getColumnNameSupplierId()).append(" = su.").append(supplierTableIDColumnName)
				// HARDCODE OF DEATH ... courtesy of AddressBMPBean
				.append(" AND a.postal_code_id = pc.").append(postalCodeTableIDColumnName)
				.append(" AND ( pc.").append(postalCodeTableIDColumnName).append(" in (");
				for (int i = 0; i < postalCodeId.length; i++) {
					if (i != 0) {
						sql.append(", ");
					}
					sql.append(postalCodeId[i]);
				}
				sql.append(") OR ica.postal_code_id in (");
				for (int i = 0; i < postalCodeId.length; i++) {
					if (i != 0) {
						sql.append(", ");
					}
					sql.append(postalCodeId[i]);
				}
				sql.append(")");

				sql.append(")");
			}
			
			if (tourType) {
				sql.append(" AND h.").append(getIDColumnName()).append("= rth.").append(getIDColumnName());
				sql.append(" AND rth.").append(tourTypeTableIDColumnName).append("= rt.").append(tourTypeTableIDColumnName);
				sql.append(" AND  rt.").append(tourTypeTableIDColumnName).append(" in (");
				for (int i = 0; i < tourTypeId.length; i++) {
					if (i != 0) {
						sql.append(", ");
					}
					sql.append(tourTypeId[i]);
				}			
				sql.append(") ");
			}
			if(name) {
				sql.append(" AND su.").append(SupplierBMPBean.COLUMN_NAME_NAME_ALL_CAPS ).append(" like ").append("'%" + supplierName.toUpperCase() + "%'");
			}

			//sql.append(" order by ").append();
			
			//System.out.println(sql.toString());
			return this.idoFindPKsBySQL(sql.toString());
		}catch (IDOLookupException e) {
			return null;
		}
	}

  public static String getTripTableName() {return "TB_TOUR";}
  public static String getHotelPickupColumnName() {return "HOTEL_PICKUP";}
  public static String getHotelPickupTimeColumnName() {return "HOTEL_PICKUP_TIME";}
  public static String getTotalSeatsColumnName() {return "TOTAL_SEATS";}
  public static String getMinimumSeatsColumnName() {return "MINIMUM_SEATS";}
  public static String getNumberOfDaysColumnName() {return "NUMBER_OF_DAYS";}
  public static String getLengthColumnName() {return "TOUR_LENGTH";}
  public static String getEstimatedSeatsUsedColumnName() {return "ESTIMATED_SEATS_USED";}

  public void setPrimaryKey(Object object) {
    super.setPrimaryKey(object);
  }

}
