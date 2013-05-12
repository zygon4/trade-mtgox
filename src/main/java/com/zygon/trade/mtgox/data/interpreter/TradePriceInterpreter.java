/**
 * 
 */

package com.zygon.trade.mtgox.data.interpreter;

import com.zygon.trade.market.data.DataProcessor;
import com.xeiam.xchange.dto.marketdata.Trade;
import com.zygon.trade.market.model.indication.numeric.NumericIndication;
import com.zygon.trade.market.model.indication.Classification;

/**
 *
 * @author zygon
 */
public class TradePriceInterpreter implements DataProcessor.Interpreter<Trade> {
    
    @Override
    public NumericIndication interpret(Trade in) {
        return new NumericIndication(in.getTradableIdentifier(), Classification.PRICE, 
                in.getTimestamp().getTime(), in.getPrice().getAmount().doubleValue());
    }
}
