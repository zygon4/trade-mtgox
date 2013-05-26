/**
 * 
 */

package com.zygon.trade.mtgox.execution;

import com.xeiam.xchange.dto.account.AccountInfo;
import com.xeiam.xchange.mtgox.v2.service.polling.MtGoxPollingAccountService;
import com.zygon.trade.execution.AccountController;

/**
 *
 * @author zygon
 */
public class MtGoxAcctController implements AccountController {

    private final MtGoxPollingAccountService accountService;

    public MtGoxAcctController(MtGoxPollingAccountService accntService) {
        this.accountService = accntService;
    }
    
    @Override
    public AccountInfo getAccountInfo() {
        return this.accountService.getAccountInfo();
    }
}
