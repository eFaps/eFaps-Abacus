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

import org.efaps.abacus.Calculator;
import org.efaps.abacus.api.ICalcPosition;
import org.testng.annotations.Test;

public class CalculatorTest
{

    @Test
    public void testDoc1()
    {
        final var positions = new ArrayList<ICalcPosition>();
        positions.add(new CalcPosition().setIndex(0).setQuantity(BigDecimal.ONE).setNetUnitPrice(new BigDecimal("12.44")));
        positions.add(new CalcPosition().setIndex(1).setQuantity(BigDecimal.ONE).setNetUnitPrice(new BigDecimal("9.14")));

        final var doc = new CalcDocument().setPositions(positions);

        final var calculator = new Calculator(new Configuration());

        calculator.calc(doc);
    }
}
