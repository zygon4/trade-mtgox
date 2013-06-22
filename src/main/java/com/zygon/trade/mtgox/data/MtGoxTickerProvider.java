/**
 * 
 */

package com.zygon.trade.mtgox.data;

import com.zygon.trade.db.Database;
import com.zygon.trade.market.data.Ticker;
import com.zygon.trade.market.data.TradeableIndex;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 */
public class MtGoxTickerProvider extends MtGoxDataProvider<Ticker> {

    public MtGoxTickerProvider(String tradableIdentifier, String currency) {
        super("Ticker Provider", 30, TimeUnit.SECONDS, tradableIdentifier, currency);
    }

    @Override
    protected void getHistoric(List<Ticker> historic) {
        Database db = this.getDatabase();
        
        if (db != null) {
//            historic.addAll(db.retrieve(Ticker.class, "SELECT t FROM Ticker t where t.ts < " + System.currentTimeMillis()));
            historic.addAll(db.retrieve(Ticker.class, "SELECT t FROM Ticker t"));
        }
    }

    private static TradeableIndex create(com.xeiam.xchange.dto.marketdata.Ticker tick) {
        TradeableIndex idx = new TradeableIndex();
        idx.setIdentifer(tick.getTradableIdentifier());
        idx.setTs(tick.getTimestamp().getTime());
        return idx;
    }
    
    @Override
    public Ticker getData() {
        com.xeiam.xchange.dto.marketdata.Ticker mtgoxTick = this.getService().getTicker(this.getTradableIdentifier(), this.getCurrency());
        
        return new Ticker (create(mtgoxTick), mtgoxTick.getLast(), mtgoxTick.getBid(), 
                mtgoxTick.getAsk(), mtgoxTick.getHigh(), mtgoxTick.getLow(), mtgoxTick.getVolume());
    }
}
