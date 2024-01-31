/*
 * Copyright © 2003 - 2024 The eFaps Team (-)
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
 */
package org.efaps.abacus.pojo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.efaps.abacus.Calculator;
import org.efaps.abacus.api.CrossTotalFlow;
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

    @Test
    public void testDoc2()
    {
        final var positions = new ArrayList<ICalcPosition>();
        positions.add(new CalcPosition()
                        .setIndex(0)
                        .setQuantity(BigDecimal.ONE)
                        .setNetUnitPrice(new BigDecimal("8.4745"))
                        .setTaxes(Collections.singletonList(Tax.getAdvalorem("IGV", new BigDecimal(18)))));
        positions.add(new CalcPosition()
                        .setIndex(1)
                        .setQuantity(BigDecimal.ONE)
                        .setNetUnitPrice(new BigDecimal("8.4745"))
                        .setTaxes(Collections.singletonList(Tax.getAdvalorem("IGV", new BigDecimal(18)))));

        final var doc = new CalcDocument().setPositions(positions);

        final var calculator = new Calculator(new Configuration());

        calculator.calc(doc);

        final var taxes = (List) doc.getPositions().get(0).getTaxes();
        assertNumericEquals(((Tax) taxes.get(0)).getAmount(), new BigDecimal("1.53"));
        assertNumericEquals(((Tax) taxes.get(0)).getAmount(), new BigDecimal("1.53"));


        assertNumericEquals(doc.getPositions().get(0).getCrossPrice(), new BigDecimal("10.0045"));
        assertNumericEquals(doc.getPositions().get(1).getCrossPrice(), new BigDecimal("10.0045"));

        assertNumericEquals(doc.getNetTotal(), new BigDecimal("16.95"));
        assertNumericEquals(doc.getCrossTotal(), new BigDecimal("20.01"));
    }

    @Test
    public void testDoc3()
    {
        final var positions = new ArrayList<ICalcPosition>();
        positions.add(new CalcPosition()
                        .setIndex(0)
                        .setQuantity(BigDecimal.ONE)
                        .setNetUnitPrice(new BigDecimal("8.4745"))
                        .setTaxes(Collections.singletonList(Tax.getAdvalorem("IGV", new BigDecimal(18)))));
        positions.add(new CalcPosition()
                        .setIndex(1)
                        .setQuantity(BigDecimal.ONE)
                        .setNetUnitPrice(new BigDecimal("8.4745"))
                        .setTaxes(Collections.singletonList(Tax.getAdvalorem("IGV", new BigDecimal(18)))));

        final var doc = new CalcDocument().setPositions(positions);

        final var calculator = new Calculator(new Configuration().setTaxScale(4).setCrossPriceScale(2));

        calculator.calc(doc);

        final var taxes = (List) doc.getPositions().get(0).getTaxes();
        assertNumericEquals(((Tax) taxes.get(0)).getAmount(), new BigDecimal("1.5254"));
        assertNumericEquals(((Tax) taxes.get(0)).getAmount(), new BigDecimal("1.5254"));

        assertNumericEquals(doc.getPositions().get(0).getCrossPrice(), new BigDecimal("10"));
        assertNumericEquals(doc.getPositions().get(1).getCrossPrice(), new BigDecimal("10"));

        assertNumericEquals(doc.getNetTotal(), new BigDecimal("16.95"));
        assertNumericEquals(doc.getCrossTotal(), new BigDecimal("20.00"));
    }

    @Test
    public void testDoc4()
    {
        final var positions = new ArrayList<ICalcPosition>();
        positions.add(new CalcPosition()
                        .setIndex(0)
                        .setQuantity(BigDecimal.ONE)
                        .setNetUnitPrice(new BigDecimal("8.4745"))
                        .setTaxes(Collections.singletonList(Tax.getAdvalorem("IGV", new BigDecimal(18)))));
        positions.add(new CalcPosition()
                        .setIndex(1)
                        .setQuantity(BigDecimal.ONE)
                        .setNetUnitPrice(new BigDecimal("8.4745"))
                        .setTaxes(Collections.singletonList(Tax.getAdvalorem("IGV", new BigDecimal(18)))));

        final var doc = new CalcDocument().setPositions(positions);

        final var calculator = new Calculator(new Configuration()
                        .setNetPriceScale(2)
                        .setTaxScale(4)
                        .setCrossPriceScale(2)
                        .setCrossTotalFlow(CrossTotalFlow.NetTotalPlusTax));

        calculator.calc(doc);

        final var taxes = (List) doc.getPositions().get(0).getTaxes();
        assertNumericEquals(((Tax) taxes.get(0)).getAmount(), new BigDecimal("1.5246"));
        assertNumericEquals(((Tax) taxes.get(0)).getAmount(), new BigDecimal("1.5246"));

        assertNumericEquals(doc.getPositions().get(0).getNetPrice(), new BigDecimal("8.47"));
        assertNumericEquals(doc.getPositions().get(1).getNetPrice(), new BigDecimal("8.47"));

        assertNumericEquals(doc.getPositions().get(0).getCrossPrice(), new BigDecimal("9.99"));
        assertNumericEquals(doc.getPositions().get(1).getCrossPrice(), new BigDecimal("9.99"));

        assertNumericEquals(doc.getNetTotal(), new BigDecimal("16.94"));
        assertNumericEquals(doc.getTaxTotal(), new BigDecimal("3.05"));
        assertNumericEquals(doc.getCrossTotal(), new BigDecimal("19.99"));
    }
}
