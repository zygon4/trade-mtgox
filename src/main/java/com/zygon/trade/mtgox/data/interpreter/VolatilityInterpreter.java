/**
 * 
 */

package com.zygon.trade.mtgox.data.interpreter;

import com.zygon.trade.market.Message;
import com.zygon.trade.market.model.indication.Aggregation;
import com.zygon.trade.market.model.indication.numeric.Volatility;
import com.zygon.trade.market.util.MovingAverage;
import com.zygon.trade.mtgox.data.Ticker;
import static com.zygon.trade.mtgox.data.interpreter.MtGoxTickerInterpreter.getWindow;

/**
 *
 * @author zygon
 */
public class VolatilityInterpreter extends MtGoxTickerInterpreter {

    private final MovingAverage ma;
    
    // TBD: could have several aggregations and produce several volatilities
    public VolatilityInterpreter(Aggregation aggregation) {
        super();
        
        if (aggregation.getType() != Aggregation.Type.AVG) {
            throw new IllegalArgumentException("Aggregations must be based on average");
        }
        
        this.ma = new MovingAverage(getWindow(aggregation));
    }
    
    @Override
    public Message[] interpret(Ticker data) {
        this.ma.add(getMidPrice(data));
        
        double high = this.ma.getHigh();
        double low = this.ma.getLow();
        double volatility = high - low;
        
        return new Message[] {
            new Volatility(data.getTradableIdentifier(), data.getTimestamp(), volatility)
        };
    }
}
