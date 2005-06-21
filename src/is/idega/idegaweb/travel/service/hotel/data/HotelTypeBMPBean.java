package is.idega.idegaweb.travel.service.hotel.data;

import java.util.Collection;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.data.GenericEntity;
import com.idega.data.IDOCompositePrimaryKeyException;
import com.idega.data.IDOQuery;
import com.idega.data.IDORelationshipException;
import com.idega.data.query.Column;
import com.idega.data.query.InCriteria;
import com.idega.data.query.JoinCriteria;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;

/**
 * @author gimmi
 */
public class HotelTypeBMPBean extends GenericEntity implements HotelType {

	private static String HOTEL_TYPE_TABLE 			= "TB_ACCOMOTATION_TYPE";
	private static String COLUMN_LOCALIZATION_KEY 	= "LOCALIZATION_KEY";
	private static String COLUMN_USE_RATING = "USE_RATING";
	private static String COLUMN_VALID 	= "IS_VALID";
	
	
	public String getEntityName() {
		return HOTEL_TYPE_TABLE;
	}

	public void initializeAttributes() {
		this.addAttribute(getIDColumnName());
		this.addAttribute(COLUMN_LOCALIZATION_KEY, "localized key", true, true, String.class);
		this.addAttribute(COLUMN_VALID, "isValid", true, true, Boolean.class);
		this.addAttribute(COLUMN_USE_RATING, "useRating", true, true, Boolean.class);
	}
	
	public void setDefaultValues() {
		setColumn(COLUMN_VALID, true);
	}
	
	public String getLocalizationKey() {
		return getStringColumnValue(COLUMN_LOCALIZATION_KEY);
	}
	
	public boolean getUseRating() {
		return getBooleanColumnValue(COLUMN_USE_RATING);
	}
	
	public void setLocalizationKey(String locKey) {
		setColumn(COLUMN_LOCALIZATION_KEY, locKey);
	}
	
	public void setUseRating(boolean useRating) {
		setColumn(COLUMN_USE_RATING, useRating);
	}
	
	public Object ejbFindByLocalizationKey(String locKey) throws FinderException {
		IDOQuery query = this.idoQuery();
		query.appendSelectAllFrom(this).appendWhereEquals(COLUMN_VALID, true)
		.appendAndEqualsQuoted(COLUMN_LOCALIZATION_KEY, locKey);
		return this.idoFindOnePKByQuery(query);
	}
	
	public Collection ejbFindAll() throws FinderException {
		IDOQuery query = this.idoQuery();
		query.appendSelectAllFrom(this).appendWhereEquals(COLUMN_VALID, true);
		return this.idoFindPKsByQuery(query);
	}
	
	public void remove() throws RemoveException {
		setColumn(COLUMN_VALID, false);
		store();
	}

	public Collection ejbFindAllUsedBySuppliers(Collection suppliers) throws IDOCompositePrimaryKeyException, IDORelationshipException, FinderException {
		Table hotelType = new Table(this);
		Table hotel = new Table(Hotel.class);
		Table product = new Table(Product.class);
		Table supplier = new Table(Supplier.class);
	
		Column prodCol = new Column(product, product.getEntityDefinition().getPrimaryKeyDefinition().getField().getSQLFieldName());
		Column hotelCol = new Column(hotel, hotel.getEntityDefinition().getPrimaryKeyDefinition().getField().getSQLFieldName());
		Column suppCol = new Column(supplier, supplier.getEntityDefinition().getPrimaryKeyDefinition().getField().getSQLFieldName());
		Column valid = new Column(hotelType, COLUMN_VALID);
		JoinCriteria jc = new JoinCriteria(prodCol, hotelCol);
		Column pkCol = new Column(hotelType, getIDColumnName());
		pkCol.setAsDistinct();
		
		SelectQuery query = new SelectQuery(hotelType);
		query.addColumn(pkCol);
		
		query.addJoin(hotelType, hotel);
		query.addCriteria(jc);
		query.addJoin(product, supplier);
		query.addCriteria(new MatchCriteria(valid, MatchCriteria.EQUALS, true));
		query.addCriteria(new InCriteria(suppCol, suppliers));
		
		return idoFindPKsByQuery(query);
	}

}
