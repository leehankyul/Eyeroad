package com.example.hoyoung.eyeload;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;
import android.util.Log;

import java.text.DecimalFormat;

public class Marker implements Comparable<Marker> {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("@#");
        
    private static final Vector symbolVector = new Vector(0, 0, 0);
    private static final Vector textVector = new Vector(0, 1, 0);

    private final Vector screenPositionVector = new Vector();
    private final Vector tmpSymbolVector = new Vector();
    private final Vector tmpVector = new Vector();
    private final Vector tmpTextVector = new Vector();
    private final float[] distanceArray = new float[1];
    private final float[] locationArray = new float[3];
    private final float[] screenPositionArray = new float[3];

    private float initialY = 0.0f;
    
    private volatile static CameraModel cam = null;

    private volatile com.example.hoyoung.eyeload.PaintableBoxedText textBox = null;
    private volatile com.example.hoyoung.eyeload.PaintablePosition textContainer = null;

    protected final float[] symbolArray = new float[3];
    protected final float[] textArray = new float[3];
    
    protected volatile PaintableObject gpsSymbol = null;
    protected volatile PaintablePosition symbolContainer = null;
    protected String name = null;
    protected volatile PhysicalLocationUtility physicalLocation = new PhysicalLocationUtility();
    protected volatile double distance = 0.0;
    protected volatile boolean isOnRadar = false;
    protected volatile boolean isInView = false;
    protected final Vector symbolXyzRelativeToCameraView = new Vector();
    protected final Vector textXyzRelativeToCameraView = new Vector();
    protected final Vector locationXyzRelativeToPhysicalLocation = new Vector();
    protected int color = Color.WHITE;

    private static boolean debugTouchZone = false;
    private static PaintableBox touchBox = null;
    private static PaintablePosition touchPosition = null;

    private static boolean debugCollisionZone = false;
    private static PaintableBox collisionBox = null;
    private static PaintablePosition collisionPosition = null;
    private Bitmap bitmap=null;
    private int markerType=0;//0: memo Marker, 1: Building Marker, 2: path Marker

    public Marker(String name, double latitude, double longitude, double altitude, int color,Bitmap bitmap) {
        set(name, latitude, longitude, altitude, color,bitmap);
    }
	public Marker(String name, double latitude, double longitude, double altitude, int color,Bitmap bitmap,int type) {
		set(name, latitude, longitude, altitude, color,bitmap);
        this.markerType=type;
	}

	public synchronized void set(String name, double latitude, double longitude, double altitude, int color,Bitmap bitmap) {
		if (name==null) throw new NullPointerException();

		this.name = name;
		this.physicalLocation.set(latitude,longitude,altitude);
		this.color = color;
		this.isOnRadar = false;
		this.isInView = false;
		this.symbolXyzRelativeToCameraView.set(0, 0, 0);
		this.textXyzRelativeToCameraView.set(0, 0, 0);
		this.locationXyzRelativeToPhysicalLocation.set(0, 0, 0);
		this.initialY = 0.0f;
        this.bitmap=bitmap;

	}

	public synchronized String getName(){
		return this.name;
	}

    public synchronized int getColor() {
    	return this.color;
    }

    public synchronized double getDistance() {
        return this.distance;
    }

    public synchronized float getInitialY() {
        return this.initialY;
    }
    public synchronized int getMarkerType() { return this.markerType; }


    public synchronized boolean isOnRadar() {
        return this.isOnRadar;
    }

    public synchronized boolean isInView() {
        return this.isInView;
    }


    public synchronized Vector getScreenPosition() {
        symbolXyzRelativeToCameraView.get(symbolArray);
        textXyzRelativeToCameraView.get(textArray);
        float x = (symbolArray[0] + textArray[0])/2;
        float y = (symbolArray[1] + textArray[1])/2;
        float z = (symbolArray[2] + textArray[2])/2;

        if (textBox!=null) y += (textBox.getHeight()/2);

        screenPositionVector.set(x, y, z);
        return screenPositionVector;
    }

    public synchronized Vector getLocation() {
        return this.locationXyzRelativeToPhysicalLocation;
    }

    public synchronized float getHeight() {
        if (symbolContainer==null || textContainer==null) return 0f;
        return symbolContainer.getHeight()+textContainer.getHeight();
    }
    
    public synchronized float getWidth() {
        if (symbolContainer==null || textContainer==null) return 0f;
        float w1 = textContainer.getWidth();
        float w2 = symbolContainer.getWidth();
        return (w1>w2)?w1:w2;
    }
    
    public synchronized void update(Canvas canvas, float addX, float addY) {
    	if (canvas==null) throw new NullPointerException();
    	
    	if (cam==null) cam = new CameraModel(canvas.getWidth(), canvas.getHeight(), true);
    	cam.set(canvas.getWidth(), canvas.getHeight(), false);
        cam.setViewAngle(CameraModel.DEFAULT_VIEW_ANGLE);
        populateMatrices(cam, addX, addY);
        updateRadar();
        updateView();
    }

	private synchronized void populateMatrices(CameraModel cam, float addX, float addY) {
		if (cam==null) throw new NullPointerException();
		
		tmpSymbolVector.set(symbolVector);
		tmpSymbolVector.add(locationXyzRelativeToPhysicalLocation);        
        tmpSymbolVector.prod(ARData.getRotationMatrix());
		
		tmpTextVector.set(textVector);
		tmpTextVector.add(locationXyzRelativeToPhysicalLocation);
		tmpTextVector.prod(ARData.getRotationMatrix());

		cam.projectPoint(tmpSymbolVector, tmpVector, addX, addY);
		symbolXyzRelativeToCameraView.set(tmpVector);
		cam.projectPoint(tmpTextVector, tmpVector, addX, addY);
		textXyzRelativeToCameraView.set(tmpVector);
	}

	private synchronized void updateRadar() {
		isOnRadar = false;

		float range = ARData.getRadius() * 1000;
		//float scale = range / Radar.RADIUS;
        float scale = range / 48;
		locationXyzRelativeToPhysicalLocation.get(locationArray);
        float x = locationArray[0] / scale;
        float y = locationArray[2] / scale; // z==y Switched on purpose 
        symbolXyzRelativeToCameraView.get(symbolArray);
		if ((symbolArray[2] < -1f) && ((x*x+y*y)<(48*48))) {
			isOnRadar = true;
		}
	}

    private synchronized void updateView() {
        isInView = false;

        symbolXyzRelativeToCameraView.get(symbolArray);
        float x1 = symbolArray[0] + (getWidth()/2);
        float y1 = symbolArray[1] + (getHeight()/2);
        float x2 = symbolArray[0] - (getWidth()/2);
        float y2 = symbolArray[1] - (getHeight()/2);
        if (x1>=-1 && x2<=(cam.getWidth()) 
            &&
            y1>=-1 && y2<=(cam.getHeight())
        ) {
            isInView = true;
        }
    }

    public synchronized void calcRelativePosition(Location location) {
		if (location==null) throw new NullPointerException();
		
	    updateDistance(location);
	    
		if (physicalLocation.getAltitude()==0.0) physicalLocation.setAltitude(location.getAltitude());
		 
		com.example.hoyoung.eyeload.PhysicalLocationUtility.convLocationToVector(location, physicalLocation, locationXyzRelativeToPhysicalLocation);
		this.initialY = locationXyzRelativeToPhysicalLocation.getY();
		updateRadar();
    }
    
    private synchronized void updateDistance(Location location) {
        if (location==null) throw new NullPointerException();

        Location.distanceBetween(physicalLocation.getLatitude(), physicalLocation.getLongitude(), location.getLatitude(), location.getLongitude(), distanceArray);
        distance = distanceArray[0];
    }

    public synchronized boolean handleClick(float x, float y) {
    	if (!isOnRadar || !isInView) return false;
        //if (!isInView) return false;
    	return isPointOnMarker(x,y,this);
    }

    public synchronized boolean isMarkerOnMarker(Marker marker) {
        return isMarkerOnMarker(marker,true);
    }

    private synchronized boolean isMarkerOnMarker(Marker marker, boolean reflect) {
        marker.getScreenPosition().get(screenPositionArray);
        float x = screenPositionArray[0];
        float y = screenPositionArray[1];        
        boolean middleOfMarker = isPointOnMarker(x,y,this);
        if (middleOfMarker) return true;

        float halfWidth = marker.getWidth()/2;
        float halfHeight = marker.getHeight()/2;

        float x1 = x - halfWidth;
        float y1 = y - halfHeight;
        boolean upperLeftOfMarker = isPointOnMarker(x1,y1,this);
        if (upperLeftOfMarker) return true;

        float x2 = x + halfWidth;
        float y2 = y1;
        boolean upperRightOfMarker = isPointOnMarker(x2,y2,this);
        if (upperRightOfMarker) return true;

        float x3 = x1;
        float y3 = y + halfHeight;
        boolean lowerLeftOfMarker = isPointOnMarker(x3,y3,this);
        if (lowerLeftOfMarker) return true;

        float x4 = x2;
        float y4 = y3;
        boolean lowerRightOfMarker = isPointOnMarker(x4,y4,this);
        if (lowerRightOfMarker) return true;

        return (reflect)?marker.isMarkerOnMarker(this,false):false;
    }

	private synchronized boolean isPointOnMarker(float x, float y, Marker marker) {
	    marker.getScreenPosition().get(screenPositionArray);
        float myX = screenPositionArray[0];
        float myY = screenPositionArray[1];
        float adjWidth = marker.getWidth()/2;
        float adjHeight = marker.getHeight()/2;

        float x1 = myX-adjWidth;
        float y1 = myY-adjHeight;
        float x2 = myX+adjWidth;
        float y2 = myY+adjHeight;

        if (x>=x1 && x<=x2 && y>=y1 && y<=y2) return true;
        
        return false;
	}

    public synchronized void draw(Canvas canvas) {
        if (canvas==null) throw new NullPointerException();

        if (!isOnRadar || !isInView) return;
        //if (!isInView) return;
        if (debugTouchZone) drawTouchZone(canvas);
        if (debugCollisionZone) drawCollisionZone(canvas);
        drawIcon(canvas);
        drawText(canvas);
    }

    protected synchronized void drawCollisionZone(Canvas canvas) {
        if (canvas==null) throw new NullPointerException();
        
        getScreenPosition().get(screenPositionArray);
        float x = screenPositionArray[0];
        float y = screenPositionArray[1];        

        float width = getWidth();
        float height = getHeight();
        float halfWidth = width/2;
        float halfHeight = height/2;

        float x1 = x - halfWidth;
        float y1 = y - halfHeight;

        float x2 = x + halfWidth;
        float y2 = y1;

        float x3 = x1;
        float y3 = y + halfHeight;

        float x4 = x2;
        float y4 = y3;

        Log.w("collisionBox", "ul (x="+x1+" y="+y1+")");
        Log.w("collisionBox", "ur (x="+x2+" y="+y2+")");
        Log.w("collisionBox", "ll (x="+x3+" y="+y3+")");
        Log.w("collisionBox", "lr (x="+x4+" y="+y4+")");
        
        if (collisionBox==null) collisionBox = new com.example.hoyoung.eyeload.PaintableBox(width,height,Color.WHITE,Color.RED);
        else collisionBox.set(width,height);

        float currentAngle = com.example.hoyoung.eyeload.Utilities.getAngle(symbolArray[0], symbolArray[1], textArray[0], textArray[1])+90;
        
        if (collisionPosition==null) collisionPosition = new com.example.hoyoung.eyeload.PaintablePosition(collisionBox, x1, y1, currentAngle, 1);
        else collisionPosition.set(collisionBox, x1, y1, currentAngle, 1);
        collisionPosition.paint(canvas);
    }

    protected synchronized void drawTouchZone(Canvas canvas) {
        if (canvas==null) throw new NullPointerException();
        
        if (gpsSymbol==null) return;
        
        symbolXyzRelativeToCameraView.get(symbolArray);
        textXyzRelativeToCameraView.get(textArray);        
        float x1 = symbolArray[0];
        float y1 = symbolArray[1];
        float x2 = textArray[0];
        float y2 = textArray[1];
        float width = getWidth();
        float height = getHeight();
        float adjX = (x1 + x2)/2;
        float adjY = (y1 + y2)/2;
        float currentAngle = com.example.hoyoung.eyeload.Utilities.getAngle(symbolArray[0], symbolArray[1], textArray[0], textArray[1])+90;
        adjX -= (width/2);
        adjY -= (gpsSymbol.getHeight()/2);
        
        Log.w("touchBox", "ul (x="+(adjX)+" y="+(adjY)+")");
        Log.w("touchBox", "ur (x="+(adjX+width)+" y="+(adjY)+")");
        Log.w("touchBox", "ll (x="+(adjX)+" y="+(adjY+height)+")");
        Log.w("touchBox", "lr (x="+(adjX+width)+" y="+(adjY+height)+")");
        
        if (touchBox==null) touchBox = new com.example.hoyoung.eyeload.PaintableBox(width,height,Color.WHITE,Color.GREEN);
        else touchBox.set(width,height);

        if (touchPosition==null) touchPosition = new com.example.hoyoung.eyeload.PaintablePosition(touchBox, adjX, adjY, currentAngle, 1);
        else touchPosition.set(touchBox, adjX, adjY, currentAngle, 1);
        touchPosition.paint(canvas);
    }
    
    protected synchronized void drawIcon(Canvas canvas) {
    	if (canvas==null||bitmap==null) throw new NullPointerException();

        //if (gpsSymbol==null) gpsSymbol = new PaintableGps(36, 36, true, getColor());
        if (gpsSymbol==null) gpsSymbol = new com.example.hoyoung.eyeload.PaintableIcon(bitmap,96,96);

        textXyzRelativeToCameraView.get(textArray);
        symbolXyzRelativeToCameraView.get(symbolArray);

        float currentAngle = com.example.hoyoung.eyeload.Utilities.getAngle(symbolArray[0], symbolArray[1], textArray[0], textArray[1]);
        float angle = currentAngle + 90;

        if (symbolContainer==null) symbolContainer = new com.example.hoyoung.eyeload.PaintablePosition(gpsSymbol, symbolArray[0], symbolArray[1], angle, 1);
        else symbolContainer.set(gpsSymbol, symbolArray[0], symbolArray[1], angle, 1);

        symbolContainer.paint(canvas);
    }

    protected synchronized void drawText(Canvas canvas) {
		if (canvas==null) throw new NullPointerException();
		
	    String textStr = null;
	    if (distance<1000.0) {
	        textStr = name + " ("+ DECIMAL_FORMAT.format(distance) + "m)";          
	    } else {
	        double d=distance/1000.0;
	        textStr = name + " (" + DECIMAL_FORMAT.format(d) + "km)";
	    }

	    textXyzRelativeToCameraView.get(textArray);
	    symbolXyzRelativeToCameraView.get(symbolArray);

	    float maxHeight = Math.round(canvas.getHeight() / 10f) + 1;
	    if (textBox==null) textBox = new com.example.hoyoung.eyeload.PaintableBoxedText(textStr, Math.round(maxHeight / 2f) + 1, 300);
	    else textBox.set(textStr, Math.round(maxHeight / 2f) + 1, 300);

	    float currentAngle = com.example.hoyoung.eyeload.Utilities.getAngle(symbolArray[0], symbolArray[1], textArray[0], textArray[1]);
        float angle = currentAngle + 90;

	    float x = textArray[0] - (textBox.getWidth() / 2);
	    float y = textArray[1] + maxHeight;

	    if (textContainer==null) textContainer = new com.example.hoyoung.eyeload.PaintablePosition(textBox, x, y, angle, 1);
	    else textContainer.set(textBox, x, y, angle, 1);
	    textContainer.paint(canvas);
	}

    public synchronized int compareTo(Marker another) {
        if (another==null) throw new NullPointerException();
        
        return name.compareTo(another.getName());
    }

    @Override
    public synchronized boolean equals(Object marker) {
        if(marker==null || name==null) throw new NullPointerException();
        
        return name.equals(((Marker)marker).getName());
    }
}