/*
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
package org.gatein.portal.ui.people;

import static org.junit.Assert.assertEquals;

import java.net.URL;
import java.util.List;

import junit.framework.AssertionFailedError;
import juzu.arquillian.Helper;

import org.gatein.portal.common.kernel.KernelLifeCycle;
import org.gatein.portal.ui.register.Controller;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Node;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.portletapp20.PortletDescriptor;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 */
@RunWith(Arquillian.class)
public class PeopleTestCase {

    @Deployment
    public static WebArchive getDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class, "portal.war");
        Helper.createBasePortletDeployment(war, "guice", Controller.class);
        PortletDescriptor descriptor = Descriptors.
                create(PortletDescriptor.class).
                createPortlet().
                portletName("PeoplePortlet").
                portletClass(PeoplePortlet.class.getName()).
                up();
        war.addAsWebInfResource(new StringAsset(descriptor.exportAsString()), "portlet.xml");
        Node node = war.get("WEB-INF/web.xml");
        WebAppDescriptor webApp = Descriptors.importAs(WebAppDescriptor.class).fromStream(node.getAsset().openStream());
        webApp.displayName("portal").createFilter().filterName("KernelLifeCycle").filterClass(KernelLifeCycle.class.getName()).up().
                createFilterMapping().filterName("KernelLifeCycle").servletName("EmbedServlet").up();
        war.delete(node.getPath());
        war.setWebXML(new StringAsset(webApp.exportAsString()));
        war.addAsWebInfResource("org/gatein/portal/ui/people/configuration.xml", "conf/configuration.xml");
        return war;
    }

    @Drone
    WebDriver driver;

    @ArquillianResource
    URL deploymentURL;
    
    private String getBaseURL() {
        try {
            return deploymentURL.toURI().resolve("./embed/PeoplePortlet").toURL().toString();
        } catch (Exception e) {
            AssertionFailedError afe = new AssertionFailedError();
            afe.initCause(e);
            throw afe;
        }
    }
    
    @Test
    @InSequence(1)
    @RunAsClient
    public void test() throws Exception {
        driver.get(getBaseURL());
        WebElement list = driver.findElement(By.id("users"));
        List<WebElement> users = list.findElements(By.tagName("li"));
        assertEquals(5, users.size());
        assertEquals("Demo gtn", users.get(0).getText());
        assertEquals("John Anthony", users.get(1).getText());
        assertEquals("Mary Kelly", users.get(2).getText());
        assertEquals("whatever whatever", users.get(3).getText());
        assertEquals("Root Root", users.get(4).getText());
    }
    
    @Test
    @InSequence(2)
    @RunAsClient
    public void testUser() throws Exception {
        driver.get(getBaseURL());
        
        WebElement userInput = driver.findElement(By.name("userName"));
        userInput.sendKeys("root");
        
        WebElement list = driver.findElement(By.id("users"));
        List<WebElement> users = list.findElements(By.tagName("li"));
        assertEquals(1, users.size());
        assertEquals("Root Root", users.get(0).getText());
    }
}
