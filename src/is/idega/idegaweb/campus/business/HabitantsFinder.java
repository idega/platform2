package is.idega.idegaweb.campus.business;

import com.idega.data.EntityFinder;
import com.idega.data.IDOFinderException;
import is.idega.idegaweb.campus.data.Habitant;
import java.util.List;

public class HabitantsFinder {

  public static List findHabitants(int complexId){
    try {
      return EntityFinder.getInstance().findAllByColumn(Habitant.class,Habitant.getColumnComplexId(),complexId);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }
}