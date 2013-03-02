package org.watt.apps.TripItConversions;

import java.util.Collection;

import org.apache.commons.collections.buffer.CircularFifoBuffer;

public class MyCircularFifoBuffer extends CircularFifoBuffer {

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String out = "";
		for(Object o: this.toArray()){
			out += o.toString();
		}
		return out;
	}

	public MyCircularFifoBuffer() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MyCircularFifoBuffer(Collection coll) {
		super(coll);
		// TODO Auto-generated constructor stub
	}

	public MyCircularFifoBuffer(int size) {
		super(size);
		// TODO Auto-generated constructor stub
	}
	

}
