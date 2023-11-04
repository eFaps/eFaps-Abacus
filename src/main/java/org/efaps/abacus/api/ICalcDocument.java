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
package org.efaps.abacus.api;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

public interface ICalcDocument
{

    Collection<ICalcPosition> getPositions();

    BigDecimal getNetTotal();

    void setNetTotal(BigDecimal netTotal);

    BigDecimal getTaxTotal();

    void setTaxTotal(BigDecimal taxTotal);

    void setTaxes(List<ITax> taxes);

    BigDecimal getCrossTotal();

    void setCrossTotal(BigDecimal crossTotal);

}
