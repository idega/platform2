package com.idega.block.albumcollection.data;


public class TrackHomeImpl extends com.idega.data.IDOFactory implements TrackHome
{
 protected Class getEntityInterfaceClass(){
  return Track.class;
 }


 public Track create() throws javax.ejb.CreateException{
  return (Track) super.createIDO();
 }


 public Track createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }


 public Track findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Track) super.findByPrimaryKeyIDO(pk);
 }


 public Track findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Track) super.findByPrimaryKeyIDO(id);
 }


 public Track findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }



}