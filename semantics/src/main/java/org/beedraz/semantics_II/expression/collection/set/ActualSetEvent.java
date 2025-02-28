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

package org.beedraz.semantics_II.expression.collection.set;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.beedraz.semantics_II.Beed;
import org.beedraz.semantics_II.CompoundEdit;
import org.beedraz.semantics_II.Edit;
import org.beedraz.semantics_II.expression.collection.AbstractCollectionEvent;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * Event that notifies of changes in an actual {@link SetBeed}.
 *
 * @author Jan Dockx
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public final class ActualSetEvent<_Element_>
    extends AbstractCollectionEvent<_Element_, Set<_Element_>>
    implements SetEvent<_Element_> {

  /**
   * @pre  source != null;
   * @pre  edit != null;
   * @pre  (edit.getState() == DONE) || (edit.getState() == UNDONE);
   *
   * @post getSource() == source;
   * @post getEdit() == edit;
   * @post getEditState() == edit.getState();
   * @post addedElements != null
   *           ? getAddedElements().equals(addedElements)
   *           : getAddedElements().isEmpty();
   * @post removedElements != null
   *           ? getRemovedElements().equals(removedElements)
   *           : getRemovedElements().isEmpty();
   */
  public ActualSetEvent(SetBeed<_Element_, ?> source,
                        Set<_Element_> addedElements,
                        Set<_Element_> removedElements,
                        Edit<?> edit) {
    super(source,
          addedElements == null ? Collections.<_Element_>emptySet() : new HashSet<_Element_>(addedElements),
          removedElements == null ? Collections.<_Element_>emptySet() : new HashSet<_Element_>(removedElements),
          edit);
  }

  @Override
  protected Set<_Element_> unmodifiable(Set<_Element_> c) {
    return Collections.unmodifiableSet(c);
  }

  /**
   * @note <p><strong>This implementation is added to work around a compiler problem.</strong></p>
   *       <ol>
   *         <li>Take tag {@code development-24} from the respository. Compile with eclipse
   *           (v3.2.1) via Project/Clean... Run all unit tests. All tests pass.</li>
   *         <li>Turn to your terminal, and run the tests with Maven 2 (<kbd>mvn test</kbd>).
   *           Nothing is compiled (the class files generated by eclipse are recent enough),
   *           and all tests pass.</li>
   *         <li>Clean, compile and test with Maven 2 (on Mac OS X with all (Java) updates
   *           applied; <kbd>java -version<kbd> says {@code build 1.5.0_07-87}): do
   *           <kbd>mvn clean test</kbd>. You get 25 errors!</li>
   *         <li>Turn to eclipse, and run all unit tests there again: you get 25 errors!
   *           on inspection, the error turns out to be a {@link AbstractMethodError}.</li>
   *       </ol>
   *       <p><strong>There is obviously a difference in the byte code generated by the 2
   *         compilers.</strong></p>
   *       <p>What should happen is that an implementation for this method is injected
   *         from {@link AbstractCollectionEvent}, although the method is redefined in
   *         {@link SetEvent} with a stronger return type. It <q>magically matches</q>.
   *         This does work with the eclipse compiler. The compiler used by Maven
   *         doesn't do this correctly, <strong>but there are no compilation errors</strong>.
   *         Obviously, it is that compiler that is at fault.</p>
   *       <p>The workaround is to explicitly do the injection, what is what we do here.</p>
   *       <p>It is unclear whether this is a problem in {@code javac} in general, or only
   *         in this version, or in the Mac OS X version.</p>
   *       <p>The problem was observed with {@code ActualSetEvent}. The fix is applied on all
   *         actual events of that hierarchy.</p>
   *
   * @todo Remove this method and this comment when the problem is solved.
   *
   * @basic
   */
  @Override
  public final Set<_Element_> getAddedElements() {
    return super.getAddedElements();
  }

  /**
   * @note <p><strong>This implementation is added to work around a compiler problem.</strong></p>
   *       <ol>
   *         <li>Take tag {@code development-24} from the respository. Compile with eclipse
   *           (v3.2.1) via Project/Clean... Run all unit tests. All tests pass.</li>
   *         <li>Turn to your terminal, and run the tests with Maven 2 (<kbd>mvn test</kbd>).
   *           Nothing is compiled (the class files generated by eclipse are recent enough),
   *           and all tests pass.</li>
   *         <li>Clean, compile and test with Maven 2 (on Mac OS X with all (Java) updates
   *           applied; <kbd>java -version<kbd> says {@code build 1.5.0_07-87}): do
   *           <kbd>mvn clean test</kbd>. You get 25 errors!</li>
   *         <li>Turn to eclipse, and run all unit tests there again: you get 25 errors!
   *           on inspection, the error turns out to be a {@link AbstractMethodError}.</li>
   *       </ol>
   *       <p><strong>There is obviously a difference in the byte code generated by the 2
   *         compilers.</strong></p>
   *       <p>What should happen is that an implementation for this method is injected
   *         from {@link AbstractCollectionEvent}, although the method is redefined in
   *         {@link SetEvent} with a stronger return type. It <q>magically matches</q>.
   *         This does work with the eclipse compiler. The compiler used by Maven
   *         doesn't do this correctly, <strong>but there are no compilation errors</strong>.
   *         Obviously, it is that compiler that is at fault.</p>
   *       <p>The workaround is to explicitly do the injection, what is what we do here.</p>
   *       <p>It is unclear whether this is a problem in {@code javac} in general, or only
   *         in this version, or in the Mac OS X version.</p>
   *       <p>The problem was observed with {@code ActualSetEvent}. The fix is applied on all
   *         actual events of that hierarchy.</p>
   *
   * @todo Remove this method and this comment when the problem is solved.
   *
   * @basic
   */
  @Override
  public final Set<_Element_> getRemovedElements() {
    return super.getRemovedElements();
  }

  @Override
  protected ActualSetEvent<_Element_> createCombinedEvent(Beed<?> source,
                                                          Set<_Element_> added,
                                                          Set<_Element_> removed,
                                                          CompoundEdit<?, ?> compoundEdit) {
    return new ActualSetEvent<_Element_>((SetBeed<_Element_, ?>)source, added, removed, compoundEdit);
  }

  @Override
  protected final Set<_Element_> freshCopy(Set<_Element_> c) {
    assert c != null;
    return new HashSet<_Element_>(c);
  }

}

