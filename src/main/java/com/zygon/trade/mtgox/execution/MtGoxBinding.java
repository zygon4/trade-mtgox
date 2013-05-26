/**
 * 
 */

package com.zygon.trade.mtgox.execution;

import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.mtgox.v1.service.account.MtGoxPollingAccountService;
import com.xeiam.xchange.mtgox.v1.service.trade.polling.MtGoxPollingTradeService;
import com.zygon.trade.execution.AccountController;
import com.zygon.trade.execution.ExecutionController;
import com.zygon.trade.execution.OrderBookProvider;
import com.zygon.trade.execution.OrderProvider;
import com.zygon.trade.execution.TradeExecutor;
import org.joda.money.CurrencyUnit;

/**
 *
 * @author zygon
 */
public class MtGoxBinding implements ExecutionController.Binding {

    private final MtGoxAcctController accntController;
    private final OrderBookProvider orderBookProvider;
    private final OrderProvider orderProvider;
    private final TradeExecutor tradeExecutor;

    public MtGoxBinding(ExchangeSpecification exchangeSpec, CurrencyUnit currency) {
        this.accntController = new MtGoxAcctController(new MtGoxPollingAccountService(exchangeSpec));
        
        MtGoxPollingTradeService tradeService = new MtGoxPollingTradeService(exchangeSpec);
        
        this.orderBookProvider = new MtGoxOrderBookProvider(tradeService);
        this.orderProvider = new MtGoxOrderProvider(currency);
        this.tradeExecutor = new MtGoxTradeExecutor(tradeService);
    }
    
    @Override
    public AccountController getAccountController(String id) {
        return this.accntController;
    }

    @Override
    public OrderBookProvider getOrderBookProvider(String id) {
        return this.orderBookProvider;
    }

    @Override
    public OrderProvider getOrderProvider(String id) {
        return this.orderProvider;
    }

    @Override
    public TradeExecutor getTradeExecutor(String id) {
        return this.tradeExecutor;
    }
}