package com.jddfun.game.bean;

import java.util.List;

/**
 * Created by xuhongliang on 2017/4/26.
 */

public class DerbrisInfo {
    String fragmentHelp;

    List<Debris> fragmentBag;

    public String getFragmentHelp() {
        return fragmentHelp;
    }

    public void setFragmentHelp(String fragmentHelp) {
        this.fragmentHelp = fragmentHelp;
    }

    public List<Debris> getFragmentBag() {
        return fragmentBag;
    }

    public void setFragmentBag(List<Debris> fragmentBag) {
        this.fragmentBag = fragmentBag;
    }
}
