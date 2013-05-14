/**
 * 
 */

package com.zygon.trade.mtgox.modules;

import au.com.bytecode.opencsv.CSVWriter;
import com.xeiam.xchange.Currencies;
import com.zygon.trade.market.data.DataListener;
import com.zygon.trade.market.data.DataProcessor;
import com.zygon.trade.market.data.DataProcessor.Interpreter;
import com.zygon.trade.market.data.provider.AbstractDataProvider;
import com.zygon.trade.market.data.provider.DataLogger;
import com.zygon.trade.market.model.indication.Aggregation;
import com.zygon.trade.modules.data.DataModule;
import com.zygon.trade.mtgox.data.MtGoxTickerProvider;
import com.zygon.trade.mtgox.data.Ticker;
import com.zygon.trade.mtgox.data.interpreter.TickerSMAInterpreter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    
    private static DataListener createListener() {
        AbstractDataProvider<Ticker> provider = new MtGoxTickerProvider(Currencies.BTC, Currencies.USD);
        
        provider.setLogger(new TickerLogger());
        
        List<Interpreter> interpreters = new ArrayList<>();
        interpreters.add(new TickerSMAInterpreter(new Aggregation(Aggregation.Type.AVG, 15, TimeUnit.MINUTES)));
        interpreters.add(new TickerSMAInterpreter(new Aggregation(Aggregation.Type.AVG, 60, TimeUnit.MINUTES)));
        
        List<DataProcessor> dataHandlers = new ArrayList<>();
        dataHandlers.add(new DataProcessor("mtgox_ticker_data_handler", interpreters));
        
        return new DataListener("MtGox Ticker mgmr", provider, dataHandlers);
    }

    public MtGoxTickerData(String name) {
        super(name, createListener());
    }
}
