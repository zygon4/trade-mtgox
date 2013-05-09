/**
 * 
 */

package com.zygon.trade.mtgox.data;

import java.math.BigDecimal;
import java.util.Date;
import org.joda.money.BigMoney;

/**
 *
 * @author zygon
 */
public class Ticker {
    
    private final String tradableIdentifier;
    private final  BigMoney last;
    private final  BigMoney bid;
    private final  BigMoney ask;
    private final  BigMoney high;
    private final  BigMoney low;
    private final  BigDecimal volume;
    private final long timestamp;
    
    public Ticker(String tradableIdentifier, BigMoney last, BigMoney bid, BigMoney ask, BigMoney high, BigMoney low, BigDecimal volume, long timestamp) {
        this.tradableIdentifier = tradableIdentifier;
        this.last = last;
        this.bid = bid;
        this.ask = ask;
        this.high = high;
        this.low = low;
        this.volume = volume;
        this.timestamp = timestamp;
    }

    public Ticker(String tradableIdentifier, BigMoney last, BigMoney bid, BigMoney ask, BigMoney high, BigMoney low, BigDecimal volume) {
        this(tradableIdentifier, last, bid, ask, high, low, volume, System.currentTimeMillis());
    }
    
    public Ticker (com.xeiam.xchange.dto.marketdata.Ticker tick) {
        this (tick.getTradableIdentifier(), tick.getLast(), tick.getBid(), tick.getAsk(), 
                tick.getHigh(), tick.getLow(), tick.getVolume(), tick.getTimestamp().getTime());
    }

    public BigMoney getAsk() {
        return ask;
    }

    public BigMoney getBid() {
        return bid;
    }

    public BigMoney getHigh() {
        return high;
    }

    public BigMoney getLast() {
        return last;
    }

    public BigMoney getLow() {
        return low;
    }

    public String getTradableIdentifier() {
        return tradableIdentifier;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("%s, last %d, bid %d, ask %d, high %d, low %d, volume %d %s", 
                this.tradableIdentifier,
                this.last.getAmount().toPlainString(),
                this.bid.getAmount().toPlainString(),
                this.ask.getAmount().toPlainString(),
                this.high.getAmount().toPlainString(),
                this.low.getAmount().toPlainString(),
                this.volume.toPlainString(),
                new Date(this.timestamp));
    }
}
