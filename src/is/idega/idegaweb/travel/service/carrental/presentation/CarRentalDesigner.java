package is.idega.idegaweb.travel.service.carrental.presentation;

import java.rmi.*;
import java.util.Collection;
import java.util.Iterator;

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
import is.idega.idegaweb.travel.service.carrental.business.*;
import is.idega.idegaweb.travel.service.carrental.data.CarRental;
import is.idega.idegaweb.travel.service.carrental.data.CarRentalHome;
import is.idega.idegaweb.travel.service.presentation.*;

/**
 * <p>Title: idega</p>
 * <p>Description: software</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: idega software</p>
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class CarRentalDesigner extends TravelManager implements DesignerForm {

  private IWResourceBundle _iwrb;
  private Supplier _supplier;
  private Service _service;
  private Product _product;
  private CarRental _carRental;
  private Timeframe _timeframe;

  String NAME_OF_FORM = ServiceDesigner.NAME_OF_FORM;
  String ServiceAction = ServiceDesigner.ServiceAction;

  private String PARAMETER_IS_UPDATE       = "isTourUpdate";
  private String PARAMETER_TIMEFRAME_ID    = "td_timeframeId";
  private String PARAMETER_NAME            = "hd_par_name";
  private String PARAMETER_DESCRIPTION     = "hd_par_desc";
  private String PARAMETER_NUMBER          = "hd_par_num";
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
  private String PARAMETER_PICKUP_PLACE    = "hd_par_pp";
  private String PARAMETER_DROPOFF_PLACE    = "hd_par_dp";
  private String PARAMETER_DISCOUNT_TYPE   = "hd_par_disc";
  private String PARAMETER_DESIGN_IMAGE_ID = "hd_par_des_img_id";
  private String PARAMETER_USE_IMAGE_ID    = "hd_par_use_img_id";


  public CarRentalDesigner(IWContext iwc) throws Exception {
    init(iwc);
  }

  private void init( IWContext iwc ) throws Exception {
    super.main( iwc );
    _iwrb = super.getResourceBundle();
    _supplier = super.getSupplier();
  }

  private boolean setupData(int serviceId) {
    try {
    	CarRentalHome cHome = (CarRentalHome) IDOLookup.getHome(CarRental.class);
      ServiceHome sHome = (ServiceHome) IDOLookup.getHome(Service.class);
      ProductHome pHome = (ProductHome) IDOLookup.getHome(Product.class);
      try {
      	_carRental = cHome.findByPrimaryKey(new Integer(serviceId));
      }catch (Exception e) {}
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
		String activeFrom = iwc.getParameter( PARAMETER_ACTIVE_FROM );
		String activeTo = iwc.getParameter( PARAMETER_ACTIVE_TO );
		String activeYearly = iwc.getParameter( PARAMETER_ACTIVE_YEARLY );
	
		String allDays = iwc.getParameter( PARAMETER_ALL_DAYS );
		String mondays = iwc.getParameter( PARAMETER_MONDAYS );
		String tuesdays = iwc.getParameter( PARAMETER_TUESDAYS );
		String wednesdays = iwc.getParameter( PARAMETER_WEDNESDAYS );
		String thursdays = iwc.getParameter( PARAMETER_THURSDAYS );
		String fridays = iwc.getParameter( PARAMETER_FRIDAYS );
		String saturdays = iwc.getParameter( PARAMETER_SATURDAYS );
		String sundays = iwc.getParameter( PARAMETER_SUNDAYS );
		String useImageId = iwc.getParameter( PARAMETER_USE_IMAGE_ID );
	/*
		String departureFrom = iwc.getParameter( PARAMETER_DEPARTURE_FROM );
		String departureTime = iwc.getParameter( PARAMETER_DEPARTURE_TIME );
		String arrivalAt = iwc.getParameter( PARAMETER_ARRIVAL_AT );
		String arrivalTime = iwc.getParameter( PARAMETER_ARRIVAL_TIME );
	*/
		String[] pickupPlaceIds = iwc.getParameterValues( PARAMETER_PICKUP_PLACE );
		String[] dropoffPlaceIds = iwc.getParameterValues( PARAMETER_DROPOFF_PLACE );
	
		String discountType = iwc.getParameter( PARAMETER_DISCOUNT_TYPE );
	
	
		boolean yearly = false;
		if ( activeYearly != null ) {
		  if ( activeYearly.equals( "Y" ) ) {
			yearly = true;
		  }
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
	
	
		IWTimestamp activeFromStamp = null;
		if ( activeFrom != null ) {
		  activeFromStamp = new IWTimestamp( activeFrom );
		}
	
		IWTimestamp activeToStamp = null;
		if ( activeTo != null ) {
		  activeToStamp = new IWTimestamp( activeTo );
		}
	/*
		IWTimestamp departureStamp = null;
		if ( departureTime != null ) {
		  departureStamp = new IWTimestamp( "2001-01-01 " + departureTime );
		}
	
		IWTimestamp arrivalStamp = null;
		if ( arrivalTime != null ) {
		  arrivalStamp = new IWTimestamp( "2001-01-01 " + arrivalTime );
		}
	*/
	
		int[] tempDays = new int[7];
		int counter = 0;
		if ( allDays != null ) {
		  tempDays[counter++] = is.idega.idegaweb.travel.data.ServiceDayBMPBean.SUNDAY;
		  tempDays[counter++] = is.idega.idegaweb.travel.data.ServiceDayBMPBean.MONDAY;
		  tempDays[counter++] = is.idega.idegaweb.travel.data.ServiceDayBMPBean.TUESDAY;
		  tempDays[counter++] = is.idega.idegaweb.travel.data.ServiceDayBMPBean.WEDNESDAY;
		  tempDays[counter++] = is.idega.idegaweb.travel.data.ServiceDayBMPBean.THURSDAY;
		  tempDays[counter++] = is.idega.idegaweb.travel.data.ServiceDayBMPBean.FRIDAY;
		  tempDays[counter++] = is.idega.idegaweb.travel.data.ServiceDayBMPBean.SATURDAY;
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
	
	
		int returner = -1;
	
		CarRentalBusiness hb = (CarRentalBusiness) IBOLookup.getServiceInstance(iwc, CarRentalBusiness.class);
	
		try {
		  if ( serviceId == -1 ) {
				hb.setTimeframe( activeFromStamp, activeToStamp, yearly );
				returner = hb.createCar(_supplier.getID(), iImageId, name, number, description, activeDays,pickupPlaceIds, dropoffPlaceIds, true, iDiscountType);
		  } else {
				String timeframeId = iwc.getParameter( PARAMETER_TIMEFRAME_ID );
				if ( timeframeId == null ) {
				  timeframeId = "-1";
				}
				hb.setTimeframe( Integer.parseInt( timeframeId ), activeFromStamp, activeToStamp, yearly );
				returner = hb.updateCar(serviceId, _supplier.getID(), iImageId, name, number, description, activeDays, pickupPlaceIds, dropoffPlaceIds, true, iDiscountType);
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
  }

  public Form getDesignerForm( IWContext iwc ) throws RemoteException, FinderException {
    return getDesignerForm(iwc, -1);
  }

  public Form getDesignerForm( IWContext iwc, int serviceId ) throws RemoteException, FinderException {
    boolean isDataValid = true;


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

      DateInput active_from = new DateInput( PARAMETER_ACTIVE_FROM );
      active_from.setDate( stamp.getDate() );
      active_from.setYearRange( 2001, currentYear + 5 );
      active_from.keepStatusOnAction();
      DateInput active_to = new DateInput( PARAMETER_ACTIVE_TO );
      stamp.addDays( 92 );
      active_to.setDate( stamp.getDate() );
      active_to.setYearRange( 2001, currentYear + 5 );
      active_to.keepStatusOnAction();
      BooleanInput active_yearly = new BooleanInput( PARAMETER_ACTIVE_YEARLY );
      active_yearly.setSelected( false );
      active_yearly.keepStatusOnAction();

      CheckBox allDays = new CheckBox( PARAMETER_ALL_DAYS );
      CheckBox mondays = new CheckBox( PARAMETER_MONDAYS );
      CheckBox tuesdays = new CheckBox( PARAMETER_TUESDAYS );
      CheckBox wednesdays = new CheckBox( PARAMETER_WEDNESDAYS );
      CheckBox thursdays = new CheckBox( PARAMETER_THURSDAYS );
      CheckBox fridays = new CheckBox( PARAMETER_FRIDAYS );
      CheckBox saturdays = new CheckBox( PARAMETER_SATURDAYS );
      CheckBox sundays = new CheckBox( PARAMETER_SUNDAYS );
      allDays.keepStatusOnAction();
      mondays.keepStatusOnAction();
      tuesdays.keepStatusOnAction();
      wednesdays.keepStatusOnAction();
      thursdays.keepStatusOnAction();
      fridays.keepStatusOnAction();
      saturdays.keepStatusOnAction();
      sundays.keepStatusOnAction();

			PickupPlaceHome ppHome = (PickupPlaceHome) IDOLookup.getHome(PickupPlace.class);
			SelectionBox pickupPlaces = new SelectionBox( PARAMETER_PICKUP_PLACE );
			Collection coll = ppHome.findHotelPickupPlaces(_supplier);
			if (coll != null && !coll.isEmpty()) {
				Iterator iter = coll.iterator();
				PickupPlace p;
				while (iter.hasNext()) {
					p = (PickupPlace) iter.next();
					pickupPlaces.addMenuElement(p.getPrimaryKey().toString(), p.getName());
				}	
			}
			SelectionBox dropoffPlaces = new SelectionBox( PARAMETER_DROPOFF_PLACE );
			coll = ppHome.findDropoffPlaces(_supplier);
			if (coll != null && !coll.isEmpty()) {
				Iterator iter = coll.iterator();
				PickupPlace p;
				while (iter.hasNext()) {
					p = (PickupPlace) iter.next();
					dropoffPlaces.addMenuElement(p.getPrimaryKey().toString(), p.getName());
				}	
			}
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
      alld.setText( _iwrb.getLocalizedString( "travel.all_days", "All" ) );
      Text mond = ( Text ) smallText.clone();
      mond.setText( _iwrb.getLocalizedString( "travel.mon", "mon" ) );
      Text tued = ( Text ) smallText.clone();
      tued.setText( _iwrb.getLocalizedString( "travel.tue", "tue" ) );
      Text wedd = ( Text ) smallText.clone();
      wedd.setText( _iwrb.getLocalizedString( "travel.wed", "wed" ) );
      Text thud = ( Text ) smallText.clone();
      thud.setText( _iwrb.getLocalizedString( "travel.thu", "thu" ) );
      Text frid = ( Text ) smallText.clone();
      frid.setText( _iwrb.getLocalizedString( "travel.fri", "fri" ) );
      Text satd = ( Text ) smallText.clone();
      satd.setText( _iwrb.getLocalizedString( "travel.sat", "sat" ) );
      Text sund = ( Text ) smallText.clone();
      sund.setText( _iwrb.getLocalizedString( "travel.sun", "sun" ) );

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
      weekdaysText.setText( _iwrb.getLocalizedString( "travel.weekdays", "Weekdays" ) );
      table.add( weekdaysText, 1, row );
      table.add( weekdayFixTable, 2, row );

			++row;
			Text pickupPlacesText = (Text) theBoldText.clone();
			table.setVerticalAlignment(1, row, Table.VERTICAL_ALIGN_TOP);
			pickupPlacesText.setText( _iwrb.getLocalizedString("travel.pickup_places","Pickup places"));
			table.add( pickupPlacesText, 1, row);
			table.add( pickupPlaces, 2, row);

			++row;
			Text dropoffPlacesText = (Text) theBoldText.clone();
			table.setVerticalAlignment(1, row, Table.VERTICAL_ALIGN_TOP);
			dropoffPlacesText.setText( _iwrb.getLocalizedString("travel.dropoff_places","Dropoff places"));
			table.add( dropoffPlacesText, 1, row);
			table.add( dropoffPlaces, 2, row);

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

      if ( _product != null ) {
        Parameter par1 = new Parameter( PARAMETER_IS_UPDATE, Integer.toString( serviceId ) );
        par1.keepStatusOnAction();
        table.add( par1 );
        if ( _timeframe != null ) {
          Parameter par2 = new Parameter( PARAMETER_TIMEFRAME_ID, Integer.toString( _timeframe.getID() ) );
          par2.keepStatusOnAction();
          table.add( par2 );
          active_from.setDate( new IWTimestamp( _timeframe.getFrom() ).getDate() );
          active_to.setDate( new IWTimestamp( _timeframe.getTo() ).getDate() );
          active_yearly.setSelected( _timeframe.getIfYearly() );
        }

        name.setContent( _product.getProductName( super.getLocaleId() ) );
        number.setContent( _product.getNumber() );
        description.setContent( _product.getProductDescription( super.getLocaleId() ) );
        try {
        	if (_carRental != null) {
						coll = _carRental.getPickupPlaces();
		        if (coll != null && !coll.isEmpty() ) {
		        	String[] temp = new String[coll.size()];
		        	Iterator iter = coll.iterator();
		        	for (int i = 0; i < temp.length ; i++ ) {
		        		temp[i] = ((PickupPlace)iter.next()).getPrimaryKey().toString();	
		        	}
							pickupPlaces.setSelectedElements(temp);
		        }
						coll = _carRental.getDropoffPlaces();
						if (coll != null && !coll.isEmpty() ) {
							String[] temp = new String[coll.size()];
							Iterator iter = coll.iterator();
							for (int i = 0; i < temp.length ; i++ ) {
		        		temp[i] = ((PickupPlace)iter.next()).getPrimaryKey().toString();	
							}
							dropoffPlaces.setSelectedElements(temp);
						}
        	}
				} catch (IDORelationshipException e1) {
					e1.printStackTrace(System.err);
				}
        

        int[] days = new int[]{};//is.idega.idegaweb.travel.data.ServiceDayBMPBean.getDaysOfWeek( service.getID() );
        try {
          ServiceDayHome sdayHome = (ServiceDayHome) IDOLookup.getHome(ServiceDay.class);
          days = sdayHome.getDaysOfWeek(_service.getID());
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


        discountType.setSelectedElement( Integer.toString( _product.getDiscountTypeId() ) );
      } else {
        discountType.setSelectedElement( Integer.toString( com.idega.block.trade.stockroom.data.ProductBMPBean.DISCOUNT_TYPE_ID_PERCENT ) );
      }
    } else {
      table.add( _iwrb.getLocalizedString( "travel.data_is_invalid", "Data is invalid" ) );
    }
    return form;
  }

}
