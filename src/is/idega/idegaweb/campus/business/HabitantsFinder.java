package is.idega.idegaweb.campus.business;

import is.idega.idegaweb.campus.data.Habitant;

import java.util.List;

import com.idega.data.EntityFinder;

public class HabitantsFinder {
  public static List findHabitants(int complexId){
    try {
      return EntityFinder.getInstance().findAllByColumn(Habitant.class,is.idega.idegaweb.campus.data.HabitantBMPBean.getColumnComplexId(),complexId);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

    return null;
  }
}