package enthralling.rest;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;

import javax.inject.*;

import restx.annotations.*;
import restx.RestxContext;
import restx.RestxRequest;
import restx.RestxRequestMatch;
import restx.RestxResponse;
import restx.StdRestxRequestMatcher;
import restx.StdRoute;
import restx.factory.Component;
import restx.http.HTTP;
import restx.factory.Component;
import restx.security.PermitAll;
import restx.security.RolesAllowed;
import restx.security.RestxSession;
import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.remote.ev3.RMIRemoteRegulatedMotor;
import lejos.remote.ev3.RemoteEV3;

@Component @RestxResource
public class EV3Resource {
    
    @GET("/EV3/motor_rotate")
    @PermitAll
    public String motor_rotate(String address, String port, String motorType, int angle, boolean immediateReturn){
    	try{
    		getMotor(address,port,motorType).rotate(angle, immediateReturn);
    	}
    	catch(Exception x){
    		return x.toString();
    	}
    	return "ok";
    }
    
    @GET("/EV3/motor_forward")
    @PermitAll
    public String motor_forward(String address, String port, String motorType){
    	try{
    		getMotor(address,port,motorType).forward();
    	}
    	catch(Exception x){
    		return x.toString();
    	}
    	return "ok";
    }
    
    @GET("/EV3/motor_backward")
    @PermitAll
    public String motor_backward(String address, String port, String motorType){
    	try{
    		getMotor(address,port,motorType).backward();
    	}
    	catch(Exception x){
    		return x.toString();
    	}
    	return "ok";
    }
    
    @GET("/EV3/motor_getLimitAngle")
    @PermitAll
    public String motor_getLimitAngle(String address, String port, String motorType){
    	try{
    		return new Integer(getMotor(address,port,motorType).getLimitAngle()).toString();
    	}
    	catch(Exception x){
    		return x.toString();
    	}
    }

    @GET("/EV3/motor_getMaxSpeed")
    @PermitAll
    public String motor_getMaxSpeed(String address, String port, String motorType){
    	try{
    		return new Float(getMotor(address,port,motorType).getMaxSpeed()).toString();
    	}
    	catch(Exception x){
    		return x.toString();
    	}
    }

    @GET("/EV3/motor_getSpeed")
    @PermitAll
    public String motor_getSpeed(String address, String port, String motorType){
    	try{
    		return new Integer(getMotor(address,port,motorType).getSpeed()).toString();
    	}
    	catch(Exception x){
    		return x.toString();
    	}
    }

    @GET("/EV3/motor_getTachoCount")
    @PermitAll
    public String motor_getTachoCount(String address, String port, String motorType){
    	try{
    		return new Integer(getMotor(address,port,motorType).getTachoCount()).toString();
    	}
    	catch(Exception x){
    		return x.toString();
    	}
    }

    @GET("/EV3/motor_isMoving")
    @PermitAll
    public String motor_isMoving(String address, String port, String motorType){
    	try{
    		return new Boolean(getMotor(address,port,motorType).isMoving()).toString();
    	}
    	catch(Exception x){
    		return x.toString();
    	}
    }
    
    @GET("/EV3/motor_isStalled")
    @PermitAll
    public String motor_isStalled(String address, String port, String motorType){
    	try{
    		return new Boolean(getMotor(address,port,motorType).isStalled()).toString();
    	}
    	catch(Exception x){
    		return x.toString();
    	}
    }
    
    @GET("/EV3/motor_resetTachoCount")
    @PermitAll
    public String motor_resetTachoCount(String address, String port, String motorType){
    	try{
    		getMotor(address,port,motorType).resetTachoCount();
    		return "ok";
    	}
    	catch(Exception x){
    		return x.toString();
    	}
    }
    
    @GET("/EV3/motor_rotateTo")
    @PermitAll
    public String motor_rotateTo(String address, String port, String motorType, int angle, boolean immediateReturn){
    	try{
    		getMotor(address,port,motorType).rotateTo(angle, immediateReturn);
    		return "ok";
    	}
    	catch(Exception x){
    		return x.toString();
    	}
    }
    
    @GET("/EV3/motor_setAcceleration")
    @PermitAll
    public String motor_setAcceleration(String address, String port, String motorType, int acceleration){
    	try{
    		getMotor(address,port,motorType).setAcceleration(acceleration);
    		return "ok";
    	}
    	catch(Exception x){
    		return x.toString();
    	}
    }
    
    @GET("/EV3/motor_setSpeed")
    @PermitAll
    public String motor_setSpeed(String address, String port, String motorType, int speed){
    	try{
    		getMotor(address,port,motorType).setSpeed(speed);
    		return "ok";
    	}
    	catch(Exception x){
    		return x.toString();
    	}
    }
    
    @GET("/EV3/motor_setStallThreshold")
    @PermitAll
    public String motor_setStallThreshold(String address, String port, String motorType, int error, int time){
    	try{
    		getMotor(address,port,motorType).setStallThreshold(error, time);
    		return "ok";
    	}
    	catch(Exception x){
    		return x.toString();
    	}
    }
    
    @GET("/EV3/motor_stop")
    @PermitAll
    public String motor_stop(String address, String port, String motorType, boolean immediateReturn){
    	try{
    		getMotor(address,port,motorType).stop(immediateReturn);
    		return "ok";
    	}
    	catch(Exception x){
    		return x.toString();
    	}
    }
    
    
    
    private static final RMIRegulatedMotor getMotor(String address, String port, char motorType) throws RemoteException, MalformedURLException, NotBoundException{
    	//return getEV3(address).createRegulatedMotor(port, motorType);
    	if(motors.containsKey(address + port)){
    		return motors.get(address + port);
    	}else{
    		RMIRegulatedMotor motor = getEV3(address).createRegulatedMotor(port, motorType);
    		motors.put(address + port, motor);
    		return motor;
    	}
    }
    
   
    
    private static final int maxTrials = 3;
    private static int currentTrial = 0;
    
    //<IPAddress,ev3>
    private static final HashMap<String,RemoteEV3>ev3s = new HashMap<String,RemoteEV3>();
    //<IPAddress|port>
    private static final HashMap<String,RMIRegulatedMotor>motors = new HashMap<String,RMIRegulatedMotor>();

    
    private static final RemoteEV3 getEV3(String address) throws RemoteException, MalformedURLException, NotBoundException{
    	if(ev3s.containsKey(address)){
    		return ev3s.get(address);
    	}else{
    		RemoteEV3 ev3 = new RemoteEV3(address);
    		ev3s.put(address, ev3);
    		return ev3;
    	}
    }
    
    private static final RMIRegulatedMotor getMotor(String address, String portName, String motorType) throws MalformedURLException, RemoteException, NotBoundException{
    	String key = address.concat(portName);
    	if( motors.containsKey(key)){
    		return motors.get(key);
    	}else{
    		RMIRegulatedMotor m;
   			m = getEV3(address).createRegulatedMotor(portName, motorType.charAt(0));
    		motors.put(key, m);
    		return m;
    	}
    	
    }    
  
}
