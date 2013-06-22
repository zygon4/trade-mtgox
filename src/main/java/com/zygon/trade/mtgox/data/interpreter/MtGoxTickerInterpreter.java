/**
 * 
 */

package com.zygon.trade.mtgox.data.interpreter;

import com.zygon.trade.market.data.DataProcessor;
import com.zygon.trade.market.model.indication.Aggregation;
import com.zygon.trade.market.data.Ticker;
import java.math.RoundingMode;
import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 *
 * @author zygon
 */
/*pkg*/ abstract class MtGoxTickerInterpreter implements DataProcessor.Interpreter<Ticker> {

    protected static double getMidPrice(Ticker in) {
        return in.getAsk().plus(in.getBid()).dividedBy(2, RoundingMode.UP).getAmount().doubleValue();
    }
    
    private static final int TICKS_PER_MINUTE = 2;
    
    private static final int HOURS_IN_DAY = 24;
    private static final int MINUTES_IN_HOUR = 60;
    private static final int SECONDS_IN_MINUTE = 60;
    private static final int MILLIS_IN_SECOND = 1000;
    private static final int NANOS_IN_MILLI = 1000;
    
    protected static int getWindow (Aggregation aggregation) {
        int window = 0;
        
        switch (aggregation.getUnits()) {
            case DAYS:
                window = aggregation.getDuration().getVal() * HOURS_IN_DAY * MINUTES_IN_HOUR * TICKS_PER_MINUTE;
                break;
            case HOURS:
                window = aggregation.getDuration().getVal() * MINUTES_IN_HOUR * TICKS_PER_MINUTE;
                break;
            case MINUTES:
                window = aggregation.getDuration().getVal() * TICKS_PER_MINUTE;
                break;
            case SECONDS:
            case MICROSECONDS:
            case MILLISECONDS:
            case NANOSECONDS:
                throw new IllegalArgumentException("unsupported aggregation time");
        }
        
        return window;
    }
    
}
