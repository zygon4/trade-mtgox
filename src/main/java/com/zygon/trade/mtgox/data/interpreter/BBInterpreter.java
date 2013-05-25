/**
 * 
 */

package com.zygon.trade.mtgox.data.interpreter;

import com.zygon.trade.market.Message;
import com.zygon.trade.market.data.DataProcessor;
import com.zygon.trade.market.model.indication.Aggregation;
import com.zygon.trade.market.model.indication.market.BollingerBand;
import com.zygon.trade.market.util.ExponentialMovingAverage;
import com.zygon.trade.market.util.MovingAverage;
import com.zygon.trade.mtgox.data.Ticker;
import java.math.RoundingMode;

/**
 *
 * @author zygon
 */
public class BBInterpreter implements DataProcessor.Interpreter<Ticker> {

    private static double getMidPrice(Ticker in) {
        return in.getAsk().plus(in.getBid()).dividedBy(2, RoundingMode.UP).getAmount().doubleValue();
    }
    
    // TODO: actual max occupancy calculation
    private static final int TICKS_PER_MINUTE = 4;
    
    private final MovingAverage ema;
    private final int kstd;

    public BBInterpreter(Aggregation ma, int kstd) {
        
        if (ma.getType() != Aggregation.Type.AVG) {
            throw new IllegalArgumentException("Aggregations must be based on average");
        }
        
        this.ema = new ExponentialMovingAverage((int)ma.getDuration().getVal() * TICKS_PER_MINUTE);;
        this.kstd = kstd;
    }
    
    @Override
    public Message[] interpret(Ticker data) {
        
        double price = getMidPrice(data);
        
        this.ema.add(price);
        
        double ma = this.ema.getMean();
        double std = this.ema.getStd();
        
        Message bb = new BollingerBand(data.getTradableIdentifier(), data.getTimestamp(), ma, std, this.kstd, price);
        
        return new Message[]{bb};
    }
}
