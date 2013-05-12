/**
 * 
 */

package com.zygon.trade.mtgox.modules;

import au.com.bytecode.opencsv.CSVWriter;
import com.xeiam.xchange.Currencies;
import com.zygon.exchange.market.data.DataManager;
import com.zygon.exchange.market.data.DataProcessor;
import com.zygon.exchange.market.data.DataProcessor.Interpreter;
import com.zygon.exchange.market.data.provider.AbstractDataProvider;
import com.zygon.exchange.market.data.provider.DataLogger;
import com.zygon.exchange.modules.data.DataModule;
import com.zygon.trade.mtgox.data.MtGoxTickerProvider;
import com.zygon.trade.mtgox.data.Ticker;
import com.zygon.trade.mtgox.data.interpreter.TickerPriceInterpreter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author zygon
 */
public class MtGoxTickerData extends DataModule {

    private static final class TickerLogger implements DataLogger<Ticker> {

        private static String getDateTime() {  
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            return df.format(new Date());
        }
        
        @Override
        public void log(Ticker data) {
            CSVWriter writer = null;
            
            try {
                writer = new CSVWriter(new BufferedWriter(new FileWriter("/tmp/ticker."+getDateTime(), true)));
                
                String[] line = new String[] {
                    data.getTradableIdentifier(), 
                    data.getBid().toString(), 
                    data.getAsk().toString(),
                    data.getHigh().toString(),
                    data.getLow().toString(),
                    data.getLast().toString(),
                    data.getVolume().toPlainString(),
                    String.valueOf(data.getTimestamp())
                };
                
                writer.writeNext(line);
                
            } catch (IOException io) {
                //TODO: log, don't dump stack traces asshole.
                io.printStackTrace();
            } finally {
                if (writer != null) {
                    try { writer.close(); } catch (IOException io) {}
                }
            }
        }
    }
    
    private static DataManager createDataManager() {
        AbstractDataProvider<Ticker> provider = new MtGoxTickerProvider(Currencies.BTC, Currencies.USD);
        
        provider.setLogger(new TickerLogger());
        
        List<Interpreter> interpreters = new ArrayList<>();
        interpreters.add(new TickerPriceInterpreter());
        
        List<DataProcessor> dataHandlers = new ArrayList<>();
        dataHandlers.add(new DataProcessor("mtgox_ticker_data_handler", interpreters));
        
        return new DataManager("MtGox Ticker mgmr", provider, dataHandlers);
    }

    public MtGoxTickerData(String name) {
        super(name, createDataManager());
    }
}
