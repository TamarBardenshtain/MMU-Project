package com.hit.client;

import com.hit.view.CacheUnitView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class CacheUnitClientObserver implements PropertyChangeListener {

    private final CacheUnitClient cUClient;

    public CacheUnitClientObserver() {
        cUClient = new CacheUnitClient();
    }
    @Override
    public void propertyChange(PropertyChangeEvent evt)
    {
        String res = null;
        CacheUnitView updateUI = (CacheUnitView) evt.getSource();
        try {
            res = cUClient.send(evt.getNewValue().toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            res = "false";
        }
        updateUI.updateUIData(res);
    }
}
