//idega 2000 - Tryggvi Larusson

/*

*Copyright 2000 idega.is All Rights Reserved.

*/



package is.idega.idegaweb.golf.entity;







import java.sql.*;

import javax.naming.*;

//import javax.sql.*;

import com.idega.data.*;

import com.idega.util.database.*;





/**

*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>

*@version 1.2

*/

public abstract class GolfEntityBMPBean extends com.idega.data.GenericEntity implements is.idega.idegaweb.golf.entity.GolfEntity {





	public GolfEntityBMPBean(){

		super();

	}



	public GolfEntityBMPBean(int id)throws SQLException{

		super(id);

	}





	/*public Connection getConnection() throws SQLException{

		return getConnection("default");

	}



	public Connection getConnection(String dbConnectionString)throws SQLException{

		try{

			InitialContext ctx = new InitialContext();

			// Look up data source in InitialContext.

			DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/" + dbConnectionString);

			if (ds != null){

				return ds.getConnection();

			}

			else{

				return null;

			}

		}

		catch(NamingException e){

			throw new SQLException("Naming failed with the exception: "+e.getMessage());

		}

	}*/



/*



	public Connection getConnection(String dbConnectionString)throws SQLException{

		return ConnectionBroker.getConnection(dbConnectionString);

	}



	public Connection getConnection()throws SQLException{

		return ConnectionBroker.getConnection();

	}





	public void freeConnection(String dbConnectionString,Connection connection){

		ConnectionBroker.freeConnection(dbConnectionString,connection);

	}



	public void freeConnection(Connection connection){

		ConnectionBroker.freeConnection(connection);

	}

*/



	public abstract String getEntityName();



	public abstract void initializeAttributes();



}

