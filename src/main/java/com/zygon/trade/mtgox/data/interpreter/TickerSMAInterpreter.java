/**
 * 
 */

package com.zygon.trade.mtgox.data.interpreter;

import com.google.common.collect.MinMaxPriorityQueue;
import com.zygon.trade.market.data.DataProcessor;
import com.zygon.trade.market.model.indication.Aggregation;
import com.zygon.trade.market.model.indication.numeric.NumericIndication;
import com.zygon.trade.market.model.indication.numeric.SimpleMovingAverage;
import com.zygon.trade.mtgox.data.Ticker;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.Iterator;

/**
 *
 * @author zygon
 */
public class TickerSMAInterpreter implements DataProcessor.Interpreter<Ticker> {

    // TODO: actual max occupancy calculation
    private static final int TICKS_PER_MINUTE = 4;
    
    private final MinMaxPriorityQueue<Ticker> tickers;
    private final Aggregation aggregation;
    
    public TickerSMAInterpreter(Aggregation aggregation) {
        
        Comparator<Ticker> comparator = new Comparator<Ticker>() {

            @Override
            public int compare(Ticker t, Ticker t1) {
                return t.getTimestamp() > t1.getTimestamp() ? -1 : t.getTimestamp() < t1.getTimestamp() ? 1 : 0;
            }
        };
        
        this.aggregation = aggregation;
        this.tickers = MinMaxPriorityQueue.orderedBy(comparator).maximumSize((int) this.aggregation.getDuration() * TICKS_PER_MINUTE).create();
    }
    
    private double getAveragePrice(Ticker in) {
        return in.getAsk().plus(in.getBid()).dividedBy(2, RoundingMode.UP).getAmount().doubleValue();
    }
    
    private double getAveragePrice() {
        double sum = 0.0;
        
        Iterator<Ticker> iterator = this.tickers.iterator();
        while (iterator.hasNext()) {
            sum += this.getAveragePrice(iterator.next());
        }
        
        return sum / this.tickers.size();
    }
    
    @Override
    public NumericIndication interpret(Ticker in) {
        
        this.tickers.add(in);
        
        double averagePrice = this.getAveragePrice();
        
        return new SimpleMovingAverage(in.getTradableIdentifier(), in.getTimestamp(), averagePrice, this.aggregation);
    }
}
