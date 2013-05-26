/**
 * 
 */

package com.zygon.trade.mtgox.execution;

import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.dto.trade.OpenOrders;
import com.xeiam.xchange.mtgox.v1.service.trade.polling.MtGoxPollingTradeService;
import com.zygon.trade.execution.OrderBookProvider;
import java.util.List;

/**
 *
 * @author zygon
 */
public class MtGoxOrderBookProvider implements OrderBookProvider {

    private final MtGoxPollingTradeService mtGoxPollingTradeService;

    public MtGoxOrderBookProvider(MtGoxPollingTradeService mtGoxPollingTradeService) {
        this.mtGoxPollingTradeService = mtGoxPollingTradeService;
    }
    
    @Override
    public void getOpenOrders(List<LimitOrder> orders) {
        OpenOrders openOrders = this.mtGoxPollingTradeService.getOpenOrders();
        orders.addAll(openOrders.getOpenOrders());
    }
}
