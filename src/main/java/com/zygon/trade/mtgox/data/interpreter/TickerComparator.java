/**
 * 
 */

package com.zygon.trade.mtgox.data.interpreter;

import com.zygon.trade.mtgox.data.Ticker;
import java.util.Comparator;

/**
 *
 * @author zygon
 */
public class TickerComparator implements Comparator<Ticker>{

    @Override
    public int compare(Ticker t, Ticker t1) {
        // null checks??
        return t.getTimestamp() < t1.getTimestamp() ? -1 : t.getTimestamp() > t1.getTimestamp() ? 1 : 0;
    }
}
