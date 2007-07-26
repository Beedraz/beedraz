/*<license>
Copyright 2007 - $Date: 2007-05-24 17:18:53 +0200 (do, 24 mei 2007) $ by the authors mentioned below.

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

package org.beedraz.semantics_II.expression.enumeration;


import static org.ppeew.annotations_I.License.Type.APACHE_V2;
import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import java.text.NumberFormat;
import java.util.Set;

import org.beedraz.semantics_II.AbstractBeed;
import org.beedraz.semantics_II.Beed;
import org.beedraz.semantics_II.expression.enumeration.EnumEvent;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * A beed containing a constant {@code _Enum_} value.
 *
 * @author  Nele Smeets
 * @author  Jan Dockx
 * @author  Peopleware n.v.
 */
@Copyright("2007 - $Date: 2007-05-24 17:18:53 +0200 (do, 24 mei 2007) $, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 916 $",
         date     = "$Date: 2007-05-24 17:18:53 +0200 (do, 24 mei 2007) $")
public class ConstantEnumBeed<_Enum_ extends Enum<_Enum_>>
    extends AbstractBeed<EnumEvent<_Enum_>>
    implements EnumBeed<_Enum_> {

  /**
   * @post  get() == value;
   */
  public ConstantEnumBeed(_Enum_ value) {
    $value = value;
  }

  /**
   * @basic
   */
  public final _Enum_ get() {
    return $value;
  }

  private _Enum_ $value;

  public final int getMaximumRootUpdateSourceDistance() {
    return 0;
  }

  public final Set<? extends Beed<?>> getUpdateSources() {
    return null;
  }

  public final Set<? extends Beed<?>> getUpdateSourcesTransitiveClosure() {
    return null;
  }

  public final void toStringDepth(StringBuffer sb, int depth, NumberFormat numberFormat) {
    sb.append(get());
  }

  @Override
  public final void toString(StringBuffer sb, int level) {
    super.toString(sb, level);
    sb.append(indent(level + 1) + "value:" + get() + "\n");
  }

}

