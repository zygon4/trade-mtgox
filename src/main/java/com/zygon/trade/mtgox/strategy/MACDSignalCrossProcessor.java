/**
 * 
 */

package com.zygon.trade.mtgox.strategy;

import com.zygon.trade.market.model.indication.market.MACD;
import com.zygon.trade.market.model.indication.market.MACDSignalCross;
import com.zygon.trade.strategy.AbstractIndicationProcessor;
import com.zygon.trade.strategy.IndicationProcessor.Advice;

/**
 *
 * @author zygon
 */
public class MACDSignalCrossProcessor extends AbstractIndicationProcessor<MACD> {

    public MACDSignalCrossProcessor() {
        super("MACDSignalCrossProcessor");
    }

    @Override
    protected Advice getAdvice(MACD in) {
        return null;
    }

    @Override
    protected Evidence getEvidence(MACD in) {
        Evidence evidence = null;
        
        switch (in.getIndicationType()) {
            case SIGNAL_CROSS:
                MACDSignalCross macdSignalLine = (MACDSignalCross) in;
                
                this.getLog().trace("Received indication " + macdSignalLine);
                
                if (macdSignalLine.crossAboveSignal()) {
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
