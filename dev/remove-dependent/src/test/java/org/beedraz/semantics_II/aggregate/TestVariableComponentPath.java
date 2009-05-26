/*<license>
Copyright 2007 - $Date: 2007-07-19 20:32:54 +0200 (do, 19 jul 2007) $ by the authors mentioned below.

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

package org.beedraz.semantics_II.aggregate;


import static org.beedraz.semantics_II.path.Paths.path;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.EditStateException;
import org.beedraz.semantics_II.IllegalEditException;
import org.beedraz.semantics_II.StubListener;
import org.beedraz.semantics_II.expression.bool.BooleanEdit;
import org.beedraz.semantics_II.expression.bool.EditableBooleanBeed;
import org.beedraz.semantics_II.expression.enumeration.ConditionalPath;
import org.beedraz.semantics_II.expression.string.EditableStringBeed;
import org.beedraz.semantics_II.expression.string.StringBeed;
import org.beedraz.semantics_II.path.MutablePath;
import org.beedraz.semantics_II.path.Path;
import org.beedraz.semantics_II.path.PathEvent;
import org.junit.Test;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


@Copyright("2007 - $Date: 2007-07-19 20:32:54 +0200 (do, 19 jul 2007) $, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 991 $",
         date     = "$Date: 2007-07-19 20:32:54 +0200 (do, 19 jul 2007) $")
public class TestVariableComponentPath {

  @Test
  public void test() throws EditStateException, IllegalEditException {
    // create a path to an aggregate beed
    SomeAggregateBeed aggregateBeed1 = new SomeAggregateBeed();
    changeCondition(aggregateBeed1, true);
    SomeAggregateBeed aggregateBeed2 = new SomeAggregateBeed();
    changeCondition(aggregateBeed2, true);
    MutablePath<SomeAggregateBeed> mutablePath =
      new MutablePath<SomeAggregateBeed>(null);
    // create a variable component path
    VariableComponentPath<SomeAggregateBeed, StringBeed> variableComponentPath =
      new VariableComponentPath<SomeAggregateBeed, StringBeed>(mutablePath) {
        @Override
        protected Path<StringBeed> selectComponentPathFromAggregate(SomeAggregateBeed aggregateBeed) {
          return aggregateBeed.conditionalPath;
        }
    };
    // add listeners to the path
    StubListener<PathEvent<StringBeed>> listener = new StubListener<PathEvent<StringBeed>>();
    variableComponentPath.addListener(listener);
    // check
    assertNull(variableComponentPath.get());
    // change the aggregate beed path
    mutablePath.set(aggregateBeed1);
    // check
    check(variableComponentPath, listener, null, aggregateBeed1.stringBeed1);
    // change the aggregate beed path
    mutablePath.set(aggregateBeed2);
    // check
    check(variableComponentPath, listener, aggregateBeed1.stringBeed1, aggregateBeed2.stringBeed1);
    // change the conditional path
    changeCondition(aggregateBeed2, false);
    // check
    check(variableComponentPath, listener, aggregateBeed2.stringBeed1, aggregateBeed2.stringBeed2);
    // change the conditional path
    changeCondition(aggregateBeed2, null);
    // check
    check(variableComponentPath, listener, aggregateBeed2.stringBeed2, null);
  }

  private void changeCondition(SomeAggregateBeed aggregateBeed, Boolean condition) throws EditStateException, IllegalEditException {
    try {
      BooleanEdit booleanEdit = new BooleanEdit(aggregateBeed.condition);
      booleanEdit.setGoal(condition);
      booleanEdit.perform();
    }
    catch (EditStateException e) {
      assert false: "Shouldn't happen";
    }
  }

  private void check(
      VariableComponentPath<SomeAggregateBeed, StringBeed> variableComponentPath,
      StubListener<PathEvent<StringBeed>> listener,
      StringBeed oldValue,
      StringBeed newValue) {
    assertEquals(newValue, variableComponentPath.get());
    assertNotNull(listener.$event);
    assertEquals(variableComponentPath, listener.$event.getSource());
    assertEquals(oldValue, listener.$event.getOldValue());
    assertEquals(newValue, listener.$event.getNewValue());
    assertNull(listener.$event.getEdit());
    listener.reset();
  }

}


class SomeAggregateBeed extends AbstractAggregateBeed {

  /*<property name="condition">*/
  //------------------------------------------------------------------

  public final EditableBooleanBeed condition = new EditableBooleanBeed(this);

  /*</property>*/

  /*<property name="stringBeeds">*/
  //------------------------------------------------------------------

  public final EditableStringBeed stringBeed1 = new EditableStringBeed(this);
  public final EditableStringBeed stringBeed2 = new EditableStringBeed(this);

  /*</property>*/


  /*<property name="calibrationFactor">*/
  //------------------------------------------------------------------

  public final ConditionalPath<StringBeed> conditionalPath =
    new ConditionalPath<StringBeed>();

  {
    conditionalPath.setConditionBeedPath(path(condition));
    conditionalPath.setFirstPath(path(stringBeed1));
    conditionalPath.setSecondPath(path(stringBeed2));
  }

  /*</property>*/

}


