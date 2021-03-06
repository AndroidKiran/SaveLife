package com.donate.savelife.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ravi on 25/11/16.
 */

public class UniqueList<T> extends ArrayList<T> {

    private static final long serialVersionUID = 1L;

    /**
     * Unique elements SET
     */
    private final Set<T> set = new HashSet();

    /**
     * Used by addAll methods
     */
    private Collection<T> addUnique(Collection<? extends T> col) {
        Collection<T> unique = new ArrayList();
        for (T e : col) {
            if (set.add(e)) unique.add(e);
        }
        return unique;
    }

    @Override
    public boolean add(T e) {
        return set.add(e) ? super.add(e) : false;
    }

    @Override
    public boolean addAll(Collection<? extends T> col) {
        return super.addAll(addUnique(col));
    }

    @Override
    public void add(int index, T e) {
        if (set.add(e)) super.add(index, e);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> col) {
        return super.addAll(index, addUnique(col));
    }
}