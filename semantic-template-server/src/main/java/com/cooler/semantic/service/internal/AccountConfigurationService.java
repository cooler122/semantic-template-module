package com.cooler.semantic.service.internal;

import com.cooler.semantic.entity.AccountConfiguration;

public interface AccountConfigurationService extends BaseService<AccountConfiguration>{
    AccountConfiguration selectAIdUId(Integer coreAccountId, Integer userId);
}
