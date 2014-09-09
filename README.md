Video Showcase App
==================

Simple app to showcase videos.


Author
------

Chris Hager <chris@bitsworking.com>


License
-------

GNU General Public License v2
(see `LICENSE` file)


Video Files
-----------

* h264 encoded, .mp4 files
* The video files have to be put into the directory `SDCARD/VIDEOSHOWCASE/`
* The videos may have thumbnail images
* Filenames:
  * Video: `video#_beschreibungs-text.mp4` (eg. `video1_kurzfilm-von-anna-lechner.mp4`)
  * Thumbnail: `thumbnail_video#.png` (eg. `thumbnail_video1.png`)


Restricted Internet Access
--------------------------

There are several options to restrict internet access on the device:

* MDM Solution (eg. [AirWatch](http://www.air-watch.com) or [others](http://www.zdnet.com/blog/consumerization/10-byod-mobile-device-management-suites-you-need-to-know/422))
* NoRoot Firewall (blocks Internet Access for Apps via VPN, no password lock)
* Custom VPN
* ...


Keep App Running and in Foreground (Kiosk Mode)
-----------------------------------------------

It's important that the showcase app is always running and on top (called 'Kiosk mode'). 
This can be achieved in two ways:

* External App: [Autostart and StaY!](https://play.google.com/store/apps/details?id=com.atasoglou.autostartandstay)
* Via Code: 
  * http://stackoverflow.com/a/24251210 (App persistance)
  * http://stackoverflow.com/a/19313091 (Be a home screen)

Hide System UI

* https://developer.android.com/training/system-ui/immersive.html


Quitting the App
----------------

An easy way to exit is by having a region in the app which reacts to a certain number of clicks.
In this video showcase app we use the topmost logo as click receiver. After about 10 clicks with
less than 1s delay between, an exit dialog with a password input field is displayed.
