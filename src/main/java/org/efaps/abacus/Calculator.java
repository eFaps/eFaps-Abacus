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
package org.efaps.abacus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.TreeMap;

import org.efaps.abacus.api.ICalcDocument;
import org.efaps.abacus.api.ICalcPosition;
import org.efaps.abacus.api.IConfig;
import org.efaps.abacus.api.ITax;
import org.efaps.abacus.api.TaxCalcFlow;
import org.efaps.abacus.pojo.Tax;
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

    public IConfig getConfig()
    {
        return config;
    }

    public void calc(final ICalcDocument document)
    {
        if (document.getPositions() == null) {
            LOG.warn("Document without positions: {} ", document);
        } else {

            final var positions = document.getPositions().stream().sorted((pos0,
                                                                           pos1) -> pos0.getIndex() - pos1.getIndex())
                            .toList();
            LOG.debug("Calculating document: {} ", document);
            for (final var position : positions) {
                LOG.debug("  Evaluating Position: {} ", position.getIndex());
                evalNetPrice(position);
                evalTaxes(position);
                evalCrossUnitPrice(position);
                evalCrossPrice(position);
            }
            evalNetTotal(document);
            evalTaxes(document);
            evalTaxTotal(document);
            evalCrossTotal(document);
            LOG.debug("result document: {} ", document);
        }
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
        document.setNetTotal(getNetTotal(document).setScale(2, RoundingMode.HALF_UP));
    }

    protected BigDecimal getNetTotal(final ICalcDocument document)
    {
        BigDecimal total = BigDecimal.ZERO;
        for (final var position : document.getPositions()) {
            if (!position.isFreeOfCharge()) {
                total = total.add(position.getNetPrice());
            }
        }
        return total;
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
                    if (config.getTaxCalcFlow().equals(TaxCalcFlow.RoundSum)) {
                        advalorem = roundTax(advalorem);
                    }
                    tax.setBase(position.getNetPrice());
                    tax.setAmount(advalorem);
                    amount = amount.add(advalorem);
                    LOG.debug("    = {}", advalorem);
                    break;
                case PERUNIT:
                    LOG.debug("    Tax PERUNIT {}", tax.getAmount());
                    tax.setBase(position.getQuantity());
                    tax.setAmount(tax.getAmount().multiply(position.getQuantity()));
                    amount = amount.add(tax.getAmount());
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected value: " + position.getTaxes());
            }
        }
        if (config.getTaxCalcFlow().equals(TaxCalcFlow.SumRound)) {
            amount = roundTax(amount);
        }
        position.setTaxAmount(amount);
    }

    protected void evalCrossTotal(final ICalcDocument document)
    {
        document.setCrossTotal(getCrossTotal(document).setScale(2, RoundingMode.HALF_UP));
    }

    protected BigDecimal getCrossTotal(final ICalcDocument document)
    {
        BigDecimal total = BigDecimal.ZERO;
        switch (config.getCrossTotalFlow()) {
            case SumCrossPrice:
                for (final var position : document.getPositions()) {
                    if (!position.isFreeOfCharge()) {
                        total = total.add(position.getCrossPrice());
                    }
                }
                break;
            case NetTotalPlusTax:
                total = document.getNetTotal().add(document.getTaxTotal());
                break;
        }
        return total;
    }

    protected BigDecimal roundTax(final BigDecimal taxAmount)
    {
        LOG.debug("    set scale to {}", config.getTaxScale());
        return taxAmount.setScale(config.getTaxScale(), RoundingMode.HALF_UP);
    }

    protected void evalCrossUnitPrice(final ICalcPosition position)
    {
        BigDecimal amount = BigDecimal.ZERO;
        for (final var tax : position.getTaxes()) {
            switch (tax.getType()) {
                case ADVALOREM:
                    var advalorem = position.getNetUnitPrice().multiply(tax.getPercentage()
                                    .divide(new BigDecimal(100).setScale(8, RoundingMode.HALF_UP)));
                    if (config.getTaxCalcFlow().equals(TaxCalcFlow.RoundSum)) {
                        advalorem = roundTax(advalorem);
                    }
                    amount = amount.add(advalorem);
                    break;
                case PERUNIT:
                    amount = amount.add(tax.getAmount());
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected value: " + position.getTaxes());
            }
        }
        if (config.getTaxCalcFlow().equals(TaxCalcFlow.SumRound)) {
            amount = roundTax(amount);
        }
        position.setCrossUnitPrice(position.getNetUnitPrice().add(amount));
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

    protected void evalTaxes(final ICalcDocument document)
    {
        document.setTaxes(getTaxes(document).stream().map(tax -> {
            tax.setAmount(tax.getAmount().setScale(2, RoundingMode.HALF_UP));
            tax.setBase(tax.getBase().setScale(2, RoundingMode.HALF_UP));
            return tax;
        }).toList());
    }

    protected void evalTaxTotal(final ICalcDocument document)
    {
        document.setTaxTotal(getTaxTotal(document).setScale(2, RoundingMode.HALF_UP));
    }

    protected BigDecimal getTaxTotal(final ICalcDocument document)
    {
        return getTaxes(document).stream()
                        .filter(tax -> !tax.isFreeOfCharge())
                        .map(ITax::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    protected List<ITax> getTaxes(final ICalcDocument document)
    {
        final var taxMap = new TreeMap<String, Tax>();
        for (final var position : document.getPositions()) {
            for (final var tax : position.getTaxes()) {
                Tax totalTax;
                if (taxMap.containsKey(tax.getKey())) {
                    totalTax = taxMap.get(tax.getKey());
                } else {
                    totalTax = new Tax()
                                    .setKey(tax.getKey())
                                    .setType(tax.getType())
                                    .setPercentage(tax.getPercentage())
                                    .setAmount(BigDecimal.ZERO)
                                    .setBase(BigDecimal.ZERO)
                                    .setFreeOfCharge(tax.isFreeOfCharge());
                    taxMap.put(tax.getKey(), totalTax);
                }
                totalTax.setAmount(totalTax.getAmount().add(tax.getAmount()));
                totalTax.setBase(totalTax.getBase().add(tax.getBase()));
            }
        }
        return taxMap.values().stream().map(tax -> (ITax) tax).toList();
    }
}
