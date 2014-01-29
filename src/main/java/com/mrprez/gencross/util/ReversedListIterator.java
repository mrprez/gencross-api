package com.mrprez.gencross.util;

import java.util.List;
import java.util.ListIterator;

public class ReversedListIterator<E> implements ListIterator<E> {
	private ListIterator<E> listIterator;
	
	
	public ReversedListIterator(List<E> list) {
		super();
		this.listIterator = list.listIterator(list.size());
	}

	@Override
	public void add(E arg0) {
		listIterator.add(arg0);
	}

	@Override
	public boolean hasNext() {
		return listIterator.hasPrevious();
	}

	@Override
	public boolean hasPrevious() {
		return listIterator.hasNext();
	}

	@Override
	public E next() {
		return listIterator.previous();
	}

	@Override
	public int nextIndex() {
		return listIterator.previousIndex();
	}

	@Override
	public E previous() {
		return listIterator.next();
	}

	@Override
	public int previousIndex() {
		return listIterator.nextIndex();
	}

	@Override
	public void remove() {
		listIterator.remove();
	}

	@Override
	public void set(E arg0) {
		listIterator.set(arg0);
	}

}
