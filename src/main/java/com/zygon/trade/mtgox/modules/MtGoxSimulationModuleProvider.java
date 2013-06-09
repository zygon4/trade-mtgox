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
import com.zygon.trade.modules.execution.ExecutionModule;
import com.zygon.trade.modules.model.InformationModule;
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
    private static TradeAgent getSimulationTrader(ExecutionController execController) {
        List<Trade> trades = new ArrayList<>();
        
        trades.add(new Trade(new MACDTrade("joe-id-macd", Currencies.BTC, execController)));
        trades.add(new Trade(new BBTrader("joe-id-bb", Currencies.BTC, execController)));
        trades.add(new Trade(new SimpleStrategy("joe-id-Simple", Currencies.BTC, execController)));
//        trades.add(new Trade(new VOLRSI("joe-id-volrsi", execController)));
        
        return new TradeAgent("SimulationTrader", trades);
    }
    
    private Module[] modules = null;
    
    @Override
    public Module[] getModules() {
        
        if (LIVE) {
            
            if (modules == null) {
                MarketConditions marketConditions = new MarketConditions("MtGoxSimulation");

                SimulationBinding simulationBinding = new SimulationBinding("joe", 
                        new Wallet[]{
                            new Wallet(CurrencyUnit.USD.getCurrencyCode(), BigMoney.of(CurrencyUnit.USD, 20000.0)), 
                            new Wallet(CurrencyUnit.of(Currencies.BTC).getCurrencyCode(), BigMoney.of(CurrencyUnit.of(Currencies.BTC), 100.0))
                        }, 
                        marketConditions);

                ExecutionController execController = new ExecutionController(simulationBinding);

                this.modules = new Module[] {
                    new ExecutionModule("MtGoxSimulationExecution", 
                        new InformationModule(
                            "MtGoxSimulationStack", 
                            new MtGoxTickerData("MtGoxSimulationTickerData"), 
                            getInformationLayer("MtGoxSimulationInformationLayer", marketConditions, getSimulationTrader(execController))),
                        simulationBinding)
                };
            }

            return this.modules;
        }
        
        return new Module[] {};
    }
}
