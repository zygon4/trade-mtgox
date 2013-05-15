/**
 * 
 */

package com.zygon.trade.mtgox.data.interpreter;

import com.zygon.trade.market.data.DataProcessor;
import com.zygon.trade.market.model.indication.Aggregation;
import com.zygon.trade.market.model.indication.numeric.NumericIndication;
import com.zygon.trade.market.model.indication.numeric.SMA15Min;
import com.zygon.trade.mtgox.data.Ticker;
import com.zygon.trade.mtgox.data.interpreter.MovingAverage.ValueProvider;
import java.math.RoundingMode;

/**
 *
 * @author zygon
 */
public class TickerSMAInterpreter implements DataProcessor.Interpreter<Ticker> {

    private static final class TickerValueProvider implements ValueProvider<Ticker> {

        private double getAveragePrice(Ticker in) {
            return in.getAsk().plus(in.getBid()).dividedBy(2, RoundingMode.UP).getAmount().doubleValue();
        }
        
        @Override
        public double getValue(Ticker in) {
            return this.getAveragePrice(in);
        }
    }
    
    // TODO: actual max occupancy calculation
    private static final int TICKS_PER_MINUTE = 4;
    
    private final Aggregation aggregation;
    private final MovingAverage<Ticker> movingAverage;
    
    public TickerSMAInterpreter(Aggregation aggregation) {
        this.aggregation = aggregation;
        this.movingAverage = new MovingAverage<>((int)this.aggregation.getDuration() * TICKS_PER_MINUTE, new TickerValueProvider());
    }
    
    @Override
    public NumericIndication interpret(Ticker in) {
        
        this.movingAverage.add(in);
        
        double averagePrice = this.movingAverage.getAverage();
        
        return new SMA15Min(in.getTradableIdentifier(), in.getTimestamp(), averagePrice);
    }
}
