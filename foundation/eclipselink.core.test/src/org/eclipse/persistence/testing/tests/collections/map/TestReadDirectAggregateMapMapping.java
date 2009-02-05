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
import org.eclipse.persistence.testing.models.collections.map.AggregateMapValue;
import org.eclipse.persistence.testing.models.collections.map.DirectAggregateMapHolder;

public class TestReadDirectAggregateMapMapping extends TestCase {
    
    protected List holders = null;
    
    public void setup(){
        UnitOfWork uow = getSession().acquireUnitOfWork();
        DirectAggregateMapHolder holder = new DirectAggregateMapHolder();
        AggregateMapValue value = new AggregateMapValue();
        value.setValue(1);
        holder.addDirectToAggregateMapItem(new Integer(1), value);
        value = new AggregateMapValue();
        value.setValue(2);
        holder.addDirectToAggregateMapItem(new Integer(2), value);
        uow.registerObject(holder);
        uow.commit();
        getSession().getIdentityMapAccessor().initializeAllIdentityMaps();
    }
    
    public void test(){
        holders = getSession().readAllObjects(DirectAggregateMapHolder.class);
    }
    
    public void verify(){
        if (holders == null || holders.size() != 1){
            throw new TestErrorException("Incorrect number of MapHolders was read.");
        }
        DirectAggregateMapHolder holder = (DirectAggregateMapHolder)holders.get(0);
        
        if (holder.getDirectToAggregateMap().size() != 2){
            throw new TestErrorException("Incorrect Number of Map values was read.");
        }
        AggregateMapValue value = (AggregateMapValue)holder.getDirectToAggregateMap().get(new Integer(1));
        if (value.getValue() != 1){
            throw new TestErrorException("Incorrect map value was read.");
        }
    }
    
    public void reset(){
        UnitOfWork uow = getSession().acquireUnitOfWork();
        uow.deleteAllObjects(holders);
        uow.commit();
    }

}

