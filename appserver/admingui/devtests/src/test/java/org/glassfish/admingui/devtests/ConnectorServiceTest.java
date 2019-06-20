/*
 * Copyright (c) 2010, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package org.glassfish.admingui.devtests;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: jasonlee
 * Date: Mar 12, 2010
 * Time: 2:38:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConnectorServiceTest extends BaseSeleniumTestClass {
    private static final String TRIGGER_CONNECTOR_SERVICE = "The policy to be used for loading classes.";

    @Test
    public void testConnectorService() {
        clickAndWait("treeForm:tree:configurations:server-config:connectorService:connectorService_link", TRIGGER_CONNECTOR_SERVICE);

        String policy = "derived";
        if (getFieldValue("propertyForm:propertySheet:propertSectionTextField:ClassLoadingPolicy:ClassLoadingPolicy").equals(policy)) {
            policy = "global";
        }
        final String timeout = Integer.toString(generateRandomNumber(120));

        setFieldValue("propertyForm:propertySheet:propertSectionTextField:timeout:tiimeout", timeout);
        selectDropdownOption("propertyForm:propertySheet:propertSectionTextField:ClassLoadingPolicy:ClassLoadingPolicy", policy);
        clickAndWait("propertyForm:propertyContentPage:topButtons:saveButton", TRIGGER_NEW_VALUES_SAVED);
        reset();
        clickAndWait("treeForm:tree:configurations:server-config:connectorService:connectorService_link", TRIGGER_CONNECTOR_SERVICE);
        assertEquals(timeout, getFieldValue("propertyForm:propertySheet:propertSectionTextField:timeout:tiimeout"));
        assertEquals(policy, getFieldValue("propertyForm:propertySheet:propertSectionTextField:ClassLoadingPolicy:ClassLoadingPolicy"));

    }
}