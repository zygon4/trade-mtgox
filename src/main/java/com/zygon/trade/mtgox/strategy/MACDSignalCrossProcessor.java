/**
 * 
 */

package com.zygon.trade.mtgox.strategy;

import com.zygon.trade.market.model.indication.market.MACD;
import static com.zygon.trade.market.model.indication.market.MACD.IndicationType.SIGNAL_CROSS;
import com.zygon.trade.market.model.indication.market.MACDSignalCross;
import com.zygon.trade.strategy.AbstractIndicationProcessor;
import com.zygon.trade.strategy.IndicationProcessor.Advice;
import com.zygon.trade.strategy.TradeAgent;

/**
 *
 * @author zygon
 */
public class MACDSignalCrossProcessor extends AbstractIndicationProcessor<MACD> {

    public MACDSignalCrossProcessor(TradeAgent agent) {
        super("MACDSignalCrossProcessor", agent);
    }

    @Override
    protected Advice getAdvice(MACD in) {
        Advice advice = Advice.DO_NOTHING;
        
        switch (in.getIndicationType()) {
            case SIGNAL_CROSS:
                MACDSignalCross macdSignalLine = (MACDSignalCross) in;
                
                this.getLog().trace("Received indication " + macdSignalLine);
                
                if (macdSignalLine.crossAboveSignal()) {
                    advice = Advice.BUY;
                } else {
                    advice = Advice.SELL;
                }
                break;
            default:
                this.getLog().trace("Ignoring indication " + in.getIndicationType().name());
        }
        
        return advice;
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
