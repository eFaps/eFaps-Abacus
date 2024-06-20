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
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.efaps.abacus.api.ICalcDocument;
import org.efaps.abacus.api.ICalcPosition;
import org.efaps.abacus.api.ITax;

public class CalcDocument
    implements ICalcDocument
{

    private BigDecimal netTotal;
    private List<ITax> taxes;

    private BigDecimal taxTotal;
    private BigDecimal crossTotal;

    private List<ICalcPosition> positions;

    @Override
    public List<ICalcPosition> getPositions()
    {
        return positions;
    }

    public CalcDocument setPositions(final List<ICalcPosition> positions)
    {
        this.positions = positions;
        return this;
    }

    public CalcDocument addPosition(final ICalcPosition position)
    {
        if (this.positions == null) {
            positions = new ArrayList<>();
        }
        positions.add(position);
        return this;
    }

    @Override
    public BigDecimal getNetTotal()
    {
        return netTotal;
    }

    @Override
    public void setNetTotal(final BigDecimal netTotal)
    {
        this.netTotal = netTotal;
    }

    @Override
    public BigDecimal getCrossTotal()
    {
        return crossTotal;
    }

    @Override
    public void setCrossTotal(final BigDecimal crossTotal)
    {
        this.crossTotal = crossTotal;
    }

    @Override
    public BigDecimal getTaxTotal()
    {
        return taxTotal;
    }

    @Override
    public void setTaxTotal(BigDecimal taxTotal)
    {
        this.taxTotal = taxTotal;
    }

    @Override
    public List<ITax> getTaxes()
    {
        return taxes;
    }

    @Override
    public void setTaxes(List<ITax> taxes)
    {
        this.taxes = taxes;
    }

    @Override
    public CalcDocument clone()
    {
        final var doc = new CalcDocument();
        doc.setNetTotal(getNetTotal());
        doc.setCrossTotal(getCrossTotal());
        doc.setTaxTotal(getTaxTotal());
        doc.setTaxes(getTaxes());
        doc.setPositions(getPositions().stream().map(ICalcPosition::clone).toList());
        return doc;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

    @Override
    public ICalcDocument updateWith(ICalcDocument document)
    {
        setNetTotal(document.getNetTotal());
        setCrossTotal(document.getCrossTotal());
        setTaxTotal(document.getTaxTotal());
        setTaxes(document.getTaxes());

        final var otherDocPosIter = document.getPositions().iterator();
        for (final ICalcPosition originalDocPos : getPositions()) {
            final var promoDocPos = otherDocPosIter.next();
            originalDocPos.updateWith(promoDocPos);
        }
        return this;
    }
}
