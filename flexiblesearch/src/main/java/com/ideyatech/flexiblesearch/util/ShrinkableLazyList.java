package com.ideyatech.flexiblesearch.util;

import org.apache.commons.collections.Factory;
import org.apache.commons.collections.list.LazyList;

import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: neilnamoro
 * Date: 9/10/13
 * Time: 3:41 PM
 *
 * Custom lazy list class to handle deletion of items in
 * a lazy list. This is used in dynamic binding collections
 * from JSP to Java.
 */
public class ShrinkableLazyList extends LazyList{
    private static final long serialVersionUID = -3982796912390050981L;

    protected ShrinkableLazyList(List list, Factory factory) {
        super(list, factory);
    }

    /**
     * Decorates list with shrinkable lazy list.
     */
    public static List decorate(List list, Factory factory) {
        return new ShrinkableLazyList(list, factory);
    }

    public void shrink() {
        for (Iterator i = getList().iterator(); i.hasNext();)
            if (i.next() == null)
                i.remove();
    }

    @Override
    public Iterator iterator() {
        shrink();
        return super.iterator();
    }
}
