package org.beedraz.semantics_II;

import java.util.Map;
import java.util.Set;

public interface Dependent {

    public Beed<?> getDependentUpdateSource();

    public Set<Beed<?>> getUpdateSources();

    public Set<Beed<?>> getUpdateSourcesTransitiveClosure();

    public void addUpdateSource(Beed<?> updateSource);

    public void removeUpdateSource(Beed<?> updateSource);

    public Set<Dependent> getDependents();

    public void fireEvent(Event event);

    public Event update(Map<? extends Beed<?>, Event> events, Edit<?> edit);

    public void updateMaximumRootUpdateSourceDistanceUp(int newSourceMROSD);

    public void updateMaximumRootUpdateSourceDistanceDown(int oldSourceMROSD);

    public int getMaximumRootUpdateSourceDistance();

}
