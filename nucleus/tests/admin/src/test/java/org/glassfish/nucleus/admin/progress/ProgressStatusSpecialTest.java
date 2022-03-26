/*
 * Copyright (c) 2022 Contributors to the Eclipse Foundation
 * Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.
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

import java.util.Iterator;
import java.util.List;

import org.glassfish.nucleus.test.tool.DomainLifecycleExtension;
import org.glassfish.nucleus.test.tool.NucleusTestUtils.NadminReturn;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.glassfish.nucleus.test.tool.NucleusTestUtils.nadminWithOutput;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author martinmares
 */
@ExtendWith(DomainLifecycleExtension.class)
public class ProgressStatusSpecialTest {

    @Test
    public void stepBackCommand() {
        NadminReturn result = nadminWithOutput("progress-step-back");
        assertTrue(result.returnValue);
        List<ProgressMessage> prgs = ProgressMessage.grepProgressMessages(result.out);
        assertFalse(ProgressMessage.isNonDecreasing(prgs));
        Iterator<ProgressMessage> itr = prgs.iterator();
        while (itr.hasNext()) {
            ProgressMessage prg = itr.next();
            if (prg.getValue() >= 80) {
                break;
            }
        }
        assertTrue(itr.hasNext()); //Exist more record
        while (itr.hasNext()) {
            ProgressMessage prg = itr.next();
            assertTrue(prg.getValue() <= 80);
            if (prg.getValue() < 80) {
                break;
            }
        }
        assertTrue(itr.hasNext()); //Exist more record
        ProgressMessage prg = itr.next();
        assertTrue(prg.getValue() < 80);
    }

    @Test
    public void doubleTotalCommand() {
        NadminReturn result = nadminWithOutput("progress-double-totals");
        assertTrue(result.returnValue);
        List<ProgressMessage> prgs = ProgressMessage.grepProgressMessages(result.out);
        assertFalse(ProgressMessage.isNonDecreasing(prgs));
    }

}
