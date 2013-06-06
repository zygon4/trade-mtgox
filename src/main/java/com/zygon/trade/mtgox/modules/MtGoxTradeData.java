/**
 * 
 */

package com.zygon.trade.mtgox.modules;

import com.xeiam.xchange.currency.Currencies;
import com.xeiam.xchange.dto.marketdata.Trade;
import com.zygon.trade.db.DatabaseMetadata;
import com.zygon.trade.db.DatabaseMetadataImpl;
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
import java.util.Map;

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
    
    private static DatabaseMetadata getDBMeta() {
        DatabaseMetadataImpl meta = new DatabaseMetadataImpl();
        Map<String,String> options = meta.getProperties();
        
        options.put("keyspace", "mtgox");
        options.put("column_family", "trade");
        // These are a backup plan in case race-condition occurs - remove in
        // the future
        options.put("cluster_name", "Test Cluster");
        options.put("host", "localhost:9160");
        
        return meta;
    }
    
    public MtGoxTradeData(String name) {
        super(name, createDataListener(), getDBMeta());
    }
}
