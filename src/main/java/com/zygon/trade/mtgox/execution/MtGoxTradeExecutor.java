/**
 * 
 */

package com.zygon.trade.mtgox.execution;

import com.xeiam.xchange.dto.Order;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.dto.trade.MarketOrder;
import com.xeiam.xchange.mtgox.v2.service.polling.MtGoxPollingTradeService;
import com.zygon.trade.execution.ExchangeException;
import com.zygon.trade.execution.TradeExecutor;

/**
 *
 * @author zygon
 */
public class MtGoxTradeExecutor implements TradeExecutor {

    private final MtGoxPollingTradeService mtGoxPollingTradeService;

    public MtGoxTradeExecutor(MtGoxPollingTradeService mtGoxPollingTradeService) {
        this.mtGoxPollingTradeService = mtGoxPollingTradeService;
    }

    @Override
    public void cancel(String orderId) throws ExchangeException {
        this.mtGoxPollingTradeService.cancelOrder(orderId);
    }
    
    @Override
    public String execute(Order order) throws ExchangeException {
        
        String orderId = null;
        
        if (order instanceof LimitOrder) {
            orderId = this.mtGoxPollingTradeService.placeLimitOrder((LimitOrder)order);
        } else if (order instanceof MarketOrder) {
            orderId = this.mtGoxPollingTradeService.placeMarketOrder((MarketOrder)order);
        }
        
        return orderId;
    }
}
