/**
 * 
 */

package com.zygon.trade.mtgox.modules;

import com.xeiam.xchange.currency.Currencies;
import com.zygon.trade.market.data.DataListener;
import com.zygon.trade.market.data.DataProcessor;
import com.zygon.trade.market.data.DataProcessor.Interpreter;
import com.zygon.trade.market.data.AbstractDataProvider;
import com.zygon.trade.market.data.PersistentDataLogger;
import com.zygon.trade.market.model.indication.Aggregation;
import com.zygon.trade.modules.data.DataModule;
import com.zygon.trade.mtgox.data.MtGoxTickerProvider;
import com.zygon.trade.market.data.Ticker;
import com.zygon.trade.mtgox.data.interpreter.BBInterpreter;
import com.zygon.trade.mtgox.data.interpreter.TickerMACD;
import com.zygon.trade.mtgox.data.interpreter.TickerPriceInterpreter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 */
public class MtGoxTickerData extends DataModule {
    
    private static DataListener createListener() {
        AbstractDataProvider<Ticker> provider = new MtGoxTickerProvider(Currencies.BTC, Currencies.USD);
        
        PersistentDataLogger dataLogger = new PersistentDataLogger();
        
        List<Interpreter> interpreters = new ArrayList<>();
        interpreters.add(new TickerMACD(
                new Aggregation(Aggregation.Type.AVG, Aggregation.Duration._1, TimeUnit.DAYS),
                new Aggregation(Aggregation.Type.AVG, Aggregation.Duration._4, TimeUnit.DAYS),
                new Aggregation(Aggregation.Type.AVG, Aggregation.Duration._4, TimeUnit.HOURS)
                ));
        interpreters.add(new TickerPriceInterpreter());
        interpreters.add(new BBInterpreter(new Aggregation(Aggregation.Type.AVG, Aggregation.Duration._1, TimeUnit.DAYS), 3));
//        interpreters.add(new RSIInterpreter(new Aggregation(Aggregation.Type.AVG, Aggregation.Duration._1, TimeUnit.HOURS)));
//        interpreters.add(new VolatilityInterpreter(new Aggregation(Aggregation.Type.AVG, Aggregation.Duration._1, TimeUnit.DAYS)));
        
        List<DataProcessor> dataHandlers = new ArrayList<>();
        dataHandlers.add(new DataProcessor("mtgox_ticker_data_handler", interpreters));
        
        return new DataListener("MtGox Ticker mgmr", provider, dataLogger, dataHandlers);
    }
    
    public MtGoxTickerData(String name) {
        super(name, createListener());
    }
}
