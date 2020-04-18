package otus;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class DIYarrayList<E> implements List<E> {

    @Override
    public String toString() {
        return "DIYarrayList [elements=" + Arrays.toString(elements) + ", elementsCount=" + elementsCount + "]";
    }

    private Object[] elements = {};
    private int elementsCount = 0;

    @Override
    public boolean addAll(Collection<? extends E> c) {
        Object[] a = c.toArray();
        int additionalLength = a.length;
        if (!adaptLength(additionalLength)) {
            return false;
        }
        System.arraycopy(a, 0, elements, elementsCount, additionalLength);
        elementsCount += additionalLength;
        return elementsCount > 0;
    }

    @Override
    public E get(int index) {
        if (index >= elementsCount)
            throw new IndexOutOfBoundsException("Index " + index + " is out of range " + elementsCount + " elements");
        return (E) elements[index];
    }

    @Override
    public E set(int index, E element) {
        E oldValue = get(index);
        elements[index] = element;
        return oldValue;
    }

    @Override
    public int size() {
        return elementsCount;
    }

    @Override
    public boolean isEmpty() {
        return elementsCount == 0;
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elements, elementsCount);
    }

    @Override
    public boolean add(E e) {
        if (!adaptLength(1)) {
            return false;
        }
        elements[elementsCount++] = e;
        return true;
    }

    private boolean adaptLength(int additionalLength) {
        int newLength = elementsCount + additionalLength;
        if (newLength >= Integer.MAX_VALUE) {
            System.err.println("Final array capacity exceeds Integer.MAX_VALUE");
            return false;
        }
        if (newLength > elements.length) {
            elements = Arrays.copyOf(elements, newLength);
        }
        return true;
    }

    @Override
    public ListIterator<E> listIterator() {
        return new MyIterator();
    }

    private class MyIterator implements ListIterator<E> {

        private int nextIndex = 0;
        private int currentIndex = -1;

        @Override
        public boolean hasNext() {
            return nextIndex != elementsCount;
        }

        @Override
        public E next() {
            try {
                E next = get(nextIndex);
                currentIndex = nextIndex;
                nextIndex++;
                return next;
            } catch (IndexOutOfBoundsException e) {
                throw new NoSuchElementException();
            }
        }

        @Override
        public boolean hasPrevious() {
            return nextIndex != 0;
        }

        @Override
        public E previous() {
            try {
                int temp = nextIndex - 1;
                E prev = get(temp);
                // alternating calls to next and previous will return the same element repeatedly
                currentIndex = nextIndex = temp;
                return prev;
            } catch (IndexOutOfBoundsException e) {
                throw new NoSuchElementException();
            }
        }

        @Override
        public int nextIndex() {
            return nextIndex;
        }

        @Override
        public int previousIndex() {
            return nextIndex - 1;
        }

        @Override
        public void set(E e) {
            DIYarrayList.this.set(currentIndex, e);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(E e) {
            throw new UnsupportedOperationException();
        }

    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

}
