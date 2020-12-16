package de.bensoft.bukkit.buku.config.impl;

import java.util.*;
import java.util.function.UnaryOperator;

/**
 * Created by CUSTDEV3 on 03/11/2020.
 */
public class BukuConfigList<T> extends ArrayList<T> {

    private final List<T> delegate;
    private final ConfigProxy configProxy;

    public BukuConfigList(final List<T> delegate,
                          final ConfigProxy configProxy) {
        this.delegate = delegate;
        this.configProxy = configProxy;
    }

    @Override
    public boolean add(T t) {
        final boolean res = delegate.add((T)configProxy.proxyObject(t));
        configProxy.writeToYaml();
        return res;
    }

    @Override
    public boolean remove(Object o) {
        final boolean res = delegate.remove(o);
        configProxy.writeToYaml();
        return res;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        final boolean res = delegate.addAll(c);
        configProxy.writeToYaml();
        return res;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        final boolean res = delegate.addAll(index, c);
        configProxy.writeToYaml();
        return res;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        final boolean res = delegate.removeAll(c);
        configProxy.writeToYaml();
        return res;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        final boolean res = delegate.retainAll(c);
        configProxy.writeToYaml();
        return res;
    }

    @Override
    public void replaceAll(UnaryOperator<T> operator) {
        delegate.replaceAll(operator);
        configProxy.writeToYaml();
    }

    @Override
    public void sort(Comparator<? super T> c) {
        delegate.sort(c);
        configProxy.writeToYaml();
    }

    @Override
    public void clear() {
        delegate.clear();
        configProxy.writeToYaml();
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        final List<T> ts = delegate.subList(fromIndex, toIndex);
        configProxy.writeToYaml();
        return ts;
    }

    @Override
    public T set(int index, T element) {
        final T res = delegate.set(index, element);
        configProxy.writeToYaml();
        return res;
    }

    @Override
    public void add(int index, T element) {
        delegate.add(index, element);
        configProxy.writeToYaml();
    }

    @Override
    public T remove(int index) {
        final T remove = delegate.remove(index);
        configProxy.writeToYaml();
        return remove;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        final boolean res = delegate.containsAll(c);
        configProxy.writeToYaml();
        return res;
    }


    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return delegate.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return delegate.iterator();
    }

    @Override
    public Object[] toArray() {
        return delegate.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return delegate.toArray(a);
    }

    @Override
    public boolean equals(Object o) {
        return delegate.equals(o);
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public T get(int index) {
        return delegate.get(index);
    }

    @Override
    public int indexOf(Object o) {
        return delegate.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return delegate.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return delegate.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return delegate.listIterator(index);
    }

    @Override
    public Spliterator<T> spliterator() {
        return delegate.spliterator();
    }

}
