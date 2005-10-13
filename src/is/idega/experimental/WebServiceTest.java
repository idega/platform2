/*
 * $Id: WebServiceTest.java,v 1.1 2005/10/13 11:57:23 laddi Exp $ Created on Oct 13, 2005
 * 
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to
 * license terms.
 */
package is.idega.experimental;

import is.mentor.vefthjonusta.SkraningNemenda;
import is.mentor.vefthjonusta.SkraningNemendaLocator;
import is.mentor.vefthjonusta.SkraningNemendaSoap;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.xml.rpc.ServiceException;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;

/**
 * Last modified: $Date: 2005/10/13 11:57:23 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class WebServiceTest extends Block {

	public void main(IWContext iwc) throws Exception {
		String reply = null;
		try {
			Calendar calendar = new GregorianCalendar();
			
			SkraningNemenda service = new SkraningNemendaLocator();
			SkraningNemendaSoap soap = service.getSkraningNemendaSoap();
			reply = soap.skraNemanda("07Pjs7Pjt0PDtsPGk8MD47PjtsPHc2tyw6E7Pj4Q47bDx0PEAwPPjs+Ozs+O3Q8cDxwPGwD8VGV4dDs+O2w8w5ps7Ozs7Ozs7Pjs7Pjt0PHA8O0AwPDs7Oz4+O2w8aTwwPjs+O2w8O2w8aTwxPjtpPDM+Oz8dDxwPPjtsPGk8MD47aTwxPjtpPDI+O2k8Mz47aTw0PjtpPDU+O2k8Nj47PjtsPHQ8dDw7QDA8aTw0PjtpPDADtpPDc+", "0202774919", "0610703899", calendar, calendar, 1);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (ServiceException e) {
			e.printStackTrace();
		}
		
		add("reply = " + reply);
	}
}