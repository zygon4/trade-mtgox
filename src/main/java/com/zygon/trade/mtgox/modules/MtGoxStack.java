/**
 * 
 */

package com.zygon.trade.mtgox.modules;

import com.xeiam.xchange.Currencies;
import com.zygon.trade.market.model.indication.IndicationListener;
import com.zygon.trade.market.model.indication.InformationManager;
import com.zygon.trade.market.model.indication.numeric.NumericIndications;
import com.zygon.trade.modules.data.DataModule;
import com.zygon.trade.modules.model.InformationModule;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zygon
 */
public class MtGoxStack extends InformationModule {

    private static final NumericIndications INDICATIONS = new NumericIndications(Currencies.BTC);
    
    private static InformationManager getInformationLayer(String name, DataModule data) {
        
        List<IndicationListener> indications = new ArrayList<>();

        indications.add(INDICATIONS.SMA_15_SEC);
        
        InformationManager mgmt = new InformationManager(name, indications);
        data.getDataManager().setInfoHandler(mgmt);
        
        return mgmt;
    }
    
    public MtGoxStack(String name, DataModule data) {
        super(name, data, getInformationLayer("mtgox_information_layer", data));
    }
}
