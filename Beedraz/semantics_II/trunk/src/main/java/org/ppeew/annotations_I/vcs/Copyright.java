package org.ppeew.annotations_I.vcs;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotation for copyright information. By using this annotation,
 * the copyright information is available in the code.
 *
 * Usage pattern:
 * <pre>
 * ATCopyright(copyright={&quot;Jan Dockx&quot;, &quot;PeopleWare n.v.&quot;},
 *             from=&quot;2007&quot;,
 *             until=&quot;$Date$&quot;)
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
public @interface Copyright {

  /**
   * List of copyright holders of the contents (the authored expression) of the file.
   */
  String[] copyright();

  /**
   * The year of first publication of the contents (the authored expression) of the file.
   */
  String from();

  /**
   * The year of the most recent change of the contents (the authored expression) of the file.
   * It makes good sense to use the $Date$ tag here, which is supported by both
   * CVS and Subversion.
   */
  String until();

}