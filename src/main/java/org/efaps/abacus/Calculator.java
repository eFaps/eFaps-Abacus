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
import org.efaps.abacus.api.TaxCalcFlow;
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
            LOG.debug("  Evaluating Position: {} ", position.getIndex());
            evalNetPrice(position);
            evalTaxes(position);
            evalCrossPrice(position);
        }
        evalNetTotal(document);
        evalCrossTotal(document);
        LOG.debug("result document: {} ", document);
    }

    protected void evalNetPrice(final ICalcPosition position)
    {
        position.setNetPrice(calcNetPrice(position.getQuantity(), position.getNetUnitPrice()));
        LOG.debug("    = {}", position.getNetPrice());
    }

    protected BigDecimal calcNetPrice(final BigDecimal quantity,
                                      final BigDecimal netUnitPrice)
    {
        LOG.debug("    NetPrice => {} * {}", quantity, netUnitPrice);
        return roundNetPrice(quantity.multiply(netUnitPrice));
    }

    protected BigDecimal roundNetPrice(final BigDecimal netPrice)
    {
        LOG.debug("    set scale to {}", config.getNetPriceScale());
        return netPrice.setScale(config.getNetPriceScale(), RoundingMode.HALF_UP);
    }

    protected void evalNetTotal(final ICalcDocument document)
    {
        BigDecimal total = BigDecimal.ZERO;
        for (final var position : document.getPositions()) {
            total = total.add(position.getNetPrice());
        }
        document.setNetTotal(total);
    }

    protected void evalTaxes(final ICalcPosition position)
    {
        BigDecimal amount = BigDecimal.ZERO;
        for (final var tax : position.getTaxes()) {
            switch (tax.getType()) {
                case ADVALOREM:
                    var advalorem = position.getNetPrice().multiply(tax.getPercentage()
                                    .divide(new BigDecimal(100).setScale(8, RoundingMode.HALF_UP)));
                    LOG.debug("    Tax ADVALOREM {}", tax.getPercentage());
                    if (config.getTaxCalcFlow().equals(TaxCalcFlow.ROUND_SUM)) {
                        advalorem = roundTax(advalorem);
                    }
                    tax.setBase(position.getNetPrice());
                    tax.setAmount(advalorem);
                    amount = amount.add(advalorem);
                    LOG.debug("    = {}", advalorem);
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected value: " + position.getTaxes());
            }
        }
        if (config.getTaxCalcFlow().equals(TaxCalcFlow.SUM_ROUND)) {
            amount = roundTax(amount);
        }
        position.setTaxAmount(amount);
    }

    protected void evalCrossTotal(final ICalcDocument document)
    {
        switch (config.getCrossTotalFlow()) {
            case SumCrossPrice:
                BigDecimal total = BigDecimal.ZERO;
                for (final var position : document.getPositions()) {
                    total = total.add(position.getCrossPrice());
                }
                document.setCrossTotal(total);
                break;
            case NetTotalPlusTax:
                break;
        }
    }

    protected BigDecimal roundTax(final BigDecimal taxAmount)
    {
        LOG.debug("    set scale to {}", config.getTaxScale());
        return taxAmount.setScale(config.getTaxScale(), RoundingMode.HALF_UP);
    }

    protected void evalCrossPrice(final ICalcPosition position)
    {
        position.setCrossPrice(calcCrossPrice(position.getNetPrice(), position.getTaxAmount()));
        LOG.debug("    = {}", position.getCrossPrice());
    }

    protected BigDecimal calcCrossPrice(final BigDecimal netPrice,
                                        final BigDecimal taxAmount)
    {
        LOG.debug("    CrossPrice => {} + {}", netPrice, taxAmount);
        return roundCrossPrice(netPrice.add(taxAmount));
    }

    protected BigDecimal roundCrossPrice(final BigDecimal crossPrice)
    {
        LOG.debug("    set scale to {}", config.getCrossPriceScale());
        return crossPrice.setScale(config.getCrossPriceScale(), RoundingMode.HALF_UP);
    }
}
