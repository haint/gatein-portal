/**
 * Copyright (C) 2012 eXo Platform SAS.
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
package org.exoplatform.portal.gadget.core;

import com.google.inject.Inject;

import org.apache.shindig.auth.BasicSecurityTokenCodec;
import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.auth.SecurityTokenCodec;
import org.apache.shindig.auth.SecurityTokenException;
import org.apache.shindig.config.ContainerConfig;

import java.util.Map;

/**
 * @author <a href="nguyenanhkien2a@gmail.com">Kien Nguyen</a>
 * @version $Revision$
 */

public class GateInSecurityTokenCodec implements SecurityTokenCodec
{
   private static final String SECURITY_TOKEN_TYPE = "gadgets.securityTokenType";

   private final SecurityTokenCodec codec;

   @Inject
   public GateInSecurityTokenCodec(ContainerConfig config)
   {
      String tokenType = config.getString(ContainerConfig.DEFAULT_CONTAINER, SECURITY_TOKEN_TYPE);
      if ("insecure".equals(tokenType))
      {
         codec = new BasicSecurityTokenCodec();
      }
      else if ("secure".equals(tokenType))
      {
         codec = new GateInBlobCrypterSecurityTokenCodec(config);
      }
      else
      {
         throw new RuntimeException("Unknown security token type specified in " + ContainerConfig.DEFAULT_CONTAINER
            + " container configuration. " + SECURITY_TOKEN_TYPE + ": " + tokenType);
      }
   }

   public SecurityToken createToken(Map<String, String> tokenParameters) throws SecurityTokenException
   {
      return codec.createToken(tokenParameters);
   }

   public String encodeToken(SecurityToken token) throws SecurityTokenException
   {
      if (token == null)
      {
         return null;
      }
      return codec.encodeToken(token);
   }
}
