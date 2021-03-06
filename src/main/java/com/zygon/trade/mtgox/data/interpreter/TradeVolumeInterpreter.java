/**
 * 
 */

package com.zygon.trade.mtgox.data.interpreter;

import com.zygon.trade.market.data.DataProcessor;
import com.xeiam.xchange.dto.marketdata.Trade;
import com.zygon.trade.market.model.indication.numeric.Volume;

/**
 *
 * @author zygon
 */
public class TradeVolumeInterpreter implements DataProcessor.Interpreter<Trade> {
    
    @Override
    public Volume[] interpret(Trade in) {
        return new Volume[] {
            new Volume(in.getTradableIdentifier(), in.getTransactionCurrency(), 
                in.getTimestamp().getTime(), in.getTradableAmount().doubleValue())};
    }
}
