package is.idega.experimental.ibotest;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import com.idega.business.IBOLookup;
/**
 * Title:        idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */
public class IBOTest {
	private static boolean lookupThroughJNDI = false;
	public static void main(String args[]) {
		doIBOTest();
	}
	public static void doIBOTest() {
		if (!lookupThroughJNDI) {
			//PoolManager.getInstance("/idega/webapps/ROOT/idegaweb/properties/db.properties");
		}
		//EntityControl.setAutoCreationOfEntities(true);
		try {
			SimpleService service = getSimpleService();
			System.out.println("Response to service.sayHello() was: "+service.sayHello());
			/*
			        Collection questions = qhome.findAllQuestionsContaining("crapps");
			        Iterator iter = questions.iterator();
			        while (iter.hasNext()) {
			          Question item = (Question)iter.next();
			          System.out.println("Found: "+item.getText()+" with id="+item.getPrimaryKey());
			        }
			*/
			System.out.println("IBOTest ran OK");
		} catch (Exception e) {
			System.out.println("Error running IBOTest");
			e.printStackTrace();
		}
	}
	private static InitialContext getInitialContext() throws NamingException {
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("/idega/jndi.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new InitialContext(properties);
	}
	
	public static SimpleService getSimpleService() throws Exception {
		if (lookupThroughJNDI) {
			SimpleServiceHome home = getSimpleServiceHome();
			return home.create();
		} else {
			return (SimpleService) IBOLookup.getServiceInstance(null, SimpleService.class);
		}
	}
	
	public static SimpleServiceHome getSimpleServiceHome() throws Exception {
		if (lookupThroughJNDI) {
			InitialContext jndiContext = getInitialContext();
			SimpleServiceHome home = null;
			Object homeObj = jndiContext.lookup(SimpleServiceBean.class.getName());
			//home = (ResponseHome) jndiContext.lookup("java:comp/env/"+ResponseBMPBean.class.getName());
			home = (SimpleServiceHome) PortableRemoteObject.narrow(homeObj, SimpleServiceHome.class);
			return home;
		} else {
			//return (SimpleServiceHome) IBOLookup.getServiceInstance(null, SimpleService.class);
			return null;
		}
	}
}
