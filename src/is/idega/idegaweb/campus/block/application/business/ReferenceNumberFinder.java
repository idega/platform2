package is.idega.idegaweb.campus.block.application.business;

import com.idega.idegaweb.IWApplicationContext;
import com.idega.block.application.business.ReferenceNumberHandler;
import com.idega.util.CypherText;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class ReferenceNumberFinder {

	public static final int LENGTH = 7;
	
  private static ReferenceNumberFinder finder;

  private String key = "";
  private CypherText cypherText;
  private ReferenceNumberHandler refHandler;

  private ReferenceNumberFinder(IWApplicationContext iwac){
    //System.err.println("Creating Finder");
    refHandler = new ReferenceNumberHandler();
    cypherText = new CypherText(iwac);
    key = refHandler.getCypherKey(iwac);
  }

  public static ReferenceNumberFinder getInstance(IWApplicationContext iwac){
    if(finder ==null)
      finder = new ReferenceNumberFinder(iwac);
    return finder;

  }

  public String lookup(int applicationId){
    String id = Integer.toString(applicationId);
//    while(id.length() < 6)
  while(id.length() < LENGTH)
      id = "0"+id;
    String refNum = cypherText.doCyper(id,key);
    //System.err.println("Referencenumber lookup : ID = "+id+" REFNUM = "+refNum);
    return refNum;
  }

}