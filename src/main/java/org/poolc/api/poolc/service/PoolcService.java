package org.poolc.api.poolc.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.poolc.domain.Poolc;
import org.poolc.api.poolc.repository.PoolcRespository;
import org.poolc.api.poolc.vo.PoolcCreateValues;
import org.poolc.api.poolc.vo.PoolcUpdateValues;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PoolcService {
    private final PoolcRespository poolcRespository;

    public void createPoolc(PoolcCreateValues createValues) {
        poolcRespository.save(Poolc.of(createValues));
    }

    //TODO: refactoring 필요느껴짐
    public Poolc get() {
        return poolcRespository.findAll().get(0);
    }

    public void updatePoolc(PoolcUpdateValues updateValues) {
        Poolc poolc = get();
        poolc.update(updateValues);
        poolcRespository.flush();
    }
}