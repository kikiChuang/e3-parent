package com.jddfun.game.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MACHINE on 2017/4/11.
 */

public class ProfitRulesBean implements Serializable {


    String ruleDescBottom;
    String ruleDescTop;
    List<RuleDetailsBean> ruleDetails;

    public String getRuleDescBottom() {
        return ruleDescBottom;
    }

    public void setRuleDescBottom(String ruleDescBottom) {
        this.ruleDescBottom = ruleDescBottom;
    }

    public String getRuleDescTop() {
        return ruleDescTop;
    }

    public void setRuleDescTop(String ruleDescTop) {
        this.ruleDescTop = ruleDescTop;
    }

    public List<RuleDetailsBean> getRuleDetails() {
        return ruleDetails;
    }

    public void setRuleDetails(List<RuleDetailsBean> ruleDetails) {
        this.ruleDetails = ruleDetails;
    }

    public static class RuleDetailsBean {
        /**
         * awardsName : 奖励
         * condition : 盈利金叶子区间
         */

        String awardsName;
        String condition;

        public String getAwardsName() {
            return awardsName;
        }

        public void setAwardsName(String awardsName) {
            this.awardsName = awardsName;
        }

        public String getCondition() {
            return condition;
        }

        public void setCondition(String condition) {
            this.condition = condition;
        }
    }
}
