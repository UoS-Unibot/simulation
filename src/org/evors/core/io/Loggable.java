/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.core.io;

import java.util.List;

/**
 *
 * @author miles
 */
public interface Loggable<T> {
    public List<String> getHeaders();
    public List<T> getDataRow();
}
