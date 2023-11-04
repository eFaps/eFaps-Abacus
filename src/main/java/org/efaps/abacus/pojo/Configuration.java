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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.efaps.abacus.api.CrossTotalFlow;
import org.efaps.abacus.api.IConfig;
import org.efaps.abacus.api.TaxCalcFlow;

public class Configuration
    implements IConfig
{

    private int netPriceScale;

    private int taxScale;

    private TaxCalcFlow taxCalcFlow;

    private int crossPriceScale;

    private CrossTotalFlow crossTotalFlow;

    public Configuration()
    {
        this.netPriceScale = 4;
        this.taxScale = 2;
        this.taxCalcFlow = TaxCalcFlow.ROUND_SUM;
        this.crossPriceScale = 4;
        this.crossTotalFlow = CrossTotalFlow.SumCrossPrice;
    }

    @Override
    public int getNetPriceScale()
    {
        return netPriceScale;
    }

    public Configuration setNetPriceScale(int netPriceScale)
    {
        this.netPriceScale = netPriceScale;
        return this;
    }

    @Override
    public int getTaxScale()
    {
        return taxScale;
    }

    @Override
    public int getCrossPriceScale()
    {
        return crossPriceScale;
    }

    public Configuration setCrossPriceScale(int crossPriceScale)
    {
        this.crossPriceScale = crossPriceScale;
        return this;
    }

    @Override
    public TaxCalcFlow getTaxCalcFlow()
    {
        return taxCalcFlow;
    }

    public void setTaxCalcFlow(TaxCalcFlow taxCalcFlow)
    {
        this.taxCalcFlow = taxCalcFlow;
    }

    @Override
    public CrossTotalFlow getCrossTotalFlow()
    {
        return crossTotalFlow;
    }

    public Configuration setCrossTotalFlow(CrossTotalFlow crossTotalFlow)
    {
        this.crossTotalFlow = crossTotalFlow;
        return this;
    }

    public Configuration setTaxScale(int taxScale)
    {
        this.taxScale = taxScale;
        return this;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
