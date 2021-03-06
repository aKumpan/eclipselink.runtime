/*******************************************************************************
 * Copyright (c) 2012, 2015 Oracle and/or its affiliates. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0
 * which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     11/22/2012-2.5 Guy Pelletier
 *       - 389090: JPA 2.1 DDL Generation Support (index metadata support)
 ******************************************************************************/
package org.eclipse.persistence.testing.models.jpa21.advanced.xml.ddl;

import java.util.ArrayList;
import java.util.List;

public class Fork extends Utensil {
    public Double price;
    public Double rental;

    public String style;
    public String color;

    public List<String> uses;
    public List<ForkUser> users;

    public Fork() {
        users = new ArrayList<ForkUser>();
    }

    public void addUser(ForkUser user) {
        users.add(user);
    }

    public String getColor() {
        return color;
    }

    public Double getPrice() {
        return price;
    }

    public Double getRental() {
        return rental;
    }

    public String getStyle() {
        return style;
    }

    public List<ForkUser> getUsers() {
        return users;
    }

    public List<String> getUses() {
        return uses;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setRental(Double rental) {
        this.rental = rental;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public void setUsers(List<ForkUser> users) {
        this.users = users;
    }

    public void setUses(List<String> uses) {
        this.uses = uses;
    }
}
