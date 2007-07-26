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

package org.beedraz.semantics_II.expression;


import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.Beed;
import org.beedraz.semantics_II.Event;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * <p>Beeds to be used as <dfn>properties</dfn> (<dfn>attributes</dfn> and
 *   <dfn>associations</dfn>) or <dfn>derived state</dfn> (reified inspectors).</p>
 * <p><dfn>Expression beeds</dfn> can be used as <dfn>active properties</dfn>
 *   with {@code BeanBeeds}, or, in general, with {@link org.beedraz.semantics_II.aggregate.AggregateBeed AggregateBeeds}.
 *   If expression beeds play the role of <dfn>active properties</dfn>
 *   of an {@link org.beedraz.semantics_II.aggregate.AggregateBeed}, the user might decide that
 *   a change in the property consitutes a change in the aggregate as a whole.
 *   Thus, if the property beed changes, the aggregate should send events
 *   and update its dependents too. To do this, we need to register the
 *   property beed with the aggregate by calling
 *   {@link org.beedraz.semantics_II.aggregate.AggregateBeed#registerAggregateElement(org.beedraz.semantics_II.Beed)},
 *   which makes the argument an update source,
 *   with the property beed as argument, e.g., with the following idiom:</p>
 * <pre>
 * public class <var>MyBean</var> extends AbstractBeanBeed {
 *
 *   <var>...</var>
 *
 *   public final <var>APropertyBeed</var> <var>myProperty</var> = new <var>APropertyBeed</var>();
 *
 *   {
 *     registerAggregateElement(<var>myProperty</var>);
 *   }
 *
 *   <var>...</var>
 *
 * }
 * </pre>
 * <p>To make this easier, expression beeds can offer 2 constructors. First, the normal
 *   default constructor, so that the above idiom can be used. Secondly, a constructor
 *   that takes an {@link org.beedraz.semantics_II.aggregate.AggregateBeed} as argument, and simply calls
 *   {@link AggregateBeed#registerAggregateElement(org.beedraz.semantics_II.Beed)}
 *   for the user. The idiom then simplifies too:</p>
 * <pre>
 * public class <var>MyBean</var> extends AbstractBeanBeed {
 *
 *   <var>...</var>
 *
 *   public final <var>APropertyBeed</var> <var>myProperty</var> = new <var>APropertyBeed</var>(this);
 *
 *   <var>...</var>
 *
 * }
 * </pre>
 * <p>Some expression beeds might store the reference to the aggregate beed, but in general, there
 *   is no obligation for expressions beeds to do so. If the reference is stored, it should be
 *   referred to as the <dfn>owner</dfn>.</p>
 * <p><dfn>Editable expression beeds</dfn> must however be registered with an {@link AggregateBeed}.
 *   Editable beeds should not offer a default constructor, but each constructor should at least
 *   register the editable beed with an {@link AggregateBeed} argument.</p>
 * <p>In general, when used as properties, <em>public</em> properties <em>should</em> be registered
 *   as aggregate elements, and non-public properties (e.g., intermediate steps in a
 *   calculation), should not. Editable expressions make only sense if they are public,
 *   and hence they should always be registered as aggregate element.</p>
 * <p>This interface is not really necessary for code-related reasons, but is provided
 *   for cleanness-of-tought.</p>
 *
 * @author Jan Dockx
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public interface ExpressionBeed<_Event_ extends Event>
    extends Beed<_Event_> {

  // NOP; see AbstractExpressionBeed

}
