/**
 * 
 */

package com.zygon.trade.mtgox.modules;

import com.zygon.trade.market.model.indication.Classification;
import com.zygon.trade.market.model.indication.IndicationListener;
import com.zygon.trade.market.model.indication.InformationManager;
import com.zygon.trade.market.model.indication.Selector;
import com.zygon.trade.market.model.indication.market.MACD;
import com.zygon.trade.modules.data.DataModule;
import com.zygon.trade.modules.model.InformationModule;
import com.zygon.trade.mtgox.strategy.SimpleMACDZeroCross;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zygon
 */
public class MtGoxStack extends InformationModule {

//    private static final NumericIndications INDICATIONS = new NumericIndications(Currencies.BTC, new SimpleMACDZeroCross());
    
    private static InformationManager getInformationLayer(String name, DataModule data) {
        
        List<IndicationListener> indications = new ArrayList<>();

        IndicationListener<MACD> listener = new IndicationListener<>("macd", Classification.PRICE, new Selector(MACD.ID, Classification.PRICE), new SimpleMACDZeroCross());
        
        indications.add(listener);
         
//        indications.add(INDICATIONS.SMA_15_MIN);
//        indications.add(INDICATIONS.SMA_60_MIN);
        
        InformationManager mgmt = new InformationManager(name, indications);
        data.getDataManager().setInfoHandler(mgmt);
        
        return mgmt;
    }
    
    public MtGoxStack(String name, DataModule data) {
        super(name, data, getInformationLayer("mtgox_information_layer", data));
    }
}
