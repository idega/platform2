package com.idega.block.finance.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.user.data.Group;


public interface BankInfoHome extends com.idega.data.IDOHome
{
 public BankInfo create() throws javax.ejb.CreateException;
 public BankInfo findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public BankInfo findByGroupId(int groupId) throws FinderException;
 public Collection findAllByClub(Group club) throws FinderException;


}