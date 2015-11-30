package es.uvigo.esei.dgss.letta.jsf.util;

import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import es.uvigo.esei.dgss.letta.domain.entities.Event;

public abstract class LazyEventList extends LazyDataModel<Event> {

    private static final long serialVersionUID = 1L;
    private int count = 0;

    /**
     * Method that returns the internal count
     *
     * @return the size of the result query
     */
    public int getCount() {
        return count;
    }

    /**
     *
     * @param count
     *            Set the internal count variable to an external value
     */
    public void setCount(final int count) {
        this.count = count;
    }

    @Override
    public List<Event> load(final int first, final int pageSize,
            final String sortField, final SortOrder sortOrder,
            final Map<String, Object> filters) {
        List<Event> events;
        events = findEvent(first, pageSize);
        this.setRowCount(count);
        return events;
    }

    /**
     *
     * @param first
     *            Index from where to start the query
     * @param pageSize
     *            Number of elements
     * @return a list with the elements from @param from to @param pageSize of
     *         the datasets
     */
    public abstract List<Event> findEvent(int first, int pageSize);

}
