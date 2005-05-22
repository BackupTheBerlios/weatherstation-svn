package org.idanso.stationinfo.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class RangeList implements Set {

	public class RangeIterator implements Iterator {
		int current;
		
		public RangeIterator()
		{
			current=min;
		}

		public boolean hasNext() {
			return current<=max;
		}

		public Object next() {
			current+=1;
			return new Integer(current-1);
		}

		public void remove() {
			// TODO Auto-generated method stub

		}

	}

	private int min;
	private int max;

	public RangeList(int min,int max)
	{
		if (max>=min)
		{
			this.min=min;
			this.max=max;
		}
		else
		{
			throw new IllegalArgumentException("max<min");
		}
	}
	
	public int size() {
		return max-min+1;
	}

	public boolean isEmpty() {
		return false;
	}

	public boolean contains(Object o) {
		return false;
	}

	public Iterator iterator() {
		return new RangeIterator();
	}

	public Object[] toArray() {
		int size=size();
		Integer[] arr=new Integer[size];
		for(int i=0;i<size;++i)
		{
			arr[i]=new Integer(min+i);			
		}
		return arr;
	}

	public Object[] toArray(Object[] arg0) {
		return toArray();
	}


	public boolean removeAll(Collection c) {
		// TODO Auto-generated method stub
		return false;
	}

	public Object get(int index) {
		int num=min+index;
		if (num<=max)
			return new Integer(num);
		else
			return null;
	}


	public void add(int arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	public Object remove(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	public int indexOf(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int lastIndexOf(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean add(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean containsAll(Collection c) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean addAll(Collection c) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean retainAll(Collection c) {
		// TODO Auto-generated method stub
		return false;
	}

	public void clear() {
		// TODO Auto-generated method stub
		
	}

}
