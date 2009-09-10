package com.idega.block.text.data;


public interface TextEntityHome extends com.idega.data.IDOHome
{
 public TextEntity create() throws javax.ejb.CreateException;
 public TextEntity findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}