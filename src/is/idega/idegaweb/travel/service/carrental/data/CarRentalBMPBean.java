package is.idega.idegaweb.travel.service.carrental.data;

import is.idega.idegaweb.travel.data.PickupPlace;
import is.idega.idegaweb.travel.data.PickupPlaceHome;
import is.idega.idegaweb.travel.data.Service;
import is.idega.idegaweb.travel.data.ServiceBMPBean;
import is.idega.idegaweb.travel.service.business.ProductCategoryFactoryBean;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductBMPBean;
import com.idega.block.trade.stockroom.data.ProductCategory;
import com.idega.block.trade.stockroom.data.ProductCategoryBMPBean;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.block.trade.stockroom.data.SupplierBMPBean;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.data.EntityControl;
import com.idega.data.GenericEntity;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.util.IWTimestamp;

/**
 * @author gimmi
 */
public class CarRentalBMPBean extends GenericEntity implements CarRental{

	private Collection allPlaces;
	private Collection dropoffPlaces;
	private Collection pickupPlaces;

	public String getEntityName() {
		return "TB_CAR"; 
	}

	public void initializeAttributes() {
		//addAttribute(getIDColumnName(),"Service_id",true,true,Integer.class,"one-to-one",Service.class);
		this.addManyToOneRelationship(getIDColumnName(), "Service_id", Service.class);
		this.getAttribute(getIDColumnName()).setAsPrimaryKey(true);
		addManyToManyRelationShip(PickupPlace.class);
	}

	private void resetPlaces() {
		allPlaces = new Vector();
		pickupPlaces = new Vector();
		dropoffPlaces = new Vector();
	}
		
	private void setAllPlaces() throws IDORelationshipException, IDOLookupException, FinderException {
		System.out.println("[CarRentalBMPBean]setAllPlaces()");
		resetPlaces();
		allPlaces = this.idoGetRelatedEntities(PickupPlace.class);
		
		if (allPlaces != null && !allPlaces.isEmpty()) {
			PickupPlaceHome pHome = (PickupPlaceHome) IDOLookup.getHome(PickupPlace.class);
			PickupPlace pPlace;
			Object obj;
			Iterator iter = allPlaces.iterator();
			while (iter.hasNext()) {
				obj = iter.next();
				pPlace = (PickupPlace) obj;
				if (pPlace.getIsPickup()) {
					pickupPlaces.add(obj);
				}else {
					dropoffPlaces.add(obj);
				}
			}	
		}
	}
	
	public Collection getPickupPlaces() throws IDOLookupException, IDORelationshipException, FinderException {
		if (pickupPlaces == null) {	
			setAllPlaces();
		}		
		return pickupPlaces;
	}	
	
	public Collection getDropoffPlaces() throws IDOLookupException, IDORelationshipException, FinderException {
		if (dropoffPlaces == null) {
			setAllPlaces();	
		}	
		return dropoffPlaces;
	}
	
	public void addPickupPlace(PickupPlace pPlace) throws IDOAddRelationshipException {
		this.idoAddTo(pPlace);
		resetPlaces();
	}
	
	public void addDropoffPlace(PickupPlace pPlace) throws IDOAddRelationshipException {
		this.idoAddTo(pPlace);
		resetPlaces();	
	}
	
	public void removeAllPickupPlaces() throws IDORemoveRelationshipException {
		this.idoRemoveFrom(PickupPlace.class);	
	}
	
	public void removeAllDropoffPlaces() throws IDORemoveRelationshipException  {
		this.idoRemoveFrom(PickupPlace.class);	
	}
	
	public void setPrimaryKey(Object obj) {
		super.setPrimaryKey(obj);	
	}

	public Collection ejbFind(IWTimestamp fromStamp, IWTimestamp toStamp, Object[] postalCodeId, Object[] supplierId, String supplierName) throws FinderException {
		
		boolean postalCode = (postalCodeId != null && postalCodeId.length > 0); 
		boolean timeframe = (fromStamp != null && toStamp != null);
		//boolean roomType = (roomTypeId != null && roomTypeId.length > 0);
		//boolean hotelType = ( hotelTypeId != null && hotelTypeId.length > 0);
		boolean supplier = (supplierId != null && supplierId.length > 0);
		boolean name = (supplierName != null && !supplierName.equals(""));

		try {		
			String addressSupplierMiddleTableName = EntityControl.getManyToManyRelationShipTableName(Address.class, Supplier.class);
			String productCategoryMiddleTableName = EntityControl.getManyToManyRelationShipTableName(Product.class, ProductCategory.class);
			//String roomTypeHotelMiddleTableName = EntityControl.getManyToManyRelationShipTableName(RoomType.class, Hotel.class);
			//String hotelTypeHotelMiddleTableName = EntityControl.getManyToManyRelationShipTableName(HotelType.class, Hotel.class);
			
			String postalCodeTableName = IDOLookup.getEntityDefinitionForClass(PostalCode.class).getSQLTableName();//  PostalCodeBMPBean.getEntityName();
			String addressTableName = IDOLookup.getEntityDefinitionForClass(Address.class).getSQLTableName();
			String serviceTableName = ServiceBMPBean.getServiceTableName();
			String productTableName = ProductBMPBean.getProductEntityName();
			String supplierTableName = SupplierBMPBean.getSupplierTableName();
			String productCategoryTableName = IDOLookup.getEntityDefinitionForClass(ProductCategory.class).getSQLTableName();
//			String roomTypeTableName = IDOLookup.getEntityDefinitionForClass(RoomType.class).getSQLTableName();
//			String hotelTypeTableName = IDOLookup.getEntityDefinitionForClass(HotelType.class).getSQLTableName();
	
			String postalCodeTableIDColumnName = postalCodeTableName+"_id";
			String addressTableIDColumnName = addressTableName+"_id";
			String serviceTableIDColumnName = serviceTableName+"_id";
			String productTableIDColumnName = productTableName+"_id";
			String supplierTableIDColumnName = supplierTableName+"_id";
			String productCategoryTableIDColumnName = productCategoryTableName+"_id";
			//String roomTypeTableIDColumnName = null;
			//String hotelTypeTableIDColumnName = null;
			/*
			try {
				roomTypeTableIDColumnName = IDOLookup.getEntityDefinitionForClass(RoomType.class).getPrimaryKeyDefinition().getField().getSQLFieldName();
				hotelTypeTableIDColumnName = IDOLookup.getEntityDefinitionForClass(HotelType.class).getPrimaryKeyDefinition().getField().getSQLFieldName();
			} catch (IDOCompositePrimaryKeyException e1) {
				roomTypeTableIDColumnName = roomTypeTableName+"_id";
				hotelTypeTableIDColumnName = hotelTypeTableName+"_id";
			}*/
			
			StringBuffer sql = new StringBuffer();
			sql.append("select distinct h.* from ").append(getEntityName()).append(" h, ")
			.append(serviceTableName).append(" s, ")
			.append(productTableName).append(" p, ")
			.append(productCategoryMiddleTableName).append(" pcm, ")
			.append(productCategoryTableName).append(" pcat");
			
			if (postalCode || supplier) {
				sql.append(", ").append(supplierTableName).append(" su");
			}	
/*			if (roomType) {
				sql.append(", ").append(roomTypeHotelMiddleTableName).append(" rth");
			}
			if (hotelType) {
				sql.append(", ").append(hotelTypeHotelMiddleTableName).append(" hth");
			}
*/
			if (postalCode) {
				sql.append(", ").append(addressSupplierMiddleTableName).append(" asm, ")
				.append(addressTableName).append(" a, ")
				.append(postalCodeTableName).append(" pc ");
			}
			
			sql.append(" where ")
			.append(" h.").append(getIDColumnName()).append(" = s.").append(serviceTableIDColumnName)
			.append(" AND s.").append(serviceTableIDColumnName).append(" = p.").append(productTableIDColumnName)
			.append(" AND p.").append(ProductBMPBean.getColumnNameIsValid()).append(" = 'Y'")
			.append(" AND p.").append(productTableIDColumnName).append(" = pcm.").append(productTableIDColumnName)
			.append(" AND pcm.").append(productCategoryTableIDColumnName).append(" = pcat.").append(productCategoryTableIDColumnName)
			.append(" AND pcat.").append(ProductCategoryBMPBean.getColumnType()).append(" = '").append(ProductCategoryFactoryBean.CATEGORY_TYPE_CAR_RENTAL).append("'");

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
				.append(" AND pc.").append(postalCodeTableIDColumnName).append(" in (");
				for (int i = 0; i < postalCodeId.length; i++) {
					if (i != 0) {
						sql.append(", ");
					}
					sql.append(postalCodeId[i]);
				}
				sql.append(")");
			}
			if(name) {
				sql.append(" AND su.").append(SupplierBMPBean.getColumnNameName()).append(" like ").append("'%" + supplierName + "%'");
			}

			/*
			if (roomType) {
				sql.append(" AND h.").append(getIDColumnName()).append("= rth.").append(getIDColumnName());
				sql.append(" AND  rth.").append(roomTypeTableIDColumnName).append(" in (");
				for (int i = 0; i < roomTypeId.length; i++) {
					if (i != 0) {
						sql.append(", ");
					}
					sql.append(roomTypeId[i]);
				}			
				sql.append(") ");
			}
*/
/*			
			if (hotelType) {
				sql.append(" AND h.").append(getIDColumnName()).append("= hth.").append(getIDColumnName());
				sql.append(" AND  hth.").append(hotelTypeTableIDColumnName).append(" in (");
				for (int i = 0; i < hotelTypeId.length; i++) {
					if (i != 0) {
						sql.append(", ");
					}
					sql.append(hotelTypeId[i]);
				}			
				sql.append(") ");
				
			}
*/	
/*
			if (minRating > -1) {
				sql.append(" AND h.").append(getColumnNameRating()).append(" >= ").append(minRating);
			}
*/
/*
			if (maxRating > -1) {
				sql.append(" AND h.").append(getColumnNameRating()).append(" <= ").append(maxRating);
			}
*/
			//sql.append(" order by ").append();
			
			//System.out.println(sql.toString());
			return this.idoFindPKsBySQL(sql.toString());
		}catch (IDOLookupException e) {
			return null;
		}
	}

}
