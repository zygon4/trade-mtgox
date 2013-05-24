/**
 * 
 */

package com.zygon.trade.mtgox.data.interpreter;

import com.zygon.trade.market.util.MovingAverage;
import com.zygon.trade.market.data.DataProcessor;
import com.zygon.trade.market.model.indication.Aggregation;
import com.zygon.trade.market.model.indication.market.MACD;
import com.zygon.trade.market.model.indication.market.MACDSignalCross;
import com.zygon.trade.market.model.indication.market.MACDZeroCross;
import com.zygon.trade.market.util.ExponentialMovingAverage;
import com.zygon.trade.mtgox.data.Ticker;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zygon
 */
public class TickerMACD implements DataProcessor.Interpreter<Ticker> {

    private static double getMidPrice(Ticker in) {
        return in.getAsk().plus(in.getBid()).dividedBy(2, RoundingMode.UP).getAmount().doubleValue();
    }
    
    // TODO: actual max occupancy calculation
    private static final int TICKS_PER_MINUTE = 4;
    
    private final Aggregation leading;
    private final Aggregation lagging;
    private final Aggregation macd;
    
    private final MovingAverage leadingMA;
    private final MovingAverage laggingMA;
    private final MovingAverage macdMA;
    
    // TODO: convert time to seconds
    private static int getWindow (Aggregation aggregation, int ticksPerMinute) {
        int ticks = 0;
        
        switch (aggregation.getUnits()) {
            case DAYS:
//                ticks 
            case HOURS:
            case MINUTES:
            case SECONDS:
            case MICROSECONDS:
            case MILLISECONDS:
            case NANOSECONDS:
        }
        
        return ticks;
    }
    
    public TickerMACD(Aggregation leading, Aggregation lagging, Aggregation macd) {
        
        if (leading.getType() != Aggregation.Type.AVG || lagging.getType() != Aggregation.Type.AVG || macd.getType() != Aggregation.Type.AVG) {
            throw new IllegalArgumentException("Aggregations must be based on average");
        }
        
        this.leading = leading;
        this.lagging = lagging;
        this.macd = macd;
        
        this.leadingMA = new ExponentialMovingAverage((int)this.leading.getDuration().getVal() * TICKS_PER_MINUTE);
        this.laggingMA = new ExponentialMovingAverage((int)this.lagging.getDuration().getVal() * TICKS_PER_MINUTE);
        
        this.macdMA = new ExponentialMovingAverage((int)this.macd.getDuration().getVal() * TICKS_PER_MINUTE);
    }
    
    private boolean firstValue = true;
    private boolean aboveZero = false;
    private boolean aboveSignal = false;
    
    @Override
    public MACD[] interpret(Ticker in) {
        
        //TBD: only get the average price every x number of ticks?
        
        this.leadingMA.add(getMidPrice(in));
        this.laggingMA.add(getMidPrice(in));
        
        double leadingPrice = this.leadingMA.getMean();
        double laggingPrice = this.laggingMA.getMean();
        
        double macdLine = leadingPrice - laggingPrice;
        
        this.macdMA.add(macdLine);
        
        double signalLine = this.macdMA.getMean();
        
        List<MACD> macds = new ArrayList<>();
        
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
                    macds.add(new MACDZeroCross(in.getTradableIdentifier(), in.getTimestamp(), this.aboveZero));
                }
            } else {
                if (macdLine > 0.0) {
                    this.aboveZero = true;
                    macds.add(new MACDZeroCross(in.getTradableIdentifier(), in.getTimestamp(), this.aboveZero));
                }
            }
            
            if (this.aboveSignal) {
                if (signalLine < macdLine) {
                    this.aboveSignal = false;
                    macds.add(new MACDSignalCross(in.getTradableIdentifier(), in.getTimestamp(), this.aboveSignal));
                }
            } else {
                if (signalLine > macdLine) {
                    this.aboveSignal = true;
                    macds.add(new MACDSignalCross(in.getTradableIdentifier(), in.getTimestamp(), this.aboveSignal));
                }
            }
        }
        
//        // TODO: need to not generate signal unless there is a cross
//        MACD macdZeroCross = new MACDZeroCross(in.getTradableIdentifier(), in.getTimestamp(), this.aboveZero);
//        
//        // TODO: need to not generate signal unless there is a cross
//        MACD macdSignalCross = new MACDSignalCross(in.getTradableIdentifier(), in.getTimestamp(), this.aboveSignal);
        
        return macds.toArray(new MACD[macds.size()]);
    }
}
