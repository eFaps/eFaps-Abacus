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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.efaps.abacus.api.ITax;
import org.efaps.abacus.api.TaxType;

public class Tax
    implements ITax
{

    private String key;
    private TaxType type;
    private BigDecimal percentage;
    private BigDecimal base;
    private BigDecimal amount;

    public BigDecimal getBase()
    {
        return base;
    }

    @Override
    public Tax setBase(BigDecimal base)
    {
        this.base = base;
        return this;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    @Override
    public Tax setAmount(BigDecimal amount)
    {
        this.amount = amount;
        return this;
    }

    @Override
    public String getKey()
    {
        return key;
    }

    public Tax setKey(String key)
    {
        this.key = key;
        return this;
    }

    @Override
    public BigDecimal getPercentage()
    {
        return percentage;
    }

    public Tax setPercentage(final BigDecimal percentage)
    {
        this.percentage = percentage;
        return this;
    }

    public Tax setType(final TaxType type)
    {
        this.type = type;
        return this;
    }

    @Override
    public TaxType getType()
    {
        return type;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

    public static Tax getAdvalorem(final String key,
                                   final BigDecimal percentage)
    {
        return new Tax().setType(TaxType.ADVALOREM).setKey(key).setPercentage(percentage);
    }

}
