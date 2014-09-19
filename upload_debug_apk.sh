#!/bin/bash
VERSION=$( grep "versionCode" src/app/build.gradle | awk '{ print $2 }' )
echo "Uploading app with versionCode: $VERSION"
echo

echo "Existing .apk files will be deleted. Continue with pressing the enter key."
read

rm -rf src/app/build/outputs/apk

echo "Old .apk files deleted. Regenerate .apk file and press enter to upload new file"
read

scp src/app/build/outputs/apk/app-debug.apk lisa:/var/www/bitsworking.at/htdocs/msf/app.apk
ssh lisa "echo $VERSION > /var/www/bitsworking.at/htdocs/msf/app.version"

echo "All done."
echo "- http://www.bitsworking.at/msf/app.version"
echo "- http://www.bitsworking.at/msf/app.apk"
