/**
 * 
 */

package com.zygon.trade.mtgox.data;

import au.com.bytecode.opencsv.CSVReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.joda.money.BigMoney;

/**
 *
 * @author zygon
 */
public class MtGoxTickerProvider extends MtGoxDataProvider<Ticker> {

    private final Object historicDataLock = new Object();
    private boolean hasHistoricData = false;
    
    public MtGoxTickerProvider(String tradableIdentifier, String currency) {
        super("Ticker Provider", 15, TimeUnit.SECONDS, tradableIdentifier, currency);
    }

    @Override
    public Collection<Ticker> getHistoric() {
        List<Ticker> historicTicks = new ArrayList<>();
        
        synchronized (this.historicDataLock) {
            if (this.hasHistoricData) {
        
                File[] tickerDataFiles = new File("/home/zygon/external/data/mtgoxticker").listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File file, String string) {
                        // ghetto file name filter, yes
                        return string.startsWith("ticker.");
                    }
                });

                Arrays.sort(tickerDataFiles);

                for (File tickerData : tickerDataFiles) {
                    CSVReader reader = null;
                    try {
                        reader = new CSVReader(new BufferedReader(new FileReader(tickerData)));

                        String[] line = null;
                        while ((line = reader.readNext()) != null) {
                            // magic indexes - woo
                            String tradeableId = line[0];
                            BigMoney bid = BigMoney.parse(line[1]);
                            BigMoney ask = BigMoney.parse(line[2]);
                            BigMoney high = BigMoney.parse(line[3]);
                            BigMoney low = BigMoney.parse(line[4]);
                            BigMoney last = BigMoney.parse(line[5]);
                            BigDecimal volume = new BigDecimal(line[6]);
                            long timestamp = Long.parseLong(line[7]);

                            Ticker ticker = new Ticker(tradeableId, last, bid, ask, high, low, volume, timestamp);
                            historicTicks.add(ticker);
                        }
                    } catch (IOException io) {
                        // TODO: log
                        io.printStackTrace();
                    } finally {
                        if (reader != null) {
                            try { reader.close(); } catch (IOException ignore) {}
                        }
                    }
                }

                this.hasHistoricData = false;
            }
        }
        
        return historicTicks;
    }
    
    @Override
    public Ticker getData() {
        return new Ticker (this.getService().getTicker(this.getTradableIdentifier(), this.getCurrency()));
    }

    @Override
    public boolean hasHistoricInformation() {
        return this.hasHistoricData;
    }
}
