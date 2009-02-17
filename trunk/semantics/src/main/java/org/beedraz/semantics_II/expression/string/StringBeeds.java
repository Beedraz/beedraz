/*<license>
Copyright 2007 - $Date: 2007-05-08 16:22:50 +0200 (di, 08 mei 2007) $ by the authors mentioned below.

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

package org.beedraz.semantics_II.expression.string;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.EditStateException;
import org.beedraz.semantics_II.IllegalEditException;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * <p>Convenience methods for working with {@link StringBeed string beeds}.</p>
 *
 * @author  Nele Smeets
 * @author  Peopleware n.v.
 */
@Copyright("2007 - $Date: 2007-05-08 16:22:50 +0200 (di, 08 mei 2007) $, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 853 $",
         date     = "$Date: 2007-05-08 16:22:50 +0200 (di, 08 mei 2007) $")
public class StringBeeds {


  /*<section name="editableStringBeed">*/
  //------------------------------------------------------------------

  public static EditableStringBeed editableStringBeed(String constant, AggregateBeed owner) throws IllegalEditException {
    try {
      EditableStringBeed editableStringBeed = new EditableStringBeed(owner);
      StringEdit edit = new StringEdit(editableStringBeed);
      edit.setGoal(constant);
      edit.perform();
      return editableStringBeed;
    }
    catch (EditStateException e) {
      assert false : "Shouldn't happen";
      return null;
    }
  }

  /*</section>*/
}

