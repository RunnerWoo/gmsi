/*
 * Copyright (C) 2016 Wu Yan Chang(吴彦昌) <wuyanchang@hotmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runnerwoo.rfid.entity;

/**
 *
 * @author Wu Yan Chang(吴彦昌) <wuyanchang@hotmail.com>
 */
public class Goods {

    private long id;
    private String name;
    private String tag;
    private long inTime;
    private long outTime;

    public Goods() {
    }
    
    public Goods(long id, String name, String tag, long inTime, long outTime) {
        this.id = id;
        this.name = name;
        this.tag = tag;
        this.inTime = inTime;
        this.outTime = outTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public long getInTime() {
        return inTime;
    }

    public void setInTime(long inTime) {
        this.inTime = inTime;
    }

    public long getOutTime() {
        return outTime;
    }

    public void setOutTime(long outTime) {
        this.outTime = outTime;
    }

    @Override
    public String toString() {
        return "Goods{" + "id=" + id + ", name=" + name + ", tag=" + tag + ", inTime=" + inTime + ", outTime=" + outTime + '}';
    }
}
