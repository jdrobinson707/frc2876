# IP Addresses #

At competitions the robot network has standard ip addresses for everything. By standard I mean FIRST makes everyone use the same thing.  So even if we are not at a competition we should use the same ip addrs to make managing robot network easier for us.

The robot network uses static ip addresses. No dhcp server is running on the wireless bridge.

Replace x.y with your team number. So for our team it would be 28.76

At competitions ip addrs are assigned as follows:
  * 10.x.y.1 Wireless Bridge on Robot
  * 10.x.y.2 CRIO
  * 10.x.y.3 Reserved
  * 10.x.y.4 Wireless Router
  * 10.x.y.5 Driver Station/Classmate (wired)
  * 10.x.y.6 Development System
  * 10.x.y.9 Driver Station/programming laptop (wireless)
  * 10.x.y.11 Camera

Any other address on the 10.x.y.z network can be used for laptops. For example:
  * 10.x.y.30
Just be sure that no two devices(computers, routers, etc) have the same ip address assigned to them else we will have problems.

# Wireless vs Wired #
You can connect a laptop directly to the wireless bridge using an ethernet cable if wireless isn't working.  Be sure to set the 'wired internet connection' on your laptop to use an ip address different than the wireless one.

You can connect a laptop directly to the CRIO but you have to use a cross-over ethernet cable.  Usually you only need to do this if imaging the crio with new firmware.