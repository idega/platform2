package is.idega.experimental.ibotest;

import com.idega.business.IBOServiceBean;

/**
 * Title:        IdegaWeb
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      Idega Software
 * @author Tryggvi Larusson
 * @version 1.0
 */

public class SimpleServiceBean extends IBOServiceBean implements SimpleService{

		public String sayHello(){
			return "Hello! This is a machine running "+System.getProperty("os.name")+" here";	
		}
}