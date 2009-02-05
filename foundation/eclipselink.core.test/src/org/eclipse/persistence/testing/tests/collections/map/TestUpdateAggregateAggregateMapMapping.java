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

import org.eclipse.persistence.sessions.UnitOfWork;
import org.eclipse.persistence.testing.framework.TestErrorException;
import org.eclipse.persistence.testing.models.collections.map.AggregateMapKey;
import org.eclipse.persistence.testing.models.collections.map.AggregateMapValue;
import org.eclipse.persistence.testing.models.collections.map.AggregateAggregateMapHolder;

public class TestUpdateAggregateAggregateMapMapping extends TestReadAggregateAggregateMapMapping {

    public void test(){
        UnitOfWork uow = getSession().acquireUnitOfWork();
        holders = uow.readAllObjects(AggregateAggregateMapHolder.class);
        AggregateAggregateMapHolder holder = (AggregateAggregateMapHolder)holders.get(0);
        AggregateMapKey key = new AggregateMapKey();
        key.setKey(11);
        holder.removeAggregateToAggregateMapItem(key);
        AggregateMapValue mapValue = new AggregateMapValue();
        mapValue.setValue(3);
        key = new AggregateMapKey();
        key.setKey(33);
        holder.addAggregateToAggregateMapItem(key, mapValue);
        uow.commit();
    }
    
    public void verify(){
        getSession().getIdentityMapAccessor().initializeIdentityMaps();
        holders = getSession().readAllObjects(AggregateAggregateMapHolder.class);
        AggregateAggregateMapHolder holder = (AggregateAggregateMapHolder)holders.get(0);
        AggregateMapKey key = new AggregateMapKey();
        key.setKey(11);
        if (holder.getAggregateToAggregateMap().containsKey(key)){
            throw new TestErrorException("Item that was removed is still present in map.");
        }
        key = new AggregateMapKey();
        key.setKey(33);
        AggregateMapValue value = (AggregateMapValue)holder.getAggregateToAggregateMap().get(key);
        if (value.getValue() != 3){
            throw new TestErrorException("Item was not correctly added to map");
        }
    }
    
}
