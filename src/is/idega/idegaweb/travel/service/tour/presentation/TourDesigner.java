package is.idega.idegaweb.travel.service.tour.presentation;

import is.idega.idegaweb.travel.data.PickupPlace;
import is.idega.idegaweb.travel.data.PickupPlaceHome;
import is.idega.idegaweb.travel.data.Service;
import is.idega.idegaweb.travel.data.ServiceDay;
import is.idega.idegaweb.travel.data.ServiceDayHome;
import is.idega.idegaweb.travel.presentation.ServiceDesigner;
import is.idega.idegaweb.travel.presentation.TravelManager;
import is.idega.idegaweb.travel.service.presentation.BookingForm;
import is.idega.idegaweb.travel.service.presentation.DesignerForm;
import is.idega.idegaweb.travel.service.tour.business.TourBusiness;
import is.idega.idegaweb.travel.service.tour.data.Tour;
import is.idega.idegaweb.travel.service.tour.data.TourType;
import is.idega.idegaweb.travel.service.tour.data.TourTypeHome;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.block.media.presentation.ImageInserter;
import com.idega.block.trade.stockroom.business.ProductEditorBusiness;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.block.trade.stockroom.data.Timeframe;
import com.idega.block.trade.stockroom.data.TravelAddress;
import com.idega.business.IBOLookup;
import com.idega.core.location.data.Address;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BooleanInput;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SelectPanel;
import com.idega.presentation.ui.SelectionBox;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.TimeInput;
import com.idega.presentation.ui.util.SelectorUtility;
import com.idega.util.IWTimestamp;
import com.idega.util.ListUtil;

/**
 *  Title: idegaWeb TravelBooking Description: Copyright: Copyright (c) 2001
 *  Company: idega
 *
 *@author     <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 *@created    16. apríl 2002
 *@version    1.0
 */

public class TourDesigner extends TravelManager implements DesignerForm{
  IWResourceBundle iwrb;
  IWBundle iwb;
  Supplier supplier;
  TourBusiness tb;

  String NAME_OF_FORM = ServiceDesigner.NAME_OF_FORM;
  String ServiceAction = ServiceDesigner.ServiceAction;

  Product product;
  Service service;
  Tour tour;
  Timeframe timeframe;
  TravelAddress depAddress;
  Address arrAddress;

  private String parameterIsUpdate = "isTourUpdate";
  private String parameterTimeframeId = "td_timeframeId";

  private String PARAMETER_TOUR_TYPE_ID = "pTtId";
  
  /**
   *  Constructor for the TourDesigner object
   *
   *@param  iwc            Description of the Parameter
   *@exception  Exception  Description of the Exception
   */
  public TourDesigner( IWContext iwc ) throws Exception {
    init( iwc );
  }

  /**
   *  Description of the Method
   *
   *@param  iwc            Description of the Parameter
   *@exception  Exception  Description of the Exception
   */
  private void init( IWContext iwc ) throws Exception {
    super.main( iwc );
    iwrb = super.getResourceBundle();
    iwb = super.getBundle();
    supplier = super.getSupplier();
    tb  = getTourBusiness(iwc);
  }

  /**
   *  Description of the Method
   *
   *@param  tourId  Description of the Parameter
   *@return         Description of the Return Value
   */
  private boolean setupData( IWContext iwc, int tourId ) throws RemoteException, FinderException {
    try {
//      ProductHome pHome = (ProductHome) IDOLookup.getHome(Product.class);
//      product = pHome.findByPrimaryKey(new Integer(tourId));
      product = getProductBusiness(iwc).getProduct( tourId );
      service = ( ( is.idega.idegaweb.travel.data.ServiceHome ) com.idega.data.IDOLookup.getHome( Service.class ) ).findByPrimaryKey( product.getPrimaryKey() );
      try {
        tour = ( ( is.idega.idegaweb.travel.service.tour.data.TourHome ) com.idega.data.IDOLookup.getHome( Tour.class ) ).findByPrimaryKey( product.getPrimaryKey() );
      }catch (FinderException fe) {
        //fe.printStackTrace(System.err);
      }
      timeframe = product.getTimeframe();

      arrAddress = getProductBusiness(iwc).getArrivalAddress( product );
      depAddress = getProductBusiness(iwc).getDepartureAddress( product );

      return true;
    } catch ( SQLException sql ) {
      sql.printStackTrace( System.err );
      return false;
    }
  }


  /**
   *  Gets the tourDesignerForm attribute of the TourDesigner object
   *
   *@param  iwc  Description of the Parameter
   *@return      The tourDesignerForm value
   */
  public Form getTourDesignerForm( IWContext iwc ) throws RemoteException, FinderException{
    return getTourDesignerForm( iwc, -1 );
  }
  public Form getDesignerForm( IWContext iwc ) throws RemoteException, FinderException{
    return getTourDesignerForm( iwc );
  }


  /**
   *  Gets the tourDesignerForm attribute of the TourDesigner object
   *
   *@param  iwc     Description of the Parameter
   *@param  tourId  Description of the Parameter
   *@return         The tourDesignerForm value
   */
  public Form getDesignerForm( IWContext iwc, int tourId ) throws RemoteException, FinderException{
    return getTourDesignerForm(iwc, tourId);
  }

  public Form getTourDesignerForm( IWContext iwc, int tourId ) throws RemoteException, FinderException{
    boolean isDataValid = true;

    if ( tourId != -1 ) {
      isDataValid = setupData( iwc, tourId );
    }

    Form form = new Form();
    form.setName( NAME_OF_FORM );
    Table table = new Table();
    form.add( table );

    if ( isDataValid ) {

      table.setWidth( "90%" );

      int row = 0;
      IWTimestamp stamp = IWTimestamp.RightNow();

      TextInput name = new TextInput( "name_of_trip" );
      name.setSize( 40 );
      //name.keepStatusOnAction();
      TextArea description = new TextArea( "description" );
      description.setWidth( "50" );
      description.setHeight( "12" );
      //description.keepStatusOnAction();
      TextInput number = new TextInput( "number" );
      number.setSize( 20 );
      number.keepStatusOnAction();
      DropdownMenu locales = getProductBusiness(iwc).getLocaleDropDown( iwc );

      int currentYear = IWTimestamp.RightNow().getYear();

      DateInput active_from = new DateInput( "active_from" );
      active_from.setDate( stamp.getSQLDate() );
      active_from.setYearRange( 2001, currentYear + 5 );
      active_from.keepStatusOnAction();
      DateInput active_to = new DateInput( "active_to" );
      stamp.addDays( 92 );
      active_to.setDate( stamp.getSQLDate() );
      active_to.setYearRange( 2001, currentYear + 5 );
      active_to.keepStatusOnAction();
      BooleanInput active_yearly = new BooleanInput( "active_yearly" );
      active_yearly.setSelected( false );
      active_yearly.keepStatusOnAction();

      CheckBox allDays = new CheckBox( "all_days" );
      CheckBox mondays = new CheckBox( "mondays" );
      CheckBox tuesdays = new CheckBox( "tuesdays" );
      CheckBox wednesdays = new CheckBox( "wednesdays" );
      CheckBox thursdays = new CheckBox( "thursdays" );
      CheckBox fridays = new CheckBox( "fridays" );
      CheckBox saturdays = new CheckBox( "saturdays" );
      CheckBox sundays = new CheckBox( "sundays" );
      allDays.keepStatusOnAction();
      mondays.keepStatusOnAction();
      tuesdays.keepStatusOnAction();
      wednesdays.keepStatusOnAction();
      thursdays.keepStatusOnAction();
      fridays.keepStatusOnAction();
      saturdays.keepStatusOnAction();
      sundays.keepStatusOnAction();

      TextInput number_of_days = new TextInput( "number_of_days" );
      number_of_days.keepStatusOnAction();
      TextInput departure_from = new TextInput( "departure_from" );
      departure_from.setSize( 40 );
      departure_from.keepStatusOnAction();
      TimeInput departure_time = new TimeInput( "departure_time" );
      departure_time.setHour( 8 );
      departure_time.setMinute( 0 );
      departure_time.keepStatusOnAction();
      TextInput arrival_at = new TextInput( "arrival_at" );
      arrival_at.setSize( 40 );
      arrival_at.keepStatusOnAction();
      TimeInput arrival_time = new TimeInput( "arrival_time" );
      arrival_time.setHour( 8 );
      arrival_time.setMinute( 0 );
      arrival_time.keepStatusOnAction();

      RadioButton hotelPickupYes = new RadioButton( "hotel_pickup", "yes" );
      hotelPickupYes.setSelected();
      hotelPickupYes.keepStatusOnAction();
      RadioButton hotelPickupNo = new RadioButton( "hotel_pickup", "no" );
      hotelPickupYes.keepStatusOnAction();
      TimeInput hotelPickupTime = new TimeInput( "hotel_pickup_time" );
      hotelPickupTime.setHour( 8 );
      hotelPickupTime.setMinute( 0 );
      hotelPickupTime.keepStatusOnAction();
      TextInput hotelPickup = new TextInput( "hotel_pickup_address" );
      hotelPickup.keepStatusOnAction();

      hotelPickup.setSize( 40 );
      hotelPickupYes.setOnClick( "this.form." + hotelPickup.getName() + ".disabled=false" );
      hotelPickupYes.setOnClick( "this.form." + hotelPickupTime.getHourName() + ".disabled=false" );
      hotelPickupYes.setOnClick( "this.form." + hotelPickupTime.getMinuteName() + ".disabled=false" );
      hotelPickupNo.setOnClick( "this.form." + hotelPickup.getName() + ".disabled=true" );
      hotelPickupNo.setOnClick( "this.form." + hotelPickupTime.getHourName() + ".disabled=true" );
      hotelPickupNo.setOnClick( "this.form." + hotelPickupTime.getMinuteName() + ".disabled=true" );

      TextInput numberOfSeats = new TextInput( "number_of_seats" );
      numberOfSeats.keepStatusOnAction();

      TextInput minNumberOfSeats = new TextInput( "min_number_of_seats" );
      minNumberOfSeats.keepStatusOnAction();

      TextInput estSeats = new TextInput( "estimated_seats" );
      estSeats.keepStatusOnAction();

      TextInput kilometers = new TextInput( "kilometers" );
      kilometers.keepStatusOnAction();

      DropdownMenu discountType = new DropdownMenu( "discountType" );
      discountType.addMenuElement( com.idega.block.trade.stockroom.data.ProductBMPBean.DISCOUNT_TYPE_ID_AMOUNT, iwrb.getLocalizedString( "travel.amount", "Amount" ) );
      discountType.addMenuElement( com.idega.block.trade.stockroom.data.ProductBMPBean.DISCOUNT_TYPE_ID_PERCENT, iwrb.getLocalizedString( "travel.percent", "Percent" ) );

			Collection categories = getTourTypeHome().findAll();
			
			SelectPanel tourTypes = new SelectPanel(PARAMETER_TOUR_TYPE_ID );
			SelectorUtility su = new SelectorUtility();
			tourTypes = (SelectPanel) su.getSelectorFromIDOEntities(tourTypes, categories, "getLocalizationKey", iwrb);


      ++row;
      Text nameText = ( Text ) theBoldText.clone();
      nameText.setText( iwrb.getLocalizedString( "travel.name_of_trip", "Name of trip" ) );
      table.add( nameText, 1, row );
      table.add( name, 2, row );

      ++row;
      Text numberText = ( Text ) theBoldText.clone();
      numberText.setText( iwrb.getLocalizedString( "travel.number", "Number" ) );
      table.add( numberText, 1, row );
      table.add( number, 2, row );

      ++row;

      Text descText = ( Text ) theBoldText.clone();
      descText.setText( iwrb.getLocalizedString( "travel.description", "Description" ) );
      Text imgText = ( Text ) theBoldText.clone();
      imgText.setText( iwrb.getLocalizedString( "travel.image", "Image" ) );

//      table.add(descText,1,row);
//      table.add(description, 2,row);
      table.setVerticalAlignment( 1, row, "top" );
      table.setVerticalAlignment( 2, row, "top" );

//      ++row;
//      table.add(locales, 2, row);
      ++row;

      ImageInserter imageInserter = new ImageInserter( "design_image_id" );
      imageInserter.setHasUseBox( true, "use_image_id" );
      String imageId = iwc.getParameter( "design_image_id" );
      if ( service != null ) {
        Product product = service.getProduct();
        if ( imageId != null ) {
          imageInserter.setImageId( Integer.parseInt( imageId ) );
          imageInserter.setSelected( true );
          //imageInserter = new ImageInserter(Integer.parseInt(imageId), "design_image_id");
        } else if ( product.getFileId() != -1 ) {
          imageInserter.setImageId( product.getFileId() );
          imageInserter.setSelected( true );
          //imageInserter = new ImageInserter(product.getFileId(), "design_image_id");
        }
      }
      //imageInserter.setWindowToReload(true);


      table.setVerticalAlignment( 1, row, "top" );
      table.setVerticalAlignment( 2, row, "top" );
      table.add( imgText, 1, row );
      table.add( imageInserter, 2, row );

      ++row;
      Text timeframeText = ( Text ) theBoldText.clone();
      timeframeText.setText( iwrb.getLocalizedString( "travel.timeframe", "Timeframe" ) );
      Text tfFromText = ( Text ) smallText.clone();
      tfFromText.setText( iwrb.getLocalizedString( "travel.from", "from" ) );
      Text tfToText = ( Text ) smallText.clone();
      tfToText.setText( iwrb.getLocalizedString( "travel.to", "to" ) );
      Text tfYearlyText = ( Text ) smallText.clone();
      tfYearlyText.setText( iwrb.getLocalizedString( "travel.yearly", "yearly" ) );

      Table activeTable = new Table( 5, 2 );

      activeTable.add( tfFromText, 1, 1 );
      activeTable.add( active_from, 1, 2 );
      activeTable.add( tfToText, 3, 1 );
      activeTable.add( active_to, 3, 2 );
      activeTable.add( tfYearlyText, 5, 1 );
      activeTable.add( active_yearly, 5, 2 );

      activeTable.setVerticalAlignment( 1, 1, "bottom" );
      activeTable.setVerticalAlignment( 3, 1, "bottom" );
      activeTable.setVerticalAlignment( 5, 1, "bottom" );

      table.add( timeframeText, 1, row );
      table.add( activeTable, 2, row );

      ++row;
      Table weekdayFixTable = new Table( 9, 2 );
      weekdayFixTable.setCellpadding( 0 );
      weekdayFixTable.setCellspacing( 1 );
      weekdayFixTable.setWidth( "350" );
      weekdayFixTable.setColumnAlignment( 1, "center" );
      weekdayFixTable.setColumnAlignment( 2, "center" );
      weekdayFixTable.setColumnAlignment( 3, "center" );
      weekdayFixTable.setColumnAlignment( 4, "center" );
      weekdayFixTable.setColumnAlignment( 5, "center" );
      weekdayFixTable.setColumnAlignment( 6, "center" );
      weekdayFixTable.setColumnAlignment( 7, "center" );
      weekdayFixTable.setColumnAlignment( 8, "center" );
      weekdayFixTable.setColumnAlignment( 9, "center" );

      Text alld = ( Text ) smallText.clone();
      alld.setText( iwrb.getLocalizedString( "travel.all_days", "All" ) );
      Text mond = ( Text ) smallText.clone();
      mond.setText( iwrb.getLocalizedString( "travel.mon", "mon" ) );
      Text tued = ( Text ) smallText.clone();
      tued.setText( iwrb.getLocalizedString( "travel.tue", "tue" ) );
      Text wedd = ( Text ) smallText.clone();
      wedd.setText( iwrb.getLocalizedString( "travel.wed", "wed" ) );
      Text thud = ( Text ) smallText.clone();
      thud.setText( iwrb.getLocalizedString( "travel.thu", "thu" ) );
      Text frid = ( Text ) smallText.clone();
      frid.setText( iwrb.getLocalizedString( "travel.fri", "fri" ) );
      Text satd = ( Text ) smallText.clone();
      satd.setText( iwrb.getLocalizedString( "travel.sat", "sat" ) );
      Text sund = ( Text ) smallText.clone();
      sund.setText( iwrb.getLocalizedString( "travel.sun", "sun" ) );

      weekdayFixTable.add( alld, 1, 1 );
      weekdayFixTable.add( mond, 3, 1 );
      weekdayFixTable.add( tued, 4, 1 );
      weekdayFixTable.add( wedd, 5, 1 );
      weekdayFixTable.add( thud, 6, 1 );
      weekdayFixTable.add( frid, 7, 1 );
      weekdayFixTable.add( satd, 8, 1 );
      weekdayFixTable.add( sund, 9, 1 );

      weekdayFixTable.add( allDays, 1, 2 );
      weekdayFixTable.add( mondays, 3, 2 );
      weekdayFixTable.add( tuesdays, 4, 2 );
      weekdayFixTable.add( wednesdays, 5, 2 );
      weekdayFixTable.add( thursdays, 6, 2 );
      weekdayFixTable.add( fridays, 7, 2 );
      weekdayFixTable.add( saturdays, 8, 2 );
      weekdayFixTable.add( sundays, 9, 2 );

      Text weekdaysText = ( Text ) theBoldText.clone();
      weekdaysText.setText( iwrb.getLocalizedString( "travel.weekdays", "Weekdays" ) );
      table.add( weekdaysText, 1, row );
      table.add( weekdayFixTable, 2, row );

      
      ++row;
      Text textTourType = ( Text ) theBoldText.clone();
      textTourType.setText(iwrb.getLocalizedString("tour.tour_type", "Tour type"));
      table.add( textTourType, 1, row );
      table.setVerticalAlignment(1, row, Table.VERTICAL_ALIGN_TOP);
      table.add( tourTypes, 2, row );
      
      ++row;
      Text numOfDays = ( Text ) theBoldText.clone();
      numOfDays.setText( iwrb.getLocalizedString( "travel.number_of_days", "Number of days" ) );
      table.add( numOfDays, 1, row );
      table.add( number_of_days, 2, row );

      ++row;
      Text departureFromText = ( Text ) theBoldText.clone();
      departureFromText.setText( iwrb.getLocalizedString( "travel.departure_from", "Departure from" ) );
      table.add( departureFromText, 1, row );
      table.add( departure_from, 2, row );

      ++row;
      Text departureTimeText = ( Text ) theBoldText.clone();
      departureTimeText.setText( iwrb.getLocalizedString( "travel.departure_time", "Departure time" ) );
      table.add( departureTimeText, 1, row );
      table.add( departure_time, 2, row );

      ++row;
      Text arrivalAtText = ( Text ) theBoldText.clone();
      arrivalAtText.setText( iwrb.getLocalizedString( "travel.arrival_at", "Arrival at" ) );
      table.add( arrivalAtText, 1, row );
      table.add( arrival_at, 2, row );

      ++row;
      Text arrivalTimeText = ( Text ) theBoldText.clone();
      arrivalTimeText.setText( iwrb.getLocalizedString( "travel.arrival_time", "Arrival time" ) );
      table.add( arrivalTimeText, 1, row );
      table.add( arrival_time, 2, row );

      ++row;
      Text hotelPickupText = ( Text ) theBoldText.clone();
      hotelPickupText.setText( iwrb.getLocalizedString( "travel.hotel_pickup", "Hotel pick-up" ) );
      PickupPlaceHome hppHome = (PickupPlaceHome) IDOLookup.getHome(PickupPlace.class);
      Collection coll = hppHome.findHotelPickupPlaces(this.supplier);
      List hpps = ListUtil.convertCollectionToList(coll);
//      HotelPickupPlace[] hpps = (HotelPickupPlace[]) coll.toArray(new HotelPickupPlace[]{});
      SelectionBox hotels = new SelectionBox( hpps );
      hotels.setName( "hotelPickupId" );
      hotels.keepStatusOnAction();

      table.add( hotelPickupText, 1, row );
      table.add( hotels, 2, row );

      table.setVerticalAlignment( 1, row, "top" );
      table.setVerticalAlignment( 2, row, "top" );


      ++row;

      Text nOSText = ( Text ) theBoldText.clone();
      nOSText.setText( iwrb.getLocalizedString( "travel.number_of_seats", "Number of seats" ) );
      table.add( nOSText, 1, row );
      table.add( numberOfSeats, 2, row );

      ++row;
      Text mNOSText = ( Text ) theBoldText.clone();
      mNOSText.setText( iwrb.getLocalizedString( "travel.minimum_number_of_seats", "Minimum number of seats" ) );
      table.add( mNOSText, 1, row );
      table.add( minNumberOfSeats, 2, row );

      ++row;
      Text eNOSText = ( Text ) theBoldText.clone();
      eNOSText.setText( iwrb.getLocalizedString( "travel.estimated_number_of_seats", "Estimated number of seats" ) );
      table.add( eNOSText, 1, row );
      table.add( estSeats, 2, row );

      ++row;
      Text noKm = ( Text ) theBoldText.clone();
      noKm.setText( iwrb.getLocalizedString( "travel.kilometers", "Kilometers" ) );
      table.add( noKm, 1, row );
      table.add( kilometers, 2, row );

      ++row;
      Text discountTypeText = ( Text ) theBoldText.clone();
      discountTypeText.setText( iwrb.getLocalizedString( "travel.discount_type", "Discount type" ) );
      table.add( discountTypeText, 1, row );
      table.add( discountType, 2, row );

      ++row;
      table.mergeCells( 1, row, 2, row );
      table.setAlignment( 1, row, "right" );
      SubmitButton submit = new SubmitButton( iwrb.getImage( "buttons/save.gif" ), ServiceDesigner.ServiceAction, ServiceDesigner.parameterCreate );
      table.add( submit, 1, row );

      table.setColumnAlignment( 1, "right" );
      table.setColumnAlignment( 2, "left" );

      if ( service != null ) {
        Parameter par1 = new Parameter( this.parameterIsUpdate, Integer.toString( tourId ) );
        par1.keepStatusOnAction();
        table.add( par1 );
        if ( timeframe != null ) {
          Parameter par2 = new Parameter( this.parameterTimeframeId, Integer.toString( timeframe.getID() ) );
          par2.keepStatusOnAction();
          table.add( par2 );
          active_from.setDate( new IWTimestamp( timeframe.getFrom() ).getSQLDate() );
          active_to.setDate( new IWTimestamp( timeframe.getTo() ).getSQLDate() );
          active_yearly.setSelected( timeframe.getIfYearly() );
        }

        name.setContent( product.getProductName(  super.getLocaleId() ) );
        number.setContent( product.getNumber() );
        description.setContent( product.getProductDescription( super.getLocaleId() ) );

        int[] days = new int[]{};//is.idega.idegaweb.travel.data.ServiceDayBMPBean.getDaysOfWeek( service.getID() );
        try {
          ServiceDayHome sdayHome = (ServiceDayHome) IDOLookup.getHome(ServiceDay.class);
          days = sdayHome.getDaysOfWeek(service.getID());
        }catch (Exception e) {
          e.printStackTrace(System.err);
        }
        for ( int i = 0; i < days.length; i++ ) {
          switch ( days[i] ) {
            case is.idega.idegaweb.travel.data.ServiceDayBMPBean.SUNDAY:
              sundays.setChecked( true );
              break;
            case is.idega.idegaweb.travel.data.ServiceDayBMPBean.MONDAY:
              mondays.setChecked( true );
              break;
            case is.idega.idegaweb.travel.data.ServiceDayBMPBean.TUESDAY:
              tuesdays.setChecked( true );
              break;
            case is.idega.idegaweb.travel.data.ServiceDayBMPBean.WEDNESDAY:
              wednesdays.setChecked( true );
              break;
            case is.idega.idegaweb.travel.data.ServiceDayBMPBean.THURSDAY:
              thursdays.setChecked( true );
              break;
            case is.idega.idegaweb.travel.data.ServiceDayBMPBean.FRIDAY:
              fridays.setChecked( true );
              break;
            case is.idega.idegaweb.travel.data.ServiceDayBMPBean.SATURDAY:
              saturdays.setChecked( true );
              break;
          }
        }
        IWTimestamp tempStamp;

        if ( depAddress != null ) {
          departure_from.setContent( depAddress.getStreetName() );
          tempStamp = new IWTimestamp( depAddress.getTime() );
          departure_time.setHour( tempStamp.getHour() );
          departure_time.setMinute( tempStamp.getMinute() );
        }

        if ( arrAddress != null ) {
          arrival_at.setContent( arrAddress.getStreetName() );
        }
        if ( service.getArrivalTime() != null) {
        		tempStamp = new IWTimestamp( service.getArrivalTime() );
            arrival_time.setHour( tempStamp.getHour() );
            arrival_time.setMinute( tempStamp.getMinute() );
        }

        Collection hppService = hppHome.findHotelPickupPlaces(this.service);
        PickupPlace hpp;
        Iterator iter = hppService.iterator();
        while (iter.hasNext()) {
          hotels.setSelectedElement(iter.next().toString());
        }

        if (tour != null) {
          if ( tour.getIsHotelPickup() ) {
            hotelPickupYes.setSelected();
          } else {
            hotelPickupNo.setSelected();
          }

          if (tour.getTotalSeats() != BookingForm.UNLIMITED_AVAILABILITY) {
          	numberOfSeats.setContent( Integer.toString( tour.getTotalSeats() ) );
          }
          minNumberOfSeats.setContent( Integer.toString( tour.getMinimumSeats() ) );
          number_of_days.setContent( Integer.toString( tour.getNumberOfDays() ) );
          estSeats.setContent( Integer.toString( tour.getEstimatedSeatsUsed() ) );
          discountType.setSelectedElement( Integer.toString( this.product.getDiscountTypeId() ) );
          kilometers.setContent( Float.toString( tour.getLength() ) );
          	
          try {
	          Collection types = tour.getTourTypes();
	        	iter = types.iterator();
	        	String[] pks = new String[types.size()];
	        	for (int i = 0; i < pks.length; i++) {
	        		pks[i] = ((TourType) iter.next()).getPrimaryKey().toString();
	        	}
	        	tourTypes.setSelectedElements(pks);
          } catch (Exception e) {
          	e.printStackTrace(System.err);
          }

        }
      } else {
        discountType.setSelectedElement( Integer.toString( com.idega.block.trade.stockroom.data.ProductBMPBean.DISCOUNT_TYPE_ID_PERCENT ) );
      }
    } else {
      table.add( iwrb.getLocalizedString( "travel.data_is_invalid", "Data is invalid" ) );
    }
    return form;
  }


  public int handleInsert( IWContext iwc ) throws RemoteException{
    return createTour(iwc);
  }

  public int createTour( IWContext iwc ) throws RemoteException {
    String sTourId = iwc.getParameter( this.parameterIsUpdate );
    int tourId = -1;
    if ( sTourId != null ) {
      tourId = Integer.parseInt( sTourId );
      try {
      	setupData(iwc, tourId);
      	if (tour == null) {
        	try {
	          tour = ( ( is.idega.idegaweb.travel.service.tour.data.TourHome ) com.idega.data.IDOLookup.getHome( Tour.class ) ).create();
          	tour.setPrimaryKey(new Integer(tourId));
          	tour.store();
        	}catch (CreateException ce) {
	          ce.printStackTrace(System.err);
        	}
      	}
      }catch (FinderException e) {
      	e.printStackTrace(System.err);
      }
    }

    String name = iwc.getParameter( "name_of_trip" );
    String number = iwc.getParameter( "number" );
    String description = iwc.getParameter( "description" );
    if ( description == null ) {
      description = "";
    }
    String imageId = iwc.getParameter( "design_image_id" );
    String activeFrom = iwc.getParameter( "active_from" );
    String activeTo = iwc.getParameter( "active_to" );
    String activeYearly = iwc.getParameter( "active_yearly" );
    String numberOfDays = iwc.getParameter( "number_of_days" );

    String allDays = iwc.getParameter( "all_days" );
    String mondays = iwc.getParameter( "mondays" );
    String tuesdays = iwc.getParameter( "tuesdays" );
    String wednesdays = iwc.getParameter( "wednesdays" );
    String thursdays = iwc.getParameter( "thursdays" );
    String fridays = iwc.getParameter( "fridays" );
    String saturdays = iwc.getParameter( "saturdays" );
    String sundays = iwc.getParameter( "sundays" );

    String departureFrom = iwc.getParameter( "departure_from" );
    String departureTime = iwc.getParameter( "departure_time" );
    String arrivalAt = iwc.getParameter( "arrival_at" );
    String arrivalTime = iwc.getParameter( "arrival_time" );
    String[] hotelPickup = iwc.getParameterValues( "hotelPickupId" );

    String numberOfSeats = iwc.getParameter( "number_of_seats" );
    String minNumberOfSeats = iwc.getParameter( "min_number_of_seats" );
    String kilometers = iwc.getParameter( "kilometers" );
    String estSeats = iwc.getParameter( "estimated_seats" );
    String discountType = iwc.getParameter( "discountType" );

    String useImageId = iwc.getParameter( "use_image_id" );
    String[] types = iwc.getParameterValues(PARAMETER_TOUR_TYPE_ID);

    /*
     *  if (hotelPickup != null) {
     *  if (hotelPickup.equals("N")) hotelPickupAddress = "";
     *  }
     */
    int serviceId = -1;

    boolean yearly = false;
    if ( activeYearly != null ) {
      if ( activeYearly.equals( "Y" ) ) {
        yearly = true;
      }
    }

    int iEstSeats = 0;
    if ( estSeats != null ) {
      try {
        iEstSeats = Integer.parseInt( estSeats );
      } catch ( NumberFormatException n ) {}
    }

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

    Integer iNumberOfSeats = null;
    if ( numberOfSeats != null ) {
      try {
        iNumberOfSeats = new Integer( numberOfSeats );
      } catch ( NumberFormatException n ) {
        iNumberOfSeats = new Integer( BookingForm.UNLIMITED_AVAILABILITY );
      }
    } else {
      iNumberOfSeats = new Integer( BookingForm.UNLIMITED_AVAILABILITY );
    }

    Integer iMinNumberOfSeats = null;
    if ( minNumberOfSeats != null ) {
      try {
        iMinNumberOfSeats = new Integer( minNumberOfSeats );
      } catch ( NumberFormatException n ) {
        iMinNumberOfSeats = new Integer( 0 );
      }
    } else {
      iMinNumberOfSeats = new Integer( 0 );
    }

    Integer iNumberOfDays = null;
    if ( numberOfDays != null ) {
      try {
        iNumberOfDays = new Integer( numberOfDays );
      } catch ( NumberFormatException n ) {
        iNumberOfDays = new Integer( 0 );
      }
    } else {
      iNumberOfDays = new Integer( 0 );
    }

    Float fKilometers = null;
    if ( kilometers != null ) {
      try {
        fKilometers = new Float( kilometers );
      } catch ( NumberFormatException n ) {
        fKilometers = new Float( 0 );
      }
    } else {
      fKilometers = new Float( 0 );
    }

    IWTimestamp activeFromStamp = null;
    if ( activeFrom != null ) {
      activeFromStamp = new IWTimestamp( activeFrom );
    }

    IWTimestamp activeToStamp = null;
    if ( activeTo != null ) {
      activeToStamp = new IWTimestamp( activeTo );
    }

    IWTimestamp departureStamp = null;
    if ( departureTime != null ) {
      departureStamp = new IWTimestamp( "2001-01-01 " + departureTime );
    }

    IWTimestamp arrivalStamp = null;
    if ( arrivalTime != null ) {
      arrivalStamp = new IWTimestamp( "2001-01-01 " + arrivalTime );
    }
    /*
     *  IWTimestamp hotelPickupTimeStamp = null;
     *  if (hotelPickupTime != null) {
     *  hotelPickupTimeStamp = new IWTimestamp("2001-01-01 "+hotelPickupTime);
     *  }
     */
    int[] tempDays = new int[7];
    int counter = 0;
    if ( allDays != null ) {
      tempDays[counter++] = java.util.GregorianCalendar.SUNDAY;
      tempDays[counter++] = java.util.GregorianCalendar.MONDAY;
      tempDays[counter++] = java.util.GregorianCalendar.TUESDAY;
      tempDays[counter++] = java.util.GregorianCalendar.WEDNESDAY;
      tempDays[counter++] = java.util.GregorianCalendar.THURSDAY;
      tempDays[counter++] = java.util.GregorianCalendar.FRIDAY;
      tempDays[counter++] = java.util.GregorianCalendar.SATURDAY;
    } else {
      if ( sundays != null ) {
        tempDays[counter++] = java.util.GregorianCalendar.SUNDAY;
      }
      if ( mondays != null ) {
        tempDays[counter++] = java.util.GregorianCalendar.MONDAY;
      }
      if ( tuesdays != null ) {
        tempDays[counter++] = java.util.GregorianCalendar.TUESDAY;
      }
      if ( wednesdays != null ) {
        tempDays[counter++] = java.util.GregorianCalendar.WEDNESDAY;
      }
      if ( thursdays != null ) {
        tempDays[counter++] = java.util.GregorianCalendar.THURSDAY;
      }
      if ( fridays != null ) {
        tempDays[counter++] = java.util.GregorianCalendar.FRIDAY;
      }
      if ( saturdays != null ) {
        tempDays[counter++] = java.util.GregorianCalendar.SATURDAY;
      }
    }

    int[] activeDays = new int[counter];
    System.arraycopy( tempDays, 0, activeDays, 0, counter );


    try {

      if ( tourId == -1 ) {
        tb.setTimeframe( activeFromStamp, activeToStamp, yearly );
        serviceId = tb.createTourService( supplier.getID(), iImageId, name, number, description, true, types, departureFrom, departureStamp, arrivalAt, arrivalStamp, hotelPickup, activeDays, iNumberOfSeats, iMinNumberOfSeats, iNumberOfDays, fKilometers, iEstSeats, iDiscountType );
      } else {
        String timeframeId = iwc.getParameter( this.parameterTimeframeId );
        if ( timeframeId == null ) {
          timeframeId = "-1";
        }
        tb.setTimeframe( Integer.parseInt( timeframeId ), activeFromStamp, activeToStamp, yearly );

        serviceId = tb.updateTourService( tourId, supplier.getID(), iImageId, name, number, description, true, types, departureFrom, departureStamp, arrivalAt, arrivalStamp, hotelPickup, activeDays, iNumberOfSeats, iMinNumberOfSeats, iNumberOfDays, fKilometers, iEstSeats, iDiscountType );
        if ( useImageId == null ) {
          Product product = getProductBusiness(iwc).getProduct( serviceId );
          ProductEditorBusiness.getInstance().dropImage( product, true );
        }
      }



    } catch ( Exception e ) {
      e.printStackTrace( System.err );
      //add("TEMP - Service EKKI smíðuð");
    }

    return serviceId;
  }

  public void finalizeCreation(IWContext iwc, Product product) throws RemoteException, FinderException {
//  		getTourBusiness(iwc).finalizeHotelCreation(product);
  }


  private TourBusiness getTourBusiness(IWApplicationContext iwac) throws RemoteException {
    return (TourBusiness) IBOLookup.getServiceInstance(iwac, TourBusiness.class);
  }

  private TourTypeHome getTourTypeHome() throws IDOLookupException {
  	return (TourTypeHome) IDOLookup.getHome(TourType.class);
  }
  
}
