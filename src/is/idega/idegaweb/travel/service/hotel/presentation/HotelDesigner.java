package is.idega.idegaweb.travel.service.hotel.presentation;

import is.idega.idegaweb.travel.service.hotel.data.HotelHome;
import is.idega.idegaweb.travel.service.hotel.data.Hotel;
import java.rmi.*;

import javax.ejb.*;

import com.idega.block.media.presentation.*;
import com.idega.block.trade.stockroom.business.*;
import com.idega.block.trade.stockroom.data.*;
import com.idega.business.*;
import com.idega.data.*;
import com.idega.idegaweb.*;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;
import com.idega.util.*;
import is.idega.idegaweb.travel.data.*;
import is.idega.idegaweb.travel.presentation.*;
import is.idega.idegaweb.travel.service.hotel.business.*;
import is.idega.idegaweb.travel.service.presentation.*;

/**
 * <p>Title: idega</p>
 * <p>Description: software</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: idega software</p>
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class HotelDesigner extends TravelManager implements DesignerForm {

  private IWResourceBundle _iwrb;
  private Supplier _supplier;
  private Service _service;
  private Product _product;
  private Timeframe _timeframe;

  String NAME_OF_FORM = ServiceDesigner.NAME_OF_FORM;
  String ServiceAction = ServiceDesigner.ServiceAction;

  private String PARAMETER_IS_UPDATE       = "isTourUpdate";
  private String PARAMETER_TIMEFRAME_ID    = "td_timeframeId";
  private String PARAMETER_NAME            = "hd_par_name";
  private String PARAMETER_DESCRIPTION     = "hd_par_desc";
  private String PARAMETER_NUMBER          = "hd_par_num";
  private String PARAMETER_NUMBER_OF_UNITS = "hd_par_num_un";
  private String PARAMETER_ACTIVE_FROM     = "hd_par_act_fr";
  private String PARAMETER_ACTIVE_TO       = "hd_par_act_to";
  private String PARAMETER_ACTIVE_YEARLY   = "hd_par_act_yrl";
  private String PARAMETER_ALL_DAYS        = "hd_par_all_days";
  private String PARAMETER_MONDAYS         = "hd_par_mon";
  private String PARAMETER_TUESDAYS        = "hd_par_tue";
  private String PARAMETER_WEDNESDAYS      = "hd_par_wed";
  private String PARAMETER_THURSDAYS       = "hd_par_thu";
  private String PARAMETER_FRIDAYS         = "hd_par_fri";
  private String PARAMETER_SATURDAYS       = "hd_par_sat";
  private String PARAMETER_SUNDAYS         = "hd_par_sun";
  private String PARAMETER_DEPARTURE_FROM  = "hd_par_dep_from";
  private String PARAMETER_DEPARTURE_TIME  = "hd_par_dep_time";
  private String PARAMETER_ARRIVAL_AT      = "hd_par_arr_at";
  private String PARAMETER_ARRIVAL_TIME    = "hd_par_arr_time";
  private String PARAMETER_DISCOUNT_TYPE   = "hd_par_disc";
  private String PARAMETER_DESIGN_IMAGE_ID = "hd_par_des_img_id";
  private String PARAMETER_USE_IMAGE_ID    = "hd_par_use_img_id";


  public HotelDesigner(IWContext iwc) throws Exception {
    init(iwc);
  }

  private void init( IWContext iwc ) throws Exception {
    super.main( iwc );
    _iwrb = super.getResourceBundle();
    _supplier = super.getSupplier();
  }

  private boolean setupData(int serviceId) {
    try {
      ServiceHome sHome = (ServiceHome) IDOLookup.getHome(Service.class);
      ProductHome pHome = (ProductHome) IDOLookup.getHome(Product.class);
      _service = sHome.findByPrimaryKey(new Integer(serviceId));
      _product = pHome.findByPrimaryKey(new Integer(serviceId));
      _timeframe = _product.getTimeframe();
      return true;
    }catch (Exception e) {
      return false;
    }
  }

  public int handleInsert(IWContext iwc) throws RemoteException {
    String sServiceId = iwc.getParameter( PARAMETER_IS_UPDATE );
    int serviceId = -1;
    if ( sServiceId != null ) {
      serviceId = Integer.parseInt( sServiceId );
    }
    setupData(serviceId);

    String name = iwc.getParameter( PARAMETER_NAME );
    String number = iwc.getParameter( PARAMETER_NUMBER );
    String description = iwc.getParameter( PARAMETER_DESCRIPTION );
    if ( description == null ) {
      description = "";
    }
    String imageId = iwc.getParameter( PARAMETER_DESIGN_IMAGE_ID );
    String numberOfUnits = iwc.getParameter( PARAMETER_NUMBER_OF_UNITS );
    String useImageId = iwc.getParameter( PARAMETER_USE_IMAGE_ID );
    String discountType = iwc.getParameter( PARAMETER_DISCOUNT_TYPE );


    int iDiscountType = com.idega.block.trade.stockroom.data.ProductBMPBean.DISCOUNT_TYPE_ID_PERCENT;
    if ( discountType != null ) {
      iDiscountType = Integer.parseInt( discountType );
    }

    Integer iImageId = null;
    if ( imageId != null ) {
      if ( !imageId.equals( "-1" ) ) {
        iImageId = new Integer( imageId );
      }
    }

    int iNumberOfUnits = 0;
    if (numberOfUnits != null) {
      try {
        iNumberOfUnits = Integer.parseInt(numberOfUnits);
      }catch (NumberFormatException n){
      }
    }


    int returner = -1;

    HotelBusiness hb = (HotelBusiness) IBOLookup.getServiceInstance(iwc, HotelBusiness.class);

    try {
      if ( serviceId == -1 ) {
//        hb.setTimeframe( activeFromStamp, activeToStamp, yearly );
        returner = hb.createHotel(_supplier.getID(), iImageId, name, number, description, iNumberOfUnits,true, iDiscountType);
      } else {
        String timeframeId = iwc.getParameter( PARAMETER_TIMEFRAME_ID );
        if ( timeframeId == null ) {
          timeframeId = "-1";
        }
//        hb.setTimeframe( Integer.parseInt( timeframeId ), activeFromStamp, activeToStamp, yearly );
        returner = hb.updateHotel(serviceId, _supplier.getID(), iImageId, name, number, description, iNumberOfUnits, true, iDiscountType);
        if ( useImageId == null ) {
          ProductEditorBusiness.getInstance().dropImage( _product, true );
        }

      }



    } catch ( Exception e ) {
      e.printStackTrace( System.err );
      //add("TEMP - Service EKKI smíðuð");
    }

    return returner;
  }

  public void finalizeCreation(IWContext iwc, Product product) throws RemoteException, FinderException {
  		getHotelBusiness(iwc).finalizeHotelCreation(product);
  }

  public Form getDesignerForm( IWContext iwc ) throws RemoteException, FinderException {
    return getDesignerForm(iwc, -1);
  }

  public Form getDesignerForm( IWContext iwc, int serviceId ) throws RemoteException, FinderException {
    boolean isDataValid = true;
    boolean isChangingFromOtherType = false;


    if ( serviceId != -1 ) {
      isDataValid = setupData( serviceId );
    }

    Form form = new Form();
    form.setName( NAME_OF_FORM );
    Table table = new Table();
    form.add( table );

    if ( isDataValid ) {

      table.setWidth( "90%" );
      table.setAlignment("center");

      int row = 0;
      IWTimestamp stamp = IWTimestamp.RightNow();

      TextInput name = new TextInput( PARAMETER_NAME );
      name.setSize( 40 );
      TextArea description = new TextArea( PARAMETER_DESCRIPTION );
      description.setWidth( "50" );
      description.setHeight( "12" );
      TextInput number = new TextInput( PARAMETER_NUMBER );
      number.setSize( 20 );
      number.keepStatusOnAction();
      DropdownMenu locales = getProductBusiness(iwc).getLocaleDropDown( iwc );

      int currentYear = IWTimestamp.RightNow().getYear();

      TextInput numberOfUnits = new TextInput( PARAMETER_NUMBER_OF_UNITS );

      DropdownMenu discountType = new DropdownMenu( PARAMETER_DISCOUNT_TYPE );
      discountType.addMenuElement( com.idega.block.trade.stockroom.data.ProductBMPBean.DISCOUNT_TYPE_ID_AMOUNT, _iwrb.getLocalizedString( "travel.amount", "Amount" ) );
      discountType.addMenuElement( com.idega.block.trade.stockroom.data.ProductBMPBean.DISCOUNT_TYPE_ID_PERCENT, _iwrb.getLocalizedString( "travel.percent", "Percent" ) );


      ++row;
      Text nameText = ( Text ) theBoldText.clone();
      nameText.setText( _iwrb.getLocalizedString( "travel.name_of_room", "Name of room" ) );
      table.add( nameText, 1, row );
      table.add( name, 2, row );

      ++row;
      Text numberText = ( Text ) theBoldText.clone();
      numberText.setText( _iwrb.getLocalizedString( "travel.number", "Number" ) );
      table.add( numberText, 1, row );
      table.add( number, 2, row );

      ++row;

      Text descText = ( Text ) theBoldText.clone();
      descText.setText( _iwrb.getLocalizedString( "travel.description", "Description" ) );
      Text imgText = ( Text ) theBoldText.clone();
      imgText.setText( _iwrb.getLocalizedString( "travel.image", "Image" ) );

      table.setVerticalAlignment( 1, row, "top" );
      table.setVerticalAlignment( 2, row, "top" );

      ++row;

      ImageInserter imageInserter = new ImageInserter( PARAMETER_DESIGN_IMAGE_ID );
      imageInserter.setHasUseBox( true, PARAMETER_USE_IMAGE_ID );
      String imageId = iwc.getParameter( PARAMETER_DESIGN_IMAGE_ID );
      if ( _service != null ) {
        _product = _service.getProduct();
        Hotel hotel = null;
        try {
          hotel = ((HotelHome) IDOLookup.getHome(Hotel.class)).findByPrimaryKey(_product.getPrimaryKey());
          numberOfUnits.setContent(Integer.toString(hotel.getNumberOfUnits()));
        }catch (FinderException fe) {
          System.out.println("[HotelDesigner] HotelBean not available");
        }
        if ( imageId != null ) {
          imageInserter.setImageId( Integer.parseInt( imageId ) );
          imageInserter.setSelected( true );
        } else if ( _product.getFileId() != -1 ) {
          imageInserter.setImageId( _product.getFileId() );
          imageInserter.setSelected( true );
        }
      }


      table.setVerticalAlignment( 1, row, "top" );
      table.setVerticalAlignment( 2, row, "top" );
      table.add( imgText, 1, row );
      table.add( imageInserter, 2, row );

      ++row;
      Text timeframeText = ( Text ) theBoldText.clone();
      timeframeText.setText( _iwrb.getLocalizedString( "travel.timeframe", "Timeframe" ) );
      Text tfFromText = ( Text ) smallText.clone();
      tfFromText.setText( _iwrb.getLocalizedString( "travel.from", "from" ) );
      Text tfToText = ( Text ) smallText.clone();
      tfToText.setText( _iwrb.getLocalizedString( "travel.to", "to" ) );
      Text tfYearlyText = ( Text ) smallText.clone();
      tfYearlyText.setText( _iwrb.getLocalizedString( "travel.yearly", "yearly" ) );

      ++row;
      Text noUnitsText = ( Text ) theBoldText.clone();
      noUnitsText.setText( _iwrb.getLocalizedString( "travel.number_of_units", "Number of units" ) );
      table.add( noUnitsText, 1, row );
      table.add( numberOfUnits, 2, row );

      ++row;
      Text discountTypeText = ( Text ) theBoldText.clone();
      discountTypeText.setText( _iwrb.getLocalizedString( "travel.discount_type", "Discount type" ) );
      table.add( discountTypeText, 1, row );
      table.add( discountType, 2, row );

      ++row;
      table.mergeCells( 1, row, 2, row );
      table.setAlignment( 1, row, "right" );
      SubmitButton submit = new SubmitButton( _iwrb.getImage( "buttons/save.gif" ), ServiceDesigner.ServiceAction, ServiceDesigner.parameterCreate );
      table.add( submit, 1, row );

      table.setColumnAlignment( 1, "right" );
      table.setColumnAlignment( 2, "left" );

      if ( _service != null ) {
        Parameter par1 = new Parameter( PARAMETER_IS_UPDATE, Integer.toString( serviceId ) );
        par1.keepStatusOnAction();
        table.add( par1 );

        name.setContent( _product.getProductName( super.getLocaleId() ) );
        number.setContent( _product.getNumber() );
        description.setContent( _product.getProductDescription( super.getLocaleId() ) );


        discountType.setSelectedElement( Integer.toString( _product.getDiscountTypeId() ) );
      } else {
        discountType.setSelectedElement( Integer.toString( com.idega.block.trade.stockroom.data.ProductBMPBean.DISCOUNT_TYPE_ID_PERCENT ) );
      }
    } else {
      table.add( _iwrb.getLocalizedString( "travel.data_is_invalid", "Data is invalid" ) );
    }
    return form;
  }

	private HotelBusiness getHotelBusiness(IWContext iwc) throws RemoteException {
		return (HotelBusiness) IBOLookup.getServiceInstance(iwc, HotelBusiness.class);	
	}

}
