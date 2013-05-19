/**
 * 
 */

package com.zygon.trade.mtgox.strategy;

import com.zygon.trade.market.model.indication.numeric.SimpleMovingAverage;
import com.zygon.trade.strategy.AbstractIndicationProcessor;
import com.zygon.trade.strategy.IndicationProcessor.Advice;
import com.zygon.trade.strategy.TradeAgent;

/**
 *
 * @author zygon
 */
public class SimpleSMAProcessor extends AbstractIndicationProcessor<SimpleMovingAverage> {

    public SimpleSMAProcessor(TradeAgent agent) {
        super("SimpleSMAProcessor", agent);
    }

    @Override
    protected Advice getAdvice(SimpleMovingAverage in) {
        return null;
    }

    private SimpleMovingAverage lead = null;
    private SimpleMovingAverage lag = null;
    
    @Override
    protected Evidence getEvidence(SimpleMovingAverage in) {
        Evidence evidence = null;
        
        // TODO;
        
        return evidence;
    }
}
