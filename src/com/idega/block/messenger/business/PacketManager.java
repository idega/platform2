package com.idega.block.messenger.business;

import com.idega.block.messenger.data.Packet;

/**
 * Title:        com.idega.block.messenger.business
 * Description:  idega classes
 * Copyright:    Copyright (c) 2001
 * Company:      Idega Software
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 * @version 1.0
 */

public interface PacketManager {

  public void processPacket(Packet packet);

}