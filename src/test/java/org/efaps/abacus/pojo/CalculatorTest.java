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
import org.efaps.abacus.api.TaxType;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CalculatorTest
{

    @Test
    public void testDoc1()
    {
        final var positions = new ArrayList<ICalcPosition>();
        positions.add(new CalcPosition()
                        .setIndex(0)
                        .setQuantity(BigDecimal.ONE)
                        .setNetUnitPrice(new BigDecimal("12.44"))
                        .setTaxes(Collections.singletonList(new Tax()
                                        .setType(TaxType.ADVALOREM)
                                        .setPercentage(new BigDecimal(19)))));
        positions.add(new CalcPosition()
                        .setIndex(1)
                        .setQuantity(BigDecimal.ONE)
                        .setNetUnitPrice(new BigDecimal("9.14"))
                        .setTaxes(Collections.singletonList(new Tax()
                                        .setType(TaxType.ADVALOREM)
                                        .setPercentage(new BigDecimal(19)))));

        final var doc = new CalcDocument().setPositions(positions);

        final var calculator = new Calculator(new Configuration());

        calculator.calc(doc);
        Assert.assertTrue(new BigDecimal("12.44").compareTo(doc.getPositions().get(0).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal("9.14").compareTo(doc.getPositions().get(1).getNetPrice()) == 0);

        final var taxes = (List) doc.getPositions().get(0).getTaxes();
        Assert.assertTrue(new BigDecimal("2.36").compareTo(((Tax) taxes.get(0)).getAmount()) == 0);
        Assert.assertTrue(doc.getPositions().get(0).getNetPrice().compareTo(((Tax) taxes.get(0)).getBase()) == 0);

        Assert.assertTrue(new BigDecimal("14.8").compareTo(doc.getPositions().get(0).getCrossPrice()) == 0);
        Assert.assertTrue(new BigDecimal("10.88").compareTo(doc.getPositions().get(1).getCrossPrice()) == 0);
    }
}
