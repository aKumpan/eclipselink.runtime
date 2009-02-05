/*******************************************************************************
 * Copyright (c) 1998, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0 
 * which accompanies this distribution. 
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at 
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     tware - initial implementation
 ******************************************************************************/  
package org.eclipse.persistence.testing.tests.collections.map;

import java.util.List;

import org.eclipse.persistence.sessions.UnitOfWork;
import org.eclipse.persistence.testing.framework.TestCase;
import org.eclipse.persistence.testing.framework.TestErrorException;
import org.eclipse.persistence.testing.models.collections.map.EntityAggregateMapHolder;
import org.eclipse.persistence.testing.models.collections.map.EntityMapKey;
import org.eclipse.persistence.testing.models.collections.map.AggregateMapValue;

public class TestReadEntityAggregateMapMapping extends TestCase {
    
    protected List holders = null;
    
    public void setup(){
        UnitOfWork uow = getSession().acquireUnitOfWork();
        EntityAggregateMapHolder holder = new EntityAggregateMapHolder();
        AggregateMapValue value = new AggregateMapValue();
        value.setValue(1);
        EntityMapKey key = new EntityMapKey();
        key.setData("11");
        key.setId(1);
        holder.addEntityToAggregateMapItem(key, value);
        uow.registerObject(key);

        
        AggregateMapValue value2 = new AggregateMapValue();
        value2.setValue(2);
        key = new EntityMapKey();
        key.setData("22");
        key.setId(2);
        holder.addEntityToAggregateMapItem(key, value2);
        uow.registerObject(holder);
        uow.registerObject(key);
        uow.commit();
        getSession().getIdentityMapAccessor().initializeAllIdentityMaps();
    }
    
    public void test(){
        holders = getSession().readAllObjects(EntityAggregateMapHolder.class);
    }
    
    public void verify(){
        if (holders == null || holders.size() != 1){
            throw new TestErrorException("Incorrect number of MapHolders was read.");
        }
        EntityAggregateMapHolder holder = (EntityAggregateMapHolder)holders.get(0);
        
        if (holder.getEntityToAggregateMap().size() != 2){
            throw new TestErrorException("Incorrect Number of MapEntityValues was read.");
        }
        EntityMapKey mapKey = new EntityMapKey();
        mapKey.setData("11");
        mapKey.setId(1);
        AggregateMapValue value = (AggregateMapValue)holder.getEntityToAggregateMap().get(mapKey);
        if (value.getValue() != 1){
            throw new TestErrorException("Incorrect MapEntityValues was read.");
        }
    }
    
    public void reset(){
        UnitOfWork uow = getSession().acquireUnitOfWork();
        uow.deleteAllObjects(holders);
        uow.commit();
    }

}

