package com.gkys.common.base;

import org.springframework.util.ClassUtils;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/24.
 */
public abstract class BaseModel<ID extends Serializable> implements Serializable{
    private static final long serialVersionUID = -2385301738895682536L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ID id;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {

        if (null == obj) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        Class<?> thisClass = ClassUtils.getUserClass(getClass());
        Class<?> objClass = ClassUtils.getUserClass(obj.getClass());

        if (!(ClassUtils.isAssignable(thisClass, objClass) || ClassUtils.isAssignable(objClass, thisClass))) {
            return false;
        }

        BaseModel<?> that = (BaseModel<?>) obj;

        return null == this.getId() ? false : this.getId().equals(that.getId());
    }
}
