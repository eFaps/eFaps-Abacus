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
package org.efaps.abacus;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.efaps.abacus.api.ICalcDocument;
import org.efaps.abacus.api.ICalcPosition;
import org.efaps.abacus.api.IConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Calculator
{
    private static final Logger LOG = LoggerFactory.getLogger(Calculator.class);

    private final IConfig config;

    public Calculator(final IConfig config)
    {
        LOG.debug("Initializing calculator with config: {} ", config);
        this.config = config;
    }

    public void calc(final ICalcDocument document)
    {
        final var positions = document.getPositions().stream().sorted((pos0,
                                                                       pos1) -> pos0.getIndex() - pos1.getIndex())
                        .toList();
        LOG.debug("Calculating document: {} ", document);
        for (final var position : positions) {
            evalNetPrice(position);
        }
        evalNetTotal(document);
        LOG.debug("result document: {} ", document);
    }

    protected void evalNetPrice(final ICalcPosition position)
    {
        LOG.debug("  Evaluating NetPrice for Position: {} ", position.getIndex());
        position.setNetPrice(calcNetPrice(position.getQuantity(), position.getNetUnitPrice()));
        LOG.debug("    = {}", position.getNetPrice());
    }

    protected BigDecimal calcNetPrice(final BigDecimal quantity,
                                      final BigDecimal netUnitPrice)
    {
       LOG.debug("    {} * {}" , quantity, netUnitPrice);
       return roundNetPrice(quantity.multiply(netUnitPrice));
    }

    protected BigDecimal roundNetPrice(final BigDecimal netPrice)
    {
        LOG.debug("    set scale to {}", config.getNetPriceScale());
        return netPrice.setScale(config.getNetPriceScale(), RoundingMode.HALF_UP);
    }

    protected void evalNetTotal(final ICalcDocument document) {
        BigDecimal total = BigDecimal.ZERO;
        for (final var position : document.getPositions()) {
            total = total.add(position.getNetPrice());
        }
        document.setNetTotal(total);
    }
}
