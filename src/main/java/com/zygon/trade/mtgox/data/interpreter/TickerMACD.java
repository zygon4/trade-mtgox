/**
 * 
 */

package com.zygon.trade.mtgox.data.interpreter;

import com.zygon.trade.market.data.DataProcessor;
import com.zygon.trade.market.model.indication.Aggregation;
import com.zygon.trade.market.model.indication.market.MACD;
import com.zygon.trade.market.model.indication.market.MACDSignalCross;
import com.zygon.trade.market.model.indication.market.MACDZeroCross;
import com.zygon.trade.mtgox.data.Ticker;
import com.zygon.trade.mtgox.data.interpreter.MovingAverage.ValueProvider;
import java.math.RoundingMode;

/**
 *
 * @author zygon
 */
public class TickerMACD implements DataProcessor.Interpreter<Ticker> {

    private static final class TickerValueProvider implements ValueProvider<Ticker> {

        private double getAveragePrice(Ticker in) {
            return in.getAsk().plus(in.getBid()).dividedBy(2, RoundingMode.UP).getAmount().doubleValue();
        }
        
        @Override
        public double getValue(Ticker in) {
            return this.getAveragePrice(in);
        }
    }
    
    private static final class IdentifyValueProvider implements ValueProvider<Double> {

        @Override
        public double getValue(Double in) {
            return in;
        }
    }
    
    // TODO: actual max occupancy calculation
    private static final int TICKS_PER_MINUTE = 4;
    
    private final Aggregation leading;
    private final Aggregation lagging;
    private final Aggregation macd;
    
    private final MovingAverage<Ticker> leadingMA;
    private final MovingAverage<Ticker> laggingMA;
    private final MovingAverage<Double> macdMA;
    
    public TickerMACD(Aggregation leading, Aggregation lagging, Aggregation macd) {
        
        if (leading.getType() != Aggregation.Type.AVG || lagging.getType() != Aggregation.Type.AVG || macd.getType() != Aggregation.Type.AVG) {
            throw new IllegalArgumentException("Aggregations must be based on average");
        }
        
        this.leading = leading;
        this.lagging = lagging;
        this.macd = macd;
        
        this.leadingMA = new MovingAverage<>((int)this.leading.getDuration().getVal() * TICKS_PER_MINUTE, new TickerValueProvider());
        this.laggingMA = new MovingAverage<>((int)this.lagging.getDuration().getVal() * TICKS_PER_MINUTE, new TickerValueProvider());
        this.macdMA = new MovingAverage<>((int)this.macd.getDuration().getVal() * TICKS_PER_MINUTE, new IdentifyValueProvider());
    }
    
    private boolean firstValue = true;
    private boolean aboveZero = false;
    private boolean aboveSignal = false;
    
    @Override
    public MACD[] interpret(Ticker in) {
        
        //TBD: only get the average price every x number of ticks?
        
        this.leadingMA.add(in);
        this.laggingMA.add(in);
        
        double leadingPrice = this.leadingMA.getAverage();
        double laggingPrice = this.laggingMA.getAverage();
        
        double macdLine = leadingPrice - laggingPrice;
        
        this.macdMA.add(macdLine);
        
        double signalLine = this.macdMA.getAverage();
        
        if (this.firstValue) {
            // First calc zero cross
            this.aboveZero = macdLine > 0.0;
            
            // Now calc signal cross
            this.aboveSignal = signalLine > macdLine;
            
            this.firstValue = false;
        } else {
            if (this.aboveZero) {
                if (macdLine < 0.0) {
                    this.aboveZero = false;
                }
            } else {
                if (macdLine > 0.0) {
                    this.aboveZero = true;
                }
            }
            
            if (this.aboveSignal) {
                if (signalLine < macdLine) {
                    this.aboveSignal = false;
                }
            } else {
                if (signalLine > macdLine) {
                    this.aboveSignal = true;
                }
            }
        }
        
        MACD macdZeroCross = new MACDZeroCross(in.getTradableIdentifier(), in.getTimestamp(), this.aboveZero);
        MACD macdSignalCross = new MACDSignalCross(in.getTradableIdentifier(), in.getTimestamp(), this.aboveSignal);
        
        return new MACD[] {
            macdZeroCross,
            macdSignalCross
        };
    }
}
