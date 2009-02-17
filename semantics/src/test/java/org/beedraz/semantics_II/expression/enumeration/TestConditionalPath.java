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

package org.beedraz.semantics_II.expression.enumeration;


import static org.beedraz.semantics_II.expression.bool.BooleanBeeds.editableBooleanBeed;
import static org.beedraz.semantics_II.expression.bool.BooleanBeeds.nullBooleanBeed;
import static org.beedraz.semantics_II.expression.string.StringBeeds.editableStringBeed;
import static org.beedraz.semantics_II.path.Paths.path;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.EditStateException;
import org.beedraz.semantics_II.IllegalEditException;
import org.beedraz.semantics_II.StubListener;
import org.beedraz.semantics_II.bean.StubBeanBeed;
import org.beedraz.semantics_II.expression.bool.BooleanBeed;
import org.beedraz.semantics_II.expression.bool.BooleanEdit;
import org.beedraz.semantics_II.expression.bool.EditableBooleanBeed;
import org.beedraz.semantics_II.expression.string.StringBeed;
import org.beedraz.semantics_II.path.MutablePath;
import org.beedraz.semantics_II.path.NullPath;
import org.beedraz.semantics_II.path.Path;
import org.beedraz.semantics_II.path.PathEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


@Copyright("2007 - $Date: 2007-05-24 16:46:42 +0200 (do, 24 mei 2007) $, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 913 $",
         date     = "$Date: 2007-05-24 16:46:42 +0200 (do, 24 mei 2007) $")
public class TestConditionalPath {

  @Before
  public void setUp() throws Exception {
    $owner = new StubBeanBeed();
    $conditionalPath = new ConditionalPath<StringBeed>();
    $firstStringBeed = editableStringBeed("true", $owner);
    $firstStringBeedPath = path($firstStringBeed);
    $secondStringBeed = editableStringBeed("false", $owner);
    $secondStringBeedPath = path($secondStringBeed);
    $false = editableBooleanBeed(false, $owner);
    $falsePath = path($false);
    $true = editableBooleanBeed(true, $owner);
    $truePath = path($true);
    $null = nullBooleanBeed($owner);
    $nullPath = path($null);
    $emptyPath = new NullPath<BooleanBeed>();
    $listener1 = new StubListener<PathEvent<StringBeed>>();
  }

  @After
  public void tearDown() throws Exception {
    $owner = null;
    $conditionalPath = null;
    $secondStringBeed = null;
    $secondStringBeedPath = null;
    $firstStringBeed = null;
    $firstStringBeedPath = null;
    $truePath = null;
    $true = null;
    $falsePath = null;
    $false = null;
    $nullPath = null;
    $null = null;
    $emptyPath = null;
    $listener1 = null;
  }

  private StubBeanBeed $owner;
  private ConditionalPath<StringBeed> $conditionalPath;
  private StringBeed $firstStringBeed;
  private Path<StringBeed> $firstStringBeedPath;
  private StringBeed $secondStringBeed;
  private Path<StringBeed> $secondStringBeedPath;
  private Path<BooleanBeed> $truePath;
  private BooleanBeed $true;
  private Path<BooleanBeed> $falsePath;
  private BooleanBeed $false;
  private Path<BooleanBeed> $nullPath;
  private BooleanBeed $null;
  private Path<BooleanBeed> $emptyPath;
  private StubListener<PathEvent<StringBeed>> $listener1;

  @Test
  public void constructor() {
    checkConditionalPath(null, null, null, null, null, null, null);
  }

  private void checkConditionalPath(
      Path<? extends BooleanBeed> conditionBeedPath,
      BooleanBeed conditionBeed,
      Boolean condition,
      Path<StringBeed> firstPath,
      Path<StringBeed> secondPath,
      Path<? extends StringBeed> selectedPath,
      StringBeed result) {
    assertEquals(conditionBeedPath, $conditionalPath.getConditionBeedPath());
    assertEquals(conditionBeed, $conditionalPath.getConditionBeed());
    assertEquals(condition, $conditionalPath.getCondition());
    assertEquals(firstPath, $conditionalPath.getFirstPath());
    assertEquals(secondPath, $conditionalPath.getSecondPath());
    assertEquals(selectedPath, $conditionalPath.getSelectedPath());
    assertEquals(result, $conditionalPath.get());
  }


  /**
   * There are no cases.
   */
  @Test
  public void setConditionBeedPath1() {
    // set a non-effective condition path
    $conditionalPath.setConditionBeedPath(null);
    checkConditionalPath(null, null, null, null, null, null, null);
    // set an effective condition path, whose value is not effective
    $conditionalPath.setConditionBeedPath($emptyPath);
    checkConditionalPath($emptyPath, null, null, null, null, null, null);
    // set an effective condition path, whose value is an effective beed with value null
    $conditionalPath.setConditionBeedPath($nullPath);
    checkConditionalPath($nullPath, $null, null, null, null, null, null);
    // set an effective condition path, whose value is an effective beed with an effective value
    $conditionalPath.setConditionBeedPath($truePath);
    checkConditionalPath($truePath, $true, true, null, null, null, null);
  }

  /**
   * There are cases.
   */
  @Test
  public void setConditionBeedPath2() {
    $conditionalPath.setSecondPath($secondStringBeedPath);
    $conditionalPath.setFirstPath($firstStringBeedPath);
    // set a non-effective condition path
    $conditionalPath.setConditionBeedPath(null);
    checkConditionalPath(null, null, null, $firstStringBeedPath, $secondStringBeedPath, null, null);
    // set an effective condition path, whose value is not effective
    $conditionalPath.setConditionBeedPath($emptyPath);
    checkConditionalPath($emptyPath, null, null, $firstStringBeedPath, $secondStringBeedPath, null, null);
    // set an effective condition path, whose value is an effective beed with value null
    $conditionalPath.setConditionBeedPath($nullPath);
    checkConditionalPath($nullPath, $null, null, $firstStringBeedPath, $secondStringBeedPath, null, null);
    // set an effective condition path, whose value is an effective beed with an effective value,
    // that is in the cases map
    $conditionalPath.setConditionBeedPath($truePath);
    checkConditionalPath($truePath, $true, true,
        $firstStringBeedPath, $secondStringBeedPath,
        $firstStringBeedPath, $firstStringBeed);
    // set an effective condition path, whose value is an effective beed with an effective value,
    // that is NOT in the cases map
    $conditionalPath.setSecondPath(null);
    $conditionalPath.setConditionBeedPath($falsePath);
    checkConditionalPath($falsePath, $false, false,
        $firstStringBeedPath, null, null, null);
  }

  /**
   * Path was null.
   * No condition selected.
   */
  @Test
  public void setFirstPath1() {
    // set the first path
    $conditionalPath.setFirstPath($firstStringBeedPath);
    checkConditionalPath(null, null, null, $firstStringBeedPath, null, null, null);
    // set the first path to null
    $conditionalPath.setFirstPath(null);
    checkConditionalPath(null, null, null, null, null, null, null);
  }

  /**
   * Path was null.
   * False is selected.
   */
  @Test
  public void setFirstPath2() {
    // set the first path
    $conditionalPath.setConditionBeedPath($falsePath);
    $conditionalPath.setFirstPath($firstStringBeedPath);
    checkConditionalPath($falsePath, $false, false,
        $firstStringBeedPath, null, null, null);
    // set the first path to null
    $conditionalPath.setFirstPath(null);
    checkConditionalPath($falsePath, $false, false,
        null, null, null, null);
  }

  /**
   * Path was null.
   * True is selected.
   */
  @Test
  public void setFirstPath3() {
    // set the first path
    $conditionalPath.setConditionBeedPath($truePath);
    $conditionalPath.setFirstPath($firstStringBeedPath);
    checkConditionalPath($truePath, $true, true,
        $firstStringBeedPath, null,
        $firstStringBeedPath, $firstStringBeed);
    // set the first path to null
    $conditionalPath.setFirstPath(null);
    checkConditionalPath($truePath, $true, true,
        null, null, null, null);
  }

  /**
   * Path was already set.
   * No condition selected
   */
  @Test
  public void setFirstPath4() {
    // set the first path
    $conditionalPath.setFirstPath($firstStringBeedPath);
    checkConditionalPath(null, null, null, $firstStringBeedPath, null, null, null);
    // set the first path again
    $conditionalPath.setFirstPath($secondStringBeedPath);
    checkConditionalPath(null, null, null, $secondStringBeedPath, null, null, null);
    // set the first path to null
    $conditionalPath.setFirstPath(null);
    checkConditionalPath(null, null, null, null, null, null, null);
  }

  /**
   * Path was already effective.
   * The condition is true.
   */
  @Test
  public void setFirstPath5() {
    $conditionalPath.setConditionBeedPath($truePath);
    // set the first path
    $conditionalPath.setFirstPath($firstStringBeedPath);
    checkConditionalPath($truePath, $true, true,
        $firstStringBeedPath, null,
        $firstStringBeedPath, $firstStringBeed);
    // set the first path again
    $conditionalPath.setFirstPath($secondStringBeedPath);
    checkConditionalPath($truePath, $true, true,
        $secondStringBeedPath, null,
        $secondStringBeedPath, $secondStringBeed);
    // set the first path to null
    $conditionalPath.setFirstPath(null);
    checkConditionalPath($truePath, $true, true,
        null, null, null, null);
  }

  /**
   * Path was already effective.
   * The condition is false.
   */
  @Test
  public void setFirstPath6() {
    $conditionalPath.setConditionBeedPath($falsePath);
    // set the first path
    $conditionalPath.setFirstPath($firstStringBeedPath);
    checkConditionalPath($falsePath, $false, false,
        $firstStringBeedPath, null, null, null);
    // set the first path again
    $conditionalPath.setFirstPath($secondStringBeedPath);
    checkConditionalPath($falsePath, $false, false,
        $secondStringBeedPath, null, null, null);
    // set the first path to null
    $conditionalPath.setFirstPath(null);
    checkConditionalPath($falsePath, $false, false,
        null, null, null, null);
  }

  /**
   * Path was null.
   * No condition selected.
   */
  @Test
  public void setSecondPath1() {
    // set the second path
    $conditionalPath.setSecondPath($secondStringBeedPath);
    checkConditionalPath(null, null, null, null, $secondStringBeedPath, null, null);
    // set the second path to null
    $conditionalPath.setSecondPath(null);
    checkConditionalPath(null, null, null, null, null, null, null);
  }

  /**
   * Path was null.
   * False is selected.
   */
  @Test
  public void setSecondPath2() {
    // set the second path
    $conditionalPath.setConditionBeedPath($falsePath);
    $conditionalPath.setSecondPath($secondStringBeedPath);
    checkConditionalPath($falsePath, $false, false,
        null, $secondStringBeedPath, $secondStringBeedPath, $secondStringBeed);
    // set the second path to null
    $conditionalPath.setSecondPath(null);
    checkConditionalPath($falsePath, $false, false,
        null, null, null, null);
  }

  /**
   * Path was null.
   * True is selected.
   */
  @Test
  public void setSecondPath3() {
    // set the second path
    $conditionalPath.setConditionBeedPath($truePath);
    $conditionalPath.setSecondPath($secondStringBeedPath);
    checkConditionalPath($truePath, $true, true,
        null, $secondStringBeedPath, null, null);
    // set the second path to null
    $conditionalPath.setSecondPath(null);
    checkConditionalPath($truePath, $true, true,
        null, null, null, null);
  }

  /**
   * Path was already set.
   * No condition selected
   */
  @Test
  public void setSecondPath4() {
    // set the second path
    $conditionalPath.setSecondPath($secondStringBeedPath);
    checkConditionalPath(null, null, null, null, $secondStringBeedPath, null, null);
    // set the second path again
    $conditionalPath.setSecondPath($firstStringBeedPath);
    checkConditionalPath(null, null, null, null, $firstStringBeedPath, null, null);
    // set the second path to null
    $conditionalPath.setSecondPath(null);
    checkConditionalPath(null, null, null, null, null, null, null);
  }

  /**
   * Path was already effective.
   * The condition is true.
   */
  @Test
  public void setSecondPath5() {
    $conditionalPath.setConditionBeedPath($truePath);
    // set the second path
    $conditionalPath.setSecondPath($secondStringBeedPath);
    checkConditionalPath($truePath, $true, true,
        null, $secondStringBeedPath, null, null);
    // set the second path again
    $conditionalPath.setSecondPath($firstStringBeedPath);
    checkConditionalPath($truePath, $true, true,
        null, $firstStringBeedPath, null, null);
    // set the second path to null
    $conditionalPath.setSecondPath(null);
    checkConditionalPath($truePath, $true, true,
        null, null, null, null);
  }

  /**
   * Path was already effective.
   * The condition is false.
   */
  @Test
  public void setSecondPath6() {
    $conditionalPath.setConditionBeedPath($falsePath);
    // set the second path
    $conditionalPath.setSecondPath($secondStringBeedPath);
    checkConditionalPath($falsePath, $false, false,
        null, $secondStringBeedPath,
        $secondStringBeedPath, $secondStringBeed);
    // set the second path again
    $conditionalPath.setSecondPath($firstStringBeedPath);
    checkConditionalPath($falsePath, $false, false,
        null, $firstStringBeedPath,
        $firstStringBeedPath, $firstStringBeed);
    // set the second path to null
    $conditionalPath.setSecondPath(null);
    checkConditionalPath($falsePath, $false, false,
        null, null, null, null);
  }

  /**
   * Value of condition beed path changes.
   */
  @Test
  public void conditionChanges1a() {
    // set first and second path
    $conditionalPath.setFirstPath($firstStringBeedPath);
    $conditionalPath.setSecondPath($secondStringBeedPath);
    // create a mutable path to a condition
    MutablePath<BooleanBeed> mutablePath = new MutablePath<BooleanBeed>($true);
    $conditionalPath.setConditionBeedPath(mutablePath);
    // initial check
    checkConditionalPath(mutablePath, $true, true,
        $firstStringBeedPath, $secondStringBeedPath,
        $firstStringBeedPath, $firstStringBeed);
    // add listeners to the conditional path
    $conditionalPath.addListener($listener1);
    // change the value of the condition path
    mutablePath.set($false);
    // final check
    checkConditionalPath(mutablePath, $false, false,
        $firstStringBeedPath, $secondStringBeedPath,
        $secondStringBeedPath, $secondStringBeed);
    // check listener
    assertNotNull($listener1.$event);
    assertEquals($conditionalPath, $listener1.$event.getSource());
    assertEquals($firstStringBeed, $listener1.$event.getOldValue());
    assertEquals($secondStringBeed, $listener1.$event.getNewValue());
    assertNull($listener1.$event.getEdit());
    $listener1.reset();
    // change the value of the condition path to null
    mutablePath.set(null);
    // final check
    checkConditionalPath(mutablePath, null, null,
        $firstStringBeedPath, $secondStringBeedPath,
        null, null);
    // check listener
    assertNotNull($listener1.$event);
    assertEquals($conditionalPath, $listener1.$event.getSource());
    assertEquals($secondStringBeed, $listener1.$event.getOldValue());
    assertEquals(null, $listener1.$event.getNewValue());
    assertNull($listener1.$event.getEdit());
    $listener1.reset();
  }

  /**
   * Value of condition beed path changes, but the resulting selected beed is equal
   * to the original one.
   */
  @Test
  public void conditionChanges1b() {
    // add mappings
    $conditionalPath.setFirstPath($firstStringBeedPath);
    $conditionalPath.setSecondPath($firstStringBeedPath);
    // create a mutable path to a condition
    MutablePath<BooleanBeed> mutablePath = new MutablePath<BooleanBeed>($true);
    $conditionalPath.setConditionBeedPath(mutablePath);
    // initial check
    checkConditionalPath(mutablePath, $true, true,
        $firstStringBeedPath, $firstStringBeedPath,
        $firstStringBeedPath, $firstStringBeed);
    // add listeners to the switch path
    $conditionalPath.addListener($listener1);
    // change the value of the condition path
    mutablePath.set($false);
    // final check
    checkConditionalPath(mutablePath, $false, false,
        $firstStringBeedPath, $firstStringBeedPath,
        $firstStringBeedPath, $firstStringBeed);
    // check listener
    assertNull($listener1.$event);
  }

  /**
   * Value of condition beed path changes, but the resulting selected beed is equal
   * to the original one.
   */
  @Test
  public void conditionChanges1c() {
    // add mappings
    $conditionalPath.setFirstPath(null);
    $conditionalPath.setSecondPath(null);
    // create a mutable path to a condition
    MutablePath<BooleanBeed> mutablePath = new MutablePath<BooleanBeed>($true);
    $conditionalPath.setConditionBeedPath(mutablePath);
    // initial check
    checkConditionalPath(mutablePath, $true, true,
        null, null, null, null);
    // add listeners to the switch path
    $conditionalPath.addListener($listener1);
    // change the value of the condition path
    mutablePath.set($false);
    // final check
    checkConditionalPath(mutablePath, $false, false,
        null, null, null, null);
    // check listener
    assertNull($listener1.$event);
  }

  /**
   * Value of condition beed changes.
   */
  @Test
  public void conditionChanges2a() throws EditStateException, IllegalEditException {
    // add mappings
    $conditionalPath.setFirstPath($firstStringBeedPath);
    $conditionalPath.setSecondPath($secondStringBeedPath);
    // create an editable condition beed
    EditableBooleanBeed conditionBeed = editableBooleanBeed(true, $owner);
    Path<BooleanBeed> conditionBeedPath = path((BooleanBeed)conditionBeed);
    $conditionalPath.setConditionBeedPath(conditionBeedPath);
    // initial check
    checkConditionalPath(conditionBeedPath, conditionBeed, true,
        $firstStringBeedPath, $secondStringBeedPath,
        $firstStringBeedPath, $firstStringBeed);
    // add listeners to the switch path
    $conditionalPath.addListener($listener1);
    // change the value of the condition beed
    BooleanEdit booleanEdit = new BooleanEdit(conditionBeed);
    booleanEdit.setGoal(false);
    booleanEdit.perform();
    // final check
    checkConditionalPath(conditionBeedPath, conditionBeed, false,
        $firstStringBeedPath, $secondStringBeedPath,
        $secondStringBeedPath, $secondStringBeed);
    // check listener
    assertNotNull($listener1.$event);
    assertEquals($conditionalPath, $listener1.$event.getSource());
    assertEquals($firstStringBeed, $listener1.$event.getOldValue());
    assertEquals($secondStringBeed, $listener1.$event.getNewValue());
    assertEquals(booleanEdit, $listener1.$event.getEdit());
    $listener1.reset();
    // change the value of the condition beed to null
    booleanEdit = new BooleanEdit(conditionBeed);
    booleanEdit.setGoal(null);
    booleanEdit.perform();
    // final check
    checkConditionalPath(conditionBeedPath, conditionBeed, null,
        $firstStringBeedPath, $secondStringBeedPath,
        null, null);
    // check listener
    assertNotNull($listener1.$event);
    assertEquals($conditionalPath, $listener1.$event.getSource());
    assertEquals($secondStringBeed, $listener1.$event.getOldValue());
    assertEquals(null, $listener1.$event.getNewValue());
    assertEquals(booleanEdit, $listener1.$event.getEdit());
    $listener1.reset();
  }

  /**
   * Value of condition beed changes, but the resulting selected beed is equal
   * to the original one.
   */
  @Test
  public void conditionChanges2b() throws EditStateException, IllegalEditException {
    // add mappings
    $conditionalPath.setFirstPath($firstStringBeedPath);
    $conditionalPath.setSecondPath($firstStringBeedPath);
    // create an editable condition beed
    EditableBooleanBeed conditionBeed = editableBooleanBeed(true, $owner);
    Path<BooleanBeed> conditionBeedPath = path((BooleanBeed)conditionBeed);
    $conditionalPath.setConditionBeedPath(conditionBeedPath);
    // initial check
    checkConditionalPath(conditionBeedPath, conditionBeed, true,
        $firstStringBeedPath, $firstStringBeedPath,
        $firstStringBeedPath, $firstStringBeed);
    // add listeners to the switch path
    $conditionalPath.addListener($listener1);
    // change the value of the condition beed
    BooleanEdit booleanEdit = new BooleanEdit(conditionBeed);
    booleanEdit.setGoal(false);
    booleanEdit.perform();
    // final check
    checkConditionalPath(conditionBeedPath, conditionBeed, false,
        $firstStringBeedPath, $firstStringBeedPath,
        $firstStringBeedPath, $firstStringBeed);
    // check listener
    assertNull($listener1.$event);
  }

  /**
   * Value of selected beed path changes.
   */
  @Test
  public void selectedBeedPathChanges1a() {
    // create a mutable path to a selected beed
    MutablePath<StringBeed> mutablePath = new MutablePath<StringBeed>($firstStringBeed);
    // add mappings
    $conditionalPath.setFirstPath(mutablePath);
    $conditionalPath.setSecondPath($secondStringBeedPath);
    // set condition path
    $conditionalPath.setConditionBeedPath($truePath);
    // initial check
    checkConditionalPath($truePath, $true, true,
        mutablePath, $secondStringBeedPath,
        mutablePath, $firstStringBeed);
    // add listeners to the switch path
    $conditionalPath.addListener($listener1);
    // change the value of the selected beed path
    mutablePath.set($secondStringBeed);
    // final check
    checkConditionalPath($truePath, $true, true,
        mutablePath, $secondStringBeedPath,
        mutablePath, $secondStringBeed);
    // check listener
    assertNotNull($listener1.$event);
    assertEquals($conditionalPath, $listener1.$event.getSource());
    assertEquals($firstStringBeed, $listener1.$event.getOldValue());
    assertEquals($secondStringBeed, $listener1.$event.getNewValue());
    assertNull($listener1.$event.getEdit());
    $listener1.reset();
    // change the value of the selected beed path to null
    mutablePath.set(null);
    // final check
    checkConditionalPath($truePath, $true, true,
        mutablePath, $secondStringBeedPath,
        mutablePath, null);
    // check listener
    assertNotNull($listener1.$event);
    assertEquals($conditionalPath, $listener1.$event.getSource());
    assertEquals($secondStringBeed, $listener1.$event.getOldValue());
    assertEquals(null, $listener1.$event.getNewValue());
    assertNull($listener1.$event.getEdit());
    $listener1.reset();
  }

  /**
   * Value of selected beed path changes, but the resulting selected beed is equal
   * to the original one.
   */
  @Test
  public void selectedBeedPathChanges1b() {
    // create a mutable path to a selected beed
    MutablePath<StringBeed> mutablePath = new MutablePath<StringBeed>($firstStringBeed);
    // add mappings
    $conditionalPath.setFirstPath(mutablePath);
    $conditionalPath.setSecondPath($secondStringBeedPath);
    // set condition path
    $conditionalPath.setConditionBeedPath($truePath);
    // initial check
    checkConditionalPath($truePath, $true, true,
        mutablePath, $secondStringBeedPath,
        mutablePath, $firstStringBeed);
    // add listeners to the switch path
    $conditionalPath.addListener($listener1);
    // change the value of the selected beed path
    mutablePath.set($firstStringBeed);
    // final check
    checkConditionalPath($truePath, $true, true,
        mutablePath, $secondStringBeedPath,
        mutablePath, $firstStringBeed);
    // check listener
    assertNull($listener1.$event);
  }

  /**
   * Value of a not-selected beed path changes.
   */
  @Test
  public void selectedBeedPathChanges2() {
    // create a mutable path to a selected beed
    MutablePath<StringBeed> mutablePath = new MutablePath<StringBeed>($secondStringBeed);
    // add mappings
    $conditionalPath.setFirstPath($firstStringBeedPath);
    $conditionalPath.setSecondPath(mutablePath);
    // set condition path
    $conditionalPath.setConditionBeedPath($truePath);
    // initial check
    checkConditionalPath($truePath, $true, true,
        $firstStringBeedPath, mutablePath,
        $firstStringBeedPath, $firstStringBeed);
    // add listeners to the switch path
    $conditionalPath.addListener($listener1);
    // change the value of the selected beed path
    mutablePath.set(null);
    // final check
    checkConditionalPath($truePath, $true, true,
        $firstStringBeedPath, mutablePath,
        $firstStringBeedPath, $firstStringBeed);
    // check listener
    assertNull($listener1.$event);
    // change the value of the selected beed path to null
    mutablePath.set(null);
    // final check
    checkConditionalPath($truePath, $true, true,
        $firstStringBeedPath, mutablePath,
        $firstStringBeedPath, $firstStringBeed);
    // check listener
    assertNull($listener1.$event);
  }

}
