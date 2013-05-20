/**
 * 
 */

package com.zygon.trade.mtgox.data.interpreter;

import com.xeiam.xchange.Currencies;
import com.zygon.trade.market.data.DataProcessor;
import com.zygon.trade.market.model.indication.numeric.Price;
import com.zygon.trade.mtgox.data.Ticker;
import java.math.RoundingMode;

/**
 *
 * @author zygon
 */
public class TickerPriceInterpreter implements DataProcessor.Interpreter<Ticker> {

    @Override
    public Price[] interpret(Ticker in) {
        return new Price[] {
            new Price (in.getTradableIdentifier(), in.getTimestamp(), 
                in.getAsk().plus(in.getBid()).dividedBy(2, RoundingMode.UP).getAmount().doubleValue(), Currencies.USD)
        };
    }
}
