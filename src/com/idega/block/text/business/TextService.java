package com.idega.block.text.business;


public interface TextService extends com.idega.business.IBOService
{
 public boolean addFileToContent(java.lang.Integer p0,java.lang.Integer p1) throws java.rmi.RemoteException;
 public boolean removeContent(java.lang.Integer p0) throws java.rmi.RemoteException;
 public boolean removeFileFromContent(java.lang.Integer p0,java.lang.Integer p1) throws java.rmi.RemoteException;
 public com.idega.block.text.data.Content storeContent(java.lang.Integer p0,java.lang.Integer p1,java.lang.Integer p2,java.lang.Integer p3,java.sql.Timestamp p4,java.sql.Timestamp p5,java.lang.String p6,java.lang.String p7,java.lang.String p8,java.util.List p9) throws java.rmi.RemoteException;
 public com.idega.block.text.data.Content storeContent(java.lang.Integer p0,java.lang.Integer p1,java.lang.Integer p2,java.lang.Integer p3,java.sql.Timestamp p4,java.sql.Timestamp p5,java.lang.String p6,java.lang.String p7,java.lang.String p8,java.util.List p9,java.sql.Timestamp p10) throws java.rmi.RemoteException;
 public com.idega.block.text.data.TxText storeText(java.lang.Integer p0,java.lang.Integer p1,java.lang.Integer p2,java.lang.Integer p3,java.lang.String p4,java.lang.String p5,java.lang.String p6) throws java.rmi.RemoteException;
 public com.idega.block.text.data.TxText storeText(java.lang.Integer p0,java.lang.Integer p1,java.lang.Integer p2,java.lang.Integer p3,java.lang.Integer p4,java.sql.Timestamp p5,java.sql.Timestamp p6,java.lang.String p7,java.lang.String p8,java.lang.String p9,java.lang.String p10,java.util.List p11) throws java.rmi.RemoteException;
}
