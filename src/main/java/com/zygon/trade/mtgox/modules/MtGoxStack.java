/**
 *
 */
package com.zygon.trade.mtgox.modules;

import com.xeiam.xchange.Currencies;
import com.zygon.trade.execution.ExecutionController;
import com.zygon.trade.execution.MarketConditions;
import com.zygon.trade.execution.simulation.SimulationBinding;
import com.zygon.trade.market.model.indication.InformationManager;
import com.zygon.trade.modules.data.DataModule;
import com.zygon.trade.modules.model.InformationModule;
import com.zygon.trade.mtgox.strategy.MACDTradeImpl;
import com.zygon.trade.strategy.Trade2;
import com.zygon.trade.strategy.TradeAgent2;
import java.util.ArrayList;
import java.util.List;
import org.joda.money.CurrencyUnit;

/**
 *
 * @author zygon
 */
public class MtGoxStack extends InformationModule {

    private static InformationManager getInformationLayer(String name, DataModule data) {
        
        MarketConditions marketConditions = new MarketConditions(Currencies.BTC);
        ExecutionController execController = new ExecutionController(Currencies.USD, new SimulationBinding("joe", CurrencyUnit.USD, 1000.0, marketConditions));
        
        List<TradeAgent2> traders = new ArrayList<>();
        List<Trade2> trades = new ArrayList<>();
        
        trades.add(new Trade2(marketConditions, new MACDTradeImpl("user-id-1", execController)));
        traders.add(new TradeAgent2(trades));
        
        InformationManager mgmt = new InformationManager(name, marketConditions, traders);
        data.getDataManager().setInfoHandler(mgmt);

        return mgmt;
    }

    public MtGoxStack(String name, DataModule data) {
        super(name, data, getInformationLayer("mtgox_information_layer", data));
    }
}
