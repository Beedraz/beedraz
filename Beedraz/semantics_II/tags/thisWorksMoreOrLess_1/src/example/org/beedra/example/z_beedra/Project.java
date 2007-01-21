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

package org.beedra.example.z_beedra;


import org.beedra_II.bean.AbstractBeanBeed;
import org.beedra_II.property.PropertyBeedSelector;
import org.beedra_II.property.association.BidirToManyPBeed;
import org.beedra_II.property.simple.StraightSimplePDB;


/**
 * To be added
 *
 * validation
 * civilization
 * propagation
 */
public class Project extends AbstractBeanBeed {

  public final StraightSimplePDB<String> name =
      new StraightSimplePDB<String>(this);

  public final BidirToManyPBeed<Project, Task> tasks =
      new BidirToManyPBeed<Project, Task>(this);

  public final static PropertyBeedSelector<Project, BidirToManyPBeed<Project, Task>> tasksSelector =
    new PropertyBeedSelector<Project, BidirToManyPBeed<Project, Task>>() {

            public BidirToManyPBeed<Project, Task> getPropertyBeed(Project owner) {
              assert owner != null;
              return owner.tasks;
            }
        };

}

