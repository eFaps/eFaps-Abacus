/*
 * Copyright 2003 - 2023 The eFaps Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.efaps.abacus.pojo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.efaps.abacus.Calculator;
import org.efaps.abacus.api.ICalcPosition;
import org.testng.annotations.Test;

public class CalculatorTest
    extends AbstractTest
{

    @Test
    public void testDoc1()
    {
        final var positions = new ArrayList<ICalcPosition>();
        positions.add(new CalcPosition()
                        .setIndex(0)
                        .setQuantity(BigDecimal.ONE)
                        .setNetUnitPrice(new BigDecimal("12.44"))
                        .setTaxes(Collections.singletonList(Tax.getAdvalorem("IGV", new BigDecimal(18)))));
        positions.add(new CalcPosition()
                        .setIndex(1)
                        .setQuantity(BigDecimal.ONE)
                        .setNetUnitPrice(new BigDecimal("9.14"))
                        .setTaxes(Collections.singletonList(Tax.getAdvalorem("IGV", new BigDecimal(18)))));

        final var doc = new CalcDocument().setPositions(positions);

        final var calculator = new Calculator(new Configuration());

        calculator.calc(doc);
        assertNumericEquals(doc.getPositions().get(0).getNetPrice(), new BigDecimal("12.44"));
        assertNumericEquals(doc.getPositions().get(1).getNetPrice(), new BigDecimal("9.14"));

        final var taxes = (List) doc.getPositions().get(0).getTaxes();
        assertNumericEquals(((Tax) taxes.get(0)).getAmount(), new BigDecimal("2.24"));
        assertNumericEquals(doc.getPositions().get(0).getNetPrice(), ((Tax) taxes.get(0)).getBase());

        assertNumericEquals(doc.getPositions().get(0).getCrossPrice(), new BigDecimal("14.68"));
        assertNumericEquals(doc.getPositions().get(1).getCrossPrice(), new BigDecimal("10.79"));

        assertNumericEquals(doc.getNetTotal(), new BigDecimal("21.58"));
        assertNumericEquals(doc.getCrossTotal(), new BigDecimal("25.47"));
    }
}
