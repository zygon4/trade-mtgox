/**
 * 
 */

package com.zygon.trade.mtgox.data;

import com.xeiam.xchange.ExchangeFactory;
import com.xeiam.xchange.mtgox.v2.MtGoxExchange;
import com.xeiam.xchange.mtgox.v2.service.polling.MtGoxPollingMarketDataService;
import com.zygon.trade.market.data.AbstractDataProvider;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 */
public abstract class MtGoxDataProvider<T> extends AbstractDataProvider<T> {
    
    private final MtGoxExchange mtGoxROExchange = 
            (MtGoxExchange) ExchangeFactory.INSTANCE.createExchange(MtGoxExchange.class.getName());
    private final MtGoxPollingMarketDataService service = (MtGoxPollingMarketDataService) mtGoxROExchange.getPollingMarketDataService();
    
    private final String tradableIdentifier;
    private final String currency;

    public MtGoxDataProvider(String name, long cacheDuration, TimeUnit units, String tradableIdentifier, String currency) {
        super(name, cacheDuration, units);
        
        this.tradableIdentifier = tradableIdentifier;
        this.currency = currency;
    }

    public String getCurrency() {
        return this.currency;
    }

    public String getTradableIdentifier() {
        return this.tradableIdentifier;
    }
    
    protected MtGoxPollingMarketDataService getService() {
        return this.service;
    }
}
