package is.idega.idegaweb.travel.service.presentation;

import com.idega.block.trade.stockroom.data.Product;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Form;
import java.rmi.RemoteException;
import javax.ejb.FinderException;
/**
 * <p>Title: idega</p>
 * <p>Description: software</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: idega software</p>
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public interface DesignerForm {
  public int handleInsert(IWContext iwc) throws RemoteException;
  public void finalizeCreation(IWContext iwc, Product product) throws RemoteException, FinderException;
  public Form getDesignerForm( IWContext iwc ) throws RemoteException, FinderException;
  public Form getDesignerForm( IWContext iwc, int serviceId ) throws RemoteException, FinderException;
}
