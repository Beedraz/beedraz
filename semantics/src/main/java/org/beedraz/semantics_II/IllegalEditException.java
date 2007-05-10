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

package org.beedraz.semantics_II;


import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * Exception thrown by an {@link Edit} during {@link Edit#perform()},
 * {@link Edit#undo()} or {@link Edit#redo()} of an edit, when the
 * method cannot complete for semantic reasons (i.e., the edit would
 * make the beed invalid).
 *
 * @author  Jan Dockx
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class IllegalEditException extends EditException {
  // TODO extends SemanticException

  /**
   * @post ComparisonUtil.equalsWithNull(s, getMessage());
   * @post getCause() == null;
   * @post getEdit() == e;
   */
  public IllegalEditException(Edit<?> e, String s) {
    super(e, s);
  }

}
