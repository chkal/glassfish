/*
 * Copyright (c) 2022 Contributors to the Eclipse Foundation
 * Copyright (c) 2012, 2020 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.nucleus.admin.progress;

import java.util.List;

import org.glassfish.nucleus.test.tool.DomainLifecycleExtension;
import org.glassfish.nucleus.test.tool.NucleusTestUtils;
import org.glassfish.nucleus.test.tool.NucleusTestUtils.NadminReturn;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.glassfish.nucleus.admin.progress.ProgressMessage.grepProgressMessages;
import static org.glassfish.nucleus.admin.progress.ProgressMessage.isNonDecreasing;
import static org.glassfish.nucleus.admin.progress.ProgressMessage.uniqueMessages;
import static org.glassfish.nucleus.test.tool.NucleusTestUtils.nadminWithOutput;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author martinmares
 */
@ExtendWith(DomainLifecycleExtension.class)
public class ProgressStatusComplexTest {

    @Test
    public void executeCommandFromCommand() {
        NadminReturn result = nadminWithOutput("progress-exec-other");

        assertTrue(result.returnValue);

        assertArrayEquals(new String[] {
            "Starting", "Preparing", "Parsing", "Working on main part",
            "Cleaning", "Finished", "Finishing outer command", "Finished outer command" },

            uniqueMessages(grepProgressMessages(result.out)));
    }

    @Test
    @Disabled
    public void executeCommandWithSupplements() {
        NadminReturn result = nadminWithOutput("progress-supplement");
        assertTrue(result.returnValue);

        List<ProgressMessage> prgs = grepProgressMessages(result.out);

        assertArrayEquals(new String[] {
            "Starting", "2 seconds supplemental command", "Parsing",
            "Working on main part", "Finished", "3 seconds supplemental" },

            uniqueMessages(prgs));

        assertTrue(prgs.size() > 10);
        assertFalse(prgs.get(4).isPercentage());
        assertTrue(prgs.get(10).isPercentage());
        assertTrue(isNonDecreasing(prgs));
    }

    // Test disabled till intermittent failures are fixed
    @Test
    @Disabled
    public void executeVeryComplexCommand() {
        NucleusTestUtils.NadminReturn result = nadminWithOutput("progress-complex");

        assertTrue(result.returnValue);

        List<ProgressMessage> prgs = ProgressMessage.grepProgressMessages(result.out);
        assertTrue(prgs.size() > 40);
        assertTrue(scopeCount(prgs, "complex:") >= 4);
        assertEquals(0, scopeCount(prgs, "complex.ch1:"));
        assertEquals(5, scopeCount(prgs, "complex.ch2-paral:"));
        assertEquals(4, scopeCount(prgs, "complex.ch3:"));
        assertEquals(5, scopeCount(prgs, "complex.ch1.ch11:"));
        assertEquals(6, scopeCount(prgs, "complex.ch1.ch12:"));
        assertEquals(25, scopeCount(prgs, "complex.ch2-paral.ch21:"));
        assertEquals(25, scopeCount(prgs, "complex.ch2-paral.ch22:"));
        assertEquals(25, scopeCount(prgs, "complex.ch2-paral.ch23:"));
        assertEquals(25, scopeCount(prgs, "complex.ch2-paral.ch24:"));
        assertEquals(5, scopeCount(prgs, "complex.ch3.ch31:"));
        assertEquals(5, scopeCount(prgs, "complex.ch3.ch32:"));
        assertTrue(ProgressMessage.isNonDecreasing(prgs));
    }

    private int scopeCount(List<ProgressMessage> prgs, String scope) {
        int result = 0;
        for (ProgressMessage prg : prgs) {
            if (scope.equals(prg.getScope())) {
                result++;
            }
        }
        return result;
    }

}
