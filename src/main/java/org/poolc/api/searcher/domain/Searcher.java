package org.poolc.api.searcher.domain;

import org.poolc.api.searcher.exception.DeleteUnsupportedException;
import org.poolc.api.searcher.exception.InsertUnsupportedException;
import org.poolc.api.searcher.exception.SearchUnsupportedException;
import org.poolc.api.searcher.exception.UpdateUnsupportedException;

public interface Searcher {
    default Object search(Object... args) {
        throw new SearchUnsupportedException(String.format("Searcher name: %s", this.getClass().getSimpleName()));
    }

    default void insert(Object jsonObject) {
        throw new InsertUnsupportedException(String.format("Searcher name: %s", this.getClass().getSimpleName()));
    }

    default void update(Object jsonObject) {
        throw new UpdateUnsupportedException(String.format("Searcher name: %s", this.getClass().getSimpleName()));
    }

    default void delete(Object jsonObject) {
        throw new DeleteUnsupportedException(String.format("Searcher name: %s", this.getClass().getSimpleName()));
    }
}
