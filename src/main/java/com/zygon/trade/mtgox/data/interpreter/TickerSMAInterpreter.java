/**
 * 
 */

package com.zygon.trade.mtgox.data.interpreter;

import com.zygon.trade.market.util.MovingAverage;
import com.zygon.trade.market.data.DataProcessor;
import com.zygon.trade.market.model.indication.Aggregation;
import com.zygon.trade.market.model.indication.numeric.SimpleMovingAverage;
import com.zygon.trade.mtgox.data.Ticker;
import java.math.RoundingMode;

/**
 *
 * @author zygon
 */
public class TickerSMAInterpreter implements DataProcessor.Interpreter<Ticker> {

    private static double getMidPrice(Ticker in) {
        return in.getAsk().plus(in.getBid()).dividedBy(2, RoundingMode.UP).getAmount().doubleValue();
    }
    
    // TODO: actual max occupancy calculation
    private static final int TICKS_PER_MINUTE = 4;
    
    private final Aggregation aggregation;
    private final MovingAverage movingAverage;
    
    public TickerSMAInterpreter(Aggregation aggregation) {
        
        if (aggregation.getType() != Aggregation.Type.AVG) {
            throw new IllegalArgumentException("Aggregation must be based on average");
        }
        
        this.aggregation = aggregation;
        this.movingAverage = new MovingAverage((int)this.aggregation.getDuration().getVal() * TICKS_PER_MINUTE);
    }
    
    @Override
    public SimpleMovingAverage[] interpret(Ticker in) {
        
        //TBD: only get the average price every x number of ticks?
        
        this.movingAverage.add(getMidPrice(in));
        
        double averagePrice = this.movingAverage.getMean();
        
        return new SimpleMovingAverage[] {
            new SimpleMovingAverage(in.getTradableIdentifier(), this.aggregation.getDuration(), this.aggregation.getUnits(), in.getTimestamp(), averagePrice) 
        };
    }
}
