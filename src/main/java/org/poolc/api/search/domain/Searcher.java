package org.poolc.api.search.domain;

import org.poolc.api.search.exception.DeleteUnsupportedException;
import org.poolc.api.search.exception.InsertUnsupportedException;
import org.poolc.api.search.exception.SearchUnsupportedException;
import org.poolc.api.search.exception.UpdateUnsupportedException;

public abstract class Searcher {
    public Object search(Object searchElement) {
        throw new SearchUnsupportedException(String.format("Searcher name: %s", this.getClass().getSimpleName()));
    }

    public void insert(Object insertElement) {
        throw new InsertUnsupportedException(String.format("Searcher name: %s", this.getClass().getSimpleName()));
    }

    public void update(Object updateElement) {
        throw new UpdateUnsupportedException(String.format("Searcher name: %s", this.getClass().getSimpleName()));
    }

    public void delete(Object deleteElement) {
        throw new DeleteUnsupportedException(String.format("Searcher name: %s", this.getClass().getSimpleName()));
    }
}
