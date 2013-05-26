/**
 *
 */
package com.zygon.trade.mtgox.modules;

import com.zygon.trade.market.model.indication.InformationManager;
import com.zygon.trade.modules.data.DataModule;
import com.zygon.trade.modules.model.InformationModule;

/**
 *
 * @author zygon
 */
public class MtGoxStack extends InformationModule {
    
    public MtGoxStack(String name, DataModule data, InformationManager infoLayer) {
        super(name, data, infoLayer);
    }
}
