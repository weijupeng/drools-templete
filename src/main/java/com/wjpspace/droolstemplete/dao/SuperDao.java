package com.wjpspace.droolstemplete.dao;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjpspace.droolstemplete.dao.mapper.SupperMapper;

import java.util.List;

/**
 * @author kancy
 * @date 12/5/2018 17:29
 */
public class SuperDao<M extends SupperMapper<T>, T> extends ServiceImpl<M, T> {
    protected List<T> find(final T obj) {
        return super.list(new Wrapper<T>() {
            private static final long serialVersionUID = 620139204341485003L;

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

            @Override
            public String getSqlSegment() {
                return null;
            }
        });
    }

    protected T findOne(final T obj) {
        return super.getOne(new Wrapper<T>() {
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

            @Override
            public String getSqlSegment() {
                return null;
            }
        }, true);
    }


}
