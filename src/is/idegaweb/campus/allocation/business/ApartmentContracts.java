package is.idegaweb.campus.allocation.business;



import is.idegaweb.campus.entity.Contract;
import com.idega.block.building.data.Apartment;
import com.idega.block.building.business.BuildingCacher;
import java.util.List;
import java.util.Vector;
import com.idega.util.idegaTimestamp;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class ApartmentContracts {

  private Apartment eApartment;
  private Vector Contracts;
  private idegaTimestamp nextDate;

  public ApartmentContracts(int iApartmentId) {
    Contracts = new Vector();
    nextDate = idegaTimestamp.RightNow();
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
        idegaTimestamp date = new idegaTimestamp(C.getValidTo());
        if(nextDate != null && date.isLaterThan(nextDate)){
          nextDate = date;
          System.err.println(date.toString());
        }
    //}
  }



  public Apartment getApartment(){
    return eApartment ;
  }

  public List getContracts(){
    return Contracts;
  }

  public idegaTimestamp getNextDate(){
    return nextDate ;
  }
}
