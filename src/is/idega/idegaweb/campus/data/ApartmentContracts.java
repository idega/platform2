package is.idega.idegaweb.campus.data;



import is.idega.idegaweb.campus.block.allocation.data.Contract;

import java.util.List;
import java.util.Vector;

import com.idega.block.building.data.Apartment;
import com.idega.util.IWTimestamp;



/**

 * Title:   idegaclasses

 * Description:

 * Copyright:    Copyright (c) 2001

 * Company:

 * @author  <a href="mailto:aron@idega.is">aron@idega.is

 * @version 1.0

 */



public class ApartmentContracts {



  private Apartment eApartment;

  private Vector Contracts;

  private IWTimestamp nextDate;



  public ApartmentContracts(int iApartmentId) {

    Contracts = new Vector();

    nextDate = IWTimestamp.RightNow();

  }



  public void setApartment(Apartment apartment){

    eApartment = apartment;

  }



  public void addContract(Contract contract){

    Contracts.add(contract);

    checkAdd(contract);

  }



  public void setContracts(List contracts){

    Contracts.addAll(contracts);



    java.util.Iterator I = Contracts.iterator();

    while(I.hasNext()){

      Contract C = (Contract) I.next();

      checkAdd(C);

    }

  }



  private void checkAdd(Contract C){

    //if(C.getStatus().equals(C.statusSigned) || C.getStatus().equals(C.statusResigned)){

        IWTimestamp date = new IWTimestamp(C.getValidTo());

        if(nextDate != null && date.isLaterThan(nextDate)){

          nextDate = date;

          //System.err.println(date.toString());

        }

    //}

  }







  public Apartment getApartment(){

    return eApartment ;

  }



  public List getContracts(){

    return Contracts;

  }



  public IWTimestamp getNextDate(){

    return nextDate ;

  }

}

