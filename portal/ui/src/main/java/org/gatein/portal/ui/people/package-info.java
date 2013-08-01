@Application @Portlet
@Bindings({
    @Binding(OrganizationService.class)
})
@Assets(
    scripts = {
      @Script(id = "jquery", src = "jquery-1.7.1.min.js"),
      @Script(src = "bootstrap-modal.js", depends = "jquery"),
      @Script(src = "people.js", depends = "juzu.ajax")
    },
    stylesheets = {
        @Stylesheet(src = "people.css")
    }
)
package org.gatein.portal.ui.people;

import juzu.Application;
import juzu.plugin.binding.Binding;
import juzu.plugin.binding.Bindings;
import juzu.plugin.portlet.Portlet;
import juzu.plugin.asset.Assets;
import juzu.plugin.asset.Script;
import juzu.plugin.asset.Stylesheet;
import org.exoplatform.services.organization.OrganizationService;