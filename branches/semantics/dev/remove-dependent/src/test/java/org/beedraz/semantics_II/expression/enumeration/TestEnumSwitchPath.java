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


import static org.beedraz.semantics_II.expression.enumeration.EnumBeeds.editableEnumBeed;
import static org.beedraz.semantics_II.expression.enumeration.TestEnumSwitchPath.Colors.BLUE;
import static org.beedraz.semantics_II.expression.enumeration.TestEnumSwitchPath.Colors.RED;
import static org.beedraz.semantics_II.expression.enumeration.TestEnumSwitchPath.Colors.YELLOW;
import static org.beedraz.semantics_II.expression.string.StringBeeds.editableStringBeed;
import static org.beedraz.semantics_II.path.Paths.path;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.EditStateException;
import org.beedraz.semantics_II.IllegalEditException;
import org.beedraz.semantics_II.StubListener;
import org.beedraz.semantics_II.bean.StubBeanBeed;
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
public class TestEnumSwitchPath {

  @Before
  public void setUp() throws Exception {
    $owner = new StubBeanBeed();
    $enumSwitchPath = new EnumSwitchPath<Colors, StringBeed>();
    $blue = editableEnumBeed(BLUE, $owner);
//    $bluePath = path($blue);
    $blueStringBeed = editableStringBeed("blue", $owner);
    $blueStringBeedPath = path($blueStringBeed);
    $red = editableEnumBeed(RED, $owner);
    $redPath = path($red);
    $redStringBeed = editableStringBeed("red", $owner);
    $redStringBeedPath = path($redStringBeed);
    $yellow = editableEnumBeed(YELLOW, $owner);
    $yellowPath = path($yellow);
    $yellowStringBeed = editableStringBeed("yellow", $owner);
    $yellowStringBeedPath = path($yellowStringBeed);
    $null = editableEnumBeed(null, $owner);
    $nullPath = path($null);
    $emptyPath = new NullPath<EnumBeed<Colors>>();
    $listener1 = new StubListener<PathEvent<StringBeed>>();
  }

  @After
  public void tearDown() throws Exception {
    $owner = null;
    $enumSwitchPath = null;
    $blue = null;
//    $bluePath = null;
    $blueStringBeed = null;
    $blueStringBeedPath = null;
    $red = null;
    $redPath = null;
    $redStringBeed = null;
    $redStringBeedPath = null;
    $yellow = null;
    $yellowPath = null;
    $yellowStringBeed = null;
    $yellowStringBeedPath = null;
    $nullPath = null;
    $null = null;
    $emptyPath = null;
    $listener1 = null;
  }

  private StubBeanBeed $owner;
  private EnumSwitchPath<Colors, StringBeed> $enumSwitchPath;
//  private Path<EnumBeed<Colors>> $bluePath;
  private EnumBeed<Colors> $blue;
  private StringBeed $blueStringBeed;
  private Path<StringBeed> $blueStringBeedPath;
  private Path<EnumBeed<Colors>> $redPath;
  private EnumBeed<Colors> $red;
  private StringBeed $redStringBeed;
  private Path<StringBeed> $redStringBeedPath;
  private Path<EnumBeed<Colors>> $yellowPath;
  private EnumBeed<Colors> $yellow;
  private StringBeed $yellowStringBeed;
  private Path<StringBeed> $yellowStringBeedPath;
  private Path<EnumBeed<Colors>> $nullPath;
  private EnumBeed<Colors> $null;
  private Path<EnumBeed<Colors>> $emptyPath;
  private StubListener<PathEvent<StringBeed>> $listener1;

  @Test
  public void constructor() {
    assertNull($enumSwitchPath.getKeyBeedPath());
    assertNull($enumSwitchPath.getKeyBeed());
    assertNull($enumSwitchPath.getKey());
    assertEquals(0, $enumSwitchPath.getCasePaths().size());
    assertNull($enumSwitchPath.getSelectedPath());
    assertNull($enumSwitchPath.get());
  }


  /**
   * There are not cases.
   */
  @Test
  public void setKeyBeedPath1() {
    // set a non-effective key path
    $enumSwitchPath.setKeyBeedPath(null);
    assertNull($enumSwitchPath.getKeyBeedPath());
    assertNull($enumSwitchPath.getKeyBeed());
    assertNull($enumSwitchPath.getKey());
    assertEquals(0, $enumSwitchPath.getCasePaths().size());
    assertNull($enumSwitchPath.getSelectedPath());
    assertNull($enumSwitchPath.get());
    // set an effective key path, whose value is not effective
    $enumSwitchPath.setKeyBeedPath($emptyPath);
    assertEquals($emptyPath, $enumSwitchPath.getKeyBeedPath());
    assertEquals(null, $enumSwitchPath.getKeyBeed());
    assertEquals(null, $enumSwitchPath.getKey());
    assertEquals(0, $enumSwitchPath.getCasePaths().size());
    assertNull($enumSwitchPath.getSelectedPath());
    assertNull($enumSwitchPath.get());
    // set an effective key path, whose value is an effective beed with value null
    $enumSwitchPath.setKeyBeedPath($nullPath);
    assertEquals($nullPath, $enumSwitchPath.getKeyBeedPath());
    assertEquals($null, $enumSwitchPath.getKeyBeed());
    assertEquals(null, $enumSwitchPath.getKey());
    assertEquals(0, $enumSwitchPath.getCasePaths().size());
    assertNull($enumSwitchPath.getSelectedPath());
    assertNull($enumSwitchPath.get());
    // set an effective key path, whose value is an effective beed with an effective value
    $enumSwitchPath.setKeyBeedPath($redPath);
    assertEquals($redPath, $enumSwitchPath.getKeyBeedPath());
    assertEquals($red, $enumSwitchPath.getKeyBeed());
    assertEquals(RED, $enumSwitchPath.getKey());
    assertEquals(0, $enumSwitchPath.getCasePaths().size());
    assertNull($enumSwitchPath.getSelectedPath());
    assertNull($enumSwitchPath.get());
  }

  /**
   * There are cases.
   */
  @Test
  public void setKeyBeedPath2() {
    $enumSwitchPath.addCasePath(RED, $redStringBeedPath);
    $enumSwitchPath.addCasePath(BLUE, $blueStringBeedPath);
    // set a non-effective key path
    $enumSwitchPath.setKeyBeedPath(null);
    assertNull($enumSwitchPath.getKeyBeedPath());
    assertNull($enumSwitchPath.getKeyBeed());
    assertNull($enumSwitchPath.getKey());
    assertEquals(2, $enumSwitchPath.getCasePaths().size());
    assertNull($enumSwitchPath.getSelectedPath());
    assertNull($enumSwitchPath.get());
    // set an effective key path, whose value is not effective
    $enumSwitchPath.setKeyBeedPath($emptyPath);
    assertEquals($emptyPath, $enumSwitchPath.getKeyBeedPath());
    assertEquals(null, $enumSwitchPath.getKeyBeed());
    assertEquals(null, $enumSwitchPath.getKey());
    assertEquals(2, $enumSwitchPath.getCasePaths().size());
    assertNull($enumSwitchPath.getSelectedPath());
    assertNull($enumSwitchPath.get());
    // set an effective key path, whose value is an effective beed with value null
    $enumSwitchPath.setKeyBeedPath($nullPath);
    assertEquals($nullPath, $enumSwitchPath.getKeyBeedPath());
    assertEquals($null, $enumSwitchPath.getKeyBeed());
    assertEquals(null, $enumSwitchPath.getKey());
    assertEquals(2, $enumSwitchPath.getCasePaths().size());
    assertNull($enumSwitchPath.getSelectedPath());
    assertNull($enumSwitchPath.get());
    // set an effective key path, whose value is an effective beed with an effective value,
    // that is in the cases map
    $enumSwitchPath.setKeyBeedPath($redPath);
    assertEquals($redPath, $enumSwitchPath.getKeyBeedPath());
    assertEquals($red, $enumSwitchPath.getKeyBeed());
    assertEquals(RED, $enumSwitchPath.getKey());
    assertEquals(2, $enumSwitchPath.getCasePaths().size());
    assertEquals($redStringBeedPath, $enumSwitchPath.getSelectedPath());
    assertEquals($redStringBeed, $enumSwitchPath.get());
    // set an effective key path, whose value is an effective beed with an effective value,
    // that is NOT in the cases map
    $enumSwitchPath.setKeyBeedPath($yellowPath);
    assertEquals($yellowPath, $enumSwitchPath.getKeyBeedPath());
    assertEquals($yellow, $enumSwitchPath.getKeyBeed());
    assertEquals(YELLOW, $enumSwitchPath.getKey());
    assertEquals(2, $enumSwitchPath.getCasePaths().size());
    assertNull($enumSwitchPath.getSelectedPath());
    assertNull($enumSwitchPath.get());
  }

  /**
   * Key that is not in {@link EnumSwitchPath#getCasePaths()}.
   * No key selected.
   */
  @Test
  public void addCasePath1a() {
    // add key RED
    Colors key1 = RED;
    Path<StringBeed> valuePath1 = $redStringBeedPath;
    $enumSwitchPath.addCasePath(key1, valuePath1);
    assertNull($enumSwitchPath.getKeyBeedPath());
    assertNull($enumSwitchPath.getKeyBeed());
    assertNull($enumSwitchPath.getKey());
    assertEquals(1, $enumSwitchPath.getCasePaths().size());
    assertTrue($enumSwitchPath.getCasePaths().containsKey(key1));
    assertEquals(valuePath1, $enumSwitchPath.getCasePaths().get(key1));
    assertNull($enumSwitchPath.getSelectedPath());
    assertNull($enumSwitchPath.get());
    // add key BLUE
    Colors key2 = BLUE;
    Path<StringBeed> valuePath2 = $blueStringBeedPath;
    $enumSwitchPath.addCasePath(key2, valuePath2);
    assertNull($enumSwitchPath.getKeyBeedPath());
    assertNull($enumSwitchPath.getKeyBeed());
    assertNull($enumSwitchPath.getKey());
    assertEquals(2, $enumSwitchPath.getCasePaths().size());
    assertTrue($enumSwitchPath.getCasePaths().containsKey(key1));
    assertEquals(valuePath1, $enumSwitchPath.getCasePaths().get(key1));
    assertTrue($enumSwitchPath.getCasePaths().containsKey(key2));
    assertEquals(valuePath2, $enumSwitchPath.getCasePaths().get(key2));
    assertNull($enumSwitchPath.getSelectedPath());
    assertNull($enumSwitchPath.get());
  }

  /**
   * Key that is not in {@link EnumSwitchPath#getCasePaths()}.
   * There is a key selected, different from the key that is added.
   */
  @Test
  public void addCasePath1b() {
    $enumSwitchPath.setKeyBeedPath($yellowPath);
    // add key RED
    Colors key1 = RED;
    Path<StringBeed> valuePath1 = $redStringBeedPath;
    $enumSwitchPath.addCasePath(key1, valuePath1);
    assertEquals($yellowPath, $enumSwitchPath.getKeyBeedPath());
    assertEquals($yellow, $enumSwitchPath.getKeyBeed());
    assertEquals(YELLOW, $enumSwitchPath.getKey());
    assertEquals(1, $enumSwitchPath.getCasePaths().size());
    assertTrue($enumSwitchPath.getCasePaths().containsKey(key1));
    assertEquals(valuePath1, $enumSwitchPath.getCasePaths().get(key1));
    assertNull($enumSwitchPath.getSelectedPath());
    assertNull($enumSwitchPath.get());
    // add key BLUE
    Colors key2 = BLUE;
    Path<StringBeed> valuePath2 = $blueStringBeedPath;
    $enumSwitchPath.addCasePath(key2, valuePath2);
    assertEquals($yellowPath, $enumSwitchPath.getKeyBeedPath());
    assertEquals($yellow, $enumSwitchPath.getKeyBeed());
    assertEquals(YELLOW, $enumSwitchPath.getKey());
    assertEquals(2, $enumSwitchPath.getCasePaths().size());
    assertTrue($enumSwitchPath.getCasePaths().containsKey(key1));
    assertEquals(valuePath1, $enumSwitchPath.getCasePaths().get(key1));
    assertTrue($enumSwitchPath.getCasePaths().containsKey(key2));
    assertEquals(valuePath2, $enumSwitchPath.getCasePaths().get(key2));
    assertNull($enumSwitchPath.getSelectedPath());
    assertNull($enumSwitchPath.get());
  }

  /**
   * Key that is not in {@link EnumSwitchPath#getCasePaths()}.
   * There is a key selected, equal to the key that is added.
   */
  @Test
  public void addCasePath1c() {
    $enumSwitchPath.setKeyBeedPath($redPath);
    // add key RED
    Colors key1 = RED;
    Path<StringBeed> valuePath1 = $redStringBeedPath;
    $enumSwitchPath.addCasePath(key1, valuePath1);
    assertEquals($redPath, $enumSwitchPath.getKeyBeedPath());
    assertEquals($red, $enumSwitchPath.getKeyBeed());
    assertEquals(RED, $enumSwitchPath.getKey());
    assertEquals(1, $enumSwitchPath.getCasePaths().size());
    assertTrue($enumSwitchPath.getCasePaths().containsKey(key1));
    assertEquals(valuePath1, $enumSwitchPath.getCasePaths().get(key1));
    assertEquals($redStringBeedPath, $enumSwitchPath.getSelectedPath());
    assertEquals($redStringBeedPath.get(), $enumSwitchPath.get());
  }

  /**
   * Key that is already in {@link EnumSwitchPath#getCasePaths()}.
   * The key is equal to the selected key value.
   */
  @Test
  public void addCasePath2() {
    $enumSwitchPath.setKeyBeedPath($redPath);
    // add key RED
    Colors key1 = RED;
    Path<StringBeed> valuePath1 = $redStringBeedPath;
    $enumSwitchPath.addCasePath(key1, valuePath1);
    assertEquals($redPath, $enumSwitchPath.getKeyBeedPath());
    assertEquals($red, $enumSwitchPath.getKeyBeed());
    assertEquals(RED, $enumSwitchPath.getKey());
    assertEquals(1, $enumSwitchPath.getCasePaths().size());
    assertTrue($enumSwitchPath.getCasePaths().containsKey(key1));
    assertEquals(valuePath1, $enumSwitchPath.getCasePaths().get(key1));
    assertEquals($redStringBeedPath, $enumSwitchPath.getSelectedPath());
    assertEquals($redStringBeed, $enumSwitchPath.get());
    // add key RED again
    Path<StringBeed> valuePath2 = $blueStringBeedPath;
    $enumSwitchPath.addCasePath(key1, valuePath2);
    assertEquals($redPath, $enumSwitchPath.getKeyBeedPath());
    assertEquals($red, $enumSwitchPath.getKeyBeed());
    assertEquals(RED, $enumSwitchPath.getKey());
    assertEquals(1, $enumSwitchPath.getCasePaths().size());
    assertTrue($enumSwitchPath.getCasePaths().containsKey(key1));
    assertEquals(valuePath2, $enumSwitchPath.getCasePaths().get(key1));
    assertEquals($blueStringBeedPath, $enumSwitchPath.getSelectedPath());
    assertEquals($blueStringBeed, $enumSwitchPath.get());
  }

  /**
   * Key that is already in {@link EnumSwitchPath#getCasePaths()}.
   * The key is different from the selected key value.
   */
  @Test
  public void addCasePath3() {
    $enumSwitchPath.setKeyBeedPath($yellowPath);
    // add key RED
    Colors key1 = RED;
    Path<StringBeed> valuePath1 = $redStringBeedPath;
    $enumSwitchPath.addCasePath(key1, valuePath1);
    assertEquals($yellowPath, $enumSwitchPath.getKeyBeedPath());
    assertEquals($yellow, $enumSwitchPath.getKeyBeed());
    assertEquals(YELLOW, $enumSwitchPath.getKey());
    assertEquals(1, $enumSwitchPath.getCasePaths().size());
    assertTrue($enumSwitchPath.getCasePaths().containsKey(key1));
    assertEquals(valuePath1, $enumSwitchPath.getCasePaths().get(key1));
    assertNull($enumSwitchPath.getSelectedPath());
    assertNull($enumSwitchPath.get());
    // add key RED again, with another value
    Path<StringBeed> valuePath2 = $blueStringBeedPath;
    $enumSwitchPath.addCasePath(key1, valuePath2);
    assertEquals($yellowPath, $enumSwitchPath.getKeyBeedPath());
    assertEquals($yellow, $enumSwitchPath.getKeyBeed());
    assertEquals(YELLOW, $enumSwitchPath.getKey());
    assertEquals(1, $enumSwitchPath.getCasePaths().size());
    assertTrue($enumSwitchPath.getCasePaths().containsKey(key1));
    assertEquals(valuePath2, $enumSwitchPath.getCasePaths().get(key1));
    assertNull($enumSwitchPath.getSelectedPath());
    assertNull($enumSwitchPath.get());
  }

  /**
   * Key that is already in {@link EnumSwitchPath#getCasePaths()}.
   * The key is equal to the selected key value.
   */
  @Test
  public void addCasePath4() {
    // add key RED, and select it
    Colors key1 = RED;
    $enumSwitchPath.setKeyBeedPath($redPath);
    Path<StringBeed> valuePath1 = $redStringBeedPath;
    $enumSwitchPath.addCasePath(key1, valuePath1);
    assertEquals($redPath, $enumSwitchPath.getKeyBeedPath());
    assertEquals($red,  $enumSwitchPath.getKeyBeed());
    assertEquals(RED, $enumSwitchPath.getKey());
    assertEquals(1, $enumSwitchPath.getCasePaths().size());
    assertTrue($enumSwitchPath.getCasePaths().containsKey(key1));
    assertEquals(valuePath1, $enumSwitchPath.getCasePaths().get(key1));
    assertEquals(valuePath1, $enumSwitchPath.getSelectedPath());
    assertEquals(valuePath1.get(), $enumSwitchPath.get());
    // add key RED again, with another value
    Path<StringBeed> valuePath2 = $blueStringBeedPath;
    $enumSwitchPath.addCasePath(key1, valuePath2);
    assertEquals($redPath, $enumSwitchPath.getKeyBeedPath());
    assertEquals($red,  $enumSwitchPath.getKeyBeed());
    assertEquals(RED, $enumSwitchPath.getKey());
    assertEquals(1, $enumSwitchPath.getCasePaths().size());
    assertTrue($enumSwitchPath.getCasePaths().containsKey(key1));
    assertEquals(valuePath2, $enumSwitchPath.getCasePaths().get(key1));
    assertEquals(valuePath2, $enumSwitchPath.getSelectedPath());
    assertEquals(valuePath2.get(), $enumSwitchPath.get());
  }

  /**
   * Key that is not in {@link EnumSwitchPath#getCasePaths()}.
   */
  @Test
  public void removeCasePath1() {
    // add key RED
    Colors addedKey = RED;
    Path<StringBeed> valuePath1 = $redStringBeedPath;
    $enumSwitchPath.addCasePath(addedKey, valuePath1);
    assertNull($enumSwitchPath.getKeyBeedPath());
    assertNull($enumSwitchPath.getKeyBeed());
    assertNull($enumSwitchPath.getKey());
    assertEquals(1, $enumSwitchPath.getCasePaths().size());
    assertTrue($enumSwitchPath.getCasePaths().containsKey(addedKey));
    assertEquals(valuePath1, $enumSwitchPath.getCasePaths().get(addedKey));
    assertNull($enumSwitchPath.getSelectedPath());
    assertNull($enumSwitchPath.get());
    // remove key YELLOW
    Colors removedKey = YELLOW;
    $enumSwitchPath.removeCasePath(removedKey);
    assertNull($enumSwitchPath.getKeyBeedPath());
    assertNull($enumSwitchPath.getKeyBeed());
    assertNull($enumSwitchPath.getKey());
    assertEquals(1, $enumSwitchPath.getCasePaths().size());
    assertTrue($enumSwitchPath.getCasePaths().containsKey(addedKey));
    assertEquals(valuePath1, $enumSwitchPath.getCasePaths().get(addedKey));
    assertNull($enumSwitchPath.getSelectedPath());
    assertNull($enumSwitchPath.get());
  }

  /**
   * Key that is in {@link EnumSwitchPath#getCasePaths()},
   * different from the selected key.
   */
  @Test
  public void removeCasePath2() {
    $enumSwitchPath.setKeyBeedPath($yellowPath);
    // add key RED
    Colors addedKey1 = RED;
    Path<StringBeed> valuePath1 = $redStringBeedPath;
    $enumSwitchPath.addCasePath(addedKey1, valuePath1);
    assertEquals($yellowPath, $enumSwitchPath.getKeyBeedPath());
    assertEquals($yellow, $enumSwitchPath.getKeyBeed());
    assertEquals(YELLOW, $enumSwitchPath.getKey());
    assertEquals(1, $enumSwitchPath.getCasePaths().size());
    assertTrue($enumSwitchPath.getCasePaths().containsKey(addedKey1));
    assertEquals(valuePath1, $enumSwitchPath.getCasePaths().get(addedKey1));
    assertNull($enumSwitchPath.getSelectedPath());
    assertNull($enumSwitchPath.get());
    // add key YELLOW
    Colors addedKey2 = YELLOW;
    Path<StringBeed> valuePath2 = $yellowStringBeedPath;
    $enumSwitchPath.addCasePath(addedKey2, valuePath2);
    assertEquals($yellowPath, $enumSwitchPath.getKeyBeedPath());
    assertEquals($yellow, $enumSwitchPath.getKeyBeed());
    assertEquals(YELLOW, $enumSwitchPath.getKey());
    assertEquals(2, $enumSwitchPath.getCasePaths().size());
    assertTrue($enumSwitchPath.getCasePaths().containsKey(addedKey1));
    assertEquals(valuePath1, $enumSwitchPath.getCasePaths().get(addedKey1));
    assertTrue($enumSwitchPath.getCasePaths().containsKey(addedKey2));
    assertEquals(valuePath2, $enumSwitchPath.getCasePaths().get(addedKey2));
    assertEquals(valuePath2, $enumSwitchPath.getSelectedPath());
    assertEquals(valuePath2.get(), $enumSwitchPath.get());
    // remove key RED
    Colors removedKey = addedKey1;
    $enumSwitchPath.removeCasePath(removedKey);
    assertEquals($yellowPath, $enumSwitchPath.getKeyBeedPath());
    assertEquals($yellow, $enumSwitchPath.getKeyBeed());
    assertEquals(YELLOW, $enumSwitchPath.getKey());
    assertEquals(1, $enumSwitchPath.getCasePaths().size());
    assertTrue($enumSwitchPath.getCasePaths().containsKey(addedKey2));
    assertEquals(valuePath2, $enumSwitchPath.getCasePaths().get(addedKey2));
    assertEquals(valuePath2, $enumSwitchPath.getSelectedPath());
    assertEquals(valuePath2.get(), $enumSwitchPath.get());
  }

  /**
   * Key that is in {@link EnumSwitchPath#getCasePaths()},
   * equal to the selected key.
   */
  @Test
  public void removeCasePath3() {
    $enumSwitchPath.setKeyBeedPath($yellowPath);
    // add key RED
    Colors addedKey1 = RED;
    Path<StringBeed> valuePath1 = $redStringBeedPath;
    $enumSwitchPath.addCasePath(addedKey1, valuePath1);
    assertEquals($yellowPath, $enumSwitchPath.getKeyBeedPath());
    assertEquals($yellow, $enumSwitchPath.getKeyBeed());
    assertEquals(YELLOW, $enumSwitchPath.getKey());
    assertEquals(1, $enumSwitchPath.getCasePaths().size());
    assertTrue($enumSwitchPath.getCasePaths().containsKey(addedKey1));
    assertEquals(valuePath1, $enumSwitchPath.getCasePaths().get(addedKey1));
    assertNull($enumSwitchPath.getSelectedPath());
    assertNull($enumSwitchPath.get());
    // add key YELLOW
    Colors addedKey2 = YELLOW;
    Path<StringBeed> valuePath2 = $yellowStringBeedPath;
    $enumSwitchPath.addCasePath(addedKey2, valuePath2);
    assertEquals($yellowPath, $enumSwitchPath.getKeyBeedPath());
    assertEquals($yellow, $enumSwitchPath.getKeyBeed());
    assertEquals(YELLOW, $enumSwitchPath.getKey());
    assertEquals(2, $enumSwitchPath.getCasePaths().size());
    assertTrue($enumSwitchPath.getCasePaths().containsKey(addedKey1));
    assertEquals(valuePath1, $enumSwitchPath.getCasePaths().get(addedKey1));
    assertTrue($enumSwitchPath.getCasePaths().containsKey(addedKey2));
    assertEquals(valuePath2, $enumSwitchPath.getCasePaths().get(addedKey2));
    assertEquals(valuePath2, $enumSwitchPath.getSelectedPath());
    assertEquals(valuePath2.get(), $enumSwitchPath.get());
    // remove key YELLOW
    Colors removedKey = addedKey2;
    $enumSwitchPath.removeCasePath(removedKey);
    assertEquals($yellowPath, $enumSwitchPath.getKeyBeedPath());
    assertEquals($yellow, $enumSwitchPath.getKeyBeed());
    assertEquals(YELLOW, $enumSwitchPath.getKey());
    assertEquals(1, $enumSwitchPath.getCasePaths().size());
    assertTrue($enumSwitchPath.getCasePaths().containsKey(addedKey1));
    assertEquals(valuePath1, $enumSwitchPath.getCasePaths().get(addedKey1));
    assertNull($enumSwitchPath.getSelectedPath());
    assertNull($enumSwitchPath.get());
  }

  /**
   * Value of key beed path changes.
   */
  @Test
  public void keyChanges1a() {
    // add mappings
    $enumSwitchPath.addCasePath(RED, $redStringBeedPath);
    $enumSwitchPath.addCasePath(BLUE, $blueStringBeedPath);
    $enumSwitchPath.addCasePath(YELLOW, $yellowStringBeedPath);
    // create a mutable path to a key
    MutablePath<EnumBeed<Colors>> mutablePath = new MutablePath<EnumBeed<Colors>>($red);
    $enumSwitchPath.setKeyBeedPath(mutablePath);
    // initial check
    assertEquals(mutablePath, $enumSwitchPath.getKeyBeedPath());
    assertEquals($red, $enumSwitchPath.getKeyBeed());
    assertEquals(RED, $enumSwitchPath.getKey());
    assertEquals(3, $enumSwitchPath.getCasePaths().size());
    assertEquals($redStringBeedPath, $enumSwitchPath.getSelectedPath());
    assertEquals($redStringBeed, $enumSwitchPath.get());
    // add listeners to the switch path
    $enumSwitchPath.addListener($listener1);
    // change the value of the key path
    mutablePath.set($blue);
    // final check
    assertEquals(mutablePath, $enumSwitchPath.getKeyBeedPath());
    assertEquals($blue, $enumSwitchPath.getKeyBeed());
    assertEquals(BLUE, $enumSwitchPath.getKey());
    assertEquals(3, $enumSwitchPath.getCasePaths().size());
    assertEquals($blueStringBeedPath, $enumSwitchPath.getSelectedPath());
    assertEquals($blueStringBeed, $enumSwitchPath.get());
    // check listener
    assertNotNull($listener1.$event);
    assertEquals($enumSwitchPath, $listener1.$event.getSource());
    assertEquals($redStringBeed, $listener1.$event.getOldValue());
    assertEquals($blueStringBeed, $listener1.$event.getNewValue());
    assertNull($listener1.$event.getEdit());
    $listener1.reset();
    // change the value of the key path to null
    mutablePath.set(null);
    // final check
    assertEquals(mutablePath, $enumSwitchPath.getKeyBeedPath());
    assertNull($enumSwitchPath.getKeyBeed());
    assertNull($enumSwitchPath.getKey());
    assertEquals(3, $enumSwitchPath.getCasePaths().size());
    assertNull($enumSwitchPath.getSelectedPath());
    assertNull($enumSwitchPath.get());
    // check listener
    assertNotNull($listener1.$event);
    assertEquals($enumSwitchPath, $listener1.$event.getSource());
    assertEquals($blueStringBeed, $listener1.$event.getOldValue());
    assertEquals(null, $listener1.$event.getNewValue());
    assertNull($listener1.$event.getEdit());
    $listener1.reset();
  }

  /**
   * Value of key beed path changes, but the resulting selected beed is equal
   * to the original one.
   */
  @Test
  public void keyChanges1b() {
    // add mappings
    $enumSwitchPath.addCasePath(RED, $redStringBeedPath);
    $enumSwitchPath.addCasePath(BLUE, $redStringBeedPath);
    $enumSwitchPath.addCasePath(YELLOW, $yellowStringBeedPath);
    // create a mutable path to a key
    MutablePath<EnumBeed<Colors>> mutablePath = new MutablePath<EnumBeed<Colors>>($red);
    $enumSwitchPath.setKeyBeedPath(mutablePath);
    // initial check
    assertEquals(mutablePath, $enumSwitchPath.getKeyBeedPath());
    assertEquals($red, $enumSwitchPath.getKeyBeed());
    assertEquals(RED, $enumSwitchPath.getKey());
    assertEquals(3, $enumSwitchPath.getCasePaths().size());
    assertEquals($redStringBeedPath, $enumSwitchPath.getSelectedPath());
    assertEquals($redStringBeed, $enumSwitchPath.get());
    // add listeners to the switch path
    $enumSwitchPath.addListener($listener1);
    // change the value of the key path
    mutablePath.set($blue);
    // final check
    assertEquals(mutablePath, $enumSwitchPath.getKeyBeedPath());
    assertEquals($blue, $enumSwitchPath.getKeyBeed());
    assertEquals(BLUE, $enumSwitchPath.getKey());
    assertEquals(3, $enumSwitchPath.getCasePaths().size());
    assertEquals($redStringBeedPath, $enumSwitchPath.getSelectedPath());
    assertEquals($redStringBeed, $enumSwitchPath.get());
    // check listener
    assertNull($listener1.$event);
  }

  /**
   * Value of key beed path changes, but the resulting selected beed is equal
   * to the original one.
   */
  @Test
  public void keyChanges1c() {
    // add mappings
    $enumSwitchPath.addCasePath(RED, null);
    $enumSwitchPath.addCasePath(BLUE, null);
    $enumSwitchPath.addCasePath(YELLOW, null);
    // create a mutable path to a key
    MutablePath<EnumBeed<Colors>> mutablePath = new MutablePath<EnumBeed<Colors>>($red);
    $enumSwitchPath.setKeyBeedPath(mutablePath);
    // initial check
    assertEquals(mutablePath, $enumSwitchPath.getKeyBeedPath());
    assertEquals($red, $enumSwitchPath.getKeyBeed());
    assertEquals(RED, $enumSwitchPath.getKey());
    assertEquals(3, $enumSwitchPath.getCasePaths().size());
    assertNull($enumSwitchPath.getSelectedPath());
    assertNull($enumSwitchPath.get());
    // add listeners to the switch path
    $enumSwitchPath.addListener($listener1);
    // change the value of the key path
    mutablePath.set($blue);
    // final check
    assertEquals(mutablePath, $enumSwitchPath.getKeyBeedPath());
    assertEquals($blue, $enumSwitchPath.getKeyBeed());
    assertEquals(BLUE, $enumSwitchPath.getKey());
    assertEquals(3, $enumSwitchPath.getCasePaths().size());
    assertNull($enumSwitchPath.getSelectedPath());
    assertNull($enumSwitchPath.get());
    // check listener
    assertNull($listener1.$event);
  }

  /**
   * Value of key beed changes.
   */
  @Test
  public void keyChanges2a() throws EditStateException, IllegalEditException {
    // add mappings
    $enumSwitchPath.addCasePath(RED, $redStringBeedPath);
    $enumSwitchPath.addCasePath(BLUE, $blueStringBeedPath);
    $enumSwitchPath.addCasePath(YELLOW, $yellowStringBeedPath);
    // create an editable key beed
    EditableEnumBeed<Colors> keyBeed = editableEnumBeed(RED, $owner);
    Path<EnumBeed<Colors>> keyBeedPath = path((EnumBeed<Colors>)keyBeed);
    $enumSwitchPath.setKeyBeedPath(keyBeedPath);
    // initial check
    assertEquals(keyBeedPath, $enumSwitchPath.getKeyBeedPath());
    assertEquals(keyBeed, $enumSwitchPath.getKeyBeed());
    assertEquals(RED, $enumSwitchPath.getKey());
    assertEquals(3, $enumSwitchPath.getCasePaths().size());
    assertEquals($redStringBeedPath, $enumSwitchPath.getSelectedPath());
    assertEquals($redStringBeed, $enumSwitchPath.get());
    // add listeners to the switch path
    $enumSwitchPath.addListener($listener1);
    // change the value of the key beed
    EnumEdit<Colors> enumEdit = new EnumEdit<Colors>(keyBeed);
    enumEdit.setGoal(BLUE);
    enumEdit.perform();
    // final check
    assertEquals(keyBeedPath, $enumSwitchPath.getKeyBeedPath());
    assertEquals(keyBeed, $enumSwitchPath.getKeyBeed());
    assertEquals(BLUE, $enumSwitchPath.getKey());
    assertEquals(3, $enumSwitchPath.getCasePaths().size());
    assertEquals($blueStringBeedPath, $enumSwitchPath.getSelectedPath());
    assertEquals($blueStringBeed, $enumSwitchPath.get());
    // check listener
    assertNotNull($listener1.$event);
    assertEquals($enumSwitchPath, $listener1.$event.getSource());
    assertEquals($redStringBeed, $listener1.$event.getOldValue());
    assertEquals($blueStringBeed, $listener1.$event.getNewValue());
    assertEquals(enumEdit, $listener1.$event.getEdit());
    $listener1.reset();
    // change the value of the key beed to null
    enumEdit = new EnumEdit<Colors>(keyBeed);
    enumEdit.setGoal(null);
    enumEdit.perform();
    // final check
    assertEquals(keyBeedPath, $enumSwitchPath.getKeyBeedPath());
    assertEquals(keyBeed, $enumSwitchPath.getKeyBeed());
    assertNull($enumSwitchPath.getKey());
    assertEquals(3, $enumSwitchPath.getCasePaths().size());
    assertNull($enumSwitchPath.getSelectedPath());
    assertNull($enumSwitchPath.get());
    // check listener
    assertNotNull($listener1.$event);
    assertEquals($enumSwitchPath, $listener1.$event.getSource());
    assertEquals($blueStringBeed, $listener1.$event.getOldValue());
    assertEquals(null, $listener1.$event.getNewValue());
    assertEquals(enumEdit, $listener1.$event.getEdit());
    $listener1.reset();
  }

  /**
   * Value of key beed changes, but the resulting selected beed is equal
   * to the original one.
   */
  @Test
  public void keyChanges2b() throws EditStateException, IllegalEditException {
    // add mappings
    $enumSwitchPath.addCasePath(RED, $redStringBeedPath);
    $enumSwitchPath.addCasePath(BLUE, $redStringBeedPath);
    $enumSwitchPath.addCasePath(YELLOW, $yellowStringBeedPath);
    // create an editable key beed
    EditableEnumBeed<Colors> keyBeed = editableEnumBeed(RED, $owner);
    Path<EnumBeed<Colors>> keyBeedPath = path((EnumBeed<Colors>)keyBeed);
    $enumSwitchPath.setKeyBeedPath(keyBeedPath);
    // initial check
    assertEquals(keyBeedPath, $enumSwitchPath.getKeyBeedPath());
    assertEquals(keyBeed, $enumSwitchPath.getKeyBeed());
    assertEquals(RED, $enumSwitchPath.getKey());
    assertEquals(3, $enumSwitchPath.getCasePaths().size());
    assertEquals($redStringBeedPath, $enumSwitchPath.getSelectedPath());
    assertEquals($redStringBeed, $enumSwitchPath.get());
    // add listeners to the switch path
    $enumSwitchPath.addListener($listener1);
    // change the value of the key beed
    EnumEdit<Colors> enumEdit = new EnumEdit<Colors>(keyBeed);
    enumEdit.setGoal(BLUE);
    enumEdit.perform();
    // final check
    assertEquals(keyBeedPath, $enumSwitchPath.getKeyBeedPath());
    assertEquals(keyBeed, $enumSwitchPath.getKeyBeed());
    assertEquals(BLUE, $enumSwitchPath.getKey());
    assertEquals(3, $enumSwitchPath.getCasePaths().size());
    assertEquals($redStringBeedPath, $enumSwitchPath.getSelectedPath());
    assertEquals($redStringBeed, $enumSwitchPath.get());
    // check listener
    assertNull($listener1.$event);
  }

  /**
   * Value of selected beed path changes.
   */
  @Test
  public void selectedBeedPathChanges1a() {
    // create a mutable path to a selected beed
    MutablePath<StringBeed> mutablePath = new MutablePath<StringBeed>($redStringBeed);
    // add mappings
    $enumSwitchPath.addCasePath(RED, mutablePath);
    $enumSwitchPath.addCasePath(BLUE, $blueStringBeedPath);
    $enumSwitchPath.addCasePath(YELLOW, $yellowStringBeedPath);
    // set key path
    $enumSwitchPath.setKeyBeedPath($redPath);
    // initial check
    assertEquals($redPath, $enumSwitchPath.getKeyBeedPath());
    assertEquals($red, $enumSwitchPath.getKeyBeed());
    assertEquals(RED, $enumSwitchPath.getKey());
    assertEquals(3, $enumSwitchPath.getCasePaths().size());
    assertEquals(mutablePath, $enumSwitchPath.getSelectedPath());
    assertEquals($redStringBeed, $enumSwitchPath.get());
    // add listeners to the switch path
    $enumSwitchPath.addListener($listener1);
    // change the value of the selected beed path
    mutablePath.set($blueStringBeed);
    // final check
    assertEquals($redPath, $enumSwitchPath.getKeyBeedPath());
    assertEquals($red, $enumSwitchPath.getKeyBeed());
    assertEquals(RED, $enumSwitchPath.getKey());
    assertEquals(3, $enumSwitchPath.getCasePaths().size());
    assertEquals(mutablePath, $enumSwitchPath.getSelectedPath());
    assertEquals($blueStringBeed, $enumSwitchPath.get());
    // check listener
    assertNotNull($listener1.$event);
    assertEquals($enumSwitchPath, $listener1.$event.getSource());
    assertEquals($redStringBeed, $listener1.$event.getOldValue());
    assertEquals($blueStringBeed, $listener1.$event.getNewValue());
    assertNull($listener1.$event.getEdit());
    $listener1.reset();
    // change the value of the selected beed path to null
    mutablePath.set(null);
    // final check
    assertEquals($redPath, $enumSwitchPath.getKeyBeedPath());
    assertEquals($red, $enumSwitchPath.getKeyBeed());
    assertEquals(RED, $enumSwitchPath.getKey());
    assertEquals(3, $enumSwitchPath.getCasePaths().size());
    assertEquals(mutablePath, $enumSwitchPath.getSelectedPath());
    assertNull($enumSwitchPath.get());
    // check listener
    assertNotNull($listener1.$event);
    assertEquals($enumSwitchPath, $listener1.$event.getSource());
    assertEquals($blueStringBeed, $listener1.$event.getOldValue());
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
    MutablePath<StringBeed> mutablePath = new MutablePath<StringBeed>($redStringBeed);
    // add mappings
    $enumSwitchPath.addCasePath(RED, mutablePath);
    $enumSwitchPath.addCasePath(BLUE, $blueStringBeedPath);
    $enumSwitchPath.addCasePath(YELLOW, $yellowStringBeedPath);
    // set key path
    $enumSwitchPath.setKeyBeedPath($redPath);
    // initial check
    assertEquals($redPath, $enumSwitchPath.getKeyBeedPath());
    assertEquals($red, $enumSwitchPath.getKeyBeed());
    assertEquals(RED, $enumSwitchPath.getKey());
    assertEquals(3, $enumSwitchPath.getCasePaths().size());
    assertEquals(mutablePath, $enumSwitchPath.getSelectedPath());
    assertEquals($redStringBeed, $enumSwitchPath.get());
    // add listeners to the switch path
    $enumSwitchPath.addListener($listener1);
    // change the value of the selected beed path
    mutablePath.set($redStringBeed);
    // final check
    assertEquals($redPath, $enumSwitchPath.getKeyBeedPath());
    assertEquals($red, $enumSwitchPath.getKeyBeed());
    assertEquals(RED, $enumSwitchPath.getKey());
    assertEquals(3, $enumSwitchPath.getCasePaths().size());
    assertEquals(mutablePath, $enumSwitchPath.getSelectedPath());
    assertEquals($redStringBeed, $enumSwitchPath.get());
    // check listener
    assertNull($listener1.$event);
  }

  /**
   * Value of a not-selected beed path changes.
   */
  @Test
  public void selectedBeedPathChanges2() {
    // create a mutable path to a selected beed
    MutablePath<StringBeed> mutablePath = new MutablePath<StringBeed>($blueStringBeed);
    // add mappings
    $enumSwitchPath.addCasePath(RED, $redStringBeedPath);
    $enumSwitchPath.addCasePath(BLUE, mutablePath);
    $enumSwitchPath.addCasePath(YELLOW, $yellowStringBeedPath);
    // set key path
    $enumSwitchPath.setKeyBeedPath($redPath);
    // initial check
    assertEquals($redPath, $enumSwitchPath.getKeyBeedPath());
    assertEquals($red, $enumSwitchPath.getKeyBeed());
    assertEquals(RED, $enumSwitchPath.getKey());
    assertEquals(3, $enumSwitchPath.getCasePaths().size());
    assertEquals($redStringBeedPath, $enumSwitchPath.getSelectedPath());
    assertEquals($redStringBeed, $enumSwitchPath.get());
    // add listeners to the switch path
    $enumSwitchPath.addListener($listener1);
    // change the value of the selected beed path
    mutablePath.set($yellowStringBeed);
    // final check
    assertEquals($redPath, $enumSwitchPath.getKeyBeedPath());
    assertEquals($red, $enumSwitchPath.getKeyBeed());
    assertEquals(RED, $enumSwitchPath.getKey());
    assertEquals(3, $enumSwitchPath.getCasePaths().size());
    assertEquals($redStringBeedPath, $enumSwitchPath.getSelectedPath());
    assertEquals($redStringBeed, $enumSwitchPath.get());
    // check listener
    assertNull($listener1.$event);
    // change the value of the selected beed path to null
    mutablePath.set(null);
    // final check
    assertEquals($redPath, $enumSwitchPath.getKeyBeedPath());
    assertEquals($red, $enumSwitchPath.getKeyBeed());
    assertEquals(RED, $enumSwitchPath.getKey());
    assertEquals(3, $enumSwitchPath.getCasePaths().size());
    assertEquals($redStringBeedPath, $enumSwitchPath.getSelectedPath());
    assertEquals($redStringBeed, $enumSwitchPath.get());
    // check listener
    assertNull($listener1.$event);
  }

  public enum Colors {
    RED,
    YELLOW,
    BLUE
  }

}
