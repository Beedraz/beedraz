/*<license>
 Copyright 2007 - $Date: 2007-05-24 16:46:42 +0200 (do, 24 mei 2007) $ by the authors mentioned below.

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

package org.beedraz.semantics_II.expression.collection.set;


import static org.beedraz.semantics_II.expression.bool.BooleanBeeds.cor;
import static org.beedraz.semantics_II.path.Paths.path;
import static org.junit.Assert.assertTrue;
import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.EditStateException;
import org.beedraz.semantics_II.IllegalEditException;
import org.beedraz.semantics_II.bean.AbstractBeanBeed;
import org.beedraz.semantics_II.bean.BeanBeed;
import org.beedraz.semantics_II.expression.bool.BooleanBeed;
import org.beedraz.semantics_II.expression.bool.BooleanEdit;
import org.beedraz.semantics_II.expression.bool.EditableBooleanBeed;
import org.beedraz.semantics_II.path.Path;
import org.beedraz.semantics_II.path.PathFactory;
import org.junit.Test;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


@Copyright("2007 - $Date: 2007-05-24 16:46:42 +0200 (do, 24 mei 2007) $, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 913 $",
         date     = "$Date: 2007-05-24 16:46:42 +0200 (do, 24 mei 2007) $")
public class TestUpdateAlgorithm {


  public static final String CRITERION2 = "filtered set beed 2 criterion";

  @Test
  public void test() throws EditStateException, IllegalEditException {
    // some bean beed, necessary for creating a set beed
    BeanBeed owner = new AbstractBeanBeed(){};
    // source, containing 1 element
    EditableSetBeed<Person> source = new EditableSetBeed<Person>(owner);
    Person person = new Person();
    SetEdit<Person> setEdit = new SetEdit<Person>(source);
    setEdit.addElementToAdd(person);
    setEdit.perform();
    // first filtered set beed
    FilteredSetBeed<Person> filteredSetBeed1 =
      new FilteredSetBeed<Person>(
        new PathFactory<Person, BooleanBeed>() {
          public Path<? extends BooleanBeed> createPath(Person person) {
            return path(person.pretty);
          }
        });
    filteredSetBeed1.setSourcePath(path(source));
    // second filtered set beed, having the first one as source
    FilteredSetBeed<Person> filteredSetBeed2 =
      new FilteredSetBeed<Person>(
        new PathFactory<Person, BooleanBeed>() {
          public Path<? extends BooleanBeed> createPath(Person person) {
            return path(person.bigComputation);
          }
          @Override
          public String toString() {
            return CRITERION2;
          }

        });
    filteredSetBeed2.setSourcePath(path(filteredSetBeed1));
    System.out.println("begin: ");
    System.out.println("The first filtered set beed contains " +
        filteredSetBeed1.get().size() + " elements.");
    System.out.println("The second filtered set beed contains " +
        filteredSetBeed1.get().size() + " elements.");
    System.out.println("mrusd of the second: " + filteredSetBeed2.getMaximumRootUpdateSourceDistance());
    // change pretty from false to true
    /*
     * By changing this value from false to true, the first filtered set beed
     * will be updated, and as a result, it will contain one element.
     * Then, the second filtered set beed gets a SetEvent saying that an extra
     * element is added to its source. As a consequence, an extra 
     * ElementCriterion is created and added to the second filterd set beed. 
     * The mrusd of the filtered set beed is 3, the mrusd of the criterion is
     * 16, so the mrusd of the filtered set beed changes to 17.
     * Later in the update algorithm, since its mrusd is 16, the newly created 
     * ElementCriterion is updated, and its value changes from false to true.  
     * Then, the second filtered set beed gets a SetEvent and a BooleanEvent.
     * At this moment, an assertion error is thrown (see sourceElementAdded), 
     * since the element to be added is already in the map of element criteria.
     */
    System.out.println();
    System.out.println("changing a property of an element in the source...");
    BooleanEdit booleanEdit = new BooleanEdit(person.pretty);
    booleanEdit.setGoal(true);
    try {
      booleanEdit.perform();
      System.out.println("perform is ok");
      assertTrue(false);
    }
    catch(AssertionError e) {
      assertTrue(true);
      System.out.println("An assertion error has occurred during the perform.");
//      e.printStackTrace();
    }
  }


}

class Person extends AbstractBeanBeed {


  /*<property name="pretty">*/
  //-------------------------------------------------------

  /**
   * A boolean value expressing whether the person is pretty or not.
   */
  public final EditableBooleanBeed pretty = new EditableBooleanBeed(this);

  /*</property>*/


  /*<property name="bigComputation">*/
  //------------------------------------------------------------------

  public final BooleanBeed bigComputation =
      cor(pretty,
        cor(pretty,
          cor(pretty,
            cor(pretty,
              cor(pretty,
                cor(pretty,
                  cor(pretty,
                    cor(pretty,
                      cor(pretty,
                        cor(pretty,
                          cor(pretty,
                            cor(pretty,
                              cor(pretty,
                                cor(pretty,
                                  cor(pretty, pretty)))))))))))))));

  {
    registerAggregateElement(bigComputation);
  }

  /*</property>*/

}
