package com.example.hoyoung.eyeload;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;


public class LocalDataSource extends DataSource{
    private List<Marker> cachedMarkers = new ArrayList<Marker>();
    private static Bitmap icon = null;
    
    public LocalDataSource(Resources res) {
        if (res==null) throw new NullPointerException();
        
        createIcon(res);
    }
    
    protected void createIcon(Resources res) {
        if (res==null) throw new NullPointerException();
        
        icon=BitmapFactory.decodeResource(res, R.drawable.ic_launcher);
    }
    
    public List<Marker> getMarkers() {
        Marker atl = new IconMarker("ATL", 39.931269, -75.051261, 0, Color.DKGRAY, icon);
        cachedMarkers.add(atl);

        Marker d=new IconMarker("Fuck5",37.557926,126.999063,63,Color.YELLOW,icon);
        cachedMarkers.add(d);

        Marker home = new Marker("Mt Laurel", 39.95, -74.9, 0, Color.YELLOW);
        cachedMarkers.add(home);

        return cachedMarkers;
    }
}