package is.idega.idegaweb.golf.member;

import com.idega.presentation.text.Text;
import com.idega.presentation.ui.InterfaceObject;


public class Input{

  private static int bodyFontSize = 1;
  private static int fontSize = 2;
 // private String  DarkColor = "#336660",LightColor = "#CEDFD0", MiddleColor = "#ADCAB1";
  private static String bodyFontColor =  "#336660";
  private static String backGroundColor = "#CEDFD0";
  private static String styleAttribute = "font-size: 8pt";
  private static boolean bodyFontBold = true;

  public void setBodyFontColor(String color){
    this.bodyFontColor = color;
  }
  public void setBodyFontSize(int size){
    this.fontSize = size;
  }
  public void setBodyFontBold(boolean bold){
    this.bodyFontBold = bold;
  }
  public void setStyleAttribute(String style){
    styleAttribute = style;
  }
  public static Text bodyText(String s){
    Text T= new Text();
    if(s!=null){
      T= new Text(s);
      T.setFontColor(bodyFontColor);
      T.setFontSize(bodyFontSize);
      if(bodyFontBold)
        T.setBold();
    }
    return T;
  }
  public static Text bodyText(int i){
    return bodyText(String.valueOf(i));
  }
   protected static void setStyle(InterfaceObject O){
    O.setStyleAttribute(styleAttribute);
  }
}