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

package org.beedraz.semantics_II.bean;


import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import org.beedraz.semantics_II.aggregate.AbstractAggregateBeed;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * Support for implementations of {@link BeanBeed}.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AbstractBeanBeed
    extends AbstractAggregateBeed
    implements BeanBeed {

  @Override
  protected String otherToStringInformation() {
    return ""; // MUDO list properties
  }

  @Override
  public void toString(StringBuffer sb, int level) {
    super.toString(sb, level);
    sb.append(indent(level + 1) + "hashCode: " + Integer.toHexString(hashCode()) + "\n");
  }

}
