package com.donate.savelife.core.user.data.model;

import java.util.List;

/**
 * Created by ravi on 07/12/16.
 */

public class Heros {

    private final List<String> heros;

    public Heros(List<String> heros) {
        this.heros = heros;
    }

    public List<String> getHeros() {
        return heros;
    }

    public int size() {
        return heros.size() ;
    }
}
