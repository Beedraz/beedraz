/*<license>
Copyright 2007 - $Date$ by the authors mentioned below.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</license>*/

package org.beedraz.semantics_II.expression.number.real.double64;


import static org.ppeew.annotations_I.License.Type.APACHE_V2;
import static org.ppeew.smallfries_I.MathUtil.castToBigDecimal;
import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Set;

import org.beedraz.semantics_II.AbstractBeed;
import org.beedraz.semantics_II.Beed;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * A beed containing a constant {@link Double} value.
 *
 * @author  Nele Smeets
 * @author  Peopleware n.v.
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class DoubleConstantBeed
    extends AbstractBeed<ActualDoubleEvent>
    implements DoubleBeed {

  /**
   * @post  constant == null
   *          ? !isEffective()
   *          : isEffective() &&
   *            getdouble() == constant;
   */
  public DoubleConstantBeed(Double constant) {
    this(constant, null);
  }

  /**
   * This constructor is added for reasons of consistency,
   * but, since this beed is constant, i.e., its value never
   * changes, it will never send events or start a topological
   * update, so there is no reason to register the beed
   * with the owner. This constructor does nothing with
   * the {@code owner}.
   *
   * @post  constant == null
   *          ? !isEffective()
   *          : isEffective() &&
   *            getdouble() == constant;
   */
  public DoubleConstantBeed(Double constant, AggregateBeed owner) {
    if (constant == null) {
      $isEffective = false;
    }
    else {
      $isEffective = true;
      $constant = constant;
    }
  }

  /**
   * @return get();
   */
  public final Double getDouble() {
    if (isEffective()) {
      return getdouble();
    }
    else {
      return null;
    }
  }

  public final BigDecimal getBigDecimal() {
    return castToBigDecimal(getDouble());
  }

  /**
   * @basic
   */
  public double getdouble() {
    return $constant;
  }

  private double $constant;

  /**
   * @basic
   */
  public boolean isEffective() {
    return $isEffective;
  }

  private boolean $isEffective;

  public int getMaximumRootUpdateSourceDistance() {
    return 0;
  }

  public Set<? extends Beed<?>> getUpdateSources() {
    return Collections.emptySet();
  }

  public Set<? extends Beed<?>> getUpdateSourcesTransitiveClosure() {
    return Collections.emptySet();
  }

  public final void toStringDepth(StringBuffer sb, int depth, NumberFormat numberFormat) {
    sb.append(numberFormat.format(getdouble()));
  }

  @Override
  public final void toString(StringBuffer sb, int level) {
    super.toString(sb, level);
    sb.append(indent(level + 1) + "value:" + getDouble() + "\n");
  }

  @Override
  protected final String otherToStringInformation() {
    return getDouble() == null ? "null" : getDouble().toString();
  }

}

