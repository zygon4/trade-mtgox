/**
 * 
 */

package com.zygon.trade.mtgox.strategy;

import com.zygon.trade.market.model.indication.market.MACD;
import static com.zygon.trade.market.model.indication.market.MACD.IndicationType.ZERO_CROSS;
import com.zygon.trade.market.model.indication.market.MACDZeroCross;
import com.zygon.trade.strategy.AbstractIndicationProcessor;
import com.zygon.trade.strategy.IndicationProcessor.Advice;

/**
 *
 * @author zygon
 */
public class SimpleMACDZeroCross extends AbstractIndicationProcessor<MACD> {

    public SimpleMACDZeroCross() {
        super("SimpleMACDZeroCross");
    }

    @Override
    protected Advice getAdvice(MACD in) {
        return null;
    }

    @Override
    protected Evidence getEvidence(MACD in) {
        Evidence evidence = null;
        
        switch (in.getIndicationType()) {
            case ZERO_CROSS:
                MACDZeroCross zeroLineSignal = (MACDZeroCross) in;
                
                this.getLog().trace("Received indication " + zeroLineSignal);
                
                if (zeroLineSignal.crossAboveZero()) {
                    evidence = Evidence.BULL;
                } else {
                    evidence = Evidence.BEAR;
                }
                break;
            default:
                this.getLog().trace("Ignoring indication " + in.getIndicationType().name());
        }
        
        return evidence;
    }
}
