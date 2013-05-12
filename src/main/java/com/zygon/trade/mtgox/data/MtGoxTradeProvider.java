/**
 * 
 */

package com.zygon.trade.mtgox.data;

import com.xeiam.xchange.dto.Order;
import com.xeiam.xchange.dto.marketdata.Trade;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;

/**
 *
 * @author zygon
 * 
 * This is just a mock provider
 */
public class MtGoxTradeProvider extends MtGoxDataProvider<Trade> {
    
    public MtGoxTradeProvider(String tradableIdentifier, String currency) {
        super("Trade Provider", 5, TimeUnit.SECONDS, tradableIdentifier, currency);
    }
    
    private final Random rand = new Random(System.currentTimeMillis());
    
    @Override
    public Trade getData() {
        
        long price = rand.nextInt(50) + 100;
        long vol = rand.nextInt(10);
        
        return new Trade(Order.OrderType.BID, BigDecimal.valueOf(vol), this.getTradableIdentifier(), 
                this.getCurrency(), BigMoney.ofMajor(CurrencyUnit.USD, price), new Date(System.currentTimeMillis()));
    }
}
