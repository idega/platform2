

package is.idega.idegaweb.campus.presentation;





import com.idega.presentation.PresentationObject;



/**

 *

 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>

 * @version 1.0

 */

public interface TabAction {



  public static final int NOACT = 0;

  public static final int ACT1 = 1;

  public static final int ACT2 = 2;

  public static final int ACT3 = 3;

  public static final int ACT4 = 4;

  public static final int ACT5 = 5;

  public static final int ACT6 = 6;

  public static final int ACT7 = 7;

  public static final int ACT8 = 8;

  public static final String sAction = "campus_action";



  PresentationObject getTabs();





}

