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
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.efaps.abacus.api.ICalcDocument;
import org.efaps.abacus.api.ICalcPosition;

public class CalcDocument
    implements ICalcDocument
{

    private BigDecimal netTotal;
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

    public void setCrossTotal(final BigDecimal crossTotal)
    {
        this.crossTotal = crossTotal;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
