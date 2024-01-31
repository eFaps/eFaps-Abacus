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
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.efaps.abacus.api.ICalcPosition;
import org.efaps.abacus.api.ITax;

public class CalcPosition
    implements ICalcPosition
{

    private int index;
    private String productOid;
    private BigDecimal quantity;
    private BigDecimal netUnitPrice;
    private BigDecimal netPrice;
    private List<ITax> taxes;
    private BigDecimal taxAmount;
    private BigDecimal crossUnitPrice;
    private BigDecimal crossPrice;

    public BigDecimal getCrossUnitPrice()
    {
        return crossUnitPrice;
    }

    public void setCrossUnitPrice(BigDecimal crossUnitPrice)
    {
        this.crossUnitPrice = crossUnitPrice;
    }

    @Override
    public BigDecimal getCrossPrice()
    {
        return crossPrice;
    }

    @Override
    public void setCrossPrice(BigDecimal crossPrice)
    {
        this.crossPrice = crossPrice;
    }

    @Override
    public int getIndex()
    {
        return index;
    }

    public CalcPosition setIndex(final int index)
    {
        this.index = index;
        return this;
    }

    @Override
    public String getProductOid()
    {
        return productOid;
    }

    public CalcPosition setProductOid(String productOid)
    {
        this.productOid = productOid;
        return this;
    }

    @Override
    public BigDecimal getNetUnitPrice()
    {
        return netUnitPrice;
    }

    public CalcPosition setNetUnitPrice(final BigDecimal netUnitPrice)
    {
        this.netUnitPrice = netUnitPrice;
        return this;
    }

    @Override
    public BigDecimal getQuantity()
    {
        return quantity;
    }

    public CalcPosition setQuantity(BigDecimal quantity)
    {
        this.quantity = quantity;
        return this;
    }

    @Override
    public BigDecimal getNetPrice()
    {
        return netPrice;
    }

    @Override
    public void setNetPrice(BigDecimal netPrice)
    {
        this.netPrice = netPrice;
    }

    @Override
    public List<ITax> getTaxes()
    {
        return taxes;
    }

    public CalcPosition setTaxes(List<ITax> taxes)
    {
        this.taxes = taxes;
        return this;
    }

    @Override
    public BigDecimal getTaxAmount()
    {
        return taxAmount;
    }

    @Override
    public void setTaxAmount(BigDecimal taxAmount)
    {
        this.taxAmount = taxAmount;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
