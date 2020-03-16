package com.wjpspace.droolstemplete.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjpspace.droolstemplete.dao.mapper.SuperMapper;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author Isaac
 * @description Dao基类
 * @date 30/4/2019 17:29
 */
public abstract class BaseDAO<M extends SuperMapper<T>, T> extends ServiceImpl<M, T> {

    protected Optional<T> selectById(Serializable id) {
        return Optional.ofNullable(baseMapper.selectById(id));
    }

    protected List<T> selectBatchIds(Collection<? extends Serializable> idList) {
        return Optional.ofNullable(baseMapper.selectBatchIds(idList)).orElse(new ArrayList<>());
    }

    protected List<T> list(T obj) {
        return Optional.ofNullable(baseMapper.selectList(new Wrapper<T>() {
            private static final long serialVersionUID = 3350538863421786036L;

            @Override
            public String getSqlSegment() {
                return null;
            }

            @Override
            public T getEntity() {
                return obj;
            }

            @Override

            public MergeSegments getExpression() {
                return null;
            }

            @Override
            public String getCustomSqlSegment() {
                return null;
            }
        })).orElse(new ArrayList<>());
    }

    protected Optional<T> selectOne(T obj) {
        List<T> list = this.list(obj);
        if (!CollectionUtils.isEmpty(list)) {
            return Optional.of(list.get(0));
        } else {
            return Optional.empty();
        }
    }
}