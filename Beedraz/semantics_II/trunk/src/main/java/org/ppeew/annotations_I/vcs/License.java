package org.ppeew.annotations_I.vcs;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotation for license information. By using this annotation,
 * the license information is available in the code.
 *
 * Usage pattern:
 * <pre>
 * ATLicense(license="Apache2",
 *           notice={"Licensed under the Apache License, Version 2.0 (the \"License\")",
 *                   "you may not use this file except in compliance with the License.",
 *                   "You may obtain a copy of the License at",
 *                   "    http://www.apache.org/licenses/LICENSE-2.0",
 *                   "Unless required by applicable law or agreed to in writing, software",
 *                   "distributed under the License is distributed on an \"AS IS\" BASIS,",
 *                   "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.",
 *                   "See the License for the specific language governing permissions and",
 *                   "limitations under the License."})
 * public class ... {
 *  ...
 * }
 * </pre>
 *
 * @author    Jan Dockx
 */
@Copyright(copyright = {"Jan Dockx", "PeopleWare n.v."}, from = "2007", until = "$Revision$")
@License(license     = "Apache2",
         notice      = {"Licensed under the Apache License, Version 2.0 (the \"License\")",
                        "you may not use this file except in compliance with the License.",
                        "You may obtain a copy of the License at",
                        "    http://www.apache.org/licenses/LICENSE-2.0",
                        "Unless required by applicable law or agreed to in writing, software",
                        "distributed under the License is distributed on an \"AS IS\" BASIS,",
                        "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.",
                        "See the License for the specific language governing permissions and",
                        "limitations under the License."})
@CvsInfo(revision    = "$Revision$",
         date        = "$Date$",
         state       = "$State$",
         tag         = "$Name$")
@Documented
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface License {

  /**
   * Name of the license. This should be one of
   * <dl>
   *   <dt>Proprietary</dt>
   *   <dd>No license. The notice should be something like
   *     <blockquote>Copyright 2007, PeopleWare n.v. NO RIGHTS ARE GRANTED FOR
   *       THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING, TO SELECTED PARTIES.</blockquote></dd>
   *   <dt>Apache2</dt>
   *   <dd>The Apache License, version 2.
   *      See <a href="http://www.apache.org/licenses/LICENSE-2.0.html">http://www.apache.org/licenses/LICENSE-2.0.html</a>.
   *      The notice should be of the form mentioned in the
   *      <a href="http://www.apache.org/licenses/LICENSE-2.0.html#apply>license appendix</a>.</dd>
   * <dl>
   *
   * @note This list will be extended as necessary.
   */
  String license();

  /**
   * The license notice, that marks the source file as being distributed under a specific license.
   * Most Open Source licenses express what this notice should be. This property is an array,
   * to support multiple lines easily.
   */
  String[] notice();

}