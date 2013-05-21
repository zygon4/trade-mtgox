/**
 * 
 */

package com.zygon.trade.mtgox.strategy;

import com.zygon.trade.execution.MarketConditions;
import com.zygon.trade.execution.MarketConditionsProvider;
import com.zygon.trade.market.model.indication.numeric.Price;
import com.zygon.trade.strategy.AbstractIndicationProcessor;
import java.math.BigDecimal;

/**
 *
 * @author zygon
 */
public class MarketConditionsProcessor extends AbstractIndicationProcessor<Price> implements MarketConditionsProvider {

    public MarketConditionsProcessor(String name) {
        super(name, null);
    }

    private volatile Price currentPrice = null;
    
    @Override
    public MarketConditions get() {
        // null pointer possible - but let's find out where/who is calling this 
        // before a call to process.
        return new MarketConditions(this.currentPrice.getTradableIdentifier(), new BigDecimal(currentPrice.getValue()), this.currentPrice.getCurrency());
    }

    @Override
    protected Advice getAdvice(Price in) {
        return null;
    }

    @Override
    protected Evidence getEvidence(Price in) {
        return null;
    }

    @Override
    public void process(Price in) {
        this.currentPrice = in;
    }
}
