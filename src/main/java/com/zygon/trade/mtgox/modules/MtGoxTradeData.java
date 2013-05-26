/**
 * 
 */

package com.zygon.trade.mtgox.modules;

import com.xeiam.xchange.currency.Currencies;
import com.xeiam.xchange.dto.marketdata.Trade;
import com.zygon.trade.market.data.DataListener;
import com.zygon.trade.market.data.DataProcessor;
import com.zygon.trade.market.data.DataProcessor.Interpreter;
import com.zygon.trade.market.data.DataProvider;
import com.zygon.trade.modules.data.DataModule;
import com.zygon.trade.mtgox.data.MtGoxTradeProvider;
import com.zygon.trade.mtgox.data.interpreter.TradePriceInterpreter;
import com.zygon.trade.mtgox.data.interpreter.TradeVolumeInterpreter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zygon
 */
public class MtGoxTradeData extends DataModule {

    private static DataListener createDataListener() {
        DataProvider<Trade> provider = new MtGoxTradeProvider(Currencies.BTC, Currencies.USD);
        
        List<Interpreter<Trade>> interpreters = new ArrayList<>();
        interpreters.add(new TradePriceInterpreter());
        interpreters.add(new TradeVolumeInterpreter());
        
        List<DataProcessor<Trade>> dataHandlers = new ArrayList<>();
        dataHandlers.add(new DataProcessor<>("mtgox_trade_data_handler", interpreters));
        
        return new DataListener<>("MtGox trade mgmr", provider, dataHandlers);
    }
    
    public MtGoxTradeData(String name) {
        super(name, createDataListener());
    }
}
