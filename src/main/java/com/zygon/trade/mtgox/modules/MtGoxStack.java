/**
 *
 */
package com.zygon.trade.mtgox.modules;

import com.xeiam.xchange.Currencies;
import com.zygon.trade.execution.ExecutionController;
import com.zygon.trade.execution.MarketConditionsProvider;
import com.zygon.trade.execution.simulation.SimulationBinding;
import com.zygon.trade.market.model.indication.IndicationListener;
import com.zygon.trade.market.model.indication.InformationManager;
import com.zygon.trade.market.model.indication.Selector;
import com.zygon.trade.market.model.indication.market.MACD;
import com.zygon.trade.modules.data.DataModule;
import com.zygon.trade.modules.model.InformationModule;
import com.zygon.trade.mtgox.strategy.BullBearTrader;
import com.zygon.trade.mtgox.strategy.MACDSignalCrossProcessor;
import com.zygon.trade.mtgox.strategy.MACDZeroCrossProcessor;
import com.zygon.trade.strategy.TradeAgent;
import java.util.ArrayList;
import java.util.List;
import org.joda.money.CurrencyUnit;

/**
 *
 * @author zygon
 */
public class MtGoxStack extends InformationModule {

    private static InformationManager getInformationLayer(String name, DataModule data) {

        
        MarketConditionsProvider marketProvider = null;
        ExecutionController execController = new ExecutionController(Currencies.BTC, new SimulationBinding("joe", CurrencyUnit.USD, 1000.0, marketProvider));
        
        TradeAgent agent = new BullBearTrader("user-id-1", execController);
        
        List<IndicationListener> indications = new ArrayList<>();

        IndicationListener<MACD> macdZeroCross = new IndicationListener<>("macdZeroCross", new Selector(MACD.ID), new MACDZeroCrossProcessor(agent));
        IndicationListener<MACD> macdSignalCross = new IndicationListener<>("macdSignalCross", new Selector(MACD.ID), new MACDSignalCrossProcessor(agent));

        indications.add(macdZeroCross);
        indications.add(macdSignalCross);

        InformationManager mgmt = new InformationManager(name, indications);
        data.getDataManager().setInfoHandler(mgmt);

        return mgmt;
    }

    public MtGoxStack(String name, DataModule data) {
        super(name, data, getInformationLayer("mtgox_information_layer", data));
    }
}
