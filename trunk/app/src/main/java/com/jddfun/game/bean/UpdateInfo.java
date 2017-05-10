package com.jddfun.game.bean;

public class UpdateInfo {

    String url;//: "/XXXX/XXXXX",
    String name;//: "/XXXX/XXXXX",
    String description;//: "upgradedesc",
    int force;//: 0：选择更新，1-强制更新，9：最新版本
    String version;//: "v1.1"


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getForce() {
        return force;
    }

    public void setForce(int force) {
        this.force = force;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
