/**
 * 
 */

package com.zygon.trade.mtgox.strategy;

import com.xeiam.xchange.Currencies;
import com.xeiam.xchange.dto.Order;
import com.xeiam.xchange.dto.Order.OrderType;
import com.zygon.trade.execution.ExchangeException;
import com.zygon.trade.execution.ExecutionController;
import com.zygon.trade.execution.MarketConditions;
import com.zygon.trade.market.model.indication.market.MACDSignalCross;
import com.zygon.trade.market.model.indication.market.MACDZeroCross;
import com.zygon.trade.strategy.TradeImpl;
import com.zygon.trade.strategy.TradeMonitor;
import com.zygon.trade.strategy.TradeType;
import static com.zygon.trade.strategy.TradeType.LONG;
import static com.zygon.trade.strategy.TradeType.SHORT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zygon
 */
public class MACDTradeImpl implements TradeImpl {
    
    // TODO: configurable
    private static final double TRADE_RISK_PERCENTAGE = 0.10; // 10 percent for now to keep things interesting while testing
    
    private double getTradeVolume(double accntBalance, double currentPrice) {
        return (accntBalance * TRADE_RISK_PERCENTAGE) / currentPrice;
    }
    
    // TODO: configurable
    private static final double PROFIT_MODIFIER = 0.250;
    private static final double STOP_LOSS_MODIFER = 0.050;
    
    private final Logger logger = LoggerFactory.getLogger(MACDTradeImpl.class);
    private final String id;
    private final ExecutionController controller;
    
    private double entryPoint = -1.0;
    private double profitPoint = -1.0;
    private double stopLossPoint = -1.0;
    private double volume = 0.0;
    private TradeType type = null;
    
    public MACDTradeImpl(String id, ExecutionController controller) {
        this.id = id;
        this.controller = controller;
    }
    
    @Override
    public void activate(MarketConditions marketConditions) throws ExchangeException {
        
        MACDZeroCross zeroCross = (MACDZeroCross) marketConditions.getIndication(MACDZeroCross.ID);
        MACDSignalCross signalCross = (MACDSignalCross) marketConditions.getIndication(MACDSignalCross.ID);
        
        double currentPrice = marketConditions.getPrice().getValue();
        this.entryPoint = currentPrice;
        
        Order.OrderType orderType = null;
        if (zeroCross.crossAboveZero() && signalCross.crossAboveSignal()) {
            this.type = TradeType.LONG;
            orderType = Order.OrderType.BID;
            this.stopLossPoint = currentPrice - STOP_LOSS_MODIFER;
            this.profitPoint = currentPrice + PROFIT_MODIFIER;
        } else if (!zeroCross.crossAboveZero() && !signalCross.crossAboveSignal()) {
            this.type = TradeType.SHORT;
            orderType = Order.OrderType.ASK;
            this.stopLossPoint = currentPrice + STOP_LOSS_MODIFER;
            this.profitPoint = currentPrice - PROFIT_MODIFIER;
        } else {
            throw new IllegalStateException("Unable to activate in the current state");
        }
        
        double accntBalance = this.controller.getBalance(this.id, Currencies.USD);
        this.volume = getTradeVolume(accntBalance, currentPrice);
        
        Order order = this.controller.generateOrder(this.id, orderType, this.volume, marketConditions.getTradeableIdentifier(), Currencies.USD);
        
        try {
            this.controller.placeOrder(this.id, order);
        } catch (ExchangeException ee) {
            this.reset();
            throw ee;
        }
        
        // place order - assume synchronous fill for now.
    }

    @Override
    public double close(MarketConditions marketConditions) throws ExchangeException {
        
        double currentPrice = marketConditions.getPrice().getValue();
        double priceMargin = 0.0;
        Order.OrderType orderType = null;
        
        switch (this.type) {
            case LONG:
                orderType = OrderType.ASK;
                priceMargin = currentPrice - this.entryPoint;
                break;
            case SHORT:
                orderType = OrderType.BID;
                priceMargin = this.entryPoint - currentPrice;
                break;
        }
        
        Order order = this.controller.generateOrder(this.id, orderType, this.volume, marketConditions.getTradeableIdentifier(), Currencies.USD);
        
        try {
            this.controller.placeOrder(this.id, order);
        } catch (ExchangeException ee) {
            this.reset();
            throw ee;
        }
        
        // place order - assume synchronous fill for now.
        
        double profit = priceMargin * this.volume;
        
        this.reset();
        
        return profit;
    }
    
    protected TradeType getTradeType() {
        return this.type;
    }

    @Override
    public TradeMonitor getTradeMonitor() {
        // TODO:
        return new TradeMonitor();
    }

    @Override
    public boolean meetsEntryConditions(MarketConditions marketConditions) {
        MACDZeroCross zeroCross = (MACDZeroCross) marketConditions.getIndication(MACDZeroCross.ID);
        MACDSignalCross signalCross = (MACDSignalCross) marketConditions.getIndication(MACDSignalCross.ID);
        
        if (zeroCross != null && signalCross != null) {
            return (zeroCross.crossAboveZero() && signalCross.crossAboveSignal()) || 
                    (!zeroCross.crossAboveZero() && !signalCross.crossAboveSignal());
        }
        
        return false;
    }

    @Override
    public boolean meetsExitConditions(MarketConditions marketConditions) {
        double price = marketConditions.getPrice().getValue();
        
        switch (this.type) {
            case LONG:
                if (price <= this.stopLossPoint || price >= this.profitPoint) {
                    return true;
                }
                break;
            case SHORT:
                if (price >= this.stopLossPoint || price <= this.profitPoint) {
                    return true;
                }
                break;
        }
        
        return false;
    }
    
    private void reset() {
        this.type = null;
        this.entryPoint = -1;
        this.stopLossPoint = -1;
        this.profitPoint = -1;
        this.volume = 0.0;
    }
}
