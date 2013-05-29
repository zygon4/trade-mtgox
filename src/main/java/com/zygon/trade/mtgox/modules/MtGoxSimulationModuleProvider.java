/**
 * 
 */

package com.zygon.trade.mtgox.modules;

import com.xeiam.xchange.currency.Currencies;
import com.xeiam.xchange.dto.trade.Wallet;
import com.zygon.trade.Module;
import com.zygon.trade.ModuleProvider;
import com.zygon.trade.execution.ExecutionController;
import com.zygon.trade.execution.MarketConditions;
import com.zygon.trade.execution.simulation.SimulationBinding;
import com.zygon.trade.market.model.indication.InformationManager;
import com.zygon.trade.strategy.Trade;
import com.zygon.trade.strategy.TradeAgent;
import com.zygon.trade.strategy.trade.BBTrader;
import com.zygon.trade.strategy.trade.MACDTrade;
import com.zygon.trade.strategy.trade.SimpleStrategy;
import java.util.ArrayList;
import java.util.List;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;

/**
 *
 * @author zygon
 */
public class MtGoxSimulationModuleProvider implements ModuleProvider {

    private static final boolean LIVE = true;
    
    private static InformationManager getInformationLayer(String name, MarketConditions marketConditions, TradeAgent trader) {
        
        List<TradeAgent> traders = new ArrayList<>();
        
        traders.add(trader);
        
        return new InformationManager(name, marketConditions, traders);
    }
    
    // Funny story.. This won't compile without these Trades. Gotta implement your own!
    private static TradeAgent getSimulationTrader(MarketConditions marketConditions) {
        List<Trade> trades = new ArrayList<>();
        
        ExecutionController execController = new ExecutionController(new SimulationBinding("joe", 
                new Wallet[]{
                    new Wallet(CurrencyUnit.USD.getCurrencyCode(), BigMoney.of(CurrencyUnit.USD, 30.0)), 
                    new Wallet(CurrencyUnit.of(Currencies.BTC).getCurrencyCode(), BigMoney.of(CurrencyUnit.of(Currencies.BTC), 1.8))
                }, 
                marketConditions));
        
        trades.add(new Trade(new MACDTrade("joe-id-macd", execController)));
        trades.add(new Trade(new BBTrader("joe-id-bb", execController)));
        trades.add(new Trade(new SimpleStrategy("joe-id-Simple", execController)));
        
        return new TradeAgent(trades);
    }
    
    @Override
    public Module[] getModules() {
        
        if (LIVE) {
            MarketConditions marketConditions = new MarketConditions("MtGoxSimulation", Currencies.BTC);

            Module[] modules = new Module[] {
                new MtGoxStack(
                    "MtGoxSimulationStack", 
                    new MtGoxTickerData("MtGoxSimulationTickerData"), 
                    getInformationLayer("MtGoxSimulationInformationLayer", marketConditions, getSimulationTrader(marketConditions)))
            };

            return modules;
        }
        
        return new Module[] {};
    }
}
