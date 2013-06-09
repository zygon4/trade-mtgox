/**
 * 
 */

package com.zygon.trade.mtgox.data;

import com.zygon.trade.market.Message;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.joda.money.BigMoney;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author zygon
 */

@Entity
@Table(name="ticker", schema = "mtgox@cassandra_pu")
public class Ticker extends Message implements Serializable {
    
    @Column(name="tradeable_id")
    private String tradableIdentifier;
    @Column(name="last_price")
    private BigMoney last;
    @Column(name="bid")
    private BigMoney bid;
    @Column(name="ask")
    private BigMoney ask;
    @Column(name="high")
    private BigMoney high;
    @Column(name="low")
    private BigMoney low;
    @Column(name="volume")
    private BigDecimal volume;
    
    @Id
    @Column(name="ts")
    private long ts;
    
    public Ticker(String tradableIdentifier, BigMoney last, BigMoney bid, BigMoney ask, BigMoney high, BigMoney low, BigDecimal volume, long timestamp) {
        this.tradableIdentifier = tradableIdentifier;
        this.last = last;
        this.bid = bid;
        this.ask = ask;
        this.high = high;
        this.low = low;
        this.volume = volume;
        this.ts = timestamp;
    }

    public Ticker(String tradableIdentifier, BigMoney last, BigMoney bid, BigMoney ask, BigMoney high, BigMoney low, BigDecimal volume) {
        this(tradableIdentifier, last, bid, ask, high, low, volume, System.currentTimeMillis());
    }
    
    public Ticker (com.xeiam.xchange.dto.marketdata.Ticker tick) {
        this (tick.getTradableIdentifier(), tick.getLast(), tick.getBid(), tick.getAsk(), 
                tick.getHigh(), tick.getLow(), tick.getVolume(), tick.getTimestamp().getTime());
    }

    public Ticker() {
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
        return ts;
    }

    public void setAsk(BigMoney ask) {
        this.ask = ask;
    }

    public void setBid(BigMoney bid) {
        this.bid = bid;
    }

    public void setHigh(BigMoney high) {
        this.high = high;
    }

    public void setLast(BigMoney last) {
        this.last = last;
    }

    public void setLow(BigMoney low) {
        this.low = low;
    }

    public void setTradableIdentifier(String tradableIdentifier) {
        this.tradableIdentifier = tradableIdentifier;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return String.format("%s: last %s, bid %s, ask %s, high %s, low %s, volume %s %s", 
                this.tradableIdentifier,
                this.last.getAmount().toPlainString(),
                this.bid.getAmount().toPlainString(),
                this.ask.getAmount().toPlainString(),
                this.high.getAmount().toPlainString(),
                this.low.getAmount().toPlainString(),
                this.volume.toPlainString(),
                new Date(this.ts));
    }
}
