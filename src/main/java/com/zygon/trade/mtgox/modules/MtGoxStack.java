/**
 *
 */
package com.zygon.trade.mtgox.modules;

import com.zygon.trade.market.model.indication.IndicationListener;
import com.zygon.trade.market.model.indication.InformationManager;
import com.zygon.trade.market.model.indication.Selector;
import com.zygon.trade.market.model.indication.market.MACD;
import com.zygon.trade.modules.data.DataModule;
import com.zygon.trade.modules.model.InformationModule;
import com.zygon.trade.mtgox.strategy.MACDSignalCrossProcessor;
import com.zygon.trade.mtgox.strategy.MACDZeroCrossProcessor;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zygon
 */
public class MtGoxStack extends InformationModule {

    private static InformationManager getInformationLayer(String name, DataModule data) {

        List<IndicationListener> indications = new ArrayList<>();

        IndicationListener<MACD> macdZeroCross = new IndicationListener<>("macdZeroCross", new Selector(MACD.ID), new MACDZeroCrossProcessor());
        IndicationListener<MACD> macdSignalCross = new IndicationListener<>("macdSignalCross", new Selector(MACD.ID), new MACDSignalCrossProcessor());

        indications.add(macdZeroCross);
        indications.add(macdSignalCross);

        InformationManager mgmt = new InformationManager(name, indications);
        data.getDataManager().setInfoHandler(mgmt);

        return mgmt;
    }

    public MtGoxStack(String name, DataModule data) {
        super(name, data, getInformationLayer("mtgox_information_layer", data));
    }
}
