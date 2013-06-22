/**
 * 
 */

package com.zygon.trade.mtgox.data;

import com.xeiam.xchange.ExchangeFactory;
import com.xeiam.xchange.mtgox.v2.MtGoxExchange;
import com.xeiam.xchange.mtgox.v2.service.polling.MtGoxPollingMarketDataService;
import com.zygon.trade.market.data.AbstractDataProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 */
public abstract class MtGoxDataProvider<T> extends AbstractDataProvider<T> {
    
    private final MtGoxExchange mtGoxROExchange = 
            (MtGoxExchange) ExchangeFactory.INSTANCE.createExchange(MtGoxExchange.class.getName());
    private final MtGoxPollingMarketDataService service = (MtGoxPollingMarketDataService) mtGoxROExchange.getPollingMarketDataService();
    
    private final Object historicDataLock = new Object();
    private boolean hasHistoricData = true;
    
    private final String tradableIdentifier;
    private final String currency;

    public MtGoxDataProvider(String name, long cacheDuration, TimeUnit units, String tradableIdentifier, String currency) {
        super(name, cacheDuration, units);
        
        this.tradableIdentifier = tradableIdentifier;
        this.currency = currency;
    }

    protected void getHistoric (List<T> historic) {
        // none by default
    }
    
    public String getCurrency() {
        return this.currency;
    }

    @Override
    public Collection<T> getHistoric() {
        List<T> historicTicks = new ArrayList<>();
        
        synchronized (this.historicDataLock) {
            try {
                if (this.hasHistoricData) {
                    this.getHistoric(historicTicks);
                    for (T t : historicTicks) {
                        System.out.println(t);
                    }
                }
            } finally {
                this.hasHistoricData = false;
            }
        }
        
        return historicTicks;
    }
    
    public String getTradableIdentifier() {
        return this.tradableIdentifier;
    }
    
    protected MtGoxPollingMarketDataService getService() {
        return this.service;
    }
    
    @Override
    public boolean hasHistoricInformation() {
        return this.hasHistoricData;
    }
}
