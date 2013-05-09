/**
 * 
 */

package com.zygon.trade.mtgox.data.interpreter;

import com.zygon.exchange.market.model.indication.numeric.NumericIndication;
import com.zygon.exchange.market.data.DataHandler;
import com.zygon.exchange.market.model.indication.Classification;
import com.zygon.trade.mtgox.data.Ticker;
import java.math.RoundingMode;

/**
 *
 * @author zygon
 */
public class TickerPriceInterpreter implements DataHandler.Interpreter<Ticker> {

    @Override
    public NumericIndication interpret(Ticker in) {
        return new NumericIndication(in.getTradableIdentifier(), Classification.PRICE, in.getTimestamp(),
                in.getAsk().plus(in.getBid()).dividedBy(2, RoundingMode.UP).getAmount().doubleValue());
    }
}
