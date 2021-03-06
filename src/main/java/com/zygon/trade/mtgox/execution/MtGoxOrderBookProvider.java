/**
 * 
 */

package com.zygon.trade.mtgox.execution;

import com.xeiam.xchange.dto.marketdata.OrderBook;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.dto.trade.OpenOrders;
import com.xeiam.xchange.mtgox.v2.service.polling.MtGoxPollingMarketDataService;
import com.xeiam.xchange.mtgox.v2.service.polling.MtGoxPollingTradeService;
import com.zygon.trade.execution.OrderBookProvider;
import java.util.List;

/**
 *
 * @author zygon
 */
public class MtGoxOrderBookProvider implements OrderBookProvider {

    private final MtGoxPollingTradeService mtGoxPollingTradeService;
    private final MtGoxPollingMarketDataService mtGoxPollingMarketDataService;

    public MtGoxOrderBookProvider(MtGoxPollingTradeService mtGoxPollingTradeService, MtGoxPollingMarketDataService mtGoxPollingMarketDataService) {
        this.mtGoxPollingTradeService = mtGoxPollingTradeService;
        this.mtGoxPollingMarketDataService = mtGoxPollingMarketDataService;
    }
    
    @Override
    public void getOpenOrders(List<LimitOrder> orders) {
        OpenOrders openOrders = this.mtGoxPollingTradeService.getOpenOrders();
        orders.addAll(openOrders.getOpenOrders());
    }

    @Override
    public void getOrderBook(String username, OrderBook orders, String tradeableIdentifer, String currency) {
        OrderBook openOrders = this.mtGoxPollingMarketDataService.getPartialOrderBook(tradeableIdentifer, currency);
        
        orders.getAsks().addAll(openOrders.getAsks());
        orders.getBids().addAll(openOrders.getBids());
    }
}
