/*
 * $Id: ExceptionWrapper.java,v 1.2 2001/05/16 18:58:06 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.jmodule.object;

/**
 *
 * @author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.2
 */
public class ExceptionWrapper extends ModuleObjectContainer {
  private Exception ex;
  private ModuleObject obj;

  public ExceptionWrapper() {
  }

  public ExceptionWrapper(Exception ex, ModuleObject thrower) {
    setException(ex);
    setThrower(thrower);
    add("Exception: in " + thrower.getClass().getName() + " <br> " + ex.getClass().getName() + "  " + ex.getMessage());
    ex.printStackTrace(System.err);
  }

  public void setException(Exception ex) {
    this.ex = ex;
  }

  public void setThrower(ModuleObject obj) {
    this.obj = obj;
  }
}
