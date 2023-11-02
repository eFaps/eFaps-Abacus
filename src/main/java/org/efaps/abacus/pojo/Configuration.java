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
import org.efaps.abacus.api.IConfig;

public class Configuration
    implements IConfig
{

    private int netPriceScale;

    private int positionTaxScale;

    private int crossPriceScale;

    public Configuration()
    {
        this.netPriceScale = 4;
        this.positionTaxScale = 2;
        this.netPriceScale = 4;
    }

    @Override
    public int getNetPriceScale()
    {
        return netPriceScale;
    }

    public void setNetPriceScale(int netPriceScale)
    {
        this.netPriceScale = netPriceScale;
    }

    @Override
    public int getPositionTaxScale()
    {
        return positionTaxScale;
    }

    public void setPositionTaxScale(int positionTaxScale)
    {
        this.positionTaxScale = positionTaxScale;
    }

    @Override
    public int getCrossPriceScale()
    {
        return crossPriceScale;
    }

    public void setCrossPriceScale(int crossPriceScale)
    {
        this.crossPriceScale = crossPriceScale;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
