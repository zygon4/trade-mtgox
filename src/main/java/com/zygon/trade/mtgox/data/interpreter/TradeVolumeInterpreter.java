/**
 * 
 */

package com.zygon.trade.mtgox.data.interpreter;

import com.zygon.exchange.market.data.DataProcessor;
import com.xeiam.xchange.dto.marketdata.Trade;
import com.zygon.exchange.market.model.indication.numeric.NumericIndication;
import com.zygon.exchange.market.model.indication.Classification;

/**
 *
 * @author zygon
 */
public class TradeVolumeInterpreter implements DataProcessor.Interpreter<Trade> {
    
    //in.getTransactionCurrency() ???
    
    @Override
    public NumericIndication interpret(Trade in) {
        return new NumericIndication(in.getTradableIdentifier(), Classification.VOLUME, 
                in.getTimestamp().getTime(), in.getTradableAmount().doubleValue());
    }
}
