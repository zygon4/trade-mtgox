/**
 * 
 */

package com.zygon.trade.mtgox.data.interpreter;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 *
 * @author zygon
 * 
 * Ultimately it would be nice to have a single moving average have the ability
 * to report several different averages.. for now we need to instantiate 
 * multiple.
 * 
 * This whole bitch is not thread safe..
 */
public class MovingAverage<T> {

    public static interface ValueProvider<T_IN> {
        public double getValue(T_IN in);
    }
    
    private final AbstractQueue<T> values;
    private final int maxValues;
    private final ValueProvider<T> valueProvider;

    public MovingAverage(int maxValues, ValueProvider<T> valueProvider) {
        this.values = new PriorityQueue<>();
        this.maxValues = maxValues;
        this.valueProvider = valueProvider;
    }
    
    public void add (T value) {
        if (this.values.size() == this.maxValues) {
            this.values.poll();
        }
        
        this.values.add(value);
    }
    
    public double getAverage() {
        double average = 0.0;
        
        Iterator<T> iterator = this.values.iterator();
        
        while (iterator.hasNext()) {
            
            T next = iterator.next();
            average += this.valueProvider.getValue(next);
        }
        
        return average / this.values.size();
    }
}
