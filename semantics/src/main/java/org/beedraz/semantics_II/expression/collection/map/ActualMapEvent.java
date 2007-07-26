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

package org.beedraz.semantics_II.expression.collection.map;


import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.beedraz.semantics_II.AbstractEvent;
import org.beedraz.semantics_II.CompoundEdit;
import org.beedraz.semantics_II.Edit;
import org.beedraz.semantics_II.Event;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * <p>Event that notifies of changes in an actual {@link MapBeed}.</p>
 * <p>Entries in the {@link #getAddedElements()} and {@link #getRemovedElements()}
 *   can mean one of 3 things:</p>
 * <ul>
 *   <li>If the key only appears in {@link #getRemovedElements()}, it means that the entry
 *     is removed from the event source.</li>
 *   <li>If the key only appears in {@link #getAddedElements()}, it means that the entry
 *     is added to the event source.</li>
 *   <li>If the key only appears in both {@link #getRemovedElements()} and {@link #getAddedElements()},
 *     it means that the value of the entry is replaced in the event source. In this
 *     case, the 2 values cannot be {@link Object#equals(Object) equal}.</li>
 * </ul>
 *
 * @author Nele Smeets
 * @author Peopleware n.v.
 *
 * @invar getAddedElements() != null;
 * @invar getRemovedElements() != null;
 * @invar ; if getAddedElements() and getRemovedElements() have entries with the
 *        same key, the value must be different (this signals a replacement)
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State: Exp $",
         tag      = "$Name:  $")
public final class ActualMapEvent<_Key_, _Value_>
    extends AbstractEvent
    implements MapEvent<_Key_, _Value_> {

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
  public ActualMapEvent(MapBeed<_Key_, _Value_, ?> source,
                        Map<_Key_, _Value_> addedElements,
                        Map<_Key_, _Value_> removedElements,
                        Edit<?> edit) {
    super(source, edit);
    $addedElements = addedElements == null
                       ? Collections.<_Key_, _Value_>emptyMap()
                       : new HashMap<_Key_, _Value_>(addedElements);
    $removedElements = removedElements == null
                         ? Collections.<_Key_, _Value_>emptyMap()
                         : new HashMap<_Key_, _Value_>(removedElements);
  }

  /**
   * @basic
   */
  public Map<_Key_, _Value_> getAddedElements() {
    return Collections.unmodifiableMap($addedElements);
  }

 /**
   * @invar $addedElements != null;
   */
  private final Map<_Key_, _Value_> $addedElements;

  /**
   * @basic
   */
  public Map<_Key_, _Value_> getRemovedElements() {
    return Collections.unmodifiableMap($removedElements);
  }

  /**
   * @invar $removedElements != null;
   */
  private final Map<_Key_, _Value_> $removedElements;

  /**
   * <p>Combine this event with {@code other} into a new combined
   *   {@code ActualMapEvent}. The logic here is quite difficult to grasp.</p>
   * <p>The following table shows all possible combinations, and the result
   *   for the combined event:</p>
   * <table>
   *   <tr>
   *     <th colspan="3">{@code this}</th>
   *     <th colspan="3">{@code other}</th>
   *     <th colspan="3">{@code combined}</th>
   *   </tr>
   *   <tr>
   *     <th>{@code removed}</th>
   *     <th>{@code added}</th>
   *     <th></th>
   *     <th>{@code removed}</th>
   *     <th>{@code added}</th>
   *     <th></th>
   *     <th></th>
   *     <th>{@code removed}</th>
   *     <th>{@code added}</th>
   *   </tr>
   *   <tr>
   *     <td>-</td>
   *     <td>-</td>
   *     <td>nop</td>
   *     <td>-</td>
   *     <td>-</td>
   *     <td>nop</td>
   *     <td>nop</td>
   *     <td>-</td>
   *     <td>-</td>
   *   </tr>
   *   <tr>
   *     <td>-</td>
   *     <td>-</td>
   *     <td>nop</td>
   *     <td>-</td>
   *     <td>x</td>
   *     <td>added</td>
   *     <td>added</td>
   *     <td>-</td>
   *     <td>x</td>
   *   </tr>
   *   <tr>
   *     <td>-</td>
   *     <td>-</td>
   *     <td>nop</td>
   *     <td>x</td>
   *     <td>-</td>
   *     <td>removed</td>
   *     <td>removed</td>
   *     <td>x</td>
   *     <td>-</td>
   *   </tr>
   *   <tr>
   *     <td>-</td>
   *     <td>-</td>
   *     <td>nop</td>
   *     <td>x</td>
   *     <td>x</td>
   *     <td>changed</td>
   *     <td>changed</td>
   *     <td>x</td>
   *     <td>x</td>
   *   </tr>
   *   <tr>
   *     <td>-</td>
   *     <td>x</td>
   *     <td>added</td>
   *     <td>-</td>
   *     <td>-</td>
   *     <td>nop</td>
   *     <td>added</td>
   *     <td>-</td>
   *     <td>x</td>
   *   </tr>
   *   <tr>
   *     <td>-</td>
   *     <td>x</td>
   *     <td>added</td>
   *     <td>-</td>
   *     <td>x</td>
   *     <td>added</td>
   *     <td>impossible</td>
   *     <td><br></td>
   *     <td>#</td>
   *   </tr>
   *   <tr>
   *     <td>-</td>
   *     <td>x</td>
   *     <td>added</td>
   *     <td>x</td>
   *     <td>-</td>
   *     <td>removed</td>
   *     <td>nop (same value)</td>
   *     <td>-</td>
   *     <td>-</td>
   *   </tr>
   *   <tr>
   *     <td>-</td>
   *     <td>x</td>
   *     <td>added</td>
   *     <td>x</td>
   *     <td>x</td>
   *     <td>changed</td>
   *     <td>added (same value)</td>
   *     <td>-</td>
   *     <td>x</td>
   *   </tr>
   *   <tr>
   *     <td>x</td>
   *     <td>-</td>
   *     <td>removed</td>
   *     <td>-</td>
   *     <td>-</td>
   *     <td>nop</td>
   *     <td>removed</td>
   *     <td>x</td>
   *     <td>-</td>
   *   </tr>
   *   <tr>
   *     <td>x</td>
   *     <td>-</td>
   *     <td>removed</td>
   *     <td>-</td>
   *     <td>x</td>
   *     <td>added</td>
   *     <td>nop or change</td>
   *     <td>?</td>
   *     <td>?</td>
   *   </tr>
   *   <tr>
   *     <td>x</td>
   *     <td>-</td>
   *     <td>removed</td>
   *     <td>x</td>
   *     <td>-</td>
   *     <td>removed</td>
   *     <td>impossible</td>
   *     <td><br></td>
   *     <td>#</td>
   *   </tr>
   *   <tr>
   *     <td>x</td>
   *     <td>-</td>
   *     <td>removed</td>
   *     <td>x</td>
   *     <td>x</td>
   *     <td>changed</td>
   *     <td>impossible</td>
   *     <td><br></td>
   *     <td>#</td>
   *   </tr>
   *   <tr>
   *     <td>x</td>
   *     <td>x</td>
   *     <td>changed</td>
   *     <td>-</td>
   *     <td>-</td>
   *     <td>nop</td>
   *     <td>changed</td>
   *     <td>x</td>
   *     <td>x</td>
   *   </tr>
   *   <tr>
   *     <td>x</td>
   *     <td>x</td>
   *     <td>changed</td>
   *     <td>-</td>
   *     <td>x</td>
   *     <td>added</td>
   *     <td>impossible</td>
   *     <td><br></td>
   *     <td>#</td>
   *   </tr>
   *   <tr>
   *     <td>x</td>
   *     <td>x</td>
   *     <td>changed</td>
   *     <td>x</td>
   *     <td>-</td>
   *     <td>removed</td>
   *     <td>removed (same value)</td>
   *     <td>x</td>
   *     <td>-</td>
   *   </tr>
   *   <tr>
   *     <td>x</td>
   *     <td>x</td>
   *     <td>changed</td>
   *     <td>x</td>
   *     <td>x</td>
   *     <td>changed</td>
   *     <td>changed (same value)</td>
   *     <td>x</td>
   *     <td>x</td>
   *   </tr>
   * </table>
   * <p>The following table reorganizes the entries above that are relevant
   *   for the combined added elements.</p>
   * <table>
   *   <tr>
   *     <th colspan="3">{@code this}</th>
   *     <th colspan="3">{@code other}</th>
   *     <th>{@code combined}</th>
   *   </tr>
   *   <tr>
   *     <th>{@code removed}</th>
   *     <th>{@code added}</th>
   *     <th></th>
   *     <th>{@code removed}</th>
   *     <th>{@code added}</th>
   *     <th></th>
   *     <th>{@code added}</th>
   *   </tr>
   *   <tr>
   *     <td>-</td>
   *     <td>-</td>
   *     <td>nop</td>
   *     <td>-</td>
   *     <td>x</td>
   *     <td>added</td>
   *     <td>x</td>
   *   </tr>
   *   <tr>
   *     <td>-</td>
   *     <td>-</td>
   *     <td>nop</td>
   *     <td>x</td>
   *     <td>x</td>
   *     <td>changed</td>
   *     <td>x</td>
   *   </tr>
   *   <tr>
   *     <td>-</td>
   *     <td>x</td>
   *     <td>added</td>
   *     <td>-</td>
   *     <td>x</td>
   *     <td>added</td>
   *     <td>#</td>
   *   </tr>
   *   <tr>
   *     <td>-</td>
   *     <td>x</td>
   *     <td>added</td>
   *     <td>x</td>
   *     <td>x</td>
   *     <td>changed</td>
   *     <td>x</td>
   *   </tr>
   *   <tr>
   *     <td>x</td>
   *     <td>-</td>
   *     <td>removed</td>
   *     <td>-</td>
   *     <td>x</td>
   *     <td>added</td>
   *     <td>?</td>
   *   </tr>
   *   <tr>
   *     <td>x</td>
   *     <td>-</td>
   *     <td>removed</td>
   *     <td>x</td>
   *     <td>x</td>
   *     <td>changed</td>
   *     <td>#</td>
   *   </tr>
   *   <tr>
   *     <td>x</td>
   *     <td>x</td>
   *     <td>changed</td>
   *     <td>-</td>
   *     <td>x</td>
   *     <td>added</td>
   *     <td>#</td>
   *   </tr>
   *   <tr>
   *     <td>x</td>
   *     <td>x</td>
   *     <td>changed</td>
   *     <td>x</td>
   *     <td>x</td>
   *     <td>changed</td>
   *     <td>x</td>
   *   </tr>
   *   <tr>
   *     <td>-</td>
   *     <td>x</td>
   *     <td>added</td>
   *     <td>-</td>
   *     <td>-</td>
   *     <td>nop</td>
   *     <td>x</td>
   *   </tr>
   *   <tr>
   *     <td>x</td>
   *     <td>x</td>
   *     <td>changed</td>
   *     <td>-</td>
   *     <td>-</td>
   *     <td>nop</td>
   *     <td>x</td>
   *   </tr>
   *   <tr>
   *     <td>-</td>
   *     <td>x</td>
   *     <td>added</td>
   *     <td>x</td>
   *     <td>-</td>
   *     <td>removed</td>
   *     <td>-</td>
   *   </tr>
   *   <tr>
   *     <td>x</td>
   *     <td>x</td>
   *     <td>changed</td>
   *     <td>x</td>
   *     <td>-</td>
   *     <td>removed</td>
   *     <td>-</td>
   *   </tr>
   * </table>
   * <p>We now see an algorithm:</p>
   * <ul>
   *   <li>For the entries {@code added2} in {@code other.}{@link #getAddedElements()},
   *     check whether there is an entry {@code removed1} with the key from {@code added2} in
   *     {@code this.}{@link #getRemovedElements()} and there is no such entry in
   *     {@code this.}{@link #getAddedElements()}
   *     <ul>
   *       <li>If so, if the value in {@code added2} and {@code removed1} are
   *         not {@link Object#equals(Object) equal}, add {@code added2} to the combined
   *         added elements (else, do nothing).</li>
   *       <li>If not, add {@code added2} to the combined added elements.</li>
   *     </ul>
   *     Thus, we need to add {@code added2} to the combined added elements if:
   *     <ul>
   *       <li>{@code this.}{@link #getRemovedElements()} has no entry with the key found
   *         in {@code added2}, or if</li>
   *       <li>{@code this.}{@link #getAddedElements()} does have an entry with
   *         the key found in {@code added2}, or</li>
   *       <li>(consecutive) if the value found in {@code this.}{@link #getRemovedElements()}
   *         for that key is different from the value in {@code added2}.</li>
   *     </ul>
   *   </li>
   *   <li>For the entries {@code added1} in {@code this.}{@link #getAddedElements()},
   *     for which there is no entry with that key in {@code other.}{@link #getAddedElements()},
   *     nor in {@code other.}{@link #getRemovedElements()}, add {@code added1} to the
   *     combined added elements.</li>
   * </ul>
   * <p>The following table reorganizes the entries above that are relevant
   *   for the combined removed elements.</p>
   * <table>
   *   <tr>
   *     <th colspan="3">{@code this}</th>
   *     <th colspan="3">{@code other}</th>
   *     <th>{@code combined}</th>
   *   </tr>
   *   <tr>
   *     <th>{@code removed}</th>
   *     <th>{@code added}</th>
   *     <th></th>
   *     <th>{@code removed}</th>
   *     <th>{@code added}</th>
   *     <th></th>
   *     <th>{@code removed}</th>
   *   </tr>
   *   <tr>
   *     <td>x</td>
   *     <td>-</td>
   *     <td>removed</td>
   *     <td>-</td>
   *     <td>-</td>
   *     <td>nop</td>
   *     <td>x</td>
   *   </tr>
   *   <tr>
   *     <td>x</td>
   *     <td>-</td>
   *     <td>removed</td>
   *     <td>-</td>
   *     <td>x</td>
   *     <td>added</td>
   *     <td>?</td>
   *   </tr>
   *   <tr>
   *     <td>x</td>
   *     <td>-</td>
   *     <td>removed</td>
   *     <td>x</td>
   *     <td>-</td>
   *     <td>removed</td>
   *     <td>#</td>
   *   </tr>
   *   <tr>
   *     <td>x</td>
   *     <td>-</td>
   *     <td>removed</td>
   *     <td>x</td>
   *     <td>x</td>
   *     <td>changed</td>
   *     <td>#</td>
   *   </tr>
   *   <tr>
   *     <td>x</td>
   *     <td>x</td>
   *     <td>changed</td>
   *     <td>-</td>
   *     <td>-</td>
   *     <td>nop</td>
   *     <td>x</td>
   *   </tr>
   *   <tr>
   *     <td>x</td>
   *     <td>x</td>
   *     <td>changed</td>
   *     <td>-</td>
   *     <td>x</td>
   *     <td>added</td>
   *     <td>#</td>
   *   </tr>
   *   <tr>
   *     <td>x</td>
   *     <td>x</td>
   *     <td>changed</td>
   *     <td>x</td>
   *     <td>-</td>
   *     <td>removed</td>
   *     <td>x</td>
   *   </tr>
   *   <tr>
   *     <td>x</td>
   *     <td>x</td>
   *     <td>changed</td>
   *     <td>x</td>
   *     <td>x</td>
   *     <td>changed</td>
   *     <td>x</td>
   *   </tr>
   *   <tr>
   *     <td>-</td>
   *     <td>-</td>
   *     <td>nop</td>
   *     <td>x</td>
   *     <td>-</td>
   *     <td>removed</td>
   *     <td>x</td>
   *   </tr>
   *   <tr>
   *     <td>-</td>
   *     <td>-</td>
   *     <td>nop</td>
   *     <td>x</td>
   *     <td>x</td>
   *     <td>changed</td>
   *     <td>x</td>
   *   </tr>
   *   <tr>
   *     <td>-</td>
   *     <td>x</td>
   *     <td>added</td>
   *     <td>x</td>
   *     <td>-</td>
   *     <td>removed</td>
   *     <td>-</td>
   *   </tr>
   *   <tr>
   *     <td>-</td>
   *     <td>x</td>
   *     <td>added</td>
   *     <td>x</td>
   *     <td>x</td>
   *     <td>changed</td>
   *     <td>-</td>
   *   </tr>
   * </table>
   * <p>We now see an algorithm:</p>
   * <ul>
   *   <li>
   *     For the entries {@code removed1} in {@code this.}{@link #getRemovedElements()},
   *     check whether there is an entry {@code added2} with the key from {@code removed1} in
   *     {@code other.}{@link #getAddedElements()} and there is no such entry in
   *     {@code other.}{@link #getRemovedElements()}
   *     <ul>
   *       <li>If so, if the value in {@code added2} and {@code removed1} are
   *         not {@link Object#equals(Object) equal}, add {@code removed1} to the combined
   *         removed elements (else, do nothing).</li>
   *       <li>If not, add {@code removed1} to the combined removed elements.</li>
   *     </ul>
   *     Thus, we need to add {@code removed1} to the combined removed elements if:
   *     <ul>
   *       <li>{@code other.}{@link #getAddedElements()} has no entry with the key found
   *         in {@code removed1}, or if</li>
   *       <li>{@code other.}{@link #getRemovedElements()} does have an entry with
   *         the key found in {@code removed1}, or</li>
   *       <li>(consecutive) if the value found in {@code other.}{@link #getAddedElements()}
   *         for that key is different from the value in {@code removed1}.</li>
   *     </ul>
   *   </li>
   *   <li>For the entries {@code removed2} in {@code other.}{@link #getRemovedElements()},
   *     for which there is no entry with that key in {@code this.}{@link #getRemovedElements()},
   *     nor in {@code this.}{@link #getAddedElements()}, add {@code removed2} to the
   *     combined removed elements.</li>
   * </ul>
   *
   * @note This method was very difficult to analyse (2+ days in trying to grasp what was needed).
   *       Implementers in maintenance mode beware!
   *
   * @mudo needs unit test
   */
  @Override
  protected final ActualMapEvent<_Key_, _Value_> createCombinedEvent(Event other, CompoundEdit<?, ?> edit) {
    ActualMapEvent<_Key_, _Value_> otherSE = (ActualMapEvent<_Key_, _Value_>)other;
    Map<_Key_, _Value_> otherRemovedElements = otherSE.getRemovedElements();
    Map<_Key_, _Value_> otherAddedElements = otherSE.getAddedElements();
    // combined add
    Map<_Key_, _Value_> combinedAdded = new HashMap<_Key_, _Value_>();
    for (Map.Entry<_Key_, _Value_> added2 : otherAddedElements.entrySet()) {
      _Key_ added2Key = added2.getKey();
      _Value_ added2Value = added2.getValue();
      _Value_ removed1Value = $removedElements.get(added2Key);
      if ((removed1Value == null) ||
          $addedElements.containsKey(added2Key) ||
          (! removed1Value.equals(added2Value))) {
        combinedAdded.put(added2Key, added2Value);
      }
    }
    for (Map.Entry<_Key_, _Value_> added1 : $addedElements.entrySet()) {
      _Key_ added1Key = added1.getKey();
      if ((! otherAddedElements.containsKey(added1Key)) &&
          (! otherRemovedElements.containsKey(added1Key))) {
        combinedAdded.put(added1Key, added1.getValue());
      }
    }
    // combined remove
    Map<_Key_, _Value_> combinedRemoved = new HashMap<_Key_, _Value_>();
    for (Map.Entry<_Key_, _Value_> removed1 : $removedElements.entrySet()) {
      _Key_ removed1Key = removed1.getKey();
      _Value_ removed1Value = removed1.getValue();
      _Value_ added2Value = otherAddedElements.get(removed1Key);
      if ((added2Value == null) ||
          otherRemovedElements.containsKey(removed1Key) ||
          (! added2Value.equals(removed1Value))) {
        combinedRemoved.put(removed1Key, removed1Value);
      }
    }
    for (Map.Entry<_Key_, _Value_> removed2 : otherRemovedElements.entrySet()) {
      _Key_ removed2Key = removed2.getKey();
      if ((! $removedElements.containsKey(removed2Key)) &&
          (! $addedElements.containsKey(removed2Key))) {
        combinedAdded.put(removed2Key, removed2.getValue());
      }
    }
    return new ActualMapEvent<_Key_, _Value_>((MapBeed<_Key_, _Value_, ?>)getSource(), combinedAdded, combinedRemoved, edit);
  }

  @Override
  protected String otherToStringInformation() {
    return super.otherToStringInformation() +
           ", added elements: " + getAddedElements() +
           ", removed elements: " + getRemovedElements();
  }

  @Override
  public void toString(StringBuffer sb, int level) {
    super.toString(sb, level);
    sb.append(indent(level + 1) + "added elements: " + getAddedElements() + "\n");
    sb.append(indent(level + 1) + "removed elements: " + getRemovedElements() + "\n");
  }

}

