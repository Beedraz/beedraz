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

package org.beedra_II.property.bool;


import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import java.text.NumberFormat;
import java.util.Set;

import org.beedra_II.AbstractBeed;
import org.beedra_II.topologicalupdate.UpdateSource;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * A beed representing the constant boolean value {@link false}.
 *
 * @invar   isEffective() == true;
 * @invar   get() == false;
 *
 * @author  Nele Smeets
 * @author  Peopleware n.v.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class BooleanFalseBeed
    extends AbstractBeed<BooleanEvent>
    implements BooleanBeed {

  public Boolean getBoolean() {
    return Boolean.FALSE;
  }

  public boolean getboolean() {
    return false;
  }

  public boolean isEffective() {
    return true;
  }

  public Boolean get() {
    return Boolean.FALSE;
  }

  public int getMaximumRootUpdateSourceDistance() {
    return 0;
  }

  public Set<? extends UpdateSource> getUpdateSources() {
    return null;
  }

  public Set<? extends UpdateSource> getUpdateSourcesTransitiveClosure() {
    return null;
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

