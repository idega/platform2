package com.idega.block.beanshell.business;


public interface BSHEngine extends com.idega.business.IBOService
{
 public bsh.Interpreter getBSHInterpreter() throws java.rmi.RemoteException;
 public java.lang.String getBshVersion() throws java.rmi.RemoteException;
 public java.lang.Object runScript(java.lang.String p0,com.idega.presentation.IWContext p1)throws bsh.EvalError,bsh.TargetError, java.rmi.RemoteException;
 public java.lang.Object runScript(java.lang.String p0)throws bsh.EvalError, java.rmi.RemoteException;
 public java.lang.Object runScriptFromBundle(com.idega.idegaweb.IWBundle p0,java.lang.String p1,com.idega.presentation.IWContext p2)throws java.io.FileNotFoundException,bsh.EvalError, java.rmi.RemoteException;
 public java.lang.Object runScriptFromBundle(com.idega.idegaweb.IWBundle p0,java.lang.String p1)throws java.io.FileNotFoundException,bsh.EvalError, java.rmi.RemoteException;
 public java.lang.Object runScriptFromFileWithPath(java.lang.String p0)throws java.io.FileNotFoundException,bsh.EvalError, java.rmi.RemoteException;
 public java.lang.Object runScriptFromFileWithPath(java.lang.String p0,com.idega.presentation.IWContext p1)throws java.io.FileNotFoundException,bsh.EvalError, java.rmi.RemoteException;
 public java.lang.Object runScriptFromICFile(com.idega.core.file.data.ICFile p0,com.idega.presentation.IWContext p1)throws java.io.FileNotFoundException,bsh.EvalError, java.rmi.RemoteException;
}
