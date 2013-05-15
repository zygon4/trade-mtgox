/**
 *
 */
package com.zygon.trade.mtgox.modules;

import com.zygon.trade.market.model.indication.IndicationListener;
import com.zygon.trade.market.model.indication.InformationManager;
import com.zygon.trade.market.model.indication.Selector;
import com.zygon.trade.market.model.indication.numeric.SMA15Min;
import com.zygon.trade.market.model.indication.numeric.SMA60Min;
import com.zygon.trade.market.model.indication.numeric.SimpleMovingAverage;
import com.zygon.trade.modules.data.DataModule;
import com.zygon.trade.modules.model.InformationModule;
import com.zygon.trade.mtgox.strategy.SimpleSMAProcessor;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zygon
 */
public class MtGoxStack extends InformationModule {

    private static InformationManager getInformationLayer(String name, DataModule data) {

        List<IndicationListener> indications = new ArrayList<>();

        IndicationListener<SimpleMovingAverage> listener = new IndicationListener<>("sma", new Selector(SMA15Min.ID, SMA60Min.ID), new SimpleSMAProcessor());

        indications.add(listener);

        InformationManager mgmt = new InformationManager(name, indications);
        data.getDataManager().setInfoHandler(mgmt);

        return mgmt;
    }

    public MtGoxStack(String name, DataModule data) {
        super(name, data, getInformationLayer("mtgox_information_layer", data));
    }
}
