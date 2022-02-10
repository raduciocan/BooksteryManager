package com.clientmodule.view;

import com.clientmodule.queryutils.EntityMaster;

public class StagesMaster {
    private static StagesMaster instance = null;
    private EntityMaster entity;

    private StagesMaster() {
        entity = new EntityMaster();
    }

    synchronized public static StagesMaster getInstance() {
        if(instance == null)
            instance = new StagesMaster();
        return instance;
    }

    public EntityMaster getEntity() {
        return entity;
    }

}
