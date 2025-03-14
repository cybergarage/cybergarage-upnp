# Programming Guide

## Introduction

UPnP™ architecture is based on open networking to enable discovery and control of networked devices and services, such as media servers and players at home.

UPnP™ architecture is based on many standard protocols, such as GENA, SSDP, SOAP, HTTPU and HTTP. Therefore you have to understand and implement these protocols to create your devices of UPnP™.

cybergarage-upnp is a development package for UPnP™ developers. The cybergarage-upnp controls these protocols automatically, and supports to create your devices and control points quickly. Please visit official [UPnP™ Forum](http://www.upnp.org/) site to know about UPnP™ in more detail.

## Device

The following static structure diagram is related classes of cybergarage-upnp to create your device of UPnP™. The device has some embedded devices and services, and the services have some actions and state variables.

![](img/device-class-overview.png)

The above static structure diagram is modified simplify to explain.

### Description

At first, you have to make some description files of your devices and the services when you want to create your UPnP™ device..

![](img/device-description.png)

The description of the root device should not have URLBase element because the element is added automatically when the device is created using the description.

The service descriptions are required to create a device, but the presentationURL and the iconList are recommended option. Please see UPnP™ specifications about the description format in more detail.

### Initiating

To create a UPnP™ device, create a instance of Device class with the root description file. The created device is a root device, and only root device can be active using Device::start(). The device is announced to the UPnP™ network when the device is started. The following shows an example of the initiating device.

```java
import org.cybergarage.upnp.*;
import org.cybergarage.upnp.device.*;

String descriptionFileName = "description/description.xml";

Try {
  Device upnpDev = new Device(descriptionFileName);
  ......
  upnpDev.start();
} catch (InvalidDescriptionException e){
  System.out.println("InvalidDescriptionException = " + e.getMessage());
}
```

The InvalidDescriptionException is occurred when the description is invalid. Use the getMessage() to know the exception reason in more
detail.

Alternatively, you can load the descriptions using Device::loadDescription() and Service::loadSCPD() instead of the description files as the following. The loading methods occur the exception when the loading is failed.

```java
String DEVICE_DESCRIPTION =
  "<?xml version=¥"1.0¥" ?>¥n" +
  "<root xmlns=¥"urn:schemas-upnp-org:device-1-0¥">¥n" +
  ....
  "</root>";

String SERVICE_DESCRIPTION[] =
  "<?xml version=¥"1.0¥"?>¥n" +
  "<scpd xmlns=¥"urn:schemas-upnp-org:service-1-0¥" >¥n" + ....
  ....
  "</scpd>";

try {
  Device upnpDev = new Device();
  boolean descSuccess = upnpDev.loadDescription(DEVICE_DESCRIPTION);
  Service upnpService = getService("urn:schemas-upnp-org:service:****:1");
  boolean scpdSuccess = upnpService.loadSCPD(SERVICE_DESCRIPTION[); 
} catch (InvalidDescriptionException e){
  System.out.println("InvalidDescriptionException = " + e.getMessage());
}
```

The active root device has some server processes, and returns the responses automatically when a control points sends a request to the device. For example, the device has a HTTP server to return the description files when a control point gets the description file. The device searches an available port for the HTTP server automatically on the machine when the device is started.

![](img/device-ports.png)

The root device is created with the following default parameters, you can change the parameters using the following methods before the root device is started.

| Parameter       | Default          | Setter Method     |
|-----------------|------------------|---------------------|
| HTTP port       | 4004             | setHTTPPort()       |
| Description URI | /description.xml | setDescriptionURI() |
| Lease time      | 1800             | setLeaseTime()      |

### Notify

Your device is announced using Device::start() to the UPnP™ network using a notify message with ssdp::alive automatically when the device is started. When device is stopped using Device::stop(), a notify message is posted with ssdp::byebye. You can announce the notify messages using Device::announce() and Device::byebye().

![](img/device-notify.png)

When a control point sends a search request with M-SEARCH to the UPnP™ network, the active device send the search response to the control point automatically. The device repeats the announcement in the lease time automatically.

### Embedded Devices

The devices may have some embedded devices. Use Device::getDeviceList() to get the embedded device list. The following example outputs friendly names of all embedded devices in the device.

```java
public void printDevice(Device dev) {
  String devName = dev.getFriendlyName();
  System.out.println(devName);
  DeviceList childDevList = dev.getDeviceList();
  int nChildDevs = childDevList.size();
  for (int n =0; n <nChildDevs; n ++) {
    Device childDev = childDevList.getDevice(n);
    printDevice(childDev);
  }
}
......
Dev rootDev = ....;
......
Device childDev = childDevList.getDevice(n); printDevice(childDev);
......
DeviceList childDevList = rootDev.getDeviceList();
int childDevs = childDevList.size();
for (int n=0; n< childDevs; n++) {
  Device childDev = rootDevList.getDevice(n);
  printDevice(childDev);
}
```

You can find a embedded device by the friendly name or UDN using Device::getDevice(). The following example gets a embedded device by the friendly name.

```java
Device homeServerDev ....
Device musicDev = homeServerDev.getDevice("music");
```

### Service

Use Device::getServiceList() to access embedded services of the device. The service may has some actions and state variables. Use
Service::getActionList() to get the actions, and use Service::getServiceStateTable() to the state variables. The following example outputs the all actions and state variables in a device.

```java
Device dev ....
ServiceList serviceList = dev.getServiceList();
int serviceCnt = serviceList.size();
for (int n=0; n<serviceCnt; n++) {
  Service service = serviceList.getService(n);
  ActionList actionList = service.getActionList();
  int actionCnt = actionList.size();
  for (int i=0; i<actionCnt; i++) {
    Action action = actionList.getAction(i);
    System.out.println("action [" + i + "] = " + action.getName()); 
  }
  ServiceStateTable stateTable = service. getServiceStateTable ();
  int varCnt = stateTable.size();
  for (int i=0; i<actionCnt; i++) {
    StateVariable stateVar = stateTable.getServiceStateVariable(i);
    System.out.println("stateVar [" + i + "] = " + stateVar.getName());
  }
}
```

You can find a service in the device by the service ID using Device::getService(), and you can find an action or state variable in
the service by name too. Use Device::getAction() or Service::getAction() to find the action, and use Device::getStateariable() or Service::getStateVariable() to find the state variable. The following example gets a service, a action and a state variable in a device by name.

```java
Device clockDev ....
Service timerSev = clockDev.getService("timer");
Action getTimeAct = clockDev.getAction("GetTime");
StateVariable timeStat = clockDev.getStateVariable("time");
```

### Control

To receive action control events from control points, the device needs to implement the ActionListener interface. The listener have to implement a actionControlReceived() that has the action and argument list parameter. The input arguments has the passed values from the control point, set the response values in the output arguments and return a true when the request is valid. Otherwise return a false when the request is invalid. UPnPError response is returned to the control point automatically when the returned value is false or the device has no the interface. The UPnPError is INVALID_ACTION as default, but use Action::setSetStatus() to return other UPnP errors.

To receive query control events from control points, the device needs to implement the QueryListener interface.

The listener have to implement a queryControlReceived() that has the service variable parameter and return a true when the request is valid. Otherwise return a false when the request is invalid. UPnPError response is returned to the control point automatically when the returned value is false or the device has no the interface

The UPnPError is INVALID_ACTION as default, but use ServiceVariable::setSetStatus() to return other UPnP errors. The following sample is a clock device. The device executes two action control requests and a query control request.

```java
public class ClockDevice extends Device implements ActionListener, QueryListener {
  public ClockDevice() {
    super ("/clock/www/description.xml");
    Action setTimeAction = getAction("SetTime");
    setTimeAction.setActionListener(this);
    Action getTimeAction = getAction("GetTime");
    getTimeAction.setActionListener(this);
    StateVariable stateVar = getStateVariable("Timer");
    stateVar.setQueryListener(this); 
  }

  public boolean actionControlRecieved(Action action) {
    ArgumentList argList = action.getArgumentList();
    String actionName = action.getName();
    if (actionName.equals("SetTime") == true) {
      Argument inTime = argList.getArgument("time");
      String timeValue = inTime.getValue();
      If (timeValue == null || timeValue.length() <= 0)
        return false;
      ........
      Argument outResult = argList.getArgument("result");
      arg.setValue("TRUE");
      return true;
    }
    else if (actionName.equals("GetTime") == true) {
      String currTimeStr = ..... 
      Argument currTimeArg = argList.getArgument("currTime");
      currTimeArg.setValue(currTimeStrs);
      return true;
    }
    action.setStatus(UPnP::INVALID_ACTION, ".....");
    return false;
  }

  public bool queryControlReceived(StateVariable stateVar) {
    if (varName.equals("Time") == true) {
      String currTimeStr = ....;
      stateVar.setValue(currTimeStr);
      return true;
    }
    stateVar.setStatus(UPnP::INVALID_VAR, ".....");
    return false;
  }
}
```

Use Device::setActionListner() or Service::setActionListnerer() to add a listener into all control actions in a device or a service. Use Device::setQueryListner() or Service::setQueryListner() to add a listener into all query actions in a device or a service. The following sample sets a listener into all actions in a device.

```java
class ClockDevice : public Device, public ActionListener, public QueryListener {
  public:
    ClockDevice() : Device("/clock/www/description.xml") {
      setActionListner(this);
      setQueryListener (this);
    }
    
    bool actionControlRecieved(Action *action) {
       ....... 
    }

    bool queryControlReceived(StateVariable *stateVar) {
      .......
    }
}
```

### Event

The control point may subscribe some events of the device. You don't need manage the subscription messages from control points because the device manages the subscription messages automatically. For example, the device adds a control point into the subscriber list when the control point sends a subscription message to the device, or the device removes the control point from the subscriber list when the control point sends a unsubscription message.

Use ServiceStateVariable::setValue() when you want to send the state to the subscribers. The event is sent to the subscribers automatically when the state variable is updated using ServiceStateVariable::setValue(). The following example updates a state variable, and the updated state is distributed to the subscribers automatically.

```java
Device clockDevice = ....
StateVariable timeVar = clockDevice.getStateVariable("Time"); String
timeStr = .....
timeVar.setValue(timeStr);
```

## Control Point

### Class Overview

The following static structure diagram is related classes of cybergarage-upnp to create your control point of UPnP™. The control point has some root devices in the UPnP™ network.

![](img/ctrlpoint-class-overview.png)


### Initiating

To create a UPnP™ control point, create a instance of ControlPoint class. Use ControlPoint::start() to active the contol point. The control point multicasts a discovery message searching for all devices to the UPnP™ network automatically when the control point is active.

```java
import org.cybergarage.upnp.*;
import org.cybergarage.upnp.device.*;
......
ControlPoint ctrlPoint = new ControlPoint();
......
ctrlPoint.start();
```

The active control point has some server processes, and returns the responses automatically when other UPnP™ devices send the messages to the control point. For example, the control point has a SSDP server to get M-SEARCH responses, and the control point searches a available port for the SSDP server automatically on the machine when the control point is started. The control point is created with the following default parameters.

| Parameter        | Default   | Setter Method    |
|------------------|-----------|------------------|
| HTTP port        | 39500     | setHTTPPort()    |
| SSDP port        | 39400     | setSSDPPort()    |
| Subscription URI | /eventSub | setEventSubURI() |
| Search Response  | 3         | setSerchMx()     |

### Notify

The control point receives notify events from devices in the UPnP™ network, and the devices are added or removed form the control point automatically. The expired device is removed from the device list of the control point automatically too.You don't manage the notify events, but you can receive the event to implement the NotifyListener interface. The following sample receives the notify messages.

```java
public class MyCtrlPoint extends ControlPoint implements NotifyListener
{
  public MyCtrlPoint() {
    ........
    addNotifyListener(this);
    start();
  }

  public void deviceNotifyReceived(SSDPPacket ssdpPacket) {
    String uuid = ssdpPacket.getUSN();
    String target = ssdpPacket.getNT();
    String subType = ssdpPacket.getNTS();
    String location = ssdpPacket.getLocation();
    .......
  }
}
```

To know only the added and removed device, you may use the following interface, DeviceChangeListener.

```java

public class MyCtrlPoint extends ControlPoint implements DeviceChangeListener {
  public MyCtrlPoint() {
    ........
    addDeviceChangeListener (this); start();
  }

  public void deviceAdded (Device dev) {
    ........
  }

  public void deviceRemoved(Device dev) {
    ........
  }
}
```

### Search

You can update the device lists using ControlPoint::search(). The discovered root devices are added to the control point automatically, and you can receive the response to implement the SearchResponseListener interface. The following sample receives the notify messages.

```java
public class MyCtrlPoint extends ControlPoint implements SearchResponseListener {
  public MyCtrlPoint() {
    ........
    addSearchResponseListener(this); start();
    ........
    search("upnp:rootdevice");
  }

  public void deviceSearchResponseReceived(SSDPPacket ssdpPacket) {
    String uuid = ssdpPacket.getUSN();
    String target = ssdpPacket.getST();
    String location = ssdpPacket.getLocation();
    ........
  }
}
```

### Root Devices

Use ControlPoint:: getDeviceList() that returns only root devices to get the discovered device list. The following example outputs friendly names of the root devices.

```java
ControlPoint ctrlPoint = new ControlPoint();
......
ctrlPoint.start();
......
DeviceList rootDevList = ctrlPoint.getDeviceList();
int nRootDevs =rootDevList.size();
for (int n=0; n<nRootDevs; n++) {
  Device dev = rootDevList.getDevice(n);
  String devName = dev.getFriendlyName();
  System.out.println("\[" + n + "\] = " + devName);
}
```

You can find a root device by the friendly name, the device type, or the UDN using ControlPoint::getDevice(). The following example gets a root device by the friendly name.

```java
ControlPoint ctrlPoint = new ControlPoint();
......
ctrlPoint.start();
......
Device homeServerDev = ctrlPoint.getDevice("xxxx-home-server");
```

### Control

The control point can send action or query control messages to the discovered devices. To send the action control message, use Action::setArgumentValue() and Action::postControlAction (). You should set the action values to the all input arguments, and the output argument values is ignored if you set. The following sample posts a action control request that sets a new time, and output the response result.

```java
Device clockDev = ....

Action setTimeAct = clockDev.getAction("SetTime");
String newTime = .... 
setTimeAct.setArgumentValue("time", newTime);
if (setTimeAct.postControlAction() == true) {
  ArgumentList outArgList = setTimeAct.getOutputArgumentList();
  int nOutArgs = outArgList.size();
  for (int n=0; n<nOutArgs; n++) {
    Argument outArg = outArgList.getArgument(n);
    String name = outArg.getName();
    String value = outArg.getValue();
    ......
    }
  }
} else {
  UPnPStatus err = setTimeAct.getUPnPStatus();
  System.out.println("Error Code = " + err.getCode());
  System.out.println("Error Desc = " + err.getDescription());
}
```

To send the query control message, use StateVariable::postQueryControl(). The following sample posts a query control request, and output the return value.

```java
Device clockDev = ....
StateVariable timeStateVar = clockDev.getStateVariable("time");
if (timeStateVar.postQueryControl() == true) {
  String value =
  timeStateVar.getValue();
  ......
} else {
  UPnPStatus err = timeStateVar.getUPnPStatus();
  System.out.println("Error Code = " + err.getCode());
  System.out.println("Error Desc = " + err.getDescription());
}
```

Use Argument::getRelatedStateVariable() to get the related StatiVariable of the argument, and use StateVariable:: getAllowedValueRange() or getAllowedValueList() to get the the allowed value range or list.

```java
Device clockDev = ....
Action timeAct = clockDev.getAction("SetTime");
Argument timeArg = timeAct.getArgument("time");
StataVariable stateVar = timeArg.getRelatedStateVariable(); 
if (stateVar != null) {
  if (stateVar.hasAllowedValueRange() == true) {
    AllowedValueRange valRange = stateVar.getAllowedValueRange();
    ......
  }
  if (stateVar.hasAllowedValueList() == true) {
    AllowedValueList valList = stateVar.getAllowedValueList ();
    ......
  }
}
```

### Event

The control point can subscribe events of the discovered devices, get the state changes of the services Use ControlPoint::subscribe() and implement the EventListener interface. The listener have to implement a eventNotifyReceived().

```java
public MyControlPoint extends ControlPoint implements EventListener {
  public MyControlPoint() {
    .....
    addEventListener(this);
  }
  .....
  public void eventNotifyReceived(String uuid, long seq, String name, String value) {
    ....
  }
}
```

The ControlPoint::subscribe() returns true when the subscription is accepted from the service, and you can get the subscription id and
timeout.

```java
ControlPoint ctrlPoint = .....
Device clockDev = ctrlPoint.getDevice("xxxx-clock");
Service timeService = clockDev.getService("time:1");
boolean subRet = ctrlPoint.subscribe(timeService);
if (subRet == true) {
  String sid = timeService.getSID();
  long timeout = timeService.getTimeout();
}
```

### IPv6

cybergarage-upnp binds all interfaces in the platform when the devices or control points are created, and the IPv6 sockets are created automatically if the interfaces have IPv6 addresses.

cybergarage-upnp supports IPv4 and IPv6 both as default. If you want to use only IPv6 interfaces, call the following method before the devices or control points are created.

```java
UPnP.setEnable(UPnP. USE_ONLY_IPV6_ADDR);
```

Link local is the default scope for multicasting of cybergarage-upnp.Use UPnP.setEnable() to change the scope. The following example changes the scope to the site local.

```java
UPnP.setEnable(UPnP. USE_IPV6_SITE_LOCAL_SCOPE);
```

## Inside cybergarage-upnp

### Overriding HTTP Service

The Device class of cybergarage-upnp implements a HTTPRequestListner interface of org.cybergarage.http package to handle
some HTTP requests from the control points. The HTTPRequestListener interface is bellow.

```java
public interface HTTPRequestListener {
  public void httpRequestRecieved(HTTPRequest httpReq);
}
```

To overide the interface, import the org.cybergarage.http and override the httpRequestRecieved method in your device that is a sub class of the Device class. The following example is a clock device using cybergarage-upnp, and adds the override method to return the presentation page.


```java
import org.cybergarage.http.*;
.........
public class ClockDevice extends Device implements ActionListener, QueryListener {
  ..........
  private final static String PRESENTATION_URI = "/presentation";
  public void httpRequestRecieved(HTTPRequest httpReq) {
    String uri = httpReq.getURI();
    if (uri.startsWith(PRESENTATION_URI) == false) {
      super.httpRequestRecieved(httpReq);
      return;
    }
    Clock clock = Clock.getInstance();
    String contents = "<HTML><BODY><H1>" + clock.toString() + "</H1></BODY></HTML>";
    HTTPResponse httpRes = new HTTPResponse();
    httpRes.setStatusCode(HTTPStatus.OK);
    httpRes.setContent(contents);
    httpReq.post(httpRes);
  }
}
```