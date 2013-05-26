/**
 * 
 */

package com.zygon.trade.mtgox.modules;

import com.xeiam.xchange.currency.Currencies;
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
import org.joda.money.CurrencyUnit;

/**
 *
 * @author zygon
 */
public class MtGoxSimulationModuleProvider implements ModuleProvider {

    private static InformationManager getInformationLayer(String name, MarketConditions marketConditions, TradeAgent trader) {
        
        List<TradeAgent> traders = new ArrayList<>();
        
        traders.add(trader);
        
        return new InformationManager(name, marketConditions, traders);
    }
    
    // Funny story.. This won't compile without these Trades. Gotta implement your own!
    private static TradeAgent getSimulationTrader(MarketConditions marketConditions) {
        List<Trade> trades = new ArrayList<>();
        
        ExecutionController execController = new ExecutionController(Currencies.USD, new SimulationBinding("joe", CurrencyUnit.USD, 200.0, marketConditions));
        
        trades.add(new Trade(new MACDTrade("joe-id-macd", execController)));
        trades.add(new Trade(new BBTrader("joe-id-bb", execController)));
        trades.add(new Trade(new SimpleStrategy("joe-id-Simple", execController)));
        
        return new TradeAgent(trades);
    }
    
    @Override
    public Module[] getModules() {
        MarketConditions marketConditions = new MarketConditions(Currencies.BTC);
        
        Module[] modules = new Module[] {
            new MtGoxStack(
                "MtGoxStack", 
                new MtGoxTickerData("MtGoxTickerData"), 
                getInformationLayer("MtGoxInformationLayer", marketConditions, getSimulationTrader(marketConditions)))
        };
        
        return modules;
    }
}
