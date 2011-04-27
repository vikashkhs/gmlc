/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.diameter.api;

import org.jdiameter.api.Message;

/**
 * 
 * DiameterMessageFactory.java
 *
 * @version 1.0 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 */
public interface DiameterMessageFactory
{
  /**
   * Creates a new Diameter Message (request or answer, depending on boolean flag) 
   * with the specified Command Code and Application Id
   *  
   * @param isRequest
   * @param commandCode
   * @param applicationId
   * @return
   */
  public Message createMessage(boolean isRequest, int commandCode, long applicationId);
  
  /**
   * Creates a new Diameter Message (Request) with the specified Command Code and 
   * Application Id 
   * 
   * @param commandCode
   * @param applicationId
   * @return
   */
  public Message createRequest(int commandCode, long applicationId);
  
  /**
   * Creates a new Diameter Message (Answer) with the specified Command Code and 
   * Application Id 
   * 
   * @param commandCode
   * @param applicationId
   * @return
   */
  public Message createAnswer(int commandCode, long applicationId);

}
