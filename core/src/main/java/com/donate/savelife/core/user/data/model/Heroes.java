package com.donate.savelife.core.user.data.model;

import java.util.List;

/**
 * Created by ravi on 07/12/16.
 */

public class Heroes {

    private final List<String> heroes;

    public Heroes(List<String> heroes) {
        this.heroes = heroes;
    }

    public List<String> getHeroes() {
        return heroes;
    }

    public int size() {
        return heroes.size() ;
    }
}
