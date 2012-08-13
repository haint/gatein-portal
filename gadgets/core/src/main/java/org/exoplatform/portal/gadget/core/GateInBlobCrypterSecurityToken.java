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

import org.apache.shindig.auth.BlobCrypterSecurityToken;
import org.apache.shindig.common.crypto.BlobCrypter;
import org.apache.shindig.common.crypto.BlobCrypterException;

import java.util.Map;

/**
 * @author <a href="nguyenanhkien2a@gmail.com">Kien Nguyen</a>
 * @version $Revision$
 */

public class GateInBlobCrypterSecurityToken extends BlobCrypterSecurityToken
{
   public GateInBlobCrypterSecurityToken(BlobCrypter crypter, String container, String domain)
   {
      super(crypter, container, domain);
   }

   public static GateInBlobCrypterSecurityToken decrypt(BlobCrypter crypter, String container, String domain, String token,
      String activeUrl) throws BlobCrypterException
   {
      Map<String, String> values = crypter.unwrap(token, MAX_TOKEN_LIFETIME_SECS);
      GateInBlobCrypterSecurityToken t = new GateInBlobCrypterSecurityToken(crypter, container, domain);
      setTokenValues(t, values);
      t.setActiveUrl(activeUrl);
      return t;
   }
}
