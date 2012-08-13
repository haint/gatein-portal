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

import org.apache.commons.lang.StringUtils;
import org.apache.shindig.auth.AnonymousSecurityToken;
import org.apache.shindig.auth.BlobCrypterSecurityTokenCodec;
import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.auth.SecurityTokenCodec;
import org.apache.shindig.auth.SecurityTokenException;
import org.apache.shindig.common.crypto.BlobCrypter;
import org.apache.shindig.common.crypto.BlobCrypterException;
import org.apache.shindig.config.ContainerConfig;

import java.util.Map;

/**
 * @author <a href="nguyenanhkien2a@gmail.com">Kien Nguyen</a>
 * @version $Revision$
 */

public class GateInBlobCrypterSecurityTokenCodec extends BlobCrypterSecurityTokenCodec
{
   @Inject
   public GateInBlobCrypterSecurityTokenCodec(ContainerConfig config)
   {
      super(config);
   }

   /**
    * TODO We will remove these token's override classes When we upgrade to Shindig higher 2.5 
    * That it support update changes of BlobCrypterSecurityTokenCodec
    * if there are changes from ContainerConfig 
    * (see commit https://github.com/apache/shindig/commit/044d5194f90ba12dabc91ec997cb28502bc02feb)
    * @see org.apache.shindig.auth.BlobCrypterSecurityTokenCodec#createToken(java.util.Map)
    */
   @Override
   public SecurityToken createToken(Map<String, String> tokenParameters) throws SecurityTokenException
   {
      String token = tokenParameters.get(SecurityTokenCodec.SECURITY_TOKEN_NAME);
      if (StringUtils.isBlank(token))
      {
         // No token is present, assume anonymous access
         return new AnonymousSecurityToken();
      }
      String[] fields = StringUtils.split(token, ':');
      if (fields.length != 2)
      {
         throw new SecurityTokenException("Invalid security token " + token);
      }
      String container = fields[0];
      BlobCrypter crypter = crypters.get(container);
      if (crypter == null)
      {
         crypter = crypters.get(ContainerConfig.DEFAULT_CONTAINER);
      }
      String domain = domains.get(container);
      if (domain == null)
      {
         domain = domains.get(ContainerConfig.DEFAULT_CONTAINER);
      }
      String activeUrl = tokenParameters.get(SecurityTokenCodec.ACTIVE_URL_NAME);
      String crypted = fields[1];
      try
      {
         return GateInBlobCrypterSecurityToken.decrypt(crypter, container, domain, crypted, activeUrl);
      }
      catch (BlobCrypterException e)
      {
         throw new SecurityTokenException(e);
      }
   }
}
