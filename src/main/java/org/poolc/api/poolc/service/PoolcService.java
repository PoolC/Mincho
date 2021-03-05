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

    public Poolc findPoolc() {
        return poolcRespository.findAll().get(0);
    }

    public void updatePoolc(PoolcUpdateValues updateValues) {
        Poolc poolc = findPoolc();
        poolc.update(updateValues);
        poolcRespository.flush();
    }
}
