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


import static org.beedraz.semantics_II.StubUpdateSource.MRUSD;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.AbstractBeed;
import org.beedraz.semantics_II.Dependent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class TestAbstractUpdateSource {

  private AbstractBeed<?> $subject;

  private Dependent $dependent1;

  private StubDependent $dependent2;

  private StubDependentUpdateSource $sdus1;

  private StubDependentUpdateSource $sdus2;

  @Before
  public void setUp() throws Exception {
    $subject = new StubUpdateSource();
    $dependent1 = new StubDependent();
    $dependent2 = new StubDependent();
    $sdus1 = new StubDependentUpdateSource();
    $sdus2 = new StubDependentUpdateSource();
  }

  @After
  public void tearDown() throws Exception {
    $subject = null;
    $dependent1 = null;
    $dependent2 = null;
    $sdus1 = null;
    $sdus2 = null;
  }

  @Test
  public void testAddDependent1() {
    assertFalse($subject.isDependent($dependent1));
    assertFalse($subject.isDependent($dependent2));
    assertTrue($subject.getDependents().isEmpty());
    assertTrue($dependent1.getUpdateSources().isEmpty());
    assertTrue($dependent2.getUpdateSources().isEmpty());
    assertEquals(0, $subject.getMaximumRootUpdateSourceDistance());
    assertEquals(0, $dependent1.getMaximumRootUpdateSourceDistance());
    assertEquals(0, $dependent2.getMaximumRootUpdateSourceDistance());
    $dependent1.updateMaximumRootUpdateSourceDistanceUp(MRUSD);
    $subject.addDependent($dependent1);
    assertTrue($subject.isDependent($dependent1));
    assertFalse($subject.isDependent($dependent2));
    assertEquals(1, $subject.getDependents().size());
    assertTrue($subject.getDependents().contains($dependent1));
    assertTrue($dependent1.getUpdateSources().isEmpty());
    assertTrue($dependent2.getUpdateSources().isEmpty());
    assertEquals(0, $subject.getMaximumRootUpdateSourceDistance());
    assertEquals(1, $dependent1.getMaximumRootUpdateSourceDistance());
    assertEquals(0, $dependent2.getMaximumRootUpdateSourceDistance());
    $dependent2.updateMaximumRootUpdateSourceDistanceUp(MRUSD);
    $subject.addDependent($dependent2);
    assertTrue($subject.isDependent($dependent1));
    assertTrue($subject.isDependent($dependent2));
    assertTrue($subject.getDependents().contains($dependent2));
    assertEquals(2, $subject.getDependents().size());
    assertTrue($dependent1.getUpdateSources().isEmpty());
    assertTrue($dependent2.getUpdateSources().isEmpty());
    assertEquals(0, $subject.getMaximumRootUpdateSourceDistance());
    assertEquals(1, $dependent1.getMaximumRootUpdateSourceDistance());
    assertEquals(1, $dependent2.getMaximumRootUpdateSourceDistance());
  }

  @Test
  public void testRemoveDependent1() {
    $dependent1.updateMaximumRootUpdateSourceDistanceUp(MRUSD);
    $subject.addDependent($dependent1);
    assertTrue($subject.isDependent($dependent1));
    assertEquals(1, $subject.getDependents().size());
    assertTrue($subject.getDependents().contains($dependent1));
    assertTrue($dependent1.getUpdateSources().isEmpty());
    $subject.removeDependent($dependent1);
    assertFalse($subject.isDependent($dependent1));
    assertFalse($subject.isDependent($dependent2));
    assertFalse($subject.getDependents().contains($dependent1));
    assertTrue($subject.getDependents().isEmpty());
    assertFalse($dependent1.getUpdateSources().contains($subject));
    assertTrue($dependent1.getUpdateSources().isEmpty());
    // MUDO test for getMaximumRootUpdateSourceDistance
  }

  @Test
  public void testRemoveDependent2() {
    $dependent1.updateMaximumRootUpdateSourceDistanceUp(MRUSD);
    $subject.addDependent($dependent1);
    assertTrue($subject.isDependent($dependent1));
    assertFalse($subject.isDependent($dependent2));
    assertEquals(1, $subject.getDependents().size());
    assertTrue($subject.getDependents().contains($dependent1));
    $subject.removeDependent(null);
    assertTrue($subject.isDependent($dependent1));
    assertFalse($subject.isDependent($dependent2));
    assertEquals(1, $subject.getDependents().size());
    assertTrue($subject.getDependents().contains($dependent1));
  }

  @Test
  public void testRemoveDependent3() {
    $dependent1.updateMaximumRootUpdateSourceDistanceUp(MRUSD);
    $subject.addDependent($dependent1);
    assertTrue($subject.isDependent($dependent1));
    assertFalse($subject.isDependent($dependent2));
    assertEquals(1, $subject.getDependents().size());
    assertTrue($subject.getDependents().contains($dependent1));
    assertTrue($dependent1.getUpdateSources().isEmpty());
    assertTrue($dependent2.getUpdateSources().isEmpty());
    $subject.removeDependent($dependent2);
    assertTrue($subject.isDependent($dependent1));
    assertFalse($subject.isDependent($dependent2));
    assertEquals(1, $subject.getDependents().size());
    assertTrue($subject.getDependents().contains($dependent1));
    assertTrue($dependent1.getUpdateSources().isEmpty());
    assertTrue($dependent2.getUpdateSources().isEmpty());
  }

  @Test
  public void testRemoveDependent4() {
    assertTrue($dependent2.getUpdateSources().isEmpty());
    assertFalse($subject.isDependent($dependent1));
    assertFalse($subject.isDependent($dependent2));
    $subject.removeDependent($dependent2);
    assertFalse($subject.isDependent($dependent1));
    assertFalse($subject.isDependent($dependent2));
    assertTrue($subject.getDependents().isEmpty());
    assertTrue($dependent2.getUpdateSources().isEmpty());
  }

  @Test
  public void testIsTransitiveDependent() {
    assertFalse($subject.isTransitiveDependent($dependent1));
    assertFalse($subject.isTransitiveDependent($sdus1.$dependent));
    assertFalse($subject.isTransitiveDependent($sdus2.$dependent));
    assertFalse($subject.isTransitiveDependent($dependent2));
    $dependent1.updateMaximumRootUpdateSourceDistanceUp(MRUSD);
    $subject.addDependent($dependent1);
    assertTrue($subject.isTransitiveDependent($dependent1));
    assertFalse($subject.isTransitiveDependent($sdus1.$dependent));
    assertFalse($subject.isTransitiveDependent($sdus2.$dependent));
    assertFalse($subject.isTransitiveDependent($dependent2));
    $sdus1.$dependent.addUpdateSource($subject);
    assertTrue($subject.isTransitiveDependent($dependent1));
    assertTrue($subject.isTransitiveDependent($sdus1.$dependent));
    assertFalse($subject.isTransitiveDependent($sdus2.$dependent));
    assertFalse($subject.isTransitiveDependent($dependent2));
    $sdus2.$dependent.addUpdateSource($sdus1);
    assertTrue($subject.isTransitiveDependent($dependent1));
    assertTrue($subject.isTransitiveDependent($sdus1.$dependent));
    assertTrue($subject.isTransitiveDependent($sdus2.$dependent));
    assertFalse($subject.isTransitiveDependent($dependent2));
    $dependent2.updateMaximumRootUpdateSourceDistanceUp(2);
    $subject.addDependent($dependent2);
    assertTrue($subject.isTransitiveDependent($dependent1));
    assertTrue($subject.isTransitiveDependent($sdus1.$dependent));
    assertTrue($subject.isTransitiveDependent($sdus2.$dependent));
    assertTrue($subject.isTransitiveDependent($dependent2));
  }

}

