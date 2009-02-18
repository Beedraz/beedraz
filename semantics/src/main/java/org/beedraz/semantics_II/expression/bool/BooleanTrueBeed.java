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

package org.beedraz.semantics_II.expression.bool;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.util.smallfries_I.MultiLineToStringUtil.indent;

import java.text.NumberFormat;
import java.util.Collections;
import java.util.Set;

import org.beedraz.semantics_II.AbstractBeed;
import org.beedraz.semantics_II.Beed;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * A beed representing the constant boolean value {@code true}.
 *
 * @invar   isEffective() == true;
 * @invar   get() == true;
 *
 * @author  Nele Smeets
 * @author  Peopleware n.v.
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class BooleanTrueBeed
    extends AbstractBeed<BooleanEvent>
    implements BooleanBeed {

  public BooleanTrueBeed() {
    this(null);
  }

  /**
   * This constructor is added for reasons of consistency,
   * but, since this beed is constact, i.e., its value never
   * changes, it will never send events or start a topological
   * update, so there is no reason to register the beed
   * with the owner. This constructor does nothing with
   * the {@code owner}.
   */
  public BooleanTrueBeed(AggregateBeed owner) {
    // NOP
  }

  public Boolean getBoolean() {
    return Boolean.TRUE;
  }

  public boolean getboolean() {
    return true;
  }

  public boolean isEffective() {
    return true;
  }

  public Boolean get() {
    return Boolean.TRUE;
  }

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
    sb.append(getboolean());
  }

  @Override
  public final void toString(StringBuffer sb, int level) {
    super.toString(sb, level);
    sb.append(indent(level + 1) + "value:" + getboolean() + "\n");
  }

}

