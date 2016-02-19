# Introduction #

First off, this is open to anyone, you don't have to be 'on the programming team' to try.  I don't know windows programming and I need your help.

Here's the challenge:
One thing that programming does often is switch network settings between 'robot' and 'BHS wifi' on laptops.  We do it by clicking on menus/stuff to find tcp/ip settings and make changes by typing in ip info or choosing dhcp.  Wouldn't it be great if we could instead click on a program that would do it automatically?

# Details #

Write a windows program/script that will change network settings.  It has to run on windows 7 and 8 (i think that's what laptops are running).

The program needs to ask the user the following:
  * Do you want static or dhcp network?
  * If static, what is your team number?

After asking those questions the program should do the following:

  * if dhcp, set wifi settings to use dhcp and connect to BHS wifi
  * if static, compute ip address and gateway, then set wifi to static and use ip address, netmask, and gateway

This program doesn't have to be written in Java or C++.. windows has a 'shell' so writing a shell script might be easiest.
Some hints to get you started:
  * https://en.wikibooks.org/wiki/Windows_Batch_Scripting
  * the netsh cmd might help

Any questions.. ask myself or one of the other mentors.

# Solutions #

We'll put ideas and solutions people come up with here.