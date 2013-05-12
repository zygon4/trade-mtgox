/**
 * 
 */

package com.zygon.trade.mtgox.data.interpreter;

import com.zygon.trade.market.model.indication.numeric.NumericIndication;
import com.zygon.trade.market.data.DataProcessor;
import com.zygon.trade.market.model.indication.Classification;
import com.zygon.trade.mtgox.data.Ticker;
import java.math.RoundingMode;

/**
 *
 * @author zygon
 */
public class TickerPriceInterpreter implements DataProcessor.Interpreter<Ticker> {

    @Override
    public NumericIndication interpret(Ticker in) {
        return new NumericIndication(in.getTradableIdentifier(), Classification.PRICE, in.getTimestamp(),
                in.getAsk().plus(in.getBid()).dividedBy(2, RoundingMode.UP).getAmount().doubleValue());
    }
}
