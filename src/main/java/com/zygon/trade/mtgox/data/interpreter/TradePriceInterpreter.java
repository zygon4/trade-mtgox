/**
 * 
 */

package com.zygon.trade.mtgox.data.interpreter;

import com.xeiam.xchange.Currencies;
import com.zygon.trade.market.data.DataProcessor;
import com.xeiam.xchange.dto.marketdata.Trade;
import com.zygon.trade.market.model.indication.numeric.Price;

/**
 *
 * @author zygon
 */
public class TradePriceInterpreter implements DataProcessor.Interpreter<Trade> {
    
    @Override
    public Price[] interpret(Trade in) {
        return new Price[] {
            new Price(in.getTradableIdentifier(), in.getTimestamp().getTime(), 
                in.getPrice().getAmount().doubleValue(), Currencies.USD)
        };
    }
}
