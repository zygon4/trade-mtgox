/**
 * 
 */

package com.zygon.trade.mtgox.data.interpreter;

import com.zygon.exchange.market.data.DataHandler;
import com.xeiam.xchange.dto.marketdata.Trade;
import com.zygon.exchange.market.model.indication.numeric.NumericIndication;
import com.zygon.exchange.market.model.indication.Classification;

/**
 *
 * @author zygon
 */
public class TradePriceInterpreter implements DataHandler.Interpreter<Trade> {
    
    @Override
    public NumericIndication interpret(Trade in) {
        return new NumericIndication(in.getTradableIdentifier(), Classification.PRICE, 
                in.getTimestamp().getTime(), in.getPrice().getAmount().doubleValue());
    }
}
